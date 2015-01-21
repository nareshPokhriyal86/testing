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
			$('#propertyLi').attr('class', 'active');
		});
				localStorage.clear();
				
		</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Update Property</title>
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
            <h1 id="page-header">Update Property</h1>
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
						<s:form name = "updatePropertyForm" id="select-demo-js" cssClass="form-horizontal themed" action="updateProperty" onsubmit="return validateProperties();">
							<fieldset>
							
							<%-- <div class="control-group">
                              <label class="control-label">Publisher 
                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
							  </label>
                                    <div class="controls">
                                    <s:select cssClass="span4 with-search" id="publisherId" name="publisherId" onchange="changePublisherForProperty();"
            								list="publishersList" value="%{selectedPublishersList.{id}}" listKey="id" listValue="value">
            						</s:select>
                                    <br>
                                   	</div> 
                             </div> --%>
                             
                             <div class="control-group">
								<label class="control-label" for="multiSelect">Publisher<span class="req star" >*</span></label>
								<div class="controls">
									<label><s:property value='companyName'/></label>
								</div>
							</div>
                             
                            <div class="control-group">
								<label class="control-label">DFP Property Name<span class="req star">*</span></label>
								<div class="controls">
									<!-- <input type='hidden'  data-init-text='' name='input' id='selectDFPPropertyName' style="width:31.6%;margin-left:-1px;"/>
									<br> -->
									<input type="text" required="required" readonly="readonly" class="span4" id="DFPPropertyName" name="DFPPropertyName"  value="<s:property value='DFPPropertyName'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Property Name<span class="req star" >*</span>
								</label>
								<div class="controls">
									<div><input class="span4" onblur="isAccountOrPropertyNameAvailable('propertyName', '<s:property value='propertyId'/>', '<s:property value='propertyName'/>', 'propertyNameAvailable');" required="required" title="Property Name is required" maxlength="49" type="text" id="propertyName" name="propertyName"  value="<s:property value='propertyName'/>"></div>
									<div id = "textValueAvailableImage" class="textValueAvailableImage"><img src="../img/tick.png" width="20"></div>
									<div id = "textValueAvailableText" class="textValueAvailableText">Property name not available. Try another?</div>
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
								<label class="control-label">Market</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="market" name="market" value="<s:property value='market'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">DMA Rank</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="DMARank" name="DMARank" value="<s:property value='DMARank'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Affiliation</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="affiliation" name="affiliation" value="<s:property value='affiliation'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Website</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="webSite" name="webSite" value="<s:property value='webSite'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Mobile Web URL</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="mobileWebURL" name="mobileWebURL" value="<s:property value='mobileWebURL'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Tablet Web URL</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="tabletWebURL" name="tabletWebURL" value="<s:property value='tabletWebURL'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">General Manager</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="generalManager" name="generalManager" value="<s:property value='generalManager'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Address</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="address" name="address" value="<s:property value='address'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Zipcode</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="zipCode" name="zipCode" value="<s:property value='zipCode'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">State</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="state" name="state" value="<s:property value='state'/>">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Phone</label>
								<div class="controls">
									<input class="span4" maxlength="49" type="text" id="phone" name="phone" value="<s:property value='phone'/>">
								</div>
							</div>

							<div class="control-group">
								<!-- <div class="controls" class="shadow"> -->
									<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
									<button type="reset" onclick="fromUpdateToPropertySetup();" class="btn btn-danger btn-large floatRight">Cancel</button>
								<!-- </div> -->
							</div>
							
							<input type="hidden" id="propertyId" name="propertyId" value="<s:property value='propertyId'/>">
							<input type="hidden" id="companyId" value="<s:property value='publisherId'/>" name="publisherId">
							<input type="hidden" id="DFPPropertyId" value="<s:property value='DFPPropertyId'/>" name="DFPPropertyId">
							<input type="hidden" id="networkId" name="networkId" value="<s:property value='networkId'/>">
							<input type="hidden" id="networkUsername" name="networkUsername" value="<s:property value='networkUsername'/>">
							
							<div id="adUnitChildsDiv">
								<s:iterator value="childs" status="stat">
									<input type="hidden" value="<s:property/>" name="childs">
								</s:iterator>
							</div>
							
							<%-- <input type="hidden" id="publisherIdsForBigQuery" value="<s:property value='publisherIdsForBigQuery'/>"> --%>
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
			<script type="text/javascript"> successMessage('Publisher created Successfully'); </script>
		</s:if>
		<s:elseif test="%{linStatus == 'failed'}">
			<script type="text/javascript"> errorMessage('Publisher create failed'); </script>
		</s:elseif>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
