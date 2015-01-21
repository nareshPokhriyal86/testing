

'use strict';
angular.module('progressChartApp', ['googlechart','tableDataApp']).controller("progressChartCtrl", function ($scope,$rootScope,$window){
	
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
	  
	 $scope.progressChart = function(){
	    	
	    	var progressBarChart = {};
	        progressBarChart.type = "BarChart";
	        progressBarChart.displayed = false;
	        progressBarChart.cssStyle = "height:80%; width:80%;";
	        progressBarChart.data = {"cols": [
	                                {id: "lineItemId", label: "lineItem", type: "string"},
	                                {id: "DeliveredId", label: "Delivered", type: "number"},
	                              //  {id: "ForecastedId", label: "Forecasted", type: "number"},
	                              //  {id: "StatusId", label: "Status", type: "boolean"}
	                                
	                            ], "rows": [
	                                /*{c: [{v: "USAA Military Millennials Mobile | Military Geo Targets | 320x50 In-App"},{v:7000000}]}*/
	                              
	       ]};
	        progressBarChart.options = {
		      		vAxis: {title: "CTR%"},
		      		hAxis: {title: "Message"},
		      		"title":"CTR% by Message",
		      		seriesType: "bars",
		      		//series: {1: {type: "line"}}
		               };

	       
	        $scope.progressBarChart = progressBarChart;
	        //$scope.$digest();
	    }
	
});

angular.module('tableDataApp', ['googlechart','ui.bootstrap']).controller("tableDataCtrl", function ($scope,$rootScope,tableDataService,$compile) {   
	
	 var formatters = {
				"number": [
				      {
				    	  columnNum: 0,		       
				    	  pattern:'###,###'			
				      },
				      {
				       	 columnNum: 1,		       
				         pattern:'###,###'		
				      },
				      {
				    	  columnNum: 2,		       
					      suffix: '%',
						  fractionDigits:4			
				      }
				     ]
	 };
	 
	$scope.initdata = function(){ // initData begin
		try{
			$("#performerLoaderId").show();
			$("#nonPerformerLoaderId").show();
			$("#mostActiveLoaderId").show();
			$("#performanceMetricsLoaderId").show();
			
			$scope.performerData1 = tableDataService.getPerformerData().then(function(performerData) {
				
		        $scope.performerData = [];
		        $scope.nonPerformerData = [];
		    	
		        performerData = performerData+"";
		    	performerData =  performerData.replace(/\\/g, "")
		    	performerData =  performerData.replace('"[{', "[{")
		    	performerData =  performerData.replace('}]"', "}]")
		    	performerData =  '{"rows" :' + performerData +'}';
		    	
		    	$scope.perNonPerData = $scope.$eval(performerData);
		    	
		    	angular.forEach($scope.perNonPerData.rows, function(value, key){
		           	if(value.s4CTR!=undefined && $('#amount2').val()<= value.s4CTR){
		           		$scope.performerData.push(value);
		           	}else{
		           		$scope.nonPerformerData.push(value);
		           	}  
		         });
		         
		    	
		           $scope.topNonPerformerData={
		           		"rows":$scope.nonPerformerData
		           }
		          
		           $scope.topPerformerData={
		           		"rows":$scope.performerData
		           		//,"formatters":formatters
		           }
		    	 
		           $("#performerLoaderId").hide();
				   $("#nonPerformerLoaderId").hide();
					
				  
	        });
		    
		  $scope.mostActiveData = tableDataService.getMostActiveData().then(function(mostActiveData) {
			    $scope.mostActiveLineItemData=[];
			   
		        mostActiveData = mostActiveData+"";
		        mostActiveData =  mostActiveData.replace(/\\/g, "")
			    mostActiveData =  mostActiveData.replace('"[{', "[{")
			    mostActiveData =  mostActiveData.replace('}]"', "}]")
			    mostActiveData =  '{"rows" :' + mostActiveData +'}';
			    $scope.mostActiveLineItemData = $scope.$eval(mostActiveData);
			    var value;
			    var type;
			    $scope.objectArr = [];
			    angular.forEach($scope.mostActiveLineItemData.rows, function(value, key){
	
			    	if( value.pacing>=100){
			    		 value = value.pacing;
			             type =  'success';
			    	}else{
			    		value = value.pacing;
			            type =  'danger';
			    	}
			    	
			    	 $scope.dynamicObject = {
			    		     value: value,
			    		      type: type
			    		    };
			    	 
			    	$scope.objectArr.push($scope.dynamicObject);
			    
			    });
			    $("#mostActiveLoaderId").hide();				
			    	
		   });
		        
		        
		 $scope.deliveryMetricsData = tableDataService.getDeliveryMetricsData().then(function(deliveryMetricsData) {
			     $('#deliveryMetricTable').dataTable().fnClearTable();
			     $scope.performanceByPlacementData=[];
			     deliveryMetricsData = deliveryMetricsData+"";
		    	 deliveryMetricsData =  deliveryMetricsData.replace(/\\/g, "")
				 deliveryMetricsData =  deliveryMetricsData.replace('"[{', "[{")
				 deliveryMetricsData =  deliveryMetricsData.replace('}]"', "}]")
				 deliveryMetricsData =  '{"rows" :' + deliveryMetricsData +'}';	    	 
		    	 
			     $scope.performanceByPlacementData = $scope.$eval(deliveryMetricsData);
		    	 $("#performanceMetricsLoaderId").hide();
			     
			     var data=new Array();
			     try{	
			    	$.each($scope.performanceByPlacementData['rows'], function( index, value ) {	
			    		 var node="<div id='metric"+index+"'  style='cursor:pointer;'><i ng-click='showTraffer(\"metric"+index+"\",\""+index+"\",\""+parseInt($scope.performanceByPlacementData.rows[index].z1lineItemId.replaceAll(',',''))+"\")' class='cus-traffic'></i></div>";
			    		 var subArray=[node];
			    		 $.each(value, function( subIndex, subValue ){
			    			 /*if($.isNumeric( subValue )){
			    				 subValue= $scope.formatNumber(subValue);
			    			 }*/
			    			 subArray.push(subValue);
			    			 
			    		 });
			    		 data.push(subArray);

			 		 
			 	     });
			      
			      $('#deliveryMetricTable').dataTable().fnAddData( data );
			      
			     
                  var tableScope=angular.element(document.getElementById("deliveryMetricTable"));
                  $compile(tableScope.contents())($scope);
			     
                  /*$("#deliveryMetricTable").each(function() {
                      $(this).format({format:"#,###", locale:"us"});
                  });*/
                  
			     }catch(err){
			    	 console.log("deliveryMetricsData:"+err);
			     }
			    		 
		  });
			
		}catch(err){
			console.log("tableDataService:exception:"+err);
		}

  }// init data	ends
	        
  // call it here
  $scope.initdata();
 
  $scope.chartReady = function() {    	
     fixGoogleChartsBarsBootstrap();
  }

  $scope.showMetricPopup=function(index){
	  if(index !=undefined){
		  alert('show metric popup...clicked row index:'+index);
		  console.log($scope.performanceByPlacementData['rows'][index]['c1lineItem']);
	  }
	 
  }	
  function fixGoogleChartsBarsBootstrap() {	  
	  $(".google-visualization-table-table img[width]").each(function(index, img) {
	      $(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
	  });
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
  
  $scope.resetTable=function(){
	  $scope.nonPerformerData = [];
      $scope.performerData = [];
      $('#deliveryMetricTable').dataTable().fnClearTable();
	  $scope.performanceByPlacementData=[];
	  $("#topNonPerformLineItemTable > tbody").html("");
	  $("#topPerformLineItemTable > tbody").html("");
  };

  $scope.changeTable=function(){
	  $scope.nonPerformerData = [];
      $scope.performerData = [];
	  angular.forEach($scope.perNonPerData.rows, function(value, key){
		if(value.s4CTR!=undefined && parseFloat($('#amount2').val())<= value.s4CTR){
      		$scope.performerData.push(value);
      	}else{
      		$scope.nonPerformerData.push(value);
      	}
      	  
      	});
    
      $scope.topNonPerformerData={
      		"rows":$scope.nonPerformerData
      }
      
      $scope.topPerformerData={
      		"rows":$scope.performerData
      }
      
      $scope.$digest();// used to reflect the changes in the table on the basis of slider value 
     }
    
    $scope.formatNumber=function(number){
	      number = number.toFixed(2) + '';
	      var x = number.split('.');
	      var x1 = x[0];	      
	      var x2 = x.length > 1 ? '.' + x[1] : '';
	      var rgx = /(\d+)(\d{3})/;
	      while (rgx.test(x1)) {
	          x1 = x1.replace(rgx, '$1' + ',' + '$2');
	      }
	      if(x2 == '.00' ){
	    	  return x1;
	      }else{
	    	  return x1 + x2;
	      }
	  }


    $scope.showCreative = function() {  
    }
    
    $scope.progressChart = function(index,status){
    	var progressBarChart = {};
    	var forcastedImp = 0;
    	var deliveredImp = 0;
    	forcastedImp = parseInt($scope.performanceByPlacementData.rows[index].c2impressionBooked.replaceAll(',','') - $scope.performanceByPlacementData.rows[index].c4impressionDelivered.replaceAll(',',''));
        deliveredImp = parseInt($scope.performanceByPlacementData.rows[index].c4impressionDelivered.replaceAll(',',''));
    	//alert("forcastedImp "+forcastedImp+"status "+status+"deliveredImp "+deliveredImp);
    	progressBarChart.type = "BarChart";
        progressBarChart.displayed = false;
        progressBarChart.cssStyle = "height:80%; width:80%;";
      	progressBarChart.data = {"cols": [
        	                                  {id: "lineItemId", label: "lineItem", type: "string"},
        	                                  {id: "DeliveredId", label: "Delivered", type: "number"},
        	                                  {id: "ForcastedId", label: "Forcasted", type: "number"},
        	                                  
        	                              ], "rows": [
        	                                  {c: [{v: $scope.performanceByPlacementData.rows[index].c1lineItem},{v:deliveredImp},{v:forcastedImp}]}
        	                                
          ]};
      	
      	 
      	 if(status=="true"){
      		  progressBarChart.options  = {
        		       title: 'DELIVERY INDICATOR',
        		       vAxis: {title: '',  titleTextStyle: {color: 'red'}},
        		 	  width:400,
        		 	  height:200,
        		 	  isStacked:true,
        		 	  colors: ['black','green','red'],
        		 	  legend:{position: 'none'}
        		     };

    	  }else{
    		  progressBarChart.options  = {
    			       title: 'DELIVERY INDICATOR',
    			       vAxis: {title: '',  titleTextStyle: {color: 'red'}},
    			 	  width:400,
    			 	  height:200,
    			 	  isStacked:true,
    			 	  colors: ['black','red','green'],
    			 	  legend:{position: 'none'}
    			     };
    		  

    	}
      	$("#trafficPopup_loader").hide();
      	var scope_progressBarChartDiv=angular.element(document.getElementById('metricsPopupId'));
      	$compile(scope_progressBarChartDiv.contents())($scope);
      	$scope.progressBarChart = progressBarChart;      
       
        
    }
    
    $scope.resetAllTables=function(){
   	     $scope.nonPerformerData = [];
   	     $scope.performerData = [];
   	     $scope.performanceByPlacementData=[];
   	     $scope.mostActiveLineItemData=[];
   	 };

    
    $scope.showTraffer = function(id,index,lineItemId) {
    	var cpm = 0.0;
    	cpm = parseFloat($scope.performanceByPlacementData.rows[index].c8spent.replaceAll(',','')) / parseFloat($scope.performanceByPlacementData.rows[index].c4impressionDelivered.replaceAll(',',''))*1000.0;
        var impressionBooked=$scope.performanceByPlacementData.rows[index].c2impressionBooked;
        var lineItemname = $scope.performanceByPlacementData.rows[index].c1lineItem;
       
    	$scope.contentDiv =
	    	"<div  class='popup_class' id='metricsPopupId'  ng-app='tableDataApp' ng-controller='tableDataCtrl' data-ng-init='progressChart()'>"+
	        "<div id='popover_content_wrapper' style='width:550px;'>"+
	        "<div class='metricPopupWraper' >"+
	        "<div class='metricPopupWraper' >" +
		        "<div class='metricPopupHeader'>" +
		          "<div class='metricPopupHeader1'><b>Campaign Name</b></div>" +
		          "<div class='metricPopupHeader2'><b>Goal Quantity</b></div>" +
		          "<div class='metricPopupHeader3'><b>CPM</b></div>" +
		          "<div class='metricPopupHeader4'><b>Start Date</b></div>" +
		          "<div class='metricPopupHeader5'><b>End Date</b></div>" +
		        "</div>"+
	        "<div class='metricPopupContent'>" +
		        "<div title='"+lineItemname+"' class='metricPopupContent1' id = 'campaign_name'>"+ lineItemname.substring(0,25)+"...</div>" +
		        "<div class='metricPopupContent2' id = 'booked_imp'>"+impressionBooked+"</div>"+
		        "<div class='metricPopupContent3' id='ecpm'>"+parseFloat(cpm).toFixed(2)+"</div>" +
		        "<div class='metricPopupContent4' id = 'start_date'>" +$scope.performanceByPlacementData.rows[index].d1startDateTime+"</div>" +
		        "<div class='metricPopupContent5' id = 'end_date'>"+$scope.performanceByPlacementData.rows[index].d2endDateTime+"</div>" +
		    "</div><br/>"+
	        "<div id='trafficPopup_loader' style='width:550px;height:30px;text-align:center'>" +
	        "<img src='img/loaders/type4/light/46.gif' alt='loader'></div>"+
	        "<div style='width: 500px; height: 220px; margin-left:10px;' google-chart chart='progressBarChart' style='{{chart.cssStyle}}' on-ready='chartReady()' >" +
	        " </div> <div style='height:10px;background-color:#FDEFBC;'></div></div></div></div></div>";

     	var bookedImp = 0;
    	var deliveredImp = 0;
        bookedImp = parseInt($scope.performanceByPlacementData.rows[index].c2impressionBooked.replaceAll(',',''));
    	deliveredImp = parseInt($scope.performanceByPlacementData.rows[index].c4impressionDelivered.replaceAll(',',''));
    	try{
    		$.ajax({
 		       type : "POST",			 		   
 		       url : "/loadLineItemForcastPopupData.lin",
 		       cache: false,
 		       data : {
 		    	 lineItemId : lineItemId,
 		    	 bookedImp : bookedImp,
 		    	 deliveredImp : deliveredImp
 			   },		
 		       dataType: 'json',
 		       success: function (data) {	
 			    	 var scope_progressBarChartDiv=angular.element(document.getElementsByClassName('popup_class')).scope();
 			    	 scope_progressBarChartDiv.safeApply(function(){
 					 scope_progressBarChartDiv.progressChart(index,data.z2forcastStatus);
 				 });
 		       },
 		       error: function(jqXHR, error) {
 		       }
 		  });
    	  
    	  var subTitle="";
    	  var title =  $('#campaignDropDown option:selected').text();		
    	  setTimeout(function(){makeTrafferPopUPDelay(id,title,subTitle, $scope.contentDiv,index)},0);
    	  
    	}catch(err){
    		console.log("showTraffer:err:"+err);
    	}		
		
    }
    
    
function makeTrafferPopUPDelay(id,title,subTitle,contentDiv,index) {
		
	var content='<div id="content">'+contentDiv+'</div>';
    $("#"+id).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;padding-right:20px;word-wrap:break-word;'>"+title+"</span>"+"<br/>"+subTitle);	
	$("#"+id).attr('data-toggle',"popover");

	var options={
			html: true,
            trigger: 'manual',
            content: content,
            placement:'right',
            title:title	            
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
	 $("#trafficPopup_loader").show();
	  
 }

}).factory('tableDataService', function($http) {
	return {
		getPerformerData: function() {
	             //return the promise directly.
	             return $http.get('/performarData.lin?orderId='+campaignId)
	                       .then(function(result) {
	                            //resolve the promise as the data
	                            return result.data;
	                        });
	    },
	    getMostActiveData: function() {
            //return the promise directly.
           return $http.get('/mostActiveData.lin?orderId='+campaignId)
   	
              .then(function(result) {
                          //resolve the promise as the data
                          return result.data;
                      });
        },
        getDeliveryMetricsData: function() {
          //return the promise directly.
         return $http.get('/deliveryMetrics.lin?orderId='+campaignId)
                   .then(function(result) {
                        //resolve the promise as the data
                        return result.data;
                    });
       }
    }
	  
 });
