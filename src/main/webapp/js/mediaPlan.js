 
/*
 *  @author Youdhveer Panwar
 *  MediaPlan js
 *  
 */


/* *********** Global js variables *********************** */
 
 var placementCounter=0; 
 var placementObjectArray=[];
 var siteObjectArray=[];
 var placementIdText="placement_";
 var siteIdText="site_";
 var placementData="";
 var proposalId=$('#proposalId').val();
 var siteCounter=0;
 var adSizeArray=[];
 var adFormatArray=[];
 /* ************** Global js variables ends ***************** */
 
 
/* 
 *  placementObject, which will hold one to many relationship
 *  with siteObject, each placementObject will hold array of siteIds
 *  under it.
 */
 function placementObject(placementId){
	 this.placementId=placementId;
	 this.siteIdArray=[];
	 
	 this.getSiteIdArray=function(){
		 return this.siteIdArray;
	 }
	 this.setSiteIdArray=function(siteIdArray){
		 this.siteIdArray=siteIdArray;
	 }
	 
	 this.getPlacementId=function(){
		 return this.placementId;
	 }
	 this.setPlacementId=function(placementId){
		 this.placementId=placementId;
	 }
 }
 
 /*
  *  @author Youdhveer Panwar
  *  siteObject, contains all ids for a site row
  */
 function siteObject(siteId){
	 this.siteId=siteId;
	 
	 this.getSiteId=function(){
		 return this.siteId;
	 }
	 this.setSiteId=function(siteId){
		 this.siteId=siteId;
	 }
 }
 
 
 $(window).load(function() {    		
		
	  $(document).ready(function(){
			 loadMediaPlanDropDowns();
			 
			$('#internal_cost').click(function(){		
				$('.p1').hide();
				$('.p2').show();
				$('.p3').show();
				$('.p4').show();
				$('.p5').show();
				$('.p6').show();			
				
				$('.publisher').show();
				$('.publisherpayout').hide();
				$('.1party_plan').show();
				$('.3party_plan').show();
				$('.1party').show();
				$('.3party').show();
				$('.1_party_header').show();
				$('.3_party_header').show();
				$('.totalcost').show();
				$('.margin').hide();
				$('.margincpm').hide();
				$('.pricequote').hide();
				$('.bujget').hide();
				$('.imp').hide();
				$('.grossrev').hide();
				$('.netrev').hide();
				$('.availableimp').hide();
				$('.reserved').hide();
				$('.forecasted').hide();
				//$('.proposed').hide();	
			});


			$('#client_price').click(function(){
				$('.p1').hide();
				$('.p2').show();
				$('.p3').show();
				$('.p4').show();
				$('.p5').show();
				$('.p6').show();
				$('.publisher').hide();
				$('.publisherpayout').hide();
				$('.1party_plan').hide();
				$('.3party_plan').hide();
				$('.1party').hide();
				$('.3party').hide();
				$('.1_party_header').hide();
				$('.3_party_header').hide();
				$('.totalcost').show();
				$('.margin').show();
				$('.margincpm').show();
				$('.pricequote').show();
				$('.bujget').show();
				$('.imp').show();
				$('.grossrev').hide();
				$('.availableimp').hide();
				$('.reserved').hide();
				$('.forecasted').hide();
				$('.netrev').hide();
				//$('.proposed').hide();
			});

			$('#Revenue_tab').click(function(){
				$('.p1').hide();
				$('.p2').show();
				$('.p3').show();
				$('.p4').show();
				$('.p5').show();
				$('.p6').show();
				$('.publisher').hide();
				$('.publisherpayout').show();
				$('.1party_plan').hide();
				$('.3party_plan').hide();
				$('.1party').hide();
				$('.3party').hide();
				$('.1_party_header').hide();
				$('.3_party_header').hide();
				$('.totalcost').hide();
				$('.margin').hide();
				$('.margincpm').hide();
				$('.pricequote').hide();
				$('.bujget').hide();
				$('.imp').hide();
				$('.grossrev').show();
				$('.availableimp').hide();
				$('.reserved').hide();
				$('.forecasted').hide();
				$('.netrev').show();
				//$('.proposed').hide();
			});

			
			$('#inv_forecast').click(function(){
				$('.p1').hide();
				$('.p2').show();
				$('.p3').show();
				$('.p4').show();
				$('.p5').show();
				$('.p6').show();
				$('.publisher').hide();
				$('.publisherpayout').hide();
				$('.1party_plan').hide();
				$('.3party_plan').hide();
				$('.1party').hide();
				$('.3party').hide();
				$('.1_party_header').hide();
				$('.3_party_header').hide();
				$('.totalcost').hide();
				$('.margin').hide();
				$('.margincpm').hide();
				$('.pricequote').hide();
				$('.bujget').hide();
				$('.imp').show();
				$('.grossrev').hide();
				$('.availableimp').show();
				$('.reserved').show();
				$('.forecasted').show();
				$('.netrev').hide();
				//$('.proposed').show();
			});

		 });
	  
	  setTimeout(function (){
		  showAllPlacements();
	  },3000);
	  //showAllPlacements();
		
  });
 
 /*
  * It will display a new placement
  */
 function displayResult(obj){
	
	try{
		//alert(placementCounter);
		var placementId=placementIdText+placementCounter;
		var table=document.getElementById("placementTable");
		var row=table.insertRow(1);
		var cell1=row.insertCell(0);
		var cell2=row.insertCell(1);
		var cell3=row.insertCell(2);
		var cell4=row.insertCell(3);
		var cell5=row.insertCell(4);
		var cell6=row.insertCell(5);
		var cell7=row.insertCell(6);
		var cell8=row.insertCell(7);
		var cell9=row.insertCell(8);
		var cell10=row.insertCell(9);
		var cell11=row.insertCell(10);
		var cell12=row.insertCell(11);
		var cell13=row.insertCell(12);
		var cell14=row.insertCell(13);
		var cell15=row.insertCell(14);
		var cell16=row.insertCell(15);
		var cell17=row.insertCell(16);
		var cell18=row.insertCell(17);
		var cell19=row.insertCell(18);
		var cell20=row.insertCell(19);
		var cell21=row.insertCell(20);
		//var cell22=row.insertCell(21);
		
		cell1.innerHTML="";
		cell2.innerHTML="<a  onclick=displaySite(this,'"+placementCounter+"') " +
				"id='add_site' style='cursor: hand; cursor: pointer;float:left; '>" +
				"<i class='icon-plus-sign'></i></a></br>" +
				"<a  onclick='removePlacement(this,"+placementCounter+")' id='remove_placement' " +
				"style='cursor: hand; cursor: pointer;float:left; '>" +
				"<i class='icon-minus-sign'></i></a><input id='"+placementId+"' type='text' class='size'/>" +
				"<input id=pid_"+placementId+" type='hidden' value='' /></br>";

		cell1.className ='p1';
		cell2.className ='p2';
		cell3.className ='p3';
		cell4.className ='p4';
		cell5.className ='p5';
		cell6.className ='p6';

		cell7.className ='publisher';
		cell8.className ='publisherpayout';
		cell9.className ='1party_plan';
		cell10.className ='3party_plan';
		cell11.className ='totalcost';
		cell12.className ='margin';
		cell13.className ='margincpm';
		cell14.className ='pricequote';
		cell15.className ='bujget';
		cell16.className ='imp';
		cell17.className ='grossrev';
		cell18.className ='netrev';
		cell19.className ='forecasted';
		cell20.className ='reserved';
		cell21.className ='availableimp';
		//cell22.className ='proposed';



		if($('.active').hasClass('cost')){
			
			cell1.setAttribute("style", "height:40px;background:#EEEEEE;width: 110px;text-align:center;display:none;");
			cell2.setAttribute("style", "background:#EEEEEE;text-align:center;");
			cell3.setAttribute("style", "background:#EEEEEE;");
			cell4.setAttribute("style", "background:#EEEEEE;width:100px;");
			cell5.setAttribute("style", "background:#EEEEEE;");
			cell6.setAttribute("style", "background:#EEEEEE;");
			cell7.setAttribute("style", "background:#EEEEEE;");
			cell8.setAttribute("style", "background:#EEEEEE;display:none;");
			cell9.setAttribute("style", "background:#EEEEEE;");
			cell10.setAttribute("style", "background:#EEEEEE;");
			cell11.setAttribute("style", "background:#EEEEEE;");
			cell12.setAttribute("style", "background:#EEEEEE;display:none;");
			cell13.setAttribute("style", "background:#EEEEEE;display:none;");
			cell14.setAttribute("style", "display:none;background:#EEEEEE;");
			cell15.setAttribute("style", "display:none;background:#EEEEEE;");
			cell16.setAttribute("style", "display:none;background:#EEEEEE;");
			cell17.setAttribute("style", "display:none;background:#EEEEEE;");
			cell18.setAttribute("style", "display:none;background:#EEEEEE;");
			cell19.setAttribute("style", "display:none;background:#EEEEEE;");
			cell20.setAttribute("style", "display:none;background:#EEEEEE;");
			cell21.setAttribute("style", "display:none;background:#EEEEEE;");
			//cell22.setAttribute("style", "display:none;background:#EEEEEE;");
		}
		
		if($('.active').hasClass('pricing')){
			cell1.setAttribute("style", "height:40px;background:#EEEEEE;width: 110px;text-align:center;display:none;");
			cell2.setAttribute("style", "background:#EEEEEE;text-align:center;");
			cell3.setAttribute("style", "background:#EEEEEE;");
			cell4.setAttribute("style", "background:#EEEEEE;width:100px;");
			cell5.setAttribute("style", "background:#EEEEEE;");
			cell6.setAttribute("style", "background:#EEEEEE;");
			cell7.setAttribute("style", "display:none;background:#EEEEEE;");
			cell8.setAttribute("style", "display:none;background:#EEEEEE;");
			cell9.setAttribute("style", "display:none;background:#EEEEEE;");
			cell10.setAttribute("style", "display:none;background:#EEEEEE;");
			cell11.setAttribute("style", "background:#EEEEEE;");
			cell12.setAttribute("style", "background:#EEEEEE;");
			cell13.setAttribute("style", "background:#EEEEEE;");
			cell14.setAttribute("style", "background:#EEEEEE;");
			cell15.setAttribute("style", "background:#EEEEEE;");
			cell16.setAttribute("style", "background:#EEEEEE;");
			cell17.setAttribute("style", "display:none;background:#EEEEEE;");
			cell18.setAttribute("style", "display:none;background:#EEEEEE;");
			cell19.setAttribute("style", "display:none;background:#EEEEEE;");
			cell20.setAttribute("style", "display:none;background:#EEEEEE;");
			cell21.setAttribute("style", "display:none;background:#EEEEEE;");
			//cell22.setAttribute("style", "display:none;background:#EEEEEE;");
		}
		
		if($('.active').hasClass('Revenue')){
			cell1.setAttribute("style", "height:40px;background:#EEEEEE;width: 110px;text-align:center;display:none;");
			cell2.setAttribute("style", "background:#EEEEEE;text-align:center;");
			cell3.setAttribute("style", "background:#EEEEEE;");
			cell4.setAttribute("style", "background:#EEEEEE;width:100px;");
			cell5.setAttribute("style", "background:#EEEEEE;");
			cell6.setAttribute("style", "background:#EEEEEE;");
			cell7.setAttribute("style", "display:none;background:#EEEEEE;");
			cell8.setAttribute("style", "background:#EEEEEE;");
			cell9.setAttribute("style", "display:none;background:#EEEEEE;");
			cell10.setAttribute("style", "display:none;background:#EEEEEE;");
			cell11.setAttribute("style", "display:none;background:#EEEEEE;");
			cell12.setAttribute("style", "display:none;background:#EEEEEE;");
			cell13.setAttribute("style", "display:none;background:#EEEEEE;");
			cell14.setAttribute("style", "display:none;background:#EEEEEE;");
			cell15.setAttribute("style", "display:none;background:#EEEEEE;");
			cell16.setAttribute("style", "display:none;background:#EEEEEE;");
			cell17.setAttribute("style", "background:#EEEEEE;");
			cell18.setAttribute("style", "background:#EEEEEE;");
			cell19.setAttribute("style", "display:none;background:#EEEEEE;");
			cell20.setAttribute("style", "display:none;background:#EEEEEE;");
			cell21.setAttribute("style", "display:none;background:#EEEEEE;");
			//cell22.setAttribute("style", "display:none;background:#EEEEEE;");
		}
		
		if($('.active').hasClass('inventory')){
			cell1.setAttribute("style", "height:40px;background:#EEEEEE;width: 110px;text-align:center;display:none;");
			cell2.setAttribute("style", "background:#EEEEEE;text-align:center;");
			cell3.setAttribute("style", "background:#EEEEEE;");
			cell4.setAttribute("style", "background:#EEEEEE;width:100px;");
			cell5.setAttribute("style", "background:#EEEEEE;");
			cell6.setAttribute("style", "background:#EEEEEE;");
			cell7.setAttribute("style", "display:none;background:#EEEEEE;");
			cell8.setAttribute("style", "display:none;background:#EEEEEE;");
			cell9.setAttribute("style", "display:none;background:#EEEEEE;");
			cell10.setAttribute("style", "display:none;background:#EEEEEE;");
			cell11.setAttribute("style", "display:none;background:#EEEEEE;");
			cell12.setAttribute("style", "display:none;background:#EEEEEE;");
			cell13.setAttribute("style", "display:none;background:#EEEEEE;");
			cell14.setAttribute("style", "display:none;background:#EEEEEE;");
			cell15.setAttribute("style", "display:none;background:#EEEEEE;");
			cell16.setAttribute("style", "background:#EEEEEE;");
			cell17.setAttribute("style", "display:none;background:#EEEEEE;");
			cell18.setAttribute("style", "display:none;background:#EEEEEE;");
			cell19.setAttribute("style", "background:#EEEEEE;");
			cell20.setAttribute("style", "background:#EEEEEE;");
			cell21.setAttribute("style", "background:#EEEEEE;");
			//cell22.setAttribute("style", "background:#EEEEEE;");
		}
		placementCounter=placementCounter+1;
		placementObjectArray[placementObjectArray.length]=new placementObject(placementId);
		//alert('placementObjectArray.length:'+placementObjectArray.length);
	}catch(error){
		//alert("displayResult () : js Exception:"+error);
	}

 }


/*
 * It will display a new site under a placement
 */
 function displaySite(obj,pIdCounter){	
    try{
    	var placementId=placementIdText+pIdCounter;
    	var siteId=siteIdText+siteCounter;
        //alert('siteIdDiv:'+siteId+" and placementIdDiv:"+placementId);
        
    	var table=document.getElementById("placementTable");
    	var row=table.insertRow(obj.parentNode.parentNode.rowIndex+1);
    	var cell1=row.insertCell(0);
    	var cell2=row.insertCell(1);
    	var cell3=row.insertCell(2);
    	var cell4=row.insertCell(3);
    	var cell5=row.insertCell(4);
    	var cell6=row.insertCell(5);
    	var cell7=row.insertCell(6);
    	var cell8=row.insertCell(7);
    	var cell9=row.insertCell(8);
    	var cell10=row.insertCell(9);
    	var cell11=row.insertCell(10);
    	var cell12=row.insertCell(11);
    	var cell13=row.insertCell(12);
    	var cell14=row.insertCell(13);
    	var cell15=row.insertCell(14);
    	var cell16=row.insertCell(15);
    	var cell17=row.insertCell(16);
    	var cell18=row.insertCell(17);
    	var cell19=row.insertCell(18);
    	var cell20=row.insertCell(19);
    	var cell21=row.insertCell(20);
    	//var cell22=row.insertCell(21);
    	
    	cell1.className ='p1';
    	cell2.className ='p2';
    	cell3.className ='p3';
    	cell4.className ='p4';
    	cell5.className ='p5';
    	cell6.className ='p6';
    	cell7.className ='publisher edit_col';
    	cell8.className ='publisherpayout';
    	cell9.className ='1party';
    	cell10.className ='3party';
    	cell11.className ='totalcost';
    	cell12.className ='margin';
    	cell13.className ='margincpm';
    	cell14.className ='pricequote';
    	cell15.className ='bujget';
    	cell16.className ='imp';
    	cell17.className ='grossrev';
    	cell18.className ='netrev';
    	cell19.className ='forecasted';
    	cell20.className ='reserved';
    	cell21.className ='availableimp';
    	//cell22.className ='proposed';
    	
    	cell1.setAttribute("id","cell_1_"+siteId );
    	cell2.setAttribute("id","cell_2_"+siteId );
    	cell3.setAttribute("id","cell_3_"+siteId );
    	cell4.setAttribute("id","cell_4_"+siteId );
    	cell5.setAttribute("id","cell_5_"+siteId );
    	cell6.setAttribute("id","cell_6_"+siteId );
    	cell7.setAttribute("id","cell_7_"+siteId );
    	cell8.setAttribute("id","cell_8_"+siteId );
    	cell9.setAttribute("id","cell_9_"+siteId );
    	cell10.setAttribute("id","cell_10_"+siteId );
    	cell11.setAttribute("id","cell_11_"+siteId );
    	cell12.setAttribute("id","cell_12_"+siteId );
    	cell13.setAttribute("id","cell_13_"+siteId );
    	cell14.setAttribute("id","cell_14_"+siteId );
    	cell15.setAttribute("id","cell_15_"+siteId );
    	cell16.setAttribute("id","cell_16_"+siteId );
    	cell17.setAttribute("id","cell_17_"+siteId );
    	cell18.setAttribute("id","cell_18_"+siteId );
    	cell19.setAttribute("id","cell_19_"+siteId );
    	cell20.setAttribute("id","cell_20_"+siteId );
    	cell21.setAttribute("id","cell_21_"+siteId );
    	//cell22.setAttribute("id","cell_22_"+siteId );
    	
    	cell2.innerHTML="<a  onclick ='removeSite(this,"+siteCounter+")' id='remove_site' " +
    			"style='cursor: hand; cursor: pointer;float:left; '><i class='icon-minus-sign'></i></a>" +
    			"<input id=sid_"+siteId+" type='hidden' value='' />";
    	
    	    	
    	cell4.innerHTML=getAdSize(siteCounter);
    	cell5.innerHTML=getAdFormatDropDownHTML(siteCounter);
    	
    	cell6.innerHTML=getAdServerCheckBoxHTML(siteCounter);
    	    	
    	cell7.innerHTML="<div id='publisherCPM_"+siteId+"' contenteditable='true' onblur='calculateData(this,1)'  style='text-align: right;'>0</div>";
    	cell9.innerHTML="0.04";
    	cell10.innerHTML="$1.25";
    	cell12.innerHTML="<div id='marginPercent_"+siteId+"' contenteditable='true' onblur='calculateData(this,2)' " +
    					 " style='text-align: right;'>0</div>";
    	cell15.innerHTML="<div id='budget_"+siteId+"' contenteditable='true' onblur='calculateData(this,3)' style='text-align: right;'>0</div>";
    	cell17.innerHTML="";
    	cell18.innerHTML="";
    	
    	cell3.setAttribute("contenteditable","true");
    	cell3.setAttribute("onblur","fetchForecastingData(this)");
    	cell3.setAttribute("onclick","setSiteName(this)");
    	
    	if($('.active').hasClass('cost')){
    		cell1.setAttribute("style", "height:40px;background:white;text-align:center;display:none;");
    		cell2.setAttribute("style", "background:white;text-align:center;");
    		cell3.setAttribute("style", "background:#ffffcc;");
    		cell4.setAttribute("style", "background:white;");
    		cell5.setAttribute("style", "background:white;");
    		cell6.setAttribute("style", "background:white;");
    		cell7.setAttribute("style", "background:#ffffcc;");
    		cell8.setAttribute("style", "background:white;display:none");
    		cell9.setAttribute("style", "background:white;text-align: right;");
    		cell10.setAttribute("style", "background:white;text-align: right;");
    		cell11.setAttribute("style", "background:white;text-align: right;");
    		cell12.setAttribute("style", "background:#ffffcc;display:none;text-align: right;");
    		cell13.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell14.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell15.setAttribute("style", "display:none;background:#ffffcc;text-align: right;");
    		cell16.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell17.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell18.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell19.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell20.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell21.setAttribute("style", "display:none;background:white;text-align: right;");
    		//cell22.setAttribute("style", "display:none;background:white;text-align: right;");
    	}
    	if($('.active').hasClass('pricing')){
    		cell1.setAttribute("style", "height:40px;background:white;text-align:center;display:none;");
    		cell2.setAttribute("style", "background:white;text-align:center;");
    		cell3.setAttribute("style", "background:#ffffcc;");
    		cell4.setAttribute("style", "background:white;");
    		cell5.setAttribute("style", "background:white;");
    		cell6.setAttribute("style", "background:white;");
    		cell7.setAttribute("style", "display:none;background:#ffffcc;text-align: right;");
    		cell8.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell9.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell10.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell11.setAttribute("style", "background:white;text-align: right;");
    		cell12.setAttribute("style", "background:#ffffcc;text-align: right;");
    		cell13.setAttribute("style", "background:white;text-align: right;");
    		cell14.setAttribute("style", "background:white;text-align: right;");
    		cell15.setAttribute("style", "background:#ffffcc;text-align: right;");
    		cell16.setAttribute("style", "background:white;text-align: right;");
    		cell17.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell18.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell19.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell20.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell21.setAttribute("style", "display:none;background:#EEEEEE;text-align: right;");
    		//cell22.setAttribute("style", "display:none;background:#EEEEEE;text-align: right;");
    	}
    	if($('.active').hasClass('Revenue')){
    		cell1.setAttribute("style", "height:40px;background:white;text-align:center;display:none;");
    		cell2.setAttribute("style", "background:white;text-align:center;");
    		cell3.setAttribute("style", "background:#ffffcc;");
    		cell4.setAttribute("style", "background:white;");
    		cell5.setAttribute("style", "background:white;");
    		cell6.setAttribute("style", "background:white;");
    		cell7.setAttribute("style", "display:none;background:#ffffcc;text-align: right;");
    		cell8.setAttribute("style", "background:white;text-align: right;");
    		cell9.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell10.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell11.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell12.setAttribute("style", "display:none;background:#ffffcc;text-align: right;");
    		cell13.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell14.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell15.setAttribute("style", "display:none;background:#ffffcc;text-align: right;");
    		cell16.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell17.setAttribute("style", "background:white;text-align: right;");
    		cell18.setAttribute("style", "background:white;text-align: right;");
    		cell19.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell20.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell21.setAttribute("style", "display:none;background:#EEEEEE;text-align: right;");
    		//cell22.setAttribute("style", "display:none;background:#EEEEEE;text-align: right;");
    	}
    	if($('.active').hasClass('inventory')){
    		cell1.setAttribute("style", "height:40px;background:white;text-align:center;display:none;");
    		cell2.setAttribute("style", "background:white;text-align:center;");
    		cell3.setAttribute("style", "background:#ffffcc;");
    		cell4.setAttribute("style", "background:white;");
    		cell5.setAttribute("style", "background:white;");
    		cell6.setAttribute("style", "background:white;");
    		cell7.setAttribute("style", "display:none;background:#ffffcc;");
    		cell8.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell9.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell10.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell11.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell12.setAttribute("style", "display:none;background:#ffffcc;text-align: right;");
    		cell13.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell14.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell15.setAttribute("style", "display:none;background:#ffffcc;text-align: right;");
    		cell16.setAttribute("style", "background:white;text-align: right;");
    		cell17.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell18.setAttribute("style", "display:none;background:white;text-align: right;");
    		cell19.setAttribute("style", "background:white;text-align: right;");
    		cell20.setAttribute("style", "background:white;text-align: right;");
    		cell21.setAttribute("style", "background:white;text-align: right;");
    		//cell22.setAttribute("style", "background:white;text-align: right;");
    	}	
    	
    	siteCounter = siteCounter+1;
    	siteObjectArray[siteObjectArray.length]=new siteObject(siteId);
    	
    	for(var i=0;i<placementObjectArray.length;i++){
    		//alert(placementObjectArray[i].getPlacementId()+" and placementIdDiv:"+placementId);
    		if(placementObjectArray[i].getPlacementId() == placementId){    			
    			var tempArray=placementObjectArray[i].getSiteIdArray();    			
    			if(tempArray ==null || tempArray.length == 0){    			
    				tempArray=[];
    			}
    			tempArray[tempArray.length]=new siteObject(siteId);
    			placementObjectArray[i].setSiteIdArray(tempArray);
    			break;
    		}
    	}
    }catch(error){
    	//alert("displaySite : error:"+error);
    }
 }

function getAdSize(counter){
	var siteId=siteIdText+counter;
	
	var sizeHTML="<li  id='adSizeDropDown_"+siteId+"' class='dropdown' style='width:140px;list-style: none;'>" +
					"<a data-toggle='dropdown' class='btn dropdown-toggle btn-small'>" +
					"<span  class='current-font'>SIZE</span>&nbsp;<b class='caret'></b></a>" +
					"<ul  class='dropdown-menu'>";
	
	if(adSizeArray.length >0){
		for(var i=0;i<adSizeArray.length;i++){			
		    sizeHTML=  sizeHTML+ 
						"<li onclick='selectAdSize(this,"+counter+")'>" +
						"<a data-wysihtml5-command-value='div' data-wysihtml5-command='formatBlock' " +
					    " unselectable='off'>" +adSizeArray[i]+"</a></li>" ;
		}
	}else{
		sizeHTML=  sizeHTML+ 
					"<li onclick='selectAdSize(this,"+counter+")'><a data-wysihtml5-command-value='div' data-wysihtml5-command='formatBlock' " +
				    " unselectable='off'>320X50</a></li>" +
				    "<li onclick='selectAdSize(this,"+counter+")'><a data-wysihtml5-command-value='h1' data-wysihtml5-command='formatBlock' " +
					" unselectable='off'>320X480</a></li>" +
					"<li onclick='selectAdSize(this,"+counter+")'><a data-wysihtml5-command-value='h2' data-wysihtml5-command='formatBlock' " +
					" unselectable='off'>768X916</a></li>" +
					"<li onclick='selectAdSize(this,"+counter+")'><a data-wysihtml5-command-value='h3' data-wysihtml5-command='formatBlock' " +
					" unselectable='off'>728X90</a></li>";
	}
	sizeHTML= sizeHTML+ "<li onclick='selectAdSize(this,"+counter+")'>" +
						"<a data-wysihtml5-command-value='h3' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Custom Size</a></li></ul></li";		
	
	sizeHTML=sizeHTML+"<br/><div id='customAdSizeDiv_"+siteId+"' style='display:none;'>" +
			"<input type='text' name='customAdSize_"+siteId+"' id='customAdSize_"+siteId+"' style='width:80px;height:18px;' /></div>";
	return sizeHTML;
 }
 
 function loadMediaPlanDropDowns(){
		try{			
			//toastr.success('loading data...please wait..');
			adSizeArray=[];
			adFormatArray=[];
			$.ajax({
				type : "POST",
				url : "/loadMediaPlanDropDowns.lin",
				cache : false,
				data : {
					
				},	
				dataType : 'json',
				success : function(data) {
					
					var adSizeMap=data['adSizeMap'];
					if(adSizeMap !=null && data['status']=='Success'){						
						for(var value in adSizeMap){
							adSizeArray.push(adSizeMap[value]);
						}
					}	
					
					var adFormatMap=data['adFormatMap'];
					if(adFormatMap !=null && data['status']=='Success'){						
						for(var value in adFormatMap){
							adFormatArray.push(adFormatMap[value]);
						}
					}	
					
					
				},
				error : function(jqXHR, exception) {
					//alert('loadAdSize: exception:'+exception);
				}
			});
			
		}catch(error){
			//alert('loadAdSize: error:'+error);
		}
 }
 
 
 function selectAdSize(obj,counter){
	 try{
		 var divId=siteIdText+counter;
		 var adSizeValue=$(obj).text();
		 $('#adSizeDropDown_'+divId).find('.current-font').text(adSizeValue);
		 if(adSizeValue =='Custom Size'){
			 $('#customAdSizeDiv_'+divId).show();
		 }else{
			 $('#customAdSizeDiv_'+divId).hide();
		 }
	 }catch(err){
		 //alert("selectAdSize: exception::"+err);
	 }
 }
 
 function getAdFormatDropDownHTML(counter){
	    var siteId=siteIdText+counter;
	    
		var sizeHTML="<li  id='adFormatDropDown_"+siteId+"' class='dropdown' style='width:140px;list-style: none;'>" +
			"<a data-toggle='dropdown' class='btn dropdown-toggle btn-small'>" +
			"<span  class='current-font'>FORMAT</span>&nbsp;<b class='caret'></b></a>" +
			"<ul  class='dropdown-menu'>" ;
				
		if(adFormatArray.length >0){
			for(var i=0;i<adFormatArray.length;i++){			
			    sizeHTML=  sizeHTML+ 
			    			"<li onclick='selectAdFormat(this,"+counter+")'>" +
			    		    "<a data-wysihtml5-command-value='div' data-wysihtml5-command='formatBlock' " +
			    		    " unselectable='off'>" +adFormatArray[i]+"</a></li>";							
			}
		}else{
			sizeHTML=  sizeHTML+ 
						"<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='div' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Expandable to 320X416</a></li>" +
						"<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='h1' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Expandable to 320X250</a></li>" +
						"<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='h2' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Expandable to 300X300</a></li>" +
						"<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='h3' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Expandable to 768X916</a></li>"+
						"<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='h3' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Expandable to 728X600</a></li>"+
						"<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='h3' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Expandable to 728X270</a></li>"+
						"<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='h3' data-wysihtml5-command='formatBlock' " +
						" unselectable='off'>Interstitial</a></li>";
		}
		
		sizeHTML=  sizeHTML + "<li onclick='selectAdFormat(this,"+counter+")'><a data-wysihtml5-command-value='h3' data-wysihtml5-command='formatBlock' " +
							" unselectable='off'>Custom Size</a></li></ul></li";
		sizeHTML=sizeHTML+"<br/><div id='customAdFormatDiv_"+siteId+"' style='display:none;'>" +
				"<input type='text' name='customAdFormat_"+siteId+"' id='customAdFormat_"+siteId+"' style='width:80px;height:18px;' /></div>";
		return sizeHTML;
  }
 
 function selectAdFormat(obj,counter){
	 try{
		 var siteId=siteIdText+counter;
		 var adFormatValue=$(obj).text();
		 $('#adFormatDropDown_'+siteId).find('.current-font').text(adFormatValue);
		 if(adFormatValue =='Custom Size'){
			 $('#customAdFormatDiv_'+siteId).show();
		 }else{
			 $('#customAdFormatDiv_'+siteId).hide();
		 }
	 }catch(err){
		 //alert("selectAdFormat: exception::"+err);
	 }
 }
 	 
 function getAdServerCheckBoxHTML(counter){
	    var siteId=siteIdText+counter;
	    var sizeHTML="<div class='control-group'><div class='controls' style='margin-left:0px;'>" +
		"<label class='checkbox'>" +
		"<input type='checkbox' name='adServerCheckBox_"+siteId+"' checked='checked' value='DFP'>" +
		"<div style='margin-left:5px;'>DFP</div></label></div>" +
		"<div class='controls' style='margin-left:0px;'>" +
		"<label class='checkbox'><input type='checkbox' name='adServerCheckBox_"+siteId+"'  value='Cletra'>" +
		"<div style='margin-left:5px;'>Cletra</div></label></div>" +
		"<div class='controls' style='margin-left:0px;'><label class='checkbox'>" +
		"<input type='checkbox' name='adServerCheckBox_"+siteId+"' value='Crisp'>" +
		"<div style='margin-left:5px;'>Crisp</div></label></div><div class='controls' style='margin-left:0px;'>" +
		"<label class='checkbox'><input type='checkbox' name='adServerCheckBox_"+siteId+"'  value='XAd'>" +
		"<div style='margin-left:5px;'>XAd</div></label></div></div>";
		
		return sizeHTML;
 }
	


function calculateData(obj, n){
	//alert("Current object id:"+$(obj).attr("id"));
	var clickedElementId=$(obj).attr("id");
	var siteDivId=clickedElementId.substring(clickedElementId.indexOf("_")+1,clickedElementId.length);
	
	var publisherCPM=$('#publisherCPM_'+siteDivId).text();
	var publisherPayOut=$('#cell_8_'+siteDivId).text();
	
	var firstPartyAdServerCost=$('#cell_9_'+siteDivId).text();
	var thirdPartyAdServerCost=$('#cell_10_'+siteDivId).text();
	thirdPartyAdServerCost=thirdPartyAdServerCost.replace('$','');
	var totalCost=$('#cell_11_'+siteDivId).text();
	var marginPercent=$('#marginPercent_'+siteDivId).text();
	var margin=$('#cell_13_'+siteDivId).text();
	var priceQuote=$('#cell_14_'+siteDivId).text();
	var budget=$('#budget_'+siteDivId).text();
	var proposedImpression=$('#cell_16_'+siteDivId).text();
	var grossRevenue=$('#cell_17_'+siteDivId).text();
	var netRevenue=$('#cell_18_'+siteDivId).text();	
	
	
	totalCost=parseFloat(publisherCPM)+parseFloat(firstPartyAdServerCost)+parseFloat(thirdPartyAdServerCost);
	$('#cell_11_'+siteDivId).text(totalCost);
	
	
	margin = ( parseFloat(marginPercent)*totalCost/100 ).toFixed(2);
	//margin=Math.round(margin * 100) / 100;
	$('#cell_13_'+siteDivId).text(margin);
	priceQuote= parseFloat(totalCost)+parseFloat(margin);
	$('#cell_14_'+siteDivId).text(priceQuote)
	
	
	proposedImpression= parseInt( ( (parseFloat(budget)/parseFloat(priceQuote))*1000 ));
	$('#cell_16_'+siteDivId).text(proposedImpression);
	publisherPayOut= ( parseFloat(proposedImpression*parseFloat(publisherCPM)/1000)).toFixed(2);
	publisherPayOut=Math.round(publisherPayOut * 100) / 100;
	$('#cell_8_'+siteDivId).text(publisherPayOut);
		
	/*if( n==1){	
		totalCost=parseFloat(publisherCPM)+parseFloat(firstPartyAdServerCost)+parseFloat(thirdPartyAdServerCost);
		$('#cell_11_'+siteDivId).text(totalCost);				
	}	
	
	
	if( n==2){	
		
		margin = (parseFloat(marginPercent)*totalCost/100);
		margin=Math.round(margin * 100) / 100;
		$('#cell_13_'+siteDivId).text(margin);
		priceQuote= parseFloat(totalCost)+parseFloat(margin);
		$('#cell_14_'+siteDivId).text(priceQuote)
		
	}else{
		marginPercent=0;
	}
	
	if( n==3){	
		proposedImpression= Math.round( (parseFloat(budget)/parseFloat(priceQuote))*1000 );
		$('#cell_16_'+siteDivId).text(proposedImpression);
		publisherPayOut= parseFloat(proposedImpression*parseFloat(publisherCPM)/1000);
		publisherPayOut=Math.round(publisherPayOut * 100) / 100;
		$('#cell_8_'+siteDivId).text(publisherPayOut);
		
	}*/
	
	var costing= ( parseInt(proposedImpression) * (parseFloat(firstPartyAdServerCost)+parseFloat(thirdPartyAdServerCost)) )/1000;
	//costing=Math.round(costing * 100) / 100;
	costing=costing.toFixed(2);
	netRevenue=((parseInt(proposedImpression))*(parseFloat(margin))/1000).toFixed(2);
	//netRevenue=Math.round(netRevenue * 100) / 100;
	
	grossRevenue=(parseFloat(netRevenue)+parseFloat(costing)).toFixed(2);
	//grossRevenue=Math.round(grossRevenue * 100) / 100;
	
	$('#cell_17_'+siteDivId).text(grossRevenue);
	$('#cell_18_'+siteDivId).text(netRevenue);		
	
}

 function update(){
	
 }
 
 function removeSite(obj,idCount){
	try{
		var siteDivId=siteIdText+idCount;
		var siteId=$('#sid_'+siteDivId).val();
	
		if(siteId !=null && $.trim(siteId) !=''){
			deletePlacementSite(siteId);
		}else{
			//alert('site is not in datastore yet...');
		}
		
		document.getElementById("placementTable").deleteRow(obj.parentNode.parentNode.rowIndex);
		// deleteRow
		for(var i=0;i<siteObjectArray.length;i++){
			if(siteObjectArray[i].getSiteId() == siteDivId){
				siteObjectArray.splice(i, 1);
				break;
			}
		}		
		
	}catch(error){
    	//alert("removeSite: error: "+error);
    }
	
}

 function removePlacement(obj,pIdCounter){	
    try{
    	var placementDivId=placementIdText+pIdCounter;
		if(document.getElementById('placementTable').rows[obj.parentNode.parentNode.rowIndex+1] != undefined 
			 && document.getElementById('placementTable').rows[obj.parentNode.parentNode.rowIndex+1].cells[obj.parentNode.cellIndex].childNodes[0].getAttribute("id") == 'remove_site'){
		
			 toastr.error('Please remove the Sites First.!');			
		}else{			
			  var placementId = $('#pid_' + placementDivId).val();
			  if (placementId != null && $.trim(placementId) != '') {
					deletePlacement(placementId);
			  }else{
					//alert('placement is not in datastore yet...');
			  }
			  
			  document.getElementById("placementTable").deleteRow(obj.parentNode.parentNode.rowIndex);
			  for ( var i = 0; i < placementObjectArray.length; i++) {
					if (placementObjectArray[i].getPlacementId() == placementDivId) {
						placementObjectArray.splice(i, 1);
						break;
					}
			  }
		  
		}
		
    }catch(error){
    	//alert("removePlacement: error: "+error);
    }
	
}



function init(){
	var tables = document.getElementsByClassName("editabletable");
	var i;
	for (i = 0; i < tables.length; i++){
		makeTableEditable(tables[i]);
	}
}

function makeTableEditable(table){
	var rows = table.rows;
	var r;
	for (r = 0; r < rows.length; r++){
		var cols = rows[r].cells;
		var c;
		for (c = 0; c < cols.length; c++){
			var cell = cols[c];
			var listener = makeEditListener(table, r, c);
			cell.addEventListener("input", listener, false);
		}
	}
}

function makeEditListener(table, row, col){
	return function(event){
		var cell = getCellElement(table, row, col);
		var text = cell.innerHTML.replace(/<br>$/, '');
		var items = split(text);

		if (items.length === 1){
			// Text is a single element, so do nothing.
			// Without this each keypress resets the focus.
			return;
		}

		var i;
		var r = row;
		var c = col;
		for (i = 0; i < items.length && r < table.rows.length; i++){
			cell = getCellElement(table, r, c);
			cell.innerHTML = items[i]; // doesn't escape HTML
			c++;
			if (c === table.rows[r].cells.length){
				r++;
				c = 0;
			}
		}
		cell.focus();
	};
}

function getCellElement(table, row, col){
	// assume each cell contains a div with the text
	return table.rows[row].cells[col].firstChild;
}

function split(str){
	// use comma and whitespace as delimiters
	return str.split(/,|\s|<br>/);
}

init();

  function saveAllPlacementsUsingJson(){
	try{
		placementData=createPlacementsJSONString();
		//console.log("placementData:"+placementData);
		toastr.success('Saving data...please wait..');
		$.ajax({
			type : "POST",
			url : "/savePlacement.lin",
			cache : false,
			data : {
				placementData:placementData
			},	
			dataType : 'json',
			success : function(data) {
				window.location.reload(true);
			},
			error : function(jqXHR, exception) {
				//alert('save placement and site: exception:'+exception);
			}
		});
		
	}catch(err){
		//alert('saveAllPlacementsUsingJson: err:'+err);
	}
  }

  function createPlacementsJSONString(){
	  
	    placementData = "{";
	    placementData += "\"placement\":";
	    placementData += "[";
	    
	    for(var i=0;i<placementObjectArray.length;i++){
	    	if(i==0 ){
            	placementData += "{";
			}else{
				placementData += ",{";
			}
	    	
	    	var placementDivId=placementObjectArray[i].getPlacementId();  
	    	var placementName=$('#'+placementDivId).val();
			var placementId=$('#pid_'+placementDivId).val();
			var siteObjArray=placementObjectArray[i].getSiteIdArray();
			//alert(siteObjArray.length);
	    	placementData += "\"proposalId\":"+"\""+proposalId+"\"" + ",";
	    	placementData += "\"placementId\":"+"\""+placementId+"\"" + ",";
	    	placementData += "\"placementName\":"+"\""+placementName+"\"" + ",";
	    	placementData += "\"createdBy\":" + "\"\"" + ",";
	    	placementData += "\"createdOn\":" + "\"\"" + ",";
	    	placementData += "\"updatedBy\":" + "\"\"" + ",";
	    	placementData += "\"updatedOn\":" + "\"\"" + ",";
	    	
	    	placementData += "\"site\":";
	    	placementData += "[";
	    	
			for(var j=0;j<siteObjArray.length;j++){				
                if(j==0 ){
                	placementData += "{";
				}else{
					placementData += ",{";
				}
                placementData += createSiteJSON(placementDivId,siteObjArray[j]);				
				placementData += "}";
			}
			placementData += "]";
			placementData += "}";
		 }
	    
	    placementData += "]";
	    placementData += "}";	   
	    
	    return placementData;
  }
  
  function createSiteJSON(placementDivId,siteObj){
	    var siteData="";
		try{
			var siteDivId=siteObj.getSiteId();
			var placementName=$('#'+placementDivId).val();
			var siteName=$('#cell_3_'+siteDivId).text();
			var adSize=$('#adSizeDropDown_'+siteDivId).find('.current-font').text();
			var customAdSize=$('#customAdSize_'+siteDivId).val();
			if(customAdSize !=null &&  $.trim(customAdSize).length>0){
				adSize=customAdSize;
			}
			var adFormat=$('#adFormatDropDown_'+siteDivId).find('.current-font').text();
			var customAdFormat=$('#customAdFormat_'+siteDivId).val();
			if(customAdFormat !=null &&  $.trim(customAdFormat).length>0){
				adFormat=customAdFormat;
			}
			
			var adServerCheckBoxValues = $('input[name=adServerCheckBox_'+siteDivId+']:checked');
			var adServer=null;
			$(adServerCheckBoxValues).each(function(data){
				if(adServer==null){
					adServer=$(this).val();
				}else{
					adServer=adServer+","+$(this).val();
				}
				
			});
			var publisherCPM=$('#publisherCPM_'+siteDivId).text();
			var publisherPayOut=$('#cell_8_'+siteDivId).text();
			
			var firstPartyAdServerCost=$('#cell_9_'+siteDivId).text();
			var thirdPartyAdServerCost=$('#cell_10_'+siteDivId).text();
			thirdPartyAdServerCost=thirdPartyAdServerCost.replace('$','');
			var totalCost=$('#cell_11_'+siteDivId).text();
			var marginPercent=$('#marginPercent_'+siteDivId).text();
			var margin=$('#cell_13_'+siteDivId).text();
			var priceQuote=$('#cell_14_'+siteDivId).text();
			var budgetAllocation=$('#budget_'+siteDivId).text();
			var proposedImpression=$('#cell_16_'+siteDivId).text();
			var grossRevenue=$('#cell_17_'+siteDivId).text();
			var netRevenue=$('#cell_18_'+siteDivId).text();
			var forcastedImpression=$('#cell_19_'+siteDivId).text();
			var reservedImpression=$('#cell_20_'+siteDivId).text();
			var availableImpression=$('#cell_21_'+siteDivId).text();
			
			var siteId=$('#sid_'+siteDivId).val();
			
			siteData += "\"siteId\":"+"\""+siteId+"\""+ ",";
			siteData += "\"siteName\":"+"\""+siteName+"\""+ ",";
			siteData += "\"adSize\":"+"\""+adSize+"\""+ ",";
			siteData += "\"customAdSize\":"+"\""+customAdSize+"\""+ ",";
			siteData += "\"adFormat\":"+"\""+adFormat+"\""+ ",";
			siteData += "\"customAdFormat\":"+"\""+customAdFormat+"\""+ ",";
			siteData += "\"adServer\":"+"\""+adServer+"\""+ ",";
			siteData += "\"publisherCPM\":"+"\""+publisherCPM+"\""+ ",";
			siteData += "\"publisherPayOut\":"+"\""+publisherPayOut+"\""+ ",";
			siteData += "\"firstPartyAdServerCost\":"+"\""+firstPartyAdServerCost+"\""+ ",";
			siteData += "\"thirdPartyAdServerCost\":"+"\""+thirdPartyAdServerCost+"\""+ ",";
			siteData += "\"totalCost\":"+"\""+totalCost+"\""+ ",";
			siteData += "\"marginPercent\":"+"\""+marginPercent+"\""+ ",";
			siteData += "\"margin\":"+"\""+margin+"\""+ ",";
			siteData += "\"priceQuote\":"+"\""+priceQuote+"\""+ ",";
			siteData += "\"budgetAllocation\":"+"\""+budgetAllocation+"\""+ ",";
			siteData += "\"grossRevenue\":"+"\""+grossRevenue+"\""+ ",";
			siteData += "\"netRevenue\":"+"\""+netRevenue+"\""+ ",";
			siteData += "\"proposedImpression\":"+"\""+proposedImpression+"\""+ ",";
			siteData += "\"forcastedImpression\":"+"\""+forcastedImpression+"\""+ ",";
			siteData += "\"reservedImpression\":"+"\""+reservedImpression+"\""+ ",";
			siteData += "\"availableImpression\":"+"\""+availableImpression+"\"";
			
			
		  }catch(err){
			//alert('createSiteJSON: err:'+err);
		}
		return siteData;  
  }
  
  

  
  function showAllPlacements(){
	 try{		
		 $('#dataAjaxLoader').show();
	    var placementArray=placementData.placement;
		for(var i=0;i< placementArray.length;i++){
			var pId=placementArray[i].proposalId;
			var pcId=placementArray[i].placementId;
			var pcName=placementArray[i].placementName;
			var createdBy=placementArray[i].createdBy;
			var updatedOn=placementArray[i].updatedOn;
			var updatedBy=placementArray[i].updatedBy;
			var siteDataArray=placementArray[i].site;
			
			var placementElement=document.getElementById("add_placement");
			var placementDivId=placementIdText+placementCounter;
			var pidCounter=placementCounter;
			displayResult(placementElement);
			
			fillPlacementData(placementDivId,pId,pcId,pcName);
			
			for(var j=0;j<siteDataArray.length;j++){				
				var id=siteDataArray[j].siteId;
				var name=siteDataArray[j].siteName;
				var adSize=siteDataArray[j].adSize;
				var adFormat=siteDataArray[j].adFormat;
				var adServer=siteDataArray[j].adServer;    				
				var publisherCPM=siteDataArray[j].publisherCPM;
				var publisherPayOut=siteDataArray[j].publisherPayOut;
				var firstPartyAdServerCost=siteDataArray[j].firstPartyAdServerCost;
				var thirdPartyAdServerCost=siteDataArray[j].thirdPartyAdServerCost;
				var totalCost=siteDataArray[j].totalCost;
				var marginPercent=siteDataArray[j].marginPercent;
				var margin=siteDataArray[j].margin;
				var priceQuote=siteDataArray[j].priceQuote;
				var budgetAllocation=siteDataArray[j].budgetAllocation;
				var grossRevenue=siteDataArray[j].grossRevenue;
				var netRevenue=siteDataArray[j].netRevenue;
				var proposedImpression=siteDataArray[j].proposedImpression;
				var forcastedImpression=siteDataArray[j].forcastedImpression;
				var reservedImpression=siteDataArray[j].reservedImpression;
				var availableImpression=siteDataArray[j].availableImpression;    
				
				var siteElement=document.getElementById("add_site");
				
				var siteDivId=siteIdText+siteCounter;
				displaySite(siteElement,pidCounter);				
				
				fillSiteData(siteDivId,id,name,adSize,adFormat,adServer,publisherCPM,publisherPayOut,
						     firstPartyAdServerCost,thirdPartyAdServerCost,totalCost,marginPercent,margin,
						     priceQuote,budgetAllocation,grossRevenue, netRevenue,proposedImpression,
						     forcastedImpression,reservedImpression,availableImpression);
						  
			}			
		}
		$('#dataAjaxLoader').hide();
	 }catch(error){
		 // alert("showAllPlacements: error:"+error);
	 }
  }
  
  function fillPlacementData(placementDivId,pId,pcId,pcName){
	  $('#'+placementDivId).val(pcName);
	  $('#proposalId').val(pId);
	  $('#pid_'+placementDivId).val(pcId);		
  }
  
  function fillSiteData(siteDivId,id,name,adSize,adFormat,adServer,publisherCPM,publisherPayOut,firstPartyAdServerCost,
		  thirdPartyAdServerCost,totalCost,marginPercent,margin,priceQuote,budgetAllocation,grossRevenue,
		  netRevenue,proposedImpression,forcastedImpression,reservedImpression,availableImpression){
	   
	    $('#sid_'+siteDivId).val(id);
		$('#cell_3_'+siteDivId).text(name);
		$('#adSizeDropDown_'+siteDivId).find('.current-font').text(adSize);
		$('#adFormatDropDown_'+siteDivId).find('.current-font').text(adFormat);
		//var adServerValues=adServer.split(",");
		$('input[name=adServerCheckBox_'+siteDivId+']').each( function () {
			
		      var temp=$(this).val();
		      var index=adServer.indexOf(temp);		     
		      if(index >=0){
	         	$(this).attr('checked', true);	         	
		      }else{
		    	$(this).attr('checked', false); 
		      }			       
		});
		
		$('#publisherCPM_'+siteDivId).text(publisherCPM);
		$('#cell_8_'+siteDivId).text(publisherPayOut);
		$('#cell_9_'+siteDivId).text(firstPartyAdServerCost);
		$('#cell_10_'+siteDivId).text('$'+thirdPartyAdServerCost);
		$('#cell_11_'+siteDivId).text(totalCost);
		$('#marginPercent_'+siteDivId).text(marginPercent);
		$('#cell_13_'+siteDivId).text(margin);
		$('#cell_14_'+siteDivId).text(priceQuote);
		$('#budget_'+siteDivId).text(budgetAllocation);
		$('#cell_16_'+siteDivId).text(proposedImpression);
		$('#cell_17_'+siteDivId).text(grossRevenue);
		$('#cell_18_'+siteDivId).text(netRevenue);
		$('#cell_19_'+siteDivId).text(forcastedImpression);
		$('#cell_20_'+siteDivId).text(reservedImpression);
		$('#cell_21_'+siteDivId).text(availableImpression);		
		
  } 
  
  function deletePlacementSite(siteId){
    try{
		$.ajax({
			type : "POST",
			url : "/deleteSite.lin",
			cache : false,
			data : {
				  siteId:siteId
			},	
			dataType : 'json',
			success : function(data) {
				toastr.success('Site deleted Succesfully.');
			},
			error : function(jqXHR, exception) {
				//alert('deletePlacementSite: exception:'+exception);
				//toastr.error('deletePlacementSite: exception:'+exception);
			}
		});
		
	 }catch(err){
		//alert('deleteSiteFromDataStore: err:'+err);
		//toastr.error('deleteSiteFromDataStore: err:'+err);
	 }
  }

  function deletePlacement(pId){
	   try{
			$.ajax({
				type : "POST",
				url : "/deletePlacement.lin",
				cache : false,
				data : {
					placementId:pId
				},	
				dataType : 'json',
				success : function(data) {
					//alert('status :'+data['status']);
					toastr.success('Proposal deleted Succesfully.');
				},
				error : function(jqXHR, exception) {
					//alert('deleteProposal: exception:'+exception);
					//toastr.error('deleteProposal: exception:'+exception);
				}
			});
			
			
		}catch(err){
			//alert('deleteProposal: err:'+err);
			//toastr.error('deleteProposal: err:'+err);
		}
  }
  
  var tmpSiteName="";
  function setSiteName(obj){
	  var cellDivId=$(obj).attr("id");	 
	  tmpSiteName= $.trim($('#'+cellDivId).text());
  }
  
  function fetchForecastingData(obj){
		try{ 
			var cellDivId=$(obj).attr("id");			
			var siteName= $.trim($('#'+cellDivId).text());
			var siteDivId=cellDivId.replace("cell_3_","");
			
			if(tmpSiteName != siteName){
				//toastr.success('loading forecasting data if available...please wait..');
				$.ajax({
					type : "POST",
					url : "/findSiteAndLoadForecastingData.lin",
					cache : false,
					data : {
						site:siteName,
						proposalId:proposalId
					},	
					dataType : 'json',
					success : function(data) {
						//toastr.success('Completed...status:'+data['status']);
						var forecastingMap=data["forecastingMap"];
						if(forecastingMap !=null && data['status']=='Success'){
							toastr.success('Forecasting data loaded for this site..');
							$('#cell_19_'+siteDivId).text(forecastingMap["ForecastedImpressions"]);
							$('#cell_20_'+siteDivId).text(forecastingMap["ReservedImpressions"]);
							$('#cell_21_'+siteDivId).text(forecastingMap["AvailableImpressions"]);	
						}else{
							toastr.success('Forecasting data is not available for this site');
						}	
					},
					error : function(jqXHR, exception) {
						//alert('fetchForecastingData: exception:'+exception);
					}
				});
			}else{				
				//toastr.success('same');
			}
			
			
		}catch(err){
			//alert('saveAllPlacementsUsingJson: err:'+err);
		}
  }
  
  function goToPage(pageNo){
	  if(pageNo=='1'){
		  location.href="/proposals.lin?proposalId="+proposalId;
	  }else if(pageNo=='2'){
		  location.href="/mediaPlanner.lin?proposalId="+proposalId;
	  }else if(pageNo=='3'){
		  
	  }
	  
  }
  
  
 /* **************************************************************************************************** */
  /* 
  function myFunction(obj, n){	
  	alert("Current object id:"+$(obj).attr("id"));
  	if( n==1){	
  		 rI = obj.parentNode.parentNode.rowIndex
  		 cI = obj.parentNode.cellIndex
  		 var total_vall = obj.innerHTML;		
  	}	
  	
  	if( n==2){	
  		 obj = document.getElementById('placementTable').rows[obj.parentNode.parentNode.rowIndex].cells[obj.parentNode.cellIndex-5]
  		 rI = obj.parentNode.rowIndex
  		 cI = obj.cellIndex	
  		 var total_vall = obj.childNodes[0].innerHTML;	 
  	}
  	if( n==3){	
  	  obj = document.getElementById('placementTable').rows[obj.parentNode.parentNode.rowIndex].cells[obj.parentNode.cellIndex-8]
  	  rI = obj.parentNode.rowIndex
  	  cI = obj.cellIndex
  	  var total_vall = obj.childNodes[0].innerHTML;
  	}
  	
  		
  	 Start: to calculate IMP amount  	
  	if(document.getElementById('placementTable').rows[rI].cells[cI+8].childNodes[0].innerHTML !="" 
  		&& document.getElementById('placementTable').rows[rI].cells[cI+7].innerHTML !=""){
  		
  		var budget_val = document.getElementById('placementTable').rows[rI].cells[cI+8].childNodes[0].innerHTML;		
  		var pricequote_val1 = document.getElementById('placementTable').rows[rI].cells[cI+7].innerHTML;		
  		var imp_val = Math.round(parseFloat(budget_val)  / parseFloat(pricequote_val1) * 1000)
  		imp_val_final = imp_val.toFixed(2).toString();		
  		document.getElementById('placementTable').rows[rI].cells[cI+9].innerHTML = imp_val_final;
  	}
  	 END: to calculate IMP amount  
  	
  	 Start: to calculate price quote amount  	
  	if(total_vall !="" 
  		&& document.getElementById('placementTable').rows[rI].cells[cI+5].childNodes[0].innerHTML != ""){
  		
  		var maginpercent_val = document.getElementById('placementTable').rows[rI].cells[cI+5].childNodes[0].innerHTML;
  		var total_val = document.getElementById('placementTable').rows[rI].cells[cI+4].innerHTML;
  		var pricequote_val = parseFloat(total_val) * (1 + parseFloat(maginpercent_val))
  		pricequote_val_final = pricequote_val.toFixed(2).toString();
  		document.getElementById('placementTable').rows[rI].cells[cI+7].innerHTML =pricequote_val_final;
  	}
  	 End: to calculate price quote amount  
  	
  	 Start: to calculate margin cpm amount  
  	
  	if(document.getElementById('placementTable').rows[rI].cells[cI+7].innerHTML != "" 
  		&& total_vall != ""){
  	   
  		var pricequote_val=document.getElementById('placementTable').rows[rI].cells[cI+7].innerHTML;
  		var total_val = document.getElementById('placementTable').rows[rI].cells[cI+4].innerHTML;
  		var margincpm_val = parseFloat(pricequote_val) - parseFloat(total_val)
  		margincpm_val_final =margincpm_val.toString(); 
  		document.getElementById('placementTable').rows[rI].cells[cI+6].innerHTML= margincpm_val_final;
  		
  	}
  	 End: to calculate margin cpm amount  
  	
  	 Start: to calculate publisher payout amount  
  	if(document.getElementById('placementTable').rows[rI].cells[cI+9].innerHTML !="" && total_vall != ""){
  		
  		var imp_val=document.getElementById('placementTable').rows[rI].cells[cI+9].innerHTML;
  		var publisherpayout_cal = parseFloat(imp_val) * total_vall / 1000
  		publisherpayout_cal_final = publisherpayout_cal.toFixed(2).toString();
  		document.getElementById('placementTable').rows[rI].cells[cI+1].innerHTML= publisherpayout_cal_final;	
  	}
  	 End: to calculate publisher cpm amount  
  	
  	 Start: to calculate toatal amount  	
  	if(total_vall != ""){	
  		var party_1=document.getElementById('placementTable').rows[rI].cells[cI+2].innerHTML.replace('$','');
  		var party_3=document.getElementById('placementTable').rows[rI].cells[cI+3].innerHTML.replace('$','');		
  		var total_val =  parseFloat(total_vall) + parseFloat(party_1) + parseFloat(party_3)
  		t= total_val.toString();		
  		document.getElementById('placementTable').rows[rI].cells[cI+4].innerHTML= t;	
  	}
  	 End: to calculate toatal amount  
  }*/