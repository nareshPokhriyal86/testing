 
  var map;
  var layer;
  var infoWindow;
  var geoXml;
  var polygonHashTable;
  var creativeSizeArray=[];
  var MOBILE_CREATIVE_SIZE_ARRAY=['300x50', '320x50', '300x250', '728x90', '768x1024'];
  //var MERGE_TABLE_ID= '1cTLtS6TAMB3N9Bv4cfq6BaBgjGBUtN6VR0nxMN8' ;
  var ALL_DMA_TABLE_ID='1QTnVfSO-agR4ueOAVa-uUzYrmNwE4DQ6l0e22EA';
  var LIN_MEDIA_SITES_TABLE_ID='1beufGaWmpduGw4VWgsKudkGcZ34AvcQEFplw51E';
  var API_KEY= 'AIzaSyC3vKMznDaMy9WmxqH4mDroyv3dIrna51c'; //'AIzaSyC80K_M0QJ84WS_k_OudC0DneY8i1PBMeI';
  var OLD_API_KEY='AIzaSyC80K_M0QJ84WS_k_OudC0DneY8i1PBMeI';
  var currentDMAData=null; // JSON object having dma table data
  var processOrder=false;
    
  $(window).load(function() {		  
		  
  });
  
  
  function initialize() {
	   try{
		  google.maps.visualRefresh = true;
		  
		  google.maps.visualRefresh = true;
		  map = new google.maps.Map(document.getElementById('map-canvas'), {
		      center: new google.maps.LatLng(37.82495120302931, -96.50708650000001),
		      zoom: 4,
		      disableDoubleClickZoom:true,
		      mapTypeId: google.maps.MapTypeId.ROADMAP
		  });
		 		
		  infoWindow = new google.maps.InfoWindow();
		 
		  // parse KML			 
		  geoXml = new geoXML3.parser({
                   map: map,
                   singleInfoWindow: false,
                   suppressInfoWindows:true,
                   afterParse: loadDMAsData
		 });
		 
		 
		 var webUrl=location.href;
		 var contexURL=webUrl.substring(0,webUrl.lastIndexOf("/"));
		
		 geoXml.parse(contexURL+'/kml/nielsen_dma.kml');	
		 //geoXml.parse(dmaKMLPath);
		 //geoXml.parse(redirectURL+'/kml/nielsen_dma.kml');	
		
	
	 }catch(err){
	      console.log('error on initialize map::'+err);
	 }
	
  }  

  function loadDMAsData(doc){	
	    polygonHashTable = new HashTable();
		geoXmlDoc = doc[0];
		//console.log(geoXmlDoc);
		for (var i = 0; i < doc[0].gpolygons.length; i++) {		    
		   var polygon = doc[0].gpolygons[i];
		   //set the name of the poly 
		   var placemark = doc[0].placemarks[i];
		   var id = doc[0].placemarks[i].name;
		   polygon.set("id", id );
		   polygon.setVisible(false);
		   var hashKey=id;
		   if(!polygonHashTable.hasItem(hashKey)){	
			   polygonHashTable.setItem(hashKey, polygon);
		   }
		}
 }

function showPolygon(polygon) {
	 polygon.setVisible(true);	   
}
	
function hidePolygon(polygon) {
	 polygon.setVisible(false);
}  

function addClickListenerOnPolygon(polygon){
	 google.maps.event.addListener(polygon,"click",function(e) {
        infoWindowControl(e,polygon,infoWindow, map);	    
	 });
}

google.maps.event.addDomListener(window, 'load', initialize);
		

function infoWindowControl(e, polygon,infoWindow, map) {
	console.log("infoWindowControl.........");
	
	var dmaCode=polygon.get("id");
	var dmaName=polygon.get("name");
	var avlImpression=polygon.get("availableImpression");
	var creativeSize="All";
	currentDMAData = new Object();
	currentDMAData.DMA_Region = dmaCode;
	currentDMAData.DMA_Name = dmaName;
	currentDMAData.Ad_Unit_Size=creativeSize;
	currentDMAData.Available_Quantity=avlImpression;
	currentDMAData.Quantity="0";
	currentDMAData.Rate="4.00";
	currentDMAData.Amount="0.00";
	
	var infoWindowHtml =
			"<div id='infoWindowDiv' class='infoWindowDiv'>" +
			"<div class='contentRow'><span id='head'>DMA Region Code :</span> " 
			   + dmaCode+"</div>"
			+"<div class='contentRow'><span id='head'>DMA Name :</span>" 
			   + dmaName+"</div>"
			+"<div class='contentRow'><span id='head'>Impressions Available : </span><span id='availableImpression'>" 
			   + formatNumber(avlImpression)+"</span></div>"
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
	    checkInfoWindowData(dmaCode,creativeSize);
	 },1000);
 
}



var allDMAWithInventoryArray=[]; 
function loadAllDMAsWithInventory(startDate,endDate){
	  allDMAWithInventoryArray=[];
	  var dataList;
	  /*var fusionTableQueryURL="https://www.googleapis.com/fusiontables/v1/query?sql="+
		  "SELECT code,sum(Available_Impressions) as Available_Impressions, name"+
		  " FROM "+MERGE_TABLE_ID+
		  " WHERE 'Start_Date' >='"+startDate+"' AND 'End_Date' <= '"+endDate+"'"+ 
		  " AND Available_Impressions >=1"+
		  " GROUP BY code,name"+
		  " ORDER BY Available_Impressions&key="+OLD_API_KEY;*/
	  //console.log("loadAllDMAsWithInventory query::"+fusionTableQueryURL);
	  try{				
			 $.ajax({
				 		type : "POST",
					//url : fusionTableQueryURL,
					  url : "/uploadDMAsWithInventory.lin",
					cache : false,
					data : {
						endtDate : endDate,
						startDate : startDate
					},	
					dataType : 'json',
					success : function(data) {	
						$.each(data, function(index, element) {
						/*	if(index=='forcastInventoryDTOList'){
								dataList=data['forcastInventoryDTOList'];
							}*/
							//element=element+"";
							//var rowDataArr=element.split(",");
							var row = new Object();
							//console.log("rowDataArr: "+rowDataArr+"rowDataArr length: "+rowDataArr.length);
							if(element!=null && element!=undefined){								
								row.code=element.code;
								row.availableImpression=element.availableImpressions;								
								row.name=element.name;
								/*if(rowDataArr.length >3){									
									for(var i=3;i<rowDataArr.length;i++){
										row.name=row.name+", "+rowDataArr[i];
									}
								}*/
								
								allDMAWithInventoryArray.push(row);
							}else{
								console.log("loadAllDMAsWithInventory:Invalid response :"+rowDataArr);
							}
						});
						rePaintAllDMAsByInventory();
						
					},
					error : function(jqXHR, exception) {
						alert("loadAllDMAsWithInventory:ajax failed:"+exception);					
					}
			});			
		 }catch(err){
			 alert("loadAllDMAsWithInventory:"+err);
	  }	  
	  return allDMAWithInventoryArray;
}

function rePaintAllDMAsByInventory(){	 
	 console.log("repainting...............all dmas........polygonHashTable.length:"+polygonHashTable.length);
	 
	 if(allDMAWithInventoryArray!=null && allDMAWithInventoryArray.length>0){
		$.each(allDMAWithInventoryArray, function(index, element) {
			var code=element.code;
			//console.log("code:"+code);
			if(polygonHashTable !=null && polygonHashTable.hasItem(code)){
				var polygon=polygonHashTable.getItem(code);				
				var availableImpression=parseInt(element.availableImpression);
				polygon.set("availableImpression",availableImpression);		
				polygon.set("name",element.name);
				
				if(availableImpression <= 10000){
					polygon.setOptions({fillColor: "#ff0000", fillOpacity: 0.5,strokeColor:"#ff0000",strokeWeight:0});
				}else if(availableImpression > 10000 && availableImpression <=100000){
					polygon.setOptions({fillColor: "#ffff00", fillOpacity: 0.5,strokeColor:"#ffff00",strokeWeight:0});
				}else{
					polygon.setOptions({fillColor: "#008000", fillOpacity: 0.5,strokeColor:"#008000",strokeWeight:0});
				}				
				polygon.setVisible(true);
				polygonHashTable.setItem(code,polygon);
				
				addClickListenerOnPolygon(polygon);
			}else{
				console.log("not found.............in polygonHashTable.....code:"+code);				
			}					
		});	
	 }else{
		 console.log("allDMAWithInventoryArray is null, check ajax...");
	 }
  }

 function fetchDataFromFusionTableViaRest(){	 
	 allDMAWithInventoryArray=loadAllDMAsWithInventory(startDate,endDate);	 
 }
  
  function fetchDataFromFusionTable(){
   try{	
	   
	    var creativeSize=$('#creativeSize').val();	   
	    var queryStr="'Start_Date' >= '"+startDate+"' AND 'End_Date' <= '"+endDate+"'" 		
		console.log("Query:"+queryStr);	    
	    /*if(creativeSize !=null && creativeSize != "-1"){
			queryStr=queryStr +" AND 'Creative_Size' = '"+creativeSize+"'"
		}else{			
			queryStr=queryStr +" AND 'Creative_Size' IN ('300x50', '320x50', '300x250', '728x90', '768x1024')"
		}	*/
	    
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
			/*if(creativeSize !=null && creativeSize != "-1"){
				queryStr=queryStr +" 'Creative_Size' = '"+creativeSize+"'"
			}else{
				//queryStr=queryStr +" 'Creative_Size' IN ("+MOBILE_CREATIVE_SIZE_ARRAY+")"
				queryStr=queryStr +" 'Creative_Size' IN ('300x50', '320x50', '300x250', '728x90', '768x1024')"
			}	  */
		    
		    
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
  
  function loadTotalInventory(e, infoWindow, map){
	  var code=e.row['code'].value;
	  var responseData=[];
	  
	  var fusionTableURL="https://www.googleapis.com/fusiontables/v1/query?sql="+
		  "SELECT sum(Available_Impressions) as Available_Impressions,"+ 
		  " DFP_Property_Name,Publisher_Id"+
		  " FROM "+MERGE_TABLE_ID+
		  " WHERE 'Start_Date' >='2014-01-12' AND 'End_Date' <= '2014-01-31'"+
		  " AND Available_Impressions >=1"+
		  " AND code="+code+" GROUP BY DFP_Property_Name,Publisher_Id&key="+OLD_API_KEY;
	  //console.log("loadTotalInventory query::"+fusionTableURL);
	  try{				
			 $.ajax({
					type : "GET",
					url : fusionTableURL,
					cache : false,
					data : {
						
					},	
					dataType : 'json',
					success : function(data) {						
						var rows=data['rows'];	
						$.each(rows, function(index, element) {
							element=element+"";
							var rowDataArr=element.split(",");
							var row = new Object();
							if(rowDataArr.length>=3){
								row.availableImpression=rowDataArr[0];
								row.siteName=rowDataArr[1];
								row.publisherId=rowDataArr[2];
								responseData.push(row);
							}else{
								console.log("loadTotalInventory:Invalid response :"+rowDataArr);
							}
						});
						customInfoWindowControl(e, infoWindow, map,responseData);
						
					},
					error : function(jqXHR, exception) {
						alert("loadTotalInventory:ajax failed:"+exception);					
					}
			});			
		 }catch(err){
			 alert("loadTotalInventory:"+err);
	  }	  
	  return responseData;
  }

  var allocationDataTable;
  var dmaObjectTable=null;
  var dmaObject;
  var publisherWiseSiteMap;
  
  function allocateInventry(data){
	  dmaObjectTable = new HashTable();
	  allocationDataTable=new HashTable();	  
	  publisherWiseSiteMap=new HashTable();
	  
	  console.log("allocate inventory......");
	  //var dmaQuery=" AND code IN ( ";
	  var dmaCode = "";
	  var dmaCodeArray=[];
	  var counter=0;
	  $.each(data, function( index, value ) {	
			//console.log(value);
		    dmaObject=new Object();
			dmaCodeArray.push(value.DMA_Region);
			if(counter==0){
				dmaCode=dmaCode+value.DMA_Region;
			}else{
				dmaCode=dmaCode+","+value.DMA_Region;
			}
			counter++;
			
			dmaObject.code=value.DMA_Region;
			dmaObject.name=value.DMA_Name;
			dmaObject.availableQty=value.Available_Quantity;
			dmaObject.bookedQty=value.Quantity;
			dmaObject.amount=value.Amount;
			dmaObject.rate=value.Rate;
			
			dmaObjectTable.setItem(value.DMA_Region,dmaObject);
			
	  });
	 // dmaQuery=dmaQuery+" ) ";
	  
	  
	/*  var fusionTableURL="https://www.googleapis.com/fusiontables/v1/query?sql="+
	  "SELECT sum(Available_Impressions) as Available_Impressions,code,Publisher_Id,Publisher_Name,DFP_Property_Name"+
	  " FROM "+MERGE_TABLE_ID+
	  " WHERE 'Start_Date' >='"+startDate+"' AND 'End_Date' <= '"+endDate+"'"+
	  dmaQuery +
	  //" AND code="+dmaCodeArray+
	  " GROUP BY code,Publisher_Id,Publisher_Name,DFP_Property_Name" +
	  " ORDER BY code&key="+OLD_API_KEY;*/
	  
	 // console.log("allocateInventry query::"+fusionTableURL);
	  try{				
		 $.ajax({
				type : "GET",
				 url : "/loadAllocateInventry.lin",
				cache : false,
				data : {
					endtDate : endDate,
					startDate : startDate,
					dmaCode : dmaCode
				},	
				dataType : 'json',
				success : function(data) {	
					$.each(data, function(index, element) {
						var row = new Object();
						if(element!=null && element!=undefined){
							row.availableImpression=element.availableImpressions;
							row.code=element.code;
							row.publisherId=element.publisherId;
							row.publisherName=element.publisherName;
							row.siteName=element.dfpPropertyName;
							var hashKey=row.code;
							
							// Make publisher_dma wise data into hash table to get site names
							var publisherWiseKey= row.publisherId+"_"+hashKey;
							if(!publisherWiseSiteMap.hasItem(publisherWiseKey)){
								publisherWiseSiteMap.setItem(publisherWiseKey,row);
							}else{
								var oldRow=publisherWiseSiteMap.getItem(publisherWiseKey);
								var netImpression=parseInt(oldRow.availableImpression)+parseInt(row.availableImpression);
								var siteNames=oldRow.siteName+", "+row.siteName;
								
								row.availableImpression=netImpression;
								row.siteName=siteNames;
								publisherWiseSiteMap.setItem(publisherWiseKey,row);
							}
							
						}else{
							console.log("allocateInventry:Invalid response :"+rowDataArr);
						}
					});
					
					
					var keys=publisherWiseSiteMap.keys();
					// Put dma wise data into hashtable, key is dma while value - array of rows (publisher wise)
					$.each(keys, function(index, element) {	
						 
						  var row=publisherWiseSiteMap.getItem(element);
						  //console.log(row);
						  var hashKey=row.code;
						  if(!allocationDataTable.hasItem(hashKey)){	
								var rowArray=[];
								rowArray.push(row);
								allocationDataTable.setItem(hashKey, rowArray);
						  }else{
								var rowArray=allocationDataTable.getItem(hashKey);
								rowArray.push(row);
								allocationDataTable.setItem(hashKey, rowArray);
						  }
						 
					});					
					  
					// Now do allocation for these
					doAllocation(allocationDataTable,dmaObjectTable);
					
				},
				error : function(jqXHR, exception) {
					console.log("allocateInventry:ajax failed:"+exception);					
				}
		});			
	 }catch(err){
		 console.log("allocateInventry:"+err);
   }
	 
  }
  
  var allocationObject;
  var allocationObjectHashTable;
  
  function doAllocation(allocationHashTable,dmaObjectHashTable){
	  allocationObjectHashTable= new HashTable();
	  
	  var keys=allocationHashTable.keys();
	  $.each(keys, function(index, element) {
		  //console.log("key:"+element+" and index:"+index);
		  var elementArray=allocationHashTable.getItem(element);
		  var dmaObject=dmaObjectHashTable.getItem(element);
		  if(elementArray.length>1){
			  
			  var totalImp=parseInt(dmaObject.availableQty);
			  var bookedQty =parseInt(dmaObject.bookedQty);
			  for(var i=0;i<elementArray.length;i++){
				 allocationObject=new Object;
				  
				 var row=elementArray[i];
				 var publisherId=row.publisherId;
				 var avlQtyByPublisher=parseInt(row.availableImpression);
				 var calculatedBookedQty= Math.round( avlQtyByPublisher*(bookedQty/totalImp));
				 allocationObject.bookedQty=calculatedBookedQty;
				 allocationObject.availableQty=totalImp;
				 allocationObject.publisherId=publisherId;
				 allocationObject.publisherName=row.publisherName;
				 allocationObject.siteName=row.siteName;
				 allocationObject.name=dmaObject.name;
				 allocationObject.rate=dmaObject.rate;
				 allocationObject.amount= ""+parseFloat( calculatedBookedQty*dmaObject.rate /1000).toFixed(2);
				 var hashKey=publisherId+"_"+element;
				 console.log("1.add this object in hashtable.....:"+hashKey);
				 allocationObjectHashTable.setItem(hashKey,allocationObject);
				 
			  }
			  
		  }else{
			  var row=elementArray[0];
			  var publisherId=row.publisherId;
			  console.log("----------------------2------------");
			  dmaObject.publisherId=publisherId;
			  dmaObject.publisherName=row.publisherName;
			  dmaObject.siteName=row.siteName;
			  var hashKey=publisherId+"_"+element;
			  console.log("2.add this object in hashtable.....:"+hashKey);
			  allocationObjectHashTable.setItem(hashKey,dmaObject);
		  }		 
	  });
	  console.log("allocation completes...allocationObjectHashTable length:"+allocationObjectHashTable.length);
	  //toastr.success("Order proccessed successfully");
	  $('#s1').hide();
	  $('#s2').show();
	  initializePropsalScreen();
	  processOrder=true;
  }
  
  function createProposal(){
	  $('#s1').hide();
	  $('#s2').show();
  }
  
  function initializePropsalScreen(){
	  var startArray=startDate.split("-");
	  var flightStartDate=startArray[1]+"-"+startArray[2]+"-"+startArray[0];	  
	  var endArray=endDate.split("-");
	  var flightEndDate=endArray[1]+"-"+endArray[2]+"-"+endArray[0];	  
	  $('#flightStartDate').val(flightStartDate);	  
	  $('#flightEndDate').val(flightEndDate);
	 

	  var geoTargets="";
	  if(dmaObjectTable !=null && dmaObjectTable.length >0){
		  var keys=dmaObjectTable.keys();
		  for(var i=0;i<keys.length;i++){
			  if(i==0){
				  geoTargets= dmaObjectTable.getItem(keys[i]).code;
			  }else{
				  geoTargets=geoTargets+","+dmaObjectTable.getItem(keys[i]).code;
			  }			  
		  }		 
	  }
	  var array=getJSONArryFromJSArray(geoTargets);
	  $("#geoTargets").select2("val",array);
	  $("#geoTargets").select2("disable");
  }
  
 
  function saveProposalAndShowList(){		
	   if(processOrder){
		   var proposalName=$('#proposalName').val();	  
			var placementData = createPlacementData(proposalName);
			
	  	    $('#placementData').val(placementData);
	  	    
	  	    
			var formSubmit=true;
			$('#nextPageControl').val(0);  //0 for ProposalList and 1 for MediaPlan
					
			
			var advertiser=$.trim($('#advertiser').val());
			var agency=$.trim($('#agency').val());
			var industry=$('#industry').val();
			var proposalType=$('#proposalType').val();
			var proposalStatus=$('#proposalStatus').val();
			
			
			var salesRep=$.trim($('#salesRep').val());		
			var kpi=$('#kpi').val();
			var geoTargets=$('#geoTargets').val();
			
			if(proposalName == null || proposalName==''){
				toastr.error('Please provide a campaign name');
				formSubmit=false;
			}else if( advertiser == null || advertiser == undefined || advertiser == '-1' || advertiser == '0' || advertiser == ''){
				toastr.error('Invalid advertiser');
				formSubmit=false;
			}else if( industry == null || industry=='-1' ){
				toastr.error('Invalid industry');
				formSubmit=false;
			}else if( proposalType == null || proposalType=='-1' ){
				toastr.error('Invalid campaign type');
				formSubmit=false;
			}else if( proposalStatus == null || proposalStatus=='-1' ){
				toastr.error('Invalid campaign status');
				formSubmit=false;
			}else if( salesRep == null || salesRep=='' ){
				toastr.error('Invalid Sales Contact Name');
				formSubmit=false;
				$('#salesRep').focus();
			}else if( kpi == null || kpi=='-1' ){
				toastr.error('Invalid kpi');
				formSubmit=false;
			}else if( geoTargets == null || geoTargets=='-1' ){
				toastr.error('Invalid Markets (DMAs)');
				formSubmit=false;
			}/*
			else if( (startDate ==null || startDate == '') || ((endDate ==null || endDate == ''))){
				toastr.error('Invalid dates');
				formSubmit=false;
			}else{
				var date1 = moment(startDate, 'MM-DD-YYYY');
				var date2 = moment(endDate, 'MM-DD-YYYY');
				var diff = date2.diff(date1, 'days'); 
				
				if(diff < 0){
					toastr.error('End Date can not be less than Start Date');
					formSubmit=false;
				}
			}*/
			
			var trafficEmail=$.trim($('#trafficEmail').val());
			var salesEmail=$.trim($('#salesEmail').val());
			
			if(salesEmail !='' && (!IsEmail(salesEmail))){
				toastr.error('Invalid sales email..');
				formSubmit=false;
			}else if(trafficEmail !='' && (!IsEmail(trafficEmail))){
				toastr.error('Invalid traffic email..');
				formSubmit=false;
			}
					
	       if(formSubmit){
	      	  document.forms["proposalForm"].submit();
	    	  toastr.success('Please wait while saving the data.');
	    	  //toastr.success("Order proccessed successfully");
		   } 
	   }else{
		   toastr.error('Please book inventory and process order first.....');
		   showTab(1);
	   }
	    
		
  }	
  
  function createPlacementData(proposalName){
	  var placementData = "{ 'placement' : [";
	  var budget=0;
	  var keys=allocationObjectHashTable.keys();
	 
	  $.each(keys, function(index, element) {
		  var placementObject=allocationObjectHashTable.getItem(element);
		  console.log(placementObject);
		  var dma=placementObject.name;
		  var placementName=proposalName+" | "+placementObject.publisherName+" | "+dma;
		  var bookedImp=placementObject.bookedQty;
		  var amount=placementObject.amount;
		  budget=budget+parseFloat(amount);
		  
		  var rate=placementObject.rate;
		  //console.log("index:"+index);
		  if(index==0){
			  placementData=placementData+"{";
		  }else{
			  placementData=placementData+",{";
		  }
		  //console.log("placementData::"+placementData);
		  
		  placementData=placementData 
		  		+" 'proposalId':'0', "
			    +" 'placementName':'"+placementName+"', "
			    +" 'placementId' : '', "
			    +" 'createdBy' : '', "
			    +" 'createdOn'	 : '', "
			    +" 'site':'"+placementObject.siteName+"', "
			    +" 'publisherCPM':'$"+rate+"', "
			    +" 'budgetAllocation':'$"+amount+"', "
			    +" 'proposedImpression':'"+bookedImp+"', "
			    +" 'marginPercent':'0.00%', "
			    +" 'effectiveCPM':'$"+rate+"', "
			    +" 'firstPartyAdServerCost':'$0.00', "
			    +" 'thirdPartyAdServerCost':'$0.00', "
			    +" 'netCostCPM':'$"+rate+"', "
			    +" 'netCost':'$"+amount+"', "
			    +" 'grossRevenue':'$"+amount+"', "
			    +" 'publisherPayout':'$"+amount+"', "
			    +" 'servingFees' :'$0.00', "
			    +" 'netRevenue':'$0.00' "
			    +" } ";
		  
	  });
	  placementData=placementData+"] }";
	  console.log(placementData);
	  $('#placementData').val(placementData);
	  $('#budget').val(budget);
	  return placementData;
  }
  
  /****************method copied from proposal.js***********/
  
  function chooseAdvertiser(){		
		var found = false;
		var search = $("#advertiser").val();
		if(search !=null && search=='0'){
			$('#customAdvertiserDiv').show();
		}else{
			$('#customAdvertiserDiv').hide();
		}
	}
	
	function chooseAgency(){
		var search = $("#agency").val();
		if(search !=null && search=='0'){
			$('#customAgencyDiv').show();
		}else{
			$('#customAgencyDiv').hide();
		}
	}	
	
	function chooseGeoTargets(){
		var search = $("#geoTargets").val();
		if(search !=null && search.indexOf('0') != -1){	
			$('#customGeoTargetsDiv').show();
		}else{
			$('#customGeoTargetsDiv').hide();
		}
	}
	
	function chooseIndustry(){		
		var search = $("#industry").val();
		if(search !=null && search.indexOf('0') != -1){			
			$('#customIndustryDiv').show();
		}else{
			$('#customIndustryDiv').hide();
		}
	}
	
	function chooseKpi(){
		var search = $("#kpi").val();
		if(search !=null && search.indexOf('0') != -1){	
			$('#customKpiDiv').show();
		}else{
			$('#customKpiDiv').hide();
		}
   }

  function getJSONArryFromJSArray(dataString){
		var array = [];
		if(dataString !=null && dataString !=''){
			var jsArray=dataString.split(",");
			 for (var i in jsArray) {
			    var val=$.trim(jsArray[i]);
			    if(val !=null && val !=''){
			    	array.push(val);
			    }		    
			}
		}
		return array;		 		 
  }
  
  function getAdvertiserAgencyByCompany() {
		var company = $('#company').val();
		if(company == null || company == undefined || $.trim(company) == '' || company == '-1' || company == '0') {
			return true;
		}
		loadAdvertisersByCompany($.trim(company));
		loadAgenciesByCompany($.trim(company));
	}
	
  function loadAdvertisersByCompany(company) {
		var mapObj;
		$('#advertiser').html('');
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadAdvertisersByCompany.lin",
		      cache: false,
		      data : {
		    	  company : company
			    	 },		    
		      dataType: 'json',
		      success: function (data) {
		    	  $.each(data, function(index, element) {
		    		  if (index == 'advertiserMap') {
		    			  mapObj = data[index];
		    	  	  }
		    	  });
		    	  if (mapObj != null  && mapObj != undefined) {
	    			  $.each(mapObj, function( index, value ) {	
	    				  $('#advertiser').append('<option value="'+index+'">'+value+'</option>');
	    			  });
	    			  $('#advertiser').select2("val","-1");
	    		  }
		     },
		     error: function(jqXHR, exception) {
		     }
		   });   
		}catch(error){
		}
	}
	
	function loadAgenciesByCompany(company) {
		var mapObj;
		$('#agency').html('');
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadAgenciesByCompany.lin",
		      cache: false,
		      data : {
		    	  company : company
			    	 },		    
		      dataType: 'json',
		      success: function (data) {
		    	  $.each(data, function(index, element) {
		    		  if (index == 'agencyMap') {
		    			  mapObj = data[index];
		    	  	  }
		    	  });
		    	  if (mapObj != null  && mapObj != undefined) {
	    			  $('#agency').html('<option value="-1">Select Agency</option>');
	    			  $.each(mapObj, function( index, value ) {	
	    				  $('#agency').append('<option value="'+index+'">'+value+'</option>');
	    			  });
	    			  $('#agency').select2("val","-1");
	    		  }
		     },
		     error: function(jqXHR, exception) {
		     }
		   });   
		}catch(error){
		}
	}
	
	function IsEmail(email) {
		var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return regex.test(email);
	}

	function formatNumber(number){
	      number = number.toFixed(2) + '';
	      var x = number.split('.');
	      var x1 = x[0];	      
	      var x2 = x.length > 1 ? '.' + x[1] : '';
	      var rgx = /(\d+)(\d{3})/;
	      while (rgx.test(x1)) {
	          x1 = x1.replace(rgx, '$1' + ',' + '$2');
	      }
	      if(x2 == '.00' ){
	    	  return x1;
	      }else{
	    	  return x1 + x2;
	      }
	 }

 /****************method copied from proposal.js ends***********/
	
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