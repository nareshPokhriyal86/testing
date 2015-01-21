 
/*
 *  @author Youdhveer Panwar
 *  Proposal js
 *  
 */

 $(window).load(function() {
		
		if(proposalId !=null && proposalId !=''){
		
			//Load all geo-targets for given proposal
			var array=getJSONArryFromJSArray(geoTargets);
			$("#geoTargets").select2("val",array);
			
			//Load all industry for given proposal
			array=getJSONArryFromJSArray(industry);
			$("#industry").select2("val",array);
			
			//Load all KPI for given proposal
			array=getJSONArryFromJSArray(kpi);
			$("#kpi").select2("val",array);
		}
		
 });

 function showProposalDataToEdit() {
		    $('#proposalId').val(proposalId);
		    if($.trim(company).length>0){
		    	$('#company').val(company);
		    }
			$('#proposalName').val(proposalName);
			
			//$('#advertiser').val(advertiser);
			$('#advertiser').val(advertiserId);
			
			//$('#agency').val(agency);
			$('#agency').val(agencyId);
			
			$('#salesRep').val(salesRep);
			//$('#industry').val(industry);
			$('#proposalType').val(proposalType);
			$('#proposalStatus').val(proposalStatus);
			$('#budget').val(budget);
			$('#flightStartDate').val(flightStartDate);
			$('#flightEndDate').val(flightEndDate);
			
			/* $("input[name=kpi]").each( function () {
			      var temp=$(this).val();
			      var index=kpi.indexOf(temp);
			      if(index >=0){
		         	$(this).attr('checked', true);
			      }			       
			}); */
			
			/* var industryArray=industry.split(",");
			for (var i in industryArray) {
			    var val=industryArray[i];
			    $("#industry option[value='" + val + "']").attr("selected", 1);
			} */
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
	
	function chooseIndustry(){		
		var search = $("#industry").val();
		if(search.indexOf('0') != -1){			
			$('#customIndustryDiv').show();
		}else{
			$('#customIndustryDiv').hide();
		}
	}
	
	function chooseKpi(){
		var search = $("#kpi").val();
		if(search.indexOf('0') != -1){	
			$('#customKpiDiv').show();
		}else{
			$('#customKpiDiv').hide();
		}
	}
	
	function saveProposalAndShowList(nextPage){
		var divScope=angular.element(document.getElementById("tab-content1")).scope();
		var placementData = "{ placement : "+JSON.stringify(divScope.myData) + " }";
    	$('#placementData').val(placementData);
    	var budget=divScope.totalBudgetAllocation;
    	$('#budget').val(budget);
		var formSubmit=true;
		$('#nextPageControl').val(nextPage);  //0 for ProposalList and 1 for MediaPlan
		
		var proposalName=$('#proposalName').val();
		var startDate=$('#flightStartDate').val();
		var endDate=$('#flightEndDate').val();
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
		}
		else if( salesRep == null || salesRep=='' ){
			toastr.error('Invalid Sales Contact Name');
			formSubmit=false;
			$('#salesRep').focus();
		}
		else if( kpi == null || kpi=='-1' ){
			toastr.error('Invalid kpi');
			formSubmit=false;
		}else if( geoTargets == null || geoTargets=='-1' ){
			toastr.error('Invalid Markets (DMAs)');
			formSubmit=false;
		}
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
		
		
		//alert($('#kpi').val());		
        if(formSubmit){
        	document.forms["proposalForm"].submit();
		} 
		
	}	
	
	function mediaPlan(){
		saveProposalAndShowList(1);
		//location.href="/mediaPlanner.lin?proposalId="+proposalId;
	}
	
	function cancel(){
		location.href="/proposals.lin";
	}
	
	function IsEmail(email) {
		var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return regex.test(email);
	}
  
  
	function autoSaveProposal(){
		try{
			var jsonObj = {proposal:{
				 proposalId : $("proposalId").val(),
				 proposalName :$("proposalName").val(),
				 company : $("company").val(),
				 advertiser :$("advertiser").val(),
				 customAdvertiser :$("customAdvertiser").val(),
				 agency :$("agency").val(),
				 customAgency :$("customAgency").val(),
				 industry :$("industry").val(),
				 customIndustry :$("customIndustry").val(),
				 proposalType :$("proposalType").val(),
				 proposalStatus :$("proposalStatus").val(),			 
				 budget :$("budget").val(),
				 salesRep :$("salesRep").val(),
				 kpi :$("kpi").val(),
				 customKpi :$("customKpi").val(),
				 geoTargets :$("geoTargets").val(),
				 customGeoTargets :$("customGeoTargets").val(),
				 flightStartDate :$("flightStartDate").val(),
				 flightEndDate :$("flightEndDate").val(),
				 updatedBy :$("updatedBy").val(),
				 updatedOn :$("updatedOn").val(),
				 salesContact :$("salesContact").val(),
				 salesEmail :$("salesEmail").val(),
				 salesPhone :$("salesPhone").val(),
				 trafficContact :$("trafficContact").val(),
				 trafficEmail :$("trafficEmail").val(),
				 trafficPhone :$("trafficPhone").val()
				 
				 
			} };
			var jsonStr = JSON.stringify(jsonObj);
			$.ajax({
				type : "POST",
				url : "/autoSaveProposal.lin",
				cache : false,
				data : {
					proposalData:jsonStr
				},	
				dataType : 'json',
				success : function(data) {
					toastr.success("Proposal data auto saved successfully");
				},
				error : function(jqXHR, exception) {
					toastr.error("Proposal data auto saved failed"+exception);					
				}
			});
		}catch(error){
			//alert("autoSaveProposal:error:"+error);
		}
				
		
		
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
	
	function validateBudget() {
		var budget = $('#budget').val();
		if(budget != null && budget != undefined) {
			budget = budget.replaceAll('$','');
			budget = budget.replaceAll(',','');
			budget = $.trim(budget);
			if(budget != '' && !isNaN(budget)) {
				budget = '$'+formatFloat(budget,2);
				$('#budget').val(budget);
			}
			else {
				$('#budget').val('');
			}
		}
	}
	
	function generateIO(IO_type) {
		var divScope=angular.element(document.getElementById("tab-content1")).scope();
		if(divScope == undefined || divScope.selectedRows == undefined ) {
			toastr.error('select atleast one row to generate IO');
			return true;
		}
		var proposalId = $('#proposalId').val();
		if(proposalId == undefined || proposalId == null || $.trim(proposalId) == ''){
			toastr.error('Save campaign before IO generation');
			return true;
		}
		proposalId = $.trim(proposalId)
		var placementObj = [];
		/*var selectedRows = "{ placement : "+JSON.stringify(divScope.selectedRows) + " }";
		if(selectedRows == null || selectedRows == undefined || selectedRows.length == 0) {
			toastr.error('select atleast one row to generate IO');
			return true;
		}*/
		var selectedPlacements = divScope.selectedRows;
		if(selectedPlacements != null && selectedPlacements != undefined && selectedPlacements.length > 0) {
			for(i=0;i<selectedPlacements.length;i++) {
				var selectedPlacement = selectedPlacements[i];
				var item = {};
				item ["placementName"] = selectedPlacement.placementName;
			    item ["site"] = selectedPlacement.site;
			    if(IO_type == 'clientIO') {
			    	item ["CPM"] = selectedPlacement.effectiveCPM;
				}
			    else if(IO_type == 'publisherIO'){
			    	item ["CPM"] = selectedPlacement.publisherCPM;
				}
			    item ["budgetAllocation"] = selectedPlacement.budgetAllocation;
			    item ["proposedImpression"] = selectedPlacement.proposedImpression;
			    placementObj.push(item);
			}
		}
		else {
			toastr.error('select atleast one row to generate IO');
			return true;
		}
		var selectedRows = JSON.stringify(placementObj);
		selectedRows = "{ placement : "+selectedRows+ " }";
		selectedRows = selectedRows.replaceAll('%','');
		if(IO_type == 'clientIO') {
			clientIOReport(selectedRows, proposalId);
		}
		else if(IO_type == 'publisherIO'){
			publisherIOReport(selectedRows, proposalId);
		}
	}
	
	function clientIOReport(selectedRows, proposalId) {
		try{	  
			location.href="/clientIOReport.lin?proposalId="+proposalId+"&selectedRows="+selectedRows;   
		}catch(error){
		}
	}
	
	function publisherIOReport(selectedRows, proposalId) {
		try{
			location.href="/publisherIOReport.lin?proposalId="+proposalId+"&selectedRows="+selectedRows;
		}catch(error){
		}
	}
	
 /* **************************************************************************************************** */
 