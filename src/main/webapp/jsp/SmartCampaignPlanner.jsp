<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
<%-- <jsp:include page="Header.jsp" />  --%>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"
			style="text-align: center">
			<h1>Smart Campaign Planner</h1>
		</div>
	</div>

	<!-- Tab Section start -->
	<div class="row-fluid">
		<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">

			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">Brief</a></li>
					<li><a href="#tabs-2">Details</a></li>
					<li><a href="#tabs-3">Targeting & Goals</a></li>
				</ul>

				<!-- Tab1 Start  -->
				<div id="tabs-1">
					<div class="container-fluid">


						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-body">
										<div class="container-fluid">
											<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Campaign
													Name :</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<input type="text" class="form-control"
														placeholder="Campaign Name" data-ng-model="campaign.name">
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title">Contact Detail</h3>
									</div>
									<div class="panel-body">
										<div id="addresstabs">
											<ul>
												<li><a href="#publisherAddr">Publisher</a></li>
												<li><a href="#advertiserAddr">Advertiser</a></li>
											</ul>
											<div id="publisherAddr">
												<div class="panel panel-default">
													<div class="container-fluid panel-body">
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Name</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Publisher Name" data-ng-model="campaign.publisher.name">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Address</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<textarea class="form-control" rows="3" data-ng-model="campaign.publisher.address"></textarea>
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Email</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Email" data-ng-model="campaign.publisher.email">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Phone</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Phone" data-ng-model="campaign.publisher.phone">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Fax</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Fax" data-ng-model="campaign.publisher.fax">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">State</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<select class="form-control input-sm" data-ng-model="campaign.publisher.state ">
																	<option>&nbsp;</option>
																	<option data-ng-repeat="state in stateList"
																		value="{{state.value}}" >{{state.text}}</option>
																</select>
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Zip
																Code</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Zip Code" data-ng-model="campaign.publisher.zipcode">
															</div>
														</div>
													</div>
												</div>

											</div>
											<div id="advertiserAddr">
												<div class="panel panel-default">
													<div class="container-fluid panel-body">
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Name</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Advertiser Name" data-ng-model="campaign.advertiser.name">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Address</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<textarea class="form-control" rows="3" data-ng-model="campaign.advertiser.address"></textarea>
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Email</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Email" data-ng-model="campaign.advertiser.email">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Phone</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Phone" data-ng-model="campaign.advertiser.phone">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Fax</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Fax" data-ng-model="campaign.advertiser.fax">
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">State</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<select class="form-control input-sm" data-ng-model="campaign.advertiser.state">
																	<option>&nbsp;</option>
																	<option data-ng-repeat="state in stateList"
																		value="{{state.value}}" >{{state.text}}</option>
																</select>
															</div>
														</div>
														<div class="row-fluid">
															<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
														</div>
														<div class="row-fluid">
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Zip
																Code</div>
															<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
																<input type="text" class="form-control"
																	placeholder="Zip Code" data-ng-model="campaign.advertiser.zipcode">
															</div>
														</div>
													</div>
												</div>

											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title">Duration</h3>
									</div>
									<div class="panel-body">
										<div class="container-fluid">
											<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Start
													Date</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">End
													Date</div>
											</div>
										<%-- 	<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<div class="form-group">
														<div class='input-group date' id='campaignStartDate'
															data-date-format="YYYY/MM/DD">
															<input type="text" class="form-control" data-ng-model="campaign.duration.start_date" /> <span
																class="input-group-addon"><span
																class="glyphicon glyphicon-time"></span> </span>
														</div>
													</div>

													<!--  <div id="dateSlider"></div>
													 -->
												</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<div class="form-group">
														<div class='input-group date' id='campaignEndDate'
															data-date-format="YYYY/MM/DD">
															<input type='text' class="form-control" data-ng-model="campaign.duration.end_date" /> <span
																class="input-group-addon"><span
																class="glyphicon glyphicon-time"></span> </span>
														</div>
													</div>
												</div>
											</div> --%>
														<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<div class="form-group">
														<div class='input-group date' id='campaignStartDate'
															data-date-format="YYYY/MM/DD">
															<input type='text' class="form-control" data-ng-model="campaign.duration.start_date"/> <span
																class="input-group-addon"><span
																class="glyphicon glyphicon-time"></span> </span>
														</div>
													</div>

													<!--  <div id="dateSlider"></div>
													 -->
												</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<div class="form-group">
														<div class='input-group date' id='campaignEndDate'
															data-date-format="YYYY/MM/DD">
															<input type='text' class="form-control" data-ng-model="campaign.duration.end_date" /> <span
																class="input-group-addon"><span
																class="glyphicon glyphicon-time"></span> </span>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>


					</div>
				</div>
				<!-- Tab1 End -->

				<!-- Tab2 Start -->
				<div id="tabs-2">
					<div class="container-fluid form-group">


						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-body">
										<div class="container-fluid">
											<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Campaign
													Type</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<select data-ng-model="campaign.rate_type"
														class="form-control input-sm">
														<option>&nbsp;</option>
														<option data-ng-repeat="cType in campaignType"
															value="{{cType.value}}">{{cType.text}}</option>
													</select>
												</div>
											</div>

											<div class="row-fluid">
												<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
											</div>

											<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Rate</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<input type="text" class="form-control" placeholder="Rate" data-ng-model="campaign.rate">
												</div>
											</div>
											<div class="row-fluid">
												<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
											</div>

											<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Budget</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<input type="text" class="form-control"
														placeholder="Budget" data-ng-model="campaign.budget">
												</div>
											</div>
											<div class="row-fluid">
												<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12"></div>
											</div>
											<div class="row-fluid">
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Notes</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
													<textarea class="form-control" rows="3" data-ng-model="campaign.notes"></textarea>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>


						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-body">

										<div class="panel panel-default">
											<div class="panel-heading">Format</div>
											<div class="panel-body">

												<div class="panel panel-default">
													<div class="panel-heading">Standard</div>
													<div class="panel-body">
													<label ng-repeat="creative in standardCreativeList">
                                                         <input type="checkbox" ng-model="creative.selected" checklist-value="creative">{{creative.size}}
                                                    </label>
														<!-- <input type="checkbox" class="checkbox splchk" data-label="320 x 200 " />
														<input type="checkbox" class="checkbox splchk" data-label="320 x 250 " /> -->
													</div>
												</div>


												<div class="panel panel-default">
													<div class="panel-heading">Video</div>
													<div class="panel-body">
													<label ng-repeat="creative in videoCreativeList">
                                                       <input type="checkbox" ng-model="creative.selected" checklist-value="device">{{creative.size}}
                                                     </label>
														<!-- <input type="checkbox" class="checkbox splchk" data-label="320 x 200 " />
														<input type="checkbox" class="checkbox splchk" data-label="320 x 250 " /> -->
													</div>
												</div>
												
												<div class="panel panel-default">
													<div class="panel-heading">Rich Media</div>
													<div class="panel-body">
													<label ng-repeat="creative in richCreativeList">
                                                        <input type="checkbox" ng-model="creative.selected" checklist-value="device">{{creative.size}}
                                                   </label>
														<!-- <input type="checkbox" class="checkbox splchk" data-label="320 x 200 " />
														<input type="checkbox" class="checkbox splchk" data-label="320 x 250 " /> -->
													</div>
												</div>
											</div>
										</div>

										<div class="panel panel-default">
											<div class="panel-heading">Devices</div>
											<div class="panel-body">
											<label ng-repeat="device in deviceList">
                                             <input type="checkbox" ng-model="device.selected" checklist-value="device">{{device.text}}
                                             </label>
												<!-- <input type="checkbox"  class="checkbox splchk" data-label="Smartphone">
												<input type="checkbox"  class="checkbox splchk" data-label="Tablet">
												<input type="checkbox"  class="checkbox splchk" data-label="Desktop">
												<input type="checkbox"  class="checkbox splchk" data-label="Connected TV" disabled>
												<input type="checkbox"  class="checkbox splchk" data-label="Wearable" disabled> -->
											</div>
										</div>


	                                   <div class="row-fluid">
											<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Frequency Cap</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
														<input type="text" class="form-control"
														placeholder="Frequency Cap" data-ng-model="campaign.frequency_cap">
														</div>
														</div>
										<!-- <div class="panel panel-default">
											<div class="panel-heading">Frequency Cap</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
														<input type="text" class="form-control"
														placeholder="Frequency Cap" data-ng-model="campaign.advertiser.zipcode">
														</div>
											<div class="panel-body"></div>
										</div> -->

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Tab2 End -->

				<!-- Tab3 Start -->
				<div id="tabs-3">
					<div class="container-fluid form-group">

						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title">Target</h3>
									</div>
									<div class="panel-body">

										<div class="panel panel-default">
											<div class="panel-heading">OS Platform</div>
											<label ng-repeat="plateform in plateFormList">
                                             <input type="checkbox" ng-model="plateform.selected" checklist-value="plateform">  {{plateform.text}}
                                             </label>
                                            
										     <!-- <div data-ng-repeat="plateform in plateFormList" >
											    <input type="checkbox" >  {{plateform.text}}
												<input type="checkbox" class="checkbox splchk" data-label="iOS" />
												<input type="checkbox" class="checkbox splchk" data-label="Windows" /> 
											</div>
											 -->

										</div>

										<div class="panel panel-default">
											<div class="panel-heading">Demographic</div>
											<div class="panel-body">
											<input type="checkbox" class="checkbox splchk" ng-model="campaign.target.demographic" checklist-model="plateform.text"/>
											</div>
										</div>


										<div class="panel panel-default">
											<div class="panel-heading">Geographic</div>
											<div class="panel-body">
											<input type="checkbox" class="checkbox splchk" ng-model="campaign.target.geographic" checklist-model="plateform.text"/>
											</div>
										</div>

										<div class="panel panel-default">
											<div class="panel-heading">Behavior	</div>
											<div class="panel-body">
											<input type="checkbox" class="checkbox splchk" ng-model="campaign.target.behavior" checklist-model="plateform.text"/>
											</div>
										</div>


										<div class="panel panel-default">
											<div class="panel-heading">Contextual</div>
											<div class="panel-body">
											<input type="checkbox" class="checkbox splchk" ng-model="campaign.target.contextual"  checklist-model="plateform.text"/>
											</div>
										</div>


                                        <div class="panel panel-default">
											<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">Goals</div>
												<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
														<input type="text" class="form-control" id="goalImp_1"
														   placeholder="Goals" >
												</div>
										</div>
										
									</div>
								</div>
							</div>
						</div>
                                           
<!-- 
						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title">Goals</h3>
									</div>
									<div class="panel-body">
										<div class="container-fluid form-group"></div>
									</div>
								</div>
							</div>
						</div> -->

					
						<div class="row-fluid">
							<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title">Flights</h3>
									</div>
									<div class="panel-body">
										<div class="container-fluid well">
											<div class="row-fluid">
												<div class="col-md-8 col-xs-8 col-sm-8 col-lg-8">
													<div id="flightDateInitSlider"></div>
												</div>
												<div class="col-md-2 col-xs-2 col-sm-2 col-lg-2">
													<button type="button" class="btn btn-primary"
														data-ng-click="addFlight()">
														<i class="glyphicon glyphicon-plus"></i> Add Flight
													</button>
												</div>
												
												<div class="col-md-2 col-xs-2 col-sm-2 col-lg-2">
												<button type="button" class="btn btn-primary" data-ng-click="resetCampaignDates()">Refresh</button>
												</div>
												
											</div>
										</div>

										<div class="container-fluid" id="flightContainer">

											<!-- Hidden Flight Row -->
											<div id="dynaFlightRow" style="display: none">
												<div class="row-fluid">
													<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">&nbsp;</div>
												</div>
												<div class="row-fluid">
													<div class="col-md-9 col-xs-9 col-sm-9 col-lg-9 fRow"></div>
													<div class="col-md-2 col-xs-2 col-sm-2 col-lg-2">
														<input type="text" class="form-control gRow" placeholder="Goal">
													</div>
													<div class="col-md-1 col-xs-1 col-sm-1 col-lg-1">
														<button type="button"
															class="btn btn-primary glyphicon glyphicon-trash"></button>
													</div>
												</div>
											</div>
											<!-- Flight Row End -->

											<div class="row-fluid">
												<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">&nbsp;</div>
											</div>
											<div class="row-fluid">
												<div class="col-md-9 col-xs-9 col-sm-9 col-lg-9">
													<div id="flightDateSlider_1" ></div>
												</div>
												<div class="col-md-2 col-xs-2 col-sm-2 col-lg-2">
													<input type="text" class="form-control" placeholder="Goal" data-ng-model="flight.impression">
												</div>
											</div>


										</div>
									</div>
								</div>
							</div>
						</div>



					</div>
				</div>
				<!-- Tab3 End -->
			</div>

		</div>
	</div>
	<!-- Tab section End -->


	<div class="row-fluid">
		<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">&nbsp;</div>
	</div>

	<div class="row-fluid">
		<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
			<button type="button" class="btn btn-lg btn-primary center-block"
				ng-click="saveCampaign()">Smart Planner</button>
		</div>
	</div>

</div>
