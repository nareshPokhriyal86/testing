<div class="tab-pane" id="s7">
<!-- .height-wrapper -->
<!-- <div class="height-wrapper"> -->
	
	<!-- 		<div id="page-content" style="margin:20px !important;min-height:1350px;"> -->
				<!-- breadcrumb -->
					
				<!-- end breadcrumb -->

				<!-- tabs view -->
				<!-- <div class="row-fluid"> -->
					<!-- <article class="span12"> -->
						<!-- new widget -->
						<!-- <div class="jarviswidget" id="widget-id-2"> -->
							<!-- widget div-->
							<!-- <div> -->
								<!-- widget edit box -->
								
			<div  id="printViewIcon_trafficking" style="float:right; margin-right: 2%; display:none">
					<a title="Print View" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getTraffickingSummaryPrintView('true');"><i class="cus-printer"></i></a>
			</div>					
								<div class="jarviswidget-editbox">
									<div>
										<label>Title:</label>
										<input type="text" />
									</div>
											
									<div>
										<label>Styles:</label>
										<span data-widget-setstyle="purple" class="purple-btn"></span>
										<span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
										<span data-widget-setstyle="green" class="green-btn"></span>
										<span data-widget-setstyle="yellow" class="yellow-btn"></span>
										<span data-widget-setstyle="orange" class="orange-btn"></span>
										<span data-widget-setstyle="pink" class="pink-btn"></span>
										<span data-widget-setstyle="red" class="red-btn"></span>
										<span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
										<span data-widget-setstyle="black" class="black-btn"></span>
									</div>
								</div>
								<!-- end widget edit box -->

								<div class="inner-spacer widget-content-padding" style="min-height:1300px"> 
									<!-- content -->
								<div id="pop-up" style="width:20px;height:10px;border:0px solid black;margin-left:40%;"></div>
									<div id="myTabContent" class="tab-content">
										<div class="tab-pane fade in active">

												<div class="fluid-container">

													<!-- widget grid -->
													<section id="widget-grid" class="">

														<div class="row-fluid">

															<!-- article -->
										                 <article class="span6 sortable-grid ui-sortable" style="margin-right: 2%;">
																<!-- new widget -->
													
									
									
									
																<div class="jarviswidget" id="widget-id-5">
																	<header>
																		<h2>Campaign Trafficking Status   
																		<div id="campaignTraffickingLoader" style="display:none;margin-left: 5px;">
																			<img src="img/loaders/type4/light/46.gif" alt="loader">
																		</div>
																		</h2>                
																	</header>
																	<div>
																	
																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label>
																				<input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label>
																				<span data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
																				<span data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow" class="yellow-btn"></span>
																				<span data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
																				<span data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>
										
																		<div class="inner-spacer"> 
																		
																			<div class="progressbars">
																			
																			
																			<div class="widget-content-padding">
																			<strong>Total </strong><strong class="pull-right" id="totalLineItems">0</strong><br><br>
																			
																			<strong>Delivering </strong><strong class="pull-right" id="deliveryCount">0</strong>
																			<div class="progress progress-info" id="delivery" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);" >
																				<div class="bar" style="width:0%" id="deliveryPct"></div>
																			</div>
																			
																			<strong>Ready </strong><strong class="pull-right" id="readyCount">0</strong>
																			<div class="progress progress-success" id="ready" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);">
																				<div class="bar" style="width:0%" id="readyPct"></div>
																			</div>
																			
																			<strong>Paused <strong><strong class="pull-right" id="pausedCount">0</strong>
																			<div class="progress progress-warning" id="paused" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);">
																				<div class="bar" style="width:0%" id="pausedPct"></div>
																			</div>
																			
																			<strong>Needs Creatives  </strong><strong class="pull-right" id="needCreativesCount">0</strong>
																			<div class="progress progress-danger" id="needsCreatives" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);">
																				<div class="bar" style="width:0%"id="needCreativesPct"></div>
																			</div>
																			
																			<strong>Inventory Released </strong><strong class="pull-right" id="releasedCount">0</strong>
																			<div class="progress progress-info" id="inventoryReleased" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);">
																				<div class="bar filled-text" style="width:0%" id="releasedPct">
																					
																				</div>
																			</div>
																			<strong>Pending Approval </strong><strong class="pull-right" id="pendingApprovalCount">0</strong>
																			<div class="progress progress-success" id="pendingApproval" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);">
																				<div class="bar filled-text" style="width:0%" id="pendingApprovalPct">
																					
																				</div>
																			</div>
																			
																			<!-- <strong>Completed </strong><strong class="pull-right" id="completedCount">0</strong>
																			<div class="progress progress-warning" id="completed" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);">
																				<div class="bar filled-text" style="width:0%" id="completedPct">
																					
																				</div>
																			</div> -->
																			
																			<strong>Draft </strong><strong class="pull-right" id="draftCount">0</strong>
																			<div class="progress progress-info" id="draft" style="cursor: hand; cursor: pointer;" onclick="loadSelectedLineItems(id);">
																				<div class="bar filled-text" style="width:0%" id="draftPct">
																					
																				</div>
																			</div>
																			
																		</div>
																		</div>
																		</div>
																	</div>
																</div> 
																<!-- end widget -->
															</article>
															<!-- end article -->

															<article class="span6 sortable-grid ui-sortable">
																<!-- new widget -->
																<div class="jarviswidget" id="widget-id-1">
																		<header>
																			<h2>
																			Campaign Schedule
																			<div id="traffickingCalendarLoader" style="display:none;margin-left: 5px;">
																			<img src="img/loaders/type4/light/46.gif" alt="loader">
																			</div>
																			</h2>                           
																		</header>
																		
																		<!-- widget div-->
																		<div>
																			<!-- widget edit box -->
																			<div class="jarviswidget-editbox">
																				<div>
																					<label>Title:</label>
																					<input type="text" />
																				</div>
																				<div>
																					<label>Styles:</label>
																					<span data-widget-setstyle="purple" class="purple-btn"></span>
																					<span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
																					<span data-widget-setstyle="green" class="green-btn"></span>
																					<span data-widget-setstyle="yellow" class="yellow-btn"></span>
																					<span data-widget-setstyle="orange" class="orange-btn"></span>
																					<span data-widget-setstyle="pink" class="pink-btn"></span>
																					<span data-widget-setstyle="red" class="red-btn"></span>
																					<span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
																					<span data-widget-setstyle="black" class="black-btn"></span>
																				</div>
																			</div>
																			<!-- end widget edit box -->
											
																			<div class="inner-spacer"> 
																			<!-- content goes here -->
																				<!--<div class="widget alert alert-info adjusted">
																					<button class="close" data-dismiss="alert">×</button>
																					<i class="cus-exclamation"></i>
																					<strong>Helpful Hint:</strong> Did you know you can click on the <strong>Calendar Cell</strong> to add a new event? Try to add an event and see how easy it is!
																				</div>-->
																				<!-- calnedar container -->
																				<div id="calendar-container">
																					<div id="calendar-buttons">
																						<div class="btn-group">
																							<a href="javascript:void(0)" class="btn btn-small" id="btn-today">Today</a>
																							<a href="javascript:void(0)" class="btn dropdown-toggle btn-small" data-toggle="dropdown"><span class="caret"></span></a>
																							<ul class="dropdown-menu btn-small pull-right">
																								<li>
																									<a href="javascript:void(0);" id="btn-month">Month</a>
																								</li>
																								<li>
																									<a href="javascript:void(0);" id="btn-agenda">Agenda</a>
																								</li>
																								<li>
																									<a href="javascript:void(0);" id="btn-day">Today</a>
																								</li>
																							</ul>
																						</div>
																						<div class="btn-group">
																							<a href="javascript:void(0)" class="btn btn-small" id="btn-prev"><i class="icon-chevron-left"></i></a>
																							<a href="javascript:void(0)" class="btn btn-small" id="btn-next"><i class="icon-chevron-right"></i></a>
																						</div>
																					</div>
																					<div class="dt-header calender-spacer"></div>
																					<div id="calendar"></div>
																				</div>
																				<!-- end calendar container -->
																			</div>
																			
																		</div>
																		<!-- end widget div -->
																	</div>
																<!-- end widget -->
															</article>
									
									
									<article class="span12" id="campaign_selected" style="margin-left: 0;display:none;">
									<!-- new widget -->
									<div class="jarviswidget" id="widget-id-0 " style="">
									<button id="close_button" onClick="javascript:$('#campaign_selected_printView').css('display', 'none');"  type="button" class="close2" style="opacity: 0.84; margin-top: -18px">&times;</button>
									    <header id="tableTitle">
									        <h2>List of Campaign Delivering</h2>                           
									    </header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
									            </div>
									            <div>
									                <label>Styles:</label>
									                <span data-widget-setstyle="purple" class="purple-btn"></span>
									                <span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
									                <span data-widget-setstyle="green" class="green-btn"></span>
									                <span data-widget-setstyle="yellow" class="yellow-btn"></span>
									                <span data-widget-setstyle="orange" class="orange-btn"></span>
									                <span data-widget-setstyle="pink" class="pink-btn"></span>
									                <span data-widget-setstyle="red" class="red-btn"></span>
									                <span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
									                <span data-widget-setstyle="black" class="black-btn"></span>
									            </div>
									        </div>
            
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
												
												<!-- <table class="table table-striped table-bordered responsive" id="dtable"> -->
												<table class="table dtable" id="selectedLineItemsTable">
													<thead>
														<tr>
															<th>Campaign Name</th>
															<th>Goal Quality</th>
															<th>CPM</th>
															<th>Start Date</th>
															<th>End Date</th>
														</tr>
													</thead>
													<tbody>
														
													</tbody>
												</table>
										        <div id="selectedLineItemsLoaderId" style="text-align:center;">
												<img src="img/loaders/type4/light/46.gif" alt="loader"></div>
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
								</br>
												
												<article class="span12" style="margin-left: 0;min-height:270px;margin-top:15px;">
									<!-- new widget -->
									<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget" id="widget-id-1">
									    <header>
									        <h2>TOTAL CAMPAIGNS</h2>                           
									    </header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
									            </div>
									            <div>
									                <label>Styles:</label>
									                <span data-widget-setstyle="purple" class="purple-btn"></span>
									                <span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
									                <span data-widget-setstyle="green" class="green-btn"></span>
									                <span data-widget-setstyle="yellow" class="yellow-btn"></span>
									                <span data-widget-setstyle="orange" class="orange-btn"></span>
									                <span data-widget-setstyle="pink" class="pink-btn"></span>
									                <span data-widget-setstyle="red" class="red-btn"></span>
									                <span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
									                <span data-widget-setstyle="black" class="black-btn"></span>
									            </div>
									        </div>
            
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
									        
												<!-- <table class="table table-striped table-bordered responsive" id="col-filter"> -->
												<table class="table dtable" id="col-filter">
													<thead>
														<tr>
															<th>Campaign Name</th>
															<th>Goal Quality</th>
															<th>CPM</th>
															<th>Start Date</th>
															<th>End Date</th>
														</tr>
													</thead>
													<tbody>
														
													</tbody>
												</table>
												<!-- <div id="traffickingAllDataLoaderId" style="text-align:center;">
												<img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
											</div>
										</div>
									</div>
								</article>
												
												
														
														</div>
														
														<!-- row-fluid -->

														
														
													</section>
												</div>
											</p>
										</div>
										<div  id="popoverData" class="" href="#" data-html="" rel="popover" data-placement="center" data-original-title="" >	</div>
	<div id="popover_content_wrapper" style="display: none;width:480px;">
	
	 
	</div>
										
									</div>
										
									
						
									</div>
									<!-- end content -->	
								</div>
			
							<!-- </div> -->
							<!-- end widget div -->
						<!-- </div> -->
						<!-- end widget -->
					<!-- </article> -->
				<!-- </div> -->
				<!-- end: tabs view -->
				
		<!-- 	</div> -->
			<!-- end main content -->

<!-- </div> -->
<!-- end .height wrapper -->

<!-- </div> -->

<script type="text/javascript">

$(document).ready(function(){
$(".close2").click(function(){
$("#campaign_selected").hide();
});
});

$(document).ready(function(){



$("#custom").click(function(){
$("#custom_date_trafficking").show();
$("#custom_enddate_trafficking").show();
$("#display_name_time").html("Custom");
});
});

</script> 
	

