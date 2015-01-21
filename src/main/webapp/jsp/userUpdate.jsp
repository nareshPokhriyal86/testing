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

<title>ONE - User Update</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	
	<!-- Le javascript
    ================================================== --> 
<script src="../js/include/jquery.min.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/include/jquery.jgrowl_minimized.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/userManagement.js?v=<s:property value="deploymentVersion"/>"></script>
<script>
$(document).ready(function(){
	setTimezone('<s:property value='timezone'/>');
});
</script>

<!-- Le CSS
    ================================================== --> 
<jsp:include page="css.jsp"/>

</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->

	<!-- end header -->

	<div id="main" role="main" class="container-fluid">
		<%-- <jsp:include page="filter.jsp"/> --%>
		<div class="contained">
			<!-- aside -->


			<!-- main content -->
			<div id="page-content" style="margin-right:40px!important; margin-left:40px!important;">
			 <jsp:include page="navigationTab.jsp"/>
			 <jsp:include page="userManagementLeftMenu.jsp"/>
            <h1 id="page-header"> User Update</h1>
            <div class="row-fluid" style="border-top:1px solid #ccc;">
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
									        <!-- content goes here -->
		<!-- -----------------   Form starts   --------------------------- -->
						<s:form name = "updateUserForm" id="select-demo-js" cssClass="form-horizontal themed" action="userUpdate.lin" onsubmit="return validateUserForm('emailId', 'emailIdRepeat', 'userName', '%{superAdminRole}', '%{administratorRole}', '');">
							<fieldset>
							<div class="heading"></div>
							
							<s:if test="%{#session.sessionDTO.superAdmin}">	
								<div class="control-group">
									<label class="control-label">Company</label>
									<div class="controls">
										<s:select cssClass="span4 with-search" headerKey="-1" headerValue="Select a company" onchange="fetchTeamsByCompanyId('%{teamAllEntity}'); fetchActiveRolesByCompanyId('%{superAdminRole}','%{administratorRole}', '%{#session.sessionDTO.superAdmin}');" id="selectCompany"
		           							value="%{selectedCompanyList.{id}}" name="companyId" list="companyList" listKey="id" listValue="companyName">
		           						</s:select>
									</div>
								</div>
							</s:if>
							
							<div class="control-group">
								<label class="control-label"> Name<span class="req star"
									>*</span>
								</label>
								<div class="controls">
									<input class="span4" type="text" required="required" title="Name is required" id="userName" name="userName" value="<s:property value='userName'/>" >
								</div>
							</div>
							
							<div class="control-group">
                              <label class="control-label">Status 
                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
							  </label>
                                    <div class="controls">
                                    <s:select id="statusId" cssClass="span4" name="status" value="%{selectedStatusList.{id}}"
            								list="statusList" listKey="id" listValue="value">
            						</s:select>
                                    <br>
                                   	</div>
                             </div>
                             
							<div class="control-group">
								<label class="control-label">Email<span class="req star" 
									>*</span>
								</label>
								<div class="controls">
									<div><input type="email" class="span4"  maxlength="49"  onblur="confirmEmailId(this); isEmailIdAvailable('emailId','<s:property value='id'/>' ,'<s:property value='emailId'/>','emailIdAvailable');" required="required" title="Email is required" id="emailId" name="emailId" value="<s:property value='emailId'/>" onpaste="return false" oncopy="return false" ondrag="return false" ondrop="return false"></div>
									<!-- <div id = "emailAvailableImage" style="display:none; float:right; margin-right:73%; margin-top: 4px;"><img src="../img/tick.png" width="20"></div>
									<div id = "emailNotAvailableText" style="display:none; float:right; margin-right:68%; color:red;">Someone already has that Email Id. Try another?</div> -->
									<div id = "textValueAvailableImage" class="textValueAvailableImage"><img src="../img/tick.png" width="20"></div>
									<div id = "textValueAvailableText" class="textValueAvailableText">Someone already has that Email Id. Try another?</div>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Confirm Email<span class="req star" 
									>*</span>
								</label>
								<div class="controls">
									<input type="email" class="span4" maxlength="49" onblur="confirmEmailId(this);" required="required" title="Email is required" id="emailIdRepeat" name="emailIdRepeat" value="<s:property value='emailIdRepeat'/>" onpaste="return false" oncopy="return false" ondrag="return false" ondrop="return false">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Timezone</label>
								<div class="controls">
           						<select class="span4" name="timezone" id="timezone" >
           							<option value="24">Select Timezone</option>
           							<option value="-12.0">(GMT -12:00) Eniwetok, Kwajalein</option>
									<option value="-11.0">(GMT -11:00) Midway Island, Samoa</option>
									<option value="-10.0">(GMT -10:00) Hawaii</option>
									<option value="-9.0">(GMT -9:00) Alaska</option>
									<option value="-8.0">(GMT -8:00) Pacific Time (US &amp; Canada)</option>
									<option value="-7.0">(GMT -7:00) Mountain Time (US &amp; Canada)</option>
									<option value="-6.0">(GMT -6:00) Central Time (US &amp; Canada), Mexico City</option>
									<option value="-5.0">(GMT -5:00) Eastern Time (US &amp; Canada), Bogota, Lima</option>
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
								<label class="control-label">Role <span
									class="help-Helpwidgets-TooltipWidget">&nbsp;<i
										title="Default Tooltip" class="cus-help"></i>
								</span> </label>
								<div class="controls" class="shadow">
									<s:select cssClass="span4" id="rolesId" name="role" onchange="setTeamsByRole('%{superAdminRole}','%{administratorRole}');"
            								list="roleList" value="%{selectedRoleList.{id}}" listKey="id" listValue="value">
            						</s:select>
									<br>
									<span style="font-size: 11px;">Administrators have full access.</span>
								</div>
							</div>
                             
                           
                            <div class="control-group" id="teamDiv"  <s:if test="%{selectedRoleType == superAdminRole}">style="display: none;"</s:if> >
								<label class="control-label" for="multiSelect">Team<span class="req star" >*</span></label>
								<div class="controls">
									<div id="selectAdminTeamDiv" <s:if test="%{selectedRoleType != administratorRole}">style="display: none;"</s:if> >
		           						<s:select id="selectAdminTeam" cssClass="span4 with-search"	value="%{selectedAdminTeamList.{id}}"
		           							name="adminTeam" list="adminTeamList" listKey="id" listValue="value">
		           						</s:select>
		           					</div>
	           						<div id="selectNonAdminTeamDiv" <s:if test="%{(selectedRoleType == superAdminRole || selectedRoleType == administratorRole)}">style="display: none;"</s:if> >
		           						<s:select id="selectNonAdminTeam" multiple="true" cssClass="span4 with-search" value="%{selectedNonAdminTeamList.{id}}"
		           							name="nonAdminTeam" list="nonAdminTeamList" listKey="id" listValue="value" onchange="selectGivenTeamFromDropDown('selectNonAdminTeam', '%{teamAllEntity}', '%{teamNoEntity}');">
		           						</s:select>
		           					</div>
								</div>
							 </div>
							 
							<div class="control-group" id="optEmail">
						  		 <label class="control-label" style="width: 150px;" >Subscribe Performance Summary Report</label>
								<div class="controls">
								<input type="checkbox" id="optEmailId"	name="optEmail"
											value="true" <s:if test="%{optEmail == true}" > checked="checked" </s:if>>
								</div>
							</div>

							<div class="control-group">
								<!-- <div class="controls" class="shadow"> -->
									<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
									<button type="reset" onclick="fromUpdateToUserSetup();" class="btn btn-danger btn-large floatRight">Cancel</button>
								<!-- </div> -->
							</div>

						 </fieldset>
						 	<input type="hidden" id="hiddenVal" name="id" value="<s:property value='id'/>" >
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
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
