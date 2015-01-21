 
/*
 *  @author Youdhveer Panwar
 *  InsertionOrder  Java Script file
 *  
 */

 $(window).load(function() {
		
		if(proposalId !=null && proposalId !=''){		
			
			//Load all geo-targets for given proposal			
			if(geoTargets !=null && geoTargets !=''){
				var jsonObj=getJSONArryFromJSArray(geoTargets);
				$("#geoTargets").select2("data",jsonObj); 		 
			}			
			
		}
		
 });

 function showInsertionOrderToEdit(){		
		    $('#proposalId').val(proposalId);
			$('#advertiser').val(advertiserId);
			$('#agency').val(agencyId);
 }
	
	
 function getJSONArryFromJSArray(dataString){
		var jsArray=dataString.split(",");
		var jsonObj = [];
		 for (var i in jsArray) {
		    var val=$.trim(jsArray[i]);
		    if(val !=null && val !=''){		    	
		        var item = {}
		        item ["id"] = val;
		        item ["text"] = val;
		        jsonObj.push(item);
		    }		    
		}
		return jsonObj;		 		 
 }	
	
 function chooseAdvertiser(){		
		var found = false;
		var search = $("#advertiser").val();
		if(search=='0'){
			$('#customAdvertiserDiv').show();
		}else{
			$('#customAdvertiserDiv').hide();
		}
 }
	
 function chooseAgency(){
		var search = $("#agency").val();
		if(search=='0'){
			$('#customAgencyDiv').show();
		}else{
			$('#customAgencyDiv').hide();
		}
 }	
	
 function chooseGeoTargets(){
		var search = $("#geoTargets").val();
		if(search.indexOf('0') != -1){	
			$('#customGeoTargetsDiv').show();
		}else{
			$('#customGeoTargetsDiv').hide();
		}
 }
	
 
 function saveIOAndShowList(nextPage){		
		var formSubmit=true;
		
		
		var proposalName=$('#proposalName').val();
		var startDate=$('#flightStartDate').val();
		var endDate=$('#flightEndDate').val();
		var advertiser=$('#advertiser').val();
		var agency=$('#agency').val();
		var industry=$('#industry').val();
		var proposalType=$('#proposalType').val();
		var proposalStatus=$('#proposalStatus').val();
		var budget=$('#budget').val();
		
		var salesRep=$('#salesRep').val();		
		var kpi=$('#kpi').val();
		var geoTargets=$('#geoTargets').val();
		
		if(proposalName == null || proposalName==''){
			toastr.error('Please provide a proposal name');
			formSubmit=false;
		}else if( advertiser == null || advertiser=='-1' ){
			toastr.error('Invalid advertiser');
			formSubmit=false;
		}else if( agency == null || agency=='-1' ){
			toastr.error('Invalid agency');
			formSubmit=false;
		}else if( industry == null || industry=='-1' ){
			toastr.error('Invalid industry');
			formSubmit=false;
		}else if( proposalType == null || proposalType=='-1' ){
			toastr.error('Invalid proposalType');
			formSubmit=false;
		}else if( proposalStatus == null || proposalStatus=='-1' ){
			toastr.error('Invalid proposalStatus');
			formSubmit=false;
		}else if( salesRep == null || salesRep=='-1' ){
			toastr.error('Invalid salesRep');
			formSubmit=false;
		}else if( !($.isNumeric( budget)) || (parseInt(budget) <0) ){
			toastr.error('Invalid budget');
			formSubmit=false;
		}else if( kpi == null || kpi=='-1' ){
			toastr.error('Invalid kpi');
			formSubmit=false;
		}else if( geoTargets == null || geoTargets=='-1' ){
			toastr.error('Invalid geoTargets');
			formSubmit=false;
		}
		else if( (startDate ==null || startDate == '') || ((endDate ==null || endDate == ''))){
			toastr.error('Invalid dates');
			formSubmit=false;
		}else{
			var date1 = moment(startDate, 'DD-MM-YYYY');
			var date2 = moment(endDate, 'DD-MM-YYYY');
			var diff = date2.diff(date1, 'days'); 
			
			if(diff < 0){
				toastr.error('End Date can not be less than Start Date');
				formSubmit=false;
			}
		}
		
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
    		toastr.success('Data Saved Succesfully.');
		} 
		
 }	
	
 function IsEmail(email) {
		var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return regex.test(email);
 }
  
  
 /* **************************************************************************************************** */
 