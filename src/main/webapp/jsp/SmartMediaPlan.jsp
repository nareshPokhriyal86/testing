<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<jsp:include page="Header.jsp" />

<html lang="en" ng-app="smartMediaPlanApp">
<head>
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - SmartMediaPlan</title>
<meta name="description" content="">
<meta name="author" content="">

<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Le CSS
    ================================================== --> 
<jsp:include page="css.jsp"/>
<link rel="stylesheet" type="text/css" href="../css/smartMediaPlan.css?v=<s:property value='deploymentVersion'/>" />


<script type="text/javascript" src="/_ah/channel/jsapi"></script>

</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->

	<!-- end header -->

	<div id="main" role="main" class="container-fluid">
		<form name="smartMediaPlan" id="select-demo-js" cssClass="form-horizontal themed" action="" >
		
		<div id="campaignCtrlDiv" ng-controller="campaignHeaderCtrl" class="contained" ng-cloak>
			<!-- main content -->
			<div id="page-content" class="pageCss">
			<jsp:include page="navigationTab.jsp"/>
            
            <div  class="form-actions">
                <h3 style="font-size:24px; margin:0px; padding:0px;"class="headerText" id="page-header">Recommended Media Plan - <b>{{ campaignHeader.name }}</b></h3>
                <a href="javascript:launchCampaign()" id="launchCampaignButtonId" class="btn btn-primary" style="margin-top: 0.5%;margin-left:1%;float: right;">Launch Campaign</a>
			    <!-- <a href="javascript:setupCampaign()" class="btn btn-primary" id="setupCampaignButtonId" style="margin-top: 0.5%;float: right;">Setup Campaign</a> -->
			    <a href="javascript:addCampaignSetupInTaskQueue()" class="btn btn-primary" id="setupCampaignButtonId" style="margin-top: 0.5%;float: right;">Setup Campaign</a>
			    
			    <div id="setupCampaignLoader" style="display:none;text-align: center;">
					<img src="img/loaders/type5/light/24.gif" alt="loader">
				</div>
				
			    <!-- <div id="setupCampaignLoader" style="display:none;text-align: center;">
				   <img src="img/loaders/type4/light/46.gif" alt="loader">
		        </div> -->
		        
			
			</div>			
									    
		  <div class="row-fluid" style="background-color:white;">	
			 <div  class="row" style="background-color:white;min-height:30px; padding: 15px 0;">
			 <div class="space_left" >
			 
				   <div class="header_details_block">			     
				     <div class="header_text_css">{{ campaignHeader.budget | currency:'$'}} </div>
				     <div class="name_details_gray">Budget</div>
				   </div>
			   
				   <div  class="header_details_block">			    
				     <div class="header_text_css" >{{ campaignHeader.impression | number }}</div>
				      <div class="name_details_gray">Impressions</div>
				   </div>
			   
			  
				   <div  class="header_details_block">			    
				     <div class="header_text_css">{{ campaignHeader.eCPM | currency:'$'}}</div>
				      <div class="name_details_gray">eCPM</div>
				   </div>
			  
			   
				   <div  class="header_details_block">			     
				     <div class="header_text_css">{{ campaignHeader.cost | currency:'$'}}</div>
				     <div class="name_details_gray">Cost</div>
				   </div>
			   
			   
				   <div  class="header_details_block">			   
				     <div class="header_text_css">{{ campaignHeader.servingFee | currency:'$'}}</div>
				     <div class="name_details_gray">ServingFee</div>
				   </div>
			  
			   
				   <div class="header_details_block">			   
				     <div class="header_text_css">{{ campaignHeader.netRevenue |  currency:'$'}}</div>
				     <div class="name_details_gray">NetRevenue</div>
				   </div>
				  <div  class="header_details_block_right">			     
						   <a class="btn" href="javascript:downloadIO();" class="downloadIcon" title="Download">
							<i class="cus-doc-excel-table"></i>
						 </a>
				   </div>
				  
				   <div class="header_details_block_right">			     
						<section style="padding:10px 0px;">
						  <input type="radio" name="selectIO" id="radioPublisherIO" value="" style="margin:0px 5px 0px 0px;"/><span>Publisher IO</span>
						  <input type="radio" name="selectIO" id="radioClientIO" value="" style="margin:0px 5px 0px 10px;"/><span>Client IO</span>
						</section> 
				   </div>
						   
			 </div> 
			 
			</div>
		
		 <div id="partnerModel" class="modal hide fade" tabindex="-1" role="dialog" 
			      aria-labelledby="myModalLabel" aria-hidden="true" 
			      style="top: 40%;max-width: 30%!important;left:50%;align-self:center; display:none;">
									
				<div class="modal-body" id ="modalDivId">
				  <div id=partnerPopup >
					 <div class="row-fluid"  >
						<h1 id="page-header" style="margin-left: 1%;background-color: #F5F5F5;">Add Partner</h1>
						 <div class="row-fluid" >
						  <article class="span12">
							<div class="control-group" style="border-bottom: none;">
							  <label class="control-label">Partner Name</label>
							   <div class="controls">
							     <select class="span12" id="partnerName" >
							        <option value="-1">Select a partner</option>
							     </select>
							   </div>
							   <button ng-click="addProductCart()" class="btn btn-success btn-large" 
							           style="margin-top: 10%;padding: 10px 12px;background: #8BB226;border: 0px;" type="button">
									    Add Product</button>
									    
							 </div>
						  </article>		
						  </div>
							
						</div>
				   </div>
				</div>
		 </div>
			 
		<div class="row-fluid"  ng-repeat="placement in placements">
		    
		    <div class="placement_div_head">
		      <div class="space_left" ></div>
			  <table class="placement_table">
			     <thead style="line-height:36px;font-size:14px;font-weight:normal;">
			      <tr style="background-color:#f1f7f7;border:1px solid #e0ebed; font-weight:bold;">

			        <td style="max-width: 100px; min-width: 125px;">Placement</td>
			        <td>Rate</td>
			        <td>Impressions</td>
			        <td>Budget</td>
			        <td style="max-width: 100px; min-width: 50px;">Creatives</td>
			        <td style="max-width: 100px; min-width: 80px;">Geo Targets</td>
			        <td style="max-width: 100px; min-width: 50px;">Notes</td>			        
			      </tr>
			    </thead>
			     <tbody >
			      <tr class="placement_table_data">
			        <td>{{ placement.name }}</td>
			        <td>{{ placement.rate | currency:'$'}}</td>
			        <td>{{ placement.impression | number}}</td>
			        <td>{{ placement.budget | currency:'$'}}</td>
			        <td>{{ placement.creatives }}</td>
			        <td>{{ placement.dmas }}</td>
			        <td>{{ placement.notes }}</td>			        
			      </tr>
			     </tbody>
			     
			  </table>
			  <a  ng-click="addPartner( placement.id )" class="btn btn-primary" id="addPartner" 
			     style="margin-left: 1%;margin-top: 0.5%;float: left;cursor:pointer;">Add Partner</a>
			    
			  </div>		

		    <div class="row-fluid" style="background-color:white;">
		     <div class="row" >
		        <article class="span4" ng-repeat="product in placement.products" style="">
		        
		          <!-- Overflow product widget div-->
		          
		          <!-- <div id="product_div" ng-show="product.partner == 'Overflow Impressions' "
					    data-widget-fullscreenbutton="false"  class="jarviswidget jarviswidget-sortable" role="widget" 
					    style="border: 1px soild #ddd !important;" > -->
					    
		        	 <div id="product_div" ng-show="product.partner == 'Overflow Impressions' && product.allocatedImp>0"
					    data-widget-fullscreenbutton="false"  class="jarviswidget jarviswidget-sortable" role="widget" 
					    style="border: 1px soild #ddd !important;" ng-model="product">
				 	 
				 	    <header role="heading" 
					       style="background:#F2F2F2; padding: 6px 12px;">					      
					      <h2 class="overflow_header_css">{{ product.partner }}</h2>
					      <%-- <span class="jarviswidget-ctrls" role="menu">
							    <a class="close" confirmed-click="deleteItem($index, product.placementId )" 
							    ng-confirm-click="Are you sure, you want to delete this product ?" style="cursor:pointer;">×</a>
						  </span> --%>
				       </header>
				       <content >
							<div class="inner-spacer widget-content-padding" style="background:#F9F9F9;min-height:260px;"> 
							<!-- content goes here -->	
								<div class="well" >
								   <table class="overflow_product">
                                       <tbody>
                                        <tr><td class="td_css">Product Name</td><td></td>                                             
                                           <td>
                                             <input type="text" value='{{ product.name }}' class="text_css_2" readonly="true" ng-change="changed(true)"/>
                                           </td>
                                        </tr>
                                        <tr><td class="td_css">Offered Rate ($)</td><td></td>
                                            <td>
                                               <input type="text" value='{{ product.rate | number:2}}' class="text_css_2" ng-change="changed(true)"
                                                my-blur="modifyProductsByRate(product.placementId,product.id)" my-focus="rate_product.id"
                                        	    ng-model="product.rate"/> 
                                            </td>
                                        </tr>
                                        <tr><td class="td_css">Available Impressions</td><td></td>
                                           <td>
                                             <%-- <span  class="text_css_2">N/A</span>     --%>
                                             <input type="text" value="N/A" class="text_css_2" readonly="readonly"/>  
                                           </td>                                           
                                         </tr>
                                        <tr><td class="td_css">Allocated Impressions</td><td></td>
                                        	<td>
                                        	
                                        	 <input type="text" currency-input = ""  value="{{ product.allocatedImp }}" ng-change="changed(true)"
                                        	    my-blur="updateProducts(product.placementId,product.id)" my-focus="allocateImp_product.id"
                                        	    class="text_css_2" ng-model="product.allocatedImp" />   
                                        	   
                                        	<!--  <input type="text" currency-input = ""  value="{{ product.allocatedImp }}"                                        	   
                                        	    class="text_css_2" my-directive ng-model="product.allocatedImp" />   -->                                      	    
                                        	                                        	    
                                        	  </td>
                                        </tr>
                                        <tr><td class="td_css">Allocated Budget ($)</td><td></td>
                                        	<td>
                                        	  <input type="text"  value="{{ product.budget }}" class="text_css_2" ng-change="changed(true)"
                                        	    my-blur="modifyProductsByBudget(product.placementId,product.id)" my-focus="allocatedBudget_product.id"
                                        	    ng-model="product.budget"/> 
                                        	
                                        	</td>
                                        </tr>
                                        <tr><td class="td_css">Rev. Share (%)</td><td></td>
                                        	<td>                                        	   
                                        	   <input type="text" value='{{ product.revenueSharingPercent }}' class="text_css_2" 
                                        	      ng-change="modifyProductsByRevenuePercent(product.placementId,product.id);changed(true);"
                                        	      ng-model="product.revenueSharingPercent" />  
                                        	</td>
                                        </tr>
                                        <tr><td class="td_css">Payout ($)</td><td></td>
                                        	<td>                                        	  
                                        	  <input type="text"  value="{{ product.payout  }}"  class="text_css_2" 
                                        	     ng-change="modifyProductsByPayout(product.placementId,product.id);changed(true);"
                                        	     ng-model="product.payout" />                                         	  
                                        	 </td>
                                        </tr>
                                        <tr><td class="td_css">Net Rate(CPM) ($)</td><td></td>
                                        	<td>                                        	  
                                        	  <input type="text" value='{{ product.cpm | number:2}}'  class="text_css_2"
                                        	      ng-change="modifyProductsByNetRate(product.placementId,product.id);changed(true);"
                                        	      ng-model="product.cpm" /> 
                                        	  
                                        	</td>
                                        </tr>
                                       </tbody>								   
								   </table>
								</div>							
							</div>
						</content>
						<!-- end of overflow content -->
				 
				 	
				 	</div> <!-- end of overflow div -->
				 	
				 	<!-- Product widget starts-->
					<div style="border: 1px soild #ddd !important;" id="product_div" ng-show="product.partner != 'Overflow Impressions'"
					    data-widget-fullscreenbutton="false"  
				 	    class="jarviswidget jarviswidget-sortable" role="widget">
							
					 <header role="heading" 
					      style="background:none;">
					     
					     <div class="product_header_css"  id="bootbox-js" ng-switch on="product.partner_logo">
					       <span ng-switch-when=""></span>
					       <span ng-switch-default><img src='{{ product.partner_logo }}' width="150" height="50"/></span>
					       
					        <!-- http://storage.googleapis.com/linmobile_dev/CompanyLogo/12/SynergyMapImages_2014-06-2611:10:46.082' style="max-height:40px; -->
						  <span class="jarviswidget-ctrls" role="menu">
						   <a class="close" confirmed-click="deleteItem($index, product.placementId )" 
						   ng-confirm-click="Are you sure, you want to delete this product ?" style="cursor:pointer;">×</a>
						  </span>
					     </div>	
					 
				     </header>
						<!-- product widget div-->
						<content>
							<div class="inner-spacer widget-content-padding" style="min-height:260px;"> 
							<!-- content goes here -->	
								<div class="well">
								   <table class="product">
                                       <tbody>
                                        <tr><td class="td_css">Product Name</td><td></td>
                                             <!-- <td><input type="text" value='{{ product.name }}' class="text_css" /></td> -->
                                            <td>
                                                <%-- <span class="text_css" >{{ product.name }}</span> --%>
                                                <input type="text" value='{{ product.name }}' class="text_css" ng-change="changed(true)" readonly="readonly"/>
                                            </td>
                                        </tr>
                                        <tr><td class="td_css">Offered Rate($)</td><td></td>
                                            <td>
                                               <input type="text" value='{{ product.rate }}' class="text_css" ng-change="changed(true)"
                                                 my-blur="modifyProductsByRate(product.placementId,product.id)" my-focus="rate_product.id"
                                        	    ng-model="product.rate"/>
                                            </td>
                                        </tr>
                                        <tr><td class="td_css">Available Impressions</td><td></td>
                                           <td>
                                              <%--  <span class="text_css" >{{ product.availableImp | number}}</span> --%>
                                               <input type="text" value="{{ product.availableImp | number}}" class="text_css" readonly="readonly"/> 
                                           </td>                                           
                                         </tr>
<%--                                         <tr ng-show="product.matchedImp >  0"><td class="td_css">Matched Impressions</td><td></td>
                                           <td>
                                               <span class="text_css" >{{ product.availableImp | number}}</span>
                                               <input type="text" value="{{ product.matchedImp | number}}" class="text_css" readonly="readonly"/> 
                                           </td>                                           
                                         </tr> --%>
                                        <tr><td class="td_css">Allocated Impressions</td><td></td>
                                        	<td>
                                        	  <input type="text" currency-input = "" value="{{ product.allocatedImp }}" class="text_css" 
                                        	    my-blur="updateProducts(product.placementId,product.id)" ng-change="changed(true)"
                                        	     my-focus="allocateImp_product.id" ng-model="product.allocatedImp" />                                          	    
                                        	 </td>
                                        </tr>
                                        <tr><td class="td_css">Allocated Budget($)</td><td></td>
                                        	<td>
                                        	  <input type="text"  value="{{ product.budget }}" class="text_css" ng-change="changed(true)"
                                        	       my-blur="modifyProductsByBudget(product.placementId,product.id)" my-focus="allocatedBudget_product.id"
                                        	       ng-model="product.budget"/> 
                                        	</td>
                                        </tr>
                                        <tr><td class="td_css">Rev. Share(%)</td><td></td>
                                        	<td>                                        	      
                                        	   <input type="text" value='{{ product.revenueSharingPercent }}' class="text_css" 
                                        	     my-blur="modifyProductsByRevenuePercent(product.placementId,product.id)" ng-change="changed(true)"
                                        	     my-focus="revShare_product.id" ng-model="product.revenueSharingPercent" />  
                                        	</td>
                                        </tr>
                                        <tr><td class="td_css">Payout($)</td><td></td>
                                        	<td>
                                        	  <input type="text"  value="{{ product.payout }}" class="text_css"
                                        	     my-blur="modifyProductsByPayout(product.placementId,product.id)" ng-change="changed(true)"
                                        	     my-focus="payout_product.id" ng-model="product.payout" />  
                                        	 </td>
                                        </tr>
                                        <tr><td class="td_css">Net Rate(CPM)($)</td><td></td>
                                        	<td> 
                                        	  <input type="text" value='{{ product.cpm | number:2}}' class="text_css" 
                                        	      my-blur="modifyProductsByNetRate(product.placementId,product.id)" ng-change="changed(true)"
                                        	      my-focus="netRate_product.id" ng-model="product.cpm" />  
                                        	</td>
                                        </tr>
                                       </tbody>								   
								   </table>
								</div>
							<!-- end content -->
							</div>
							
						</content>
						<!-- end content -->
					</div>
					
				</article>
		      </div>		     
		    </div>			   
	    	</div>
		  <div id="errorDiv" style="color:red;font-size: 16px;text-align: center;"></div>					    
	  </div>
		  
		  <div id="ajaxLoaderDiv" style="display:none;text-align: center;">
				<img src="img/loaders/type4/light/46.gif" alt="loader">
		  </div>
			
		  <div  class="form-actions" style="margin-top: 50px;">
		   
			<a href="javascript:saveSmartMediaPlan(false)"  id="saveMediaPlanId" class="btn btn-primary" style="float: left;">
			   Save Media Plan</a>
		    
			   
	     </div>
	    
		</div>		
		
		
			
	</div> <!-- end of controller div -->
	
				<!-- end: tabs view -->
				<!-- <input type="hidden" id="hiddenVal" name="id" value="" > -->
				<input type="hidden" id="productId" name="productId" value="" >
				<input type="hidden" id="adServerId" name="adServerId" value="" >
				<input type="hidden" id="smartMediaPlanId" name="smartMediaPlanId" value="" >
			</form>	
	  </div>
  
  <!-- end main content -->

	<!--end fluid-container-->
	<div class="push"></div>
</div>
<!-- end .height wrapper -->

<!--================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

	<!-- Le javascript
    ================================================== --> 

<%-- <script src="../js/angular/angular.min.js"></script>  // use only one angular js--%>  
<%-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> --%>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.0-beta.13/angular.min.js"></script> 
<!--  use only one angular js -->
<script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.6.0.js"></script>
<script type="text/javascript" src="../js/poolmap/hashtable.js"></script> 
<script type='text/javascript' src='../js/include/toastr.min.js'></script>
<script type='text/javascript' src='../js/include/jquery.jgrowl.min.js'></script>

<script> 

  var campaignJsonObj = eval( <s:property escape="false" value="jsonData" /> );
  //console.log(campaignJsonObj);
  var smartMediaPlanId='';
  var saveMediaPlan=false;
  var dfpOrderId=campaignJsonObj.header.dfp_order_id;
  var campaignId=campaignJsonObj.header.campaign_id;
  var orderSetup=false;
  var lineItemsSetup=false;
  var userId=<s:property value="#session.sessionDTO.userId"/>;
  var token='<s:property value="#session.token"/>';
  var campaignStatus= campaignJsonObj.header.status;//"DRAFT";
  var isProcessing=campaignJsonObj.isProcessing;
  var inProgress=false;
  var partnerHashTable=new HashTable();
  
  if(dfpOrderId != null && dfpOrderId != undefined && dfpOrderId > 0) {
	  $('#setupCampaignButtonId').hide();
  }
  
  $(document).ready(function() {
		$('#cmapaignPlanLi').attr('class', 'main-nav-li_selected');
		//createChannel();     // For channel API
		if(inProgress || ( !(campaignStatus == 'Draft' || campaignStatus == 'DRAFT' || campaignStatus == '') ) || (dfpOrderId != 0 || dfpOrderId != '0')){
			$('#saveMediaPlanId').hide();
		}else{
			console.log("inProgress :"+inProgress+", campaignStatus:"+campaignStatus+", dfpOrderId:"+dfpOrderId);
		}
  });
  
/*   setTimeout(function(){
	    console.log("Saving smart plan in datastore....");
	    saveSmartMediaPlan();
  },10000);
   */
   var campaignSetupCheckInterval = 0;
   
   function startCampaignSetupCheck() {
	   campaignSetupCheckInterval=setInterval(function(){
		    console.log("Check campaign setup...dfpOrderId:"+dfpOrderId+", isProcessing: "+isProcessing);
		    if((dfpOrderId==0 || dfpOrderId=='0') && (isProcessing != undefined) 
		    		&& (isProcessing == 'true' || isProcessing==true)){
		    	toastr.clear();
		    	$('#setupCampaignLoader').show();
				$('#setupCampaignButtonId').hide();
				toastr.info("Campaign is setting up in DFP, Please wait.");
				toastr.options.timeOut = "29000";
		    	checkCampaignSetupStatus();
		    	
		    }else{
		    	 clearInterval(campaignSetupCheckInterval);
		    	 $('#setupCampaignLoader').hide();
		    	 console.log('Interval cleared');
		    }
	 },30000);
   }
   
  function checkCampaignSetupStatus(){
	  try{
		  
		  $.ajax({
		      type : "POST",
		      url : "/checkCampaignSetupStatus.lin",
		      cache: false,
		      data : {
		    	  campaignId : campaignId
			  },		    
		      dataType: 'json',	      
		      success: function (data) {		    	
		    	  var id=$.trim(data);
		    	  console.log('respnse (dfp order id) : '+id);
		    	  var notANumber=isNaN(id);
		    	  if(notANumber || id=='0'){
		    		  console.log("Campaign has not setup yet.");
		    	  }else{
		    		  toastr.clear();
		    		  dfpOrderId=id;
		    		  isProcessing=false;
		    		  toastr.success("This campaign has been setup in DFP with order Id :"+dfpOrderId);
		    		  clearInterval(campaignSetupCheckInterval);
		    		  $('#setupCampaignLoader').hide();
		    	  }	
		     },
		     error: function(jqXHR, exception) {		    	 
		    	 console.log("ajax response exception --"+exception);
		     }
		   }); 
		  
	  }catch(error){
		  console.log("checkCampaignSetupStatus : error : "+error);
	  }
  }
  
  function saveSmartMediaPlan(isGenerateReport) {
	  var returnVal = false;
	  $('#ajaxLoaderDiv').show();
	  var scope = angular.element($("#campaignCtrlDiv")).scope();
	  scope.validatePage();
	  
	 // saveMediaPlan=false; //For test
	  
	  if(saveMediaPlan){
		  try{	  
			    $.ajax({
			      type : "POST",
			      url : "/saveSmartMediaPlan.lin",
			      cache: false,
			      data : {
			    	  jsonData : JSON.stringify(campaignJsonObj)
				  },		    
			      dataType: 'json',	      
			      success: function (data) {
			    	  $('#ajaxLoaderDiv').hide();
			    	  var id=$.trim(data);
			    	  var notANumber=isNaN(id);
			    	  if(notANumber){
			    		  toastr.error("Media plan failed to saved.");
			    	  }else{
			    		  returnVal = true;
			    		  $('#smartMediaPlanId').val(id);
						  smartMediaPlanId=id;
						  campaignJsonObj.header.id=id;
						  toastr.success("Media plan updated successfully.");
						  isChanged = false;
						  if(isGenerateReport) {
							  downloadIO();
						  }
			    	  }					 
					  console.log(campaignJsonObj.header);
					 
			     },
			     error: function(jqXHR, exception) {
			    	 $('#ajaxLoaderDiv').hide();
			    	 toastr.error("ajax response exception --"+exception);
			     }
			   });   
			}catch(error){
				$('#ajaxLoaderDiv').hide();
				toastr.error("error on page --"+error);
			}
	  }else{
		  $('#ajaxLoaderDiv').hide();
	  }
	 return returnVal;
 }
  
 function downloadIO() {
	 var isGenerateIO = true;
	 if(isChanged) {
		 if(confirm("Do you want to save changes and generate updated report ?")) {
			 isGenerateIO = false;
			 saveSmartMediaPlan(true);
		 }
	 }
	 if(isGenerateIO) {
		 var mediaPlanId = campaignJsonObj.header.id;
		 var impressions=campaignJsonObj.header.impression;
		 if(isNaN(mediaPlanId) || mediaPlanId == 0) {
			 toastr.error('Save Media Plan before IO generation');
			 return false;
		 }
		 if(impressions == null || isNaN(impressions) || impressions <= 0) {
			 toastr.error('No impressions for this campaign');
			 return false;
		 }
		
		 if ($('#radioPublisherIO').is(':checked')) {
		 	location.href='POExcelReport.lin?mediaPlanId='+mediaPlanId;
		 }
		 else if ($('#radioClientIO').is(':checked')) {
		 	location.href='clientIOExcelReport.lin?mediaPlanId='+mediaPlanId;
		 }else{
			 console.log('Invalid check--');
		 }
	 }
 }
 
  
 function addCampaignSetupInTaskQueue(){	  
	  smartMediaPlanId=campaignJsonObj.header.id;
	  console.log('dfpOrderId:'+dfpOrderId+' and smartMediaPlanId:'+smartMediaPlanId+", userId:"+userId);
	  
	  if( (isNaN(dfpOrderId) || dfpOrderId=='0' )){
		  
		  console.log('setup campaign for smartMediaPlanId:'+smartMediaPlanId);
		  var scope = angular.element($("#campaignCtrlDiv")).scope();
		  scope.validatePage();
		  if(saveMediaPlan){
			  
			  if(!confirm("Are you sure you want to setup this campaign ?")) {
				 // toastr.info("test toastr....");
				  //toastr.options.timeOut = 0;
					//return false;
			  }else{
				  toastr.clear();
				  inProgress=true;
				  isProcessing=true;
				  $('#setupCampaignLoader').show();
				  toastr.info("Campaign is setting up in DFP, Please wait.");
				  toastr.options.timeOut = "29000";
				  $('#setupCampaignButtonId').hide();
				  $('#saveMediaPlanId').hide();
				  try{	  
					    $.ajax({
					      type : "POST",
					      url : "/addCampaignSetupInQueue.lin",
					      cache: false,
					      data : {
					    	  jsonData : JSON.stringify(campaignJsonObj),
					    	  smartMediaPlanId : smartMediaPlanId,
					    	  campaignStatus : campaignStatus,
					    	  userId : userId,
					    	  campaignId : campaignId
						  },		    
					      dataType: 'json',	      
					      success: function (data) {
					    	  //$('#setupCampaignLoader').hide();
					    	  var response=data+'';
					    	  console.log("response -- "+response);
					    		 
					     },
					     error: function(jqXHR, exception) {
					    	 $('#setupCampaignLoader').hide();
					    	 toastr.error("Error: "+exception);
					     }
					   });   
					}catch(error){
						$('#setupCampaignLoader').hide();
						toastr.error("error on page --"+error);
					}
					startCampaignSetupCheck();
			  }
			  
		  }
	  }else{
		  toastr.success("This campaign has already been setup in DFP with order Id :"+dfpOrderId);
		  toastr.options.timeOut = "0";
	  }
	 
}
 
 
 
 function launchCampaign(){	  
	  smartMediaPlanId=campaignJsonObj.header.id;
	  
	  var status= campaignJsonObj.header.status;
	  console.log('dfpOrderId:'+dfpOrderId+' and smartMediaPlanId:'+smartMediaPlanId+', status:'+status);
	  if(status ==undefined){
		  status="";
	  }
	  
	  if( (status == '' || status == 'DRAFT' ) && (!inProgress) ){
		  $('#setupCampaignLoader').show();
		  console.log('setup campaign for smartMediaPlanId:'+smartMediaPlanId);
		  var scope = angular.element($("#campaignCtrlDiv")).scope();
		  scope.validatePage();
		  if(saveMediaPlan){
			  inProgress=true;
			  toastr.info("Please wait for some time while campaign is being launched ....");
			  toastr.options.timeOut = "100";
			  
			  $('#launchCampaignButtonId').hide();
			  $('#saveMediaPlanId').hide();
			  
			  try{	  
				    $.ajax({
				      type : "POST",
				      url : "/launchCampaignOnDFP.lin",
				      cache: false,
				      data : {
				    	  smartMediaPlanId : smartMediaPlanId,
				    	  orderId : dfpOrderId,
				    	  campaignId : campaignId
					  },		    
				      dataType: 'json',	      
				      success: function (data) {
				    	  $('#setupCampaignLoader').hide();
				    	  var response=data+'';
				    	  console.log("response -- "+response);				    	  
				    	   if(response.contains('error')){
				    		  toastr.error("DFP Campaign Status :"+response);
				    		  toastr.options.timeOut = "100";
				    	  }else{				    		  
				    		  toastr.success("Campaign has been launched successfully with status :"+response);
				    		  toastr.options.timeOut = "100";
				    		  campaignJsonObj.header.status=response;	
				    	  }			 	 
				     },
				     error: function(jqXHR, exception) {
				    	 $('#setupCampaignLoader').hide();
				    	 toastr.error("ajax response exception --"+exception);
				    	 toastr.options.timeOut = "100";
				     }
				   });   
				}catch(error){
					$('#setupCampaignLoader').hide();
					toastr.error("error on page --"+error);
					toastr.options.timeOut = "100";
				}
		  }
	  }else{
		  if(inProgress){
			  toastr.warning("This campaign is already processing another request.");
			  $('#setupCampaignLoader').show();
		  }else{
			  toastr.info("This campaign has already been launched on DFP with status : "+status);
		  }
		  
		  toastr.options.timeOut = "100";
	  }
	 
 }
 
 var partnersJSObjArray=[];
 
 function partnersJSObject(id,partnerName,placementId){	
	 this.id=id;
	 this.partnerName=partnerName;
	 this.placementId=placementId;
	 this.selected=false;
	 
	 this.getPartnerName=function(){
		 return this.partnerName;
	 }
	 this.setPartnerName=function(partnerName){
		 this.partnerName=partnerName;
	 }
	 
	 this.getId=function(){
		 return this.id;
	 }
	 this.setId=function(id){
		 this.id=id;
	 }
	 
	 this.getPlacementId=function(){
		 return this.placementId;
	 }
	 this.setPlacementId=function(placementId){
		 this.placementId=placementId;
	 }
	 
	 this.getSelected=function(){
		 return this.selected;
	 }
	 this.setSelected=function(selected){
		 this.selected=selected;
	 }
 }
 
 

 
 
 /***************************************** Channel API implementation***************************************/
 //Use channel API to notify after a task completion in task queue
 function createChannel(){
	 token='<s:property value="#session.token"/>';
	
	  try{
			var channel = new goog.appengine.Channel(token);
			var socket = channel.open();
			socket.onopen = onOpened;
			socket.onmessage = onMessage;
			socket.onerror = onError;
			socket.onclose = onClose;
	  }catch(error){		  
		 console.log('Error: Channel not created after expires...'+error);		  	  
	  }
 }

 onOpened = function() {
	console.log("channel opened...");
 }; 

 onMessage = function(message) {
	inProgress=false;
	$('#setupCampaignLoader').hide();
	console.log("======= Campaign status via channel API ======");	
	var response=$.parseJSON(message.data);
	
	//show your message
	if(response.dfpOrderId !='0'){
		toastr.success("Campaign has been setup with OrderId  -- "+dfpOrderId);		
		dfpOrderId=response.dfpOrderId;
		toastr.options.timeOut = 400;
	}else{
		toastr.error("Campaign failed to setup  -- "+response.status);		
		toastr.options.timeOut = 400;
	}		
	toastr.options.closeButton = true;
	
 }; 
		

 onError=function(){
	try{
		var child = $wnd.parent.document.getElementById("wcs-iframe");
		if ( child != null ) {
			child.parentNode.removeChild(child);	
		} 
	}catch(error){
		console.log("error -- "+error);
	}
	reCreateChannel();
 };

 onClose = function() {
	try{
		var child = $wnd.parent.document.getElementById("wcs-iframe");
		if ( child != null ) {
			child.parentNode.removeChild(child);	
		} 
	}catch(error){
		console.log('error : '+error);
	}	
	reCreateChannel();
 }; 
 
var counter=0;
function reCreateChannel(){
	try{
		if(counter <3){		
			console.log("create channel after relogin attempts :"+counter);
			counter++;
			$.ajax({
			      url: "/createChannelAfterLogin.lin",
			      dataType: 'json',	     
			      contentType: 'application/JSON',
			      type: "GET",
			      success: function(data) {
			    	  console.log('channelToken -- '+data);
			    	  token=data;
		 			  createChannel();
			      },
			      error: function(jqXHR, exception) {
			       console.log("ajax exception -- "+exception);
			      }
			});
		}else{
			console.log("Please check channel api -- not able to get channel after relogin attempts :"+counter);
		}
	}catch(err){
		console.log("error in recreate channel- "+err);
	}
	
}

/***************************************** Channel API implementation ends ***************************************/

</script>

<script src="../js/angular/smartMediaPlan.js?v=<s:property value='deploymentVersion'/>"></script>

 <jsp:include page="js.jsp"/>
 
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
