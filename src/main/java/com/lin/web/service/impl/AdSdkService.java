package com.lin.web.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.persistance.dao.IPerformanceMonitoringDAO;
import com.lin.persistance.dao.IProductDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.AdSdkDAO;
import com.lin.persistance.dao.impl.MediaPlanDAO;
import com.lin.persistance.dao.impl.PerformanceMonitoringDAO;
import com.lin.persistance.dao.impl.ProductDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.CityObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.ProductForecastObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.server.bean.StateObj;
import com.lin.web.documents.TextSearchDocument;
import com.lin.web.dto.AdSdkDTO;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.ProductForecastDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.ProductDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SmartCampaignPlacementDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IAdSdkService;
import com.lin.web.service.IProductService;
import com.lin.web.service.IUserService;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DBUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;


public class AdSdkService implements IAdSdkService{ 
	private static final Logger log = Logger.getLogger(AdSdkService.class.getName());

	@Override
	public int saveSdkDTO(AdSdkDTO dto) {
		return new AdSdkDAO().saveSdkDTO(dto);
	}

	
	
	
}
