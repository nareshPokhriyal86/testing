<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
<%@page import="com.lin.web.util.LinMobileVariables" %>
<%@page import="com.lin.web.util.LinMobileConstants" %>  
<%@page import="com.lin.web.dto.ProposalDTO" %>
<jsp:include page="Header.jsp" />
<!DOCTYPE html>
<html lang="en">
<head>

<link rel="stylesheet" href="../css/poolMap/poolmap2.css?v=<s:property value="deploymentVersion"/>">
<link rel="stylesheet" type="text/css" href="../css/angular/ng-grid.min.css" />

<div id='content-window'></div>


<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
%>
<title>ONE - One Audience</title>

<script>
var searchInventoryTab = '<%=TabsName.THE_POOL_SEARCH_INVENTORY%>';
var createProposalTab = '<%=TabsName.THE_POOL_CREATE_PROPOSAL%>';
$(document).ready(function() {
	$('#thePoolLi').attr('class', 'main-nav-li_selected');
});
</script>
 
<jsp:include page="css.jsp"/>

</head>

<body  ng-app="poolApp" ng-controller="poolController">
 
 <div id="mainDiv" class="height-wrapper">
  <div id="page-content" class="mlr" >
  	<jsp:include page="navigationTab.jsp"/>
  	<div class="row-fluid">
		<div class="inner-spacer widget-content-padding"> 
			<ul class="nav nav-tabs upper_tabs" id="myTab1">
				<s:if test="%{authorisationKeywordList.contains('oaSearchInventory')}">
					<li class="active"><a href="#s1" onclick="showTab(1)" id="search_inventory" class="upper_tab1"><%=TabsName.THE_POOL_SEARCH_INVENTORY%></a></li>
				</s:if>
				<s:if test="%{authorisationKeywordList.contains('oaCampaignPlanning')}">
					<li><a href="#s2" id="create_proposal" onclick="showTab(2)" class="upper_tab1"><%=TabsName.THE_POOL_CREATE_PROPOSAL%></a></li>
				</s:if>
			</ul>
  	
  	<s:if test="%{authorisationKeywordList.contains('oaSearchInventory')}">	
	  	<div class="tab-pane" id="s1">
	      <div  class="row-fluid">
			<button onclick="fetchDataFromFusionTableViaRest()"
					class="btn medium btn-success" type="button">Search Inventory</button>
			<br>
			    
			<div  id="creativeDiv" style="display:none;">
				<span >
			       <label class="control-label" for="input01">Creative Size : </label>
			    </span>&nbsp;
				<select name="creativeSize" id="creativeSize" class="span12 with-search" style="width:200px;">
				    <option value="-1">Select Creative Size</option>
					<option value="300x50">300x50</option>   
					<option value="320x50">320x50</option>   
					<option value="300x250">300x250</option>   
					<option value="728x90">728x90</option>  
					<option value="768x1024">768x1024</option>   
	   			</select> 
		    </div>        
																
	    </div>
	     
	     <div class="well" style="float:right;margin-right:-13px;">	 
			  <div id="daterange" class="pull-right" style="background: #fff; cursor: pointer; padding: 0px 3px; border: 1px solid #ccc">
		          <i class="icon-calendar icon-large" style="float:left;"></i>
		          <div style="font-size:15px;font-weight:bold; margin-left: 27px;margin-right: 12px;margin-bottom: 6px;"></div>
		          <p style="float:left;font-size:14px;margin:0px;"></p> 
		          <b class="caret" style="margin-top: -15px;float:right;margin-left:5px;"></b>
		      </div>	   
	      </div> 
	      <br> 
		  <!-- <button ng-click="addRow()">Add Row</button> -->
	      
	    
	       <div class="row-fluid " >     
		      <article class="span4 sortable-grid ui-sortable">
		          <div id="map-canvas" ></div>
		      </article>
		      
		      <article class="span4 sortable-grid ui-sortable">
		          <div id="bookOrdertable" class="jarviswidget">        
	           		<div class="bookOrdertableGrid"  ng-grid="gridOptions"></div>
	           		<div ng-grid-footer></div>
	           		<s:if test="%{authorisationKeywordList.contains('oaProcessOrder')}">
			   		<button ng-click="bookOrder()"
						class="btn medium btn-success" id="bookOrder" type="button">Process Order</button>
					</s:if>
		      	 </div>    
		      </article>
	    </div>
	 
			  <br>
	   </div>
  	</s:if>
  
	<s:if test="%{authorisationKeywordList.contains('oaCampaignPlanning')}">
	   <div class="tab-pane" id="s2" style="display:none;">
	     <a href="javascript:void(0);" onclick="showTab(1)" style="float: right; margin-top: 0%; margin-right: 1%;" class="btn btn-danger btn-large floatRight">Close</a>
		 <a href="javascript:void(0);" onclick="saveProposalAndShowList()" style="float: right; margin-top: 0%; margin-right: 1%;" class="btn btn-success btn-large">Save</a>
		  <br>			
	     <jsp:include page="PoolProposal.jsp"/>
	   </div>
  	</s:if>
  </div>
  </div>
  </div>
  
</div>
 <script >
  var pageName="poolMapView";
  var advertiserViewPage="";
  var publisherViewPage="";
  var MERGE_TABLE_ID= '<%=LinMobileVariables.FUSION_POOL_MAP_MERGED_TABLE %>' ;
  var redirectURL='<%=LinMobileVariables.REDIRECT_URL %>';
  var dmaKMLPath='<%=LinMobileConstants.DMA_KML_CLOUD_STORAGE_PATH%>';
  <%-- var fusionTableAccessToken='<%=request.getAttribute("FusionAccessToken")%>'; --%>
  
 function showTab(tabNo){
	 if(tabNo=='1'){
		 $('#s1').show();
		 $('#s2').hide();
	 }else if(tabNo=='2'){
		 $('#s1').hide();
		 $('#s2').show();
		 initializePropsalScreen();
	 }
 }
</script>




<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="../js/moment.js?v=<s:property value="deploymentVersion"/>"></script>
<script type="text/javascript" src="../js/daterangepicker.js?v=<s:property value="deploymentVersion"/>"></script>  

<script type="text/javascript" src="../js/poolmap/poolmap-datepicker.js?v=<s:property value="deploymentVersion"/>"></script>

<%-- <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script> --%>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="../js/poolmap/geoxml3.js"></script>
<script type="text/javascript" src="../js/ProjectedOverlay.js"></script>

<script type="text/javascript" src="../js/angular/angular.min.js?v=<s:property value="deploymentVersion"/>"></script>
<script type="text/javascript" src="../js/angular/ng-grid.min.js?v=<s:property value="deploymentVersion"/>"></script>

<script type="text/javascript" src="../js/poolmap/poolMap2.js?v=<s:property value="deploymentVersion"/>"></script>
<script type="text/javascript" src="../js/poolmap/hashtable.js?v=<s:property value="deploymentVersion"/>"></script>


    
<script>
   var poolApp= angular.module('poolApp',['ngGrid']);   
     
     
   var hashTable = new HashTable();
   
   function checkInfoWindowData(dmaCode,creativeSize){	
	   var hashKey=dmaCode+"_"+creativeSize;
	   if(hashTable.hasItem(hashKey)){		   
			var quantity=hashTable.getItem(hashKey).Quantity;
			var avlQty=hashTable.getItem(hashKey).Available_Quantity;
			if(parseInt(avlQty) < parseInt(quantity)){
				alert('Quantity can not be greater than available impressions');
				$('#quantity').val(0);
				currentDMAData.Quantity=0;
			}else{
				$('#quantity').val(quantity);
				currentDMAData.Quantity=quantity;
			}
		}
   }
   
   function bindInfoWindowToAngular(){	   
	  var status=checkDataBeforePushInGrid();
	  if(status){
		  var hashKey=currentDMAData.DMA_Region+"_"+currentDMAData.Ad_Unit_Size;
		  hashTable.setItem(hashKey, currentDMAData);	  
		  
	      scope1=angular.element('#infoWindowDiv').scope();       
	      scope1.currentDMAData=currentDMAData;
	      scope1.hashTable=hashTable;
	      
		  scope1.safeApply(function(){
			   var dataArray=[];
		       scope1.hashTable.each(function(key, value) {
		    	   dataArray.push(value);
	 		   });		       
		       scope1.myData=dataArray;
		  }); 
		  
		  infoWindow.close();
	  }else{
		  console.log("invalid data");
	  }
	 
	 
   }
   
   function checkDataBeforePushInGrid(){	   
	    try{	
	    	
	      var availableImp=document.getElementById('availableImpression').innerHTML;
	      availableImp = parseInt(availableImp.replace(/\,/g, ''));
	      
		  var quantity=parseInt($('#quantity').val());
		 
		  
		  if(quantity > availableImp){
		     alert('Quantity can not be greater than available impressions');
			 document.getElementById('quantity').value=0;
			 return false;
		  }else{			  
			  var rate=parseFloat(currentDMAData.Rate);
			  currentDMAData.Quantity=''+quantity;
			  currentDMAData.Amount=''+parseFloat( quantity*rate /1000).toFixed(2);
			  return true;
		  }	
		}catch(err){
		  alert('err:'+err);
		  return false;
		}	
	}
	
   // This is the angular controller for poolMap
   function poolController($scope,$rootScope, $timeout) {

	   $scope.formats = ['Banner','Interactive'];
	   	 $scope.myData = [
	     {
	       'DMA_Region':'0',	       
	       'DMA_Name':'Empty',
	       'Ad_Unit_Size':'Empty',
	       'Available_Quantity':'0',
	       'Quantity':'0',
	       'Rate':'$4.00',
	       'Amount':'$00.00'
	     }
	   ];
	      
	   $scope.selections = [];

	   $scope.gridOptions = {
	        data: 'myData',	        
	   		enableRowSelection: true,
	   		multiSelect: true,
	        enableRowReordering: false,
	   		enableCellSelection: true,
	   		enableCellEditOnFocus: true,
	   		enableCellEdit: true,
	        showGroupPanel: true,
	        showFooter: true,
	        enableColumnResize: true,
	        columnDefs: 'customDef',
	        aggregateTemplate: '<div ng-click="row.toggleExpand()" ng-style="rowStyle(row)" class="ngAggregate"> <span class="ngAggregateText">{{row.label CUSTOM_FILTERS}} (count: {{row.totalChildren()}} {{AggItemsLabel}} Remaining: {{aggFC(row)}})</span> <div class="{{row.aggClass()}}"></div> </div>'
	   };
	   	
	   $scope.customDef =
			[
			 { field: 'DMA_Region', displayName: 'DMARegion',width:'90px' ,enableCellEdit:false},
			 { field: 'DMA_Name', displayName: 'DMA Name',width:'90px',enableCellEdit:false },
			 { field: 'Ad_Unit_Size', displayName: 'Ad Unit Size',width:'95px',enableCellEdit:false},
			 { field: 'Available_Quantity', displayName: 'Avl.Quantity',width:'95px',enableCellEdit:false},			 
			 { field: 'Quantity', displayName: 'Quantity',width:'90px'},
			 { field: 'Rate', displayName: 'Rate',width:'50px'},
			 { field: 'Amount', displayName: 'Amount',color:'red',enableCellEdit:false}
	       ];
	   
	   $scope.callMethod = function(){
	   		angular.forEach($scope.myData, function(item, index){
	   			$scope.selections.push(item);
	   			
	   		});
	   		
	   };	  

	   $scope.addRow = function() {
	         $scope.myData.push(
	         	{
			       'DMA_Region':'0',	       
			       'DMA_Name':'Empty',
			       'Ad_Unit_Size':'Empty',
			       'Quantity':'0',
			       'Rate':'$0.00',
			       'Amount':'$0.00'
			     }

	         	);
	   };	  
	   
	   $scope.removeRow = function() {
	         $scope.myData.delete({name: 'Empty', age: 0});
	   };


	   $scope.bookOrder = function() {
		        //alert($scope.myData.length);
		        console.log("book order for..");
	   			//console.log($scope.myData);
	   			allocateInventry($scope.myData);
	   			
	   };
	   
	   
	   $rootScope.safeApply = function( fn ) {
		    var phase = this.$root.$$phase;
		    if(phase == '$apply' || phase == '$digest') {
		        if(fn) {
		            fn();
		        }
		    } else {
		        this.$apply(fn);
		    }
	  };
		
	   $scope.aggFC = function (row) {
		    var res = 0;
		    var calculateChildren = function(cur) {
		      var res = 0;
		      var remaining;
		      angular.forEach(cur.children, function(a) {
		        remaining = a.getProperty('fields.Amount');
		        if (remaining) { res += remaining; }
		      });
		      return res;
		    };

		    var calculateAggChildren = function(cur) {
		      var res = 0;
		      res += calculateChildren(cur);
		      angular.forEach(cur.aggChildren, function(a) {
		        res += calculateAggChildren(a);
		      });
		      return res;
		    };

		    return (calculateAggChildren(row) / 3600).toFixed(2);
		} 
	   
	  /*  $scope.getSum = function(table_array){
			return (_.reduce(table_array, function(memo, num){ return memo + num }, 0))
	   };
  	
	   $scope.processed_data = _.map($scope.myData,function(character){
         return {
				name: character.name,
				scores: character.scores,
				total: $scope.getSum(character.scores)
			}
      }); */
  }
   
  
   
           
   
</script>
    
</body>
 <jsp:include page="js.jsp"/>
<jsp:include page="googleAnalytics.jsp"/>
</html>
