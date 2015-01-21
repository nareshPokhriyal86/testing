
	
<div ng-app="settingApp" ng-controller="settingCtrl">
				
			<div  id="percampaignperformance">
				
					<label class="control-label">Day and time&#160;&#160;<i class="icon-question-sign"></i>All days and times&#160;&#160;&#160;<font color="3366FF">edit</font></label>
					<label class="control-label">Frequency&#160;&#160;&#160;&#160;&#160;&#160;<i class="icon-question-sign"></i><input name="States" class="checkbox" type="checkbox" value="Nome" checked />:<b>Set per user frequency cap</b></label>
			</div>
			
				<input id="kpival" type="text" />
	    	<select id="kpi">
				<option value="1">eCPM</option>
				<option value="2">Impression</option>
				<option value="3">Click</option>
				<option value="4">Revenue</option>		
			</select>
			<button ng-click="addValue()">Add Value</button>
				<div style="width:350px;margin-left: 205px;">
					<input  id="benchmark" type="text"/>
				</div>
			<button onclick ="showValue()" style="margin-left: 205px;">Show Value</button>
	     
		  <!--  <div style="margin-left:205px;" ng-repeat="camp in campaigns" >
			
						<a ng-click="removeCampaign(camp)" style="color:BLUE" style="cursor:hand;"  >[x]</a>
						<input type="text"  name="imp" ng-model="campaign.valueofclicks"  style="width: 75px"><b>impressions per</b>
								 <input type="text"  name="duration"  style="width: 12px;"/>
								  <select  id="" name="" style="width: 80px;">
								<option>days</option>
								<option>weeks</option>
								<option>months</option>
								<option>years</option>
						</select>
	
					</div>
				
					<div style="margin-left:192px">
					<a href="#" style="color:BLUE;cursor: hand;" ng-click="addCampaign()" >+
						<font color="3366FF" style="font-size: 15px">&#160;&#160;Additional frequency cap</font></a>
				</div> -->
	
				
 </div>