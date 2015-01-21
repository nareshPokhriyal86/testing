
	
  var map;
  var layer;
  var infoWindow;
  var creativeSizeArray=[];
  var MOBILE_CREATIVE_SIZE_ARRAY=['300x50', '320x50', '300x250', '728x90', '768x1024'];
  var MERGE_TABLE_ID='1gNCPNqGLkdjph9-yPUPfI-b8nRd69XC8hm-iOtw'; //1Fhnl0uOC3Ol3rMHI5FQIiTDIP3gw7QbRjldvFgY
  var ALL_DMA_TABLE_ID='1QTnVfSO-agR4ueOAVa-uUzYrmNwE4DQ6l0e22EA';
  var LIN_MEDIA_SITES_TABLE_ID='1beufGaWmpduGw4VWgsKudkGcZ34AvcQEFplw51E';
  var API_KEY='AIzaSyBSW_nbk0I2AlVO3eE6wWt2lwALTTrzbWg';
  var currentDMAData=null; // JSON object having dma table data
  
  $(window).load(function() {		  
		 //fetchAllCreativeSizeFromFusionTable();
	     appendDataToSelectDropDown(MOBILE_CREATIVE_SIZE_ARRAY,'creativeSize');	
  });
  
  function initialize() {
	  try{
		  google.maps.visualRefresh = true;
		    map = new google.maps.Map(document.getElementById('map_canvas'), {
		      center: new google.maps.LatLng(37.82495120302931, -96.50708650000001),
		      zoom: 4,
		      disableDoubleClickZoom:true,
		      mapTypeId: google.maps.MapTypeId.ROADMAP
		    });
		  
		    layer = new google.maps.FusionTablesLayer({
					  map: map,
					  heatmap: { enabled: false },
					  suppressInfoWindows:true

			});
			
		    infoWindow = new google.maps.InfoWindow();

		    google.maps.event.addListener(layer, 'click', function(e) {
		    	infoWindowControl(e, infoWindow, map);
		    });

		
	  }catch(err){
		  console.log('error on map::'+err);
	  }
    
		
  }

  //Open the info window at the click on layer with customized template  
  function infoWindowControl(e, infoWindow, map) {
	  
	currentDMAData = new Object();
	currentDMAData.DMA_Region = ""+e.row['code'].value;
	currentDMAData.DMA_Name = ""+e.row['name'].value;
	currentDMAData.Ad_Unit_Size=""+e.row['Creative_Size'].value;
	currentDMAData.Available_Quantity=""+e.row['Available_Impressions'].value;
	currentDMAData.Quantity="0";
	currentDMAData.Rate="4.00";
	currentDMAData.Amount="0.00";
		
	var infoWindowHtml =
			"<div id='infoWindowDiv' class='infoWindowDiv'>" +
			"<div class='contentRow'><span id='head'>DMA Region Code :</span> " 
			   + e.row['code'].value+"</div>"
			+"<div class='contentRow'><span id='head'>DMA Name :</span>" 
			   + e.row['name'].value +"</div>"
			//+"<div class='contentRow'><span id='head'>Publisher :</span>" + e.row['Publisher_Name'].value +"</div>"
			+"<div class='contentRow'><span id='head'>Creative Size :</span>" 
			   + e.row['Creative_Size'].value +"</div>"
			+"<div class='contentRow'><span id='head'>Impressions Available : </span><span id='availableImpression'>" 
			   + e.row['Available_Impressions'].value+"</span></div>"
			+"<div class='contentRow'><span id='head'>Quantity : </span>" +
					"<input class='inputDiv' type='text' id='quantity'  name='quantity' value='0' /></div>"
			+"<div class='contentRow'><span id='head'>Rate : &nbsp;&nbsp;&nbsp;&nbsp; $ </span>" +
					"<input class='inputDiv' type='text' name='rate' value='4.00'/></div>"
			+"<br>"
			+"<div style='background-color:#FDEFBC;'>"
         	+"<span id='reserveButton' class='btn btn-inverse medium' data-toggle='tab' style='font-weight:bold;margin-bottom:8px;margin-left:10px;margin-top:10px;'" 
         	+" onclick='bindInfoWindowToAngular()'>Reserve</span>"	            
         	+"</div><br></div>"; 

	//e.infoWindowHtml=infoWindowHtml;
	
	infoWindow.setContent(infoWindowHtml);
	infoWindow.setPosition(e.latLng);
	infoWindow.open(map);
	
    setTimeout(function(){
    	checkInfoWindowData(e.row['code'].value,e.row['Creative_Size'].value);
    },1000);
    
  }

  google.maps.event.addDomListener(window, 'load', initialize);
  
  function fetchDataFromFusionTable(){
   try{	
	    var creativeSize=$('#creativeSize').val();	   
	    var queryStr="'Start_Date' >= '"+startDate+"' AND 'End_Date' <= '"+endDate+"' " 		
			    
	    if(creativeSize !=null && creativeSize != "-1"){
			queryStr=queryStr +" AND 'Creative_Size' = '"+creativeSize+"'"
		}else{			
			queryStr=queryStr +" AND 'Creative_Size' IN ('300x50', '320x50', '300x250', '728x90', '768x1024')"
		}	
	    
	  	var fusionOptions = {			  
			  query: {
				select: 'Geometry',				
				from: MERGE_TABLE_ID,
				where: queryStr
			  },			 
        			 
            styles: [{
				  
            	 where: 'Available_Impressions > 0 AND Available_Impressions <= 10000',
				  polygonOptions: {
					fillColor: '#ff0000',
					fillOpacity: 0.4
				  }
				}, {
				  where: 'Available_Impressions > 10000 AND Available_Impressions <=100000',
				  polygonOptions: {
					fillColor: '#ffff00',
					fillOpacity: 0.4
				  }
				},{
				  where: 'Available_Impressions > 100000',
				  polygonOptions: {
					fillColor: '#008000',
					fillOpacity: 0.4
				  }
			 }]			
        
			
		}
	  
	    layer.setOptions(fusionOptions);
	
	}catch(err){
	  console.log('error in fetchDataFromFusionTable:'+err);
	}	
  } 
  
  
  function fetchAllDMAs(){
	   try{			
		   
		    var creativeSize=$('#creativeSize').val();	   
		    var queryStr="" 		
			if(creativeSize !=null && creativeSize != "-1"){
				queryStr=queryStr +" 'Creative_Size' = '"+creativeSize+"'"
			}else{
				//queryStr=queryStr +" 'Creative_Size' IN ("+MOBILE_CREATIVE_SIZE_ARRAY+")"
				queryStr=queryStr +" 'Creative_Size' IN ('300x50', '320x50', '300x250', '728x90', '768x1024')"
			}	  
		    //alert(queryStr);
		    var fusionOptions = {			  
				  query: {
					select: "Geometry",
					from: MERGE_TABLE_ID,   //ALL_DMA_TABLE_ID,
					where: queryStr
				  },			 
	              styles: [{				  
					  where: 'Available_Impressions > 0 AND Available_Impressions <= 10000',
					  polygonOptions: {
						fillColor: '#ff0000',
						fillOpacity: 0.4
					  }
					}, {
					  where: 'Available_Impressions > 10000 AND Available_Impressions <=100000',
					  polygonOptions: {
						fillColor: '#ffff00',
						fillOpacity: 0.4
					  }
					},{
					  where: 'Available_Impressions > 100000',
					  polygonOptions: {
						fillColor: '#008000',
						fillOpacity: 0.4
					  }
				 }]			
				 
			}
		  
		    layer.setOptions(fusionOptions);
		  
		}catch(err){
			console.log('error in fetchAllDMAs:'+err);
		}	
  }
  
  function fetchLinMediaSitesViaFusionTable(){
	   try{	
		   
		   var queryStr=""		    
		   var fusionOptions = {			  
				  query: {
					select: 'Address',				
					from: LIN_MEDIA_SITES_TABLE_ID,
					where: queryStr
				  },
				  styles: [{					  
					  where: '',
					  markerOptions: {
						iconName:'large_red'
					  }
				  }]
		   }		  
		   layer.setOptions(fusionOptions);
		
		}catch(err){
			console.log('error in fetchLinMediaSitesViaFusionTable:'+err);
		}	
  }
  
  function fetchAllCreativeSizeFromFusionTable(){
	 try{	
		  
		 var queryURL="https://www.googleapis.com/fusiontables/v1/query?sql="+
			"SELECT Creative_Size FROM "+ MERGE_TABLE_ID 
			+" GROUP BY Creative_Size &key="+API_KEY
			
		 $.ajax({
				type : "GET",
				url : queryURL,
				cache : false,
				data : {
					
				},	
				dataType : 'json',
				success : function(data) {
					var rows=data['rows'];
					//creativeSizeArray=rows;
					//alert(rows.length+" and "+rows[0]);
					appendDataToSelectDropDown(rows,'creativeSize');					
					
				},
				error : function(jqXHR, exception) {
					alert("ajax failed to load creative size:"+exception);					
				}
		});			
	 }catch(err){
		 alert(err);
	 }	  
  } 
  
  function appendDataToSelectDropDown(dataArray,elementId){
	  if(dataArray !=null && dataArray.length>0){
		  $('#'+elementId).find('option').remove().end()
		    .append('<option value="-1">Select Creative Size</option>');

		  $.each(dataArray, function( index, value ) {	
				$('#'+elementId).append(new Option(value, value));
		  });
	  }
  }
  
  function hideDatePicker(){
		if(lastPopUpId != undefined){
			$('#'+lastPopUpId).popover('hide');
		}
		
		$(".daterangepicker").css({'display':'none'});
  }