<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
 
<script>	 
	function loadRichMediaEventPopup(id) {
		
		 var richMediaEventPopupList;
		 var lineItemName = $('#'+id+'_title').html();
		 richMediaPopupOuterDivContent = '';
		 
		 $('#richMediaPopupOuterDiv').html('');
		 $('#myModalLabel1').html(lineItemName);
		 $('#richMediaEventPopupLoader').css('display','block');
		 $('#myModalRichMedia').modal('show');
		 
		 try{
	 		 $.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadRichMediaEventPopup.lin",
	 		       cache: false,
	 		      data : {
	 		    	  lineItem : lineItemName,
			    	  startDate:startDate,
			    	  endDate:endDate
			    	  },
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {
	 		        	  if(index == 'richMediaEventPopupList') {
	 		        		 if(element != null && element.length  > 0) {
	 		        			var length = element.length;
	 		        			var whiteBackGroundheight = (Math.round(length/2))*190;
	 		        			$('#white').css("min-height",whiteBackGroundheight+"px");
	 		        			 richMediaEventPopupList=element;
	 		        			for(i=0; i<length; i++) {
	 		        				var event = richMediaEventPopupList[i];
	 		        				createRichMediaPopupEventBox(event.customizedCustomEvent, event.value, event.customEvent);
	 		        			}
	 		        		 }
	 		        		 else {
	 		        			richMediaPopupOuterDivContent = '<center><h3>No Events</h3></center>';
	 		        		 }
						  }
	 		           });
	 		          $('#richMediaEventPopupLoader').css('display','none');
	 		          $('#richMediaPopupOuterDiv').append(richMediaPopupOuterDivContent);
	 		       },
	 		       error: function(jqXHR, error) {
	 		    	  $('#richMediaEventPopupLoader').css('display','none');
	 		    	  $('#richMediaPopupOuterDiv').append('<center><h3>No Events</h3></center>');
	 		        }
	 		  });
			}catch(exception){
			}
	 }
	
	function loadRichMediaEventGraph() {
		 $('#richMediaEventGraphOuterDiv').html('');
		 $('#selectRichMediaEventGraphMetricsDiv').css('display','none');
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
	 		        		 richMediaGraphTable=element;
						  }
	 		        	 if(index == 'customEvents') {
	 		        		customEvents=element;
	 		        	 }
	 		           });
	 		    	   drawVisualization('Market', 0);
	 		       },
	 		       error: function(jqXHR, error) {
	 		    	  $('#richMediaEventGraphOuterDiv').html('');
	 		        }
	 		  });
			}catch(exception){
			}
	 }


    function drawVisualization(selectedMetricsText,selectedMetrics) {
    	$('#richMediaEventGraphOuterDiv').html('');
    	if(richMediaGraphTable != null && richMediaGraphTable.length  >0 && customEvents != null && customEvents.length  >0) {
    		$('#selectRichMediaEventGraphMetricsLabel').html(selectedMetricsText+'<span class="caret" style="margin-left: 6px;margin-top: 8px;"></span>');
    		$('#selectRichMediaEventGraphMetricsDiv').css('display','block');
			for(i=0; i<customEvents.length; i++) {
				var customEventId = customEvents[i].id;
				var customEvent = customEvents[i].value;
		    	var richMediaEventGraphAnalyticBoardId = customEventId+'AnalyticsBoard';
		    	var richMediaEventGraphTotalId = customEventId+'Total';
		    	var richMediaEventGraphPieChartId = customEventId+'PieChart';
		    	var richMediaEventGraphDetailsId = customEventId+'Details';
		    	
		    	var divContent = '<div name="richMediaEventGraphAnalyticsBoard" class="analytics-boards" id="'+richMediaEventGraphAnalyticBoardId+'" style="display: block;">'
				+'<div class="board-container" style="min-height:335px;"><div class="board has-chart" style="padding: 0px;"><div class="board-outer">'
				+'<h2 class="account-color board-inner"><div style="display: block;" class="richMediaEventGraphTotal" id="'+richMediaEventGraphTotalId+'"></div></h2>'
				+'</div><div class="chart-area"><div id="dashboard"><div class="rich-chart-area" id="'+richMediaEventGraphPieChartId+'"></div></div>'
				+'</div><div class="filter_data board-filter"><article><div><div style="display: block;" class="richMediaEventGraphDetail" id="'+richMediaEventGraphDetailsId+'"></div>'
				+'</div></article></div></div></div></div>';
				
		    	$('#richMediaEventGraphOuterDiv').append(divContent);
		      // Prepare the data
		      var data = google.visualization.arrayToDataTable(eval("["+richMediaGraphTable+"]"));
		      var formatter = new google.visualization.NumberFormat({pattern:'#,###.##%'});
		      formatter.format(data, 8); // Apply formatter to second column
		      
		      var customEventView = new google.visualization.DataView(data);
		      customEventView.setRows(customEventView.getFilteredRows([{column: 3, value:customEvent}]));
		
		      // Define a Pie chart
		      var richMediaEventGraphPieChart = new google.visualization.ChartWrapper({
		        'chartType': 'PieChart',
		        'containerId': richMediaEventGraphPieChartId,
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
		        
		        'dataTable' : google.visualization.data.group(customEventView, [selectedMetrics],
		                  [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
		        
		      });
		      richMediaEventGraphPieChart.draw();
		
		      var richMediaEventGraphDetails = new google.visualization.ChartWrapper({
		          'chartType': 'Table',
		          'containerId': richMediaEventGraphDetailsId,
		          'options': {
		              'width': '100%'
		          },
		          'dataTable':google.visualization.data.group(customEventView, [selectedMetrics], 
		                    [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
		      }); 
		      richMediaEventGraphDetails.draw();
		
		      // Create a dashboard
		   var richMediaEventGraphTotal = new google.visualization.ChartWrapper({
		          'chartType': 'Table',
		          'containerId': richMediaEventGraphTotalId,
		          'options': {
		              'width': '100%'
		          },
		          'dataTable':google.visualization.data.group(customEventView, [i+5], 
		                    [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
		      }); 
		      richMediaEventGraphTotal.draw();
		      
		  //  var tablen = new google.visualization.Table(document.getElementById('test_dataview'));
		      // tablen.draw(view); 
		       
		       /* // Define a slider control for the Age column.
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
		       }); */
		   
		      /*  new google.visualization.Dashboard(document.getElementById('dashboard')).
		          // Establish bindings, declaring the both the slider and the category
		          // picker will drive both charts.
		          //bind([SitecategoryPicker, FormatcategoryPicker, MarketcategoryPicker, AdSizecategoryPicker, CouponcategoryPicker], [pie, table]).
		          bind(SitecategoryPicker, FormatcategoryPicker).
		              bind(FormatcategoryPicker, MarketcategoryPicker).
		              bind(MarketcategoryPicker, AdSizecategoryPicker).
		              bind(AdSizecategoryPicker, CouponcategoryPicker).
		              bind(CouponcategoryPicker, [table]).
		          // Draw the entire dashboard.
		         
		          draw(data);  */
		          
		       /* // Define a table
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
		       }); */
		      
		      
		      /* google.visualization.events.addListener(table, 'ready',
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
		                                       //chart_core_ctr.draw();
		                                       //chart_core_ctr_total.draw();
		                                       
		                                  }); */
		                                 
		     //  var table = new google.visualization.Table(document.getElementById('table1'));
		     // table.draw(data1, null);   
		     /*  var richMediaBoxEmptyCount = 0;
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
		      richMediaBoxEmptyCount = 0; */
			}
		}
		else {
			$('#selectRichMediaEventGraphMetricsDiv').css('display','none');
			countEmptyDataTables();
		}
      
    }

 //   google.setOnLoadCallback(drawVisualization);
    
    
    function createRichMediaPopupEventBox(eventTitle, eventValue, tooltip) {
    	richMediaPopupOuterDivContent = richMediaPopupOuterDivContent + '<div title="'+tooltip+'" class="analytics-boards" style="float:left;margin-right:15px;margin-top:15px;margin-left:10px;margin-bottom:10px;width:333px;">'
        +'<div class="board-container" style="width:46%;margin-right:6%;min-height:50px;">'
        +'<div class="board has-chart" style="padding:0px;min-height:50px;">'
        +'<div style="padding:0px;text-align:center;background-color:grey;min-height:38px">'
        +'<h2 class="account-color"  style="color:white;width:100%;word-wrap:break-word;background-color:grey;margin:0px;text-align:center;min-height:30px;">'+eventTitle+'</h2>'
        +'</div>'
        +'<div class="chart-area" style="min-height:115px;">' 
        +'<div class="big-number" style="margin-left: 0px;margin-top:20px;color:black;width:100%;text-align:center;font-size:60px;">'+eventValue+'</div>'
        +'</div></div></div></div>';
    	/*$('#richMediaPopupOuterDiv').append(divContent);*/
    }
</Script>