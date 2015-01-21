<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<s:set name="theme" value="'simple'" scope="page" />
<%@page import="com.lin.web.util.TabsName" %>

 <ul id="theme-links-js" class="main-nav" >
	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(publisherViewPageName)}">
		<li id="publisherViewLi" class="main-nav-li">
			<a href="publisher.lin" ><s:property value="publisherViewPageName"/></a>
		</li>
	</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(campaignPerformancePageName)}">
		<li id="campaignPerformanceViewLi" class="main-nav-li">
			<a href="newAdvertiserView.lin" ><s:property value="campaignPerformancePageName"/></a>
		</li>
	</s:if>
	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(advertiserViewPageName)}">
		<li id="advertiserViewLi" class="main-nav-li">
			<a href="campaignPerformanceListing.lin" ><s:property value="advertiserViewPageName"/></a>
		</li>
	</s:if>
	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(operationalViewPageName)}">
		<li id="operationalViewLi" class="main-nav-li">
			<a href="proposals.lin" ><s:property value="operationalViewPageName"/></a>
		</li>
	</s:if>
<%-- 	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(poolPageName)}">
		<li id="thePoolLi" class="main-nav-li">
			<a href="newPoolMap.lin" ><s:property value="poolPageName"/></a>
		</li>
	</s:if> --%>
	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(unifiedCampaign)}">
		<li id="cmapaignPlanLi" class="main-nav-li">
			<a href="smartPlanner.lin" ><s:property value="unifiedCampaign"/></a>
		</li>
	</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(poolPageName)}">
		<li id="thePoolLi" class="main-nav-li">
			<a href="newPoolMap.lin" ><s:property value="poolPageName"/></a>
		</li>
	</s:if>
	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(newsAndResearchPageName)}">
		<li id="newsAndResearchLi" class="main-nav-li">
			<a href="newsAndResearch.lin" ><s:property value="newsAndResearchPageName"/></a>
		</li>
	</s:if>
	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(mapEngineName)}">
		<li id="mapEngineLi" class="main-nav-li">
			<a href="mapEngine.lin" ><s:property value="mapEngineName"/></a>
		</li>
	</s:if>
	<s:if test="%{userDetailsDTO.authorisedPagesList.contains(report)}">
		<li id="reportLi" class="main-nav-li">
			<a href="reporting.lin"><s:property value="report"/></a>
		</li>
	</s:if>
	<s:if test="%{#session.sessionDTO.pageName == mySettingPageName}">
		<li id="mySettingLi" class="main-nav-li_selected">
			<a href="javascript:void(0)" onclick="location.href='initUserOwnProfileUpdate.lin'" ><s:property value="mySettingPageName"/></a>
		</li>
	</s:if>
	<s:if test="%{ (#session.sessionDTO.superAdmin) || (#session.sessionDTO.adminUser)}">
		<li id="adminPageLi" class="main-nav-li-lastChild">
			<a href="javascript:void(0)" onclick="location.href='userSetup.lin'" ><s:property value="adminPageName"/></a>
		</li>
	</s:if>
</ul>



		
		
<div class="responsiveDiv">
       
		<ul  id="accordion-menu-js" class="menu " >
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(publisherViewPageName)}">
				<li id="publisherViewLi" class="main-nav-li">
					<a href="publisher.lin" onclick="location.href='publisher.lin'" ><s:property value="publisherViewPageName"/></a>
				</li>
				<s:if test="%{ (#session.sessionDTO.pageName.contains(publisherViewPageName))}">
				
						<s:if test="%{authorisationKeywordList.contains('publisherInvRevView')}">
								<li>
								  <a href="javascript:$('#inv_rev').click();" >-<%=TabsName.PUBLISHER_VIEW_SUMMARY%></a>
								</li>
						</s:if>
						<s:if test="%{authorisationKeywordList.contains('publisherTreAnsView')}">
				        	     <li>
				        	       <a href="javascript:$('#tre_ana').click();" >-<%=TabsName.PUBLISHER_VIEW_TRENDS%></a>
				        	     </li>
				        </s:if>
				        <s:if test="%{authorisationKeywordList.contains('publisherDiagToolView')}">
				        	     <li>
				        	       <a href="javascript:$('#diagnosticTools').click();"  >-<%=TabsName.PUBLISHER_VIEW_DIAGNOSTIC_TOOLS%></a>
				        	     </li>
				        </s:if>
				        <s:if test="%{authorisationKeywordList.contains('publisherTrafView')}">
				                <li>
				        	       <a href="javascript:$('#trafficking').click();" >-<%=TabsName.PUBLISHER_VIEW_TRAFFICKING%></a>
				               </li>
				        </s:if>
				
				</s:if>
			
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(campaignPerformancePageName)}">
			<li id="campaignPerformanceViewLi" class="main-nav-li">
				<a href="newAdvertiserView.lin" onclick="location.href='newAdvertiserView.lin'" ><s:property value="campaignPerformancePageName"/></a>
			</li>
					<s:if test="%{ (#session.sessionDTO.pageName.contains(advertiserViewPageName))}">
						 <li>
		                    <a href="javascript:$('#per_sum').click();" >-<%=TabsName.ADVERTIER_VIEW_SUMMARY%></a>
		                 </li>
		                 <li>
		    	            <a href="javascript:$('#richMediaSummary').click();" >-<%=TabsName.ADVERTIER_VIEW_RICH_MEDIA%></a>
		                 </li>
					</s:if>
		  
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(advertiserViewPageName)}">
			<li id="advertiserViewLi" class="main-nav-li">
				<a href="campaignPerformanceListing.lin" onclick="location.href='campaignPerformanceListing.lin'" ><s:property value="advertiserViewPageName"/></a>
			</li>
					<%-- <s:if test="%{ (#session.sessionDTO.pageName.contains(advertiserViewPageName))}">
						 <li>
		                    <a href="javascript:$('#per_sum').click();" >-<%=TabsName.ADVERTIER_VIEW_SUMMARY%></a>
		                 </li>
		                 <li>
		    	            <a href="javascript:$('#richMediaSummary').click();" >-<%=TabsName.ADVERTIER_VIEW_RICH_MEDIA%></a>
		                 </li>
					</s:if> --%>
		  
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(operationalViewPageName)}">
			<li id="operationalViewLi" class="main-nav-li">
				<a href="proposals.lin" onclick="location.href='proposals.lin'" ><s:property value="operationalViewPageName"/></a>
			</li>
					<s:if test="%{ (#session.sessionDTO.pageName.contains(operationalViewPageName))}">
					       <s:if test="%{proposalDTO.showTabs.contains('yes')}">
						       <li>
						    	    <a href="javascript:$('#campaignBrief').click();" >-<%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS_BRIEF%></a>
						       </li>
						       <li>
						    	    <a href="javascript:$('#mediaPlan').click();" >-<%=TabsName.CAMPAIGN_VIEW_MEDIA_PLANNER%></a>
						       </li>
						       <li>
						    	    <a href="javascript:$('#insertionOrder').click();" >-<%=TabsName.CAMPAIGN_VIEW_IO%></a>
						       </li>
					       </s:if>
					       <s:else>
					       	<li>
					    	    <a href="javascript:$('#richMediaSummary').click();" >-<%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS%></a>
					       </li>
					       </s:else>
		      		</s:if>
		</s:if>
		
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(unifiedCampaign)}">
			<li id="cmapaignPlanLi" class="main-nav-li">
				<a href="smartPlanner.lin" onclick="location.href='newPoolMap.lin'" ><s:property value="unifiedCampaign"/></a>
			</li>
		</s:if>
		
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(poolPageName)}">
			<li id="thePoolLi" class="main-nav-li">
				<a href="newPoolMap.lin" onclick="location.href='newPoolMap.lin'" ><s:property value="poolPageName"/></a>
			</li>
				<s:if test="%{ (#session.sessionDTO.pageName.contains(poolPageName))}">
					<li>
			    	    <a href="javascript:$('#search_inventory').click();" >-<%=TabsName.THE_POOL_SEARCH_INVENTORY%></a>
			       </li>
			       <li>
			    	    <a href="javascript:$('#create_proposal').click();" >-<%=TabsName.THE_POOL_CREATE_PROPOSAL%></a>
			       </li>
		        </s:if>
		</s:if>

		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(newsAndResearchPageName)}">
			<li id="newsAndResearchLi" class="main-nav-li">
				<a href="newsAndResearch.lin" onclick="location.href='newsAndResearch.lin'" ><s:property value="newsAndResearchPageName"/></a>
			</li>
				<s:if test="%{ (#session.sessionDTO.pageName.contains(newsAndResearchPageName))}">
					<li>
			    	    <a href="javascript:$('#indus_new').click();" >-<%=TabsName.NEWS_AND_RESEARCH_NEWS%></a>
			       </li>
		       </s:if>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(mapEngineName)}">
			<li id="mapEngineLi" class="main-nav-li">
				<a href="mapEngine.lin" onclick="location.href='mapEngine.lin'" ><s:property value="mapEngineName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(report)}">
			<li id="reportLi" class="main-nav-li">
				<a href="reporting.lin" onclick="location.href='reporting.lin'" ><s:property value="report"/></a>
			</li>
		</s:if>
		<s:if test="%{#session.sessionDTO.pageName == mySettingPageName}">
			<li id="mySettingLi" class="main-nav-li_selected">
				<a href="initUserOwnProfileUpdate.lin" onclick="location.href='initUserOwnProfileUpdate.lin'" ><s:property value="mySettingPageName"/></a>
			</li>
		</s:if>
			<s:if test="%{ (#session.sessionDTO.superAdmin) || (#session.sessionDTO.adminUser)}">
					<li id="adminPageLi" class="main-nav-li-lastChild">
						<a href="userSetup.lin" onclick="location.href='userSetup.lin'" ><s:property value="adminPageName"/></a>
				   </li>
				   <s:if test="%{ (#session.sessionDTO.pageName.contains(adminPageName))}">
					<li>
						 <a href="userSetup.lin" >-Users</a>
					</li>
					<li>
						 <a href="roleSetup.lin" >-Roles</a>
					</li>
					<li>
						 <a href="teamSetup.lin" >-Teams</a>
					</li>
					<s:if test="%{#session.sessionDTO.superAdmin}">
					<li>
						 <a href="companySetup.lin" >-Companies</a>
					</li>
					</s:if>
					<s:if test="%{#session.sessionDTO.superAdmin || #session.sessionDTO.publisherPoolPartner}">
						<li><a href="propertySetup.lin" >-Property</a></li>
						<li><a href="publisherProduct.lin" >-Products</a></li>
					</s:if>
					<s:if test="%{#session.sessionDTO.superAdmin || #session.sessionDTO.publisherPoolPartner}">
					<li>
						 <a href="companySettings.lin" >-Company Settings</a>
					</li>
					</s:if>
				    </s:if>
			</s:if>
	</ul>
	
</div>
