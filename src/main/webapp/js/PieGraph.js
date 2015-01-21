 google.load("visualization", "1", {packages:["corechart"]});
 
 
 google.setOnLoadCallback(drawPieChart);
 
      function drawPieChart(divName,title, dataStr) { 
    	var json_arr = eval( '(' + dataStr + ')');
        var data = google.visualization.arrayToDataTable(json_arr);
        var options = {
          title: title
        };
        var chart = new google.visualization.PieChart(document.getElementById(divName));
        chart.draw(data, options);
        
        
        var item =null;
		google.visualization.events.addListener(chart, 'select', function() {
			  var selection = chart.getSelection();
		  for (var i = 0; i < selection.length; i++) {
			item = selection[i];
			}
		  displayPopup(data.getValue(item.row, 0));
        
      });
	}
      
   /* function displayPopup(title){
    	alert(title)
    	$("#popoverData").attr('data-original-title', "<span style='font-size:14px;font-weight:bold;'>"+title+"</span>| Nov 2012 - Nov 2013 | ROS | Mobile | 728x90<button class='close1' data-dismiss='alert' id='trendclose' style='float:right;'>×</button></br><span style='color:#9ac050;font-size:14px;'><span style='margin-left:2px;'><img src='img/Arrow2Down.png'width='15'></span><span style='margin-left:10px;margin-top:2px;color:red;'>-0.35%</span></span>");
    	$("#popoverData").click();
  	}*/