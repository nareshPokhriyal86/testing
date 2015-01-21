<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>



<s:if test="%{session.sessionDTO.pageName == advertiserViewPageName}">
<div class="slide-out-div">
                  

                    <div class="indented" style="overflow:hidden; border-radius:4px;" >
                    
                    <div style="width:145px;float:left;margin-bottom: 15px;display:none;">
                        <div style="color:#eec14c;font-size: 12px;font-weight: normal;margin-bottom: 7px;float:left">PUBLISHER</div>
                        <div id="advertiserViewPublishersDropDown" class="btn-group dropdown-list" style="display:inline;float:left;clear:both;margin-left: 4px;">
                            <a id="advertiserViewPubTitle" class="btn btn-inverse btn-small" href="javascript:void(0);" style="font-size:16px;"></a>
                            <a class="btn btn-inverse btn-small dropdown-toggle"
                                data-toggle="dropdown" href="javascript:void(0);"><span
                                class="caret" ></span></a>
                            <ul class="dropdown-menu">
                            	
                            </ul>
                        </div>
                    </div><br>
                     <div style="width:145px;float:left;clear:both;margin-bottom: 12px;display:none;">
                        <div style="color:#eec14c;font-size: 12px;font-weight: normal;margin-bottom: 7px;float:left">PROPERTY</div>
                        <div id="advertiserViewPropertyDropDown" class="btn-group dropdown-list" style="display:inline;float:left;clear:both;margin-left: 4px;">
                            <a id="advertiserViewPropertyTitle" class="btn btn-inverse btn-small" href="javascript:void(0);" style="font-size:16px;"></a>
                            <a class="btn btn-inverse btn-small dropdown-toggle"
                                data-toggle="dropdown" href="javascript:void(0);"><span
                                class="caret" ></span></a>
                            <ul class="dropdown-menu">
                            	
                            </ul>
                        </div>
                    </div><br>
                    
	                     
	                     
	                     <div style="width:350px;clear:both;">
		<div id="agency_dropdown" style="margin-left:5px;margin-right:5px;margin-bottom:10px;">
		
		<span class="select" style="float:left;color: #eec14c;font-weight:normal;font-size:12px;margin-left: -4px;">AGENCY    </span>
			<input type='hidden'  data-init-text='Bla bla' name='input' id='test' style="width:300px;margin-left:-1px;"/>
			<div id="agency_clear" style="color:white;float:right;margin-top:8px;cursor: hand; cursor: pointer;display:none;">Clear</div>
			</div>
									<div id="advertiser_dropdown" style=" margin:5px;">	
									<span class="select" style="color: #eec14c;font-weight:normal;font-size:12px;margin-left: -4px;">ADVERTISER</span>
									<input type='hidden'  data-init-text='Bla bla' name='input' id='test2' style="width:300px;margin-left:-1px;"/>
									<div id="advertiser_clear" style="color:white;float:right;margin-top:8px;cursor: hand; cursor: pointer;display:none;">Clear</div>
									</div>
									
									<div class='agency_second_filter' style='display:none;clear:both;float:left;margin-left:-2px;box-shadow:none;border:none;margin-bottom: 15px;'> 
									   <span class="select" style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>AGENCY  </span>
									   <div id="agencyId_second_filter" style='float:left;color:white;margin-left:10px;clear:both;'> 
									      <span  >
									      All Agency
											</span>
 										</div>
 									</div>
 									
									<div class='adjusted advertiser_second_filter' style='display:none;margin-bottom: 10px;clear:both;float:left;margin-left:-2px;box-shadow:none;border:none;'> 
									   <span class="select" style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>ADVERTISER  </span>
									   <div id="advertiserId_second_filter" style='float:left;margin-left:10px;clear:both;color:white;'>
									       <span>
									       All Advertiser
									       </span> 
									    </div>
									 </div>
									
									<div id="order_dropdown_text" style="display:none;width: 360px;float:left; margin-left:4px;clear:both;margin-bottom:10px;min-height:80px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">ORDER  </span>
									<div style="border:1px dotted white;min-height:56px;">
											<div id="order_dropdown_name" style="width:290px;float:left;color:white;margin:5px;"></div>
											<div id="order_clear_text" style="color:white;float:left;width:57px;margin-top:0px;cursor: hand;min-height:55px; cursor: pointer;background:#303030;"><div style="margin: 12px;margin-top: 18px;">Clear</div></div>
									</div>
									</div>
									
									
									<div id="order_dropdown" style="display:none; margin:5px;clear:both;clear:both;float:left;min-height:80px;margin-top:-2px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;margin-right:40px;">ORDER       </span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='test3' style="width:300px;min-height:55px;margin-left: 3px;"/>
									<div id="order_clear" style="color:white;float:right;margin-top:8px;cursor: hand;display:none; cursor: pointer;">Clear</div>
									</div>
									
									
									<div id="line_dropdown_text" style="display:none;width: 360px;float:left; margin-left:4px;clear:both;min-height:80px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">LINE ITEMS  </span>
									<div style="border:1px dotted white;min-height:56px;">
											<div id="line_dropdown_name" style="width:290px;float:left;color:white;margin:5px;"></div>
											<div id="line_clear_text" style="color:white;float:left;width:57px;margin-top:0px;cursor: hand;min-height:55px; cursor: pointer;background:#303030;"><div style="margin: 12px;margin-top: 18px;">Clear</div></div>
									</div>
									</div>
									
									
									<div id="lineItems_dropdown" style="display:none; margin:5px;clear:both;float:left;min-height:80px;margin-top: -10px;">
									<span id="line_itemName" class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">LINE ITEMS</span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='test4' style="position:relative;width:300px;margin-left: 3px;"/>
									</div>
									
									
									<div id="lineItems_dropdown_single" style="display:none; margin:5px;clear:both;float:left;margin-top: -17px;">
									<span id="line_itemName_single" class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">LINE ITEM</span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='test4_single' style="position:relative;width:300px;"/>
									</div>
									<div id="reallocation_dropdown" style="display:none; margin:5px;clear:both;float:left;margin-top: -17px;">
									<span id="reallocation_itemName" class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">ORDER</span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='reallocationFilter' style="position:relative;width:300px;"/>
									</div>
			</div>
	                     
	                                             
	                    
                                                                     
	                    
	                    <div class="fluid-container">
                        
                        <!-- widget grid -->
                        <section id="widget-grid" class="">

                                                <form class="form-horizontal themed" style="margin-top:15px;">
                                                    <fieldset style="margin-top:15px;">
                                                   
                                                    <div style="width:100%; height:40px; margin-top:15px;">    
                                                        
                                                    
                                                            
                                                            <a href="javascript:void(0);" onclick="advertiser_clear_apply()" class="btn medium btn-primary" id="enable-select-demo" style="float:right; margin-top:5px; margin-right:5px;">
                                                                Cancel
                                                            </a>
                                                            
                                                            <a href="#" onclick="advertiser_filter_apply();" class="btn medium btn-warning" id="disable-select-demo " style="float:right;  margin-top:5px;  margin-right:5px;">
                                                                Apply
                                                            </a>
                                                        
</div>
                                                        
                                                    </fieldset>
                                                </form>
                
                            
                        </section>
                       <!--  end widget grid -->
                    </div>
                    
                    
                    </div>
                     <a class="handle" href="javascript:slide();"></a>
               </div>


</s:if>
<s:elseif test="%{session.sessionDTO.pageName == publisherViewPageName}">


<div class="slide-out-div" style="margin-top:0px;" >
                     

                    <div class="indented" style="overflow:hidden;  border-radius:4px;" >
                     <div id="publisher_outer" style="width:145px;float:left;min-height:135px;">
                        <div style="color:#eec14c;font-size: 12px;font-weight: normal;margin-bottom: 7px;float:left">PUBLISHER</div>
                        <div id="channelsDropDown" class="btn-group dropdown-list" style="display:inline;float:left;clear:both;">
                            <a id="pub_title" class="btn btn-inverse btn-small" href="javascript:void(0);" style="font-size:16px;"></a>
                            <a class="btn btn-inverse btn-small dropdown-toggle"
                                data-toggle="dropdown" href="javascript:void(0);"><span
                                class="caret" ></span></a>
                            <ul class="dropdown-menu">
                                <!-- <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Lin TV</a></li>
                                <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Belo</a></li>
                                <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Conde Nast</a></li>
                                <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Meredith</a></li> -->
                            
                            </ul>
                        </div>
                    </div></br>
                     <div id="propertyDropDownPublisher" style="width:145px;float:left;clear:both;margin-bottom: 12px;display:none;">
                        <div style="color:#eec14c;font-size: 12px;font-weight: normal;margin-bottom: 7px;float:left">PROPERTY</div>
                        <div id="publisherViewPropertyDropDown" class="btn-group dropdown-list" style="display:inline;float:left;clear:both;margin-left: 4px;">
                            <a id="publisherViewPropertyTitle" class="btn btn-inverse btn-small" href="javascript:void(0);" style="font-size:16px;"></a>
                            <a class="btn btn-inverse btn-small dropdown-toggle"
                                data-toggle="dropdown" href="javascript:void(0);"><span
                                class="caret" ></span></a>
                            <ul class="dropdown-menu">
                            	
                            </ul>
                        </div>
                    </div><br>
<div>
                    
                    
                    <div class="fluid-container">
                      
                        <section id="widget-grid" class="">
                            
                      
                            
                            </br>
                            <form class="form-horizontal themed" id = "filter_Channel" style="margin-top:15px;">
                                                    <fieldset style="margin-top:-85px;">
                                                                                                                
                                                                
                                                                
                                                                <span style="font-size:12px; line-height:40px; color:#eec14c; text-transform:uppercase;">Channels:</span>
                                                                
                                                                <a class="btn btn-info btn-mini" href="javascript:selectAllFilterCheckbox();">All</a>
                                                                <a class="btn btn-info btn-mini" href="javascript:unselectAllFilterCheckbox();">None</a>
                                                                
                                                                <div id="channelsDiv">
                                                                </div>
                                                               
                                                               
                                                                
                                                        
                                                        
                                                    </fieldset>
                                                </form>
                         
                            <form class="form-horizontal themed" id="select-demo-js">
                                                    <fieldset style="position:relative;width:205px!important;">
                                                    </fieldset>
                                                </form>
                                                
                                                 
                                                        <!--add new filters   -->
                                                        
								<div id="agency_dropdown_advertiser" style="margin-left:5px;margin-right:5px;margin-bottom:10px;display:none;">
								<span class="select" style="float:left;clear:both;width:100px;color: #eec14c;font-weight:normal;font-size:12px;margin-left: -4px;">AGENCY    </span>
									<input type='hidden'  data-init-text='Bla bla' name='input' id='agencyFilter_Advertiser' style="width:300px;margin-left:-1px;"/>
									<div id="agency_clear_advertiser" style="color:white;float:right;margin-top:8px;cursor: hand; cursor: pointer;display:none;">Clear</div>
									</div>
									
									
									<div id="advertiser_dropdown_advertiser" style=" margin:5px;display:none;">	
									<span class="select" style="color: #eec14c;font-weight:normal;clear:both;width:150px;font-size:12px;margin-left: -4px;margin-right:10px;">ADVERTISER</span>
									<input type='hidden'  data-init-text='Bla bla' name='input' id='advertiserFilter_Advertiser' style="width:302px;margin-left:-1px;"/>
									<div id="advertiser_clear_Advertiser" style="color:white;float:right;margin-top:8px;cursor: hand; cursor: pointer;display:none;">Clear</div>
									</div>
									
									<div class='agency_second_filter_publisher' style='display:none;clear:both;float:left;margin-left:-2px;box-shadow:none;border:none;margin-bottom: 15px;'> 
									   <span class="select" style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>AGENCY  </span>
									   <div id="agencyId_second_filter_publisher" style='float:left;color:white;margin-left:10px;clear:both;'> 
									      <span  >
									      All Agency
											</span>
 										</div>
 									</div>
 									
									<div class='adjusted advertiser_second_filter_publisher' style='display:none;margin-bottom: 10px;clear:both;float:left;margin-left:-2px;box-shadow:none;border:none;'> 
									   <span class="select" style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>ADVERTISER  </span>
									   <div id="advertiserId_second_filter_publisher" style='float:left;margin-left:10px;clear:both;color:white;'>
									       <span>
									       All Advertiser
									       </span> 
									    </div>
									 </div>
									 
									<div id="order_dropdown_text_publisher" style="display:none;width: 360px;float:left; margin-left:4px;clear:both;margin-bottom:10px;min-height:80px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">ORDER  </span>
									<div style="border:1px dotted white;min-height:56px;">
											<div id="order_dropdown_name_publisher" style="width:288px;float:left;color:white;margin:5px;word-wrap:break-word;"></div>
											<div id="order_clear_text_publisher" style="color:white;float:left;width:57px;margin-top:0px;cursor: hand;min-height:55px; cursor: pointer;background:#303030;"><div style="margin: 12px;margin-top: 18px;">Clear</div></div>
									</div>
									</div>
									
									
									<div id="order_dropdown_publisher" style="display:none; margin:5px;clear:both;clear:both;float:left;min-height:80px;margin-top:-2px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;margin-right:40px;">ORDER       </span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='orderFilterPublisher' style="width:300px;min-height:55px;margin-left: 3px;"/>
									<div id="order_clear_publisher" style="color:white;float:right;margin-top:8px;cursor: hand;display:none; cursor: pointer;">Clear</div>
									</div>
									
									
									<div id="line_dropdown_text_publisher" style="display:none;width: 360px;float:left; margin-left:4px;clear:both;min-height:80px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">LINE ITEMS  </span>
									<div style="border:1px dotted white;min-height:56px;">
											<div id="line_dropdown_name_publisher" style="width:288px;float:left;color:white;margin:5px;word-wrap:break-word;"></div>
											<div id="line_clear_text_publisher" style="color:white;float:left;width:57px;margin-top:0px;cursor: hand;min-height:55px; cursor: pointer;background:#303030;"><div style="margin: 12px;margin-top: 18px;">Clear</div></div>
									</div>
									</div>
									
									
									<div id="lineItems_dropdown_publisher" style="display:none; width:350px;margin:5px;clear:both;float:left;min-height:80px;margin-top: -10px;">
									<span id="line_itemName_publisher" class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;margin-right:20px;">LINE ITEMS</span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='lineItemFilterPublisher' style="position:relative;width:300px;margin-left: 3px;"/>
									</div>
									
									<div id="lineItems_dropdown_single_publisher" style="display:none; margin:5px;clear:both;float:left;margin-top: -17px;">
									<span id="line_itemName_single_publisher" class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;clear:both;width:150px;margin-right:18px;">LINE ITEM</span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='lineItemFilterSinglePublisher' style="position:relative;width:300px;margin-left:12px;;"/>
									</div>
									
                                                <form class="form-horizontal themed" style="margin-top:15px;">
                                                    <fieldset style="margin-top:15px;">
                                                    <span style="font-size:12px; line-height:40px; color:#eec14c; text-transform:uppercase;">Property Type:</span>
                                                         <label class="checkbox" style="color:#fff; margin-left:5px;">
                                                             
                                                                    <div class="checker" id=""><span class="outer-optionsCheckbox-11" ><input type="checkbox" id="optionsCheckbox-11" value="option6" checked="checked" name="checkProperty" disabled></span></div>
                                                                    Mobile App 
                                                                </label>
                                                                
                                                                <label class="checkbox" style="color:#fff; margin-left:5px;">
                                                                    <div class="checker" id="uniform-optionsCheckbox-10"><span class="outer-optionsCheckbox-10"><input type="checkbox" id="optionsCheckbox-10" value="option6" checked="checked" name="checkProperty" disabled></span></div>
                                                                    Mobile Web 
                                                                </label>                                                        
                                                       
						
                                                    <div style="width:100%; height:40px; margin-top:15px;">    
                                                        
                                                    
                                                            
                                                            <a href="javascript:void(0);" onclick="clear_apply()" class="btn medium btn-primary" id="enable-select-demo" style="float:right; margin-top:5px; margin-right:5px;">
                                                                Cancel
                                                            </a>
                                                            
                                                            <a href="javascript:void(0);" onclick="filter_apply();" class="btn medium btn-warning" id="disable-select-demo " style="float:right;  margin-top:5px;  margin-right:5px;">
                                                                Apply
                                                            </a>
                                                        
</div>
                                                        
                                                    </fieldset>
                                                </form>
                
                            
                        </section>
                    
                    </div>
                    
                    
                    </div>
                    </div>
                    
                      <a class="handle" href="javascript:slide();"></a>
                    
                </div>

</s:elseif>

<s:elseif test="%{(session.sessionDTO.pageName == operationalViewPageName)}">


<div class="slide-out-div">
                           
                    <div class="indented" style="overflow:hidden; border-radius:4px;" >
                     <div id="publisher_outer" style="width:145px;float:left;min-height:135px;">
                        <div style="color:#eec14c;font-size: 12px;font-weight: normal;margin-bottom: 7px;float:left">PUBLISHER</div>
                        <div id="channelsDropDown" class="btn-group dropdown-list" style="display:inline;float:left;clear:both;">
                            <a id="pub_title" class="btn btn-inverse btn-small" href="javascript:void(0);" style="font-size:16px;"></a>
                            <a class="btn btn-inverse btn-small dropdown-toggle"
                                data-toggle="dropdown" href="javascript:void(0);"><span
                                class="caret" ></span></a>
                            <ul class="dropdown-menu">
                                <!-- <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Lin TV</a></li>
                                <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Belo</a></li>
                                <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Conde Nast</a></li>
                                <li><a href="javascript:void(0);" onclick="changeDropdown(this)" style="color:black;">Meredith</a></li> -->
                            
                            </ul>
                        </div>
                    </div></br>
                     <div id="propertyDropDownPublisher" style="width:145px;float:left;clear:both;margin-bottom: 12px;display:none;">
                        <div style="color:#eec14c;font-size: 12px;font-weight: normal;margin-bottom: 7px;float:left">PROPERTY</div>
                        <div id="publisherViewPropertyDropDown" class="btn-group dropdown-list" style="display:inline;float:left;clear:both;margin-left: 4px;">
                            <a id="publisherViewPropertyTitle" class="btn btn-inverse btn-small" href="javascript:void(0);" style="font-size:16px;"></a>
                            <a class="btn btn-inverse btn-small dropdown-toggle"
                                data-toggle="dropdown" href="javascript:void(0);"><span
                                class="caret" ></span></a>
                            <ul class="dropdown-menu">
                            	
                            </ul>
                        </div>
                    </div><br>
<div>
                    
                    
                    <div class="fluid-container">
                      
                        <section id="widget-grid" class="">
                            
                      
                            
                            </br>
                            <form class="form-horizontal themed" id = "filter_Channel" style="margin-top:15px;">
                                                    <fieldset style="margin-top:-85px;">
                                                                                                                
                                                                
                                                                
                                                                <span style="font-size:12px; line-height:40px; color:#eec14c; text-transform:uppercase;">Channels:</span>
                                                                
                                                                <a class="btn btn-info btn-mini" href="javascript:selectAllFilterCheckbox();">All</a>
                                                                <a class="btn btn-info btn-mini" href="javascript:unselectAllFilterCheckbox();">None</a>
                                                                
                                                                <div id="channelsDiv">
                                                                </div>
                                                               
                                                               
                                                                
                                                        
                                                        
                                                    </fieldset>
                                                </form>
                         
                            <form class="form-horizontal themed" id="select-demo-js">
                                                    <fieldset style="position:relative;width:205px!important;">
                                                    </fieldset>
                                                </form>
                                                
                                                 
                                                        <!--add new filters   -->
                                                        
								<div id="agency_dropdown_advertiser" style="margin-left:5px;margin-right:5px;margin-bottom:10px;display:none;">
								<span class="select" style="float:left;clear:both;width:100px;color: #eec14c;font-weight:normal;font-size:12px;margin-left: -4px;">AGENCY    </span>
									<input type='hidden'  data-init-text='Bla bla' name='input' id='agencyFilter_Advertiser' style="width:300px;margin-left:-1px;"/>
									<div id="agency_clear_advertiser" style="color:white;float:right;margin-top:8px;cursor: hand; cursor: pointer;display:none;">Clear</div>
									</div>
									
									
									<div id="advertiser_dropdown_advertiser" style=" margin:5px;display:none;">	
									<span class="select" style="color: #eec14c;font-weight:normal;clear:both;width:150px;font-size:12px;margin-left: -4px;margin-right:10px;">ADVERTISER</span>
									<input type='hidden'  data-init-text='Bla bla' name='input' id='advertiserFilter_Advertiser' style="width:302px;margin-left:-1px;"/>
									<div id="advertiser_clear_Advertiser" style="color:white;float:right;margin-top:8px;cursor: hand; cursor: pointer;display:none;">Clear</div>
									</div>
									
									<div class='agency_second_filter_publisher' style='display:none;clear:both;float:left;margin-left:-2px;box-shadow:none;border:none;margin-bottom: 15px;'> 
									   <span class="select" style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>AGENCY  </span>
									   <div id="agencyId_second_filter_publisher" style='float:left;color:white;margin-left:10px;clear:both;'> 
									      <span  >
									      All Agency
											</span>
 										</div>
 									</div>
 									
									<div class='adjusted advertiser_second_filter_publisher' style='display:none;margin-bottom: 10px;clear:both;float:left;margin-left:-2px;box-shadow:none;border:none;'> 
									   <span class="select" style='float:left;margin-left:4px;color:#eec14c;font-size: 12px;font-weight: normal;margin-top: -6px;'>ADVERTISER  </span>
									   <div id="advertiserId_second_filter_publisher" style='float:left;margin-left:10px;clear:both;color:white;'>
									       <span>
									       All Advertiser
									       </span> 
									    </div>
									 </div>
									 
									<div id="order_dropdown_text_publisher" style="display:none;width: 360px;float:left; margin-left:4px;clear:both;margin-bottom:10px;min-height:80px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">ORDER  </span>
									<div style="border:1px dotted white;min-height:56px;">
											<div id="order_dropdown_name_publisher" style="width:288px;float:left;color:white;margin:5px;word-wrap:break-word;"></div>
											<div id="order_clear_text_publisher" style="color:white;float:left;width:57px;margin-top:0px;cursor: hand;min-height:55px; cursor: pointer;background:#303030;"><div style="margin: 12px;margin-top: 18px;">Clear</div></div>
									</div>
									</div>
									
									
									<div id="order_dropdown_publisher" style="display:none; margin:5px;clear:both;clear:both;float:left;min-height:80px;margin-top:-2px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;margin-right:40px;">ORDER       </span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='orderFilterPublisher' style="width:300px;min-height:55px;margin-left: 3px;"/>
									<div id="order_clear_publisher" style="color:white;float:right;margin-top:8px;cursor: hand;display:none; cursor: pointer;">Clear</div>
									</div>
									
									
									<div id="line_dropdown_text_publisher" style="display:none;width: 360px;float:left; margin-left:4px;clear:both;min-height:80px;">
									<span class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;">LINE ITEMS  </span>
									<div style="border:1px dotted white;min-height:56px;">
											<div id="line_dropdown_name_publisher" style="width:288px;float:left;color:white;margin:5px;word-wrap:break-word;"></div>
											<div id="line_clear_text_publisher" style="color:white;float:left;width:57px;margin-top:0px;cursor: hand;min-height:55px; cursor: pointer;background:#303030;"><div style="margin: 12px;margin-top: 18px;">Clear</div></div>
									</div>
									</div>
									
									
									<div id="lineItems_dropdown_publisher" style="display:none; width:350px;margin:5px;clear:both;float:left;min-height:80px;margin-top: -10px;">
									<span id="line_itemName_publisher" class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;margin-right:20px;">LINE ITEMS</span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='lineItemFilterPublisher' style="position:relative;width:300px;margin-left: 3px;"/>
									</div>
									
									<div id="lineItems_dropdown_single_publisher" style="display:none; margin:5px;clear:both;float:left;margin-top: -17px;">
									<span id="line_itemName_single_publisher" class="select" style="color:#eec14c;font-size: 12px;font-weight: normal;margin-left: -4px;clear:both;width:150px;margin-right:18px;">LINE ITEM</span>
											<input type='hidden'  data-init-text='Bla bla' name='input' id='lineItemFilterSinglePublisher' style="position:relative;width:300px;margin-left:12px;;"/>
									</div>
									
                                                <form class="form-horizontal themed" style="margin-top:15px;">
                                                    <fieldset style="margin-top:15px;">
                                                    <span style="font-size:12px; line-height:40px; color:#eec14c; text-transform:uppercase;">Property Type:</span>
                                                         <label class="checkbox" style="color:#fff; margin-left:5px;">
                                                             
                                                                    <div class="checker" id=""><span class="outer-optionsCheckbox-11" ><input type="checkbox" id="optionsCheckbox-11" value="option6" checked="checked" name="checkProperty" disabled></span></div>
                                                                    Mobile App 
                                                                </label>
                                                                
                                                                <label class="checkbox" style="color:#fff; margin-left:5px;">
                                                                    <div class="checker" id="uniform-optionsCheckbox-10"><span class="outer-optionsCheckbox-10"><input type="checkbox" id="optionsCheckbox-10" value="option6" checked="checked" name="checkProperty" disabled></span></div>
                                                                    Mobile Web 
                                                                </label>                                                        
                                                       
						
                                                    <div style="width:100%; height:40px; margin-top:15px;">    
                                                        
                                                    
                                                            
                                                            <a href="javascript:void(0);" onclick="clear_apply()" class="btn medium btn-primary" id="enable-select-demo" style="float:right; margin-top:5px; margin-right:5px;">
                                                                Cancel
                                                            </a>
                                                            
                                                            <a href="javascript:void(0);" onclick="operation_filter_apply();" class="btn medium btn-warning" id="disable-select-demo " style="float:right;  margin-top:5px;  margin-right:5px;">
                                                                Apply
                                                            </a>
                                                        
</div>
                                                        
                                                    </fieldset>
                                                </form>
                
                            
                        </section>
                    
                    </div>
                    
                    
                    </div>
                    </div>
                   
            <a class="handle" href="javascript:slide();"></a> 
                    
                    
                </div>

</s:elseif>

<script>
    $(document).ready(function() {

$('#test').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaAgencyDropDownList.lin",
            cache : false,
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	selectedPublisherId : selectedPublisherId,
                    types: "agencies",
                    limit: -1,
                    term: term
                };
            },
            results: function(data, page ) {
            	//alert(data.agencies);
                return { results:data.agencies }
            }
        },
        formatResult: function(agencies) { 
        	//alert(exercise);
            return "<div class='select2-user-result' onclick='fun1(this)' >" + agencies.agencyName + "</div>"; 
        },
        formatSelection: function(agencies) { 
            return agencies; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    
$('#agencyFilter_Advertiser').select2({
    minimumInputLength: 1,
    placeholder: 'Search',
    ajax: {
        url: "/getRichMediaAgencyDropDownList.lin",
        cache : false,
        dataType: 'json',
        quietMillis: 100,
        data: function(term, page) {
            return {
            	selectedPublisherId : selectedPublisherId,
                types: "agencies",
                limit: -1,
                term: term
            };
        },
        results: function(data, page ) {
        	//alert(data.agencies);
            return { results:data.agencies }
        }
    },
    formatResult: function(agencies) { 
    	
        return "<div class='select2-user-result' onClick='agencyFilter(this)'>" + agencies.agencyName + "</div>"; 
    },
    formatSelection: function(agencies) { 
        return agencies; 
    },
    initSelection : function (element, callback) {
        var elementText = $(element).attr('data-init-text');
        callback({"term":elementText});
    }
});
	 	
    $('#test2').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaAdvertiserDropDownList.lin",
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	selectedPublisherId : selectedPublisherId,
                    types: "advertisers",
                    limit: -1,
                    term: term,
                    agencyId: agencyname
                };
            },
            results: function(data, page ) {
            	//alert(data.advertisers);
                return { results: data.advertisers }
            }
        },
        formatResult: function(advertisers) { 
            return "<div class='select2-user-result' onclick='fun2(this)'>" + advertisers.advertiserName + "</div>"; 
        },
        formatSelection: function(advertisers) { 
            return advertisers; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    
    $('#advertiserFilter_Advertiser').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaAdvertiserDropDownList.lin",
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	selectedPublisherId : selectedPublisherId,
                    types: "advertisers",
                    limit: -1,
                    term: term,
                    agencyId: agencyname
                };
            },
            results: function(data, page ) {
            	//alert(data.advertisers);
                return { results: data.advertisers }
            }
        },
        formatResult: function(advertisers) { 
            return "<div class='select2-user-result' onclick='advertiserFilter(this)'>" + advertisers.advertiserName + "</div>"; 
        },
        formatSelection: function(advertisers) { 
            return advertisers; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });

    $('#test3').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaOrderDropDownList.lin",
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	publisherName : selectedPublisher,
                    types: "orders",
                    limit: -1,
                    term: term,
                    agencyId: agencyname,
                    advertiserId: advertisername
                };
            },
            results: function(data, page ) {
                return { results: data.orders }
            }
        },
        formatResult: function(orders) { 
            return "<div class='select2-user-result' onclick='fun3(this)'>" + orders.orderName + "</div>"; 
        },
        formatSelection: function(orders) { 
            return orders; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    $('#orderFilterPublisher').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaOrderDropDownList.lin",
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	publisherName : selectedPublisher,
                    types: "orders",
                    limit: -1,
                    term: term,
                    agencyId: agencyname,
                    advertiserId: advertisername
                };
            },
            results: function(data, page ) {
                return { results: data.orders }
            }
        },
        formatResult: function(orders) { 
            return "<div class='select2-user-result' onclick='orderFilterPublisher(this)'>" + orders.orderName + "</div>"; 
        },
        formatSelection: function(orders) { 
            return orders; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    $('#reallocationFilter').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaOrderDropDownList.lin",
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	publisherName : selectedPublisher,
                    types: "orders",
                    limit: -1,
                    term: term,
                    agencyId: agencyname,
                    advertiserId: advertisername
                };
            },
            results: function(data, page ) {
                return { results: data.orders }
            }
        },
        formatResult: function(orders) { 
            return "<div id = '"+orders.orderId+"' class='select2-user-result' onclick='fun_reallocation(this)'>" + orders.orderName + "</div>"; 
        },
        formatSelection: function(orders) { 
            return orders; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    $('#test4').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaLineItemDropDownList.lin",
            dataType: 'json',
            multiple:'true',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	publisherName : 'Lin Media',
                    types: "lineitems",
                    limit: -1,
                    term: term,
                    orderId: '1'
                };
            },
            results: function(data, page ) {
                return { results: data.lineitems }
            }
        },
        formatResult: function(lineitems) { 
            return "<div class='select2-user-result' onclick='fun4(this)'>" + lineitems.name + "</div>"; 
        },
        formatSelection: function(lineitems) { 
            return lineitems; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    
    $('#lineItemFilterPublisher').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaLineItemDropDownList.lin",
            dataType: 'json',
            multiple:'true',
            quietMillis: 100,
            data: function(term, page) {
                return {
                	publisherName : selectedPublisher,
                    types: "lineitems",
                    limit: -1,
                    term: term,
                    orderId: ordername
                };
            },
            results: function(data, page ) {
                return { results: data.lineitems }
            }
        },
        formatResult: function(lineitems) { 
            return "<div class='select2-user-result' onclick='lineItemFilterPublisher(this)'>" + lineitems.name + "</div>"; 
        },
        formatSelection: function(lineitems) { 
            return lineitems; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    // for single select line item
    $('#test4_single').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaLineItemDropDownList.lin",
            dataType: 'json',
           
            quietMillis: 100,
            data: function(term, page) {
                return {
                	publisherName : selectedPublisher,
                    types: "lineitems",
                    limit: -1,
                    term: term,
                    orderId: ordername
                };
            },
            results: function(data, page ) {
                return { results: data.lineitems }
            }
        },
        formatResult: function(lineitems) { 
        	
        	
        	
            return "<div class='select2-user-result' onclick='getLineOrder(this)' ><span>" + lineitems.name + "</span><div style='display:none;'>"+ lineitems.orderName +"</div></div>"; 
        },
        formatSelection: function(lineitems) { 
            return lineitems; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    
    $('#lineItemFilterSinglePublisher').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/getRichMediaLineItemDropDownList.lin",
            dataType: 'json',
           
            quietMillis: 100,
            data: function(term, page) {
                return {
                	publisherName : selectedPublisher,
                    types: "lineitems",
                    limit: -1,
                    term: term,
                    orderId: ordername
                };
            },
            results: function(data, page ) {
                return { results: data.lineitems }
            }
        },
        formatResult: function(lineitems) { 
        	
        	
        	
            return "<div class='select2-user-result' onclick='lineItemFilterSinglePublisher(this)' ><span>" + lineitems.name + "</span><div style='display:none;'>"+ lineitems.orderName +"</div></div>"; 
        },
        formatSelection: function(lineitems) { 
            return lineitems; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        }
    });
    
  
    
   
});
    
var agencyname;
var advertisername;
var ordername;
var lineitem;
var lineitemSingle;
var orderIdReallocationFilter;

function fun1(obj){
	agencyname = $(obj).text();

	$("#s2id_test").children().html("<a href='#' onclick='return false;'style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_test").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	 $("#agency_clear").css({'display':'inline'});

}

function agencyFilter(obj){
	agencyname = $(obj).text();
	$("#s2id_agencyFilter_Advertiser").children().html("<a href='#' onclick='return false;'style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_agencyFilter_Advertiser").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	$("#agency_clear_advertiser").css({'display':'inline'});

}

function fun2(obj){
	advertisername = $(obj).text();

	$("#s2id_test2").children().html("<a href='#' onclick='return false;'style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_test2").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	$("#advertiser_clear").css({'display':'inline'});

}
function advertiserFilter(obj){
	advertisername = $(obj).text();

	$("#s2id_advertiserFilter_Advertiser").children().html("<a href='#' onclick='return false;'style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_advertiserFilter_Advertiser").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	$("#advertiser_clear_Advertiser").css({'display':'inline'});

}

function fun3(obj){
	ordername = $(obj).text();
	//alert(isTrendDefault);

	$("#s2id_test3").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_test3").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	//alert($("#s2id_test").text());
	
	$("#order_clear").css({'display':'inline'});
	if(ordername != null){
		 $("#lineItems_dropdown_single").css({'display':'none'});
		 $("#lineItems_dropdown").css({'display':'inline'});
	}
}

function orderFilterPublisher(obj){
	ordername = $(obj).text();
	//alert(isTrendDefault);

	$("#s2id_orderFilterPublisher").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_orderFilterPublisher").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	//alert($("#s2id_test").text());
	
	$("#order_clear_publisher").css({'display':'inline'});
	if(ordername != null){
		 $("#lineItems_dropdown_single_publisher").css({'display':'none'});
		 $("#lineItems_dropdown_publisher").css({'display':'inline'});
	}
}
function fun_reallocation(obj){
	ordername = $(obj).text();
	orderIdReallocationFilter = $(obj).attr("id");
	$("#s2id_reallocationFilter").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_reallocationFilter").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	//alert($("#s2id_test").text());
	
	$("#order_clear").css({'display':'inline'});
	if(ordername != null){
		 $("#lineItems_dropdown_single").css({'display':'none'});
		 $("#lineItems_dropdown").css({'display':'inline'});
	}
}


var lineItemArr='';
aArr =[];

startaflag = 0;
acount=1;
idLineItem=[];



function fun4(obj){
	lineitem = $(obj).text();


	////////////////////////////////////////////
	 if ($(obj).children("span").text()!= ""){

		 lineitem = $(obj).children("span").text();
		
		 }
	if($(obj).children("span").text()== ""){

		 lineitem = $(obj).text();
	

		 }

/////////////////////////////////////////////////////////////

if ( lineItemArr.indexOf(lineitem) == -1 ){
	//alert("startaflag...."+startaflag+".."+lineItemArr);
		if (startaflag == 1){
			//alert("startaflag,,,,,,,"+startaflag+".."+lineItemArr);
			lineItemArr = lineItemArr+','
		}
		aArr.push(lineitem);
		lineItemArr = lineItemArr + lineitem;
		startaflag =1;
		//alert("startaflag++++++"+startaflag+".."+lineItemArr);
		
	
		idLineItem.push("lineItem_DYNAMIC_"+acount);
		$("#s2id_test4").after("<div id='lineItem_DYNAMIC_"+acount+"' class='widget alert alert-info-header adjusted lineItem multipleLineItems' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;margin-bottom:10px;word-wrap:break-word;'>	<button data-dismiss='' class='close3' onclick=lineItem_close('"+acount+"'); style='margin-left:5px;margin-top:-7px;margin-right:-3px!important;background:none;border:none;'>×</button>	<div style='float:left;margin-left:-21px;color:white;width:300px;word-wrap:break-word;' id='label_"+acount+"'>	<strong>"+lineitem+"</strong> </div>	</div>");
		
		acount++;

		
	}
	

}

function lineItemFilterPublisher(obj){
	lineitem = $(obj).text();
	 //alert("lineitem...."+lineitem);

	////////////////////////////////////////////
	 if ($(obj).children("span").text()!= ""){

		 lineitem = $(obj).children("span").text();
		
		 }
	if($(obj).children("span").text()== ""){

		 lineitem = $(obj).text();
	

		 }

/////////////////////////////////////////////////////////////

if ( lineItemArr.indexOf(lineitem) == -1 ){
	  // alert("startaflag...."+startaflag);
	
		if (startaflag == 1){
			// alert("startaflag,,,,,,,"+startaflag);
			lineItemArr = lineItemArr+','
		}
		//alert("startaflag++++++"+startaflag);
		aArr.push(lineitem);
		lineItemArr = lineItemArr + lineitem;
		startaflag =1;
		
	
		idLineItem.push("lineItem_DYNAMIC_"+acount);
		$("#s2id_lineItemFilterPublisher").after("<div id='lineItem_DYNAMIC_"+acount+"' class='widget alert alert-info-header adjusted lineItem multipleLineItems' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;margin-bottom:10px;'>	<button data-dismiss='' class='close3' onclick=lineItem_close('"+acount+"'); style='margin-left:5px;margin-top:-7px;margin-right:-3px!important;background:none;border:none;'>×</button>	<div style='float:left;margin-left:-21px;color:white;width:300px;word-wrap:break-word;' id='label_"+acount+"'>	<strong>"+lineitem+"</strong> </div>	</div>");
		
		acount++;

		
	}
	

}
/* var propertNameArr='';
propertyArr =[];

startaflagproperty = 0;
acountProperty=1;
idPropertyType=[];



function getPropertyName(obj){
	propertName = $(obj).text();


	////////////////////////////////////////////
	 if ($(obj).children("span").text()!= ""){

		 propertName = $(obj).children("span").text();
		
		 }
	if($(obj).children("span").text()== ""){

		 propertName = $(obj).text();
	

		 }

/////////////////////////////////////////////////////////////

if ( propertNameArr.indexOf(propertName) == -1 ){
	
		if (startaflagproperty == 1){
			propertNameArr = propertNameArr+','
		}

		propertyArr.push(propertName);
		propertNameArr = propertNameArr + propertName;
		startaflagproperty =1;
		
	
		idPropertyType.push("propertyType_"+acountProperty);
		$("#s2id_test5_publisher").after("<div id='propertyType_"+acountProperty+"' class='widget alert alert-info-header adjusted propertName multipleLineItems' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;margin-bottom:10px;'>	<button data-dismiss='' class='close3' onclick=property_close('"+acountProperty+"'); style='margin-left:5px;margin-top:-7px;margin-right:-3px!important;background:none;border:none;'>×</button>	<div style='float:left;margin-left:-21px;color:white;' id='labelProperty_"+acountProperty+"'>	<strong>"+propertName+"</strong> </div>	</div>");
		
		acountProperty++;

		
	}
	

} */


function getLineOrder(obj){
	lineItemArr='';

	ordername = $(obj).children("div").text();
	lineItemArr =  $(obj).children("span").text();
	aArr.push($(obj).children("span").text());


	$("#s2id_test4_single").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).children("span").text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_test4_single").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	
	$("#order_dropdown_name").html("<span style='text-transform: uppercase;'>"+ordername+"</span> ");
	$("#order_dropdown").css({'display':'none'});
	$(".multipleLineItems").remove();
	$("#s2id_test4").after("<div id='lineItem_DYNAMIC_0' class='widget alert alert-info-header adjusted lineItem multipleLineItems' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;margin-bottom:10px;'>	<button data-dismiss='alert' class='close3' onclick=lineItem_close(0); style='margin-left:5px;margin-top:-7px;margin-right:-3px!important;background:none;border:none;'>×</button>	<div style='float:left;margin-left:-21px;color:white;width:300px;word-wrap:break-word;' id='label_0'>	<strong>"+$(obj).children("span").text()+"</strong> </div>	</div>");
	$("#order_dropdown_text").css({'display':'inline'});
	$("#lineItems_dropdown_single").css({'display':'none'});
	$("#lineItems_dropdown").css({'display':'inline'});
	
	fun4($(obj));
	
}

function lineItemFilterSinglePublisher(obj){
	lineItemArr='';

	ordername = $(obj).children("div").text();
	lineItemArr =  $(obj).children("span").text();
	aArr.push($(obj).children("span").text());


	$("#s2id_lineItems_dropdown_single_publisher").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>"+ $(obj).children("span").text() +"</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
	$("#s2id_lineItems_dropdown_single_publisher").removeClass("select2-dropdown-open select2-container-active");
	$(".select2-drop-active").hide();
	
	$("#order_dropdown_name_publisher").html("<span style='text-transform: uppercase;'>"+ordername+"</span> ");
	$("#order_dropdown_publisher").css({'display':'none'});
	$(".multipleLineItems").remove();
	$("#s2id_lineItemFilterPublisher").after("<div id='lineItem_DYNAMIC_0' class='widget alert alert-info-header adjusted lineItem multipleLineItems' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top:10px;margin-bottom:10px;'>	<button data-dismiss='alert' class='close3' onclick=lineItem_close(0); style='margin-left:5px;margin-top:-7px;margin-right:-3px!important;background:none;border:none;'>×</button>	<div style='float:left;margin-left:-21px;color:white;width:300px;word-wrap:break-word;' id='label_0'>	<strong>"+$(obj).children("span").text()+"</strong> </div>	</div>");
	$("#order_dropdown_text_publisher").css({'display':'inline'});
	$("#lineItems_dropdown_single_publisher").css({'display':'none'});
	$("#lineItems_dropdown_publisher").css({'display':'inline'});
	$("#line_itemName_publisher").css({'display':'inline'});
	
	lineItemFilterPublisher($(obj));
	
}

function lineItem_close(id){
	id0="label_"+id
	id1="lineItem_DYNAMIC_"+id
	labletext = $("#"+id0).text();
	index = aArr.indexOf($.trim(labletext));

	aArr.splice(index,1);

	lineItemArr='';
	startaflag =0;
	for(i=0; i<aArr.length; i++ )
	{
		if (startaflag == 1){
			lineItemArr = lineItemArr+','
		}
		startaflag =1;

		lineItemArr = lineItemArr+aArr[i];

	}	
	

	$("#"+id1).remove();
	
}

// function to close property types
/* function property_close(id){
	id0="labelProperty_"+id
	id1="propertyType_"+id
	labletext = $("#"+id0).text();
	index = propertyArr.indexOf($.trim(labletext));

	propertyArr.splice(index,1);

	propertNameArr='';
	startaflagproperty =0;
	for(i=0; i<propertyArr.length; i++ )
	{
		if (startaflagproperty == 1){
			propertNameArr = propertNameArr+','
		}
		startaflagproperty =1;

		propertNameArr = propertNameArr+propertyArr[i];

	}	
	

	$("#"+id1).remove();
	
} */


// end
$(document).ready(function(){
	 $("#s2id_test3").click(function(){
		
		 lineItemArr='';
		 aArr=[];
		 
		for(i=0;i<idLineItem.length;i++)
		{
			$("#"+idLineItem[i]).remove();	
		}
		 
	 });
	 
});

$(document).ready(function(){
	 $("#s2id_orderFilterPublisher").click(function(){
		
		 lineItemArr='';
		 aArr=[];
		 
		for(i=0;i<idLineItem.length;i++)
		{
			$("#"+idLineItem[i]).remove();	
		}
		 
	 });
	 
});
$(document).ready(function(){
	 $("#s2id_reallocationFilter").click(function(){
		
		 lineItemArr='';
		 aArr=[];
		 
		for(i=0;i<idLineItem.length;i++)
		{
			$("#"+idLineItem[i]).remove();	
		}
		 
	 });
	 
});
$(document).ready(function(){
	 $("#order_clear").click(function(){

		 $("#s2id_test3").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
		 $("#lineItems_dropdown_single").css({'display':'inline'});
		 $("#lineItems_dropdown").css({'display':'none'});
		 $("#order_clear").css({'display':'none'});
		 lineItemArr='';
		 aArr=[];
		 ordername='';
	 });
	 $("#line_clear_text").click(function(){
	 	$("#line_dropdown_text").css({'display':'none'});
	 	$("#lineItems_dropdown_single").css({'display':'inline'});
	 	lineItemArr='';
		aArr=[];
		ordername='';
	 });
});

$(document).ready(function(){
	 $("#order_clear_publisher").click(function(){
		 $("#s2id_orderFilterPublisher").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
		 $("#lineItems_dropdown_single_publisher").css({'display':'inline'});
		 $("#lineItems_dropdown_publisher").css({'display':'none'});
		 $("#order_clear_publisher").css({'display':'none'});
		 lineItemArr='';
		 aArr=[];
		 ordername='';
	 });
	 $("#line_clear_text_publisher").click(function(){
	 	$("#line_dropdown_text_publisher").css({'display':'none'});
	 	$("#lineItems_dropdown_single_publisher").css({'display':'inline'});
	 	lineItemArr='';
		aArr=[];
		ordername='';
	 });
});

$(document).ready(function(){
	$("#order_clear_text").click(function(){
		$("#order_dropdown_text").css({'display':'none'});
		$("#order_dropdown").css({'display':'inline'});
		
			
		//$("#s2id_test4_single").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
		//$("#s2id_test4_single").removeClass("select2-dropdown-open select2-container-active");
		//$(".select2-drop-active").hide();
		
		
		$("#lineItems_dropdown_single").css({'display':'inline'});
		$("#lineItems_dropdown").css({'display':'none'});
		$("#line_dropdown_text").css({'display':'none'});
		$(".multipleLineItems").remove();
		lineItemArr='';
		aArr=[];
		ordername='';
	});
});

$(document).ready(function(){
	$("#order_clear_text_publisher").click(function(){
		$("#order_dropdown_text_publisher").css({'display':'none'});
		$("#order_dropdown_publisher").css({'display':'inline'});
		
			
		//$("#s2id_test4_single").children().html("<a href='#' onclick='return false;' style='color:#999 !important;font-size:12px;border-bottom:none;text-decoration: none;' tabindex='-1'><span>Search</span><abbr class='select2-search-choice-close' style='display: none;'></abbr>   <div><b></b></div></a>");
		//$("#s2id_test4_single").removeClass("select2-dropdown-open select2-container-active");
		//$(".select2-drop-active").hide();
		
		
		$("#lineItems_dropdown_single_publisher").css({'display':'inline'});
		$("#lineItems_dropdown_publisher").css({'display':'none'});
		$("#line_dropdown_text_publisher").css({'display':'none'});
		$(".multipleLineItems").remove();
		lineItemArr='';
		aArr=[];
		ordername='';
	});
});

</script>
