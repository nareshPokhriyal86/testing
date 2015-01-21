package com.lin.web.action;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lin.web.util.DateUtil;

public class ProductActionTest {

	private static final Logger log = Logger.getLogger(ProductActionTest.class.getName());
	private ProductAction productAction;

	  private final LocalServiceTestHelper helper =
	      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig())
	          .setEnvIsLoggedIn(true)
	          .setEnvAuthDomain("localhost");
	  
	  HttpServletRequest request=null;
	  HttpServletResponse response=null;
	  
	  @Before
	  public void setupProductAction() {
	    helper.setUp();
	    productAction = new ProductAction();
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

	 
	  
	 /* @Test
	  public void testLoadStates(){
		   
		    String testCountryCode="US";			
					    
		    when(request.getParameter("countryCode")).thenReturn(testCountryCode);
		   
		    productAction.setServletRequest(request);
		    productAction.loadStates();
		    JSONObject jsonObject=productAction.getJsonObject();
		    String error=(String) jsonObject.get("error");
		    log.info("jsonObject:"+jsonObject.toString());
		    assertEquals(jsonObject.toString(),error, null);
		   
	  }*/

}