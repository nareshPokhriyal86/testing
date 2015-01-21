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
$(document).ready( function() {
	$(document).ready(function() {
		$('#teamLi').attr('class', 'active');
	});
	/* loadSelectedAccountsFromBigQuery(); */
});
</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Update Team</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	
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
            <h1 id="page-header">Update Team</h1>
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
		<!-- -----------------   Form starts   --------------------------- -->
		                           <s:form name="updateTeamForm" id="select-demo-js" action="updateTeam" cssClass="form-horizontal themed">
									<fieldset>
											
										<s:if test="%{#session.sessionDTO.superAdmin}">	
											<div class="control-group">
												<label class="control-label" for="multiSelect">Company</label>
												<div class="controls">
													<label><s:property value='companyName'/></label>
												</div>
											</div>
										</s:if>
											
										<div class="control-group">
											<label class="control-label"> Name<span class="req star" >*</span>
											</label>
											<div class="controls">
												<s:if test="teamType == definedTypeBuiltIn">
													<label><s:property value='teamName'/></label>
													<input type= "hidden" name="teamName" value= "<s:property value='teamName'/>">
												</s:if>
												<s:else>
													<div><input class="span4" onblur="isAvailable('teamName', '<s:property value='teamId'/>', '<s:property value='teamName'/>', 'teamNameAvailable');" required="required" value= "<s:property value='teamName'/>" title="Team name is required" maxlength="49" type="text" id="teamName" name="teamName"></div>
													<div id = "textValueAvailableImage" class="textValueAvailableImage"><img src="../img/tick.png" width="20"></div>
													<div id = "textValueAvailableText" class="textValueAvailableText">Team name not available. Try another?</div>
												</s:else>
											</div>
										</div>
										
										<div class="control-group">
			                              <label class="control-label">Status 
			                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
										  </label>
			                                    <div class="controls">
			                                    	<s:if test="teamType == definedTypeBuiltIn">
														<label><s:property value='teamStatus'/></label>
														<input type="hidden" name="teamStatus" value="<s:property value='teamStatus'/>">
													</s:if>
													<s:else>
					                                    <s:select cssClass="span4" id="statusId" name="teamStatus" value="%{selectedStatusList.{id}}"
					            								list="statusList" listKey="id" listValue="value">
					            						</s:select>
					            					</s:else>
			                                   	</div>
			                             </div>
										
										<div class="control-group">
											<label class="control-label" for="textarea">Description</label>
											<div class="controls">
												<textarea class="span4 uniform" id="teamDescription" name="teamDescription" rows="3"><s:property value='teamDescription'/></textarea>
											</div>
										</div>
										
										<div style="margin-left:20px; margin-top:10px; font-size:15px; font-weight:bold;">Choose Restriction</div>
										
										<div class="control-group">
											<label class="control-label" for="multiSelect">App Views</label>
											<div class="controls">
												<s:select cssClass="span4 with-search"	multiple="true" id="selectAppViews" value="%{selectedAppViewsList.{id}}"
				           							name="appViews" list="appViewsList" listKey="id" listValue="value" >
				           						</s:select>
				           						<div id="selectAppViewsLoaderDiv" class="displayNone"><img src="img/loaders/type3/light/24.gif" alt="loading.."></div>
											</div>
											<%-- 
											<div class="checker">
												<input type="checkbox" name="saveDefaultSettings" value="1">
											</div>
											<div id="defaultSettingsLabel">Save as default setting for <s:property value='defaultSelectedCompanyType'/> company type</div> --%>
										</div>
											
										<div class="control-group" id="propertiesDivId" <s:if test="%{!((defaultSelectedCompanyType == companyTypePublisherPartner) && (accessToProperties == 1))}">style="display: none;"</s:if> >
											<label class="control-label" for="multiSelect">Properties</label>
											<div class="controls">
												<s:select cssClass="span4 with-search"	multiple="true" id="selectProperties" value="%{selectedPropertiesList.{id}}"
				           							name="properties" list="propertiesList" listKey="id" listValue="value" >
				           						</s:select>
				           						<div class="controlsCheck">
												<input type="checkbox" name="allPropertiesFlag"
												 <s:if test="%{allPropertiesFlag == 1}" > checked="checked" </s:if> value="1">
													All Properties
												</div>
				           						<div id="selectPropertiesLoaderDiv" class="displayNone"><img src="img/loaders/type3/light/24.gif" alt="loading.."></div>
											</div>
										</div>
										
										<div class="control-group" id="accountsDivId" <s:if test="%{(accessToAccounts != 1)}">style="display: none;"</s:if> >	
											<label class="control-label" for="multiSelect">Accounts</label>
											<div class="controls">
												<label class="radio inline">
												  	<input type="radio" id="radioChooseAccounts" name="accountsFlag" onclick="javascript: $('#accountSelector').show();"  value= "1" 
												  		<s:if test="%{accountsFlag == 1}" > checked="checked" </s:if> />
												  	Selected Accounts
												</label>
												
												<label class="radio inline">
													<input type="radio" id="radioAllAccounts" name="accountsFlag"  onclick="javascript: $('#accountSelector').hide();" value= "2" 
														<s:if test="%{accountsFlag == 2}" > checked="checked" </s:if> />
													All Accounts
												</label>
												
												<s:if test="%{defaultSelectedCompanyType == companyTypePublisherPartner}">
													<label class="radio inline">
														<input type="radio" id="radioNoRestrictions" name="accountsFlag"  onclick="javascript: $('#accountSelector').hide();" value= "3" 
															<s:if test="%{accountsFlag == 3}" > checked="checked" </s:if> />
														No Restrictions
													</label>
												</s:if>
											</div>
											<div id="accountSelector" class="controls" <s:if test="%{(accountsFlag != 1)}" > style="display: none;" </s:if> >
												<%-- <input type='hidden'  data-init-text='' name='input' id='selectAdServerAccounts' style="width:300px;margin-left:-1px;"/>
												<select class="with-search" style="width:300px;margin-left:-1px;" multiple="multiple" id="selectedAdServerAccounts" name="accounts" >
				           						</select> --%>
				           						<s:select cssClass="span4 with-search"	multiple="true" id="selectAccounts" value="%{selectedAccountsList.{id}}"
				           							name="accounts" list="accountsList" listKey="id" listValue="value">
				           						</s:select>
				           						<div id="selectAdServerAccountsLoaderDiv" class="displayNone"><img src="img/loaders/type3/light/24.gif" alt="loading.."></div>
											</div>
										</div>
									 
										<div class="control-group">
											<!-- <div class="controls" class="shadow"> -->
												<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
												<button type="reset" onclick="fromUpdateToTeamSetup();" class="btn btn-danger btn-large floatRight">Cancel</button>
											<!-- </div> -->
										</div>
									</fieldset>
										
									<input type="hidden" id="teamId" name="teamId" value="<s:property value='teamId'/>" >
									<input type="hidden" id="selectedCompanyType" value="<s:property value='defaultSelectedCompanyType'/>" name="selectedCompanyType">
									
									<%-- <input type="hidden" id="agencyIdStringForBigQuery" value="<s:property value='agencyIdStringForBigQuery'/>" >
									<input type="hidden" id="advertiserIdStringForBigQuery" value="<s:property value='advertiserIdStringForBigQuery'/>" > --%>
									<input type="hidden" id="accessToAccounts" name="accessToAccounts" value="<s:property value='accessToAccounts'/>">
									<input type="hidden" id="accessToProperties" name="accessToProperties" value="<s:property value='accessToProperties'/>">
									<%-- <input type="hidden" id="publisherIdsForBigQuery" value="<s:property value='publisherIdsForBigQuery'/>"> --%>
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
