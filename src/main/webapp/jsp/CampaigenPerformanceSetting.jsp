 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
    <link href="../css/bootstrap-tagsinput.css" rel="stylesheet">
 <script src="../js/angular/angular.min.js"></script>
 <script src="../js/angular/controller/settingTab.js"/></script>
 <script src="../js/bootstrap-tagsinput-angular.js"></script>
 <script src="../js/bootstrap-tagsinput.js"></script>
 <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="left:8%;right:8%;margin-left:0px;display:none;">
	<div class="modal-header">
		<h3 id="myModalLabel"></h3>
	</div>
	<div class="modal-body" id ="modalDivId">
	
	<div id="" style="height:500px ; width:100%;display: table;">
	<div id="tab-content2" class="tab-content">
	
    <ul class="nav nav-tabs" id="myTab1" style="font-size:16px;">
        <li class="active"><a href="#t1" data-toggle="tab">Delivery Goals</a></li>
        <li ><a href="#t2" data-toggle="tab">Performance Benchmarks</a></li>
    </ul>
    <div class="tab-pane active" id="t1">
    <div class="control-group">
    		<label class="control-label">Campaign Lifetime</label>
				<div class="controls">
					<input class="span4"  maxlength="49" type="text"  name="CampaignLifetime" value="">
				</div>
	</div>
     <div class="tabbable tabs-left">
        <ul class="nav nav-tabs">
          <li class="active"><a href="#a" data-toggle="tab">Time Based</a></li>
          <li ><a href="#b" data-toggle="tab">Geo Targets Based</a></li>
          <li ><a href="#c" data-toggle="tab">Device/OS Targets Based</a></li>
        </ul>
        <div class="tab-content">
         <div class="tab-pane active" id="a">
             <jsp:include page="./CampaignSetting/TimeBasedTab.jsp" />
         </div>
         <div class="tab-pane" id="b">
              <jsp:include page="./CampaignSetting/GeoTargetBasedTab.jsp" />
         </div>
         <div class="tab-pane" id="c">
            <jsp:include page="./CampaignSetting/DeviceOsTargetTab.jsp" />
         </div>
        </div>
      </div> 
    
    </div>
    
    <div class="tab-pane" id="t2">
<div class="control-group">
         <label class="control-label"> Performance Benchmarks<span class="req star"
									>*</span>
								</label>
								<div class="controls">
									<input class="span4" required="required"  maxlength="49" type="text" id="campaignLifetime" name="campaignLifetime">
								</div>

</div>


</div>
    </div>
	 
      </div>

</div> 
</div>