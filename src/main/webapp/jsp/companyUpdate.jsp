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
$(document).ready(function() {
	$('#companyLi').attr('class', 'active');
	if($('#radioAdserverOther').attr("checked") == "checked") {
   		removeCssClassOnDFPCredentials();
   	}
});
var adServerCredentialsCounter = '<s:property value="adServerCredentialsCounterValue"/>';
var passbackSiteTypeCounter = '<s:property value="passbackSiteTypeCounterValue"/>';
var serviceURLCounter = '<s:property value="serviceURLCounterValue"/>';
</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Update Company</title>
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
<script src="../js/common.js?v=<s:property value="deploymentVersion"/>"></script>
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
            <h1 id="page-header">Update Company</h1>
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
						<s:form name = "updateCompanyForm" enctype="multipart/form-data" id="select-demo-js" cssClass="form-horizontal themed" action="updateCompany.lin">
							<fieldset>
							
							<div class="control-group">
								<label class="control-label">Company Logo</label>
								<div class="controls">
									<s:file name ="companyLogo"/>
								</div>
								<div class="controls"><img src="<s:property value='companyLogoURL'/>" height="32" width="100" align="left"></div>
								<div class="passBackInput"><s:property value='companyLogoFileName'/></div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Company Name<span class="req star">*</span>
								</label>
								<div class="controls">
									<div><input class="span4" onblur="isAvailable('company', '<s:property value='companyId'/>', '<s:property value='companyName'/>', 'companyNameAvailable');" required="required" title="Company Name is required" maxlength="49" type="text" id="company" name="companyName" value="<s:property value='companyName'/>"></div>
									<div id = "textValueAvailableImage" class="textValueAvailableImage"><img src="../img/tick.png" width="20"></div>
									<div id = "textValueAvailableText" class="textValueAvailableText">Company name not available. Try another?</div>
								</div>
							</div>
							
							<div class="control-group">
                              <label class="control-label">Status 
                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
							  </label>
                                    <div class="controls">
                                    <s:select cssClass="span4" id="statusId" name="status" value="%{selectedStatusList.{id}}"
            								list="statusList" listKey="id" listValue="value">
            						</s:select>
                                    <br>
                                   	</div>
                             </div>
							
							 <div class="control-group">
	                            <label class="control-label">Type 
	                             	<span  class="help-Helpwidgets-TooltipWidget"></span>
							    </label>
                                <div class="controls">
                                	<label class="radio inline">
									  <input type="radio" id="radioCompanyTypePublisherPartner" name="companyTypeToUpdate" onclick="javascript: setPageToCreatePublisher();"  value= "<s:property value='companyTypePublisherPartner'/>" 
	          							<s:if test="%{companyTypeToUpdate == companyTypePublisherPartner}" > checked="checked" </s:if> />
									  <s:property value='companyTypePublisherPartner'/>
									</label>
									
									<label class="radio inline">
									  <input type="radio" id="radioCompanyTypeDemandPartner" name="companyTypeToUpdate"  onclick="javascript: setPageToCreateDemandPartner();" value= "<s:property value='companyTypeDemandPartner'/>" 
	          							<s:if test="%{companyTypeToUpdate == companyTypeDemandPartner}" > checked="checked" </s:if> />
									  <s:property value='companyTypeDemandPartner'/>
									</label>
									
									<label class="radio inline">
									  <input type="radio" id="radioCompanyTypeClient" name="companyTypeToUpdate"  onclick="javascript: setPageToCreateClient();" value= "<s:property value='companyTypeClient'/>" 
	          							<s:if test="%{companyTypeToUpdate== companyTypeClient}" > checked="checked" </s:if> />
									  <s:property value='companyTypeClient'/>
									</label>
                               	</div> 
                            </div>
                            
                            <div class="control-group">
								<label class="control-label">Address</label>
								<div class="controls">
									<textarea class="span4 uniform" name="companyAddress" rows="3"><s:property value='companyAddress'/></textarea>
								</div>
							</div>
                            
                            <div class="control-group">
								<label class="control-label">Phone</label>
								<div class="controls">
									<input class="span4"  maxlength="49" type="text"  name="phone" value="<s:property value='phone'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Fax</label>
								<div class="controls">
									<input class="span4"  maxlength="49" type="text"  name="fax" value="<s:property value='fax'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Email</label>
								<div class="controls">
									<input class="span4"  maxlength="49" type="email"  name="companyEmail" value="<s:property value='companyEmail'/>">
								</div>
							</div>
                             
                             <div class="control-group">
								<label class="control-label">Web URL</label>
								<div class="controls">
									<input class="span4"  maxlength="49" type="url"  name="webURL" value="<s:property value='webURL'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Contact Person Name</label>
								<div class="controls">
									<input class="span4"  maxlength="49" type="text"  name="contactPersonName" value="<s:property value='contactPersonName'/>">
								</div>
							</div>
							
							<div id="publisherPartnerInfoDiv" <s:if test="%{companyTypeToUpdate != companyTypePublisherPartner}" > style="display: none;" </s:if>>
								<div class="control-group">
	                              	<label class="control-label">Demand Partners</label>
	                                 <div class="controls">
		                                 <s:select multiple="true" cssClass="span4 with-search" id="demandPartnerId" name="demandPartnerId"
		         								list="demandPartnersList" listKey="id" listValue="value" value="%{selectedDemandPartnersList.{id}}">
		         						 </s:select>
	                                 </div> 
                             	</div>
							</div>
							
							<div id="demandPartnerInfoDiv" <s:if test="%{companyTypeToUpdate != companyTypeDemandPartner}" > style="display: none;" </s:if>>
								<div class="control-group">
									<label class="control-label">Data Source<span class="req star">*</span></label>
									<div class="controls">
										<input class="span4" <s:if test="%{companyTypeToUpdate == companyTypeDemandPartner}" >required="required"</s:if>  title="Data Source is required" maxlength="49" type="text" id="dataSource" name="dataSource" value= "<s:property value='dataSource'/>">
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">Demand Partner Category</label>
									<div class="controls">
										<input class="span4" maxlength="49" type="text" id="demandPartnerCategory" name="demandPartnerCategory" value= "<s:property value='demandPartnerCategory'/>">
									</div>
								</div>
	                             
	                            <div class="control-group">
	                              <label class="control-label">Demand Partner Type 
	                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
								  </label>
	                                    <div class="controls">
	                                    <s:select cssClass="span4 with-search" id="demandPartnerType" name="demandPartnerType"
	            								 value="%{selectedDemandPartnerTypeList.{id}}" list="demandPartnerTypeList" listKey="id" listValue="value">
	            						</s:select>
	                                    <br>
	                                   	</div> 
	                            </div>
						        
						        <div class="control-group">
									<label class="control-label">Passback Site Type</label>
									<div class="controls">
										<div id='passbackSiteTypeGroup'>
											<s:iterator value="passbackSiteTypeList" status="stat">
												<div <s:if test="%{id != 1}"> class="clearBoth" </s:if> id="passbackSiteTypeDiv<s:property value="id"/>">
													<div class="floatLeft">
														<label class="passBackLabel">Passback Site Type : </label>
													</div>
													<div class="passBackInput">
														<input type="text" id="passbackSiteType<s:property value='id'/>" name="passbackSiteType" value="<s:property value='value'/>">
													</div>
													<div class="passBackInput">
														<img class="icon-zoom-in" onclick="removePassbackSiteType('<s:property value='id'/>');" src="../img/close_icon.png">
													</div>
												</div>
											</s:iterator>
										</div>
										<br><br>
										<a class="btn btn-success btn-mini" id='addButton' href="javascript:addMorePassbackSiteType();">Add More</a>
	                                </div>
								</div>
							</div>
							
							<div class="control-group">
	                            <label class="control-label">Ad server information 
	                             	<span  class="help-Helpwidgets-TooltipWidget"></span>
							    </label>
                                <div class="controls">
                                	<label class="radio inline">
									  <input type="radio" id="radioAdserverDFP" name="adServerInfo" onclick="javascript: $('#serviceURLCredentials').css('display', 'none'); $('#dfpCredentials').css('display', 'block'); addCssClassOnDFPCredentials();" value="<s:property value="dfpDataSource"/>"
									  	<s:if test="%{adServerInfo == dfpDataSource || adServerInfo != 'Other'}" > checked="checked" </s:if> />
									  <s:property value="dfpDataSource"/>
									</label>
									
									<label class="radio inline">
									  <input type="radio" id="radioAdserverOther" name="adServerInfo" onclick="javascript: $('#dfpCredentials').css('display', 'none'); $('#serviceURLCredentials').css('display', 'block'); removeCssClassOnDFPCredentials();" value="Other"
									  <s:if test="%{adServerInfo == 'Other'}" > checked="checked" </s:if> />
									  Other
									</label>
                               	</div> 
                            </div>
                             
                            <div id="dfpCredentials" class="control-group" <s:if test="%{adServerInfo == 'Other'}" > style="display: none;" </s:if>>
								<label class="control-label"><s:property value="dfpDataSource"/> Credentials</label>
								<div class="controls">
									<div id='adServerCredentialsGroup'>
										 <s:iterator value="adServerCredentialsDTOList" status="stat">
										 	<div <s:if test="%{counter != 1}"> class="clearBoth" </s:if> id="adServerCredentialsDiv<s:property value='counter'/>">
												<div class="floatLeft">
													<input type="text" required="required" maxlength="49" placeholder="Ad server id" id="adServerId<s:property value='counter'/>" name="adServerId" value="<s:property value='adServerId'/>">
												</div>
												<div class="passBackInput">
													<input type="text" required="required" maxlength="49" placeholder="Ad server username" id="adServerUsername<s:property value='counter'/>" name="adServerUsername" value="<s:property value='adServerUsername'/>" autocomplete="off">
												</div>
												<div class="passBackInput">
													<input type="password" required="required" maxlength="49" placeholder="Ad server password" id="adServerPassword<s:property value='counter'/>" name="adServerPassword" value="<s:property value='adServerPassword'/>" autocomplete="off" onpaste="return false" oncopy="return false" ondrag="return false" ondrop="return false">
												</div>
												<div class="passBackInput">
													<img class="icon-zoom-in" onclick="removeAdServerCredentials('<s:property value='counter'/>');" src="../img/close_icon.png">
												</div>
											</div>
										 </s:iterator>
									</div>
									<br><br>
									<a class="btn btn-success btn-mini" id='addButton' href="javascript:addMoreAdServerCredentials();">Add More</a>
								</div>
							</div>
							
							<div id="serviceURLCredentials" class="control-group" <s:if test="%{adServerInfo != 'Other'}" > style="display: none;" </s:if> >
								<label class="control-label">Service URL</label>
								<div class="controls">
									<div id='serviceURLGroup'>
										<s:iterator value="serviceURLList" status="stat">
											<div <s:if test="%{id != 1}"> class="clearBoth" </s:if> id="serviceURLDiv<s:property value="id"/>">
												<div class="floatLeft">
													<label class="passBackLabel"></label>
												</div>
												<div class="passBackInput">
													<input type="text" id="serviceURL<s:property value='id'/>" name="serviceURL" value="<s:property value='value'/>">
												</div>
												<div class="passBackInput">
													<img class="icon-zoom-in" onclick="removeServiceURL('<s:property value='id'/>');" src="../img/close_icon.png">
												</div>
											</div>
										</s:iterator>
									</div>
									<br><br>
									<a class="btn btn-success btn-mini" id='addButton' href="javascript:addMoreServiceURL();">Add More</a>
                                </div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="multiSelect">App Views</label>
								<div class="controls">
									<s:select cssClass="span4 with-search"	multiple="true" id="selectAppViews"
	           						 name="appViews" list="appViewsList" listKey="id" listValue="value" value="%{selectedAppViewsList.{id}}">
	           						</s:select>
								</div>
							</div>
							
							<div class="control-group">
								<label class="checkbox">
									<div class="checker">
										<span><input type="checkbox" name="accessToAccounts"
										<s:if test="%{accessToAccounts == 1}" > checked="checked" </s:if> value="1"></span>
									</div>
									Has access to accounts
								</label>
								<div id="accessPropertiesCheckBoxDiv" <s:if test="%{companyTypeToUpdate != companyTypePublisherPartner}" > style="display: none;" </s:if>><label class="checkbox">
									<div class="checker">
										<span><input type="checkbox" name="accessToProperties"
										<s:if test="%{accessToProperties == 1}" > checked="checked" </s:if> value="1"></span>
									</div>
									Has access to properties
								</label></div>
							</div>

							<div class="control-group">
								<!-- <div class="controls" class="shadow"> -->
									<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
									<button type="reset" onclick="fromUpdateToCompanySetup();" class="btn btn-danger btn-large floatRight">Cancel</button>
								<!-- </div> -->
							</div>

						 </fieldset>
						 <input type="hidden" id="hiddenVal" name="companyId" value="<s:property value='companyId'/>" >
						 <input type="hidden" name="formerCompanyType" value="<s:property value='companyTypeToUpdate'/>" >
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

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
