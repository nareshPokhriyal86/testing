<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="theme" value="'simple'" scope="page" />
<jsp:include page="Header.jsp" />

<html lang="en">
<head>
		
<script>
localStorage.clear();
</script>	
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - My Settings</title>
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
		
		<div class="contained">
			<!-- aside -->
			
			<!-- aside end -->

			<!-- main content -->
			<div id="page-content" class="mlr">
			<jsp:include page="navigationTab.jsp"/>
            <!-- <h1 id="page-header">My Settings</h1> -->
            <div class="row-fluid Profile" >
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
						<s:form name = "userProfileForm" id="select-demo-js" cssClass="form-horizontal themed" action="userOwnProfileUpdate.lin" onsubmit="return validateUserForm('emailId', 'emailIdRepeat', 'userName', '%{superAdminRole}', '%{administratorRole}', 'mySettings');">
							<fieldset>
							
							<div class="heading"></div>
							<div class="control-group">
								<label class="control-label"> Name<span class="req star"
									>*</span>
								</label>
								<div>
									<input class="prfile_name" type="text" required="required" title="Name is required" id="userName" name="userName" value="<s:property value='userName'/>"
										>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Email<span class="req star" >*</span>
								</label>
								<div class="controls">
									<input type="email"  maxlength="49"  onblur="confirmEmailId(this); isEmailIdAvailable('emailId','<s:property value='emailId'/>');" required="required" title="Email is required" id="emailId" name="emailId" value="<s:property value='emailId'/>">
									<div id = "emailAvailableImage" style="display:none; float:right; margin-right:73%; margin-top: 4px;"><img src="../img/tick.png" width="20"></div>
									<div id = "emailNotAvailableText" style="display:none; float:right; margin-right:68%; color:red;">Someone already has that Email Id. Try another?</div>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Confirm Email<span
									class="req star">*</span>
								</label>
								<div class="controls">
									<input type="email" maxlength="49" onblur="confirmEmailId(this);" required="required" title="Email is required" id="emailIdRepeat" name="emailIdRepeat" value="<s:property value='emailIdRepeat'/>">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">password<span class="req star" >*</span>
								</label>
								<div class="controls">
									<input type="password" maxlength="49" onblur="confirmPassword(this);" required="required" title="Password is required" id="password" name="password" value="<s:property value='password'/>">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Confirm Password<span
									class="req star">*</span>
								</label>
								<div class="controls">
									<input type="password" maxlength="49" onblur="confirmPassword(this);" required="required" title="Password is required" id="passwordRepeat" name="passwordRepeat" value="<s:property value='passwordRepeat'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Timezone</label>
								<div class="controls">
           						<select class="span4" name="timezone" id="timezone" >
           							<option value="none">Select Timezone</option>
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
								<div class="controls" class="shadow">
									<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
									<button type="reset" onclick="javascript:setTimezone('<s:property value='timezone'/>');" class="btn btn-danger btn-large">Cancel</button>
								</div>
							</div>

						 </fieldset>
						 	<input type="hidden" id="hiddenVal" name="id" value="<s:property value='session.sessionDTO.userId'/>" >
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
								<script type="text/javascript"> errorMessage('Profile view failed'); </script>
							</s:if>
							<s:elseif test="%{updateUserStatus == 0}">
								<script type="text/javascript"> errorMessage('User update failed'); </script>
							</s:elseif>
							<s:elseif test="%{updateUserStatus == 1}">
								<script type="text/javascript"> successMessage('Updated successfully'); </script>
							</s:elseif>
							<s:elseif test="%{linStatus == 'inappropriate'}">
								<script type="text/javascript"> errorMessage('Inappropriate information'); </script>
							</s:elseif>
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
