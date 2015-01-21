package com.lin.web.action;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lin.web.util.DateUtil;
import com.lin.web.util.StringUtil;

public class CampaignSetupActionTest {

	private static final Logger log = Logger.getLogger(CampaignSetupActionTest.class.getName());
	private CampaignSetupAction campaignSetupAction;

	  private final LocalServiceTestHelper helper =
	      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig())
	          .setEnvIsLoggedIn(true)
	          .setEnvAuthDomain("localhost");
	  
	  HttpServletRequest request=null;
	  HttpServletResponse response=null;
	  
	  @Before
	  public void setupCampaignSetupAction() {
	    helper.setUp();
	    campaignSetupAction = new CampaignSetupAction();
	    if(request==null){
			  request = mock(HttpServletRequest.class);
		}
		if(response==null){
			  response = mock(HttpServletResponse.class);
		}
	  }

	  @After
	  public void tearDownHelper() {
	    helper.tearDown();
	  }


	  public void testSetUpAdUnit(){
		  
		    String testAdUnitName="lin.test";
			String testLevel="2";
			String testOrderName="ICC Cricket 2014";
			String testAgencyName="Lin Digital";		
			String testPlacement="ICC September";
			String testAdSize="300x50";
			
		 
		    when(request.getParameter("adUnitName")).thenReturn(testAdUnitName);
		    when(request.getParameter("adUnitLevel")).thenReturn(testLevel);
		    when(request.getParameter("orderName")).thenReturn(testOrderName);
		    when(request.getParameter("agencyName")).thenReturn(testAgencyName);
		    when(request.getParameter("placement")).thenReturn(testPlacement);		    
		    when(request.getParameter("adSizes")).thenReturn(testAdSize);
		    

		    campaignSetupAction.setServletRequest(request);
		    campaignSetupAction.setUpAdUnit();
		    boolean passTest=false;
		    String status=campaignSetupAction.getStatus();
		    long adUnit=StringUtil.getLongValue( status);
		    if(adUnit >0){
		    	passTest=true;
		    }
		    assertEquals(passTest, true);
	  }
	  
	  public void testGptTagGenerator()  {
		
	  /*  HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);*/

	    String testNetworkCode = "5678";	  
	    String testAdUnitCode1= "66082029" ;//"lin.mobile";
		String testAdUnitCode2="75606549"; //"checking_status_campaign_camp_1_status_cbs_-_ted";
		String testAdSize="300x50";
		
	    when(request.getParameter("networkCode")).thenReturn(testNetworkCode);
	    when(request.getParameter("adUnitCode1")).thenReturn(testAdUnitCode1);
	    when(request.getParameter("adUnitCode2")).thenReturn(testAdUnitCode2);
	    when(request.getParameter("adSize")).thenReturn(testAdSize);
	    

	    campaignSetupAction.setServletRequest(request);
	    campaignSetupAction.gptTagGenerator();
	    String status=campaignSetupAction.getStatus();

	    String [] testAdSizeArr=testAdSize.split("[xX]");
	    StringBuffer tagScriptBuffer=new StringBuffer();
	    tagScriptBuffer.append("<script src=\"//www.googletagservices.com/tag/js/gpt.js\">");
		   tagScriptBuffer.append("\n");
		   tagScriptBuffer.append("googletag.pubads().definePassback(");
		   tagScriptBuffer.append("\n");		   
		   tagScriptBuffer.append("\"/"+testNetworkCode+"/"+testAdUnitCode1+"/"+testAdUnitCode2+"\", ["+testAdSizeArr[0]+", "+testAdSizeArr[1]+"])");
		   tagScriptBuffer.append("\n");
		   tagScriptBuffer.append(".setClickUrl(\"%%CLICK_URL_UNESC%%\")");
		   tagScriptBuffer.append("\n");
		   tagScriptBuffer.append(".display();");
		   tagScriptBuffer.append("\n");
		   tagScriptBuffer.append("</script>");
	   
	       assertEquals(status, tagScriptBuffer.toString());	 
	  }
	  
	  
	  public void testForecastInventory(){
		   
		    String testAdUnitName="lin.event";
			String testAdUnitId="60694542";
			String testAdSize="300x250,320x50,320x480";
			String testNetworkCode="5678";
			
			String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			String testStartDate= DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
			String testEndDate=DateUtil.getModifiedDateStringByDays(currentDate,30, "yyyy-MM-dd");
			//testStartDate="2014-07-21";
			//testEndDate="2014-12-31";
		    when(request.getParameter("adUnitId")).thenReturn(testAdUnitId);
		    when(request.getParameter("adUnitName")).thenReturn(testAdUnitName);
		    when(request.getParameter("adSize")).thenReturn(testAdSize);
		    when(request.getParameter("startDate")).thenReturn(testStartDate);
		    when(request.getParameter("endDate")).thenReturn(testEndDate);
		    when(request.getParameter("networkCode")).thenReturn(testNetworkCode);
		    
		    campaignSetupAction.setServletRequest(request);
		    campaignSetupAction.forecastInventory();
		    JSONObject jsonObject=campaignSetupAction.getJsonObject();
		    String error=(String) jsonObject.get("error");
		    log.info("jsonObject:"+jsonObject.toString());
		    assertEquals(jsonObject.toString(),error, null);
		   
	  }
	  
	/*  @Test
	  public void testLineItemsNeedCreatives(){
		  String testOrderId="217221269";
		  when(request.getParameter("orderId")).thenReturn(testOrderId);
		  campaignSetupAction.setServletRequest(request);
		  
		  String status=campaignSetupAction.getAllLineItemsNeedsCreative();
		  System.out.println("status -"+status);
		  assertEquals(status,status, "success");
	  }*/

}