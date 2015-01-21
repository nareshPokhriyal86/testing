
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<header> <!-- tool bar -->
<div id="header-toolbar" class="container-fluid">

	<!-- .contained -->
	<div class="contained">

		<div class="welcome" id="user_button">
			<div style="float: left; margin-top: -18px; margin-bottom: 10px;">
				<%-- <jsp:include page="DatePicker.jsp" /> --%>
			</div>
			<div style="float: left;">
				<strong>Welcome,</strong>
				<s:property value="#session.sessionDTO.userName" />
			</div>
			<div style="float: left">
				<div class="img_user_icon">
					<a href="#"> <img src="../img/arrow_fff.png" width="7"
						height="7" /></a>
				</div>
				<div class="ID-account_list details_icon vR" id="user_drop"
					style="display: none;">
					<div class="oV"
						style="font-family: 'Lato', Arial, Helvetica, sans-serif;">
						<div id="ID-accountPanel" class="sub_h clear">
							<a href="initUserOwnProfileUpdate.lin"><div class="Sub_1">My
									Settings</div></a>
						</div>

						<div id="ID-accountPanel" class="sub_h sub_panel">
							<a href="logout.lin"><div class="Sub_1">Sign-Out</div></a>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- theme name -->
		<s:if test="%{#session.sessionDTO.companyLogo}">
			<h1>
				<img src="<s:property value='#session.sessionDTO.companyLogoURL'/>"
					height="32" width="" align="left">
				<div class="site_title"></div>
				<span class="hidden-phone"></span>
			</h1>
		</s:if>
		<h1>
			<div class="site_title"></div>
			<span class="hidden-phone"></span>
		</h1>

		<div class="pull-left">
			<!-- demo theme switcher-->
			<div class="btn-group hidden-phone "
				style="margin-top: 8px; font-size: 1.25em;"></div>
			<!-- end .contained -->
		</div>
		<!-- end theme name -->

		<!-- span4 -->
		<div class="pull-right">
			<!-- demo theme switcher-->
			<div id="theme-switcher" class="btn-toolbar">

				<!-- search and log off button for phone devices -->
				<div class="btn-group pull-right visible-phone">
					<a href="logout.lin" class="btn btn-inverse btn-small"><i
						class="icon-off"></i></a>
				</div>
				<!-- end buttons for phone device -->
				<div class="btn-group hidden-phone" style="margin-left: 4px;">
					<a href="campaignAlertListing.lin"><img
						style="cursor: pointer; cursor: hand;" src="img/bell-image.png"></a>
				</div>

				<!-- theme dropdown -->
				<div class="btn-group hidden-phone" style="margin-left: 4px;">

					<a href="javascript:void(0)" class="btn btn-small btn-inverse "
						id="reset-widget"><i style="color: #FFF;" class="icon-refresh"></i></a>
					<!--</div>-->
					<!-- end theme dropdown-->

				</div>

				<!-- end demo theme switcher-->
			</div>
			<!-- end span4 -->
		</div>

		<!-- end .contained -->
	</div>
	<!-- end tool bar -->


</div>

<script type="text/javascript"
	src="../js/include/jquery.min.js?v=<s:property value='deploymentVersion'/>"></script>
<script>
	$(function() {
		$("#user_button").click(function(e) {
			$("#user_drop").slideToggle().toggleClass("active");
			e.stopPropagation();
		});

		$(document).click(function(e) {
			$('#user_drop').hide().removeClass('active');
		});
	});

	$(function() {
		var pull = $('#manu_button_toggle');
		menu = $('.mune_inear_box');
		$(pull).on('click', function(e) {
			e.preventDefault();
			menu.slideToggle();
		});

		$(window).resize(function() {
			var w = $(this).width();
			if (w > 800 && menu.is(':hidden')) {
				menu.removeAttr('style');
			}
		});
		$('.mune_inear_box').on('click', function(e) {
			var w = $(window).width();
			if (w < 480) {
				menu.slideToggle();
			}
		});

	});
</script> </header>
<!-- end header -->

