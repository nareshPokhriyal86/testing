<%@taglib uri="/struts-tags" prefix="s"%>

  <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  
 <style>
  @CHARSET "UTF-8";
    #left_nav {
        width:250px;
    }
    #body {
        width:90%;
        float:left;
        margin:10px;
    }
    .accordion {
        margin: 0;
        padding:10px;
        height:20px;
        border-top:#f0f0f0 1px solid;
        background: #cccccc;
        font-family: Arial, Helvetica, sans-serif;
        text-decoration:none;
        text-transform:uppercase;
        color: #000;
        font-size:1em;
    }
    .accordion-open {
        background:#000;
        color: #fff;
    }
    .accordion-open span {
        display:block;
        float:right;
        padding:10px;
    }
    .accordion-open span {
        background:url(../images/minus.png) center center no-repeat;
    }
    .accordion-close span {
        display:block;
        float:right;
        background:url(../images/plus.png) center center no-repeat;
        padding:10px;
    }
    div.container {
        padding:0;
        margin:0;
    }
    div.content {
        background:#f0f0f0;
        margin: 0;
        padding:10px;
        font-size:.9em;
        line-height:1.5em;
        font-family:"Helvetica Neue", Arial, Helvetica, Geneva, sans-serif;
    }
    div.content ul, div.content p {
        padding:0;
        margin:0;
        padding:3px;
    }
    div.content ul li {
        list-style-position:inside;
        line-height:25px;
    }
    div.content ul li a {
        color:#555555;
    }
    code {
        overflow:auto;
    }
</style>
 
 <%-- 
  <script>
  $( document ).ready(function() {
    $( "#accordion" ).accordion();
  });
  </script> --%>
  
<form id="campaignSettingForm" class="form-horizontal themed">

<div id="tab-content1" class="tab-content">
<div id= "accordion">
	<div id="commonsetting" style="margin-left: 1%;" class="row-fluid Profile tab-pane active">
	<article class="span6">
	  <!-- new widget -->
			<div class="jarviswidget" id="widget-id-0">
	  <!-- wrap div -->
	  <div>
	
		<div class="jarviswidget-editbox">
			<div>
	 	 		<label>Title:</label> <input type="text" />
		   		</div>
				<div>
					<label>Styles:</label> <span data-widget-setstyle="purple"
						class="purple-btn"></span> <span data-widget-setstyle="navyblue"
						class="navyblue-btn"></span> <span data-widget-setstyle="green"
						class="green-btn"></span> <span data-widget-setstyle="yellow"
						class="yellow-btn"></span> <span data-widget-setstyle="orange"
						class="orange-btn"></span> <span data-widget-setstyle="pink"
						class="pink-btn"></span> <span data-widget-setstyle="red"
						class="red-btn"></span> <span data-widget-setstyle="darkgrey"
						class="darkgrey-btn"></span> <span data-widget-setstyle="black"
						class="black-btn"></span>
				</div>
			</div>
		
		 		<fieldset>
					<div class="inner-spacer">
					<!-- content goes here -->
	
								<div class="control-group">
									<label class="control-label" for="multiSelect">Campaign
										Name<span class="req star">*</span>
									</label>
									<div class="controls">
										<input name="campaignName" type="text" class="span12"
											id="campaignName" />
									</div>
								</div>
								
									<div class="control-group">
									<label class="control-label" for="multiSelect">Lifetime 
										Goal<span class="req star">*</span>
									</label>
									<div class="controls">
										<input name="lifetimegoal" type="text" class="span6"
											id="lifetimegoal" />
									</div>
								</div>
								
						 	<div class="control-group">			
						 			 <label class="control-label" for="multiSelect">Benchmark 
											CTR
									</label>							
			             	 	  <div id="uislider-demo" class="controls sliderCTR">
			               	
										<div class="controls">
				           	 				<input type="text" size="10" id="amount2"  class="ui-display-label-black" />
				           	 			</div>	
				           	 	<div id="slider-range-min-ctr" class="warning-slider" ></div>
			             	  	</div>
				          </div>
	
								
								<div class="control-group">
									<label class="control-label" for="multiSelect">Devices</label>
								    	<div class="controls">
										<select multiple="multiple" id="multiSelect" class="span12 with-search">
											<option value="size1">Smart Phone</option>
									        <option value="size2">Tablet</option>
									        <option value="size3">Desktop</option>
										</select>
										</div>
	     						</div>
	     						
	     						
								
									<div class="control-group">
									<label class="control-label" for="multiSelect">Ad Sizes</label>
								    	<div class="controls">
										<select multiple="multiple" id="multiSelect" class="span12 with-search">
											<option value="size1">300X50</option>
									        <option value="size2">320X50</option>
									        <option value="size3">300X250</option>
									        <option value="size4">728X90</option>
										</select>
										</div>
										<label class="control-label" for="multiSelect" style="margin-top: 52px;">Other</label>
										<input name="otherAdSize" type="text" class="span6" style="margin-left: 80px;margin-top: 52px;" id="otherAdSize" />
	     						</div>
							
						
					</div>
				</fieldset>
			 </div>	
			</div>
	  
	</article>
	
		<article style="margin-left: 0%;" class="span6">
				<!-- new widget -->
				<div class="jarviswidget" id="widget-id-0">
				
				
					<!-- wrap div -->
				<div>
				
					<div class="jarviswidget-editbox">
							<div>
								<label>Title:</label> <input type="text" />
							</div>
							<div>
								<label>Styles:</label> <span data-widget-setstyle="purple"
									class="purple-btn"></span> <span data-widget-setstyle="navyblue"
									class="navyblue-btn"></span> <span data-widget-setstyle="green"
									class="green-btn"></span> <span data-widget-setstyle="yellow"
									class="yellow-btn"></span> <span data-widget-setstyle="orange"
									class="orange-btn"></span> <span data-widget-setstyle="pink"
									class="pink-btn"></span> <span data-widget-setstyle="red"
									class="red-btn"></span> <span data-widget-setstyle="darkgrey"
									class="darkgrey-btn"></span> <span data-widget-setstyle="black"
									class="black-btn"></span>
							</div>
						</div>
						
						<fieldset>
							<div class="inner-spacer">
								<!-- content goes here -->
							
							
								<div class="control-group">
								<label class="control-label" for="multiSelect">StartDate/EndDate</label>
								<div class="controls">
							       <div id="dateslider"></div>
							    </div>
							   </div>
							
							
	     						
	     						<div class="control-group">
									<label class="control-label" for="multiSelect">Frequency
										Cap<span class="req star">*</span>
									</label>
									<div class="controls">
										<input name="frequencycap" type="text" class="span3"
											id="frequencycap" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="multiSelect">Rate
										<span class="req star">*</span>
									</label>
									<div class="controls">
										<input name="rate" type="text" class="span3"
											id="rate" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="textarea">Notes</label>
										<div class="controls">
								     		<textarea class="span12" id="notes" rows="3"></textarea>
										</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="multiSelect">Ad Format</label>
								    	<div class="controls">
										<select multiple="multiple" id="multiSelect" class="span12 with-search">
											<option value="size1">Static</option>
									        <option value="size2">Rich Media</option>
									        <option value="size3">Video</option>
										</select>
										</div>
	     						</div>
	     						
						</div>
					</fieldset>
					
			</div>
		</div>
		</article>
		
	</div>
	<div id="targetsetting" style="margin-left: 1%;" class="row-fluid Profile tab-pane">
	<article class="span6">
	 <!-- new widget -->
			<div class="jarviswidget" id="widget-id-0">
	  <!-- wrap div -->
	   <div>
	             <header>
			       <h2 style="padding-left: 20px;">Geo Targetiing</h2>                           
			     </header>
		<div class="jarviswidget-editbox">
			<div>
	 	 		<label>Title:</label> <input type="text" />
		   		</div>
				<div>
					<label>Styles:</label> <span data-widget-setstyle="purple"
						class="purple-btn"></span> <span data-widget-setstyle="navyblue"
						class="navyblue-btn"></span> <span data-widget-setstyle="green"
						class="green-btn"></span> <span data-widget-setstyle="yellow"
						class="yellow-btn"></span> <span data-widget-setstyle="orange"
						class="orange-btn"></span> <span data-widget-setstyle="pink"
						class="pink-btn"></span> <span data-widget-setstyle="red"
						class="red-btn"></span> <span data-widget-setstyle="darkgrey"
						class="darkgrey-btn"></span> <span data-widget-setstyle="black"
						class="black-btn"></span>
				</div>
			</div>
			<fieldset>
					<div class="inner-spacer">
					<!-- content goes here -->
					
				                                    		<div class="control-group">
																<label class="control-label" for="select01">Country</label>
																<div class="controls">
																	<select id="countrylist" class="span12 with-search">
														                <option value="AK">USA</option>
														                <option value="HI">India</option>
														                <option value="CA">United Kingdom</option>
														                <option value="NV">South Africa</option>
														                <option value="OR">Slovenia</option>
														                <option value="WA">Switzerland</option>
														                <option value="AZ">Japan</option>
														                <option value="CO">Australia</option>
															        </select>
																</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="multiSelect">DMA</label>
																<div class="controls">
																	<select multiple="multiple" id="dmalist" class="span12 with-search">
														                <option value="AK">New York</option>
														                <option value="HI">Philadelphia</option>
														                <option value="CA">Savannah</option>
														                <option value="NV">Ft. Wayne</option>
														                <option value="OR">Washington</option>
														                <option value="WA">Buffalo</option>
														                <option value="AZ">Providence</option>
														                <option value="CO">Indianapolis</option>
															        </select>
																</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="multiSelect">State</label>
																<div class="controls">
																	<select multiple="multiple" id="statelist" class="span12 with-search">
														                <option value="AK">New York</option>
														                <option value="HI">Pennsylvania</option>
														                <option value="CA">Georgia</option>
														                <option value="NV">Indiana</option>
														                <option value="OR">Columbia</option>
														                <option value="WA">Indiana</option>
														                <option value="AZ">Rhode Island</option>
														                <option value="CO">Connecticut</option>
															        </select>
																</div>
															</div>
															
																<div class="control-group">
																<label class="control-label" for="multiSelect">Zip</label>
																<div class="controls">
																	<select multiple="multiple" id="ziplist" class="span12 with-search">
														                <option value="AK">31405</option>
														                <option value="HI">46808</option>
														                <option value="CA">14207</option>
														                <option value="NV">2914</option>
														                <option value="OR">46202</option>
														                <option value="WA">6510</option>
														                <option value="AZ">45439</option>
														                <option value="CO">44512</option>
															        </select>
																</div>
															</div>
	
								
		        	</div>
			</fieldset>
			
			</div>
	  </div>
	</article>
	
	<article style="margin-left: 0%;" class="span6">
	 <!-- new widget -->
			<div class="jarviswidget" id="widget-id-0">
	  <!-- wrap div -->
	   <div>
	             <header>
			       <h2 style="padding-left: 20px;">Demo Targetiing</h2>                           
			     </header>
		<div class="jarviswidget-editbox">
			<div>
	 	 		<label>Title:</label> <input type="text" />
		   		</div>
				<div>
					<label>Styles:</label> <span data-widget-setstyle="purple"
						class="purple-btn"></span> <span data-widget-setstyle="navyblue"
						class="navyblue-btn"></span> <span data-widget-setstyle="green"
						class="green-btn"></span> <span data-widget-setstyle="yellow"
						class="yellow-btn"></span> <span data-widget-setstyle="orange"
						class="orange-btn"></span> <span data-widget-setstyle="pink"
						class="pink-btn"></span> <span data-widget-setstyle="red"
						class="red-btn"></span> <span data-widget-setstyle="darkgrey"
						class="darkgrey-btn"></span> <span data-widget-setstyle="black"
						class="black-btn"></span>
				</div>
			</div>
					<fieldset>
					<div class="control-group">
						<label class="control-label">Gender</label>
							<div class="controls">
					    		<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-5" value="option5">
									Male 
								</label>
						    	<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-6" value="option6" >
							    	Female 
								</label>
						 </div>
					</div>
					
						<div class="control-group">
						<label class="control-label">Age</label>
							<div class="controls">
					    		<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-5" value="option5">
									Below 15 yr 
								</label>
						    	<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-6" value="option6" >
							    	15 to 50 yr 
								</label>
								<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-6" value="option6" >
							    	Above 50 yr 
								</label>
						 </div>
					</div>
					
					<div class="control-group">
						<label class="control-label">Income</label>
							<div class="controls">
					    		<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-5" value="option5">
									Below 10K 
								</label>
						    	<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-6" value="option6" >
							    	10K to 25K 
								</label>
								<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-6" value="option6" >
							    	Above 25K 
								</label>
						 </div>
					</div>
					
					<div class="control-group">
						<label class="control-label">Education</label>
							<div class="controls">
					    		<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-5" value="option5">
									Non Graduate 
								</label>
						    	<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-6" value="option6" >
							    	Graduate 
								</label>
								<label class="checkbox inline">
									<input type="checkbox" id="optionsCheckbox-6" value="option6" >
							    	Post Graduate 
								</label>
						 </div>
					</div>
					</fieldset>
					</div>
				</div>
			</article>
	</div>
	</div>
</div>
	<!-- <a href="javascript:void(0);" onclick="" style="float: right; margin-top: 0%; margin-right: 1%;" class="btn btn-danger btn-large floatRight">Close</a> -->
    <a id="nextButton" href="javascript:void(0);" onclick="nextTab();" style="float: right; margin-top: 0%; margin-right: 1%;" class="btn btn-success btn-large">Next</a>
     <a id="saveButton" href="javascript:void(0);" onclick="" style="float: right; margin-top: 0%; margin-right: 1%; display: none;" class="btn btn-success btn-large">Save</a>
    <a id="backButton" href="javascript:void(0);" onclick="backTab();" style="float: left; margin-top: 0%; margin-right: 1%; display: none;" class="btn btn-success btn-large">Back</a>
</form>