      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawAreaGraph);
      function drawAreaGraph(dataStr,title,graphWidth,graphheight,hAxisTitle,titleTextStyle,divName) {
    	  
    	
    	var json_arr = eval( '(' + dataStr + ')');

        var data = google.visualization.arrayToDataTable(json_arr);
        
        var options = {
          title: title,
		  width: graphWidth,
		  height: graphheight,
          hAxis: {title: hAxisTitle,  titleTextStyle: {color: 'red'}},
	      legend:{position: 'none'}
        };

        var chart = new google.visualization.AreaChart(document.getElementById(divName));
        chart.draw(data, options);
      }