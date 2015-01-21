package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.Saver;
import com.lin.persistance.dao.IAdSdkDAO;
import com.lin.persistance.dao.IProductDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.CityObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.ProductForecastObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.AdSdkDTO;
import com.lin.web.service.impl.OfyService;


public class AdSdkDAO implements IAdSdkDAO{
	
	private static final Logger log = Logger.getLogger(AdSdkDAO.class.getName());	
	private Objectify obfy = OfyService.ofy();	
	private Objectify strongObfy = OfyService.ofy().consistency(Consistency.STRONG);
	
	public int saveSdkDTO(AdSdkDTO obj){	
		obfy.save().entity(obj).now();
		return 1;
	}

	}
