package com.lin.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.lin.server.bean.CustomizeTableHeaderObj;
import com.lin.web.service.ICustomizeTableHeaderService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.OfyService;

public class CustomizeTableHeaderAction {
	private static final Logger log = Logger.getLogger(CustomizeTableHeaderAction.class.getName());
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	CustomizeTableHeaderObj headerObj = new CustomizeTableHeaderObj();
	
	
	public void advertiserPerformer(){
		
		List<CustomizeTableHeaderObj> headerValue = new ArrayList<CustomizeTableHeaderObj>();
		ICustomizeTableHeaderService service = (ICustomizeTableHeaderService) BusinessServiceLocator.locate(ICustomizeTableHeaderService.class);
		headerObj.setId("advertiserPerformer");
		headerObj.setName("advertiserPerformer");
		headerObj.setColumn1("CAMPAIGN LINE ITEMS");
		headerObj.setColumn2("IMPRESSION DELIVERED");
		headerObj.setColumn3("CLICKS");
		headerObj.setColumn4("CTR(%)");
		OfyService ofy = new OfyService();
		
	}
}
