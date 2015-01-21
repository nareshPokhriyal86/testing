


function drawLineChart(divName, title, dataStr) {
	var json_arr = eval('(' + dataStr + ')');
	var data = google.visualization.arrayToDataTable(json_arr);

	var options = {
		title : title
	};

	var chart = new google.visualization.LineChart(document.getElementById(divName));
	chart.draw(data, options);
}
