<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="theme" value="'simple'" scope="page" />

<html lang="en">
<head>
		
<script>
localStorage.clear();
</script>
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>SynergyMAP - Reset Password</title>
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
<script src="../js/common.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/userManagement.js?v=<s:property value="deploymentVersion"/>"></script>

</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->
	<header>
		<!-- tool bar -->
			<div id="header-toolbar" class="container-fluid">
				<!-- .contained -->
				<div class="contained">

					<!-- theme name -->
					<h1>
					<!-- <img src="../img/oneAnalytics.png" width="100" height="32"/> -->
					<img src="../img/SynergyMapImages.png" width="" height="32"/>
					<span class="hidden-phone"></span>
					</h1>
					<!-- end theme name -->

					<!-- span4 -->
					<div class="pull-right">
						<!-- demo theme switcher-->
						<div id="theme-switcher" class="btn-toolbar">

							<!-- search and log off button for phone devices -->
							<div class="btn-group pull-right visible-phone">
								<a href="javascript:void(0)" class="btn btn-inverse btn-small"><i
								class="icon-search"></i></a> <a href="login.html"
								class="btn btn-inverse btn-small"><i class="icon-off"></i></a>
							</div>
							<!-- end buttons for phone device -->

							<!-- theme dropdown -->
							<div class="btn-group hidden-phone" style="margin-left:4px;">
								<a href="javascript:void(0)" class="btn btn-small btn-inverse "
									id="reset-widget"><i class="icon-refresh"></i></a>
							</div>
							<!-- end theme dropdown-->

						</div>
						<!-- end demo theme switcher-->
					</div>
					<!-- end span4 -->
				</div>
			<!-- end .contained -->
	   </div>
		<!-- end tool bar -->

	</header>
	<!-- end header -->

	<div id="main" role="main" class="container-fluid">

		<div style="margin:0 auto; width:400px; background:#292929; height:200px; margin-top:200px;">
        <div class="fluid-container">
						
						<!-- widget grid -->
						<section id="widget-grid" class="">
							
							<!-- row-fluid -->
							
							<div class="row-fluid">
								<article class="span12 sortable-grid ui-sortable">
									<!-- new widget -->
									<div class="jarviswidget jarviswidget-sortable" id="widget-id-0" role="widget">
									    
									    <!-- wrap div -->
									    <div role="content">
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text">
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
									        
									         <s:if test="%{linStatus == 'failed'}" >
												<div class="widget alert alert-error adjusted">
													<i class="cus-cross-octagon"></i>
													<strong>Error!</strong>Please try again.
												</div>
											</s:if>
											<s:elseif test="%{linStatus == 'inappropriate'}" >
												<div class="widget alert alert-error adjusted">
													<i class="cus-cross-octagon"></i>
													<strong>Error!</strong>Wrong data.
												</div>
											</s:elseif>
									        
									        <!-- content goes here -->
												<s:form id="validate-demo-js" cssClass="form-horizontal themed" action="resetPassword" onsubmit="confirmEmail('password', 'passwordRepeat');">
														<fieldset>
																<div class="row-fluid">
																										

																	<div class="control-group">
																		<label class="control-label">Enter Password<span class="req" 
																				style="color: #ff0000; font-weight: bold;">*</span></label>
																		<div class="controls">
																			<input type="password" onblur="confirmPassword(this);" required="required" title="Password is required" class="required span12" name="password" id="password">
																		</div>
																	</div>

																	<div class="control-group" style="background:none">
																		<label class="control-label">Confirm Password<span class="req" 
																				style="color: #ff0000; font-weight: bold;">*</span></label>
																		<div class="controls">
																			<input type="password" onblur="confirmPassword(this);" required="required" title="Password is required" class="required span12" name="passwordRepeat" id="passwordRepeat">
																		</div>
																	</div>
														
																	<div class="form-actions" style="min-height:0px;">
																		
																		<button type="submit" class="btn medium btn-primary">
																			Submit
																		</button>
																	</div>

																</div>
															
														</fieldset>
														<input type="hidden" name="id" value="<s:property value='id'/>" >
														<input type="hidden" name="randomNumber" value="<s:property value='randomNumber'/>" >
													</s:form>
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
							</div>
							
							<!-- end row-fluid -->
							
						</section>
						<!-- end widget grid -->
		</div>
        
        
        
        
        </div>
				
	</div>
			<!-- end main content -->

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
