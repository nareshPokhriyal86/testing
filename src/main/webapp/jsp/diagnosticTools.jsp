
<div class="tab-pane" id="s5">

		
			<div class="diagnostic_tools" >
					<div class="diagnostic_tools_outer" >
						<div class="diagnostic_tools_publisher" >PUBLISHER :</div>
						<div id="diagnosticToolsPublisher" class="diagnostic_tools_publisher_text" ></div>
						<div id="diagnosticToolsHeader" class="channel_outer">CHANNELS :</div>
					
					</div>
					
					<div style="float:right; margin-right: 1%">
						<a title="Print View" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getDiagnosticToolsPrintView('true');"><i class="cus-printer"></i></a>
					</div>
					
					<div id="diagnosticToolsHeaderLoader" style="display:block;margin-left:45%;margin-top:20px;">
						<img src="img/loaders/type6/light/80.gif" alt="loader">
					</div>
					<br>
					
		</div>
			

		<div style="height:15px;clear:both;"></div>
		<!-- div for left table -->
		
		
		<!-- row-fluid -->

		<div class="row-fluid">
			<article class="span12">
				<!-- new widget -->
				<div class="jarviswidget">
					<header>
						<h2><div style="float:left; font-size-adjust: 0.58;">RECONCILIATION SUMMARY</div>
						<!-- <div id="preloader1" style="float:left;margin-left: 150px;margin-top:5px;width:100%;position:absolute; text-align:center;"> -->
						
						</h2>
					</header>
						<!-- wrap div -->
							<div>
								<div class="jarviswidget-editbox">
									<div>
										<label>Title:</label> <input type="text" />
									</div>
									<div>
										<label>Styles:</label> <span data-widget-setstyle="purple"
											class="purple-btn"></span> <span
											data-widget-setstyle="navyblue" class="navyblue-btn"></span>
										<span data-widget-setstyle="green" class="green-btn"></span>
										<span data-widget-setstyle="yellow" class="yellow-btn"></span>
										<span data-widget-setstyle="orange" class="orange-btn"></span>
										<span data-widget-setstyle="pink" class="pink-btn"></span> <span
											data-widget-setstyle="red" class="red-btn"></span> <span
											data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
										<span data-widget-setstyle="black" class="black-btn"></span>
									</div>
								</div>

								<div class="inner-spacer">
							<!-- content goes here -->
							<table class="table table-striped table-bordered responsive" id="reconciliationSummaryTable">
								<thead>
									<tr>
										<th class="reconcilation_table_style"  colspan="1">Demand Partners</th>
										<th class="reconcilation_table_style" colspan="3">DFP Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Demand Partner Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Variance</th>
									</tr>
									<tr>
										<th class="reconcilation_table_style"></th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
									</tr>
								</thead>
								<tbody>
									<tr class="odd gradeX">
										<td colspan="10" style="color:red; text-align:center;"><img src="img/loaders/type4/light/46.gif" alt="loader"></td>
									</tr>
								</tbody>
							</table>
							<!-- <div id="reconciliationSummaryLoader" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
						</div>
								<!-- end content-->
						</div>
						<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
		</div>
		
		<div class="control-group" style="padding:0px;">
			 <div class="controls">
              <select class="span2 with-search" id="selectChannel" onchange="changeChannelDropDown();" style="font-size:16px;">
              
              </select>
             </div>
        </div>

        <!-- row-fluid -->
		<div class="row-fluid" style="border-top:1px solid #ccc;">
			<article class="span12">
				<!-- new widget -->
				<div class="jarviswidget">
				  	<header id="reconciliationDetailsHeader">
						<h2>RECONCILIATION DETAILS</h2>
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
							<table class="table dtable" id="reconciliationDetailTable">
								<thead>
									<tr>
										<th class="reconcilation_table_style" colspan="1">Date</th>
										<th class="reconcilation_table_style" colspan="3">DFP Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Demand Partner Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Variance</th>
									</tr>
									<tr>
										<th class="reconcilation_table_style"></th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
									</tr>
								</thead>
								<tbody>
							
								</tbody>
							</table>
					        <div id="reconciliationDetailLoader" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>	
					    </div>
					    <!-- end content-->
				    </div>
				    <!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			</div>
		<!--end-->
</div>


<!-- <script type="text/javascript">

$(document).ready(function(){
	getTraffickingTabData();
});

</script>  -->
