var placementIncrement = 1;
var timePeriod='This Week';
var nextPreFlag = 0;
var cityJsonObj = [];
var cityIds = [];
var zipJsonObj = [];
var zipIds = [];
var regenerateCampaignId="";
var  regenerateStatus="";
var  regenerateProgressStatus="";

function dropDownChanged(dropDownId) {
	try{
		var val = $('#'+dropDownId).val().join(',');
		if (val != undefined && val != null && (val.indexOf(allOptionId) >= 0 || val.indexOf(noneOptionId) >= 0) && val.indexOf(',') >= 0) {
			var arr = [];
			if(val.indexOf(allOptionId) >= 0) {
				arr.push(allOptionId);
			}else {
				arr.push(noneOptionId);
			}
			$("#"+dropDownId).select2("val",arr);
		}
	}catch(err){
		
	}
	
}

function validateFlightGoal() {
	 var goal = $('#totalGoal').val();
	 var flightGoal = 0; 
	 for(var i=0;i<=flightIncrement;i++){
    	if(flightGoal!=null && flightGoal!=undefined){
    	var fGoal =  $('#goalId_'+i).val();
    	if(fGoal!=null && fGoal!=undefined ){
    	fGoal = fGoal.replaceAll(',','');
        	fGoal = parseFloat(fGoal);
        	flightGoal = flightGoal + fGoal;
    	}
    	}
    	
    }
    if(goal!=null && goal !=undefined && flightGoal > 0 && flightGoal!=goal){
    	$('#flightGoalError').html("Flights goal should be equal to campaign goal");
    	$("#goalId_"+i).focus();
    	return false;
    }else{
    	$('#flightGoalError').html("");
    }
}

function campaignNameChanged() {
	var campaignName = $('#campaignName').val();
	if(campaignName==null || campaignName==undefined || campaignName==""){
	$('#campaignNameError').html("Campaign Name is Required");
	$("#campaignName").focus();
	return false;
	}else{
	$('#campaignNameError').html("");
	}
}

function placementNameChanged() {
	var placementName = $('#placementNameId').val();
	if(placementName==null || placementName==undefined || placementName==""){
	$('#placementNameIdError').html("Placement Name is Required");
	$("#placementNameId").focus();
	return false;
	}else{
	$('#placementNameIdError').html("");
	}
}

function setSearchBoxDefaultValue(idArray, searchBoxJsonArray, defaultJson, searchBoxId) {
	if(campaignStatus != 'update') {
	searchBoxJsonArray.push(defaultJson);
	idArray.push(allOptionId);
	$('#'+searchBoxId).select2("data",defaultJson);
	}
}

function checkAllNoneOptionsForSearchBox(searchBoxId, allOptionJson, noneOptionJson) {
	var val = $('#'+searchBoxId).val();
	if (val != undefined && val != null && (val.indexOf(allOptionId) >= 0 || val.indexOf(noneOptionId) >= 0) && val.indexOf(',') >= 0) {
	var arr = [];
	if(val.indexOf(allOptionId) >= 0) {
	arr.push(allOptionJson);
	}else {
	arr.push(noneOptionJson);
	}
	$("#"+searchBoxId).select2("data",arr);
	}
};

function creativeChanged() {
	var val = $('#creativeId').val();
	if (val == undefined || val == null || $.trim(val) == '') {
	$('#creativeError').html("Creative size is Required");
	$("#creativeError").focus();
	}else {
	$('#creativeError').html("");
	}
}


$( document ).ready(function() {
	 $('input').change(function(e)  {
	        var goal = $('#totalGoal').val();
	        var rate = $('#rate').val();
	        var budget = $('#budget').val();
	        var rateType = $('#rateType').val();
	        goal = goal.replaceAll(',','');
	        rate = rate.replaceAll(',','');
	        rate = rate.replaceAll('$','');
	        budget = budget.replaceAll(',','');
	        budget = budget.replaceAll('$','');
	        goal = parseInt(goal);
	        rate = parseFloat(rate);
	        budget = parseFloat(budget);
	        //var budget = 0.0;
	        var campaignStartDate = new Date($('#cStartDate').val());
	        var campaignEndDate = new Date($('#cEndDate').val());
	        var placementStartDate = new Date($('#pStartDate').val());
	        var  placementEndDate = new Date($('#pEndDate').val());
	        var  flightStartDate = new Date($('#startdateId_0').val());
	        var flightEndDate = new Date($('#enddateId_0').val());
	        var ctr = $('#pGoal').val();
	        var frequencyCap = $('#frequencyCap').val();
	        var pacing = $('#pacing').val();	
	        pacing = pacing.replaceAll(',','');
	        frequencyCap = frequencyCap.replaceAll(',','');
	        
	        if(campaignStartDate!=null && campaignStartDate!=undefined && campaignEndDate !=null && campaignEndDate !=undefined && campaignStartDate>campaignEndDate){
	        	$('#campaignDateError').html("End date should be greater");
	        	$("#cEndDate").focus();
	        	return false;
	        }else if(campaignStartDate!=null && campaignStartDate!=undefined && campaignEndDate !=null && campaignEndDate !=undefined) {
	        	$('#campaignDateError').html("");
	        	if(campaignStartDateOnLoad != $('#cStartDate').val()) {				// if campaign start date is changed
	        		campaignStartDateOnLoad = $('#cStartDate').val();
	        		$('#pStartDate').val($('#cStartDate').val());
	        		return false;
	        	}
	        	if(campaignEndDateOnLoad != $('#cEndDate').val()) {					// if campaign end date is changed
	        		campaignEndDateOnLoad = $('#cEndDate').val();
	        		$('#pEndDate').val($('#cEndDate').val());
	        		return false;
	        	}
	        }else {
	        	$('#campaignDateError').html("Invalid Date");
	        	$("#cEndDate").focus();
	        	return false;
	        }
	        
	        if(placementStartDate!=null && placementStartDate!=undefined && placementEndDate !=null && placementEndDate !=undefined && placementStartDate>placementEndDate){
	        	$('#placementDateError').html("End date should be greater");
	        	$("#pEndDate").focus();
	        	return false;
	        }else if(placementStartDate!=null && placementStartDate!=undefined && placementEndDate !=null && placementEndDate !=undefined){
	        	$('#placementDateError').html("");
	        }else {
	        	$('#placementDateError').html("Invalid Date");
	        	$("#pEndDate").focus();
	        	return false;
	        }
	        
	        if(placementStartDate!=null && placementStartDate!=undefined && campaignStartDate !=null && campaignStartDate !=undefined && placementStartDate<campaignStartDate){
	        	$('#placementDateError').html("placement start date should not be greater than campaign start date");
	        	$("#pStartDate").focus();
	        	return false;
	        }else if(placementEndDate!=null && placementEndDate!=undefined && campaignEndDate !=null && campaignEndDate !=undefined && placementEndDate>campaignEndDate){
	        	$('#placementDateError').html("placement end date should not be greater than campaign end date");
	        	$("#pEndDate").focus();
	        	return false;
	        }else if(placementEndDate!=null && placementEndDate!=undefined && campaignEndDate !=null && campaignEndDate !=undefined) {
	        	$('#placementDateError').html("");
	        }else {
	        	$('#placementDateError').html("Invalid Date");
	        	$("#pEndDate").focus();
	        	return false;
	        }
	        
	        if(goal!=null && goal!=undefined && goal=="" && isNaN(goal) ){
	        	$('#totalGoalError').html("Goal Should Be a Number");
	        	$("#totalGoal").focus();
	        	return false;
	        }else{
	        	$('#totalGoalError').html("");
	        }
	    	
	        if(rate!=null && rate!=undefined && rate=="" && isNaN(rate) ){
	        	$('#rateError').html("Rate Should Be a Number");
	        	$("#rate").focus();
	        	return false;
	        }else{
	        	$('#rateError').html("");
	        }
	        
	        if(budget!=null && budget!=undefined && budget=="" && isNaN(budget) ){
	        	$('#budgetError').html("Budget Should Be a Number");
	        	$("#budget").focus();
	        	return false;
	        }else{
	        	$('#budgetError').html("");
	        }
	        
	        if(flightStartDate!=null && flightStartDate!=undefined && flightEndDate !=null && flightEndDate !=undefined && flightStartDate>flightEndDate){
	        	$('#flightDateError').html("End date should be greater");
	        	$("#enddateId_0").focus();
	        	return false;
	        }else if(flightStartDate!=null && flightStartDate!=undefined && flightEndDate !=null && flightEndDate !=undefined) {
	        	$('#flightDateError').html("");
	        }else {
	        	$('#flightDateError').html("Invalid Date");
	        	$("#enddateId_0").focus();
	        	return false;
	        }
	        
	        if(campaignStartDate!=null && campaignStartDate!=undefined && flightStartDate!=null && flightStartDate!=undefined && flightStartDate< campaignStartDate){
	        	$('#flightDateError').html("Flight start date not greater than campaign end date");
	        	$("#startdateId_0").focus();
	        	return false;
	        }else if(campaignEndDate!=null && campaignEndDate!=undefined && flightEndDate!=null && flightEndDate!=undefined && flightEndDate> campaignEndDate){
	        	$('#flightDateError').html("Flight end date not greater than campaign end date");
	        	$("#enddateId_0").focus();
	        	return false;
	        }else if(campaignEndDate!=null && campaignEndDate!=undefined && flightEndDate!=null && flightEndDate!=undefined){
	        	$('#flightDateError').html("");
	        }else {
	        	$('#flightDateError').html("Invalid Date");
	        	$("#enddateId_0").focus();
	        	return false;
	        }
	        
	        if(placementStartDate!=null && placementStartDate!=undefined && flightStartDate!=null && flightStartDate!=undefined && flightStartDate< placementStartDate){
	        	$('#flightDateError').html("Flight start date not greater than placement end date");
	        	$("#startdateId_0").focus();
	        	return false;
	        }else if(placementEndDate!=null && placementEndDate!=undefined && flightEndDate!=null && flightEndDate!=undefined && flightEndDate> placementEndDate){
	        	$('#flightDateError').html("Flight end date not greater than placement end date");
	        	$("#enddateId_0").focus();
	        	return false;
	        }else if(placementEndDate!=null && placementEndDate!=undefined && flightEndDate!=null && flightEndDate!=undefined){
	        	$('#flightDateError').html("");
	        }else {
	        	$('#flightDateError').html("Invalid Date");
	        	$("#enddateId_0").focus();
	        	return false;
	        }
	        

	        if( ctr!=null && ctr!=undefined && ctr!="" && isNaN(ctr)){
	        	$('#pGoalError').html("CTR Should Be a Number");
	        	$("#pGoal").focus();
	        	return false;
	        }else{
	        	$('#pGoalError').html("");
	        }
	        
	        if( frequencyCap!=null && frequencyCap!=undefined && frequencyCap!="" && isNaN(frequencyCap)){
	        	$('#frequencyCapError').html("Frequency Cap Should Be a Number");
	        	$("#frequencyCap").focus();
	        	return false;
	        }else{
	        	$('#frequencyCapError').html("");
	        }
	        
	        if(pacing!=null && pacing!=undefined && pacing!="" && isNaN(pacing)){
	        	$('#pacingError').html("Daily Pacing Should Be a Number");
	        	$("#pacing").focus();
	        	return false;
	        }else{
	        	$('#pacingError').html("");
	        }
	        
	        if(rateType == 1){
	        	if(goal!=undefined && goal!=null && !isNaN(goal) && rate!=undefined && rate!=null && !isNaN(rate) && (budget==undefined || budget==null || isNaN(budget))){
	        	budget = (goal*rate)/1000;
	        $('#budget').val(formatFloat(budget,2));
	        }else if(budget!=undefined && budget!=null && !isNaN(budget) && rate!=undefined && rate!=null && !isNaN(rate) && (goal==undefined || goal==null || isNaN(goal))){
	        	goal = (budget*1000)/rate ;
	        $('#totalGoal').val( goal);
	        }else if(goal!=undefined && goal!=null && !isNaN(goal) && rate!=undefined && rate!=null && !isNaN(rate) && budget!=undefined && budget!=null && !isNaN(budget) ){
	        	budget = (goal*rate)/1000;
	        	goal = (budget*1000)/rate ;
	        	$('#budget').val(formatFloat(budget,2));
	        	 $('#totalGoal').val( Math.round(goal));
	        }else if(budget!=undefined && budget!=null && !isNaN(budget) && goal!=undefined && goal!=null && !isNaN(goal) && (rate==undefined || rate==null || isNaN(rate))){
	        	rate = (budget*1000)/goal;
	        	  $('#rate').val(rate);
	        }
	        }else if(rateType == 2){
	        	if(goal!=undefined && goal!=null && !isNaN(goal) && rate!=undefined && rate!=null && !isNaN(rate) && (budget==undefined || budget==null || isNaN(budget))){
	        	budget = goal*rate;
	        $('#budget').val(formatFloat(budget,2));
	        }else if(budget!=undefined && budget!=null && !isNaN(budget) && rate!=undefined && rate!=null && !isNaN(rate) && (goal==undefined || goal==null || isNaN(goal))){
	        	goal = budget/rate;
	        $('#totalGoal').val(  Math.round(goal));
	        }else if(goal!=undefined && goal!=null && !isNaN(goal) && rate!=undefined && rate!=null && !isNaN(rate) && budget!=undefined && budget!=null && !isNaN(budget) ){
	        	budget = goal*rate;
	        	goal = budget/rate;
	        	$('#budget').val(formatFloat(budget,2));
	        	 $('#totalGoal').val(  Math.round(goal));
	        }else if(budget!=undefined && budget!=null && !isNaN(budget) && goal!=undefined && goal!=null && !isNaN(goal) && (rate==undefined || rate==null || isNaN(rate))){
	        	if(budget!=0){
	        	rate = budget/goal;
	        	$('#rate').val(rate);
	        	}
	        }
	        }else if(rateType == 3){
	        	if(goal!=undefined && goal!=null && !isNaN(goal) && rate!=undefined && rate!=null && !isNaN(rate) && (budget==undefined || budget==null || isNaN(budget))){
	        	budget = goal*rate;
	        $('#budget').val(formatFloat(budget,2));
	        }else if(budget!=undefined && budget!=null && !isNaN(budget) && rate!=undefined && rate!=null && !isNaN(rate) && (goal==undefined || goal==null || isNaN(goal))){
	        	goal = budget/rate;
	        $('#totalGoal').val(  Math.round(goal));
	        }else if(goal!=undefined && goal!=null && !isNaN(goal) && rate!=undefined && rate!=null && !isNaN(rate) && budget!=undefined && budget!=null && !isNaN(budget) ){
	        	budget = goal*rate;
	        	goal = budget/rate;
	        	$('#budget').val(formatFloat(budget,2));
	        	 $('#totalGoal').val(  Math.round(goal));
	        }else if(budget!=undefined && budget!=null && !isNaN(budget) && goal!=undefined && goal!=null && !isNaN(goal) && (rate==undefined || rate==null || isNaN(rate))){
	        	if(budget!=0){
	        	rate = budget/goal;
	        	$('#rate').val(rate);
	        	}
	        }
	        }
	        
	    });
	 
	  // Handler for .ready() called.
	
	$('body').on('focus',".startdatedivclass", function(){
	$(this).datepicker('destroy').datepicker({showOn:'focus'}).focus();
	});
	
	
	
	$('body').on('focus',".enddatedivclass", function(){
	$(this).datepicker('destroy').datepicker({showOn:'focus'}).focus();
	});
	    	    
	    $('#zipSearchPlacement').select2({
	        minimumInputLength: 2,
	        multiple : true,
	        placeholder: 'Search Zip',
	        ajax: {
	            url: "/searchZips.lin",
	            dataType: 'json',
	            quietMillis: 100,
	            data: function(term, page) {
	                return {
	                    types: "zips",
	                    limit: -1,
	                    searchText: term
	                };
	            },
	            results: function(data, page ) {
	            	//isModified = true;
	                return { results: data.zips }
	            }
	        },
	        formatResult: function(zips) {
	        	if($.inArray(zips.id, zipIds) == -1) {	// if not found
	        	zipJsonObj.push(zips);
	        	zipIds.push(zips.id);
	}
	            return "<div class='select2-user-result'>" + zips.name + "</div>";
	        },
	        formatSelection: function(zips) { 
	            return zips.name; 
	        },
	        initSelection : function (element, callback) {
	            var elementText = $(element).attr('data-init-text');
	            callback({"searchText":elementText});
	        },
	        //a:setTimeout(function() {setSearchBoxDefaultValue(zipIds, zipJsonObj, {id:allOptionId,name:allOption,cityId:allOption,cityName:allOption,stateId:allOptionId}, 'zipSearchPlacement');},500)
	    });
	    $('#zipSearchPlacement').on("change", function(e) { 
	    	checkAllNoneOptionsForSearchBox('zipSearchPlacement', {id:allOptionId,name:allOption,cityId:allOption,cityName:allOption,stateId:allOptionId}, {id:noneOptionId,name:noneOption,cityId:noneOption,cityName:noneOption,stateId:noneOptionId});
	isModified = true;
	});
	    
  	    $('#citySearchPlacement').select2({
	        minimumInputLength: 2,
	        multiple : true,
	        placeholder: 'Search City',
	        ajax: {
	            url: "/searchCities.lin",
	            dataType: 'json',
	            quietMillis: 100,
	            data: function(term, page) {
	                return {
	                    types: "cities",
	                    limit: -1,
	                    searchText: term
	                };
	            },
	            results: function(data, page ) {
	            	isModified = true;
	                return { results: data.cities }
	            }
	        },
	        formatResult: function(cities) {
	        	if($.inArray(cities.id, cityIds) == -1) {	// if not found
	cityJsonObj.push(cities);
	        	cityIds.push(cities.id);
	}
	            return "<div class='select2-user-result'>" + cities.name + "</div>";
	        },
	        formatSelection: function(cities) { 
	            return cities.name; 
	        },
	        initSelection : function (element, callback) {
	            var elementText = $(element).attr('data-init-text');
	            callback({"searchText":elementText});
	        },
	        //a:setTimeout(function() {setSearchBoxDefaultValue(cityIds, cityJsonObj, {id:allOptionId,name:allOption,stateId:allOptionId}, 'citySearchPlacement');},500)
	    });
   	$('#citySearchPlacement').on("change", function(e) {
   	checkAllNoneOptionsForSearchBox('citySearchPlacement', {id:allOptionId,name:allOption,stateId:allOptionId}, {id:noneOptionId,name:noneOption,stateId:noneOptionId});
   	isModified = true;
   	});
  	    

	});

function getJsonData() {
	var tempObjectCity = [];
	var tempObjectZip = [];
	var cityVal = $('#citySearchPlacement').val();
	var zipVal = $('#zipSearchPlacement').val();
	if(cityVal != undefined && cityVal != "") {
	var arr = cityVal.split(',');
	if(arr != undefined && arr.length > 0) {
	for(i=0; i<arr.length; i++) {
	for(j=0; j<cityJsonObj.length; j++) {
	if(cityJsonObj[j].id == $.trim(arr[i])) {
	tempObjectCity.push(cityJsonObj[j]);
	break;
	}
	}
	}
	}
	}
	$('#cityJsonStrId').val(JSON.stringify(tempObjectCity));
	
	if(zipVal != undefined && zipVal != "") {
	var arr = zipVal.split(',');
	if(arr != undefined && arr.length > 0) {
	for(i=0; i<arr.length; i++) {
	for(j=0; j<zipJsonObj.length; j++) {
	if(zipJsonObj[j].id == $.trim(arr[i])) {
	tempObjectZip.push(zipJsonObj[j]);
	break;
	}
	}
	}
	}
	}
	$('#zipJsonStrId').val(JSON.stringify(tempObjectZip));
	 //validateCreative();
	return true;
}



function addMorePlacementTab(campaignId){
	$("#campaignPlacementId").val(campaignId);
	 $( "#loaderId" ).show();
	$("#placementForm").submit(function(e)
	{
	      
	    var postData = $(this).serializeArray();
	    var formURL = $(this).attr("action");
	    $.ajax(
	    {
	        url : formURL,
	        type: "POST",
	        data : postData,
	        success:function(data, textStatus, jqXHR) 
	        {
	     /*   	var row = $("#dynamicFlight_0").clone();
	        	$( "#dynamicFlight_1" ).hide();
	        	flightIncrement = 1;
	        	row.attr("id", "dynamicFlight_" + flightIncrement);
	        	row.show();
	        	row.find(".gclass").attr("id","goalId_"+flightIncrement);
	        	row.find(".startdateclass").attr("id","startdateId_"+flightIncrement);
	        	row.find(".enddateclass").attr("id","enddateId_"+flightIncrement);
	        	row.find(".buttonId").html('<button type="button" id="removeflightId_'+flightIncrement+'" onclick="removeFlight('+flightIncrement+')" class="btn disabled btn-link"><i class="cus-cross"></i>Remove Flight</button>');

	        	$("#flightContainer").append(row);*/
	        	 $('#placementNameId').val('');
	        	 $('#totalGoalPlacement').val('');
	        	 $( "#loaderId" ).hide();
	        	 toastr.success('Placement Save Successfully');
	            //data: return data from server
	        },
	        error: function(jqXHR, textStatus, errorThrown) 
	        {
	        	 $( "#loaderId" ).hide();
	        	 toastr.error('Placement Save Error');
	            //if fails      
	        }
	    });
	    e.preventDefault(); //STOP default action
	    e.unbind(); //unbind. to stop multiple form submit.
	});
	 
	$("#placementForm").submit(); //Submit  the FORM
	//document.unifiedCampaignListForm.action = "savePlacement.lin";
	//document.unifiedCampaignListForm.submit();
}

function prePlacementTab(){
	if(placementIncrement>1){
	$( "#dynamicPlacement_"+placementIncrement ).hide();
	$( "#dynamicPlacement_"+placementIncrement-1 ).show();
	}
	
}


function addPlacement1(){
	$("#campaignName").removeAttr('disabled');
	$("#rateType").attr("readonly", false);
	$("#cStartDate").removeAttr('readonly');
	$("#cEndDate").removeAttr('readonly');
	$("#address").removeAttr('disabled');
	$("#advertiserListId").select2('enable', false);
	$("#agencyListId").select2('enable', false);
	$("#addplacementId").css({'display':'inline'});
	$("#buttonRowId2").css({'display':'inline'});
	$( "#buttonRowId").hide();
}

function updatePlacement(campaignId,placementId){
	console.log("isProgress:"+isProgress);
	if(isProgress=='true'){
	toastr.warning("This campaign is already in progress (processing another request), please try after some time.");
	setTimeout(loadcampaignProgressStatus(), 30000);
	}else{
	location.href =  "/initEditPlacement.lin?campaignId="+campaignId+"&placementId="+placementId;	
	}
	
}

function loadcampaignProgressStatus(){
	console.log("before reloading -- isProgress:"+isProgress+" and campaignId:"+campaignId);
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadCampaignProgress.lin",
	      cache: false,
	      data : {
	    	  campaignId : campaignId
	  },	    
	      dataType: 'json',	      
	      success: function (data) {
	    	  var response=data+'';
	    	  console.log("response -- "+response);
	    	  if(!response.contains('error')){
	    	  isProgress=response;
	    	  }
	    	  
	     },
	     error: function(jqXHR, exception) {
	    	 toastr.error("ajax response exception --"+exception);	    	
	     }
	   });   
	}catch(error){
	console.log("error on page --"+error);
	
	}
}
function cancleTab(){
	location.href =  "/smartPlanner.lin";
}
function addFlight() {
	
	var row = $("#dynamicFlight_0").clone();

	//$scope.flightRowCount++;
	flightIncrement++;
	
	row.attr("id", "dynamicFlight_" + flightIncrement);
	row.show();
	row.find(".gclass").attr("id","goalId_"+flightIncrement);
	row.find(".startdateclass").attr("id","startdateId_"+flightIncrement);
	row.find(".enddateclass").attr("id","enddateId_"+flightIncrement);
	row.find(".buttonId").html('<label class="control-label">&nbsp;</label><button type="button" id="removeflightId_'+flightIncrement+'" onclick="removeFlight('+flightIncrement+')" class="btn disabled"><i class="cus-cross"></i>Remove Flight</button>');

	$("#removeFlightDiv").prepend(row);
	$("#goalId_"+flightIncrement).val("");
	$("#startdateId_"+flightIncrement).val("");
	$("#enddateId_"+flightIncrement).val("");
	if(flightCount!=null && flightCount!=undefined && isNaN(flightCount)){
	flightCount++;
	}
	/*$("#flightDateSlider_" + flightIncrement).dateRangeSlider({
	    bounds: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)},
	    defaultValues: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)}});*/

};

function removeFlight(rowId){
	if(!confirm("Are you sure you want to Delete this Flight ?")) {
	return false;
	}
	$("#dynamicFlight_" + rowId).remove();
	flightIncrement--;
}

$('#demographicId').on('show', function() {
	$("#demobuttonId img").attr('src','img/btn_minus.png');
	$("#isDemographic").val("true");
})
$('#demographicId').on('hide', function() {
	$("#demobuttonId img").attr('src','img/btn_plus.png');
	$("#isDemographic").val("false");
})
$('#geographicId').on('show', function() {
    $("#geobuttonId img").attr('src','img/btn_minus.png');
	$("#isGeographic").val("true");
})
$('#geographicId').on('hide', function() {
	$("#geobuttonId img").attr('src','img/btn_plus.png');
	$("#isGeographic").val("false");
})



function createCampaign(){
	
    location.href =  "/initCampaign.lin";
}

function updateCampaign(campaignId) {
	$("#campaignId").val(campaignId);
	document.placementForm.action = "savePlacement.lin";
	document.placementForm.submit();
}

function generateMediaPlan(campaignId){
	 location.href =  "/smartMediaPlan.lin?campaignId="+campaignId;
}

function deletePlacement(campaignId,placementId){
	if(!confirm("Are you sure you want to Delete this Placement ?")) {
	return false;
	}
	location.href =  "/deletePlacement.lin?campaignId="+campaignId+"&placementId="+placementId;
}

var copyCampaignId = '';
var copyPlacementId = '';
var copyPlacementName = '';

function copyPlacement(campaignId, placementId, placementName) {
	copyCampaignId = campaignId;
	copyPlacementId = placementId;
	copyPlacementName = placementName;
	showCustomPrompt("Please enter Placement Name :", "Copy of "+placementName);
}

function showCustomPrompt(message, textData) {
	$('#customAlertMessageId').html(message);
	$('#copyPlacementNameId').val(textData);
	$('#customAlertId').css({'width': '40%','margin-left': 'auto','margin-right': 'auto'}).modal('show');
}

function hideCustomPrompt() {
	$('#customAlertId').modal('hide');
}

function hideCustomPromptRegenerate() {
	$('#customAlertId').attr('style', 'display: none !important');
	$('#customAlertId').modal('hide');
}

function regenerateSmartMediaPlanForManual(){
	var divScope=angular.element(document.getElementById("unifiedCampaignSetupDiv")).scope();
	divScope.regenerateSmartMediaPlan(regenerateCampaignId,regenerateStatus,regenerateProgressStatus);
	$('#customAlertId').attr('style', 'display: none !important');
	$('#customAlertId').modal('hide');
	//$scope.regenerateSmartMediaPlan(regenerateCampaignId,regenerateStatus,regenerateProgressStatus,true);
}

function getCopyPlacementName() {
	hideCustomPrompt();
	var placementName = $('#copyPlacementNameId').val();
    if (placementName == undefined || placementName == null) {
    	return;
    }
    placementName = $.trim(placementName);
    if(placementName != '') {
    	checkPlacementNameAvailability(copyCampaignId, copyPlacementId, placementName, copyPlacementName);
    }else {
    	setTimeout(function(){showCustomPrompt("Placement name can't be empty.<br>Please enter Placement Name :", "Copy of "+copyPlacementName);},500);
    }
}

function checkPlacementNameAvailability(campaignId, placementId, placementNameToCheck, nameOfPlacementToCopy) {
	toastr.clear();
	toastr.info("Checking if placement name is available.. Please wait");
	$('#removeBorderForm').attr('disabled', 'disabled');
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/checkPlacementNameAvailability.lin",
	      cache: false,		    
	      dataType: 'json',
	      data : {
	    	  campaignId : campaignId,
	    	  placementNameToCheck : placementNameToCheck
			      },
	      success: function (data) {
	    	  toastr.clear();
	    	  var isPlacementNameAvailable = data.isPlacementNameAvailable;
	    	  var isInSession = data.isInSession;
	    	  var isAuthorised = data.isAuthorised;
	    	  var error = data.error;
	    	  console.log("Error : "+error+", isAuthorised : "+isAuthorised+", isInSession : "+isInSession+", isPlacementNameAvailable : "+isPlacementNameAvailable);
	    	  if(isPlacementNameAvailable != null && isPlacementNameAvailable != undefined && isPlacementNameAvailable) {
	    		  createPlacementCopy(campaignId, placementId, placementNameToCheck);
	    	  }else if(isPlacementNameAvailable != null && isPlacementNameAvailable != undefined && !isPlacementNameAvailable) {
	    		  //toastr.error("Placement name already exists");
	    		  $('#removeBorderForm').removeAttr('disabled');
	    		  setTimeout(function(){showCustomPrompt("Placement name already exists.<br>Please enter Placement Name :", "Copy of "+nameOfPlacementToCopy);},500);
	    	  }else if(isInSession != null && isInSession != undefined && !isInSession) {
	    		  location.href =  '/login.lin';
	    	  }else if(isAuthorised != null && isAuthorised != undefined && !isAuthorised){
	    		  toastr.error('Unauthorised Access.');
	    		  console.log('Unauthorised Access.');
	    	  }else if(error != null && error != undefined) {
	    		  toastr.error('Error : '+error);
	    		  console.log("Error : "+error);
	    	  }
	     },
	     error: function(jqXHR, error) {
	    	 console.log("error : "+error);
	    	 toastr.error("Placement name availability check failed");
	    	 $('#removeBorderForm').removeAttr('disabled');
	     }
	   });   
	}catch(error){
	  	console.log("error : "+error);
	  	$('#removeBorderForm').removeAttr('disabled');
  	}
}

function createPlacementCopy(campaignId, idOfPlacementToCopy, placementName) {
	$('#removeBorderForm').attr('disabled', 'disabled');
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/copyPlacement.lin",
	      cache: false,
	      dataType: 'json',
	      data : {
	    	  campaignId : campaignId,
	    	  idOfPlacementToCopy : idOfPlacementToCopy,
	    	  placementName : placementName
			      },
	      success: function (data) {
	    	  var isPlacementCopied = data.isPlacementCopied;
	    	  var isInSession = data.isInSession;
	    	  var isAuthorised = data.isAuthorised;
	    	  var error = data.error;
	    	  console.log("Error : "+error+", isAuthorised : "+isAuthorised+", isInSession : "+isInSession+", idOfPlacementToCopy : "+idOfPlacementToCopy);
	    	  if(isPlacementCopied != null && isPlacementCopied != undefined && isPlacementCopied) {
	    		  toastr.success('Placement copied successfully.');
	    		  $('#copyPlacementCampaignId').val(campaignId);
	    		  document.copyPlacementForm.submit();
	    	  }else if(isPlacementCopied != null && isPlacementCopied != undefined && !isPlacementCopied) {
	    		  toastr.error("Copy could not be made, Please try again.");
	    		  $('#removeBorderForm').removeAttr('disabled');
	    	  }else if(isInSession != null && isInSession != undefined && !isInSession) {
	    		  location.href =  '/login.lin';
	    	  }else if(isAuthorised != null && isAuthorised != undefined && !isAuthorised){
	    		  toastr.error('Unauthorised Access.');
	    		  console.log('Unauthorised Access.');
	    	  }else if(error != null && error != undefined) {
	    		  toastr.error('Error : '+error);
	    		  console.log("Error : "+error);
	    	  }
	     },
	     error: function(jqXHR, error) {
	    	 console.log("error : "+error);
	    	 toastr.error("Placement copy process failed");
	    	 $('#removeBorderForm').removeAttr('disabled');
	     }
	   });   
	}catch(error){
	  	console.log("error : "+error);
	  	$('#removeBorderForm').removeAttr('disabled');
  	}
}

function addNewAdvertiserPopup(){
	var selectedValue = $('#advertiserListId').val();
	if(selectedValue!=null && selectedValue==0){
	$('#advertiserError').html("Advertiser is Required");
	var $modal = $('#advertiserModel').modal({
	show:false
	}).css({
	    'width': '85%'
	});
	$("#myModalLabel").html("chartTitle");
	    $modal.modal('show');
//	 $( "#dialog-form" ).dialog( "open" );
	}else if(selectedValue!=null  && selectedValue>0) {
	$('#advertiserError').html("");
	}else {
	$('#advertiserError').html("Advertiser is Required");
	}
}

function addAdvertiser(){
	var name =  $('#advertiserName').val();
	var address= $('#advertiserAddress').val();
	var phone = $('#advertiserPhone').val();
	var fax = $('#advertiserFax').val();
	var email = $('#advertiserEmail').val();
	var zip = $('#advertiserZip').val();
	if(name==null || name==undefined || name==""){
	//agencyNameError alert("name is required");
	$('#advertiserNameError').html("Name is Required");
	 $("#advertiserName").focus();
	 return false;
	}else{
	$('#advertiserNameError').html("");
    $.ajax(
	    {
	        url : "/addAdvertiser.lin",
	        type: "POST",
	        data : {
	        	name : name,
	        	address :address,
	        	phone : phone,
	        	fax : fax,
	        	email : email,
	        	zip : zip
	        },
	        dataType: 'json',
	        success:function(data, textStatus, jqXHR) 
	        {
	        	if(data!=null && data!=undefined && data.name!=null && data.name!=undefined){
	        	 $('#advertiserListId').append("<option value="+data.id+">"+data.name+"</option>");
	        	 $("#advertiserListId").select2('val',data.id);
	        	 $('#advertiserName').val('');
	        	     $('#advertiserAddress').val('');
	        	 $('#advertiserPhone').val('');
	        	 $('#advertiserFax').val('');
	        	 $('#advertiserEmail').val('');
	        	 $('#advertiserZip').val('');
	        	 toastr.success('Advertiser Save Successfully');
	     	var $modal = $('#advertiserModel').modal({
	    	show:false
	    	}).css({
	    	    'width': '85%'
	    	});
	    	$("#myModalLabel").html("chartTitle");
	    	    $modal.modal('hide');
	    	    $('#advertiserError').html("");
	        	}
	        //console.log(data);
	        	 
	            //data: return data from server
	        },
	        error: function(jqXHR, textStatus, errorThrown) 
	        {
	        	 $( "#loaderId" ).hide();
	        	 toastr.error('Advertiser name already existed');
	            //if fails      
	        }
	    });
	}
}

function addNewAgencyPopup(){
	var selectedValue = $('#agencyListId').val();
	if(selectedValue!=null && selectedValue==0){
	var $modal = $('#agencyModel').modal({
	show:false
	}).css({
	    'width': '85%'
	});
	$("#myModalLabel").html("chartTitle");
	    $modal.modal('show');
//	 $( "#dialog-form" ).dialog( "open" );
	}
}


function addAgency(){
	var name =  $('#agencyName').val();
	var address= $('#agencyAddress').val();
	var phone = $('#agencyPhone').val();
	var fax = $('#agencyFax').val();
	var email = $('#agencyEmail').val();
	var zip = $('#agencyZip').val();
	if(name==null || name==undefined || name==""){
	//agencyNameError alert("name is required");
	$('#agencyNameError').html("Name is Required");
	 $("#agencyName").focus();
	 return false;
	}else{
	$('#agencyNameError').html("");
	    $.ajax(
	    {
	        url : "/addAgency.lin",
	        type: "POST",
	        data : {
	        	name : name,
	        	address :address,
	        	phone : phone,
	        	fax : fax,
	        	email : email,
	        	zip : zip
	        },
	        dataType: 'json',
	        success:function(data, textStatus, jqXHR) 
	        {
	        	if(data!=null && data!=undefined){
	        	$('#agencyListId').append("<option value="+data.id+">"+data.name+"</option>");
	        	 $("#agencyListId").select2('val',data.id);
	        	 $('#agencyName').val('');
	        	     $('#agencyAddress').val('');
	        	 $('#agencyPhone').val('');
	        	 $('#agencyFax').val('');
	        	 $('#agencyEmail').val('');
	        	 $('#agencyZip').val('');
	        	 toastr.success('Agency Save Successfully');
	     	var $modal = $('#agencyModel').modal({
	    	show:false
	    	}).css({
	    	    'width': '85%'
	    	});
	    	$("#myModalLabel").html("chartTitle");
	    	    $modal.modal('hide');
	        	}
	        //console.log(data);
	        	 
	            //data: return data from server
	        },
	        error: function(jqXHR, textStatus, errorThrown) 
	        {
	        	 $( "#loaderId" ).hide();
	        	 toastr.error('Agency name already existed');
	            //if fails      
	        }
	    });
	}

}

function cancleAdvertiser(){
	var $modal = $('#advertiserModel').modal({
	show:false
	}).css({
	    'width': '85%'
	});
	$("#myModalLabel").html("chartTitle");
    $modal.modal('hide');
    $('#advertiserError').html("Advertiser is Required");
	$("#advertiserError").focus();
}

function cancleAgency(){
	var $modal = $('#agencyModel').modal({
	show:false
	}).css({
	    'width': '85%'
	});
	$("#myModalLabel").html("chartTitle");
    $modal.modal('hide');
}

function addCampaign(){
	
	var campaignName = $('#campaignName').val();
	var campaignStartDate = $('#cStartDate').val();
    var campaignEndDate = $('#cEndDate').val();
    var placementStartDate =$('#pStartDate').val();
    var  placementEndDate = $('#pEndDate').val();
    var goal = $('#totalGoal').val();
    var rate = $('#rate').val();
    var budget = $('#budget').val();
    goal = goal.replaceAll(',','');
    rate = rate.replaceAll(',','');
    rate = rate.replaceAll('$','');
    budget = budget.replaceAll(',','');
    budget = budget.replaceAll('$','');
    goal = parseFloat(goal);
    rate = parseFloat(rate);
    budget = parseFloat(budget);
    var creative = $('#creativeId').val();
    var advertiser = $('#advertiserListId').val();
    var placementName = $('#placementNameId').val();
    
    if(campaignName==null || campaignName==undefined || campaignName==""){
	$('#campaignNameError').html("Campaign Name is Required");
	$("#campaignName").focus();
	return false;
	}else{
	$('#campaignNameError').html("");
	}
    
    if(campaignStartDate==null || campaignStartDate==undefined || campaignStartDate=="" ){
    	$('#campaignDateError').html("Campaign  Date is Required");
    	$("#cStartDate").focus();
    	return false;
    }else if(campaignEndDate==null || campaignEndDate==undefined || campaignEndDate=="" ){
    	$('#campaignDateError').html("");
    	$('#campaignDateError').html("Campaign  Date is Required");
    	$("#cEndDate").focus();
    	return false;
    }else{
    	$('#campaignDateError').html("");
    }
    
    if(advertiser==null ||  advertiser== undefined || advertiser=="" || advertiser=="0"){
	   $('#advertiserError').html("Advertiser  is Required");
	 $("#advertiserError").focus();
	 return false;
    }else{
	   $('#advertiserError').html("");
    }
    
    if(placementName==null || placementName==undefined || placementName==""){
	$('#placementNameIdError').html("Placement Name is Required");
	$("#placementNameId").focus();
	return false;
	}else{
	$('#placementNameIdError').html("");
	}
    if(placementStartDate==null || placementStartDate==undefined || placementStartDate=="" ){
    	$('#placementDateError').html("Placement  Date is Required");
    	$("#pStartDate").focus();
    	return false;
    }else if(placementEndDate==null || placementEndDate==undefined || placementEndDate==""){
    	$('#placementDateError').html("");
    	$('#placementDateError').html("Placement  Date is Required");
    	$("#pEndDate").focus();
    	return false;
    }else{
    	$('#placementDateError').html("");
    }
    
    if(goal==null || goal==undefined || goal=="" ){
    	$('#totalGoalError').html("Goal is Required");
    	$("#totalGoal").focus();
    	return false;
    }else{
    	$('#totalGoalError').html("");
    }
    
    if(isNaN(goal) ){
    	$('#totalGoalError').html("Goal Should Be a Number");
    	$("#totalGoal").focus();
    	return false;
    }else{
    	$('#totalGoalError').html("");
    }
	
    if(rate==null || rate==undefined || rate==""){
    	$('#rateError').html("Rate is Required");
    	$("#rate").focus();
    	return false;
    }else{
    	$('#rateError').html("");
    }
    if(isNaN(rate) ){
    	$('#rateError').html("Rate Should Be a Number");
    	$("#rate").focus();
    	return false;
    }else{
    	$('#rateError').html("");
    }
    
    if(budget==null || budget==undefined || budget=="" ){
    	$('#budgetError').html("Budget is Required");
    	$("#budget").focus();
    	return false;
    }else{
    	$('#budgetError').html("");
    }
    if(isNaN(budget) ){
    	$('#budgetError').html("Budget Should Be a Number");
    	$("#budget").focus();
    	return false;
    }else{
    	$('#budgetError').html("");
    }
    
    if( creative==null ||  creative== undefined || creative=="") {
	   $('#creativeError').html("Creative size is Required");
	   $("#creativeError").focus();
	   return false;
	   }else{
	   $('#creativeError').html("");
	   }
    
    var flightGoal = 0;
    
 
    for(var i=0;i<=flightIncrement;i++){
    	if(flightGoal!=null && flightGoal!=undefined){
    	var fGoal =  $('#goalId_'+i).val();
    	if(fGoal!=null && fGoal!=undefined && fGoal!=""){
    	fGoal = fGoal.replaceAll(',','');
        	fGoal = parseFloat(fGoal);
        	flightGoal = flightGoal + fGoal;
    	}
    	}
    	
    }
    if(goal!=null && goal !=undefined && flightGoal > 0 && flightGoal!=goal){
    	$('#flightGoalError').html("Flight goal should be equal to campaign goal");
    	$("#goalId_0").focus();
    	return false;
    }else{
    	$('#flightGoalError').html("");
    }
    
    
	if(campaignName!=null && campaignName!=undefined && campaignName!="" && placementName!=null && placementName!=undefined && placementName!="" && 
	campaignStartDate!="" && campaignStartDate!=null && campaignStartDate!= undefined && campaignEndDate!=null && campaignEndDate!=undefined
	&& campaignEndDate!="" && placementStartDate!=null && placementStartDate!=undefined &&  placementStartDate!="" && placementEndDate!=null &&  
	placementEndDate !=undefined &&  placementEndDate!="" && goal!=null && goal!=undefined && !isNaN(goal) && goal!="" && rate !=null && rate!=undefined 
	&& !isNaN(rate) && rate!="" && budget !=null && budget!=undefined && !isNaN(budget) && budget!="" &&  creative!=null && creative!= undefined
	&& creative!="" && advertiser!=null &&  advertiser!= undefined && advertiser!="" && advertiser!='0' && flightGoal!=null && flightGoal!=undefined && flightGoal<=goal){
	   	getJsonData();
	document.unifiedCampaignForm.action = "saveCampaign.lin";
	document.unifiedCampaignForm.submit();
	   }
}

function saveNewPlacement(campaignId,statusId,dfpOrderId,dfpOrderName,campaignStatus){
	
	var campaignName = $('#campaignName').val();
	var campaignStartDate = $('#cStartDate').val();
    var campaignEndDate = $('#cEndDate').val();
    var placementStartDate =$('#pStartDate').val();
    var  placementEndDate = $('#pEndDate').val();
    var goal = $('#totalGoal').val();
    var rate = $('#rate').val();
    var budget = $('#budget').val();
    goal = goal.replaceAll(',','');
    rate = rate.replaceAll(',','');
    rate = rate.replaceAll('$','');
    budget = budget.replaceAll(',','');
    budget = budget.replaceAll('$','');
    goal = parseFloat(goal);
    rate = parseFloat(rate);
    budget = parseFloat(budget);
    var creative = $('#creativeId').val();
    var advertiser = $('#advertiserListId').val();
    var placementName = $('#placementNameId').val();
    
    if(campaignName==null || campaignName==undefined || campaignName==""){
	$('#campaignNameError').html("Campaign Name is Required");
	$("#campaignName").focus();
	return false;
	}else{
	$('#campaignNameError').html("");
	}
    
    if(campaignStartDate==null || campaignStartDate==undefined || campaignStartDate=="" ){
     	
     	$('#campaignDateError').html("Campaign  Date is Required");
     	$("#cStartDate").focus();
     	return false;
     }else if(campaignEndDate==null || campaignEndDate==undefined || campaignEndDate=="" ){
     	$('#campaignDateError').html("");
     	$('#campaignDateError').html("Campaign  Date is Required");
     	$("#cEndDate").focus();
     	return false;
     }else{
     	$('#campaignDateError').html("");
     }
    
    if(advertiser==null ||  advertiser== undefined || advertiser=="" || advertiser=='0'){
	   $('#advertiserError').html("Advertiser  is Required");
	 $("#advertiserError").focus();
	 return false;
   }else{
	   $('#advertiserError').html("");
   }
    
    if(placementName==null || placementName==undefined || placementName==""){
	$('#placementNameIdError').html("Placement Name is Required");
	$("#placementNameId").focus();
	return false;
	}else{
	$('#placementNameIdError').html("");
	}
   
 if(placementStartDate==null || placementStartDate==undefined || placementStartDate=="" ){
 	$('#placementDateError').html("Placement  Date is Required");
 	$("#pStartDate").focus();
 	return false;
 }else if(placementEndDate==null || placementEndDate==undefined || placementEndDate==""){
 	$('#placementDateError').html("");
 	$('#placementDateError').html("Placement  Date is Required");
 	$("#pEndDate").focus();
 	return false;
 }else{
 	$('#placementDateError').html("");
 }
 
 if(budget==null || budget==undefined || budget=="" ){
	 	$('#budgetError').html("Budget is Required");
	 	$("#budget").focus();
	 	return false;
	 }else{
	 	$('#budgetError').html("");
	 }
	   if(isNaN(budget) ){
	    	$('#budgetError').html("Budget Should Be a Number");
	    	$("#budget").focus();
	    	return false;
	    }else{
	    	$('#budgetError').html("");
	    }

	if(rate==null || rate==undefined || rate==""){
	$('#rateError').html("Rate is Required");
	$("#rate").focus();
	return false;
	}else{
	$('#rateError').html("");
	}
	if(isNaN(rate) ){
	$('#rateError').html("Rate Should Be a Number");
	$("#rate").focus();
	return false;
	}else{
	$('#rateError').html("");
	}
	
	if(goal==null || goal==undefined || goal=="" ){
	$('#totalGoalError').html("Goal is Required");
	$("#totalGoal").focus();
	return false;
	}else{
	$('#totalGoalError').html("");
	}
	
	if(isNaN(goal) ){
	$('#totalGoalError').html("Goal Should Be a Number");
	$("#totalGoal").focus();
	return false;
	}else{
	$('#totalGoalError').html("");
	}
	
	if( creative==null ||  creative== undefined || creative=="")
	   {
	   $('#creativeError').html("Creative size is Required");
	   $("#creativeError").focus();
	   return false;
	   }else{
	   $('#creativeError').html("");
	   }
    
    
 var flightGoal = 0;
    
    for(var i=0;i<=flightIncrement;i++){
    	if(flightGoal!=null && flightGoal!=undefined){
    	var fGoal =  $('#goalId_'+i).val();
    	if(fGoal!=null && fGoal!=undefined && fGoal!=""){
    	fGoal = fGoal.replaceAll(',','');
        	fGoal = parseFloat(fGoal);
        	flightGoal = flightGoal + fGoal;
    	}
    	}
    	
    }
    if(goal!=null && goal !=undefined && flightGoal > 0 && flightGoal!=goal){
    	$('#flightGoalError').html("Flights goal should be equal to campaign goal");
    	$("#goalId_"+i).focus();
    	return false;
    }else{
    	$('#flightGoalError').html("");
    }
    
	if(campaignName!=null && campaignName!=undefined && campaignName!="" && placementName!=null && placementName!=undefined && placementName!="" && 
	campaignStartDate!="" && campaignStartDate!=null && campaignStartDate!= undefined && campaignEndDate!=null && campaignEndDate!=undefined
	&& campaignEndDate!="" && placementStartDate!=null && placementStartDate!=undefined &&  placementStartDate!="" && placementEndDate!=null &&  
	placementEndDate !=undefined &&  placementEndDate!="" && goal!=null && goal!=undefined && !isNaN(goal) && goal!="" && rate !=null && rate!=undefined 
	&& !isNaN(rate) && rate!="" && budget !=null && budget!=undefined && !isNaN(budget) && budget!="" &&  creative!=null && creative!= undefined
	&& creative!="" && advertiser!=null &&  advertiser!= undefined && advertiser!="" && advertiser!='0' && flightGoal!=null && flightGoal!=undefined && flightGoal<=goal){
	$("#campaignId").val(campaignId);
	$("#statusId").val(statusId);
	var placementStatus = "create";
	$("#placementStatus").val(placementStatus);
	$("#dfpOrderId").val(dfpOrderId);
	$("#dfpOrderName").val(dfpOrderName);
	$("#campaignStatus").val(campaignStatus);
	 getJsonData();
	document.unifiedCampaignEditForm.submit();
	   }
}

function editPlacement(campaignId,placementId,statusId,campaignStatus){
	
	var campaignName = $('#campaignName').val();
	var campaignStartDate = $('#cStartDate').val();
    var campaignEndDate = $('#cEndDate').val();
    var placementStartDate =$('#pStartDate').val();
    var  placementEndDate = $('#pEndDate').val();
    var goal = $('#totalGoal').val();
    var rate = $('#rate').val();
    var budget = $('#budget').val();
    goal = goal.replaceAll(',','');
    rate = rate.replaceAll(',','');
    rate = rate.replaceAll('$','');
    budget = budget.replaceAll(',','');
    budget = budget.replaceAll('$','');
    goal = parseFloat(goal);
    rate = parseFloat(rate);
    budget = parseFloat(budget);
    var creative = $('#creativeId').val();
    var advertiser = $('#advertiserListId').val();
    var placementName = $('#placementNameId').val();
    
    if(campaignName==null || campaignName==undefined || campaignName==""){
	$('#campaignNameError').html("Campaign Name is Required");
	$("#campaignName").focus();
	return false;
	}else{
	$('#campaignNameError').html("");
	}
   
if(campaignStartDate==null || campaignStartDate==undefined || campaignStartDate=="" ){
	
	$('#campaignDateError').html("Campaign  Date is Required");
	$("#cStartDate").focus();
	return false;
}else if(campaignEndDate==null || campaignEndDate==undefined || campaignEndDate=="" ){
	$('#campaignDateError').html("");
	$('#campaignDateError').html("Campaign  Date is Required");
	$("#cEndDate").focus();
	return false;
}else{
	$('#campaignDateError').html("");
}

    if(advertiser==null ||  advertiser== undefined || advertiser=="" || advertiser=='0'){
	   $('#advertiserError').html("Advertiser  is Required");
	 $("#advertiserError").focus();
	   return false;
	   }else{
	   $('#advertiserError').html("");
	   }
    
    if(placementName==null || placementName==undefined || placementName==""){
	$('#placementNameIdError').html("Placement Name is Required");
	$("#placementNameId").focus();
	return false;
	
	}else{
	$('#placementNameIdError').html("");
	}
if(placementStartDate==null || placementStartDate==undefined || placementStartDate=="" ){
	$('#placementDateError').html("Placement  Date is Required");
	$("#pStartDate").focus();
	return false;
}else if(placementEndDate==null || placementEndDate==undefined || placementEndDate==""){
	$('#placementDateError').html("");
	$('#placementDateError').html("Placement  Date is Required");
	$("#pEndDate").focus();
	return false;
}else{
	$('#placementDateError').html("");
}

if(goal==null || goal==undefined || goal=="" ){
 	$('#totalGoalError').html("Goal is Required");
 	$("#totalGoal").focus();
 	return false;
 }else{
 	$('#totalGoalError').html("");
 }

 if(isNaN(goal) ){
 	$('#totalGoalError').html("Goal Should Be a Number");
 	$("#totalGoal").focus();
 	return false;
 }else{
 	$('#totalGoalError').html("");
 }

if(rate==null || rate==undefined || rate==""){
	$('#rateError').html("Rate is Required");
	$("#rate").focus();
	return false;
}else{
	$('#rateError').html("");
}
if(isNaN(rate) ){
	$('#rateError').html("Rate Should Be a Number");
	$("#rate").focus();
	return false;
}else{
	$('#rateError').html("");
}

if(budget==null || budget==undefined || budget=="" ){
 	$('#budgetError').html("Budget is Required");
 	$("#budget").focus();
 	return false;
 }else{
 	$('#budgetError').html("");
 }
   if(isNaN(budget) ){
    	$('#budgetError').html("Budget Should Be a Number");
    	$("#budget").focus();
    	return false;
    }else{
    	$('#budgetError').html("");
    }
    
	if( creative==null ||  creative== undefined || creative=="")
	   {
	   $('#creativeError').html("Creative size is Required");
	   $("#creativeError").focus();
	   return false;
	   }else{
	   $('#creativeError').html("");
	   }
    
    var flightGoal = 0;
    
    for(var i=0;i<=flightCount;i++){
    	if(flightGoal!=null && flightGoal!=undefined){
    	var fGoal =  $('#goalId_'+i).val();
    	if(fGoal!=null && fGoal!=undefined && fGoal!=""){
    	fGoal = fGoal.replaceAll(',','');
        	fGoal = parseFloat(fGoal);
        	flightGoal = flightGoal + fGoal;
    	}
    	
    	}
    	
    }
    if(goal!=null && goal !=undefined && flightGoal > 0 && flightGoal!=goal){
    	$('#flightGoalError').html("Flights goal should be equal to campaign goal");
    	$("#goalId_"+i).focus();
    	return false;
    }else{
    	$('#flightGoalError').html("");
    }
    
	if(campaignName!=null && campaignName!=undefined && campaignName!="" && placementName!=null && placementName!=undefined && placementName!="" && 
	campaignStartDate!="" && campaignStartDate!=null && campaignStartDate!= undefined && campaignEndDate!=null && campaignEndDate!=undefined
	&& campaignEndDate!="" && placementStartDate!=null && placementStartDate!=undefined &&  placementStartDate!="" && placementEndDate!=null &&  
	placementEndDate !=undefined &&  placementEndDate!="" && goal!=null && goal!=undefined && !isNaN(goal) && goal!="" && rate !=null && rate!=undefined 
	&& !isNaN(rate) && rate!="" && budget !=null && budget!=undefined && !isNaN(budget) && budget!="" &&  creative!=null && creative!= undefined
	&& creative!="" && advertiser!=null &&  advertiser!= undefined && advertiser!="" && advertiser!='0' && flightGoal!=null && flightGoal!=undefined && flightGoal<=goal){
	$("#campaignId").val(campaignId);
	$("#statusId").val(statusId);
	$("#placementId").val(placementId);
	//$("#dfpOrderId").val(dfpOrderId);
	//$("#dfpOrderName").val(dfpOrderName);
	$("#campaignStatus").val(campaignStatus);
	 getJsonData();
	document.campaignPlacementEditForm.submit();
	   }
}

String.prototype.replaceAll = function(stringToFind, stringToReplace) {
	if (stringToFind == stringToReplace) return this;
	var temp = this;
	var index = temp.indexOf(stringToFind);
	while (index != -1) {
	temp = temp.replace(stringToFind, stringToReplace);
	index = temp.indexOf(stringToFind);
	}
	return temp;
	}


 /*   $('body').on('click','.startdateclass', function() {
    	alert(flightIncrement);
    	if(flightIncrement>1){
        $(this).datepicker('destroy').datepicker({showOn:'focus'}).focus();
    	}
    });*/

/*function openDateCalender(){
	alert("hi")
	if(flightIncrement>1){
	$(this).datepicker('destroy').datepicker({showOn:'focus'}).focus();
	}
	
}*/

//New Module
var page = 0;
var campaignIds = [];
var limit = 25;
var searchKeyword = '';
var unifiedCampaignSetupApp = angular.module('unifiedCampaignSetupApp', [ 'ui.bootstrap', 'ngSanitize' ]);
var canceler = null;
// Factory
unifiedCampaignSetupApp.factory('unifiedCampaignSetupFactory', function($http, $q) {
	return { 
	getCampaigns: function(campaignStatus) {
	/*return $http.get('/loadAllCampaigns.lin?campaignStatus='+campaignStatus+'&offset='+page+'&searchKeyword='+searchKeyword).then(function(result) {
	    	 //console.log(result.data);
             return result.data;
                       });*/
		if(canceler != null) {
			canceler.resolve();
		}
		canceler = $q.defer();
		return $http.get('/loadAllCampaigns.lin?campaignStatus='+campaignStatus+'&offset='+page+'&searchKeyword='+searchKeyword, {timeout: canceler.promise})
				.then(function(result) {
	    	 //console.log(result.data);
            return result.data;
                      });
       },
		
       deleteCampaign: function(campaignId,progressStatus) {
    	   if(progressStatus ==false || progressStatus=='false'){
    		   return $http.get('/deleteCampaign.lin?campaignId='+campaignId).then(function(result) {
    		    	 //console.log(result.data);
    		         return result.data;
    		   });
    	   }else{
    		   toastr.warning("Please wait for some time, there is already a request in progress.");
  			   toastr.options.timeOut = 100; 
  			   return null;
    	   }
    	  
	   },
       
       
	   unarchiveCampaign: function(campaignId) {
		   return $http.get('/unarchiveCampaign.lin?campaignId='+campaignId).then(function(result) {
	    	 console.log(result.data);
	         return result.data;
	        });
	   },
	   
	   pauseCampaign: function(campaignId) {
	return $http.get('/pauseCampaign.lin?campaignId='+campaignId).then(function(result) {
	    	 //console.log(result.data);
	         return result.data;
	                   });
	   },
      
      
	   resumeCampaign: function(campaignId) {
	return $http.get('/resumeCampaign.lin?campaignId='+campaignId).then(function(result) {
	    	 console.log(result.data);
	         return result.data;
	                   });
	   },
	   
	   cancelCampaign: function(campaignId) {
	return $http.get('/cancelCampaign.lin?campaignId='+campaignId).then(function(result) {
	    	 console.log(result.data);
	         return result.data;
	                   });
	   },
	   
	   unCancelCampaign: function(campaignId) {
	return $http.get('/unCancelCampaign.lin?campaignId='+campaignId).then(function(result) {
	    	 console.log(result.data);
	         return result.data;
	                   });
	   }
	};
});

// Controller
unifiedCampaignSetupApp.controller('unifiedCampaignSetupController', function($scope, $filter, unifiedCampaignSetupFactory) {
	if($scope.predicate == undefined) {
	$scope.predicate = 'lastUpdate';
	}
	if($scope.reverse == undefined) {
	$scope.reverse = true;
	}
	
	if($scope.campaigns == undefined) {
	$scope.campaigns = [];
	}
	
	$scope.campaignListData={};
	$scope.loadUnifiedCampaignData = function(currentMatches) {
	try {
	if($scope.selectedStatus!=undefined && $scope.selectedStatus!=null && $scope.selectedStatus.id != undefined) {
	$scope.campaignStatus = $scope.selectedStatus.id;
	}else {
	$scope.campaignStatus = 1;
	}
	$('#loaderRowId').css('display','table-row');
	$('#noMatchRowId').css('display','none');
	unifiedCampaignSetupFactory.getCampaigns($scope.campaignStatus).then(function(campaignData) {
	    	if(campaignData == undefined || campaignData == null || (JSON.stringify(campaignData)) == '[]') {
	    	console.log("No Data");
	    	if(currentMatches <= 0) {
	    	$('#noMatchRowId').css('display','table-row');
	    	}
	    	//$scope.campaigns = campaignData;
	    	}
	    	else {
	    	$.each( campaignData, function(index, value) {
	if($.inArray(value.id, campaignIds) == -1) {	// if not found
    	$scope.campaigns.push(value);
    	campaignIds.push(value.id);
	}
	});
    	console.log('campaignData : ');
	console.log(campaignData);
	$scope.campaignListData=campaignData;
	page = page + 1;
	    	}
	    	$('#loaderRowId').css('display','none');
	});
	}
	catch(err){
	    	console.log("unifiedCampaignSetupFactory err : "+err);
	    }
	};
	
	
	
	$scope.getCampaignDetails = function(campaignId,isProgress) {
	console.log("isProgress : "+isProgress);
	$('#campaignId').val(campaignId);
	if(isProgress == 'true'){
	toastr.warning('There is already a request in progress. Please try later.');
	}else{
	document.unifiedCampaignSetupForm.submit();
	}
	
	};
	
	$scope.deleteCampaign = function(campaignId,isProgress) {
	try {
	if(!confirm("Are you sure you want to Archive this Campaign ?")) {
	return false;
	}
	unifiedCampaignSetupFactory.deleteCampaign(campaignId,isProgress).then(function(isDeleted) {
	    	if(isDeleted == undefined || isDeleted == null || isDeleted == 'false') {
	    	toastr.error('Delete Failed');
	    	console.log("Delete Failed");
	    	}
	    	else if(isDeleted == 'true') {
	    	var index = -1;
	    	for(i=0; i<$scope.campaigns.length; i++) {
	    	var obj = $scope.campaigns[i];
	    	if(obj.id == campaignId) {
	    	//console.log("object to delete is at index : "+i);
	    	index = i;
	    	break;
	    	}
	    	}
	    	if(index >= 0) {
	    	$scope.campaigns.splice(index, 1);	// remove 1 element at index
	    	toastr.success('Archived Successfully');
	//console.log("Delete Successful");
	    	}
	    	}
	});
	}
	catch(err){
	    	console.log("unifiedCampaignSetupFactory err : "+err);
	    }
	};
	
	$scope.unarchiveCampaign = function(campaignId) {
	try {
	if(!confirm("Are you sure you want to Unarchive this Campaign ?")) {
	return false;
	}
	unifiedCampaignSetupFactory.unarchiveCampaign(campaignId).then(function(isUnarchive) {
	    	if(isUnarchive == undefined || isUnarchive == null || isUnarchive == 'false') {
	    	toastr.error('Unarchived Failed');
	    	console.log("Unarchived Failed");
	    	}
	    	else if(isUnarchive == 'true') {
	    	var index = -1;
	    	for(i=0; i<$scope.campaigns.length; i++) {
	    	var obj = $scope.campaigns[i];
	    	if(obj.id == campaignId) {
	    	//console.log("object to delete is at index : "+i);
	    	index = i;
	    	break;
	    	}
	    	}
	    	if(index >= 0) {
	    	$scope.campaigns.splice(index, 1);	// remove 1 element at index
	    	toastr.success('Unarchived Successfully');
	//console.log("Delete Successful");
	    	}
	    	}
	});
	}
	catch(err){
	    	console.log("unifiedCampaignSetupFactory err : "+err);
	    }
	};
	
	$scope.pauseCampaign = function(campaignId) {
	try {
	if(!confirm("Are you sure you want to Pause this Campaign ?")) {
	return false;
	}
	unifiedCampaignSetupFactory.pauseCampaign(campaignId).then(function(isPaused) {
	    	if(isPaused == undefined || isPaused == null || isPaused == 'false') {
	    	toastr.error('Pause Failed');
	    	console.log("Pause Failed");
	    	}
	    	else if(isPaused == 'true') {
	    	var index = -1;
	    	for(i=0; i<$scope.campaigns.length; i++) {
	    	var obj = $scope.campaigns[i];
	    	if(obj.id == campaignId) {
	    	//console.log("object to delete is at index : "+i);
	    	index = i;
	    	break;
	    	}
	    	}
	    	if(index >= 0) {
	    	$scope.campaigns.splice(index, 1);	// remove 1 element at index
	    	toastr.success('Paused Successfully');
	//console.log("Delete Successful");
	    	}
	    	}
	});
	}
	catch(err){
	    	console.log("unifiedCampaignSetupFactory err : "+err);
	    }
	};
	
	$scope.resumeCampaign = function(campaignId) {
	try {
	if(!confirm("Are you sure you want to Resume this Campaign ?")) {
	return false;
	}
	unifiedCampaignSetupFactory.resumeCampaign(campaignId).then(function(isResumed) {
	    	if(isResumed == undefined || isResumed == null || isResumed == 'false') {
	    	toastr.error('Resume Failed');
	    	console.log("Resume Failed");
	    	}
	    	else if(isResumed == 'true') {
	    	var index = -1;
	    	for(i=0; i<$scope.campaigns.length; i++) {
	    	var obj = $scope.campaigns[i];
	    	if(obj.id == campaignId) {
	    	//console.log("object to delete is at index : "+i);
	    	index = i;
	    	break;
	    	}
	    	}
	    	if(index >= 0) {
	    	$scope.campaigns.splice(index, 1);	// remove 1 element at index
	    	toastr.success('Resumed Successfully');
	//console.log("Delete Successful");
	    	}
	    	}
	});
	}
	catch(err){
	    	console.log("unifiedCampaignSetupFactory err : "+err);
	    }
	};
	
	$scope.cancelCampaign = function(campaignId) {
	try {
	if(!confirm("Are you sure you want to Cancel this Campaign ?")) {
	return false;
	}
	unifiedCampaignSetupFactory.cancelCampaign(campaignId).then(function(isCanceled) {
	    	if(isCanceled == undefined || isCanceled == null || isCanceled == 'false') {
	    	toastr.error('Cancel Failed');
	    	console.log("Cancel Failed");
	    	}
	    	else if(isCanceled == 'true') {
	    	var index = -1;
	    	for(i=0; i<$scope.campaigns.length; i++) {
	    	var obj = $scope.campaigns[i];
	    	if(obj.id == campaignId) {
	    	//console.log("object to delete is at index : "+i);
	    	index = i;
	    	break;
	    	}
	    	}
	    	if(index >= 0) {
	    	$scope.campaigns.splice(index, 1);	// remove 1 element at index
	    	toastr.success('Canceled Successfully');
	//console.log("Delete Successful");
	    	}
	    	}
	});
	}
	catch(err){
	    	console.log("unifiedCampaignSetupFactory err : "+err);
	    }
	};
	
	$scope.unCancelCampaign = function(campaignId) {
	try {
	if(!confirm("Are you sure you want to Start this Campaign ?")) {
	return false;
	}
	unifiedCampaignSetupFactory.unCancelCampaign(campaignId).then(function(isUncanceled) {
	    	if(isUncanceled == undefined || isUncanceled == null || isUncanceled == 'false') {
	    	toastr.error('Start Failed');
	    	console.log("Start Failed");
	    	}
	    	else if(isUncanceled == 'true') {
	    	var index = -1;
	    	for(i=0; i<$scope.campaigns.length; i++) {
	    	var obj = $scope.campaigns[i];
	    	if(obj.id == campaignId) {
	    	//console.log("object to delete is at index : "+i);
	    	index = i;
	    	break;
	    	}
	    	}
	    	if(index >= 0) {
	    	$scope.campaigns.splice(index, 1);	// remove 1 element at index
	    	toastr.success('Started Successfully');
	//console.log("Delete Successful");
	    	}
	    	}
	});
	}
	catch(err){
	    	console.log("unifiedCampaignSetupFactory err : "+err);
	    }
	};
	
	/*$scope.$watch("searchCampaign",function(query){
	$scope.filterCampaigns = $filter("filter")($scope.campaigns , query);
	});*/
	
	$scope.$watch("searchCampaign",function(query){
	$scope.filterCampaigns = $filter("filter")($scope.campaigns , query);
	var temp = searchKeyword;
	if(query != undefined ) {
	searchKeyword = query;
	}
	if(query != undefined  && query != temp && $scope.filterCampaigns.length < limit) {
		page = 0;
		$scope.loadUnifiedCampaignData($scope.filterCampaigns.length);
	}
	});
	

	$scope.status = [
              {id:'1', name:'Active Campaigns'},
              {id:'0', name:'All campaign'},
              {id:'2', name:'Running'},
              {id:'3', name:'Paused'},
              {id:'4', name:'Draft'},
              {id:'5', name:'Completed'},
              {id:'6', name:'Archived'},
              {id:'8', name:'Ready'},
              {id:'10', name:'Canceled'}
	];
    $scope.selectedStatus = $scope.status[0]; // Active
	
	$scope.campaignFilter = function(){
		page = 1;
		campaignIds = [];
		$scope.campaigns = [];
		$scope.loadUnifiedCampaignData(0);
	};
	
	$scope.goSmartMediaPlan=function(campaignId,progressStatus,planType){
		 console.log("campaign id---"+campaignId+" and token:"+token+", progressStatus:"+progressStatus);
		 //location.href='smartMediaPlan.lin?campaignId='+campaignId+"&status="+status;
		 
		 if(progressStatus == 'false' || progressStatus == false){ 
		
			 try{	  
			      selectedCampaignId=campaignId;
				  toastr.info("Please wait for some time while media plan is being generated....");				
				  toastr.options.timeOut = 100;
				 // var planType='auto';
				  $('#create_media_plan_draft_'+campaignId).hide();
			      $('#regenrate_media_plan_draft_'+campaignId).hide();
			      $('#view_media_plan_draft_'+campaignId).hide();
			      $('#view_media_plan_ready_'+campaignId).hide();
				  $('#smartMediaPlanLoader_'+campaignId).show();
				
				  $("#edit_draft_"+campaignId).hide();
				  $("#edit_draft_disable_"+campaignId).show();
				  
				  $("#edit_ready_"+campaignId).hide();
				  $("#edit_ready_disable_"+campaignId).show();
				
				  $("#archive_draft_"+campaignId).hide();
				  $("#archive_draft_disable_"+campaignId).show();
				 
				  $("#archive_ready_"+campaignId).hide();
				  $("#archive_ready_disable_"+campaignId).show();
				 
				    $.ajax({
				      type : "POST",
				      url : "/checkSmartMediaPlan.lin",
				      cache: false,
				      data : {
				    	  campaignId : campaignId,
				    	  status : status,
				    	  planType : planType
				      },	    
				      dataType: 'json',	      
				      success: function (data) {
				    	  //$('#setupCampaignLoader').hide();
				    	  var response=data+'';
				    	  console.log("response -- "+response);
				    	  if(!isNaN(response)){
				    		  location.href='/smartMediaPlan.lin?campaignId='+campaignId;
				    	  }
				    	  
				     },
				     error: function(jqXHR, exception) {
				    	 toastr.error("ajax response exception --"+exception);
				     }
				   });   
				}catch(error){
					toastr.error("error on page --"+error);
				}
		  
		 }else{
			 toastr.warning("Please wait for some time, there is already a request in progress.");
			 toastr.options.timeOut = 400;
		 }  
	 
	};
	
	$scope.viewSmartMediaPlan=function(campaignId){
		location.href='/smartMediaPlan.lin?campaignId='+campaignId;
	};
	
	$scope.checkPlanType=function(campaignId,status,progressStatus,planTypeFlag){
		regenerateCampaignId = campaignId;
		regenerateStatus = status;
		regenerateProgressStatus = progressStatus
		if(planTypeFlag!=null && planTypeFlag!=undefined && planTypeFlag==1){
			showCustomPromptForRegeneration("Are you sure, you want to regenerate media plan for this campaign! As It has been manually generated. " +
					"All the previous information will be lost");
		}else{
			showCustomPromptForRegeneration("Are you sure, you want to regenerate media plan for this campaign! Click 'OK' to regenerate MediaPlan");
			//$scope.regenerateSmartMediaPlan(campaignId,status,progressStatus,false);
		}
	};
	
	
	
	function showCustomPromptForRegeneration(message) {
		$('#customAlertMessageId').html(message);
		$('#customAlertId').attr('style', 'display: block !important').modal('show');
	}
	
	$scope.regenerateSmartMediaPlan=function(campaignId,status,progressStatus,planFlag){
	/*	if(!planFlag){
			var r = confirm("Are you sure, you want to regenerate media plan for this campaign! Click 'OK' to regenerate MediaPlan");
		}*/
		
		//if(r==true){
			var active=0;
			console.log("campaign id---"+campaignId+" , progressStatus:"+progressStatus+", active:"+active);
		 
			if(progressStatus == 'false' || progressStatus ==false){
				$('#smartMediaPlanLoader_'+campaignId).show();
				toastr.info("Please wait for some time while media plan is being generated....");			 
				toastr.options.timeOut = 400;
			  
			     
			     $('#create_media_plan_draft_'+campaignId).hide();
			     $('#regenrate_media_plan_draft_'+campaignId).hide();
			     $('#view_media_plan_draft_'+campaignId).hide();
			     $('#view_media_plan_ready_'+campaignId).hide();
			     
			     $('#setup_'+campaignId).hide();
				 $('#smartMediaPlanLoader_'+campaignId).show();
				 
				  $("#edit_draft_"+campaignId).hide();
				  $("#edit_draft_disable_"+campaignId).show();
				  
				  $("#edit_ready_"+campaignId).hide();
				  $("#edit_ready_disable_"+campaignId).show();
				
				  $("#archive_draft_"+campaignId).hide();
				  $("#archive_draft_disable_"+campaignId).show();
				 
				  $("#archive_ready_"+campaignId).hide();
				  $("#archive_ready_disable_"+campaignId).show();
				  
				 
			  try{	  
			    $.ajax({
			      type : "POST",
			      url : "/checkUpdateSmartMediaPlan.lin",
			      cache: false,
			      data : {
			    	  campaignId : campaignId,
			    	  status : status,
			    	  active : active
			      },	    
			      dataType: 'json',	      
			      success: function (data) {
			    	  //$('#smartMediaPlanLoader_'+campaignId).hide();
			    	  var response=data+'';
			    	  console.log("response -- "+response);
			    	  if(!isNaN(response)){
			    		  location.href='/smartMediaPlan.lin?campaignId='+campaignId;
			    	  }
			    	  
			     },
			     error: function(jqXHR, exception) {
			    	 toastr.error("ajax response exception --"+exception);
			    	 $('#smartMediaPlanLoader_'+campaignId).hide();
			     }
			   });   
			}catch(error){
				toastr.error("error on page --"+error);
				$('#smartMediaPlanLoader_'+campaignId).hide();
			}
		  }else{
			 toastr.warning("Please wait, this request is already in progress.");
			 toastr.options.timeOut = 400;
			 $('#smartMediaPlanLoader_'+campaignId).hide();
			 inProgress=true; 
		  }
		
   }
	
	$scope.setupCampaign=function(campaignId){ 	
		console.log("Setup campaign--"+campaignId);
		var isProcessing=false;
		var isSetupOnDFP=false;
		var campaignArray=$scope.campaignListData;	  
		try{  
		  angular.forEach(campaignArray, function(campaign, key) {
			  console.log(campaign);
			  var id=campaign.id;
			  if(id==campaignId){
				  selectedCampaignId=campaignId;
				  isProcessing=campaign.isProcessing;
				  isSetupOnDFP=campaign.isSetupOnDFP;
				  console.log(" isSetupOnDFP--"+isSetupOnDFP+": isProcessing:"+isProcessing);
				  
				  if(isProcessing==true || isProcessing=='true'){
				    toastr.warning("There is already another request is in progress for this campaign.");
				    toastr.options.timeOut = 400;
				  }else{
					  var r = confirm("Are you sure, you want to setup this campaign on DFP! Click 'OK' to setup");
					  if(r==true){
						     //$('#setupCampaignLoader_'+campaignId).show();
						     $('#setup_'+campaignId).hide();			
						     
						     $('#create_media_plan_draft_'+campaignId).hide();
						     $('#regenrate_media_plan_draft_'+campaignId).hide();
						     $('#view_media_plan_draft_'+campaignId).hide();
						     $('#view_media_plan_ready_'+campaignId).hide();
						     
							
						      $("#edit_draft_"+campaignId).hide();
							  $("#edit_draft_disable_"+campaignId).show();
							  
							  $("#edit_ready_"+campaignId).hide();
							  $("#edit_ready_disable_"+campaignId).show();
							
							  $("#archive_draft_"+campaignId).hide();
							  $("#archive_draft_disable_"+campaignId).show();
							 
							  $("#archive_ready_"+campaignId).hide();
							  $("#archive_ready_disable_"+campaignId).show();
							  
							 
						    console.log('Setup campaign on dfp campaignId:'+campaignId);
						  
						    toastr.info("Please wait for some time while campaign is being setup ....");
						    toastr.options.timeOut = 400;
						    campaign.isProcessing=true;
					    	isProcessing=true;
					 	  
						    $.ajax({
						      type : "POST",
						      url : "/addCampaignSetupInQueue.lin",
						      cache: false,
						      data : {
						    	  //smartMediaPlanId : smartMediaPlanId,
						    	  campaignStatus : 'DRAFT',
						    	  userId : userId,
						    	  campaignId : campaignId
						      },	    
						      dataType: 'json',	      
						      success: function (data) {
						    	  $('#setupCampaignLoader_'+campaignId).hide();
						    	  var response=data+'';
						    	  console.log("response -- "+response);	 
						    	  if(response.contains('error')){
						    		  $('#setup_'+campaignId).show();
							    	  toastr.error(""+response);
							    	  toastr.options.timeOut = 400;
						    	  }
						    	  campaign.isProcessing=false;
						          isProcessing=false;
						     },
						     error: function(jqXHR, exception) {
						    	 $('#setupCampaignLoader_'+campaignId).hide();
						    	 $('#setup_'+campaignId).show();
						    	 console.log("ajax response exception --"+exception);
						    	 toastr.error("ajax response exception --"+exception);
						    	 toastr.options.timeOut = 400;
						    	 campaign.isProcessing=false;
						    	 isProcessing=false;
						     }
						    }); 
					  }				   
				  }	  
			 }
		  
		  });//end of outer for each	  
		  
		}catch(error){
			//$('#setupCampaignLoader').hide();
			console.log("error on page --"+error);
			toastr.error("error on page --"+error);
			toastr.options.timeOut = 400;
			isProcessing=false;
		}
	  
	 
	 };
	 
	 $scope.launchCampaign=function(campaignId){ 	
		console.log("Launch campaign--"+campaignId);
		var smartMediaPlanId=null;
		var dfpOrderId=0;
		var isProcessing=false;
		var isSetupOnDFP=true;
		var campaignArray=$scope.campaignListData;
		  
		try{
		  
		  angular.forEach(campaignArray, function(campaign, key) {
			  console.log(campaign);
			  var id=campaign.id;
			  if(id==campaignId){
				  selectedCampaignId=campaignId;
				  dfpOrderId=campaign.orderId;
				  
				  isProcessing=campaign.isProcessing;
				  isSetupOnDFP=campaign.isSetupOnDFP;
				  console.log("DFP OrderId--"+dfpOrderId+": isProcessing:"+isProcessing);
				  if(isProcessing==true || isProcessing=='true'){
					  toastr.warning("There is already another request is in progress for this campaign.");
					  toastr.options.timeOut = 100;
				  }else if(dfpOrderId==0 || dfpOrderId=='0'){
					  toastr.warning("This campaign has not been setup on DFP yet.");
					  toastr.options.timeOut = 100;
				  }else{
					  //$('#setupCampaignLoader').show();
					  var r = confirm("Are you sure, you want to launch this campaign on DFP! Click 'OK' to launch");
					  if(r==true){
						  console.log('launch campaign for dfpOrderId:'+dfpOrderId);
						  
						  toastr.info("Please wait for some time while campaign is being launched ....");
						  toastr.options.timeOut = 100;
						  campaign.isProcessing=true;
						  isProcessing=true;
						  //$('#launchCampaignButtonId').hide();
						 	  
						    $.ajax({
						      type : "POST",
						      url : "/launchCampaignOnDFP.lin",
						      cache: false,
						      data : {	    	  
						    	  orderId : dfpOrderId,
						    	  campaignId : campaignId
						      },	    
						      dataType: 'json',	      
						      success: function (data) {
						    	  // $('#setupCampaignLoader').hide();
						    	  var response=data+'';
						    	  console.log("response -- "+response);	    	  
						    	  if(response.contains('error')){
							    	  toastr.error("DFP Campaign Status :"+response);
							    	  toastr.options.timeOut = 100;
						    	  }else{	    	  
							    	  toastr.success("Campaign has been launched successfully with status :"+response);
							    	  toastr.options.timeOut = 100;
							    	  campaignJsonObj.header.status=response;	
							    	  campaign.isProcessing=false;
							    	  isProcessing=false;
						    	  }	 	 
						     },
						     error: function(jqXHR, exception) {
						    	 //$('#setupCampaignLoader').hide();
						    	 toastr.error("ajax response exception --"+exception);
						    	 toastr.options.timeOut = 100;
						    	 campaign.isProcessing=false;
						    	 isProcessing=false;
						     }
						   }); 
					  }
	  
				  }	  
			  }
		  
		  });	  
		  
		}catch(error){
			$('#setupCampaignLoader').hide();
			toastr.error("error on page --"+error);
			toastr.options.timeOut = 100;
			isProcessing=false;
		}
   };

});