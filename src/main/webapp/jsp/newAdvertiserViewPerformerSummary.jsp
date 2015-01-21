	<p>
	<!-- upper menu -->
	

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
   <!-- end upper menu -->
   <br/>
   <br/>
   
   <!-- Line chart starts -->
    <div id="lineChartDiv" ng-app="lineChartApp" ng-controller="lineChartCtrl" class="row-fluid" >	    

		 <article class="span12">	 
	     
			<!-- new widget -->
			<div title="Performance by Site" class="jarviswidget" >
				<header>
					<h2>Performance by Site</h2>
				</header>
				<!-- wrap div -->
				<div>
					<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
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
					    <div id="performanceSiteLoaderId" style="text-align:center;">
					     <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				
						 <div id="linechartdataSelectBox" style="margin-left:10px;margin-top:5px;font-family:Arial,Helvetica,sans-serif;">
					          <label class="control-label" for="chartType">Chart Types</label>
					          <div class="controls">
					             <select id="chartType" ng-model="displaydatatype" ng-change="chartSelectionChange()">
					                                   <option value="CTR">CTR</option>
					                                   <option value="Impressions">Impressions</option>
					                                   <option value="Clicks">Clicks</option>
					             </select> 
					           </div>
					      </div>
				      
						<div id="linechartdatadiv" google-chart chart="chart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
						<div id="emptyChartdiv" style="height: 200px; display:none">
							<div style="text-align: center; margin-top: 100px;">No Data</div>
						</div>
					</div>
					
					<!-- end content-->
				</div>
				<!-- end wrap div -->
			</div>
			<!-- end widget -->
		</article>
    </div>
                    
   
   
   <!-- Line chart ends -->
   
    <!-- start of geo location chart --> 
  <div id="geoChartDiv" ng-app="geoChartModule" ng-controller="geoChartCtrl" class="row-fluid" >	    
   <article class="span12">
			<!-- new widget -->
		<div title="Performance By Location" class="jarviswidget">
			<header><h2>Performance By Location</h2>
			  <!-- <div id="by_location_zoom" style="margin-left: 125px; margin-top: 7px;"><i class="icon-zoom-in"></i></div> -->
			</header>
			<!-- widget div-->
			<div>
			<!-- widget edit box -->
			  <div class="jarviswidget-editbox">
				<div><label>Title:</label> <input type="text" /></div>
				<div><label>Styles:</label> <span data-widget-setstyle="purple" class="purple-btn"></span>
					<span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
					<span data-widget-setstyle="green" class="green-btn"></span>
					<span data-widget-setstyle="yellow"	class="yellow-btn"></span>
					<span data-widget-setstyle="orange" class="orange-btn"></span>
					<span data-widget-setstyle="pink" class="pink-btn"></span>
					<span data-widget-setstyle="red" class="red-btn"></span>
					<span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
					<span data-widget-setstyle="black" class="black-btn"></span>
			  </div>
		     </div><!-- end widget edit box -->
             <div id="performanceLocationLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
		 <div class="inner-spacer">
		    <!-- <div id="geomap" ></div> -->
		   <div id="geoChartId" google-chart chart="geoChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>

  	      </div>
  	     
	     </div>
		<!-- end widget div -->
		</div>
		<!-- end widget -->
	</article>
	</div>
	
  
	<!-- end of geo location chart -->	
														
<div id="pieChartDiv" ng-app="pieChartApp" ng-controller="pieChartCtrl" class="row-fluid"  >
		<article class="span4 sortable-grid ui-sortable">
		<!-- new widget -->
		<div id="pieChartDiv1" 
		     class="jarviswidget" >
			<header>
				<h2>Impressions by OS</h2>
			</header>
			<!-- wrap div -->
			<div>		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
				<div id="performanceOSLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
					   
				<div class="inner-spacer">				
					<!-- content goes here -->
					<div google-chart chart="pieChart1" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>
	
		<article class="span4 sortable-grid ui-sortable">
		<!-- new widget -->
		<div id="pieChartDiv2"   
		     class="jarviswidget" >
			<header>
				<h2>Impressions by Device</h2>
			</header>
			<!-- wrap div -->
			<div>
		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
		        <div id="performanceDeviceLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				<div class="inner-spacer">				
					<!-- content goes here -->
					<div google-chart chart="pieChart2" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>	
		
		<article class="span4 sortable-grid ui-sortable">
		<!-- new widget -->
		<div id="pieChartDiv3"  
		     class="jarviswidget" >
			<header>
				<h2>Impressions By Placement</h2>
			</header>
			<!-- wrap div -->
			<div>		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
				<div id="mobileWebVsAppLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				<div class="inner-spacer">
					<!-- content goes here -->
					<div google-chart chart="pieChart3" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>
		</div>
		
		<div id="barChartDiv" ng-app="barChartApp" ng-controller="barChartCtrl" class="row-fluid"  >
		<div class="row-fluid">
		<article class="span4 sortable-grid ui-sortable">
		<!-- new widget -->
		<div  id="barChartByOSDiv" 
		     class="jarviswidget" >
			<header>
				<h2>Performance by OS</h2>
			</header>
			<!-- wrap div -->
			<div>
		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
		        <div id="barChartByOSLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				<div class="inner-spacer">					
					<div  google-chart chart="barChartByOS" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div> 
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>
		
			<article class="span4 sortable-grid ui-sortable">
		<!-- new widget -->
		<div  id="barChartByDeviceDiv" 
		     class="jarviswidget" >
			<header>
				<h2>Performance by Device</h2>
			</header>
			<!-- wrap div -->
			<div>
		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
		        <div id="barChartByDeviceLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				<div class="inner-spacer">					
					<div  google-chart chart="barChartByDevice" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div> 
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>
		
			<article class="span4 sortable-grid ui-sortable">
		<!-- new widget -->
		<div  id="barChartMobileDiv" 
		     class="jarviswidget" >
			<header>
				<h2>Performance By Placement</h2>
			</header>
			<!-- wrap div -->
			<div>
		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
		        <div id="barChartByMobileLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				<div class="inner-spacer">					
					<div  google-chart chart="barChartByMobile" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div> 
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>
		
		</div>
   </div>
	 
	 	<div class="row-fluid">
		<article class="span6 sortable-grid ui-sortable">
		<!-- new widget -->
		<div  id="barChartPerfAdSizeDiv" ng-app="barChartPerfAdSizeApp" ng-controller="barChartCtrl" class="jarviswidget" >
			<header>
				<h2>Performance by Ad Size</h2>
			</header>
			<!-- wrap div -->
			<div>
		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
		        <div id="performanceAdSizeLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				<div class="inner-spacer">					
					<div  google-chart chart="barChart1" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div> 
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>
		      
	   <article id="impressionByAdSize" class="span6 sortable-grid ui-sortable">
		<!-- new widget -->
		<div id="pieChartImpByAdSizeDiv" ng-app="pieChartImpByAdSizeApp" ng-controller="pieChartCtrl" class="jarviswidget">
			<header>
				<h2>Impressions by Ad Size</h2>
			</header>
			<!-- wrap div -->
			<div>		
				<div class="jarviswidget-editbox">
					<div>
						<label>Title:</label> <input type="text" />
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
				<div id="impByAdSizeLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
				<div class="inner-spacer">
					<!-- content goes here -->
					<div google-chart chart="pieChart4" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
				</div>
				
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</div>
		<!-- end widget -->
		</article>
	    </div>
	 
      <div class="fluid-container" id="tableDataDiv" ng-app="tableDataApp" ng-controller="tableDataCtrl" >
    <!-- widget grid -->
    <section id="widget-grid" class="">
	<!-- row-fluid -->
	 <div class="row-fluid" style="margin-top: 10px;">
		<!-- article -->
		<article class="span6">
			<!-- new widget -->
			<div title="Top sites with the highest absolute CTR% values" class="jarviswidget" id="topPerformer" >
				<header><h2>PERFORMER</h2></header>
				
				<!-- wrap div -->
				<div>
					<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
						<div>`
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
		
					<div id="topPerformLineItemDiv" class="inner-spacer" >
					
						<!-- content goes here -->
						<table id="topPerformLineItemTable" class="table text-left">
							<thead>
								<tr>
									<th>SITES</th>		
									<th style="text-align: left;">IMPRESSIONS DELIVERED</th>
									<th style="text-align: left;">CLICKS</th>
									<th style="text-align: left;">CTR(%)</th>		
								</tr>
							</thead>
							<tbody ng-repeat="data in topPerformerData" >
								<!-- <tr class="odd gradeX">
									<td colspan="5" style="color: red; text-align: center;">
									   <img src="img/loaders/type4/light/46.gif" alt="loader">
									</td>
								</tr> -->
								
								<tr ng-repeat="rows in data  | orderBy:'c6CTR':true">
								  <td ng-repeat="row in rows" >
								    {{ row }}
								  </td>
								</tr>
								
							</tbody>
						</table>
						<div id="performerLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
					</div>
					<!-- end content-->
				</div>
				<!-- end wrap div -->
			</div>
			<!-- end widget -->
		</article>
		<!-- end article -->
		
		<article class="span6">
																<!-- new widget -->
		<div title="Top under-delivering sites with highest delivery variance"
			class="jarviswidget" id="topnonperformers">
		 <header>	<h2>NON PERFORMER</h2></header>
																	<!-- wrap div -->
				<div>
					<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
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
		
					<div id="topNonPerformLineItemDiv" class="inner-spacer">					
						<!-- content goes here -->
						<table id='topNonPerformLineItemTable' class="table text-left">
							<thead>
								<tr>	
									<th>SITES</th>		
									<th style="text-align: left;">IMPRESSIONS DELIVERED</th>
									<th style="text-align: left;">CLICKS</th>
									<th style="text-align: left;">CTR(%)</th>		
								</tr>
							</thead>
							<tbody ng-repeat="data in topNonPerformerData">
								<!-- <tr class="odd gradeX">
									<td colspan="5" style="color: red; text-align: center;">
									   <img src="img/loaders/type4/light/46.gif" alt="loader">
									</td>
								</tr> -->
								<tr ng-repeat="items in data | orderBy:'c6CTR':false">								 
								  <td ng-repeat="item in items" >
								    {{ item }}
								  </td>
								</tr>
								
							</tbody>
						</table>
						<div id="nonPerformerLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
					</div>
					<!-- end content-->
				</div>
			</div>
			<!-- end widget -->
		</article>
															
      </div>
	  <!-- end row-fluid -->
	  <!-- row-fluid -->
	  

	<!-- 	<div class="row-fluid">
         article
		  <article class="span6">
		  new widget
          <div title="Top sites with the highest CTR gain from previous period"
               class="jarviswidget" id="topperformers">
               <header><h2>TOP GAINERS</h2></header>
           wrap div
			<div>
					<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
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
		
					<div id="topGainersDiv" class="inner-spacer">
						<div class="widget alert alert-warning adjusted">
							<button class="close" data-dismiss="alert">×</button>
							<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
							Top sites with the highest CTR gain from previous period. 
						</div>
						content goes here
						<table class="table text-left">
							<thead>
								<tr>
									<th>SITES</th>		
									<th style="text-align: left;">IMPRESSION DELIVERED</th>
									<th style="text-align: left;">CLICKS</th>
									<th style="text-align: left;">CTR(%)</th>		
								</tr>
							</thead>
							<tbody ng-repeat="data in topGainersData">
								<tr class="odd gradeX">
									<td colspan="5" style="color: red; text-align: center;">
									   <img src="img/loaders/type4/light/46.gif" alt="loader">
									</td>
								</tr>
								<tr ng-repeat="items in data">								 
								  <td ng-repeat="item in items" >
								    {{ item }}
								  </td>
								</tr>
								
							</tbody>
						</table>
					</div>
					end content
				</div>
				end wrap div
				</div>
		end widget
		</article>
		end article
		
		article
		<article class="span6">
		new widget
		<div title="Top sites with the highest CTR loss from previous period"
			class="jarviswidget" id="toplosers">
			<header><h2>TOP LOSERS</h2></header>
		   wrap div
			<div>
					<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
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
		
					<div id="topLoserDiv" class="inner-spacer">
						<div class="widget alert alert-warning adjusted">
							<button class="close" data-dismiss="alert">×</button>
							<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
							Top sites with the highest CTR loss from previous period. 
						</div>
						content goes here
						<table class="table text-left">
							<thead>
								<tr>	
									<th>SITES</th>		
									<th style="text-align: left;">IMPRESSION DELIVERED</th>
									<th style="text-align: left;">CLICKS</th>
									<th style="text-align: left;">CTR(%)</th>		
								</tr>
							</thead>
							<tbody ng-repeat="data in topLoserData">
								<tr class="odd gradeX">
									<td colspan="5" style="color: red; text-align: center;">
									   <img src="img/loaders/type4/light/46.gif" alt="loader">
									</td>
								</tr>
								<tr ng-repeat="items in data">								 								 
								  <td ng-repeat="item in items" >
								    {{ item }}
								  </td>
								</tr>
								
							</tbody>
						</table>
					</div>
					end content
				</div>
	            end wrap div
		</div>
		end widget
		</article>
	    end article				
	
	</div> -->
	
	<!-- row-fluid --> 

   <div class="row-fluid">
     <!-- article -->
	 <article class="span12">
		<!-- new widget -->
		<div title="Top over-pacing campaign line items with highest delivery variance."
			class="jarviswidget" id="mostactive">
			<header><h2>MOST ACTIVE</h2></header>
		 <!-- wrap div -->
         <div>
					<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
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
		
					<div id="topPerformLineItemDiv" class="inner-spacer">						
						<%!int count=10; %>
						<table id="topPerformLineItemTable" class="table text-left">
							<thead>
								<tr>		
									
									<th>CAMPAIGN LINE ITEMS</th>
									<th style="text-align: left;">BOOKED IMPRESSIONS</th>		
									<th style="text-align: left;">IMPRESSIONS DELIVERED</th>
									<th style="text-align: left;">PACING</th>
									<th style="text-align: left;"> </th>
									
								</tr>
							</thead>
							<tbody ng-repeat="data in mostActiveLineItemData">
							
								<!-- <tr class="odd gradeX">
									<td colspan="5" style="color: red; text-align: center;">
									   <img src="img/loaders/type4/light/46.gif" alt="loader">
									</td>
								</tr> -->
								<tr ng-repeat="rows in data" ng-repeat="i in data.length ">
								
								  <td ng-repeat="row in rows" >
								 <!--  <div ng-bind-html-unsafe="item"></div>
								    -->
								    {{row}}
								  </td>
								  <td id="paceId{{$index}}" style="width: 85px; cursor: pointer;"  >
								  
								    <progress percent="objectArr[$index]" class="progress-striped active"  >
								    </progress>
								    
								  </td> 
								</tr>
								
							</tbody>
						</table>
						<div id="mostActiveLoaderId" style="text-align:center;">
					      <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
					</div>
					<!-- end content-->
				</div>
				<!-- end wrap div -->
			</div>
	      <!-- end widget -->
		</article>
	   <!-- end article -->

	  							
     
	  </div>
	<!-- row-fluid -->
	

 
  <div class="row-fluid">
	<article class="span12"> <!-- new widget -->
	<div class="jarviswidget">
		<header>
		<h2>Delivery Metrics</h2>
		</header>
		<!-- wrap div -->
		<div>
		 <div class="jarviswidget-editbox">
			<div><label>Title:</label> <input type="text" /></div>
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
				<table id="deliveryMetricTable" class="table dtable" >
					<thead>
						<tr>
						    <th></th>
							<th style="text-align: left;">CAMPAIGN LINE ITEMS</th>		
							<th style="text-align: left;">BOOKED IMPRESSIONS</th>
							<th style="text-align: left;">DELIVERED IMPRESSIONS</th>
							<th style="text-align: left;">PACING</th>
							<th style="text-align: left;">CLICKS</th>
							<th style="text-align: left;">CTR(%)</th>		
							<th style="text-align: left;">BUDGET</th>
							<th style="text-align: left;">SPENT</th>
							<th style="text-align: left;">BALANCE</th>
							<th style="text-align: left;">START DATE</th>
							<th style="text-align: left;">END DATE</th>							
						</tr>
					</thead>
					<tbody></tbody>
					<!-- <tbody ng-repeat="data in performanceByPlacementData">								
								<tr  ng-mouseover="showCreative()" ng-repeat="items in data" >
								<td ng-click="showTraffer()"   href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>
								 
								  <td   ng-repeat="item in items" >
								    {{ item }}
								  </td>
								</tr>
								
				  </tbody> -->
				</table>
				
					<div id="performanceMetricsLoaderId" style="text-align:center;">
					   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> 
			</div>
			
			<!-- end content-->
		</div>
		<!-- end wrap div -->
	</div>
	<!-- end widget --> 
	</article>
</div>
						
												
	<!-- end row-fluid -->
   </section>
  </div>