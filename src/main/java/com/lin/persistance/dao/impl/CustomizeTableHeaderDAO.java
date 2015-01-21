package com.lin.persistance.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Objectify;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.web.service.impl.OfyService;

public class CustomizeTableHeaderDAO {
	private static final Logger log = Logger.getLogger(CustomizeTableHeaderDAO.class.getName());
	private Objectify obfy = OfyService.ofy();
	
	public List<AdvertiserReportObj> loadPerformingLineItemsForAdvertiser(int size,String lowerDate,String upperDate) throws DataServiceException{
		List<AdvertiserReportObj> advertiserReportList=null;
		advertiserReportList=obfy.load().type(AdvertiserReportObj.class)		                         
		                         /*.filter("date >= ",lowerDate)
	    	                     .filter("date <= ",upperDate)
	    	                     .order("-date")*/
	    	                     .order("-adServerCTR")
		                         .limit(size)
		                         .list();
		return advertiserReportList;
	}
}
