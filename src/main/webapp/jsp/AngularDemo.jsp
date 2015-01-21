<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s"%>
<html lang="en">
    <head>
         <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.js" type="text/javascript"></script>
         <script src="https://www.google.com/jsapi" type="text/javascript"></script>
         <script src="../js/main.js?v=<s:property value="deploymentVersion"/>" type="text/javascript"></script>
         <script src="../js/ngGoogleCharts.js?v=<s:property value="deploymentVersion"/>" type="text/javascript"></script>
         <style type="text/css">
             .bigGraph {width:500px;height:500px;float:left;}
             .mediumGraph {width:400px;height:400px;float:left;}
             .smallGraph {width:200px;height:200px;float:left;}
         </style>
    </head>
    <body ng-controller="IndexCtrl">
        <div google-chart="PieChart" ng-model="data1" class="bigGraph"></div>
        <div google-chart="BarChart" ng-model="data2" class="mediumGraph"></div>
        <div google-chart="LineChart" ng-model="data3" class="smallGraph"></div>
    </body>
</html>