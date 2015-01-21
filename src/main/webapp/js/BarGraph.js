      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawBarGraph);
      function drawBarGraph(divName,title,dataStr,vAxisTitle,vAxisColor) {
    	  var json_arr = eval( '(' + dataStr + ')');
    	  var data = google.visualization.arrayToDataTable(json_arr);
        
        var options = {
          title: title,
          vAxis: {title: vAxisTitle,  titleTextStyle: {color: vAxisColor}}
        };

        var chart = new google.visualization.BarChart(document.getElementById(divName));
        chart.draw(data, options);
      }