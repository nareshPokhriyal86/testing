// single select
/* $("#adServerForPublisher").select2("val", newElement.id);  */

var selectedAdvertiserIds = {};
var selectedOrderIds = {};
var dfpMemcacheStatus = false;

var validAccountflag = false;

function getPublisherIdsForBigQuery() {
	var returnVal = '';
	var publisherIdsForBigQuery = $('#publisherIdsForBigQuery').val();
	if(publisherIdsForBigQuery != null && publisherIdsForBigQuery != undefined) {
		returnVal = publisherIdsForBigQuery;
	}
	return returnVal;
}

function getSelectedSiteName() {
	var siteName = $('#DFPPropertyName').val();
	if(siteName != null && siteName != undefined && siteName != '') {
		siteName = "'"+siteName+"'";
	}
	return siteName;
}

/*function getSelectedSiteId() {
	var siteId = $('#').val();
	if(siteId != null && siteId != undefined && siteId != '') {
		siteId = "'"+siteId+"'"
	}
	return siteId;
}*/

function getselectedAccountIds() {
	var accountIds = '';
	var accountIdArr = $('#selectedAdServerAccounts').val();
	if(accountIdArr != null) {
		accountIds = accountIdArr.join(",");
	}
	return accountIds;
}

function getNetworkId() {
	var networkId = $('#networkId').val();
	if(networkId == null || networkId == undefined || $.trim(networkId) == '') {
		toastr.error('DFP Credentials are mandatory');
	}
	return $.trim(networkId);
}

function getNetworkUsername() {
	var networkUsername = $('#networkUsername').val();
	var networkId = $('#networkId').val();
	if($.trim(networkId) != '' && (networkUsername == null || networkUsername == undefined || $.trim(networkUsername) == '')) {
		toastr.error('DFP Credentials are mandatory');
	}
	return $.trim(networkUsername);
}

function loadSelectedAccountsFromBigQuery() {
	var selectedAgencies = $('#agencyIdStringForBigQuery').val();
	var selectedAdvertisers = $('#advertiserIdStringForBigQuery').val();
	var jsonObj = [];
	if(selectedAgencies == null || selectedAgencies == undefined || selectedAgencies == '') {
		if(selectedAdvertisers == null || selectedAdvertisers == undefined || selectedAdvertisers == '') {
			return true;
		}
	}
	$('#selectAdServerAccountsLoaderDiv').css('display', 'block');
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadSelectedAccountsFromBigQuery.lin",
	      cache: false,
	      data : {
	    	  selectedAgencies : selectedAgencies,
	    	  selectedAdvertisers : selectedAdvertisers
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
		    	  if (index == 'accountList' && element != null  && element != undefined  && element.length > 0) {
	    			  jsonObj = [];
	    			  $.each(element,function (newIndex,newElement){
		         		  $('#selectedAdServerAccounts').append('<option value="'+newElement.accountId+'">'+newElement.accountName+'</option>');
		         			var item = {};
		         			item ["id"] = newElement.accountId;
		         	        item ["text"] = newElement.accountName;
		         	        jsonObj.push(item);
		         	   });
	    			  $("#selectedAdServerAccounts").select2("data",jsonObj);
	    		  }
	    	  });
	    	  $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     },
	     error: function(jqXHR, exception) {
	    	 $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     }
	   });   
	  }catch(error){
		  $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	  }
}

$(document).ready(function() {		
	 $('#selectAdServerAccounts').select2({
	        minimumInputLength: 1,
	        placeholder: 'Search Accounts',
	        ajax: {
	            url: "/loadAccountsForTeam.lin",
	            dataType: 'json',
	            multiple:'true',
	            timeout: 10000,
	            data: function(term, page) {
	                return {
	                    types : "accountList",
	                    limit : -1,
	                    term : term,
	                    accountIdsNotIn : getselectedAccountIds(),
	                    publisherIdsForBigQuery : getPublisherIdsForBigQuery()
	                };
	            },
	            results: function(data, page ) {
	                return { results: data.accountList }
	            }
	        },
	        formatResult: function(accountList) {
	        	if(accountList == null || accountList.length > 0) {
	        	}
	        	return "<div class='select2-user-result' id='"+accountList.accountId+"' onclick='addSelectedAccounts(this)'>" + accountList.accountName + "</div>";
	        },
	        formatSelection: function(accountList) { 
	            return accountList; 
	        },
	        initSelection : function (element, callback) {
	            var elementText = $(element).attr('data-init-text');
	            callback({"term":elementText});
	        }
	    });
	 
	 /*$('#selectDFPPropertyName').select2({
	        minimumInputLength: 1,
	        placeholder: 'Search Site Name',
	        ajax: {
	            url: "/loadSitesForProperty.lin",
	            dataType: 'json',
	            quietMillis: 100,
	            data: function(term, page) {
	                return {
	                    types : "siteList",
	                    limit : -1,
	                    term : term,
	                    siteIdNotIn : getSelectedSiteId(),
	                    siteNameNotIn : getSelectedSiteName(),
	                    publisherIdsForBigQuery : getPublisherIdsForBigQuery()
	                };
	            },
	            results: function(data, page ) {
	                return { results: data.siteList }
	            }
	        },
	        formatResult: function(siteList) {
	        	return "<div class='select2-user-result' id='"+siteList.siteId+"' onclick='addSelectedSite(this)'>" + siteList.siteName + "</div>";
	        },
	        formatSelection: function(siteList) { 
	            return accountList; 
	        },
	        initSelection : function (element, callback) {
	            var elementText = $(element).attr('data-init-text');
	            callback({"term":elementText});
	        }
	    });*/
	 
	 $('#selectDFPPropertyName').select2({
	        minimumInputLength: 1,
	        placeholder: 'Search Sites',
	        ajax: {
	            url: "/loadSitesForProperty.lin",
	            dataType: 'json',
	            quietMillis: 100,
	            data: function(term, page) {
	                return {
	                    types : "siteList",
	                    limit : -1,
	                    term : term,
	                    adServerId : getNetworkId(),
	                    adServerUsername : getNetworkUsername()
	                };
	            },
	            results: function(data, page ) {
	                return { results: data.siteList }
	            }
	        },
	        formatResult: function(siteList) {
	        	return "<div class='select2-user-result' id='"+siteList.siteId+"' onclick='addSelectedSite(this)'>" + siteList.siteName + "</div>";
	        },
	        formatSelection: function(siteList) { 
	            return accountList; 
	        },
	        initSelection : function (element, callback) {
	            var elementText = $(element).attr('data-init-text');
	            callback({"term":elementText});
	        }
	    });
	 
	 /*$('#selectDfpAccountName').select2({
	        minimumInputLength: 1,
	        placeholder: 'Search Accounts',
	        ajax: {
	            url: "/searchAccounts.lin",
	            dataType: 'json',
	            multiple:'true',
	            quietMillis: 100,
	            data: function(term, page) {
	                return {
	                    types : "accountList",
	                    limit : -1,
	                    term : term,
	                    adServerId : getNetworkId(),
	                    adServerUsername : getNetworkUsername()
	                };
	            },
	            results: function(data, page ) {
	                return { results: data.accountList }
	            }
	        },
	        formatResult: function(accountList) {
	        	return "<div class='select2-user-result' id='"+accountList.accountId+"<SEP>"+accountList.accountType+"' onclick='addSearchedAccounts(this)'>" + accountList.accountName + "</div>";
	        },
	        formatSelection: function(accountList) { 
	            return accountList; 
	        },
	        initSelection : function (element, callback) {
	            var elementText = $(element).attr('data-init-text');
	            callback({"term":elementText});
	        }
	    });*/
	 
	 $('#selectDfpAccountName').select2({
	        minimumInputLength: 1,
	        placeholder: 'Search Accounts',
	        ajax: {
	            url: "/searchAccounts.lin",
	            dataType: 'json',
	            multiple:'true',
	            quietMillis: 100,
	            data: function(term, page) {
	                return {
	                    types : "dfpAccountList",
	                    limit : -1,
	                    term : term,
	                    adServerId : getNetworkId(),
	                    adServerUsername : getNetworkUsername()
	                };
	            },
	            results: function(data, page ) {
	                return { results: data.dfpAccountList }
	            }
	        },
	        formatResult: function(dfpAccountList) {
	        	return "<div class='select2-user-result' id='"+dfpAccountList.id+"<SEP>"+dfpAccountList.type+"' onclick='addSearchedAccounts(this)'>" + dfpAccountList.name + "</div>";
	        },
	        formatSelection: function(dfpAccountList) { 
	            return accountList; 
	        },
	        initSelection : function (element, callback) {
	            var elementText = $(element).attr('data-init-text');
	            callback({"term":elementText});
	        }
	    });
});

function addSelectedAccounts(obj){
	var id = $(obj).attr('id');
	var name = $(obj).text();
	$(obj).remove();
	if($("#selectedAdServerAccounts option[value=" + id + "]").val() == undefined) {	   // if does not exists then add
		$('#selectedAdServerAccounts').append('<option value="'+id+'">'+name+'</option>');
	}
	var selectedItems = $("#selectedAdServerAccounts").select2("val");
	selectedItems.push(id);
	$("#selectedAdServerAccounts").select2("val", selectedItems);
}

function addSelectedSite(obj){
	var id = $(obj).attr('id');
	var name = $(obj).text();
	//$(obj).remove();
	$(".select2-drop-active").hide();
	/*$('#DFPPropertyNameLabel').html(name);*/
	$('#DFPPropertyName').val(name);
	$('#propertyName').val(name);
	$('#DFPPropertyId').val(id);
	validAccountflag = false;
	accountOrPropertyNameAvailable(name, '', 'propertyNameAvailable');
	isDfpIdAvailable(id, 'dfpPropertyAvailable', true);
}

function addSearchedAccounts(obj) { 
	var id = $(obj).attr('id');
	var name = $(obj).text();
	//$(obj).remove();
	$(".select2-drop-active").hide();
	if(id != null || id != undefined) {
		var array = id.split('<SEP>');
		if(array != null && array != undefined && array.length == 2) {
			var id = array[0];
			var type = array[1];
			$('#accountDfpId').val(id);
			$('#dfpAccountName').val(name);
			$('#accountType').val(type);
			$('#showAccountType').html(type);
			$('#accountName').val(name);
			validAccountflag = false;
			accountOrPropertyNameAvailable(name, type, 'accountNameAvailable');
			isDfpIdAvailable(id, 'dfpAccountAvailable', false);
		}
	}
}

function validateEmail(email) {
	  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  if( !emailReg.test( email ) ) {
	    return false;
	  } else {
	    return true;
	  }
}

function confirmEmail(email, emailRepeat) {
	var emailVal = $('#'+email).val().trim();
	var emailRepeatVal = $('#'+emailRepeat).val().trim();
	if(validateEmail(emailVal) && validateEmail(emailRepeatVal)) {
		if(emailVal != "" && emailRepeatVal != "" && emailVal == emailRepeatVal) {
			return true;
		}
	}
	return false;
}

function confirmPasswords(password, passwordRepeat) {
	var emailVal = $('#'+email).val().trim();
	var emailRepeatVal = $('#'+emailRepeat).val().trim();
	if(validateEmail(emailVal) && validateEmail(emailRepeatVal)) {
		if(emailVal != "" && emailRepeatVal != "" && emailVal == emailRepeatVal) {
			return true;
		}
	}
	return false;
}

function validateUserForm(email, emailRepeat, userName, superAdminRole, administratorRole, page) {
	var role = $('#rolesId option:selected').text();
	if(!(confirmEmail(email, emailRepeat))) {
		toastr.error('EmailId do not match');
		return false;
	}
	if($('#'+userName).val() == "") {
		toastr.error('User name is required');
		return false;
	}
	if(page != "mySettings" && role != superAdminRole && role != administratorRole &&($('#selectNonAdminTeam').val() == null || $('#selectNonAdminTeam').val() == "")) {
		toastr.error('Team is mandatory');
		return false;
	}
	return true;
}

function confirmEmailId(input) {
    if ($("#emailId").val() != "" && $("#emailIdRepeat").val() != "" && $("#emailId").val() != $("#emailIdRepeat").val()) {
    	document.getElementById("emailIdRepeat").setCustomValidity("Please confirm email address.");
    	//input.setCustomValidity("Please confirm email address.");
        return false;
    } else {
    	document.getElementById("emailIdRepeat").setCustomValidity("");
    	//input.setCustomValidity("");
        return true;
    }
}

function confirmPassword(input) {
	if ($("#password").val() != "" && $("#passwordRepeat").val() != "" && $("#password").val() != $("#passwordRepeat").val()) {
		document.getElementById("passwordRepeat").setCustomValidity("Please confirm password.");
		//input.setCustomValidity("Please confirm password.");
        return false;
    } else {
    	document.getElementById("passwordRepeat").setCustomValidity("");
    	//input.setCustomValidity("");
        return true;
    }
}

function confirmDelete(msg) {
	return confirm(msg);
}

function setUpdateUserId(userId, hiddenVal) {
	$("#"+hiddenVal).val(userId);
	document.userSetupForm.action = "initEditUser.lin";
	document.userSetupForm.submit();
}

function setCompanyId(companyId, companyType) {
	$("#companyId").val(companyId);
	$('#companyTypeToUpdate').val(companyType);
	document.companySetupForm.action = "initEditCompany.lin";
	document.companySetupForm.submit();
}

function fromCreateToUserSetup() {
	document.createNewUserForm.action = "userSetup.lin";
	document.createNewUserForm.submit();
}

function fromUpdateToUserSetup() {
	document.updateUserForm.action = "userSetup.lin";
	document.updateUserForm.submit();
}

function fromCreateToCompanySetup() {
	document.createCompanyForm.action = "companySetup.lin";
	document.createCompanyForm.submit();
}

function fromUpdateToCompanySetup() {
	document.updateCompanyForm.action = "companySetup.lin";
	document.updateCompanyForm.submit();
}

function fromCreateToPropertySetup() {
	document.createPropertyForm.action = "propertySetup.lin";
	document.createPropertyForm.submit();
}

function fromUpdateToPropertySetup() {
	document.updatePropertyForm.action = "propertySetup.lin";
	document.updatePropertyForm.submit();
}

function removeUser(userIdToDelete) {
	var table = $('#userSetupTable').DataTable();
	var row = $('#userIdToDelete_'+userIdToDelete).closest("tr").get(0);
	var confirmVal = confirmDelete("Are you sure you want to remove the user ?");
	if(!confirmVal) {
		return false;
	}
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/deleteUser.lin",
	      cache: false,
	      data : {
	    	  userIdToDelete : userIdToDelete
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  table.fnDeleteRow(table.fnGetPosition(row));
	    	  toastr.success('User removed successfully');
	     },
	     error: function(jqXHR, exception) {
	    	 toastr.error('Remove operation failed');
	     }
	   });   
	}catch(error){
		toastr.error('Remove operation failed');
	}
}

function resendAuthorizeEmail(userId) {
	var confirmVal = confirmDelete("Are you sure you want to resend Invite ?");
	if(!confirmVal) {
		return false;
	}
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/resendAuthorizeEmail.lin",
	      cache: false,
	      data : {
	    	  userId : userId
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  if(data != null && data != undefined) {
	    		  var message = data.message;
	    		  if(data.error != null && data.error != undefined) {
	    			  toastr.error(message);
	    		  }else {
	    			  if(data.userStatus != null && data.userStatus != undefined) {
	    				  $('#invite_'+userId).hide();
	    				  $('#status_'+userId).html(data.userStatus);
	    				  toastr.info(message);
	    			  }else {
	    				  toastr.success(message);
	    			  }
	    		  }
	    	  }else {
	    		  toastr.error('Invite operation failed');
	    	  }
	     },
	     error: function(jqXHR, exception) {
	    	 toastr.error('Invite operation failed');
	     }
	   });   
	}catch(error){
		toastr.error('Invite operation failed');
	}
}

function isAvailable(checkValId, entityId, oldCheckVal, action) {
	var checkVal = $('#'+checkValId).val().trim();
	if(checkVal == "") {
		$('#textValueAvailableText').css("display","none");
		$('#textValueAvailableImage').css("display","none");
		return;
	}
	else if(checkVal == oldCheckVal) {
		$("#submitButton").removeAttr("disabled");
		$('#textValueAvailableText').css("display","none");
		$('#textValueAvailableImage').css("display","none");
		return;
	}
	$("#submitButton").attr("disabled", "disabled");
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/"+action+".lin",
	      cache: false,
	      data : {
	    	  checkVal : checkVal,
	    	  id : entityId
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $('#textValueAvailableImage').css("display","block");
	    	  $('#textValueAvailableText').css("display","none");
	    	  $("#submitButton").removeAttr("disabled");
	     },
	     error: function(jqXHR, exception) {
	    	 $('#textValueAvailableText').css("display","block");
	    	 $('#textValueAvailableImage').css("display","none");
	    	 //$('#'+checkValId).focus();
	     }
	   });   
	  }catch(error){
		  $('#textValueAvailableText').css("display","none");
		  $('#textValueAvailableImage').css("display","none");
		  //$('#'+checkValId).focus();
	  }
}

function accountOrPropertyNameAvailable(name, accountType, action) {
	$("#submitButton").attr("disabled", "disabled");
	if($.trim(name) == "") {
		$('#textValueAvailableText').css("display","none");
		$('#textValueAvailableImage').css("display","none");
		return;
	}
	var networkId = $('#networkId').val();
	var networkUsername = $('#networkUsername').val();
	var companyId = $('#companyId').val();
	if(networkId == undefined || networkId == null || $.trim(networkId) == '' || 
		networkUsername == undefined || networkUsername == null || $.trim(networkUsername) == '' ) {
		toastr.error('DFP Credentials are mandatory');
		return false;
	}
	try{
	    $.ajax({
	      type : "POST",
	      url : "/"+action+".lin",
	      cache: false,
	      data : {
	    	  name : name,
	    	  companyId : companyId,
	    	  accountType : accountType
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $('#textValueAvailableImage').css("display","block");
	    	  $('#textValueAvailableText').css("display","none");
	    	  if(validAccountflag) {
	    		  $("#submitButton").removeAttr("disabled");
	    	  }
	    	  else {
	    		  validAccountflag = true;
	    	  }
	     },
	     error: function(jqXHR, error) {
	    	 $('#textValueAvailableText').css("display","block");
	    	 $('#textValueAvailableImage').css("display","none");
	    	 console.log("Error in accountOrPropertyNameAvailable() : "+error);
	     }
	   });   
	  }catch(exception){
		  $('#textValueAvailableText').css("display","none");
		  $('#textValueAvailableImage').css("display","none");
		  console.log("Exception in accountOrPropertyNameAvailable() : "+exception);
	  }
}

function isDfpIdAvailable(dfpId, action, isProperty) {
	$("#submitButton").attr("disabled", "disabled");
	if($.trim(dfpId) == "") {
		$('#dfpAvailableText').css("display","none");
		$('#dfpAvailableImage').css("display","none");
		return;
	}
	var networkId = $('#networkId').val();
	var networkUsername = $('#networkUsername').val();
	var companyId = $('#companyId').val();
	if(networkId == undefined || networkId == null || $.trim(networkId) == '' || 
		networkUsername == undefined || networkUsername == null || $.trim(networkUsername) == '' ) {
		toastr.error('DFP Credentials are mandatory');
		return false;
	}
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/"+action+".lin",
	      cache: false,
	      data : {
	    	  dfpId : dfpId,
	    	  companyId : companyId
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $('#dfpAvailableImage').css("display","block");
	    	  $('#dfpAvailableText').css("display","none");
	    	  if(validAccountflag) {
	    		  $("#submitButton").removeAttr("disabled");
	    	  }
	    	  else {
	    		  validAccountflag = true;
	    	  }
	    	  if(networkId == '5678' && isProperty) {
	    		  getAdUnitChilds(dfpId, networkId, networkUsername);
	    	  }
	     },
	     error: function(jqXHR, error) {
	    	 $('#dfpAvailableText').css("display","block");
	    	 $('#dfpAvailableImage').css("display","none");
	    	 $.trim($('#accountDfpId').val(""));
	    	 console.log("Error in isDfpIdAvailable() : "+error);
	     }
	   });   
	  }catch(exception){
		  $('#dfpAvailableText').css("display","none");
		  $('#dfpAvailableImage').css("display","none");
		  console.log("Exception in isDfpIdAvailable() : "+exception);
	  }
}

function isAccountOrPropertyNameAvailable(checkValId, entityId, oldCheckVal, action) {
	var name = $('#'+checkValId).val().trim();
	if(name == "") {
		$('#textValueAvailableText').css("display","none");
		$('#textValueAvailableImage').css("display","none");
		return;
	}
	else if(name == oldCheckVal) {
		$("#submitButton").removeAttr("disabled");
		$('#textValueAvailableText').css("display","none");
		$('#textValueAvailableImage').css("display","none");
		return;
	}
	var accountType = $('#accountType').val();
	var networkId = $('#networkId').val();
	var networkUsername = $('#networkUsername').val();
	var companyId = $('#companyId').val();
	if(networkId == undefined || networkId == null || $.trim(networkId) == '' || 
		networkUsername == undefined || networkUsername == null || $.trim(networkUsername) == '' ) {
		toastr.error('DFP Credentials are mandatory');
		return false;
	}
	$("#submitButton").attr("disabled", "disabled");
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/"+action+".lin",
	      cache: false,
	      data : {
	    	  id : entityId,
	    	  name : name,
	    	  accountType : accountType,
	    	  companyId : companyId
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $('#textValueAvailableImage').css("display","block");
	    	  $('#textValueAvailableText').css("display","none");
	    	  if(($.trim($('#accountDfpId').val()) != "" && $.trim($('#accountDfpId').val()) != undefined) || ($.trim($('#DFPPropertyId').val()) != "" && $.trim($('#DFPPropertyId').val()) != undefined)) {
	    		  $("#submitButton").removeAttr("disabled");
	    	  }
	     },
	     error: function(jqXHR, error) {
	    	 $('#textValueAvailableText').css("display","block");
	    	 $('#textValueAvailableImage').css("display","none");
	    	 console.log("Error in isAccountOrPropertyNameAvailable() : "+error);
	     }
	   });   
	  }catch(exception){
		  $('#textValueAvailableText').css("display","none");
		  $('#textValueAvailableImage').css("display","none");
		  console.log("Exception in isAccountOrPropertyNameAvailable() : "+exception);
	  }
}

function isEmailIdAvailable(checkValId, entityId, oldCheckVal, action) {
	var checkVal = $('#'+checkValId).val().trim();
	if(checkVal == "") {
		$('#textValueAvailableText').css("display","none");
		$('#textValueAvailableImage').css("display","none");
		return;
	}
	else if(checkVal == oldCheckVal) {
		$("#submitButton").removeAttr("disabled");
		$('#textValueAvailableText').css("display","none");
		$('#textValueAvailableImage').css("display","none");
		return;
	}
	if (validateEmail(checkVal)) {
		$("#submitButton").attr("disabled", "disabled");
		try{	  
		    $.ajax({
		      type : "POST",
		     /* url : "/emailIdAvailable.lin",*/
		      url : "/"+action+".lin",
		      cache: false,
		      data : {
		    	  checkVal : checkVal,
		    	  id : entityId
			    	 },		    
		      dataType: 'json',
		      success: function (data) {
		    	  $('#textValueAvailableImage').css("display","block");
		    	  $('#textValueAvailableText').css("display","none");
		    	  $("#submitButton").removeAttr("disabled");
		     },
		     error: function(jqXHR, exception) {
		    	 $('#textValueAvailableText').css("display","block");
		    	 $('#textValueAvailableImage').css("display","none");
		    	 //$('#'+checkValId).focus();
		     }
		   });   
	  }	catch(error){
		  $('#textValueAvailableText').css("display","none");
		  $('#textValueAvailableImage').css("display","none");
		  //$('#'+checkValId).focus();
	  }
	}
}

function setTimezone(selectedOption) {
	//$("#timezone").val(selectedOption);
	setTimeout(function(){$("#timezone").val(selectedOption)},100);
}

function setUpdateRoleId(roleId, hiddenVal) {
	$("#"+hiddenVal).val(roleId);
	document.roleSetupForm.action = "initEditRole.lin";
	document.roleSetupForm.submit();
}

function deleteRole() {
	document.updateRoleForm.action = "deleteRole.lin";
	document.updateRoleForm.submit();
}

function copyRole() {
	if($('#rolesId').val() == -1) {
		return false;
	}
	document.createRoleForm.action = "copyRole.lin";
	document.createRoleForm.submit();
}

function copyRoleUpdate() {
	if($('#rolesId').val() == -1) {
		return false;
	}
	document.updateRoleForm.action = "copyRoleUpdate.lin";
	document.updateRoleForm.submit();
}

function fromCreateToRoleSetup() {
	document.createRoleForm.action = "roleSetup.lin";
	document.createRoleForm.submit();
}

function fromUpdateToRoleSetup() {
	document.updateRoleForm.action = "roleSetup.lin";
	document.updateRoleForm.submit();
}

function fromCreateToTeamSetup() {
	document.createTeamForm.action = "teamSetup.lin";
	document.createTeamForm.submit();
}

function fromUpdateToTeamSetup() {
	document.updateTeamForm.action = "teamSetup.lin";
	document.updateTeamForm.submit();
}

function setUpdateTeam(teamId, hiddenVal) {
	$("#"+hiddenVal).val(teamId);
	document.teamSetupForm.action = "initEditTeam.lin";
	document.teamSetupForm.submit();
}

function setPublisherId(publisherId, hiddenVal) {
	$("#"+hiddenVal).val(publisherId);
	document.publisherSetupForm.action = "initEditPublisher.lin";
	document.publisherSetupForm.submit();
}

function fromUpdateToPublisherSetup() {
	document.updatePublisherForm.action = "publisherSetup.lin";
	document.updatePublisherForm.submit();
}

function setTeamsByRole(superAdminRole,administratorRole) {
	var role = $('#rolesId option:selected').text();
	if(role == superAdminRole) {
		$('#teamDiv').css('display','none');
	}else if(role == administratorRole) {
		$('#selectNonAdminTeamDiv').css('display','none');
		$('#selectAdminTeamDiv').css('display','block');
		$('#teamDiv').css('display','block');
	}
	else {
		$('#selectNonAdminTeamDiv').css('display','block');
		$('#selectAdminTeamDiv').css('display','none');
		$('#teamDiv').css('display','block');
	}
	$('#selectedRoleType').val(role);
}

function setDemandPartnerId(demandPartnerId, hiddenVal) {
	$("#"+hiddenVal).val(demandPartnerId);
	document.demandPartnerSetupForm.action = "initEditDemandPartner.lin";
	document.demandPartnerSetupForm.submit();
}

function fromUpdateToDemandPartnerSetup() {
	document.updateDemandPartnerForm.action = "demandPartnerSetup.lin";
	document.updateDemandPartnerForm.submit();
}

function addMorePassbackSiteType() {
	var newTextBoxDiv = $(document.createElement('div')).attr("id", 'passbackSiteTypeDiv' + passbackSiteTypeCounter);
	if($("#passbackSiteTypeGroup").has("div").length > 0) {
		newTextBoxDiv.attr("class", 'clearBoth');
	}
	newTextBoxDiv.after().html('<div class="floatLeft"><label class="passBackLabel">Passback Site Type : </label></div>' +
						      '<div class="passBackInput"><input type="text" id="passbackSiteType' + passbackSiteTypeCounter + '" name="passbackSiteType"></div>' +
						      '<div class="passBackInput"><img class="icon-zoom-in" onclick="removePassbackSiteType('+ passbackSiteTypeCounter +');" src="../img/close_icon.png"></div>');
	newTextBoxDiv.appendTo("#passbackSiteTypeGroup");
	passbackSiteTypeCounter++;
}

function removePassbackSiteType(id) {
		$("#passbackSiteTypeDiv" + id).remove();
}

function addMoreServiceURL() {
	var newTextBoxDiv = $(document.createElement('div')).attr("id", 'serviceURLDiv' + serviceURLCounter);
	if($("#serviceURLGroup").has("div").length > 0) {
		newTextBoxDiv.attr("class", 'clearBoth');
	}
	newTextBoxDiv.after().html('<div class="floatLeft"><label class="passBackLabel"></label></div>' +
						      '<div class="passBackInput"><input type="text" id="serviceURL' + serviceURLCounter + '" name="serviceURL"></div>' +
						      '<div class="passBackInput"><img class="icon-zoom-in" onclick="removeServiceURL('+ serviceURLCounter +');" src="../img/close_icon.png"></div>');
	newTextBoxDiv.appendTo("#serviceURLGroup");
	serviceURLCounter++;
}

function removeServiceURL(id) {
	$("#serviceURLDiv" + id).remove();
}

function setPropertyId(propertyId, hiddenVal) {
	$("#"+hiddenVal).val(propertyId);
	document.propertySetupForm.action = "initEditProperty.lin";
	document.propertySetupForm.submit();
}

function addMoreAdServerCredentials() {
	var newTextBoxDiv = $(document.createElement('div')).attr("id", 'adServerCredentialsDiv' + adServerCredentialsCounter);
	if($("#adServerCredentialsGroup").has("div").length > 0) {
		newTextBoxDiv.addClass('clearBoth'); 
	}
	newTextBoxDiv.after().html('<div class="floatLeft"><input required="required" maxlength="49" placeholder="Ad server id" type="text" id="adServerId' + adServerCredentialsCounter + '" name="adServerId"></div>' +
								'<div class="passBackInput"><input required="required" maxlength="49" autocomplete="off" placeholder="Ad server username" type="text" id="adServerUsername' + adServerCredentialsCounter + '" name="adServerUsername"></div>' +
								'<div class="passBackInput"><input required="required" maxlength="49" autocomplete="off" placeholder="Ad server password" type="password" id="adServerPassword' + adServerCredentialsCounter + '" name="adServerPassword" onpaste="return false" oncopy="return false" ondrag="return false" ondrop="return false"></div>' +
								'<div class="passBackInput"><img class="icon-zoom-in" onclick="removeAdServerCredentials('+ adServerCredentialsCounter +');" src="../img/close_icon.png"></div>');
	newTextBoxDiv.appendTo("#adServerCredentialsGroup");
	adServerCredentialsCounter++;
}

function removeAdServerCredentials(id) {
		$("#adServerCredentialsDiv" + id).remove();
}

function setCompanyTypeToCreate(companyType) {
	$('#selectCompanyTypeLabel').html('New '+companyType+'<span class="caret" style="margin-left: 6px;margin-top: 8px;"></span>');
	$('#companyTypeToCreate').val(companyType);
	document.companySetupForm.action = "initCreateNewCompany.lin";
	document.companySetupForm.submit();
}

function fetchTeamsByCompanyId(teamAllEntity) {
	var companyId = $('#selectCompany').val();
	var jsonObj = [];
	$("#selectAdminTeam").select2("data",jsonObj);
	$("#selectNonAdminTeam").select2("data",jsonObj);
	$('#selectAdminTeam').html('');
	$('#selectNonAdminTeam').html('');
	if(companyId != undefined && companyId != '-1' && companyId != '') {
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/fetchTeamsByCompanyId.lin",
		      cache: false,
		      data : {
		    	  companyId : companyId
			      },		    
		      dataType: 'json',
		      success: function (data) {
		    	  $.each(data, function(index, element) {
		    		  if (index == 'nonAdminTeamList' && element != null  && element != undefined  && element.length > 0) {
		    			  jsonObj = [];
		    			  $.each(element,function (newIndex,newElement){		         		  
			         		  $('#selectNonAdminTeam').append('<option value="'+newElement.id+'">'+newElement.value+'</option>');
			         		  if(newElement.value.indexOf(teamAllEntity) >= 0) {
			         			 $('#selectAdminTeam').append('<option value="'+newElement.id+'">'+newElement.value+'</option>');
			         			 $("#selectAdminTeam").select2("val", newElement.id);
			         		  }
			         	   });
		    		  }
		    	  });
		     },
		     error: function(jqXHR, exception) {
		     }
		   });   
	  }catch(error){
	  }
   }
}

function fetchActiveRolesByCompanyId(superAdminRole, administratorRole, isSuperAdmin) {
	var companyId = $('#selectCompany').val();
	var jsonObj = [];
	$("#rolesId").select2("data",jsonObj);
	$('#rolesId').html('');
	if(companyId != undefined && companyId != '-1' && companyId != '') {
		try{	  
	    $.ajax({
	      type : "POST",
	      url : "/fetchActiveRolesByCompanyId.lin",
	      cache: false,
	      data : {
	    	  companyId : companyId
		      },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
	    		  if (index == 'rolesList' && element != null  && element != undefined  && element.length > 0) {
	    			  $.each(element,function (newIndex,newElement){
	    				  if(!(!isSuperAdmin && newElement.value == superAdminRole)) {
	    					  $('#rolesId').append('<option value="'+newElement.id+'">'+newElement.value+'</option>');
	    				  }
		         	   });
	    			  setTeamsByRole(superAdminRole,administratorRole);
	    		  }
	    	  });
	     },
	     error: function(jqXHR, exception) {
	     }
	   });   
	  }catch(error){
	  }
   }
}

function getTeamDataByCompany(publisherPartner, demandPartner, client) {
	var jsonObj = [];
	var company = $('#selectCompany').val();
	$('#selectedCompanyType').val('');
	
	$("input:radio").removeAttr("checked");
	$("#radioChooseAccounts").prop("checked", "checked");
	$("#uniform-radioChooseAccounts span").attr("class", "checked");
	$("#radioAllAccounts").removeAttr("checked");
	$("#uniform-radioAllAccounts span").attr("class", "");
	$("#radioNoRestrictions").removeAttr("checked");
	$("#uniform-radioNoRestrictions span").attr("class", "");
	
	$('#accountSelector').show();
	

	$("#selectAppViews").select2("data",jsonObj);
	$('#selectAppViews').html('');
	$('#selectAppViewsLoaderDiv').css('display', 'block');
	
	$("#selectProperties").select2("data",jsonObj);
	$('#selectProperties').html('');
	$('#selectPropertiesLoaderDiv').css('display', 'block');
	
	$("#selectAccounts").select2("data",jsonObj);
	$('#selectAccounts').html('');
	$('#selectAdServerAccountsLoaderDiv').css('display', 'block');
	
	$("#selectedAdServerAccounts").select2("data",jsonObj);
	$('#selectedAdServerAccounts').html('');
	$('#selectAdServerAccountsLoaderDiv').css('display', 'block');
	$('#publisherIdsForBigQuery').val("");
	
	$('#accessToAccounts').val('');
	
	if(company == null || company == undefined || company == '') {
		$('#selectAppViewsLoaderDiv').css('display', 'none');
   	 	$('#selectPropertiesLoaderDiv').css('display', 'none');
   	 	$('#selectAdServerAccountsLoaderDiv').css('display', 'none');
		return true;
	}
	
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadTeamDataByCompany.lin",
	      cache: false,
	      data : {
	    	  companyId : company
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
	    		  if (index == 'teamDataList' && element != null  && element != undefined  && element.length > 0) {
	    			  element = element[0];
	    			  if(element.accessToAccounts) {
	    				  var accounts = element.accountsList;
	    				  if(accounts != null && accounts != undefined && accounts.length > 0) {
	    					  $.each(accounts,function (accountIndex,accountElement){
	    		         		  $('#selectAccounts').append('<option value="'+accountElement.id+'">'+accountElement.value+'</option>');
	    		         	  });
	    				  }
	    				  $('#accountsDivId').css("display", "block");
	    				  $('#accessToAccounts').val('1');
	    			  }
	    			  else {
	    				  $('#accountsDivId').css("display", "none");
	    				  $('#accessToAccounts').val('0');
	    			  }
	    			  
	    			  if(element.accessToProperties) {
	    				  var property = element.propertyList;
	    				  if(property != null && property != undefined && property.length > 0) {
	    					  $.each(property,function (propertyIndex,propertyElement){
	    		         		  $('#selectProperties').append('<option value="'+propertyElement.id+'">'+propertyElement.value+'</option>');
	    		         	  });
	    				  }
	    				  $('#propertiesDivId').css("display", "block");
	    				  $('#accessToProperties').val('1');
	    			  }
	    			  else {
	    				  $('#propertiesDivId').css("display", "none");
	    				  $('#accessToProperties').val('0');
	    			  }
	    			  
	    			  var appView = element.appViews;
    				  if(appView != null && appView != undefined && appView.length > 0) {
    					  $.each(appView,function (appViewIndex,appViewElement){
    		         		  $('#selectAppViews').append('<option value="'+appViewElement.id+'">'+appViewElement.value+'</option>');
    		         	  });
    				  }
    				  
    				  var publisherIdsForBQ = element.publisherIdsForBigQuery;
    				  if (publisherIdsForBQ != null  && publisherIdsForBQ != undefined) {
    	    			  $('#publisherIdsForBigQuery').val(publisherIdsForBQ);
    	    		  }
    				  $('#selectedCompanyType').val(element.companyType);
    				  if(element.companyType == publisherPartner) {
    					  /*$('#testDiv').show();*/
    					  $('#testDiv').css("visibility","visible");
    				  }
    				  else {
    					 /* $('#testDiv').hide();*/
    					  $('#testDiv').css("visibility","hidden");
    				  }
	    		  }
	    	  });
	    	  $('#selectAppViewsLoaderDiv').css('display', 'none');
	    	  $('#selectPropertiesLoaderDiv').css('display', 'none');
	    	  $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     },
	     error: function(jqXHR, exception) {
	    	 $('#selectAppViewsLoaderDiv').css('display', 'none');
	    	 $('#selectPropertiesLoaderDiv').css('display', 'none');
	    	 $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     }
	   });   
	}catch(error){
		$('#selectAppViewsLoaderDiv').css('display', 'none');
  	    $('#selectPropertiesLoaderDiv').css('display', 'none');
  	    $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	}
	
	 /*if($("#companyID"+company).val() == client) {
		$('#selectedCompanyType').val(client);
		$('#propertiesDivId').css("display", "none");
		
		$("#selectedAdServerAccounts").select2("data",jsonObj);
		$('#selectedAdServerAccounts').html('');
		$('#accountsDivId').css("display", "block");
		getPublisherIdsForBigQueryByCompanyId(company);
	 }
	 else if($("#companyID"+company).val() == demandPartner) {
		$('#selectedCompanyType').val(demandPartner);
		$('#propertiesDivId').css("display", "none");
		$('#accountsDivId').css("display", "none");
		//getPublisherIdsForBigQueryByCompanyId(company);
	 }
	 else if($("#companyID"+company).val() == publisherPartner) {
		$('#selectedCompanyType').val(publisherPartner);
		
		$("#selectProperties").select2("data",jsonObj);
		$('#selectProperties').html('');
		$('#propertiesDivId').css("display", "block");
		$('#selectPropertiesLoaderDiv').css('display', 'block');
		
		$("#selectedAdServerAccounts").select2("data",jsonObj);
		$('#selectedAdServerAccounts').html('');
		$('#accountsDivId').css("display", "block");
		getPublisherIdsForBigQueryByCompanyId(company);
		
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadPublisherPartnerDataForTeam.lin",
		      cache: false,
		      data : {
		    	  companyId : company
			    	 },		    
		      dataType: 'json',
		      success: function (data) {
		    	  $.each(data, function(index, element) {
		    		  if (index == 'propertiesDropDownList' && element != null  && element != undefined  && element.length > 0) {
		    			  $.each(element,function (newIndex,newElement){
			         		  $('#selectProperties').append('<option value="'+newElement.id+'">'+newElement.value+'</option>');
			         	   });
		    		  }
		    	  });
		    	  $('#selectPropertiesLoaderDiv').css('display', 'none');
		     },
		     error: function(jqXHR, exception) {
		    	 $('#selectPropertiesLoaderDiv').css('display', 'none');
		     }
		   });   
		}catch(error){
			$('#selectPropertiesLoaderDiv').css('display', 'none');
		}
	 }
	 
	 var currentCompanyType = $('#selectedCompanyType').val();
	 if(previousCompanyType != currentCompanyType) {
		 getAppViewsForCompanyType(currentCompanyType);
	 }*/
}

function getAppViewsForCompanyType(companyType) {
	var jsonObj = [];
	$("#selectAppViews").select2("data",jsonObj);
	$('#selectAppViews').html('');
	$('#selectAppViewsLoaderDiv').css('display', 'block');
	
	if(companyType == null || companyType == undefined || companyType == '') {
		return true;
	}
	
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadDefaultAppViewsForCompanyType.lin",
	      cache: false,
	      data : {
	    	  companyType : companyType
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
	    		  if (index == 'appViewsList' && element != null  && element != undefined  && element.length > 0) {
	    			  $.each(element,function (newIndex,newElement){
		         		  $('#selectAppViews').append('<option value="'+newElement.id+'">'+newElement.value+'</option>');
		         	  });
	    		  }
	    		  if (index == 'defaultSelectedAppViewsList' && element != null  && element != undefined  && element.length > 0) {
	    			  $.each(element,function (newIndex,newElement){
	    	     	      jsonObj.push(newElement);
		         	  });
	    			  $("#selectAppViews").select2("val", jsonObj);
	    		  }
	    	  });
	    	  $('#selectAppViewsLoaderDiv').css('display', 'none');
	     },
	     error: function(jqXHR, exception) {
	    	 $('#selectAppViewsLoaderDiv').css('display', 'none');
	     }
	   });   
	 }catch(error){
		 $('#selectAppViewsLoaderDiv').css('display', 'none');
	 }
}

/*function getPublisherIdsForBigQueryByCompanyId(company) {
	$('#publisherIdsForBigQuery').val("");
	$('#selectAdServerAccountsLoaderDiv').css('display', 'block');
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadPublisherIdsForBigQueryByCompanyId.lin",
	      cache: false,
	      data : {
	    	  companyId : company
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
	    		  if (index == 'publisherIdsForBigQuery' && element != null  && element != undefined) {
	    			  $('#publisherIdsForBigQuery').val(element);
	    		  }
	    	  });
	    	  $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     },
	     error: function(jqXHR, exception) {
	    	 $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     }
	   });   
  }catch(error){
	  $('#selectAdServerAccountsLoaderDiv').css('display', 'none');
  }
}*/

function selectGivenTeamFromDropDown(dropDownId, team1, team2) {
	// usable only if given value is the substring of id
	var team = $('#'+dropDownId).val();
	var allTeamText =  $('#'+dropDownId+ ' option:selected').text();
	if(allTeamText.indexOf(team1) >= 0 || allTeamText.indexOf(team2) >= 0) {
		for(i=0;i<team.length;i++) {
			if(team[i].indexOf(team1) >= 0 || team[i].indexOf(team2) >= 0) {
				var teamId = team[i];
				var teamText = $("#"+dropDownId+ " option[value='"+team[i]+"']").text();
				var item = {};
				var jsonObj = [];
     			item ["id"] = teamId;
     	        item ["text"] = teamText;
     	        jsonObj.push(item);
     	        $('#'+dropDownId).select2("data",jsonObj);
     	        break;
			}
		}
	}
}

function selectGivenValueFromDropDown(dropDownId, value) {
	// usable only if id and text of given value are same.
	var selectedValue = $('#'+dropDownId).val();
	var allSelectedValueText =  $('#'+dropDownId+ ' option:selected').text();
	if(allSelectedValueText.indexOf(value) >= 0) {
		var item = {};
		var jsonObj = [];
		item ["id"] = value;
        item ["text"] = value;
        jsonObj.push(item);
        $('#'+dropDownId).select2("data",jsonObj);
	}
}

function changePublisherForProperty() {
	var jsonObj = [];
	$('#networkId').val('');
    $('#networkUsername').val('');
    
    $('#propertyDfpId').val('');
	$('#DFPPropertyName').val('');
	$('#propertyName').val('');
	$('#textValueAvailableImage').css("display","none");
	$('#textValueAvailableText').css("display","none");
	$("#submitButton").removeAttr("disabled");
    
    $('#selectDfpCredentialsDiv').hide();
	$('#showDfpCredentials').hide();
	$('#noDfpCredentialsMessage').hide();
	$('#selectDfpCredentials').select2("data",jsonObj);
	$('#selectDfpCredentials').html('');
	
	var publisherId = $('#companyId').val();
	if(publisherId == null || publisherId == undefined || publisherId == '') {
		return true;
	}
	
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadAdserverCredentialsByCompanyId.lin",
	      cache: false,
	      data : {
	    	  companyId : publisherId
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
	    		  if (index == 'serverCredentialsList') {
	    			  if(element == null  || element.length == undefined || element.length == 0) {
	    				  $('#selectDfpCredentialsDiv').hide();
	    				  $('#showDfpCredentials').hide();
	    				  $('#noDfpCredentialsMessage').show();
	    				  $('#networkId').val('');
	    				  $('#networkUsername').val('');
	    			  }
	    			  else if(element.length == 1) {
	    				  $('#noDfpCredentialsMessage').hide();
	    				  $('#selectDfpCredentialsDiv').hide();
	    				  $('#showDfpCredentials').show();
	    				  $('#showDfpCredentials').html('Ad server id : '+element[0].adServerId+'<br> Ad server username : '+element[0].adServerUsername);
	    				  $('#networkId').val(element[0].adServerId);
	    				  $('#networkUsername').val(element[0].adServerUsername);
	    			  }
	    			  else if(element.length > 1) {
	    				  $('#noDfpCredentialsMessage').hide();
	    				  $('#showDfpCredentials').hide();
	    				  $.each(element,function (newIndex,newElement){
		    	     	     $('#selectDfpCredentials').append('<option value="'+newElement.adServerIdUsername+'">'+newElement.customizedValue+'</option>');
			         	  });
		    			  $("#selectDfpCredentials").select2("val", element[0].adServerIdUsername);
	    				  $('#selectDfpCredentialsDiv').show();
	    				  $('#networkId').val(element[0].adServerId);
	    				  $('#networkUsername').val(element[0].adServerUsername);
	    			  }
	    		  }
	    	  });
	    	  //$('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     },
	     error: function(jqXHR, exception) {
	    	 //$('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     }
	   });   
	}catch(error){
		//$('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	}
	
}

function addCssClassOnDFPCredentials() {
	$('[name="adServerId"]').each (function () {
		$('#'+this.id).attr("required","required");
    });
	$('[name="adServerUsername"]').each (function () {
		$('#'+this.id).attr("required","required");
    });
	$('[name="adServerPassword"]').each (function () {
		$('#'+this.id).attr("required","required");
    });
}

function removeCssClassOnDFPCredentials() {
	$('[name="adServerId"]').each (function () {
		$('#'+this.id).removeAttr("required");
    });
	$('[name="adServerUsername"]').each (function () {
		$('#'+this.id).removeAttr("required");
    });
	$('[name="adServerPassword"]').each (function () {
		$('#'+this.id).removeAttr("required");
    });
}

function setPageToCreateClient() {
	$('#demandPartnerInfoDiv').css('display', 'none');
	$('#publisherPartnerInfoDiv').css('display', 'none');
	$('#dataSource').removeAttr('required');
	$('#accessPropertiesCheckBoxDiv').css('display', 'none');
}

function setPageToCreatePublisher() {
	$('#demandPartnerInfoDiv').css('display', 'none');
	$('#publisherPartnerInfoDiv').css('display', 'block');
	$('#dataSource').removeAttr('required');
	$('#accessPropertiesCheckBoxDiv').css('display', 'block');
}

function setPageToCreateDemandPartner() {
	$('#demandPartnerInfoDiv').css('display', 'block');
	$('#publisherPartnerInfoDiv').css('display', 'none');
	$('#dataSource').attr('required','required');
	$('#accessPropertiesCheckBoxDiv').css('display', 'none');
}

function fromCreateToAccountSetup() {
	document.createAccountForm.action = "accountSetup.lin";
	document.createAccountForm.submit();
}

function fromUpdateToAccountSetup() {
	document.updateAccountForm.action = "accountSetup.lin";
	document.updateAccountForm.submit();
}

function setUpdateAccount(accountId, hiddenVal) {
	$("#"+hiddenVal).val(accountId);
	document.accountSetupForm.action = "initEditAccount.lin";
	document.accountSetupForm.submit();
}

function validateAccounts() {
	//var company = $('#companyId').val();
	var industry = $('#industry').val();
	var networkId = $('#networkId').val();
	var networkUsername = $('#networkUsername').val();
	/*if(company == undefined || company == null || company == '' || company == '-1') {
		toastr.error('Invalid company');
		return false;
	}*/
	if(networkId == undefined || networkId == null || $.trim(networkId) == '' || networkUsername == undefined || networkUsername == null || $.trim(networkUsername) == '') {
		toastr.error('DFP Credentials are mandatory');
		return false;
	}
	/*if(industry == undefined || industry == null || industry == '' || industry == '-1') {
		toastr.error('Invalid industry');
		return false;
	}*/
	var dfpAccountName = $('#dfpAccountName').val();
	if(dfpAccountName == undefined || dfpAccountName == null || $.trim(dfpAccountName) == '') {
		toastr.error('DFP Account Name is mandatory');
		return false;
	}
	
	var accountName = $('#accountName').val();
	if(accountName == undefined || accountName == null || $.trim(accountName) == '') {
		toastr.error('Account is mandatory');
		return false;
	}
	return true;
}

function validateProperties() {
	var propertyName = $('#propertyName').val();
	if(propertyName == undefined || propertyName == null || $.trim(propertyName) == '') {
		toastr.error('Property Name is mandatory');
		return false;
	}
	
	var DFPPropertyName = $('#DFPPropertyName').val();
	if(DFPPropertyName == undefined || DFPPropertyName == null || $.trim(DFPPropertyName) == '') {
		toastr.error('DFP Property Name is mandatory');
		return false;
	}
	return true;
}

function getAdserverCredentialsByCompany(adServerCompanyId) {
	var jsonObj = [];
	$('#networkId').val('');
    $('#networkUsername').val('');
    
    $('#accountDfpId').val('');
	$('#dfpAccountName').val('');
	$('#accountType').val('');
	$('#showAccountType').html('');
	$('#accountName').val('');
	
	$('#DFPPropertyId').val('');
	$('#DFPPropertyName').val('');
	$('#propertyName').val('');
	
	$('#textValueAvailableImage').css("display","none");
	$('#textValueAvailableText').css("display","none");
	$("#submitButton").removeAttr("disabled");
    
    $('#selectDfpCredentialsDiv').hide();
	$('#showDfpCredentials').hide();
	$('#noDfpCredentialsMessage').hide();
	$('#selectDfpCredentials').select2("data",jsonObj);
	$('#selectDfpCredentials').html('');
	var companyId = $('#'+adServerCompanyId).val();
	if(companyId == undefined || companyId == null || $.trim(companyId) == '' || $.trim(companyId) == '-1') {
		$('#noDfpCredentialsMessage').show();
		return false;
	}
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadAdserverCredentialsByCompanyId.lin",
	      cache: false,
	      data : {
	    	  companyId : companyId
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
	    		  if (index == 'serverCredentialsList') {
	    			  if(element == null  || element.length == undefined || element.length == 0) {
	    				  $('#selectDfpCredentialsDiv').hide();
	    				  $('#showDfpCredentials').hide();
	    				  $('#noDfpCredentialsMessage').show();
	    				  $('#networkId').val('');
	    				  $('#networkUsername').val('');
	    			  }
	    			  else if(element.length == 1) {
	    				  $('#noDfpCredentialsMessage').hide();
	    				  $('#selectDfpCredentialsDiv').hide();
	    				  $('#showDfpCredentials').show();
	    				  $('#showDfpCredentials').html('Ad server id : '+element[0].adServerId+'<br> Ad server username : '+element[0].adServerUsername);
	    				  $('#networkId').val(element[0].adServerId);
	    				  $('#networkUsername').val(element[0].adServerUsername);
	    			  }
	    			  else if(element.length > 1) {
	    				  $('#noDfpCredentialsMessage').hide();
	    				  $('#showDfpCredentials').hide();
	    				  $.each(element,function (newIndex,newElement){
		    	     	     $('#selectDfpCredentials').append('<option value="'+newElement.adServerIdUsername+'">'+newElement.customizedValue+'</option>');
			         	  });
		    			  $("#selectDfpCredentials").select2("val", element[0].adServerIdUsername);
	    				  $('#selectDfpCredentialsDiv').show();
	    				  $('#networkId').val(element[0].adServerId);
	    				  $('#networkUsername').val(element[0].adServerUsername);
	    			  }
	    		  }
	    	  });
	    	  //$('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     },
	     error: function(jqXHR, exception) {
	    	 //$('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	     }
	   });   
	}catch(error){
		//$('#selectAdServerAccountsLoaderDiv').css('display', 'none');
	}
}

/*function setProppertyNetWorkInfo() {
	var selectDfpCredentials = $('#selectDfpCredentials').val();
	var array = selectDfpCredentials.split('<SEP>');
	$('#networkId').val(array[0]);
	$('#networkUsername').val(array[1]);
	
	$('#DFPPropertyId').val('');
	$('#DFPPropertyName').val('');
	$('#showAccountType').html('');
	$('#propertyName').val('');
	$('#textValueAvailableImage').css("display","none");
	$('#textValueAvailableText').css("display","none");
	$("#submitButton").removeAttr("disabled");
}*/

function setNetWorkInfo() {
	var selectDfpCredentials = $('#selectDfpCredentials').val();
	var array = selectDfpCredentials.split('<SEP>');
	$('#networkId').val(array[0]);
	$('#networkUsername').val(array[1]);
	
	$('#textValueAvailableImage').css("display","none");
	$('#textValueAvailableText').css("display","none");
	$("#submitButton").removeAttr("disabled");
	
	$('#accountDfpId').val('');
	$('#dfpAccountName').val('');
	$('#accountType').val('');
	$('#showAccountType').html('');
	$('#accountName').val('');
	
	$('#DFPPropertyId').val('');
	$('#DFPPropertyName').val('');
	$('#propertyName').val('');
}

function refreshDFPAccountMemcache() {
	dfpMemcacheStatus = false;
	var networkId = $('#networkId').val();
	var networkUsername = $('#networkUsername').val();
	if(networkId == undefined || networkId == null || $.trim(networkId) == '' || 
		networkUsername == undefined || networkUsername == null || $.trim(networkUsername) == '' ) {
		return false;
	}
	$('#selectDfpAccountNameLoaderDiv').css("visibility","visible");
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/refreshDFPAccountMemcache.lin",
	      cache: false,
	      data : {
	    	  networkId : networkId,
	    	  networkUsername : networkUsername
		    	 },		    
		    	 
	      dataType: 'json',
	      success: function (data) {
	    	  var interval = setInterval(check, 5000);
	    	  function check() {
	    		  checkDFPMemcache(networkId, networkUsername, 'checkDFPAccountMemcache');
	    		  if(dfpMemcacheStatus) {
	    			  clearInterval(interval);
	    			  $('#selectDfpAccountNameLoaderDiv').css("visibility","hidden");
	    	    	  toastr.success('List refreshed');
	    		  }
	    		}
	      },
	      error: function(jqXHR, error) {
	    	  console.log("Error in refreshDFPAccountMemcache() "+error);
	      }
	   });   
	}catch(exception){
		console.log("Exception in refreshDFPAccountMemcache() "+exception);
	}
}

function checkDFPMemcache(networkId, networkUsername, action) {
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/"+action+".lin",
	      cache: false,
	      data : {
	    	  networkId : networkId,
	    	  networkUsername : networkUsername
		    	 },		    
		    	 
	      dataType: 'json',
	      success: function (data) {
	    	  dfpMemcacheStatus = true;
	      },
	      error: function(jqXHR, error) {
	    	  dfpMemcacheStatus = false;
	      }
	   });   
	}catch(exception){
		console.log("Exception in checkDFPMemcache() "+exception);
	}
}

function refreshDFPPropertyMemcache() {
	dfpMemcacheStatus = false;
	var networkId = $('#networkId').val();
	var networkUsername = $('#networkUsername').val();
	if(networkId == undefined || networkId == null || $.trim(networkId) == '' || 
		networkUsername == undefined || networkUsername == null || $.trim(networkUsername) == '' ) {
		return false;
	}
	$('#selectDfpPropertyNameLoaderDiv').css("visibility","visible");
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/refreshDFPPropertyMemcache.lin",
	      cache: false,
	      data : {
	    	  networkId : networkId,
	    	  networkUsername : networkUsername
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  var interval = setInterval(check, 5000);
	    	  function check() {
	    		  checkDFPMemcache(networkId, networkUsername, 'checkDFPPropertyMemcache');
	    		  if(dfpMemcacheStatus) {
	    			  clearInterval(interval);
	    			  $('#selectDfpPropertyNameLoaderDiv').css("visibility","hidden");
	    	    	  toastr.success('List refreshed');
	    		  }
	    	  }
	      },
	      error: function(jqXHR, error) {
	    	  console.log("Error in refreshDFPPropertyMemcache() "+error);
	      }
	   });   
	}catch(exception){
		console.log("Exception in refreshDFPPropertyMemcache() "+exception);
	}
}

function getAdUnitChilds(dfpId, networkId, networkUsername) {
	var htmlContent = "";
	 $('#adUnitChildsDiv').html(htmlContent);
	$("#submitButton").attr("disabled", "disabled");
	if(networkId == undefined || networkId == null || $.trim(networkId) == '' || 
		networkUsername == undefined || networkUsername == null || $.trim(networkUsername) == '' ) {
		return false;
	}
	$('#selectDfpAccountNameLoaderDiv').css("visibility","visible");
	try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadAdUnitChilds.lin",
	      cache: false,
	      data : {
	    	  adServerId : networkId,
	    	  adServerUsername : networkUsername,
	    	  adUnitId : dfpId
		    	 },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $.each(data, function(index, element) {
	    		  if (index == 'siteList' && element != null && element.length != undefined && element.length > 0) {
	    			  $.each(element,function (newIndex,newElement) {
		    	     	    htmlContent = htmlContent + '<input type="hidden" value="'+newElement.siteId+'<SEP>'+newElement.accountType+'" name="childs"><br>';
			          });
	    			  $('#adUnitChildsDiv').html(htmlContent);
	    		  }
	    	  });
	    	  $('#selectDfpAccountNameLoaderDiv').css("visibility","hidden");
	    	  $("#submitButton").removeAttr("disabled");
	      },
	      error: function(jqXHR, error) {
	    	  $('#selectDfpAccountNameLoaderDiv').css("visibility","hidden");
	    	  $('#DFPPropertyName').val("");
	    	  $('#propertyName').val("");
	    	  $('#DFPPropertyId').val("");
	    	  console.log("Error in getAdUnitChilds() "+error);
	      }
	   });   
	}catch(exception){
		$('#selectDfpAccountNameLoaderDiv').css("visibility","hidden");
		$('#DFPPropertyName').val("");
  	  	$('#propertyName').val("");
  	  	$('#DFPPropertyId').val("");
		console.log("Exception in getAdUnitChilds() "+exception);
	}
}


