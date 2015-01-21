package com.lin.web.action;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.PropertyChildObj;
import com.lin.server.bean.PropertyObj;
import com.lin.web.dto.AdSdkDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IAdSdkService;
import com.lin.web.service.IProductService;
import com.lin.web.service.IReportService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.AdSdkService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.EncriptionUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

/*
 * @author Manish Mudgal
 * 
 * This class is used to store data coming from mobile devices ad sdk. 
 * Communication to happen on https. With Authorization key provided as signature of the sender.
 */
public class AdSdkAction implements ServletRequestAware,ServletResponseAware,SessionAware{
	private static final String AUTH_KEY = "ashvbn!@#$5567907HHFC5";
	
	private static final Logger log = Logger.getLogger(AdSdkAction.class.getName());
	
	private String reportsResponse;	

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		
		return Action.SUCCESS;
	}
	 /**
			Json Structure
			Root
				DeviceParam - Json
					NetworkParams - Json
						ConnectionProvider - JsonArray
							GeoParams - JsonArray
				UserParam - Json


	  * 
	  * 
	  * @throws IOException
	  */
	public void getUserDataFromSDK() throws IOException{
		log.info("Dev mode is: ["+request.getParameter("devmode")+"]");
		log.info("Auth Key is: ["+request.getParameter("authKey")+"]");
		log.info("JSON is ["+request.getParameter("jsonData")+"]");
		
		
		try{
			if(request.getParameter("devmode")== null){
			if(!request.getScheme().equals("https")){
				throw new Exception("Only https protocol allowed for security reasons.");
			}
			if(!AUTH_KEY.equals(request.getParameter("authKey"))){
				throw new Exception("Invalid authentication key");
			}
			if(!request.getMethod().equals("POST")){
				throw new Exception("Only post method is allowed.");
			}
			}
		log.info("Request is valid. Processing.");	
		response.setContentType("application/json");
		String requestedJson = request.getParameter("jsonData");
	
		//String json = "{\"key\":\"\",\"timeStamp\":\"2014-12-17 16:21:10\",\"make\":\"\",\"model\":\"\",\"idfa\":\"\",\"idfv\":\"\",\"androidId\":\"weurhuie432ee\",\"googleAdId\":\"\",\"deviceId\":\"er483232bewd83d\",\"odin\":\"\",\"pushNotificationId\":\"\",\"deviceParam\":{\"os\":\"iOS\",\"osVersion\":\"8.1.2\",\"imei\":\"hehd3ueiwhdh932ed\",\"meid\":\"\",\"serialId\":\"\",\"multiSim\":true,\"networkParams\":{\"wifiTimeStamp\":\"2014-12-17 16:21:10\",\"wifiMacAddress\":\"\",\"wifiName\":\"\",\"wifiIpAddress\":\"192.168.1.10\",\"type\":\"wifi\",\"connectionProvider\":[{\"timeStamp\":\"2014-12-17 16:21:10\",\"sim\":\"sim1\",\"mcc\":\"404\",\"mnc\":\"10\",\"timezone\":\"\",\"networkConnection\":{\"timeStamp\":\"2014-12-17 16:21:10\",\"macAddress\":\"er:33:e3:w2:33:45\",\"ipAddress\":\"10.0.3.13\",\"type\":\"hspda\"},\"geoParams\":[{\"timeStamp\":\"2014-12-17 16:21:10\",\"latitue\":\"34.98475\",\"longitude\":\"45.88477\",\"cellId\":\"302\",\"lac\":\"34564\"},{\"timeStamp\":\"2014-12-17 16:21:10\",\"latitue\":\"34.98475\",\"longitude\":\"45.88477\",\"cellId\":\"302\",\"lac\":\"34564\"}]}]},\"userParams\":{\"language\":\"en\",\"appstoreid\":\"agrim@mediaagility.com\",\"country\":\"INDIA\",\"firstName\":\"\",\"lastName\":\"\"}}}";
		String key = parseJsonAndSave(requestedJson); 
		
			response.getWriter().print("{\"key\":\""+key+"\",\"error\":\"false\",\"debug\":\"\"}");
		}catch(Exception e){
			response.getWriter().print("{\"key\":\"\",\"error\":\"true\",\"debug\":\""+e.getMessage()+"\"}");
			log.severe("Error while saving dto");
		}

	}
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		//String json = "{ \"androidId\" : \"\", \"idfa\" : \"3DC543A1-110D-489E-B403-F6B662245147\", \"deviceParam\" : { \"serialId\" : \"\", \"os\" : \"iOS\", \"meid\" : \"\", \"multiSim\" : \"\", \"networkParams\" : { \"type\" : \"\", \"wifiMacAddress\" : \"\", \"wifiIpAddress\" : \"\", \"wifiTimeStamp\" : \"\", \"wifiName\" : \"\", \"connectionProvider\" : [ { \"mcc\" : \"\", \"mnc\" : \"\", \"networkConnection\" : { \"macAddress\" : \"\", \"ipAddress\" : \"\", \"type\" : \"\", \"timeStamp\" : \"2014-12-19 20:31:02\" }, \"timezone\" : \"\", \"geoParams\" : [ { \"longitude\" : \"77.033781\", \"timeStamp\" : \"2014-12-19 20:31:02\", \"latitude\" : \"28.458226\", \"lac\" : \"\", \"cellId\" : \"\" } ], \"timeStamp\" : \"2014-12-19 20:31:02\", \"sim\" : \"\" } ] }, \"osVersion\" : \"8.1.2\", \"userParams\" : { \"country\" : \"\", \"id\" : \"\", \"lastName\" : \"\", \"language\" : \"en\", \"appstoreid\" : \"\", \"firstName\" : \"\" }, \"imei\" : \"\" }, \"pushNotificationId\" : \"\", \"odin\" : \"\", \"key\" : \"\", \"timeStamp\" : \"2014-12-19 20:31:02\", \"make\" : \"\", \"googleAdId\" : \"\", \"model\" : \"iPad Mini (WiFi)\", \"idfv\" : \"F28B0B2B-3D42-4077-BDD7-87AA4B9333D3\", \"deviceId\" : \"\" }";
		String json = "{ \"androidId\": \"1e29db3aa1262030\", \"deviceId\": \"\", \"deviceParam\": { \"imei\": \"\", \"meid\": \"\", \"multiSim\": \"\", \"networkParams\": { \"connectionProvider\": [ { \"geoParams\": [ { \"cellId\": \"\", \"lac\": \"\", \"latitue\": \"0.0\", \"longitude\": \"0.0\", \"timeStamp\": \"2015-01-16 02:34:19\" } ], \"mcc\": \"\", \"mnc\": \"\", \"networkConnection\": { \"ipAddress\": \"\", \"macAddress\": \"\", \"timeStamp\": \"2015-01-16 02:34:19,\" \"type\": \"Unknown\" }, \"sim\": \"\", \"timeStamp\": \"2015-01-16 02:34:19,\" \"timezone\": \"\" } ], \"type\": \"\", \"wifiIpAddress\": \"\", \"wifiMacAddress\": \"30:85:a9:da:63:97\", \"wifiName\": \"\", \"wifiTimeStamp\": \"2015-01-16 02:34:19\" }, \"os\": \"Android\", \"osVersion\": \"5.0.2\", \"serialId\": \"\", \"userParams\": { \"appstoreid\": \"chetali.agarwal@mediaagility.com\", \"country\": \"\", \"firstName\": \"\", \"language\": \"\", \"lastName\": \"\" } }, \"googleAdId\": \"\", \"idfa\": \"\", \"idfv\": \"\", \"key\": \"\", \"make\": \"\", \"model\": \"\", \"odin\": \"\", \"pushNotificationId\": \"\", \"timeStamp\": \"2015-01-16 02:34:19\" }";
		new AdSdkAction().parseJsonAndSave(json);
	}
	public String parseJsonAndSave(String requestedJson) throws GeneralSecurityException, IOException {
		JsonParser parser = new JsonParser();
		String key = null;
		JsonElement element = parser.parse(requestedJson);
		 if (element.isJsonObject()) {
			 JsonObject root = element.getAsJsonObject();
			 JsonObject deviceParam  = root.get("deviceParam").getAsJsonObject();
			 JsonObject userParam  = deviceParam.get("userParams").getAsJsonObject();
			 JsonObject networkParams = deviceParam.get("networkParams").getAsJsonObject();		 
			 JsonArray connectionProviders  = networkParams.getAsJsonArray("connectionProvider");
			 key = verifyKey(root.get("key").getAsString());
			 for (int i = 0; i < connectionProviders.size(); i++) {
				 JsonObject connectionProvider = connectionProviders.get(i).getAsJsonObject();
				 log.info("Processing connection provider ["+connectionProvider.toString()+"]");
				 JsonObject networkConnection = connectionProvider.get("networkConnection").getAsJsonObject();
				 JsonArray geoParams = connectionProvider.getAsJsonArray("geoParams");
				 for (int j = 0; j < geoParams.size(); j++) { 
					 JsonObject geoParam = geoParams.get(j).getAsJsonObject();
					 log.info("Processing geoParam ["+geoParam.toString()+"]");
					 AdSdkDTO dto = new AdSdkDTO();
					 
					 // Root Params
					 dto.setKey(key);
					 dto.setTimestamp(root.get("timeStamp").getAsString());
					 dto.setMake(root.get("make").getAsString());
					 dto.setModel(root.get("model").getAsString());
					 dto.setIdfa(root.get("idfa").getAsString());
					 dto.setIdfv(root.get("idfv").getAsString());
					 dto.setAndroidId(root.get("androidId").getAsString());
					 dto.setGoogleAdId(root.get("googleAdId").getAsString());
					 dto.setDeviceId(root.get("deviceId").getAsString());
					 dto.setOdin(root.get("odin").getAsString());
					 dto.setPushNotificationId(root.get("pushNotificationId").getAsString());
					 log.info("Set root params");
					 
					 // Device Parameters
					 dto.setOsDeviceParam(deviceParam.get("os").getAsString());
					 dto.setOsVersion(deviceParam.get("osVersion").getAsString());
					 dto.setImei(deviceParam.get("imei").getAsString());
					 dto.setMeid(deviceParam.get("meid").getAsString());
					 dto.setSerialId(deviceParam.get("serialId").getAsString());
					 dto.setMultisim(deviceParam.get("multiSim").getAsString());
					 log.info("Set device params");
					 
					 // Network params
					 dto.setWifiTimeStamp(networkParams.get("wifiTimeStamp").getAsString());
					 dto.setWifiMacAddress(networkParams.get("wifiMacAddress").getAsString());
					 dto.setWifiName(networkParams.get("wifiName").getAsString());
					 dto.setWifiIpAddress(networkParams.get("wifiIpAddress").getAsString());
					 dto.setNetworkType(networkParams.get("type").getAsString());
					 log.info("Set network params");
					 
					 // Connection Provider Params
					 dto.setConnectionProviderTimestamp(connectionProvider.get("timeStamp").getAsString());
					 dto.setSim(connectionProvider.get("sim").getAsString());
					 dto.setMcc(connectionProvider.get("mcc").getAsString());
					 dto.setMnc(connectionProvider.get("mnc").getAsString());
					 dto.setTimezone(connectionProvider.get("timezone").getAsString());
					 log.info("Set connetion provider params");
					 
					 // Network connection params
					 dto.setNetworkConnectionTimestamp(networkConnection.get("timeStamp").getAsString());
					 dto.setMacAddress(networkConnection.get("macAddress").getAsString());
					 dto.setIpAddress(networkConnection.get("ipAddress").getAsString());
					 dto.setNetworkConnectionType(networkConnection.get("type").getAsString());
					 log.info("Set network connection params");
					 
					 
					 // geo params
					 dto.setGeoTimestamp(geoParam.get("timeStamp").getAsString());
					 dto.setLatitude(geoParam.get("latitude").getAsString());
					 dto.setLongitude(geoParam.get("longitude").getAsString());
					 dto.setCellId(geoParam.get("cellId").getAsString());
					 dto.setLac(geoParam.get("lac").getAsString());
					 log.info("Set geo params");
					 
					 // userParams
					 dto.setLanguage(userParam.get("language").getAsString());
					 dto.setAppstoreId(userParam.get("appstoreid").getAsString());
					 dto.setCountry(userParam.get("country").getAsString());
					 dto.setFirstName(userParam.get("firstName").getAsString());
					 dto.setLastName(userParam.get("lastName").getAsString());
					 dto.setInsertDate(DateUtil.getDateAsString(new Date(), "MM-dd-yyyy"));
					 dto.setInsertTime(DateUtil.getDateAsString(new Date(), "MM-dd-yyyy HH:mm:ss"));
					 log.info("Set user Params");
					 
					 log.info("Saving data to bigquery.");
					 BigQueryUtil.uploadAdSdkDTOData(dto.getColumnValueArray());
					 // saveSdkData(dto);	
				 }
				}
			 
		 }
		return key;
	}
	private void saveSdkData(AdSdkDTO dto) {
		IAdSdkService service = (IAdSdkService) BusinessServiceLocator.locate(IAdSdkService.class);
		service.saveSdkDTO(dto);
		
	}
	private String verifyKey(String key) {
		if(key == null || key.trim().length() == 0){
			key = generateKey();
		}
		return key;
	}
	private String generateKey() {
		String passKey = String.valueOf(System.currentTimeMillis());
		String md5 = EncriptionUtil.getEncriptedStrMD5(passKey+(new BigInteger(130, new SecureRandom()).toString(32)));
		log.info("Generated md5 key  ["+md5+"]");
		return md5;
	}
	
	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}

	public void setReportsResponse(String reportsResponse) {
		this.reportsResponse = reportsResponse;
	}


	public String getReportsResponse() {
		return reportsResponse;
	}


}


