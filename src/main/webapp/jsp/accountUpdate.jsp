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
	$('#accountLi').attr('class', 'active');
});
localStorage.clear();
</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Update Accounts</title>
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
            <h1 id="page-header">Update Accounts</h1>
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
		                            <s:form name="updateAccountForm" id="select-demo-js" action="updateAccount" onsubmit="return validateAccounts();" cssClass="form-horizontal themed">
										<fieldset>
											<%-- <s:if test="%{#session.sessionDTO.superAdmin}">
													
													<div class="control-group">
														<label class="control-label">Company<span class="req star" >*</span></label>
														<div class="controls">
															<s:if test="companyList !=null && companyList.size()>0">
																<s:select cssClass="span4 with-search" onchange="getAdserverCredentialsByCompany('companyId');"
								           							 id="companyId" name="companyId" list="companyList" listKey="id" listValue="companyName" headerKey="-1" headerValue="Select Company">
								           						</s:select>
								           					</s:if>
								           					<s:else>
								           						<select class="span4 with-search" id="companyId" name="companyId">
								           							<option value="-1">Select Company</option>
								           						</select>
								           					</s:else>
														</div>
													</div>
											</s:if>
											<s:else>
												<input type="hidden" id="companyId" name="companyId" value="<s:property value='companyId'/>">
											</s:else> --%>
											
											<div class="control-group">
				                                <label class="control-label">Status</label>
			                                    <div class="controls">
			                                    <s:select id="statusId" cssClass="span4 with-search" name="status"
			            								list="statusList" listKey="id" listValue="value">
			            						</s:select>
			                                    <br>
			                                   	</div>
				                             </div>
				                             
				                            <div class="control-group">
												<label class="control-label" for="multiSelect">Company<span class="req star" >*</span></label>
												<div class="controls">
													<label><s:property value='companyName'/></label>
												</div>
											</div>
				                             
				                             <div class="control-group">
												<label class="control-label">DFP Credentials<span class="req star">*</span></label>
												<div class="controls">
													 <div style="display:none" id="noDfpCredentialsMessage" >No DFP credentials assigned for the company</div>
													 <div id="selectDfpCredentialsDiv" style="display:none"> 
													     <select class="span4 with-search" id="selectDfpCredentials" name="dfpCredentials" onchange="setNetWorkInfo();">
						           						 </select>
						           					 </div>
					           						 <div id="showDfpCredentials">
					           						 	<label>Ad server id : <s:property value='networkId'/><br> Ad server username : <s:property value='networkUsername'/></label>
					           						 </div>
												</div>
											</div>
											
											<div class="control-group">	
												<label class="control-label" for="multiSelect">DFP Account Name<span class="req star" >*</span></label>
												<div class="controls">
													<!-- <input type='hidden'  data-init-text='' name='input' id='selectDfpAccountName' style="width:31.6%;margin-left:-1px;"/>
													<br> -->
													<input type="text" required="required" readonly="readonly" class="span4" id="dfpAccountName" name="dfpAccountName" value="<s:property value='dfpAccountName'/>">
					           						<!-- <div id="selectDfpAccountNameLoaderDiv" class="displayNone"><img src="img/loaders/type3/light/24.gif" alt="loading.."></div> -->
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Account<span class="req star" >*</span></label>
												<div class="controls">
												<div><input class="span4" onblur="isAccountOrPropertyNameAvailable('accountName','<s:property value='accountId'/>' ,'', 'accountNameAvailable');" required="required" title="Account is required" type="text" value="<s:property value='accountName'/>" id="accountName" name="accountName"></div>
												<div id = "textValueAvailableImage" class="textValueAvailableImage"><img src="../img/tick.png" width="20"></div>
												<div id = "textValueAvailableText" class="textValueAvailableText">Account not available. Try another?</div>
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label" for="multiSelect">Type</label>
												<div class="controls">
													<label id="showAccountType"><s:property value='accountType'/></label>
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Industry</label>
												<div class="controls">
												  <s:if test="industryList !=null && industryList.size()>0">
												     <s:select cssClass="span4 with-search" id="industry" name="industry" list="industryList"
												     		listKey="industryId" listValue="industryName" headerKey="-1" headerValue="Select Industry">
					           						 </s:select>
											      </s:if>
											      <s:else>
					           						  <select class="span4 with-search" id="industry" name="industry">
					           							  <option value="-1">Select Industry</option>
					           						  </select>
					           					  </s:else>
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Web URL</label>
												<div class="controls">
													<input class="span4"  maxlength="49" type="url" value="<s:property value='webURL'/>"  name="webURL">
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Contact</label>
												<div class="controls">
													<input class="span4"  maxlength="49" type="text" value="<s:property value='contact'/>" name="contact">
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Phone</label>
												<div class="controls">
													<input class="span4"  maxlength="49" type="text" value="<s:property value='phone'/>"  name="phone">
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Email</label>
												<div class="controls">
													<input class="span4"  maxlength="49" type="email" value="<s:property value='emailId'/>"  name="emailId">
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Fax</label>
												<div class="controls">
													<input class="span4"  maxlength="49" type="text" value="<s:property value='fax'/>"  name="fax">
												</div>
											</div>
									 	
											<div class="control-group">
												<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
												<button type="reset" onclick="fromUpdateToAccountSetup();" class="btn btn-danger btn-large floatRight">Cancel</button>
											</div>
										
											<input type="hidden" id="accountId" value="<s:property value='accountId'/>" name="accountId">
											<input type="hidden" id="selectedCompanyType" value="<s:property value='defaultSelectedCompanyType'/>" name="selectedCompanyType">
											<input type="hidden" id="accountDfpId" value="<s:property value='accountDfpId'/>" name="accountDfpId">
											<input type="hidden" id="accountType" value="<s:property value='accountType'/>" name="accountType">
											<input type="hidden" id="networkId" name="networkId" value="<s:property value='networkId'/>">
											<input type="hidden" id="networkUsername" name="networkUsername" value="<s:property value='networkUsername'/>">
											<s:iterator value="companyList" status="stat">
												<input type="hidden" id="companyID<s:property value='id'/>" value="<s:property value='companyType'/>">
											</s:iterator>
											<input type="hidden" id="companyId" name="companyId" value="<s:property value='companyId'/>">
										
										</fieldset>
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

		<s:if test="%{linStatus == 'success'}">
			<script type="text/javascript"> successMessage('Saved Successfully'); </script>
		</s:if>
		<s:elseif test="%{linStatus == 'failed'}">
			<script type="text/javascript"> errorMessage('Failed, please try again'); </script>
		</s:elseif>

<!-- end scripts -->
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
