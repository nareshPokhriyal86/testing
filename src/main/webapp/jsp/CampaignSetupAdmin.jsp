<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ONE - campaign Setup admin</title>

</head>
<body >
 		
	<%-- <div>
	   <h3>Set up Ad Unit for new DFP instance -- LinMobile</h3>
	   <form name="adunitSetup" id="adunitSetup">
	       <div>
	        <select id="adUnitlevel" >
	           <option value="-1">Select adUnitlevel</option>
	           <option value="1">1</option> 
	           <option value="2">2</option>         
           </select>
           </div>
           <div><span>AdUnit Name:</span><input type="text" id="adUnitName" value="" /><br/></div>
           <div><span>Agency Name:</span><input type="text" id="agencyName" value="" /><br/></div>
           <div><span>Order Name:</span><input type="text" id="orderName" value="" /><br/></div>
           <div><span>Placement Name:</span><input type="text" id="placement" value="" /><br/></div>
           <div>
	           <select id="adSizes" multiple="multiple">
		           <option value="-1">Select adSizes</option>		           
		           <option value="216x36">216x36</option>   
		           <option value="300x250">300x250</option> 
		           <option value="300x50">300x50</option>
		           <option value="320x50">320x50</option>  
		           <option value="728x90">728x90</option>          
	           </select>
           </div>
           <span onclick="adUnitSetup()" style="cursor:pointer;background:yellow;">AdUnitSetup</span>
	   </form>
	   <div id="status" style="color:red;"></div>
	</div> --%>
	
	<div>
	   <h3>Set up Company(ADVERTISER/AGENCY) for new DFP instance -- LinMobile</h3>
	   <form name="companySetUp" id="companySetUp">
	       <div>
	        <select id="companyType" >
	           <option value="-1">Select companyType</option>
	           <option value="ADVERTISER">ADVERTISER</option> 
	           <option value="AGENCY">AGENCY</option>         
           </select>
           </div>
           <div><span>Company Name:</span><input type="text" id="companyName" value="" /><br/></div>
           <div><span>Address:</span><input type="text" id="address" value="" /><br/></div>
           <div><span>State:</span><input type="text" id="state" value="" /><br/></div>
           <div><span>Email:</span><input type="email" id="email" value="" /><br/></div>
           <div><span>Phone:</span><input type="text" id="phone" value="" /><br/></div>
           <div><span>Fax:</span><input type="text" id="fax" value="" /><br/></div>           
           <span onclick="companySetUp()" style="cursor:pointer;background:yellow;">Company Setup</span>
	   </form>
	   <input type="hidden" id="advertiserId" name="advertiserId"/>
       <input type="hidden" id="agencyId" name="agencyId"/>
	   <div id="companyStatus" style="color:red;"></div>
	</div>
	
	<div>
	   <h3>Set up Campaign for new DFP instance -- LinMobile</h3>
	   <form name="campaignSetup" id="campaignSetup">
	      <div><span>DFP Instance</span>
	          <select id="dfpAccount" >
		           <option value="-1">Select DFP instance</option>		      
		          <!--  <option value="12008447">MediaAgility</option>   -->
		            <option value="9331149">Lin Mobile</option>
		          <!--   <option value="5678">Lin Media</option>  
		           <option value="45604844">Tribune</option>       -->      
	          </select>
	      </div>
	      <div><span>Publisher </span>
		      <select id="publisherName" >
		           <option value="-1">Select publisher</option>
		           <option value="Lin Media">Lin Media</option> 
		           <option value="Lin Mobile">Lin Mobile</option>
		           <option value="Run DSP">Run DSP</option>   
		           <option value="Tribune">Tribune</option>            
	          </select>
	      </div>
	      <div><span>Campaign Name:</span><input type="text" id="campaignName" value="" /><br/></div>
	      <div><span>Rate:</span><input type="text" id="rate" value="" /><br/></div>
	      <div><span>Units bought:</span><input type="text" id="unitsBought" value="" /><br/></div>
	     <%--  <div><span>AdUnit Name:</span><input type="text" id="adUnitName" value="" /><br/></div> --%>
           <div><span>Agency Name:</span><input type="text" id="agencyName" value="" /><br/></div>
           <div><span>Advertiser Name:</span><input type="text" id="advertiserName" value="" /><br/></div>
           <div><span>Order Name:</span><input type="text" id="orderName" value="" /><br/></div>
           <div><span>Placement Name:</span><input type="text" id="placement" value="" /><br/></div>
           <div><span>Flight:</span><input type="text" id="startDate" value="yyyy-MM-dd" /><br/>
           							<input type="text" id="endDate" value="yyyy-MM-dd" /><br/>
           </div>
           <div>
	           <select id="adSizes" multiple="multiple">
		           <option value="-1">Select adSizes</option>		           
		           <option value="216x36">216x36</option>   
		           <option value="300x250">300x250</option> 
		           <option value="300x50">300x50</option>
		           <option value="320x50">320x50</option>  
		           <option value="728x90">728x90</option>          
	           </select>
           </div>
           <span onclick="doCampaignSetup()" style="cursor:pointer;background:yellow;">Campaign Setup</span>
	   </form>
	   <div id="status" style="color:red;"></div>
	</div>
	
</body>
<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<script type="text/javascript">
 function doCampaignSetup(){
	 try{	
		 var campaignName=$.trim($('#campaignName').val());
		 var publisherName=$.trim($('#publisherName').val());
		 var dfpAccount=$.trim($('#dfpAccount').val());
		 var agencyName=$.trim($('#agencyName').val());
		 var advertiserName=$.trim($('#advertiserName').val());
		 var orderName=$.trim($('#orderName').val());
		 var placement=$.trim($('#placement').val());
		 var adSizes = $('#adSizes').val();
		 var endDate=$.trim($('#endDate').val());
		 var startDate=$.trim($('#startDate').val());
		 var rate=$.trim($('#rate').val());
		 var unitsBought=$.trim($('#unitsBought').val());
		 console.log(adSizes);
		 adSizes=adSizes+"";
		 
		 $.ajax({
				type : "GET",
				url : "/setUpOrder.lin",
				cache : false,
				data : {
					campaignName :  campaignName,
					publisherName : publisherName,
					dfpAccount : dfpAccount,
					agencyName: agencyName,
					orderName: orderName,
					placement: placement,
					adSizes: adSizes,
					startDate : startDate,
					endDate : endDate,
					advertiserName : advertiserName,
					rate: rate,
					unitsBought : unitsBought
				},	
				dataType : 'json',
				success : function(data) {
					console.log("status:"+data);
					$('#status').html("status : "+data);
					document.getElementById("campaignSetup").reset();
					
				},
				error : function(jqXHR, exception) {
					console.log("campaignSetup :ajax failed:"+exception);					
				}
		  });			
	 }catch(err){
		 console.log("campaignSetup:"+err);
     }	  
 }
 
 function companySetUp(){
	 try{	
		 var companyType=$.trim($('#companyType').val());
		 var companyName=$.trim($('#companyName').val());
		 var address=$.trim($('#address').val());		
		 var state=$.trim($('#state').val());				
		 var email=$.trim($('#email').val());		
		 var fax=$.trim($('#fax').val());		
		 var phone=$.trim($('#phone').val());		
		 
		 $.ajax({
				type : 'GET',
				url : '/setUpCompany.lin',
				cache : false,
				data : {
					companyType : companyType,
					companyName :companyName,
					address : address,
					state : state,
					email : email,
					fax : fax,
					phone : phone
				},	
				dataType : 'json',
				success : function(data) {
					console.log("status:"+data);
					$('#companyStatus').html('Created Company Id : '+data);
					document.getElementById("companySetUp").reset();
					if(companyType=='ADVERTISER'){
						$('#advertiserId').val(data);
					}else{
						$('#agencyId').val(data);
					}
					
				},
				error : function(jqXHR, exception) {
					console.log('companySetUp :ajax failed:'+exception);					
				}
		});			
	 }catch(err){
		 console.log('companySetUp:'+err);
     }	  
 }
</script>  
</html>