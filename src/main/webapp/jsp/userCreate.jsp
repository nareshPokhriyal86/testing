<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="theme" value="'simple'" scope="page" />
<jsp:include page="Header.jsp" />

<html lang="en">
<head>
		
<script>
$(document).ready(function() {
	$('#userLi').attr('class', 'active');
});	
		localStorage.clear();
		
</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Create User</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	
	<!-- Le CSS
    ================================================== --> 
<jsp:include page="css.jsp"/>
	
	<!-- Le javascript
    ================================================== -->   
<script src="../js/include/jquery.min.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/include/jquery.jgrowl_minimized.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/userManagement.js?v=<s:property value="deploymentVersion"/>"></script>

</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->
	
	<!-- end header -->

	<div id="main" role="main" class="container-fluid">

		<div class="contained">
			<!-- aside -->
			<!-- aside end -->

			<!-- main content -->
			<div id="page-content" class="mlr">
			 <jsp:include page="navigationTab.jsp"/>
			 <jsp:include page="userManagementLeftMenu.jsp"/>
			<%-- <jsp:include page="filter.jsp"/> --%>
            <h1 id="page-header">Create New User</h1>
            <div class="row-fluid Profile">
								<!-- article -->	
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget" id="widget-id-1">
									    
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
									            </div>
									            <div>
									                <label>Styles:</label>
									                <span data-widget-setstyle="purple" class="purple-btn"></span>
									                <span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
									                <span data-widget-setstyle="green" class="green-btn"></span>
									                <span data-widget-setstyle="yellow" class="yellow-btn"></span>
									                <span data-widget-setstyle="orange" class="orange-btn"></span>
									                <span data-widget-setstyle="pink" class="pink-btn"></span>
									                <span data-widget-setstyle="red" class="red-btn"></span>
									                <span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
									                <span data-widget-setstyle="black" class="black-btn"></span>
									            </div>
									        </div>
            
									        <div class="inner-spacer"> 
									      <%--   <div class="row-fluid dt-header"><div class="span6"><div class="dataTables_filter" id="userSetupTable_filter"><label><input type="text" aria-controls="userSetupTable" placeholder="Search Filter"></label></div></div><div class="span6 hidden-phone"><div class="DTTT_container"><a class="DTTT_button DTTT_button_print" id="ToolTables_userSetupTable_0" title="View print view"><span><i class="cus-printer oTable-adjust"></i> Print View</span></a><a class="DTTT_button DTTT_button_pdf" id="ToolTables_userSetupTable_1"><span><i class="cus-doc-pdf oTable-adjust"></i> Save to PDF</span><div style="position: absolute; left: 0px; top: 0px; width: 97px; height: 26px; z-index: 99;"><embed id="ZeroClipboard_TableToolsMovie_1" src="js/include/assets/DT/swf/copy_csv_xls_pdf.swf" loop="false" menu="false" quality="best" bgcolor="#ffffff" width="97" height="26" name="ZeroClipboard_TableToolsMovie_1" align="middle" allowscriptaccess="always" allowfullscreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" flashvars="id=1&amp;width=97&amp;height=26" wmode="transparent"></div></a><a class="DTTT_button DTTT_button_xls" id="ToolTables_userSetupTable_2"><span><i class="cus-doc-excel-table oTable-adjust"></i> Save for Excel</span><div style="position: absolute; left: 0px; top: 0px; width: 109px; height: 26px; z-index: 99;"><embed id="ZeroClipboard_TableToolsMovie_2" src="js/include/assets/DT/swf/copy_csv_xls_pdf.swf" loop="false" menu="false" quality="best" bgcolor="#ffffff" width="109" height="26" name="ZeroClipboard_TableToolsMovie_2" align="middle" allowscriptaccess="always" allowfullscreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" flashvars="id=2&amp;width=109&amp;height=26" wmode="transparent"></div></a></div></div></div> --%>
									        <!-- content goes here -->
		<!-- -----------------   Form starts   --------------------------- -->
						<s:form name = "createNewUserForm" id="select-demo-js" cssClass="form-horizontal themed" action="createNewUser.lin" onsubmit="return validateUserForm('emailId', 'emailIdRepeat', 'userName', '%{superAdminRole}', '%{administratorRole}', '');">
							<fieldset>
							
							<s:if test="%{#session.sessionDTO.superAdmin}">	
								<div class="control-group">
									<label class="control-label">Company</label>
									<div class="controls">
										<s:select cssClass="span4 with-search" onchange="fetchTeamsByCompanyId('%{teamAllEntity}'); fetchActiveRolesByCompanyId('%{superAdminRole}','%{administratorRole}', '%{#session.sessionDTO.superAdmin}');" id="selectCompany"
		           							name="companyId" list="companyList" listKey="id" listValue="companyName">
		           						</s:select>
									</div>
								</div>
							</s:if>
							
							<div class="control-group">
								<label class="control-label"> Name<span class="req star"
									>*</span>
								</label>
								<div class="controls">
									<input class="span4" required="required" title="Name is required" maxlength="49" type="text" id="userName" name="userName">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Email<span class="req star" 
									>*</span>
								</label>
								<div class="controls">
									<div><input type="email" class="span4" maxlength="49"  onblur="confirmEmailId(this); isEmailIdAvailable('emailId','' ,'', 'emailIdAvailable');" required="required" title="Email is required" id="emailId" name="emailId" oncopy="return false" ondrag="return false" ondrop="return false"></div>
									<!-- <div id = "emailAvailableImage" style="display:none; float:right; margin-right:73%; margin-top: 4px;"><img src="../img/tick.png" width="20"></div> -->
									<div id = "textValueAvailableImage" class="textValueAvailableImage"><img src="../img/tick.png" width="20"></div>
									<div id = "textValueAvailableText" class="textValueAvailableText">Someone already has that Email Id. Try another?</div>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Confirm Email<span
									class="req star">*</span>
								</label>
								<div class="controls">
									<input type="email" class="span4"  maxlength="49"  onblur="confirmEmailId(this);" required="required" title="Email is required" id="emailIdRepeat" name="emailIdRepeat" onpaste="return false" oncopy="return false" ondrag="return false" ondrop="return false">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Timezone</label>
								<div class="controls">
           						<select class="span4" name="timezone" id="timezone" >
           							<option value="-12.0">(GMT -12:00) Eniwetok, Kwajalein</option>
									<option value="-11.0">(GMT -11:00) Midway Island, Samoa</option>
									<option value="-10.0">(GMT -10:00) Hawaii</option>
									<option value="-9.0">(GMT -9:00) Alaska</option>
									<option value="-8.0">(GMT -8:00) Pacific Time (US &amp; Canada)</option>
									<option value="-7.0">(GMT -7:00) Mountain Time (US &amp; Canada)</option>
									<option value="-6.0">(GMT -6:00) Central Time (US &amp; Canada), Mexico City</option>
									<option value="-5.0" selected="selected">(GMT -5:00) Eastern Time (US &amp; Canada), Bogota, Lima</option>
									<option value="-4.0">(GMT -4:00) Atlantic Time (Canada), Caracas, La Paz</option>
									<option value="-3.5">(GMT -3:30) Newfoundland</option>
									<option value="-3.0">(GMT -3:00) Brazil, Buenos Aires, Georgetown</option>
									<option value="-2.0">(GMT -2:00) Mid-Atlantic</option>
									<option value="-1.0">(GMT -1:00 hour) Azores, Cape Verde Islands</option>
									<option value="0.0">(GMT) Western Europe Time, London, Lisbon, Casablanca</option>
									<option value="1.0">(GMT +1:00 hour) Brussels, Copenhagen, Madrid, Paris</option>
									<option value="2.0">(GMT +2:00) Kaliningrad, South Africa</option>
									<option value="3.0">(GMT +3:00) Baghdad, Riyadh, Moscow, St. Petersburg</option>
									<option value="3.5">(GMT +3:30) Tehran</option>
									<option value="4.0">(GMT +4:00) Abu Dhabi, Muscat, Baku, Tbilisi</option>
									<option value="4.5">(GMT +4:30) Kabul</option>
									<option value="5.0">(GMT +5:00) Ekaterinburg, Islamabad, Karachi, Tashkent</option>
									<option value="5.5">(GMT +5:30) Bombay, Calcutta, Madras, New Delhi</option>
									<option value="5.75">(GMT +5:45) Kathmandu</option>
									<option value="6.0">(GMT +6:00) Almaty, Dhaka, Colombo</option>
									<option value="7.0">(GMT +7:00) Bangkok, Hanoi, Jakarta</option>
									<option value="8.0">(GMT +8:00) Beijing, Perth, Singapore, Hong Kong</option>
									<option value="9.0">(GMT +9:00) Tokyo, Seoul, Osaka, Sapporo, Yakutsk</option>
									<option value="9.5">(GMT +9:30) Adelaide, Darwin</option>
									<option value="10.0">(GMT +10:00) Eastern Australia, Guam, Vladivostok</option>
									<option value="11.0">(GMT +11:00) Magadan, Solomon Islands, New Caledonia</option>
									<option value="12.0">(GMT +12:00) Auckland, Wellington, Fiji, Kamchatka</option>
           						</select>
								</div>
							  </div>					
							
							
							 <div class="control-group">
                              <label class="control-label">Status 
                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
							  </label>
                                    <div class="controls">
                                    <s:select cssClass="span4" id="statusId" name="status"
            								list="statusList" listKey="id" listValue="value">
            						</s:select>
                                    <br>
                                   	</div>
                             </div>
                             
                            <div class="control-group">
								<label class="control-label">Role <span
									class="help-Helpwidgets-TooltipWidget">&nbsp;<i
										title="Default Tooltip" class="cus-help"></i>
								</span> </label>
								<div class="controls" class="shadow">
									<s:select cssClass="span4" id="rolesId" name="role" onchange="setTeamsByRole('%{superAdminRole}','%{administratorRole}');"
            								list="roleList" listKey="id" listValue="value">
            						</s:select>
									<br>
									<span style="font-size: 11px;">Administrators have full access.</span>
								</div>
							</div>
                             
                           
                            <div class="control-group" id="teamDiv"  <s:if test="%{selectedRoleType == superAdminRole}">style="display: none;"</s:if> >
								<label class="control-label" for="multiSelect">Team<span class="req star" >*</span></label>
								<div class="controls">
									<div id="selectAdminTeamDiv" <s:if test="%{selectedRoleType != administratorRole}">style="display: none;"</s:if> >
		           						<s:select id="selectAdminTeam" cssClass="span4 with-search"	
		           							name="adminTeam" list="adminTeamList" listKey="id" listValue="value">
		           						</s:select>
		           					</div>
	           						<div id="selectNonAdminTeamDiv" <s:if test="%{(selectedRoleType == superAdminRole || selectedRoleType == administratorRole)}">style="display: none;"</s:if> >
		           						<s:select id="selectNonAdminTeam" multiple="true" cssClass="span4 with-search" onchange="selectGivenTeamFromDropDown('selectNonAdminTeam', '%{teamAllEntity}', '%{teamNoEntity}');"
		           							name="nonAdminTeam" list="nonAdminTeamList" listKey="id" listValue="value">
		           						</s:select>
		           					</div>
								</div>
							 </div>
							 
							   <%-- <s:if test="%{selectedRoleType == administratorRole}"> --%>
							   		<div class="control-group" id="optEmail">
							  		 <label class="control-label" style="width: 150px;" >Subscribe Performance Summary Report</label>
									<div class="controls">
									<input type="checkbox" id="optEmailId"	name="optEmail"
												value="true" >
									</div>
							  		 </div>
							   <%-- </s:if>  --%>
								
							
							
						


							<div class="control-group">
								<!-- <div class="controls" class="shadow"> -->
									<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
									<button type="reset" onclick="fromCreateToUserSetup();" class="btn btn-danger btn-large floatRight">Cancel</button>
								<!-- </div> -->
							</div>

						 </fieldset>
						 <input type="hidden" name="selectedRoleType" id="selectedRoleType" value='<s:property value='selectedRoleType'/>'/>
						</s:form>

		<!-- -----------------   Form Ends   --------------------------- -->
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
								<!-- end article-->
			</div>
            <!-- end content-->
			</div>
									    <!-- end wrap div -->
		</div>
									<!-- end widget -->
	</div>                                                                         

			<!-- end main content -->



			<!-- aside right on high res -->
			
	<!--end fluid-container-->
	<div class="push"></div>
</div>
<!-- end .height wrapper -->

<!-- footer -->

<!-- if you like you can insert your footer here -->

<!-- end footer -->

<!--================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

		<!-- Le javascript
    ================================================== --> 
<jsp:include page="js.jsp"/>




<!-- end scripts -->
							<s:if test="%{linStatus == 'failed'}">
								<script type="text/javascript"> errorMessage('Create user failed'); </script>
							</s:if>
							<s:elseif test="%{linStatus == 'inappropriate'}">
								<script type="text/javascript"> errorMessage('Wrong data'); </script>
							</s:elseif>
							<s:elseif test="%{linStatus == 'exists'}">
								<script type="text/javascript"> errorMessage('Email Id belongs to a user'); </script>
							</s:elseif>
							<s:elseif test="%{linStatus == 'success'}">
								<script type="text/javascript"> successMessage('User created successfully'); </script>
							</s:elseif>
							<s:elseif test="%{linStatus == 'mailSentFailed'}">
								<script type="text/javascript"> successMessage('User created successfully'); </script>
								<script type="text/javascript"> errorMessage('Account ativation mail sent failed'); </script>
							</s:elseif>
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
