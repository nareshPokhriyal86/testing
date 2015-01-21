<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!-- Link to Google CDN's jQuery + jQueryUI; fall back to local -->
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<!-- IMPORTANT: Jquery Touch Punch is always placed under Jquery UI -->
<script type='text/javascript'
	src='../js/include/jquery.ui.touch-punch.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Mobile responsive menu generator -->
<script type='text/javascript'
	src='../js/include/selectnav.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Datatable components -->
<script type='text/javascript'
	src='../js/include/jquery.accordion.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Toastr & Jgrowl notifications  -->
<script type='text/javascript'
	src='../js/include/toastr.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/jquery.jgrowl.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Sleek scroll UI  -->
<script type='text/javascript'
	src='../js/include/slimScroll.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Datatable components -->
<script type='text/javascript'
	src='../js/include/jquery.dataTables.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/DT_bootstrap.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Form element skin  -->
<script type='text/javascript'
	src='../js/include/jquery.uniform.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: AmCharts  -->
<script type='text/javascript'
	src='../js/include/amchart/amcharts.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/amchart/amchart-draw1.js?v=<s:property value="deploymentVersion"/>'></script>
<!-- REQUIRED: iButton -->
<script type='text/javascript'
	src='../js/include/jquery.ibutton.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Justgage animated charts -->
<script type='text/javascript'
	src='../js/include/raphael.2.1.0.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/justgage.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Morris Charts -->
<script type='text/javascript'
	src='../js/include/morris.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/morris-chart-settings.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Animated pie chart -->
<script type='text/javascript'
	src='../js/include/jquery.easy-pie-chart.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Functional Widgets -->
<script type='text/javascript'
	src='../js/include/jarvis.widget.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/mobiledevices.min.js?v=<s:property value="deploymentVersion"/>'></script>
<!-- DISABLED (only needed for IE7 <script src="js/include/json2.js"></script> -->

<!-- REQUIRED: Full Calendar -->
<script type='text/javascript'
	src='../js/include/jquery.fullcalendar.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Flot Chart Engine -->
<script type='text/javascript'
	src='../js/include/jquery.flot.cust.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/jquery.flot.resize.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/jquery.flot.tooltip.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/jquery.flot.orderBar.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/jquery.flot.fillbetween.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/jquery.flot.pie.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Sparkline Charts -->
<script type='text/javascript'
	src='../js/include/jquery.sparkline.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Infinite Sliding Menu (used with inbox page) -->
<script type='text/javascript'
	src='../js/include/jquery.inbox.slashc.sliding-menu.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Form validation plugin -->
<script type='text/javascript'
	src='../js/include/jquery.validate.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Progress bar animation -->
<script type='text/javascript'
	src='../js/include/bootstrap-progressbar.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: wysihtml5 editor -->
<script type='text/javascript'
	src='../js/include/wysihtml5-0.3.0.min.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/include/bootstrap-wysihtml5.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Masked Input -->
<script type='text/javascript'
	src='../js/include/jquery.maskedinput.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Bootstrap Date Picker -->
<script type='text/javascript'
	src='../js/include/bootstrap-datepicker.min.js?v=<s:property value="deploymentVersion"/>'></script>

<!-- REQUIRED: Bootstrap Wizard -->
<script type='text/javascript'
	src='./js/include/bootstrap.wizard.min.js'></script>

<!-- REQUIRED: Bootstrap Color Picker -->
<script type='text/javascript'
	src='../js/include/bootstrap-colorpicker.min.js'></script>

<!-- REQUIRED: Bootstrap Time Picker -->
<script type='text/javascript'
	src='../js/include/bootstrap-timepicker.min.js'></script>

<!-- REQUIRED: Bootstrap Prompt -->
<script type='text/javascript' src='../js/include/bootbox.min.js'></script>

<!-- REQUIRED: Bootstrap engine -->
<script type='text/javascript' src='../js/include/bootstrap.min.js'></script>

<!-- DO NOT REMOVE: Theme Config file -->
<script type='text/javascript' src='../js/config.js'></script>
<script type='text/javascript'
	src='../js/script.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/moment.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/daterangepicker.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/jquery.tabSlideOut.v1.3.js?v=<s:property value="deploymentVersion"/>'></script>
<script type='text/javascript'
	src='../js/common.js?v=<s:property value="deploymentVersion"/>'></script>

<script type="text/javascript">
	window.jQuery
			|| document
					.write('<script src="../js/libs/jquery.min.js"><\/script>');

	window.jQuery.ui
			|| document
					.write('<script src="../js/libs/jquery.ui.min.js"><\/script>');

	var ismobile = (/ipod|android|blackberry|mini|windows\sce|palm/i
			.test(navigator.userAgent.toLowerCase()));
	if (!ismobile) {

		/** ONLY EXECUTE THESE CODES IF MOBILE DETECTION IS FALSE **/

		/* REQUIRED: Datatable PDF/Excel output componant */

		document
				.write('<script src="../js/include/ZeroClipboard.min.js"><\/script>');
		document
				.write('<script src="../js/include/TableTools.min.js"><\/script>');
		document.write('<script src="../js/include/select2.min.js"><\/script>');
		document
				.write('<script src="../js/include/jquery.excanvas.min.js"><\/script>');
		document
				.write('<script src="../js/include/jquery.placeholder.min.js"><\/script>');
	} else {

		/** ONLY EXECUTE THESE CODES IF MOBILE DETECTION IS TRUE **/

		document
				.write('<script src="../js/include/selectnav.min.js"><\/script>');
	}
	$('.carousel').carousel({
		interval : 2000
	});
	/* Start: function to show and hide news section*/

	function slide() {

		if ($('.slide-out-div').css('left') == '-396px') {
			$(".slide-out-div").animate({
				left : '0px'
			});
		} else {
			$(".slide-out-div").animate({
				left : '-396px'
			});
		}
	}

	var selectedID = [];
	var propertiesID = [];
	var propertyID = [];
	var unslectedID = [];
	var midSelectedID = [];

	function maintainFilterState() {
		$(':checkbox[name="checkMr"]').each(function() {
			if ($('#' + this.id).attr("checked") == "checked") {
				selectedID.push(this.id);
			}
		});
		$(':checkbox[name="checkProperties"]:checked').each(function() {
			propertiesID.push(this.id);
		});
		$(':checkbox[name="checkProperty"]:checked').each(function() {
			propertyID.push(this.id);
		});
	}

	function filter_apply() {
		$('#' + lastPopUpId).popover('hide');

		selectedID = [];
		unslectedID = [];
		propertiesID = [];
		propertyID = [];

		$(".alert-info-header").remove();

		$(':checkbox[name="checkMr"]').each(function() {
			if ($('#' + this.id).attr("checked") == "checked") {
				selectedID.push(this.id);
			} else {
				unslectedID.push(this.id);
			}
		});

		$(':checkbox[name="checkProperties"]:checked').each(function() {
			propertiesID.push(this.id);
		});
		$(':checkbox[name="checkProperty"]:checked').each(function() {
			propertyID.push(this.id);
		});

		// in order to show same sequence of channel label on header screen
		for (var i = selectedID.length - 1; i >= 0; i--) {

			$("#second_channel")
					.after(
							"<div class='widget alert alert-info-header adjusted' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: -6px;'>	<div style='float:left;margin-left:-21px;color:white;margin-right: 4px;'>	<strong style='text-transform: uppercase;'>"
									+ $("#" + selectedID[i]).parent().parent()
											.parent().text()
									+ "</strong> </div></div>");
			$("#first_channel")
					.after(
							"<div class='widget alert alert-info-header adjusted' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: -6px;'>	<div style='float:left;margin-left:-21px;color:white;margin-right: 4px;'>	<strong style='text-transform: uppercase;'>"
									+ $("#" + selectedID[i]).parent().parent()
											.parent().text()
									+ "</strong> </div></div>");
		}

		// inorder to make a list of selected channels
		allChannelName = "";
		for (var i = 0; i < selectedID.length; i++) {
			allChannelName = allChannelName
					+ $("#" + selectedID[i]).parent().parent().parent().text()
							.trim();
			if (i != selectedID.length - 1) {
				allChannelName = allChannelName + ",";
			}
		}

		publisherName1 = $("#pub_title").text();

		if (selectedID.length == 0) {
			toastr.error('Please select atleast one channel.')
		}
		if (selectedID.length != 0) {
			defaultExecutionFlag = 1;
			loadAllDataPublisher();
			if (previousSelectedPublisherId == selectedPublisherId) {
				fillReconciliationSummaryTable();
			} else if (selectedPublisherId != previousSelectedPublisherId) {
				previousSelectedPublisherId = selectedPublisherId;
				getAllReconciliationData();
			}
		}
		if (agencyname == null || agencyname == "" || agencyname == undefined) {

			$(".agency_second_filter").html("");
			$(".agency_option").remove();
			$(".agency_second_filter")
					.html(
							"<span class='select' style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>AGENCY </span><div style='float:left;color:white;margin-left:10px;clear:both'><span> All Agency</span></div>");
			$("#agency_first")
					.after(
							"<div style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 4px;' class='widget alert alert-info-header adjusted agency_option'><div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>All</strong></div> </div>");
		}

		if (advertisername == null || advertisername == ""
				|| advertisername == undefined) {

			$(".advertiser_option").remove();

			$("#advertiser_first")
					.after(
							"<div style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 4px;' class='widget alert alert-info-header adjusted advertiser_option'><div style='float:left;margin-left:-21px;color:white;'><strong>All</strong></div> </div>");
		}
		if (agencyname != null && agencyname != "") {
			$(".agency_option").remove();

			$("#agency_first")
					.after(
							"<div class='widget alert alert-info-header adjusted agency_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;MARGIN-TOP:2PX;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>"
									+ agencyname + "</strong> </div></div>");
		}
		if (advertisername != null && advertisername != "") {

			$(".advertiser_option").remove();

			$("#advertiser_first")
					.after(
							"<div class='widget alert alert-info-header adjusted advertiser_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:2px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>"
									+ advertisername + "</strong> </div></div>");
		}

		if (ordername == null || ordername == "" || ordername == undefined) {
			$(".order_option").remove();
			$("#order_trends_publisher")
					.after(
							"<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>All</strong> </div></div>");

		}
		if (ordername != null && ordername != "") {

			$(".order_option").remove();
			$("#order_trends_publisher")
					.after(
							"<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>"
									+ ordername + "</strong> </div></div>");

		}

		if (aArr.length == 0) {

			$(".lineOrder_option").remove();
			$("#lineItem_trends_publisher")
					.after(
							"<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>All</strong> </div></div>");

		}

		if (aArr.length == 1) {

			$(".lineOrder_option").remove();
			$("#lineItem_trends_publisher")
					.after(
							"<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>Single</strong> </div></div>");

		}
		acount = 0;
		if (aArr.length > 1) {

			$(".lineOrder_option").remove();
			$("#lineItem_trends_publisher")
					.after(
							"<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>Multiple</strong> </div></div>");
			$("#s2id_lineItemFilterPublisher").after("");
			for (var j = 0; j < aArr.length; j++) {

				$("#s2id_lineItemFilterPublisher")
						.after(
								"<div id='lineItem_DYNAMIC_"+acount+"' class='widget alert alert-info-header adjusted lineItem multipleLineItems' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;margin-bottom:10px;'>	<button data-dismiss='' class='close3' onclick=lineItem_close('"
										+ acount
										+ "'); style='margin-left:5px;margin-top:-7px;margin-right:-3px!important;background:none;border:none;'>×</button>	<div style='float:left;margin-left:-21px;color:white;width:300px;word-wrap:break-word;' id='label_"+acount+"'>	<strong>"
										+ aArr[j] + "</strong> </div>	</div>");
			}
		}
		$("#first_publisher").html($("#pub_title").text());
		$("#second_publisher").html($("#pub_title").text());
		$("#diagnosticToolsPublisher").html($("#pub_title").text());
		$(".slide-out-div").css({
			'left' : '-396px'
		});
		$(".slide-out-div").removeClass('open');

	}

	function operation_filter_apply() {

		selectedID = [];
		unslectedID = [];
		propertiesID = [];
		propertyID = [];

		$(".alert-info-header").remove();

		$(':checkbox[name="checkMr"]').each(function() {
			if ($('#' + this.id).attr("checked") == "checked") {
				selectedID.push(this.id);
			} else {
				unslectedID.push(this.id);
			}
		});

		$(':checkbox[name="checkProperties"]:checked').each(function() {
			propertiesID.push(this.id);
		});
		$(':checkbox[name="checkProperty"]:checked').each(function() {
			propertyID.push(this.id);
		});

		// inorder to make a list of selected channels
		allChannelName = "";
		for (var i = 0; i < selectedID.length; i++) {
			allChannelName = allChannelName
					+ $("#" + selectedID[i]).parent().parent().parent().text()
							.trim();
			if (i != selectedID.length - 1) {
				allChannelName = allChannelName + ",";
			}
		}

		if (selectedID.length == 0) {
			toastr.error('Please select atleast one channel.')
		}
		if (selectedID.length != 0) {
			if (previousSelectedPublisherId == selectedPublisherId) {
				fillReconciliationSummaryTable();
			} else if (selectedPublisherId != previousSelectedPublisherId) {
				previousSelectedPublisherId = selectedPublisherId;
				getAllReconciliationData();
			}
		}
		$("#diagnosticToolsPublisher").html($("#pub_title").text());
		$(".slide-out-div").css({
			'left' : '-396px'
		});
		$(".slide-out-div").removeClass('open');

	}

	function clear_apply() {
		for (var j = 0; j < unslectedID.length; j++) {
			$('#' + unslectedID[j] + '-span').removeAttr("class");
			$('#' + unslectedID[j]).removeAttr("checked");
		}
		for (var i = 0; i < selectedID.length; i++) {
			$('#' + selectedID[i] + '-span').attr("class", "checked");
			$('#' + selectedID[i]).attr("checked", "checked");
		}
		$(".slide-out-div").css({
			'left' : '-396px'
		});
		$(".slide-out-div").removeClass('open');
	}
	function changeDropdown(obj) {
		$("#pub_title").html($(obj).text());
		$(".dropdown-list").removeClass('open');
	}
	function changeAdvertiseViewPublisherDropdown(publisherName) {
		$("#advertiserViewPubTitle").html(publisherName);
		$(".dropdown-list").removeClass('open');
		selectedPublisher = publisherName;
		lineItem = '';
		lineItemArr = '';
		order = '';
		ordername = '';
		advertisername = '';
		agencyname = '';
		advertisers = '';
		agencies = '';
		loadAjaxCounter = 0;
		loadAjaxCounter1 = 0;
		loadAjaxCounter2 = 0;
		arrLoadAjaxCounter = [];
		isTrendDefault = true;
	}

	function changeAdvertiseViewPropertyDropdown(propertyName) {
		$("#advertiserViewPropertyTitle").html(propertyName);
		$(".dropdown-list").removeClass('open');
		SelectedProperty = propertyName;

	}

	function changePublisherViewPropertyDropdown(propertyName, DFP_propertyName) {
		$("#publisherViewPropertyTitle").html(propertyName);
		$(".dropdown-list").removeClass('open');
		SelectedPropertyPublisher = propertyName;
		SelectedDFP_propertyPublisher = DFP_propertyName;

	}

	function changeAdvertiserDropdown(obj) {
		$("#advertiser_title").html($(obj).text());
		$(".adver_dropdown-list").removeClass('open');
	}
	function changeAgencyDropdown(obj) {
		$("#agency_title").html($(obj).text());
		$(".agency_dropdown-list").removeClass('open');
	}
	function changeCampaignDropdown(obj) {
		$("#campaign_title").html($(obj).text());
		$(".campaign_dropdown-list").removeClass('open');
	}
	function changeLineDropdown(obj) {
		$("#line_title").html($(obj).text());
		$(".line_dropdown-list").removeClass('open');
	}

	var tabName = "";

	function getText(obj) {

		tabName = $(obj).text();
		if (tabName == "Trends and Analysis") {
			$("#orderSearch").css({
				'display' : 'inline'
			});
			$("#lineOrderSearch").css({
				'display' : 'inline'
			});
		}

		else {
			$("#orderSearch").css({
				'display' : 'none'
			});
			$("#lineOrderSearch").css({
				'display' : 'none'
			});
		}
	}

	function advertiser_filter_apply() {
		$('#' + lastPopUpId).popover('hide');
		$(".slide-out-div").css({
			'left' : '-396px'
		});
		$(".slide-out-div").removeClass('open');

		$(".agency_option").remove();
		$(".advertiser_option").remove();
		$(".order_option").remove();

		if (agencyname == null || agencyname == "" || agencyname == undefined) {

			$(".agency_second_filter").html("");
			$(".agency_option").remove();
			$(".agency_second_filter")
					.html(
							"<span class='select' style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>AGENCY </span><div style='float:left;color:white;margin-left:10px;clear:both'><span> All Agency</span></div>");
			$("#agency_first")
					.after(
							"<div style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 4px;' class='widget alert alert-info-header adjusted agency_option'><div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>All</strong></div> </div>");
		}

		if (advertisername == null || advertisername == ""
				|| advertisername == undefined) {

			$(".advertiser_second_filter").html("");
			$(".advertiser_option").remove();
			$(".advertiser_second_filter")
					.html(
							"<span class='select' style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>ADVERTISER  </span><div style='float:left;color:white;margin-left:10px;clear:both'><span> All Advertiser</span></div>");
			$("#advertiser_first")
					.after(
							"<div style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 4px;' class='widget alert alert-info-header adjusted advertiser_option'><div style='float:left;margin-left:-21px;color:white;'><strong>All</strong></div> </div>");
		}
		if (agencyname != null && agencyname != "") {
			$(".agency_second_filter").html("");
			$(".agency_option").remove();
			$(".agency_second_filter")
					.html(
							"<span class='select' style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>AGENCY </span><div style='float:left;color:white;margin-left:10px;clear:both'><span>"
									+ agencyname + "</span></div>");
			$("#agency_first")
					.after(
							"<div class='widget alert alert-info-header adjusted agency_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;MARGIN-TOP:2PX;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>"
									+ agencyname + "</strong> </div></div>");
		}
		if (advertisername != null && advertisername != "") {
			$(".advertiser_second_filter").html("");
			$(".advertiser_option").remove();
			$(".advertiser_second_filter")
					.html(
							"<span class='select' style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>ADVERTISER </span><div style='float:left;color:white;margin-left:10px;clear:both'><span>"
									+ advertisername + "</span></div>");
			$("#advertiser_first")
					.after(
							"<div class='widget alert alert-info-header adjusted advertiser_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:2px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>"
									+ advertisername + "</strong> </div></div>");
		}

		if (ordername == null || ordername == "" || ordername == undefined) {
			$(".order_option").remove();
			$("#order_trends")
					.after(
							"<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>All</strong> </div></div>");
			$("#order_reallocation")
					.after(
							"<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>All</strong> </div></div>");
		}
		if (ordername != null && ordername != "") {
			$(".order_option").remove();
			$("#order_trends")
					.after(
							"<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>"
									+ ordername + "</strong> </div></div>");
			$("#order_reallocation")
					.after(
							"<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong>"
									+ ordername + "</strong> </div></div>");
		}
		if (aArr.length == 0) {

			$(".lineOrder_option").remove();
			$("#lineItem_trends")
					.after(
							"<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>All</strong> </div></div>");
		}

		if (aArr.length == 1) {

			$(".lineOrder_option").remove();
			$("#lineItem_trends")
					.after(
							"<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>Single</strong> </div></div>");
		}
		if (aArr.length > 1) {
			$(".lineOrder_option").remove();
			$("#lineItem_trends")
					.after(
							"<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>Multiple</strong> </div></div>");
		}

		loadAllDataAdvertiser();

	}
	function advertiser_clear_apply() {
		$(".slide-out-div").css({
			'left' : '-396px'
		});
		$(".slide-out-div").removeClass('open');
	}
	function onAdvertiserTabClick(obj) {
		if ($(obj).text() == "Trends and Analysis") {
			$("#agency_dropdown").css({
				'display' : 'none'
			});
			$("#advertiser_dropdown").css({
				'display' : 'none'
			});
			$(".agency_second_filter").show();
			$(".advertiser_second_filter").show();
			$("#order_dropdown_text").css({
				'display' : 'inline'
			});
			$("#line_dropdown_text").css({
				'display' : 'inline'
			});
			$(".well").css({
				'display' : 'inline'
			});
			$(".slide-out-div").css({
				'display' : 'inline'
			});
			$("#reallocation_dropdown").css({
				'display' : 'none'
			});

		}
		if ($(obj).text() == "Reallocation") {
			$("#agency_dropdown").css({
				'display' : 'none'
			});
			$("#advertiser_dropdown").css({
				'display' : 'none'
			});
			$(".agency_second_filter").show();
			$(".advertiser_second_filter").show();
			$("#reallocation_dropdown").css({
				'display' : 'inline'
			});
			$(".well").css({
				'display' : 'inline'
			});
			$(".slide-out-div").css({
				'display' : 'inline'
			});

		}
		if ($(obj).text() == "Performance Summary") {
			$("#agency_dropdown").css({
				'display' : 'block'
			});
			$("#advertiser_dropdown").css({
				'display' : 'block'
			});

			$("#order_dropdown").css({
				'display' : 'none'
			});
			$("#lineItems_dropdown_single").css({
				'display' : 'none'
			});
			$("#lineItems_dropdown").css({
				'display' : 'none'
			});
			$(".agency_second_filter").css({
				'display' : 'none'
			});
			$(".advertiser_second_filter").css({
				'display' : 'none'
			});
			$("#order_dropdown_text").css({
				'display' : 'none'
			});
			$("#line_dropdown_text").css({
				'display' : 'none'
			});
			$(".well").css({
				'display' : 'inline'
			});
			$(".slide-out-div").css({
				'display' : 'inline'
			});
			$("#reallocation_dropdown").css({
				'display' : 'none'
			});

		}
		if ($(obj).text() == "Industry News and Research") {
			$(".well").css({
				'display' : 'none'
			});
			$(".slide-out-div").css({
				'display' : 'none'
			});
		}
	}

	$(document)
			.ready(
					function() {
						$('#indus_new').click(function() {
							$('#s4').show();
						});
						$('#inv_rev').click(function() {
							$('#s4').hide();
						});
						$('#tre_ana').click(function() {
							$('#s4').hide();
						});
						$('#rea_inv').click(function() {
							$('#s4').hide();
						});

						$('.dropdown-toggle').dropdown();// on click drop down toggle

						$("#agency_clear")
								.click(
										function() {
											$("#s2id_test")
													.children()
													.html(
															"<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");

											agencyname = '';
											$("#agency_clear").css({
												'display' : 'none'
											});
											$(".agency_second_filter")
													.html(
															"<span class='select' style='float:left;margin-left:9px;'>Agency :</span><div id='agencyId_second_filter' style='float:left;color:white;margin-left:10px;margin-top:8px;'><span  style='text-transform: uppercase;'></span></div>");
										});

						$("#advertiser_clear")
								.click(
										function() {

											$("#s2id_test2")
													.children()
													.html(
															"<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
											advertisername = '';
											$("#advertiser_clear").css({
												'display' : 'none'
											});
											$(".advertiser_second_filter")
													.html(
															"<span class='select' style='float:left;margin-left:9px;'>Advertiser :</span><div id='advertiserId_second_filter' style='float:left;color:white;margin-left:10px;margin-top:8px;'><span  style='text-transform: uppercase;'></span></div>");
										});
						$("#agency_clear_advertiser")
								.click(
										function() {
											$("#s2id_agencyFilter_Advertiser")
													.children()
													.html(
															"<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");

											agencyname = '';
											$("#agency_clear_advertiser").css({
												'display' : 'none'
											});

										});
						$("#advertiser_clear_Advertiser")
								.click(
										function() {

											$(
													"#s2id_advertiserFilter_Advertiser")
													.children()
													.html(
															"<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
											advertisername = '';
											$("#advertiser_clear_Advertiser")
													.css({
														'display' : 'none'
													});

										});

					});

	$(".slide-out-div").click(function() {

		$("#advertiserViewPublishersDropDown").removeClass("open");
		$("#advertiserViewPropertyDropDown").removeClass("open");
	});
</script>

