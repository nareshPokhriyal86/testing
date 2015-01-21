<%@taglib uri="/struts-tags" prefix="s"%>
<!-- <span id="agency_first" style="font-size: 16px;">ACCOUNT : <select
	id="accountDropDown" style="width: 30%; margin-top: 10px;"
	onchange="getOrderDropDownData()">
		<option value="-1">Select Account</option>
</select> </span> -->

<span id="agency_first" class="form-horizontal themed filterPadding">
	<label class="control-label filterLabel">ACCOUNT :&nbsp</label>
	<div style="margin-left : 0%;"><select class="span3 with-search" onchange="getOrderDropDownData()" id="accountDropDown" name="companyId">	
	</select></div>
</span>

<span id="advertiser_first" class="form-horizontal themed filterPadding">
	<label class="control-label filterLabel">CAMPAIGN :&nbsp</label>
	<div style="margin-left : 0%;"><select id="campaignDropDown" class="span3 with-search" onchange="javascript:{campaignId = $('#campaignDropDown').val()}">
	</select></div>
</span>


<div style="display: block;width:170px;float: left; margin :0px 0px 0px 29px">
	<div style="display: block;width:auto;float: left;">
		<label class="control-label filterLabel" style="font-size:12px;">NOISE FILTER :&nbsp</label>
		<div id="isNoise" class="switch switch-mini" data-on="warning" data-off="danger">
		    <input type="checkbox" value="1" checked /> 
		</div>
	</div>
	<div id="thresholdSlider" class="summary_bar" style="float:right;">
		<fieldset id="uislider-demo" class="sliderCTR">
			<div class="controls">
				<input type="text" size="10" id="threshold" class="threshold-slider-value ui-display-label-white" />
				<div id="" class="threshold-slider-range warning-slider"></div>
			</div>
		</fieldset>
	</div>
</div>								
								
<span style="width: 30%; margin-left: 2%; z-index: 10;"> <a
	href="#" onclick="applyFilters()" class="btn medium btn-success">GO</a>
</span>

<!-- <div style="margin-left: 83%; margin-top: -2%;">
	<div id="isNoise" class="switch switch-mini" data-on="warning" data-off="danger">
	    <input type="checkbox" value="1" checked /> 
	</div> 
	
	
</div>
-->
<%-- <input type="text" id="threshold" value="<s:property value="threshold"/>"/> --%>
<div style="margin-right: 1%; float: right; margin-top: 0%;">
	<%-- <s:if test="%{authorisationKeywordList.contains('cpCampaignSettings')}">
		<a title="Setting" class="btn" style="height: 19px;" href="#"><i class="cus-cog"></i></a>
	</s:if> --%>
<%-- 	<s:if test="%{authorisationKeywordList.contains('cpCampaignSettings')}">
		<a title="Print" class="btn" style="height: 19px;" href="javascript:printSummary();"><i class="cus-printer"></i></a>
	</s:if> --%>
 	<s:if test="%{authorisationKeywordList.contains('cpDownloadReport')}">
		<a title="Download Report" class="btn" style="height: 19px;" href="javascript:downLoadAdvertiserReport();">
			<i class="cus-doc-excel-table"></i></a>
	</s:if>
</div>