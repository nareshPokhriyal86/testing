
	  google.load('visualization', '1', {'packages': ['geochart']});
	  google.setOnLoadCallback(drawGeoChart);
	  function drawGeoChart(divName,dataJsonStr,region,displayMode,colorAxisStr){
	         var json_object = eval( '(' + colorAxisStr + ')'); 
	         //alert("dataJsonStr: "+dataJsonStr)
	         var json_arr = eval( '(' + dataJsonStr + ')');
	         //alert("json_arr : "+json_arr);
	   	     var data = google.visualization.arrayToDataTable(json_arr);
		     var options = {
		        region: region,
		        displayMode: displayMode,
		        colorAxis: json_object
		      };
		      var chart =null;
		      $("#"+divName).html("");
		      chart = new google.visualization.GeoChart(document.getElementById(divName));
		      try{
		        chart.draw(data, options);
		      }catch(error){
		    	  alert('Error in geo map:'+error);
		      }
	   };
	   
	   

    
	   
  