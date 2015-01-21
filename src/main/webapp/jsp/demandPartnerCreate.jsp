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
		
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-41865102-1', 'linmobile-backend.appspot.com');
  ga('send', 'pageview', {
		  'page': 'userCreate.jsp',
		  'title': 'Create New User'
		});
  ga('set', 'dimension1', '<s:property value="#session.sessionDTO.userId"/>');
  ga('set', 'dimension2', '<s:property value="#session.sessionDTO.userName"/>');

</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>SynergyMAP</title>
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
<script src="../js/include/jquery.min.js"></script>
<script src="../js/include/jquery.jgrowl_minimized.js"></script>
<script src="../js/common.js"></script>
<script src="../js/userManagement.js"></script>


</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->
	
	<!-- end header -->

	<div id="main" role="main" class="container-fluid">

		<div class="contained">
			<!-- aside -->
			<jsp:include page="userManagementLeftMenu.jsp"/>
			<!-- aside end -->

			<!-- main content -->
			<div id="page-content" class="mlr">
			
			<%-- <jsp:include page="filter.jsp"/> --%>
            <h1 id="page-header">New Demand Partner </h1>
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
		                            <form class="form-horizontal themed">
													<fieldset>
													<%-- <div class="control-group">
															<label class="control-label" for="select01">Channel</label>
															<div class="controls">
																<select id="select01" class="span4 with-search">
													                <option value="AK">Local salesdirect</option>
													                <option value="HI">National Salesdirect</option>
													                <option value="CA">Undertone</option>
													                <option value="NV">Nexage</option>
													                <option value="OR">Mojiva</option>
													                <option value="WA">Google AdExchange</option>
													                <option value="AZ">House</option>
													                														        </select>
															</div>
														</div> --%>
														
														<div style="font-weight:bold; font-size:13px; color:#000; margin:20px 20px 0px 20px;">Demand Partner Name:-</div>
														<div class="control-group">
															<label class="control-label" for="input01">Name</label>
															<div class="controls">
																<input type="text" class="span4"  id="input01" />
															</div>
														</div>
														
														
													<%-- 	<div class="control-group">											
															<label class="control-label" for="multiSelect">Channel Demand Partner</label>
															<div class="controls">
																<select multiple="multiple" id="multiSelect" class="span4 with-search">
																	<option value="AK">Mojiva</option>
													                <option value="HI">Lin Media</option>  
																</select>
															</div>
														</div> --%>
														<div style="font-weight:bold; font-size:13px; color:#000; margin:20px 20px 0px 20px;">Contact Person Details:-</div>
														<div class="control-group">
															<label class="control-label" for="input01">Name</label>
															<div class="controls">
																<input type="text" class="span4"  id="input01" />
															</div>
														</div>
													
													<div class="control-group">
															<label class="control-label" for="input01">Email</label>
															<div class="controls">
																<input type="text" class="span4"  id="input01" />
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Phone Number</label>
															<div class="controls">
																<input type="text" class="span4"  id="input01" />
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="textarea">Address</label>
															<div class="controls">
																<textarea class="span4" id="textarea" rows="3"></textarea>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Website</label>
															<div class="controls">
																<input type="text" class="span4"  id="input01" />
															</div>
													</div>	
														
													
											<div class="control-group">
                                                      <label class="control-label">Status 
                                                       	<span class="help-Helpwidgets-TooltipWidget"></span>
							                            </label>
                                                   <div class="controls">
                                    
            						                    <select name="status" id="statusId" class="span4">
                                                                <option value="Active">Active</option>
                                                                <option value="Inactive">Inactive</option>
                                                         </select>
                                                          <br>
                                                     </div>
                                              </div>
														
														
												     
												 
								<div class="control-group">
								<div class="controls" class="shadow">
									<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
									<button type="reset" class="btn btn-danger btn-large">Cancel</button>
								</div>
							</div>
														
														
													</fieldset>
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
</html>
