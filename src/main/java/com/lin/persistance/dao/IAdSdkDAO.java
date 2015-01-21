package com.lin.persistance.dao;

import java.util.List;

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

public interface IAdSdkDAO extends IBaseDao { 
	
	public int saveSdkDTO(AdSdkDTO dto);
}
