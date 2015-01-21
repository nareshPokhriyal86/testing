var proposalId = '<s:property value="proposalDTO.proposalId" />';
var placementData = '<s:property escape="false" value="placementMap"/>';
var app = angular.module('mediaPlannerApp',['ngGrid']);

app.controller('gridDataCtrl', function($scope) {
	$scope.selectedRows = [];
	$scope.proposalId = proposalId;
	$scope.deleteRowTemplate = "<div><a ng-click='deleteRow($index)' style='cursor: hand; cursor: pointer; '><i class='icon-minus-sign'></i></a></div>";
	$scope.updateCellTemplate = "<input type='text' ng-class=\"'colt' + col.index\" ng-input='COL_FIELD' ng-blur='update(col, row, cellValue)' ng-model='cellValue'/>";
	//var checkboxCellTemplate='<div ng-repeat="adServer in adServers"><input type="checkbox" id="{{row.rowIndex}}{{adServer}}" value={{adServer}} ng-checked="cbVal(row, $index)" />{{adServer}}</div>';
	
	$scope.totalBudgetAllocation = '$0.00';
	$scope.totalProposedImpression = '0';
	$scope.totalEffectiveCPM = '$0.00';
	$scope.totalNetCost = '$0.00';
	$scope.totalGrossRevenue = '$0.00';
	$scope.totalPublisherPayout = '$0.00';
	$scope.totalServingFees = '$0.00';
	$scope.totalNetRevenue = '$0.00';
	$scope.totalMarginPercent = '$0.00';
	
	if(placementData != null && placementData != undefined) {
		$scope.myData = eval(placementData.placement);
	}
	if($scope.myData == null || $scope.myData == undefined || $scope.myData.length == 0) {
		$scope.myData = [{
	  		'proposalId':$scope.proposalId,
		    'placementName':'',
		    'placementId' : '',
		    'createdBy' : '',
		    'createdOn'	 : '',
		    'site':'',
		    'publisherCPM':'$0.00',
		    'budgetAllocation':'$0.00',
		    'proposedImpression':'0',
		    'marginPercent':'0.00%',
		    'effectiveCPM':'$0.00',
		    'firstPartyAdServerCost':'$0.00',
		    'thirdPartyAdServerCost':'$0.00',
		    'netCostCPM':'$0.00',
		    'netCost':'$0.00',
		    'grossRevenue':'$0.00',
		    'publisherPayout':'$0.00',
		    'servingFees' :'$0.00',
		    'netRevenue':'$0.00'
			}];
	}
	/*$scope.myData = [
	  {
		'proposalId':$scope.proposalId,
	    'placementName':'Weather.com',
	    'placementId' : '',
	    'createdBy' : '',
	    'createdOn'	 : '',
	    'site':'Weather.com',
	    'publisherCPM':'$5.00',
	    'budgetAllocation':'$3,000.00',
	    'proposedImpression':'397,614',
	    'marginPercent':'50.00%',
	    'effectiveCPM':'$7.55',
	    'firstPartyAdServerCost':'$0.03',
	    'thirdPartyAdServerCost':'$0.00',
	    'netCostCPM':'$5.03',
	    'netCost':'$2,000.00',
	    'grossRevenue':'$3,000.00',
	    'publisherPayout':'$1,988.07',
	    'servingFees' :'$11.93',
	    'netRevenue':'$1,000.00',
	  },
	  {
		'proposalId':$scope.proposalId,
	    'placementName':'USA Today',
	    'placementId' : '',
	    'createdBy' : '',
	    'createdOn'	 : '',
	    'site':'USA Today',
	    'publisherCPM':'$8.40',
	    'budgetAllocation':'$3,000.00',
	    'proposedImpression':'284,698',
	    'marginPercent':'25.00%',
	    'effectiveCPM':'$10.54',
	    'firstPartyAdServerCost':'$0.03',
	    'thirdPartyAdServerCost':'$0.00',
	    'netCostCPM':'$08.43',
	    'netCost':'$2,400.00',
	    'grossRevenue':'$3,000.00',
	    'publisherPayout':'$2,391.46',
	    'servingFees' :'$8.54',
	    'netRevenue':'$600.00',
	  },
	  {
		'proposalId':$scope.proposalId,
	    'placementName':'xAd',
	    'placementId' : '',
	    'createdBy' : '',
	    'createdOn'	 : '',
	    'site':'xAd',
	    'publisherCPM':'$4.00',
	    'budgetAllocation':'$5,000.00',
	    'proposedImpression':'992,556',
	    'marginPercent':'25.00%',
	    'effectiveCPM':'$5.04',
	    'firstPartyAdServerCost':'$0.03',
	    'thirdPartyAdServerCost':'$0.00',
	    'netCostCPM':'$4.03',
	    'netCost':'$4,000.00',
	    'grossRevenue':'$5,000.00',
	    'publisherPayout':'$3,970.22',
	    'servingFees' :'$29.78',
	    'netRevenue':'$1,000.00',
	  },
	  {
		'proposalId':$scope.proposalId,
	    'placementName':'Tribune RON - ROS',
	    'placementId' : '',
	    'createdBy' : '',
	    'createdOn'	 : '',
	    'site':'Tribune RON - ROS',
	    'publisherCPM':'$3.00',
	    'budgetAllocation':'$800.00',
	    'proposedImpression':'211,221',
	    'marginPercent':'25.00%',
	    'effectiveCPM':'$3.79',
	    'firstPartyAdServerCost':'$0.03',
	    'thirdPartyAdServerCost':'$0.00',
	    'netCostCPM':'$3.03',
	    'netCost':'$640.00',
	    'grossRevenue':'$800.00',
	    'publisherPayout':'$633.66',
	    'servingFees' :'$6.34',
	    'netRevenue':'$160.00',
	  },
	  {
		'proposalId':$scope.proposalId,
	    'placementName':'Tribune RON - Sports',
	    'placementId' : '',
	    'createdBy' : '',
	    'createdOn'	 : '',
	    'site':'Tribune RON - Sports',
	    'publisherCPM':'$5.00',
	    'budgetAllocation':'$70.00',
	    'proposedImpression':'11,133',
	    'marginPercent':'25.00%',
	    'effectiveCPM':'$6.29',
	    'firstPartyAdServerCost':'$0.03',
	    'thirdPartyAdServerCost':'$0.00',
	    'netCostCPM':'$5.03',
	    'netCost':'$56.00',
	    'grossRevenue':'$70.00',
	    'publisherPayout':'$55.67',
	    'servingFees' :'$0.33',
	    'netRevenue':'$14.00',
	  },
	  {
		'proposalId':$scope.proposalId,
	    'placementName':'cnn.com',
	    'placementId' : '',
	    'createdBy' : '',
	    'createdOn'	 : '',
	    'site':'cnn.com',
	    'publisherCPM':'$8.04',
	    'budgetAllocation':'$3,500.00',
	    'proposedImpression':'293,809',
	    'marginPercent':'25.00%',
	    'effectiveCPM':'$11.91',
	    'firstPartyAdServerCost':'$0.03',
	    'thirdPartyAdServerCost':'$1.10',
	    'netCostCPM':'$9.53',
	    'netCost':'$2,800.00',
	    'grossRevenue':'$3,500.00',
	    'publisherPayout':'$2,468.00',
	    'servingFees' :'$332.00',
	    'netRevenue':'$700.00',
	  },
	  {
		'proposalId':$scope.proposalId,
	    'placementName':'tribune.com',
	    'placementId' : '',
	    'createdBy' : '',
	    'createdOn'	 : '',
	    'site':'tribune.com',
	    'publisherCPM':'$9.00',
	    'budgetAllocation':'$7,000.00',
	    'proposedImpression':'552,813',
	    'marginPercent':'25.00%',
	    'effectiveCPM':'$12.66',
	    'firstPartyAdServerCost':'$0.03',
	    'thirdPartyAdServerCost':'$1.10',
	    'netCostCPM':'$10.13',
	    'netCost':'$5,600.00',
	    'grossRevenue':'$7,000.00',
	    'publisherPayout':'$4,975.32',
	    'servingFees' :'$624.68',
	    'netRevenue':'$1,400.00',
	  }
	];*/

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
        /*aggregateTemplate: "<div ng-click=\"row.toggleExpand()\" ng-style=\"rowStyle(row)\" class=\"ngAggregate\">" +
        "    <span class=\"ngAggregateText\"><span class='ngAggregateTextLeading'>{{row.totalChildren()}} {{row.label CUSTOM_FILTERS}} {{entryMaybePlural(row)}}</span><span>Total : {{aggFunc(row)}}</span></span>" +
        "    <div class=\"{{row.aggClass()}}\"></div>" +
        "</div>" +
        "",*/
        columnDefs: 'myDefs',
        footerTemplate: '<div id="commonTotal" style="display:inline;">Budget:{{totalBudgetAllocation}}&nbsp&nbsp&nbsp&nbsp Impression:{{totalProposedImpression}}&nbsp&nbsp&nbsp&nbsp Margin:{{totalMarginPercent}}&nbsp&nbsp&nbsp&nbsp eCPM:{{totalEffectiveCPM}}&nbsp&nbsp&nbsp&nbsp</div>'+
        				'<div id="costTotal" style="display:inline;">Cost:{{totalNetCost}}&nbsp&nbsp&nbsp&nbsp </div><div id="revenueTotal" style="display:none;">Gross Revenue:{{totalGrossRevenue}}&nbsp&nbsp&nbsp&nbsp Payout:{{totalPublisherPayout}}&nbsp&nbsp&nbsp&nbsp'+
        				'Serving Fees:{{totalServingFees}}&nbsp&nbsp&nbsp&nbsp Net Revenue:{{totalNetRevenue}}</div>'
    };
	
	$scope.IOgridOptions = {
	        data: 'myData',
			enableRowSelection: true,
			multiSelect: true,
	        enableRowReordering: false,
			enableCellSelection: true,
			enableCellEditOnFocus: false,
			enableCellEdit: false,
	        showGroupPanel: true,
	        showFooter: true,
	        showSelectionCheckbox : true,
	        enableColumnResize: true,
	        selectedItems: $scope.selectedRows,
	        columnDefs: 'IOColDefs',
	        footerTemplate: '<div id="IOcommonTotal" style="display:inline;">Budget:{{totalBudgetAllocation}}&nbsp&nbsp&nbsp&nbsp Impression:{{totalProposedImpression}}&nbsp&nbsp&nbsp&nbsp Margin:{{totalMarginPercent}}&nbsp&nbsp&nbsp&nbsp eCPM:{{totalEffectiveCPM}}&nbsp&nbsp&nbsp&nbsp</div>'+
							'<div id="IOcostTotal" style="display:inline;">Cost:{{totalNetCost}}&nbsp&nbsp&nbsp&nbsp </div><div id="IOrevenueTotal" style="display:none;">Gross Revenue:{{totalGrossRevenue}}&nbsp&nbsp&nbsp&nbsp Payout:{{totalPublisherPayout}}&nbsp&nbsp&nbsp&nbsp'+
							'Serving Fees:{{totalServingFees}}&nbsp&nbsp&nbsp&nbsp Net Revenue:{{totalNetRevenue}}</div>'
	        /*footerTemplate: '<div>Total Budget : {{totalBudgetAllocation}}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTotal Impression : {{totalProposedImpression}}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTotal Effective CPM : {{totalEffectiveCPM}}</div>'+
	        				'<div>Total Net Cost : {{totalNetCost}}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTotal Gross Revenue : {{totalGrossRevenue}}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTotal Publisher Payout : {{totalPublisherPayout}}</div>'+
	        				'<div>Total Serving Fees : {{totalServingFees}}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTotal Net Revenue : {{totalNetRevenue}}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTotal Margin % : {{totalMarginPercent}}</div>'*/
	    };
	
	$scope.aggregateRow = function() {
		var deleteRowIndex = -1;
        var selectedRow;
        var length = $scope.myData.length;
        var budget_Allocation = 0;
        var impression = 0;
        var effectiveCPM = 0;
        var netCost = 0;
        var grossRevenue = 0;
        var publisherPayout = 0;
        var servingFees = 0;
        var netRevenue = 0;
        var margin = 0;
        for(var i=0; i<length; i++) {
        	selectedRow  = $scope.myData[i];
        	if(selectedRow.placementName == 'Total') {
        		deleteRowIndex = i;
        	}
        	else {
	        	budget_Allocation= budget_Allocation + parseFloat((selectedRow.budgetAllocation).replaceAll('$','').replaceAll(',',''));
	        	impression = impression + parseInt((selectedRow.proposedImpression).replaceAll(',',''));
	        	effectiveCPM = effectiveCPM + parseFloat((selectedRow.effectiveCPM).replaceAll('$','').replaceAll(',',''));
	        	netCost = netCost + parseFloat((selectedRow.netCost).replaceAll('$','').replaceAll(',',''));
	        	grossRevenue = grossRevenue + parseFloat((selectedRow.grossRevenue).replaceAll('$','').replaceAll(',',''));
	        	publisherPayout = publisherPayout + parseFloat((selectedRow.publisherPayout).replaceAll('$','').replaceAll(',',''));
	        	servingFees = servingFees + parseFloat((selectedRow.servingFees).replaceAll('$','').replaceAll(',',''));
	        	margin = parseFloat((selectedRow.marginPercent).replaceAll('%','').replaceAll(',',''));
        	}
    	}
        if(length > 0) {
        	effectiveCPM = effectiveCPM/length;
        	netRevenue = grossRevenue - (publisherPayout + servingFees);
        	margin = budget_Allocation - netCost;
        }
        
        $scope.totalBudgetAllocation = '$'+formatFloat(budget_Allocation,2);
    	$scope.totalProposedImpression = formatInt(impression);
    	$scope.totalEffectiveCPM = '$'+formatFloat(effectiveCPM,2);
    	$scope.totalNetCost = '$'+formatFloat(netCost,2);
    	$scope.totalGrossRevenue = '$'+formatFloat(grossRevenue,2);
    	$scope.totalPublisherPayout = '$'+formatFloat(publisherPayout,2);
    	$scope.totalServingFees = '$'+formatFloat(servingFees,2);
    	$scope.totalNetRevenue = '$'+formatFloat(netRevenue,2);
    	$scope.totalMarginPercent = '$'+formatFloat(margin,2);
        
        /*if(deleteRowIndex >= 0) {
        	$scope.myData[deleteRowIndex].budgetAllocation = '$'+formatFloat(budget_Allocation,2);
        	$scope.myData[deleteRowIndex].proposedImpression = formatInt(impression);
        	$scope.myData[deleteRowIndex].effectiveCPM = '$'+formatFloat(effectiveCPM,2);
        	$scope.myData[deleteRowIndex].netCost = '$'+formatFloat(netCost,2);
        	$scope.myData[deleteRowIndex].grossRevenue = '$'+formatFloat(grossRevenue,2);
        	$scope.myData[deleteRowIndex].publisherPayout = '$'+formatFloat(publisherPayout,2);
        	$scope.myData[deleteRowIndex].servingFees = '$'+formatFloat(servingFees,2);
        	$scope.myData[deleteRowIndex].netRevenue = '$'+formatFloat(netRevenue,2);
        	$scope.myData[deleteRowIndex].marginPercent = '$'+formatFloat(margin,2);
        }
        else {
	        $scope.myData.push(
		  	{
	  		'proposalId':$scope.proposalId,
		    'placementName':'Total',
		    'site':'',
		    'publisherCPM':'',
		    'budgetAllocation':'$'+formatFloat(budget_Allocation,2),
		    'proposedImpression':formatInt(impression),
		    'marginPercent':'$'+formatFloat(margin,2),
		    'effectiveCPM':'$'+formatFloat(effectiveCPM,2),
		    'firstPartyAdServerCost':'',
		    'thirdPartyAdServerCost':'',
		    'netCostCPM':'',
		    'netCost':'$'+formatFloat(netCost,2),
		    'grossRevenue':'$'+formatFloat(grossRevenue,2),
		    'publisherPayout':'$'+formatFloat(publisherPayout,2),
		    'servingFees' : '$'+formatFloat(servingFees,2),
		    'netRevenue':'$'+formatFloat(netRevenue,2),
			});
		}*/
    };
	
	$scope.costDefs = function(){
		$('#revenueTotal').css("display", "none");
		$('#costTotal').css("display", "inline");
		$scope.myDefs =
		[
		{ field: 'delete_row', displayName: '', headerCellTemplate:'', enableCellEditOnFocus: false, enableCellEdit: false, width:'20px', cellTemplate:$scope.deleteRowTemplate},
		{ field: 'placementName', displayName: 'Placement Name', width:'140px', cellClass:'editableCell'},
		{ field: 'site', displayName: 'Site', width:'100px', cellClass:'editableCell'},
		{ field: 'publisherCPM', displayName: 'Publisher CPM', width:'120px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'budgetAllocation', displayName: 'Budget ($)', width:'100px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'proposedImpression', displayName: 'Impressions', width:'120px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'marginPercent', displayName: 'Margin %', width:'80px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'effectiveCPM', displayName: 'Effective CPM', width:'120px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'firstPartyAdServerCost', displayName: 'FP Server Cost', width:'120px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'thirdPartyAdServerCost', displayName: 'TP Server Cost', width:'140px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'netCostCPM', displayName: 'Net Cost (CPM)', width:'140px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'netCost', displayName: 'Net Cost ($)', width:'140px', enableCellEditOnFocus: false, enableCellEdit: false}
		];
	};
	$scope.revenueDefs = function(){
		$('#costTotal').css("display", "none");
		$('#revenueTotal').css("display", "inline");
		$scope.myDefs =
		[
		{ field: 'delete_row', displayName: '', headerCellTemplate:'', enableCellEditOnFocus: false, enableCellEdit: false, width:'20px', cellTemplate:$scope.deleteRowTemplate},
		{ field: 'placementName', displayName: 'Placement Name', width:'140px', cellClass:'editableCell'},
		{ field: 'site', displayName: 'Site', width:'100px', cellClass:'editableCell'},
		{ field: 'publisherCPM', displayName: 'Publisher CPM', width:'120px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'budgetAllocation', displayName: 'Budget ($)', width:'100px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'proposedImpression', displayName: 'Impressions', width:'120px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'marginPercent', displayName: 'Margin %', width:'80px', cellClass:'editableCell', editableCellTemplate: $scope.updateCellTemplate},
		{ field: 'effectiveCPM', displayName: 'Effective CPM', width:'120px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'grossRevenue', displayName: 'Gross Revenue', width:'120px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'publisherPayout', displayName: 'Payout', width:'80px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'servingFees', displayName: 'Serving Fees', width:'110px', enableCellEditOnFocus: false, enableCellEdit: false},
		{ field: 'netRevenue', displayName: 'Net Revenue', width:'110px', enableCellEditOnFocus: false, enableCellEdit: false}
		];
	};
	$scope.IOcostDefs = function(){
		$('#IOrevenueTotal').css("display", "none");
		$('#IOcostTotal').css("display", "inline");
		$scope.IOColDefs =
		[
		{ field: 'placementName', displayName: 'Placement Name', width:'140px'},
		{ field: 'site', displayName: 'Site', width:'100px'},
		{ field: 'publisherCPM', displayName: 'Publisher CPM', width:'120px'},
		{ field: 'budgetAllocation', displayName: 'Budget ($)', width:'100px'},
		{ field: 'proposedImpression', displayName: 'Impressions', width:'120px'},
		{ field: 'marginPercent', displayName: 'Margin %', width:'80px'},
		{ field: 'effectiveCPM', displayName: 'Effective CPM', width:'120px'},
		{ field: 'firstPartyAdServerCost', displayName: 'FP Server Cost', width:'120px'},
		{ field: 'thirdPartyAdServerCost', displayName: 'TP Server Cost', width:'140px'},
		{ field: 'netCostCPM', displayName: 'Net Cost (CPM)', width:'140px'},
		{ field: 'netCost', displayName: 'Net Cost ($)', width:'140px'}
		];
	};
	$scope.IOrevenueDefs = function(){
		$('#IOcostTotal').css("display", "none");
		$('#IOrevenueTotal').css("display", "inline");
		$scope.IOColDefs =
		[
		{ field: 'placementName', displayName: 'Placement Name', width:'140px'},
		{ field: 'site', displayName: 'Site', width:'100px'},
		{ field: 'publisherCPM', displayName: 'Publisher CPM', width:'120px'},
		{ field: 'budgetAllocation', displayName: 'Budget ($)', width:'100px'},
		{ field: 'proposedImpression', displayName: 'Impressions', width:'120px'},
		{ field: 'marginPercent', displayName: 'Margin %', width:'80px'},
		{ field: 'effectiveCPM', displayName: 'Effective CPM', width:'120px'},
		{ field: 'grossRevenue', displayName: 'Gross Revenue', width:'120px'},
		{ field: 'publisherPayout', displayName: 'Payout', width:'80px'},
		{ field: 'servingFees', displayName: 'Serving Fees', width:'110px'},
		{ field: 'netRevenue', displayName: 'Net Revenue', width:'110px'}
		];
	};
	$scope.costDefs();
	$scope.IOcostDefs();
	$scope.aggregateRow();
	
	 $scope.addRow = function() {
		 //$scope.deleteLastRow();
		 $scope.myData.push(
		  	{
	  		'proposalId':$scope.proposalId,
		    'placementName':'',
		    'placementId' : '',
		    'createdBy' : '',
		    'createdOn'	 : '',
		    'site':'',
		    'publisherCPM':'$0.00',
		    'budgetAllocation':'$0.00',
		    'proposedImpression':'0',
		    'marginPercent':'0.00%',
		    'effectiveCPM':'$0.00',
		    'firstPartyAdServerCost':'$0.00',
		    'thirdPartyAdServerCost':'$0.00',
		    'netCostCPM':'$0.00',
		    'netCost':'$0.00',
		    'grossRevenue':'$0.00',
		    'publisherPayout':'$0.00',
		    'servingFees' :'$0.00',
		    'netRevenue':'$0.00',
			});
		 $scope.aggregateRow();
	 };
	 
    $scope.update = function(column, row, cellValue) {
        //alert("row.entity: "+row.entity+", column.field : "+column.field+", cellValue : "+cellValue);
    	if(cellValue != undefined) {
	    	cellValue = cellValue.replaceAll('$','');
	    	cellValue = cellValue.replaceAll(' ','');
	    	if(cellValue != '' && !(isNaN(cellValue))) {
	    		if(column.field == 'marginPercent') {
	    			cellValue = formatFloat(cellValue,2)+'%';
	    		} else {
	    			cellValue = '$'+formatFloat(cellValue,2);
	    		}
		        row.entity[column.field] = cellValue;
		        
		        var budget = row.entity['budgetAllocation'];
		        	budget = parseFloat(budget.replaceAll('$','').replaceAll(',',''));
		        var publisherCPM = row.entity['publisherCPM'];
		        	publisherCPM = parseFloat(publisherCPM.replaceAll('$','').replaceAll(',',''));
		        var marginPercent=row.entity['marginPercent'];
		        	marginPercent = parseFloat(marginPercent.replaceAll('%','').replaceAll(',',''));
		    	var firstPartyAdServerCost=row.entity['firstPartyAdServerCost'];
		    		firstPartyAdServerCost = parseFloat(firstPartyAdServerCost.replaceAll('$','').replaceAll(',',''));
		    	var thirdPartyAdServerCost=row.entity['thirdPartyAdServerCost'];
		    		thirdPartyAdServerCost=parseFloat(thirdPartyAdServerCost.replaceAll('$','').replaceAll(',',''));		    		
		    	var netCost = 0;
		    	var netCostCPM = 0;
		    	var proposedImpression = 0;
		    	var effectiveCPM = 0;
		    	var grossRevenue = 0;
		    	var publisherPayOut = 0;
		    	var servingFees = 0;
		    	var netRevenue = 0;
		    	
		    	netCost = budget/(1 + (marginPercent/100));
		    	netCostCPM = publisherCPM + firstPartyAdServerCost + thirdPartyAdServerCost;
		    	if(netCostCPM > 0) {
		    		proposedImpression = (netCost * 1000)/netCostCPM;
		    	}
		    	if(proposedImpression > 0) {
		    		effectiveCPM = (budget * 1000)/proposedImpression;
		    	}
		    	grossRevenue = (effectiveCPM * proposedImpression)/1000;
		    	publisherPayOut = (proposedImpression * publisherCPM)/1000;
		    	servingFees = (proposedImpression * (netCostCPM - publisherCPM))/1000;
		    	netRevenue = grossRevenue - (publisherPayOut + servingFees);
		    	
		    	netCost = Math.round(netCost * 100) / 100;
		    	netCostCPM = Math.round(netCostCPM * 100) / 100;
		    	proposedImpression = Math.round(proposedImpression * 100) / 100;
		    	effectiveCPM = Math.round(effectiveCPM * 100) / 100;
		    	grossRevenue = Math.round(grossRevenue * 100) / 100;
		    	publisherPayOut = Math.round(publisherPayOut * 100) / 100;
		    	servingFees = Math.round(servingFees * 100) / 100;
		    	netRevenue = Math.round(netRevenue * 100) / 100;
		    	
		    	row.entity['netCost'] = '$'+formatFloat(netCost,2);
		    	row.entity['netCostCPM'] = '$'+formatFloat(netCostCPM,2);
		    	row.entity['proposedImpression'] = formatInt(proposedImpression);
		    	row.entity['effectiveCPM'] = '$'+formatFloat(effectiveCPM,2);
		    	row.entity['grossRevenue'] = '$'+formatFloat(grossRevenue,2);
		    	row.entity['publisherPayout'] = '$'+formatFloat(publisherPayOut,2);
		    	row.entity['servingFees'] = '$'+formatFloat(servingFees,2);
		    	row.entity['netRevenue'] = '$'+formatFloat(netRevenue,2);
		    	
		    	$scope.aggregateRow();
	    	}
    	}
    };
    
    $scope.deleteRow = function(indexVal) {
    	if(this.row.entity['placementName'] != 'Total') {
	        var index = this.row.rowIndex;
	        $scope.gridOptions.selectItem(index, false);
	        $scope.myData.splice(index, 1);
	        $scope.aggregateRow();
    	}
    };
    
    $scope.deleteLastRow = function() {
    	var index = $scope.myData.length - 1;
        $scope.gridOptions.selectItem(index, false);
        $scope.myData.splice(index, 1);
    };
    
    /*
     $scope.cbVal = function (row, index) {
    	var server = $scope.adServers[index];
    	var selectedAdServers = row.entity.AdServer;
    	var length = row.entity.AdServer.length;
    	for(var i=0; i<length; i++) {
    		if(selectedAdServers[i] == server) {
    			return 1;
    		}
    	}
    	return 0;
    };
    
     $scope.aggFunc = function (row) {
        var total = 0;
        angular.forEach(row.children, function(cropEntry) {
          total+=cropEntry.entity.proposedImpression;
        });
        return total.toString();
    };
      
      $scope.entryMaybePlural = function (row) {
        if(row.children.length>1)
        {
          return "entries";
        }
        else
          return "entry";
      };*/
    
});

app.directive('ngBlur', function () {
    return function (scope, elem, attrs) {
        elem.bind('blur', function () {
            scope.$apply(attrs.ngBlur);
        });
    };
});
