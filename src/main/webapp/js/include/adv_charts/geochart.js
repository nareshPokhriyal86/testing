var map; 
google.load('visualization', '1', {'packages': ['geochart', 'corechart']});
google.setOnLoadCallback(drawRegionsMap); 

function drawRegionsMap() {
    var data = google.visualization.arrayToDataTable([
      ['State',   'Impression', 'CTR(%)'],
['Rhode Island',5,20.00],
['Indiana',25,8.00],
['District of Columbia',16,6.25],
['Michigan',41,4.88],
['Georgia',82,3.66],
['North Dakota',29,3.45],
['Maryland',37,2.70],
['Illinois',83,2.41],
['South Carolina',57,1.75],
['Virginia',65,1.54],
['Texas',75659,0.40],
['California',265,0.38],
['Florida',421,0.24],
['Unknown',41501,0.16],
['Colorado',157,0.00],
['New York',106,0.00],
['Missouri',73,0.00],
['Oklahoma',66,0.00],
['Washington',65,0.00],
['Nevada',64,0.00],
['New Mexico',62,0.00],
['Louisiana',60,0.00],
['Arizona',47,0.00],
['North Carolina',45,0.00],
['Tennessee',44,0.00],
['Wisconsin',44,0.00],
['Ohio',42,0.00],
['Minnesota',32,0.00],
['Pennsylvania',31,0.00],
['Kentucky',28,0.00],
['Nebraska',24,0.00],
['Mississippi',23,0.00],
['Massachusetts',19,0.00],
['Oregon',19,0.00],
['Hawaii',18,0.00],
['Iowa',16,0.00],
['Kansas',16,0.00],
['Utah',15,0.00],
['Alaska',14,0.00],
['New Jersey',14,0.00],
['Arkansas',13,0.00],
['New Hampshire',13,0.00],
['Alabama',11,0.00],
['Connecticut',8,0.00],
['Maine',4,0.00],
['Montana',3,0.00],
['Idaho',2,0.00],
['South Dakota',2,0.00],
['West Virginia',1,0.00],
['Wyoming',1,0.00]
    ]);

    var options = {
        region: 'US',
        displayMode: 'markers',
        colorAxis: {colors: ['red', 'green'],
	displayMode: 'auto'}
      };

    var chart = new google.visualization.GeoChart(document.getElementById('geomap'));
    chart.draw(data, options);
};

var map; 
google.load('visualization', '1', {'packages': ['geochart', 'corechart']});
google.setOnLoadCallback(drawRegionsMap1); 
function drawRegionsMap1() {
    var data = google.visualization.arrayToDataTable([
      ['State',   'lin-property', 'CTR(%)'],
       ['New Mexico',      'lin.kxan',    0.31]
       
    ]);

    var options = {
        region: 'US',
        displayMode: 'markers',
        colorAxis: {colors: ['red', 'green'],
	displayMode: 'auto'}
      };

    var chart = new google.visualization.GeoChart(document.getElementById('geomap1'));
    chart.draw(data, options);
};

