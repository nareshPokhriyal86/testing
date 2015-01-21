<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="theme" value="'simple'" scope="page" />

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Login</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<!-- Le javascript
    ================================================== -->
<script
	src="js/userManagement.js?v=<s:property value='deploymentVersion'/>"></script>

<!-- Le CSS
    ================================================== -->
<jsp:include page="css.jsp" />

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
						<div style="margin-top: 11px; margin-left: 7px; float: left;"></div>
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
							<div class="btn-group hidden-phone">
								<a href="javascript:void(0)" class="btn btn-small btn-inverse"
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
		<div id="login-logo"></div>
		<div class="jarviswidget login" id="login_page">
			<header>
				<h2>
					<img src="img/oneAnalytics.png"
						style="width: 120px; margin-right: 6px; margin-top: -14px;">-
					Sign In
				</h2>
			</header>

			<s:if test="%{loginStatus == 'noRole'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong>Sorry, no
					role assigned to you.
				</div>
			</s:if>
			<s:elseif test="%{loginStatus == 'UnauthorisedGoogleAccount'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong>
					Unauthorised Google Account.
				</div>
			</s:elseif>
			<s:elseif test="%{loginStatus == 'inactive'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong> Inactive
					account.
				</div>
			</s:elseif>
			<s:elseif test="%{loginStatus == 'alreadyLoggedIn'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong> Already
					logged in with another account.
				</div>
			</s:elseif>
			<s:elseif test="%{loginStatus == 'invalidUser'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong> The email
					id or password you entered is incorrect.
				</div>
			</s:elseif>
			<s:elseif test="%{loginStatus == 'sessionExpired'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Session expired.
						Please login again.</strong>
				</div>
			</s:elseif>
			<s:elseif test="%{loginStatus == 'logout'}">
				<div class="signedOut widget alert adjusted-login">
					<!-- <i class="cus-exclamation"></i> -->
					<strong>Signed out successfully</strong>
				</div>
			</s:elseif>
			<s:elseif test="%{forgetPasswordStatus == 'mailSend'}">
				<div class="widget alert alert-info adjusted">
					<i class="cus-exclamation"></i> <strong>Password reset
						link sent to your email id on records</strong>
				</div>
			</s:elseif>
			<s:elseif test="%{forgetPasswordStatus == 'emailNotFound'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong> The email
					id you entered is incorrect.
				</div>
			</s:elseif>
			<s:elseif test="%{forgetPasswordStatus == 'inactiveUser'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong> Inactive
					account.
				</div>
			</s:elseif>
			<s:elseif test="%{forgetPasswordStatus == 'tryAgain'}">
				<div class="widget alert alert-error adjusted">
					<i class="cus-cross-octagon"></i> <strong>Error!</strong> Please
					try again.
				</div>
			</s:elseif>
			<s:else>
				<div class="widget alert adjusted-login "
					style="padding-left: 13px;">
					<!-- <i class="cus-exclamation"></i> -->
					<strong>Please type in your email id and password.</strong>
				</div>
			</s:else>
			<!-- content goes here -->
			<div id="main-content" class="login_mainbox">

				<s:form name="loginForm" cssClass="form-signin" id="target"
					action="login.lin">
					<div class="control-group">
						<input type="email" required="required" title="Email is required"
							name="emailId" class="input-block-level" autofocus
							placeholder="Email Id" value="">
					</div>
					<div class="control-group">
						<input type="password" required="required"
							title="Password is required" name="password" id="passwordId"
							class="input-block-level" placeholder="Password" value="">
					</div>
					<div class="control-group no-border">
						<div class="signIn_outer">
							<input class="btn btn-login" type="submit" value="Sign in"
								id="login-btn">
							<div class="signIn_button">
								<a href="/GoogleLoginOAuthIndexServlet"> Sign in with Google
									<span>&nbsp;<img src="img/icon_googleaccounts1.png"></span>
								</a>
							</div>
						</div>
						<div class="forget_pass">
							<a href="javascript:void(0);" id="forget"> Forgot password</a>
						</div>
					</div>
				</s:form>

			</div>
			<!-- end content goes here -->

		</div>

		<div class="jarviswidget login" style="display: none;"
			id="forget_password">
			<header>
				<h2>
					<img src="img/oneAnalytics.png"
						style="width: 120px; margin-right: 6px; margin-top: -14px;">-
					Sign In
				</h2>
			</header>

			<div class="widget alert adjusted-login " style="padding-left: 13px;">
				<!-- <i class="cus-exclamation"></i> -->
				<strong>Please type in your email id</strong>
			</div>
			<!-- content goes here -->
			<div id="main-content" class="login_mainbox">

				<s:form class="form-signin" name="forgetPasswordForm" id="target"
					action="forgetPassword.lin">
					<div class="control-group">
						<input type="email" required="required"
							title="Email Id is required" name="emailId"
							class="input-block-level" autofocus placeholder="Email Id"
							value="">
					</div>
					<div class="control-group no-border">
						<div class="submit_button">
							<input class="btn btn-login" type="submit" value="Submit"
								id="login-btn">

						</div>
						<div class="sign_again">
							<a href="javascript:void(0);" id="login_signin"> Sign in</a>

						</div>
					</div>
				</s:form>
			</div>
			<!-- end content goes here -->
		</div>
	</div>


	<!-- end .height-wrapper -->

	<!-- footer -->

	<!-- if you like you can insert your footer here -->

	<!-- end footer -->

	<!--================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<!-- Le javascript
    ================================================== -->
	<jsp:include page="js.jsp" />

	<script>
		$(document).ready(function() {
			$('#forget').click(function() {
				$('#login_page').hide(1000);
				$('#forget_password').show();
			});

			$('#login_signin').click(function() {
				$('#login_page').show(1000);
				$('#forget_password').hide();
			});
		});
	</script>

	<!-- end scripts -->
</body>
<jsp:include page="googleAnalytics.jsp" />
</html>
