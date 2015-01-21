"use strict";


google.load('visualization', '1', {packages: ['corechart']});
 
 
/*var myApp = myApp || angular.module("my-app",["google-chart"]);
 
myApp.controller("IndexCtrl",  function($scope){    
    $scope.data1 = {};
    $scope.data1.dataTable = new google.visualization.DataTable();
    $scope.data1.dataTable.addColumn("string","Name")
    $scope.data1.dataTable.addColumn("number","Qty")
    $scope.data1.dataTable.addRow(["Test",1]);
    $scope.data1.dataTable.addRow(["Test2",2]);
    $scope.data1.dataTable.addRow(["Test3",3]);
    $scope.data1.title="My Pie"
 
    $scope.data2 = {};
    $scope.data2.dataTable = new google.visualization.DataTable();
    $scope.data2.dataTable.addColumn("string","Name")
    $scope.data2.dataTable.addColumn("number","Qty")
    $scope.data2.dataTable.addRow(["Test",1]);
    $scope.data2.dataTable.addRow(["Test2",2]);
    $scope.data2.dataTable.addRow(["Test3",3]);
 
 
    $scope.data3 = {};
    $scope.data3.dataTable = new google.visualization.DataTable();
    $scope.data3.dataTable.addColumn("string","Name")
    $scope.data3.dataTable.addColumn("number","Qty")
    $scope.data3.dataTable.addRow(["Test",1]);
    $scope.data3.dataTable.addRow(["Test2",2]);
    $scope.data3.dataTable.addRow(["Test3",3]);
});

*/

var dashBoardApp = angular.module("dashBoardApp",["google-chart"]);

dashBoardApp.controller("dashBoardCtrl", function($scope) {
	  alert('hi')
// Create the dashboard.
	  $scope.dashboard = new google.visualization.Dashboard();
	  
// Prepare the data.
	  $scope.dashboard.data1 = {};
	  $scope.dashboard.data1 = {"cols": [
			        {id: "date", label: "Date", type: "string"},
			        {id: "lin.khon-id", label: "lin.khon", type: "number"},
			        {id: "lin.krqe-id", label: "lin.krqe", type: "number"},
			        {id: "lin.ksnt-id", label: "lin.ksnt", type: "number"},
			        {id: "lin.ksnw-id", label: "lin.ksnw", type: "number"},
			        {id: "lin.kxan-id", label: "lin.kxan", type: "number"},
			        {id: "lin.wala-id", label: "lin.wala", type: "number"},
			        {id: "lin.wane-id", label: "lin.wane", type: "number"},
			        {id: "lin.wavy-id", label: "lin.wavy", type: "number"},
			        {id: "lin.wdtn-id", label: "lin.wdtn", type: "number"},
			        {id: "lin.wiat-id", label: "lin.wiat", type: "number"},
			        {id: "lin.wjcl-id", label: "lin.wjcl", type: "number"},
			        {id: "lin.wwlp-id", label: "lin.wwlp", type: "number"}
      		], 
			    "rows": [
					{c:[{v:"2013-10-10"},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0}]},
					{c:[{v:"2013-10-11"},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0}]},
					{c:[{v:"2013-10-15"},{v:0.1491350169},{v:0.1514386673},{v:0.1941747573},{v:0.0689179876},{v:0.0826873385},{v:0},{v:0.0524952754},{v:0.1054481547},{v:0.1377884947},{v:0},{v:0},{v:0.0824628917}]},
					{c:[{v:"2013-10-16"},{v:0.1526884066},{v:0.1446968601},{v:0.2538071066},{v:0.0555864369},{v:0.0862520408},{v:0},{v:0.0753336203},{v:0.0684150513},{v:0.121901666},{v:0},{v:0.3067484663},{v:0.0862122052}]},
					{c:[{v:"2013-10-17"},{v:0.149750416},{v:0.1172463849},{v:0.1858736059},{v:0.1019021739},{v:0.064951132},{v:0},{v:0.0909936507},{v:0.1474684581},{v:0.1211754014},{v:0.3851091142},{v:0},{v:0.1210090291}]},
					{c:[{v:"2013-10-18"},{v:0.1341531582},{v:0.1112612255},{v:0.4827031376},{v:0.2780094523},{v:0.1165301436},{v:0},{v:0.0764044944},{v:0.1384767557},{v:0.1637073416},{v:0},{v:0},{v:0.1239157373}]},
					{c:[{v:"2013-10-19"},{v:0.094144229},{v:0.1009081736},{v:0.0905797101},{v:0.0710732054},{v:0.0451161741},{v:0},{v:0.0968992248},{v:0.1581920904},{v:0.0559284116},{v:0},{v:0},{v:0.1398444929}]},
					{c:[{v:"2013-10-20"},{v:0.1006947941},{v:0.1469063256},{v:0.3628447025},{v:0.1370801919},{v:0.0923809987},{v:0},{v:0.0643924427},{v:0.1237623762},{v:0.1081431816},{v:0.2590673575},{v:0},{v:0.1220256254}]},
					{c:[{v:"2013-10-21"},{v:0.1709626512},{v:0.0928433269},{v:0.5006257822},{v:0.1156737999},{v:0.1346680433},{v:0},{v:0.0718364564},{v:0.0552181115},{v:0.1081665765},{v:0},{v:0.3717472119},{v:0.0963133394}]},
					{c:[{v:"2013-10-22"},{v:0.0688231246},{v:0.1161440186},{v:0.5384063173},{v:0.2019182231},{v:0.0987209203},{v:0},{v:0.0736105999},{v:0.1929437707},{v:0.1455074573},{v:0},{v:0},{v:0.1235548495}]},
					{c:[{v:"2013-10-23"},{v:0.2060641743},{v:0.0636942675},{v:0},{v:0.406504065},{v:0.2312138728},{v:0},{v:0.1727115717},{v:0.185528757},{v:0.1179245283},{v:0},{v:0},{v:0.1314924392}]},
				]

  
  };

// Define a category picker for the 'Metric' column.
	  $scope.dashboard.categoryPicker = new google.visualization.ControlWrapper({
 'controlType': 'CategoryFilter',
 'containerId': 'control1',
 'options': {
   'filterColumnLabel': 'Metric',
   'ui': {
     'allowTyping': false,
     'allowMultiple': true,
     'selectedValuesLayout': 'belowStacked'
   }
 },
 // Define an initial state, i.e. a set of metrics to be initially selected.
 'state': {'selectedValues': ['CPU', 'Memory']}
});

// Define a gauge chart.
	  $scope.dashboard.gaugeChart = new google.visualization.ChartWrapper({
 'chartType': 'LineChart',
 'containerId': 'chart1',
 'options': {
   'width': 400,
   'height': 180
 }
});



});
