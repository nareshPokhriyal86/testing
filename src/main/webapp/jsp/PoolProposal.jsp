
<%@taglib uri="/struts-tags" prefix="s"%>

<form id="proposalForm" class="form-horizontal themed"
	action="newProposal.lin" method="post">
	<div style="margin-left: 1%;">
		<s:if
			test="companyMap !=null && companyMap.size()==1 && !(#session.sessionDTO.superAdmin)">
			<!-- not a super admin -->
			<s:iterator value="companyMap">
				<input type="hidden" name="company" id="company"
					value='<s:property value="key"/>' />
			</s:iterator>
		</s:if>
		<s:else>
			<label class="control-label">Company</label>
			<s:if test="companyMap !=null && companyMap.size()>1">
				<Select name="company" class="span4" id="company"
					onchange="getAdvertiserAgencyByCompany();">
					<s:iterator value="companyMap">
						<option value='<s:property value="key"/>'>
							<s:property value="value" />
						</option>
					</s:iterator>
				</Select>
			</s:if>
			<s:elseif
				test="companyMap !=null && companyMap.size()==1 && #session.sessionDTO.superAdmin">
				<s:iterator value="companyMap">
					<input type="text" value='<s:property value="value"/>'
						readonly="readonly" class="span4" />
					<input type="hidden" name="company" id="company"
						value='<s:property value="key"/>' />
				</s:iterator>
			</s:elseif>
			<s:else>
				<Select name="company" class="span4" id="company">
					<option value=""></option>
				</Select>
			</s:else>
		</s:else>
	</div>
	<br>

	<!-- row-fluid -->

	<div style="margin-left: 1%;" class="row-fluid Profile">

		<article class="span6">
			<!-- new widget -->
			<div class="jarviswidget" id="widget-id-0">
				<!-- wrap div -->
				<div>

					<div class="jarviswidget-editbox">
						<div>
							<label>Title:</label> <input type="text" />
						</div>
						<div>
							<label>Styles:</label> <span data-widget-setstyle="purple"
								class="purple-btn"></span> <span data-widget-setstyle="navyblue"
								class="navyblue-btn"></span> <span data-widget-setstyle="green"
								class="green-btn"></span> <span data-widget-setstyle="yellow"
								class="yellow-btn"></span> <span data-widget-setstyle="orange"
								class="orange-btn"></span> <span data-widget-setstyle="pink"
								class="pink-btn"></span> <span data-widget-setstyle="red"
								class="red-btn"></span> <span data-widget-setstyle="darkgrey"
								class="darkgrey-btn"></span> <span data-widget-setstyle="black"
								class="black-btn"></span>
						</div>
					</div>

					<fieldset>
						<div class="inner-spacer">
							<!-- content goes here -->

							<div class="control-group">
								<label class="control-label" for="multiSelect">Campaign
									Name<span class="req star">*</span>
								</label>
								<div class="controls">
									<input name="proposalName" type="text" class="span12"
										id="proposalName" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="multiSelect">Advertiser/Client Name<span class="req star">*</span></label>
								<div class="controls">
									<s:if test="advertiserMap !=null && advertiserMap.size()>0">
									  <select name="advertiser" id="advertiser"  class="span12 with-search" onchange="chooseAdvertiser()">
										  <s:iterator value="advertiserMap">
										     <option value='<s:property value="key"/>'><s:property value="value"/></option>
										  </s:iterator>
									  </select>
									</s:if>
									<s:else>
									  <select name="advertiser" id="advertiser"  class="span12 with-search" onchange="chooseAdvertiser()">
									  </select>
									</s:else>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="input01">Agency</label>
								<div class="controls">
									 <s:if test="agencyMap !=null && agencyMap.size()>0">
									    <select name="agency" id="agency" class="span12 with-search" onchange="chooseAgency()">
									      <option value="-1">Select Agency</option>
									      <s:iterator value="agencyMap">																       
									       <option value='<s:property value="key"/>'><s:property value="value"/></option>
									      </s:iterator>
									     </select>
									    </s:if>
									    <s:else>
									       <select name="agency" id="agency" class="span12 with-search" onchange="chooseAgency()">
									         <option value="-1">Select Agency</option>
									       </select>
									    </s:else>																
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="input01">Campaign Type<span
									class="req star">*</span></label>
								<div class="controls">
									<Select name="proposalType" type="text" class="span12"
										id="proposalType">
										<option value="-1"></option>
										<s:if
											test="campaignTypeMap !=null && campaignTypeMap.size()>0">
											<s:iterator value="campaignTypeMap">
												<option value='<s:property value="key"/>'>
													<s:property value="value" />
												</option>
											</s:iterator>
										</s:if>
									</Select>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="input01">Industry<span
									class="req star">*</span></label>
								<div class="controls">
									<s:if test="industryMap !=null && industryMap.size()>0">
										<select name="industry" id="industry"
											onchange="chooseIndustry()" class="span12 with-search">
											<option value="-1"></option>
											<s:iterator value="industryMap">
												<option value='<s:property value="key"/>'>
													<s:property value="value" />
												</option>
											</s:iterator>
											<option value="0">New</option>
										</select>
										<div id="customIndustryDiv" style="display: none">
											<input name="customIndustry" type="text" class="span12"
												id="customIndustry" />
										</div>
									</s:if>
									<s:else>
										<select multiple="multiple" name="industry" id="industry"
											onchange="chooseIndustry()" class="span12 with-search">
											<option value="0">New</option>
										</select>
										<div id="customIndustryDiv">
											<input name="customIndustry" type="text" class="span12"
												id="customIndustry" />
										</div>
									</s:else>

								</div>
							</div>

							<div id="flightStartDateDiv" class="control-group">
								<label class="control-label">Flight Start Date<span
									class="req star">*</span></label>
								<div class="controls">
									<!-- id="datepicker-js" -->
									<div class="input-append date" data-date=""
										data-date-format="mm-dd-yyyy">
										<input readonly="true" class="datepicker-input" size="16"
											type="text" onchange="$('#flightStartDateDiv').mousedown();"
											name="flightStartDate" id="flightStartDate" value=""
											placeholder="Select a date" />
										<!--<span class="add-on"> <i class="cus-calendar-2"></i> -->
										</span>
									</div>
								</div>
							</div>

							<div id="flightEndDateDiv" class="control-group">
								<label class="control-label">Flight End Date<span
									class="req star">*</span></label>
								<div class="controls">
									<div class="input-append date" data-date=""
										data-date-format="mm-dd-yyyy">
										<input readonly="true" class="datepicker-input" size="16"
											type="text" onchange="$('#flightEndDateDiv').mousedown();"
											name="flightEndDate" id="flightEndDate" value=""
											placeholder="Select a date" />
										<%-- <span class="add-on"> <i class="cus-calendar-2"></i> --%>
										</span>
									</div>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">KPIs<span class="req star">*</span></label>
								<div class="controls">
									<s:if test="kpiMap !=null && kpiMap.size()>0">
										<select multiple="multiple" name="kpi" id="kpi"
											onchange="chooseKpi()" class="span12 with-search">
											<option value="-1"></option>
											<s:iterator value="kpiMap">
												<option value='<s:property value="key"/>'>
													<s:property value="value" />
												</option>
											</s:iterator>
											<option value="0">New</option>
										</select>
										<div id="customKpiDiv" style="display: none">
											<input name="customKpi" type="text" class="span12"
												id="customKpi" />
										</div>
									</s:if>
									<s:else>
										<select multiple="multiple" name="kpi" id="kpi"
											onchange="chooseKpi()" class="span12 with-search">
											<option value="0">New</option>
										</select>
										<div id="customKpiDiv">
											<input name="customKpi" type="text" class="span12"
												id="customKpi" />
										</div>
									</s:else>


								</div>
							</div>
						</div>
					</fieldset>
					<input type="hidden" id="proposalId" name="proposalId"
						value='<s:property value="proposalDTO.proposalId" />' /> <input
						type="hidden" id="nextPageControl" name="nextPageControl"
						value="0" /> <input type="hidden" id="placementData"
						name="placementData"
						value='<s:property value="proposalDTO.placementData" />' />

				</div>
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</article>

		<article style="margin-left: 0%;" class="span6">
			<!-- new widget -->
			<div class="jarviswidget" id="widget-id-0">
				<!-- wrap div -->
				<div>

					<div class="jarviswidget-editbox">
						<div>
							<label>Title:</label> <input type="text" />
						</div>
						<div>
							<label>Styles:</label> <span data-widget-setstyle="purple"
								class="purple-btn"></span> <span data-widget-setstyle="navyblue"
								class="navyblue-btn"></span> <span data-widget-setstyle="green"
								class="green-btn"></span> <span data-widget-setstyle="yellow"
								class="yellow-btn"></span> <span data-widget-setstyle="orange"
								class="orange-btn"></span> <span data-widget-setstyle="pink"
								class="pink-btn"></span> <span data-widget-setstyle="red"
								class="red-btn"></span> <span data-widget-setstyle="darkgrey"
								class="darkgrey-btn"></span> <span data-widget-setstyle="black"
								class="black-btn"></span>
						</div>
					</div>

					<fieldset>
						<div class="inner-spacer">
							<!-- content goes here -->

							<!-- <div class="control-group">
	<label class="control-label" for="multiSelect">Budget</label>
			
	<div class="controls">
		<input name="budget" type="text" onblur="validateBudget();" class="span12" id="budget" />
	</div>
</div> -->

							<div class="control-group">
								<label class="control-label" for="input01">Markets
									(DMAs)<span class="req star">*</span>
								</label>
								<div class="controls">
									<select multiple="multiple" name="geoTargets" id="geoTargets" 
										onchange="chooseGeoTargets()" class="span12 with-search">
										<option value="-1"></option>
										<s:iterator value="geoTargetMap">
											<option value='<s:property value="key"/>'>
												<s:property value="value" />
											</option>
										</s:iterator>
									</select>									
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="input01">Sales Contact
									Name<span class="req star">*</span>
								</label>
								<div class="controls">
									<input name="salesRep" type="text" class="span12" id="salesRep" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="multiSelect">Sales
									Email</label>
								<div class="controls">
									<input type="email" name="salesEmail" id="salesEmail"
										class="span12"
										value='<s:property value="proposalDTO.salesEmail" />' />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="multiSelect">Sales
									Phone</label>
								<div class="controls">
									<input type="text" name="salesPhone" id="salesPhone"
										class="span12"
										value='<s:property value="proposalDTO.salesPhone" />' />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="multiSelect">Trafficking
									Contact Name</label>
								<div class="controls">
									<input type="text" name="trafficContact" id="trafficContact"
										class="span12"
										value='<s:property value="proposalDTO.trafficContact" />' />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="multiSelect">Trafficking
									Email</label>
								<div class="controls">
									<input type="email" name="trafficEmail" id="trafficEmail"
										class="span12"
										value='<s:property value="proposalDTO.trafficEmail" />' />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="multiSelect">Trafficking
									Phone</label>
								<div class="controls">
									<input type="text" name="trafficPhone" id="trafficPhone"
										class="span12"
										value='<s:property value="proposalDTO.trafficPhone" />' />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="input01">Campaign
									Status<span class="req star">*</span>
								</label>
								<div class="controls">
									<Select name="proposalStatus" type="text" class="span12"
										id="proposalStatus">
										<!-- <option value="-1"></option> -->
										<s:if
											test="campaignStatusMap !=null && campaignStatusMap.size()>0">
											<s:iterator value="campaignStatusMap">
												<option value='<s:property value="key"/>'>
													<s:property value="value" />
												</option>
											</s:iterator>
										</s:if>
									</Select>
								</div>
							</div>

						</div>
					</fieldset>
					<input type="hidden" name="budget" id="budget"
						value='<s:property value="proposalDTO.budget" />' />
					 <input type="hidden" id="proposalId" name="proposalId"
						value='<s:property value="proposalDTO.proposalId" />' />
					 <input type="hidden" id="nextPageControl" name="nextPageControl" value="0" />
					 <input type="hidden" id="placementData" name="placementData"
						value='<s:property value="proposalDTO.placementData" />' />
					 <input type="hidden" id="fromPool" name="fromPool" value='1' />
				</div>
				<!-- end content-->
			</div>
			<!-- end wrap div -->
		</article>
	</div>
	<!-- end widget -->
</form>