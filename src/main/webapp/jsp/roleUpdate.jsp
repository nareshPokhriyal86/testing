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
	$('#roleLi').attr('class', 'active');
});
		localStorage.clear();	
</script>	
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Role update</title>
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
            <h1 id="page-header">Update Role</h1>
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
									        <!-- content goes here -->
		<!-- -----------------   Form starts   --------------------------- -->
				<form name="updateRoleForm" action="updateRole.lin" class="form-horizontal themed">
						<fieldset>
						<div class="control-group">
							<label class="control-label" for="input01">Name<span class="req star">*</span></label>
							<div class="controls">
								<div><input class="span4" onblur="isAvailable('roleName','<s:property value='roleId'/>' ,'<s:property value='roleName'/>', 'roleNameAvailable');" required="required" value= "<s:property value='roleName'/>" title="Role name is required" maxlength="49" type="text" id="roleName" name="roleName" <s:if test="%{roleType == definedTypeBuiltIn}" > disabled="disabled" </s:if> ></div>
								<div id = "textValueAvailableImage" class="textValueAvailableImage"><img src="../img/tick.png" width="20"></div>
								<div id = "textValueAvailableText" class="textValueAvailableText">Role name not available. Try another?</div>
							</div>
						</div>
						
						<s:if test="%{roleType != definedTypeBuiltIn}">
							<div class="control-group">
                              <label class="control-label">Status 
                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
							  </label>
                                    <div class="controls">
                                    <s:select cssClass="span4" id="statusId" name="roleStatus" value="%{selectedStatusList.{id}}"
            								list="statusList" listKey="id" listValue="value">
            						</s:select>
                                    <br>
                                   	</div>
                             </div>
						</s:if>
						
						<div class="control-group">
							<label class="control-label" >Copy permissions from role</label>
							<div class="controls">
							  <s:if test="%{roleType == definedTypeBuiltIn}" >
								<s:select cssClass="span4" disabled="true" id="rolesId" name="role" value="%{selectedRoleList.{id}}"
           								list="roleList" listKey="id" listValue="value" onchange="copyRoleUpdate();">
           						</s:select>
           					  </s:if>
           					  <s:else>
           					  	<s:select cssClass="span4" id="rolesId" name="role" value="%{selectedRoleList.{id}}"
           								list="roleList" listKey="id" listValue="value" onchange="copyRoleUpdate();">
           						</s:select>
           					  </s:else>
							</div>
						</div>					
						
						<div class="control-group" style="border-top:none;">
							<label class="control-label">Role description</label>
							<div class="controls">
								<textarea class="span4 uniform" <s:if test="%{roleType == definedTypeBuiltIn}" > disabled="disabled" </s:if> id="roleDescription" name="roleDescription" rows="3"><s:property value='roleDescription'/></textarea>
							</div>
						</div>
						
						<s:if test="%{#session.sessionDTO.superAdmin && roleType != definedTypeBuiltIn}">
							<div class="control-group">
								<label class="control-label" >Role for Company</label>
								<div class="controls">
									<s:select cssClass="span4" id="selectCompany" name="companyId" value="%{selectedCompanyList.{id}}"
		          							list="companyList" listKey="id" listValue="companyName">
		          					</s:select>
								</div>
							</div>
						</s:if>
						
						<div style="font-weight:bold; font-size:13px; color:#000; margin:20px 20px 0px 20px;"><h3>Permissions :</h3></div>
						
						<div class="control-group">
							<s:set var="authorisationPageName" value="" scope="page"/>
							<s:iterator value="authorisationTextList" status="stat">
								<s:if test="%{#stat.index == 0}">
									<s:set var="authorisationPageName" value="authorisationForPage" />
									<h4><s:property value='authorisationPageName'/></h4>
								</s:if>
								<s:elseif test="%{#authorisationPageName != authorisationForPage}">
									<s:set var="authorisationPageName" value="authorisationForPage" />
									<br><h4><s:property value='authorisationPageName'/></h4>
								</s:elseif>			
							<label class="checkbox">
								<div class="checker">
									<span><input type="checkbox" name="<s:property value='rolesAndAuthorisationColumnName'/>"
											<s:if test="%{permission == 1}" > checked="checked" </s:if>
											<s:if test="%{roleType == definedTypeBuiltIn}" > disabled="disabled" </s:if> value="1">
									</span>
								</div>
								<s:property value='authorisationText'/>
							</label>
							</s:iterator>		
						</div>
						
						<div class="control-group">
							<s:if test="%{(roleType == 'Custom') || (roleType == 'custom')}">
								<button type="Submit" id="submitButton" class="btn btn-success btn-large">Save</button>
							</s:if>
							<button type="button" onclick="fromUpdateToRoleSetup();" class="btn btn-danger btn-large floatRight">Cancel</button>
						</div>
														
					</fieldset>
					<input type="hidden" id="hiddenVal" name="roleId" value="<s:property value='roleId'/>" >
				</form>

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
