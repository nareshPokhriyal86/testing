package example;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import au.com.bytecode.opencsv.CSVReader;

import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201403.ReportDownloader;
import com.google.api.ads.dfp.axis.v201403.ExportFormat;
import com.google.api.ads.dfp.axis.v201403.ReportJob;
import com.google.api.ads.dfp.axis.v201403.ReportServiceInterface;
import com.google.api.ads.dfp.axis.v201403.Column;
import com.google.api.ads.dfp.axis.v201403.Dimension;
import com.google.api.ads.dfp.axis.v201403.DimensionAttribute;
import com.google.api.ads.dfp.axis.v201403.Statement;
import com.google.api.ads.dfp.jaxws.utils.v201403.DateTimes;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitTargeting;
import com.google.api.ads.dfp.jaxws.v201403.ApiException;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.CreativePlaceholder;
import com.google.api.ads.dfp.jaxws.v201403.CreativeRotationType;
import com.google.api.ads.dfp.jaxws.v201403.Forecast;
import com.google.api.ads.dfp.jaxws.v201403.ForecastServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.InventoryTargeting;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemType;
import com.google.api.ads.dfp.jaxws.v201403.ReportQueryAdUnitView;
import com.google.api.ads.dfp.jaxws.v201403.ReservationDetailsErrorReason;
import com.google.api.ads.dfp.jaxws.v201403.RoadblockingType;
import com.google.api.ads.dfp.jaxws.v201403.Size;
import com.google.api.ads.dfp.jaxws.v201403.TargetPlatform;
import com.google.api.ads.dfp.jaxws.v201403.Targeting;
import com.google.api.ads.dfp.jaxws.v201403.UnitType;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.ads.dfp.lib.utils.ReportCallback;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.bigquery.model.TableDataInsertAllRequest;
import com.google.api.services.bigquery.model.TableDataInsertAllResponse;
import com.google.api.services.bigquery.model.TableRow;
import com.google.common.collect.ImmutableList;
import com.google.gdata.data.analytics.AccountEntry;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.AdvertiserDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.service.IHistoricalReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.SmartCampaignPlannerService;
import com.lin.web.util.DataLoaderUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;

class Singleton {

	private static class Loader {
		static Singleton INSTANCE = new Singleton();
	}

	private Singleton() {
		System.out.println("hello");
	}

	public static Singleton getInstance() {
		return Loader.INSTANCE;
	}
}

enum MySingleton {
	SINGLE;
	public void getInstance() {
		System.out.println("Hello");
	}
}

public class Test {

	private void syncAccountAdertiserAgencies() {
		ISmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
		try {
			List<AdvertiserObj> advertisers = dao
					.getAllAdvertiserByAdServerId(null);
			List<AgencyObj> agencies = dao.getAllAgencyByAdServerId(null);
			List<AccountsEntity> accounts = dao.getAllAccounts();

			for (AdvertiserObj advertiser : advertisers) {
				boolean found = false;
				for (AccountsEntity accountsEntity : accounts) {
					if (accountsEntity.getAccountDfpId().equals(
							advertiser.getAdvertiserId())) {
						accountsEntity.setContactPersonName(advertiser
								.getContactPersonName());
						accountsEntity.setAddress(advertiser.getAddress());
						accountsEntity
								.setAccountType(LinMobileConstants.ADVERTISER_ID_PREFIX);
						dao.saveObject(accountsEntity);
						found = true;
						break;
					}
				}
				if (!found) {
					AccountsEntity newAccount = getAccount(advertiser, null);
					dao.saveObject(newAccount);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private AccountsEntity getAccount(AdvertiserObj advertiser, AgencyObj agency) {
		AccountsEntity accountsEntity = new AccountsEntity();
		accountsEntity.setAccountDfpId(advertiser.getAdvertiserId() + "");
		accountsEntity.setAccountName(advertiser.getName());
		accountsEntity.setAccountType(LinMobileConstants.ADVERTISER_ID_PREFIX);
		accountsEntity.setAddress(advertiser.getAddress());
		accountsEntity.setAdServerId(advertiser.getDfpNetworkCode());
		// accountsEntity.setAdServerUserName(advertiser.);
		// accountsEntity.setCompanyId(advertiser.get);
		accountsEntity.setContactPersonName(advertiser.getContactPersonName());
		// accountsEntity.setCreatedByUserId(advertiser.getCreatedBy());
		accountsEntity.setCreationDate(new Date());
		// accountsEntity.setDfpAccountName(advertiser.get);
		accountsEntity.setEmail(advertiser.getEmail());
		accountsEntity.setPhone(advertiser.getPhone());
		accountsEntity.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
		return accountsEntity;
	}

	public static String formatedFlightDuration(String startDate, String endDate) {
		String flightDuration = "";
		String flightStart = DateUtil.getFormatedDate(startDate, "MM-dd-yyyy",
				"MMM");
		String flightEnd = DateUtil.getFormatedDate(endDate, "MM-dd-yyyy",
				"MMM");
		String startYear = DateUtil.getFormatedDate(startDate, "MM-dd-yyyy",
				"yyyy");
		String endYear = DateUtil
				.getFormatedDate(endDate, "MM-dd-yyyy", "yyyy");

		if (startYear != null && endYear != null && !startYear.equals(endYear)) {
			System.out.println("Here....");
			flightStart = flightStart + " " + startYear;
			flightEnd = flightEnd + " " + endYear;
			flightDuration = flightStart + " - " + flightEnd;
		} else if (startYear != null && endYear != null
				&& startYear.equals(endYear)) {

			if (flightStart != null && flightEnd != null
					&& flightStart.equals(flightEnd)) {
				flightStart = flightStart + " " + endYear;
				flightDuration = flightStart;
			} else {
				flightEnd = flightEnd + " " + endYear;
				flightDuration = flightStart + " - " + flightEnd;
			}
		}

		return flightDuration;
	}

	public static List<String> getNonFinaliseMonthlyDate(String startDate,
			String endDate) {
		System.out.println("Here...");
		List<String> nonFinaliseDateList = new ArrayList<String>();
		startDate = startDate.replaceAll("-", "_");
		endDate = endDate.replaceAll("-", "_");
		System.out
				.println("startDate:" + startDate + " and endDate:" + endDate);
		if (startDate.equals(endDate)) {
			nonFinaliseDateList.add(startDate);
		} else {
			String currentDate = DateUtil.getCurrentTimeStamp("yyyy_MM_dd");

			try {
				String startDateStr = DateUtil.getModifiedDateStringByDays(
						currentDate,
						-(LinMobileConstants.CHANGE_WINDOW_SIZE - 1),
						"yyyy_MM_dd");
				System.out.println("startDateStr:" + startDateStr);
				while (!startDateStr.equals(endDate)) {
					if (!nonFinaliseDateList.contains(startDateStr)) {
						System.out.println("added...");
						nonFinaliseDateList.add(startDateStr);
					}
					startDateStr = DateUtil.getModifiedDateStringByDays(
							startDateStr, 1, "yyyy_MM_dd");
					System.out.println("startDateStr:222:" + startDateStr);
				}
				if (!nonFinaliseDateList.contains(endDate)) {
					nonFinaliseDateList.add(endDate);
				}

			} catch (Exception e) {
				System.out.println(e.toString());
				nonFinaliseDateList = null;
			}
		}

		return nonFinaliseDateList;
	}

	public static String getModifiedDateByMonths(String dateStr, int months,
			String format) throws Exception {
		Date date = DateUtil.getFormatedDate(dateStr, format);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		String modifiedDate = sdf.format(c.getTime());
		return modifiedDate;
	}

	public static void JSONParseDemo() {
		String jsonStr = "{\"kind\": \"adexchangeseller#report\", \"totalMatchedRows\": \"646\","
				+ " \"headers\": [{   \"name\": \"DATE\",   \"type\": \"DIMENSION\"  },"
				+ "{   \"name\": \"AD_CLIENT_ID\",   \"type\": \"DIMENSION\"  }]"
				+ " , \"rows\":"
				+ "[ [   \"2013-06-18\",   \"ca-pub-6761535370094960\",   \"ca-pub-6761535370094960:5636863130\",   \"LIN Mobile | Smartphone | 320x50\",   \"University of San Francisco\",   \"0\",   \"0.11\",   \"6\",   \"6\",   \"0\",   null,   \"0.00\"  ], "
				+ "[   \"2013-06-18\",   \"ca-pub-6761535370094960\",   \"ca-pub-6761535370094960:5636863130\",   \"LIN Mobile | Smartphone | 320x50\",   \"UEI College\",   \"0\",   \"0.56\",   \"3\",   \"3\",   \"0\",   null,   \"0.00\"  ]"
				+ "] }";
		System.out.println("jsonStr:" + jsonStr);
		JSONObject nexageSiteDetails = (JSONObject) JSONSerializer
				.toJSON(jsonStr);

		JSONArray jsonArray = nexageSiteDetails.getJSONArray("rows");
		System.out.println("jsonArray.size():" + jsonArray.size());
		if (jsonArray.size() == 0) {
			System.out.println("===================");
		} else {
			for (int i = 0; i < jsonArray.size(); i++) {
				System.out.println("hi.....");
				JSONArray subArray = jsonArray.getJSONArray(i);
				System.out.println("hi...................subArray:"
						+ subArray.size());
				for (int j = 0; j < subArray.size(); j++) {
					System.out.println("hi.....222" + subArray.getString(j));

				}

			}
		}
	}

	public static String readCSVFile(String url) throws IOException {
		StringBuffer dataBuffer = new StringBuffer();

		HttpURLConnection connection;
		if (LinMobileConstants.PROXY_URL != null) {
			connection = (HttpURLConnection) new URL("https",
					LinMobileConstants.PROXY_URL, 443, url).openConnection(); // For
																				// MA
																				// local
																				// machine
		} else {
			connection = (HttpURLConnection) new URL(url).openConnection(); // For
																			// AppEngine
		}
		connection.setRequestProperty("content-type", "binary/data");
		InputStream response = connection.getInputStream();

		BufferedReader rd = new BufferedReader(new InputStreamReader(response));
		String line;
		while ((line = rd.readLine()) != null) {
			dataBuffer.append(line);
		}
		System.out.println("==========================");
		BufferedInputStream inputStream = new BufferedInputStream(
				new URL(url).openStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		CSVReader csvReader = new CSVReader(reader);
		List<String[]> allElements = csvReader.readAll();
		System.out.println("allElements:" + allElements.size());
		for (String csvLine[] : allElements) {
			System.out.println("line[0]" + csvLine[0]);
		}

		return dataBuffer.toString();
	}

	public static void modifyListDuringIteration() {
		List<String> aList = new ArrayList<String>();
		aList.add("1");
		aList.add("2");
		aList.add("3");
		aList.add("4");

		int i = 0;
		for (String a : aList) {
			if (a.equals("3")) {
				aList.remove(i);
			}
			i++;
		}
		System.out.println(aList);
		System.out
				.println("*************2nd using iterator********************");
		Iterator<String> itr = aList.iterator();

		while (itr.hasNext()) {
			String element = itr.next();
			if ("2".equals(element)) {
				itr.remove();
			}
		}
		System.out.println(aList);
	}

	public void downloadReport() throws Exception {
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(new NetHttpTransport())
				.setJsonFactory(new GsonFactory())
				.setServiceAccountId(LinMobileVariables.SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountScopes(
						ImmutableList.of("https://www.googleapis.com/auth/dfp"))
				.setServiceAccountPrivateKeyFromP12File(
						new File(
								"C:/Users/user/git/linmobile-dev/src/main/resources/env/keys/"
										+ LinMobileVariables.SERVICE_ACCOUNT_KEY))
				.build();
		credential.refreshToken();

		// Construct a DfpSession.
		DfpSession dfpSession = new DfpSession.Builder()
				.withNetworkCode("5678")
				.withApplicationName(
						LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME)
				.withOAuth2Credential(credential).build();

		List<String> orderIdList = new ArrayList<String>();
		String[] arr = new String[] { "24659502" ,"25870542" ,"26561262","26902062","26931702","27484902","28013982","28020822","28876182","29080302","29087742","29362902","29363022","29444622","29549382","29552022","29579502","29579622","29627742","29681982","30177102","30728142","31250982","31572822","31572942","32483382","32590782","32590902","32829702","33206142","33503142","33503262","33503382","34000182","34000302","35545782","35703102","36098502","36110862","36470382","36470502","36638142","36811782","37747182","38299062","38648022","39611142","40346142","41246382","41418582","41580342","41580702","42039462","42784062","42784902","43043982","43437342","43592742","44056182","44058702","44800542","45060342","45337782","45495342","45512982","45524022","45953382","46585662","47319582","47595222","47600622","48008982","48381702","48384702","48462582","49058022","49670622","49916742","50038422","50038542","50038902","50477622","50629422","51143022","51150702","51416982","51533742","52090542","52090662","52285422","52530822","52641222","52757742","52856022","52856142","53299542","53378382","53390982","54006342","54006462","54366222","54503382","54503502","55187862","55451262","56067462","56214702","56363382","56534862","57378462","57803262","57998142","58534182","59066622","59865942","60022542","60024582","60174462","60754902","60903582","61217022","61217142","61993182","62283222","62886582","62886942","63035142","63772662","63971742","65351262"};
		orderIdList = Arrays.asList(arr);
//		IDFPReportService dfpReportService = new DFPReportService();
	//	com.google.api.ads.dfp.jaxws.factory.DfpServices dfpServices  = LinMobileProperties.getInstance().getDfpServices();
	//	String downloadUrl =   dfpReportService.getDFPReportByLocationByAccountIds(dfpServices, dfpSession, "2014-11-17", "2014-11-17", orderIdList);
	// return;
		
		Dimension[] dimensionArray = new Dimension[] { Dimension.DATE,
				Dimension.ADVERTISER_ID, Dimension.ADVERTISER_NAME,
				/*
				 * Dimension.AD_UNIT_ID, Dimension.AD_UNIT_NAME,
				 */
				Dimension.ORDER_ID, Dimension.ORDER_NAME,
				Dimension.LINE_ITEM_ID, Dimension.LINE_ITEM_NAME,
				Dimension.LINE_ITEM_TYPE, Dimension.COUNTRY_CRITERIA_ID,
				Dimension.REGION_CRITERIA_ID, Dimension.CITY_CRITERIA_ID,
				Dimension.COUNTRY_NAME, Dimension.REGION_NAME,
				Dimension.CITY_NAME
		};

		Column[] columnArray = new Column[] { Column.AD_SERVER_IMPRESSIONS,
				Column.AD_SERVER_CLICKS,
				Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM, Column.AD_SERVER_CTR,
				Column.AD_SERVER_CPM_AND_CPC_REVENUE,
				Column.AD_SERVER_CPD_REVENUE, Column.AD_SERVER_ALL_REVENUE,
				// Column.AD_SERVER_DELIVERY_INDICATOR,
				Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM };

		DimensionAttribute[] dimAttributeArray = new DimensionAttribute[] {
				DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
				DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
				DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
				DimensionAttribute.LINE_ITEM_COST_TYPE,
				DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
				DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
				DimensionAttribute.LINE_ITEM_START_DATE_TIME,
				DimensionAttribute.LINE_ITEM_END_DATE_TIME,
				DimensionAttribute.ORDER_AGENCY,
				DimensionAttribute.ORDER_AGENCY_ID,
				DimensionAttribute.ORDER_LIFETIME_CLICKS,
				DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
				DimensionAttribute.ORDER_PO_NUMBER,
				DimensionAttribute.ORDER_START_DATE_TIME,
				DimensionAttribute.ORDER_END_DATE_TIME,
				DimensionAttribute.ORDER_TRAFFICKER };
		String start = "2014-11-17";
		String end = "2014-11-17";
		String[] startArray = start.split("-");
		String[] endArray = end.split("-");
		com.google.api.ads.dfp.axis.v201403.ReportQuery reportQuery = new com.google.api.ads.dfp.axis.v201403.ReportQuery();
		reportQuery.setDateRangeType(com.google.api.ads.dfp.axis.v201403.DateRangeType.CUSTOM_DATE);
		// reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);

		com.google.api.ads.dfp.axis.v201403.Date startDate = new com.google.api.ads.dfp.axis.v201403.Date();

		startDate.setDay(Integer.parseInt(startArray[2]));
		startDate.setMonth(Integer.parseInt(startArray[1]));
		startDate.setYear(Integer.parseInt(startArray[0]));

		com.google.api.ads.dfp.axis.v201403.Date endDate = new com.google.api.ads.dfp.axis.v201403.Date();
		endDate.setDay(Integer.parseInt(endArray[2]));
		endDate.setMonth(Integer.parseInt(endArray[1]));
		endDate.setYear(Integer.parseInt(endArray[0]));

		reportQuery.setStartDate(startDate);
		reportQuery.setEndDate(endDate);
		
		Statement statement = new Statement();
		StringBuffer query = new StringBuffer();
		query.append(" WHERE ADVERTISER_ID IN ( ");

		for (int i = 0; i < orderIdList.size(); i++) {
			if (i == 0) {
				query.append(orderIdList.get(i));
			} else {
				query.append(",");
				query.append(orderIdList.get(i));
			}
		}
		query.append(" )");

		statement.setQuery(query.toString());
		reportQuery.setStatement(statement);
		/****** using statement code ends *******/

		reportQuery.setAdUnitView(com.google.api.ads.dfp.axis.v201403.ReportQueryAdUnitView.HIERARCHICAL);
		List<Dimension> list = new ArrayList<Dimension>();
		list.addAll(Arrays.asList(dimensionArray));
		
		reportQuery.setDimensions(dimensionArray);
		reportQuery.setColumns(columnArray);
		reportQuery.setDimensionAttributes(dimAttributeArray);
		DfpServices dfpServices1 = new DfpServices();
		ReportServiceInterface reportService = dfpServices1.get(dfpSession,
				ReportServiceInterface.class);

		ReportJob reportJob = new ReportJob();
		reportJob.setReportQuery(reportQuery);

		reportJob = reportService.runReportJob(reportJob);

		// Create report downloader.
		final ReportDownloader reportDownloader = new ReportDownloader(
				reportService, reportJob.getId());

		reportDownloader.whenReportReady(new ReportCallback() {
			public void onSuccess() {
				// Change to your file location.
				String filePath;
				try {
					filePath = File.createTempFile("async-report-",
							".csv.gz", new File("c:\\testdownload"))
							.toString();
					System.out.printf("Downloading report to %s ...",
							filePath);

					// Download the report.
					reportDownloader.downloadReport(ExportFormat.CSV_DUMP,
							filePath);

					System.out.println("Async process completed.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub
				System.out.println("failed");
			}

			@Override
			public void onInterruption() {
				// TODO Auto-generated method stub
				System.out.println("interrupted");
			}

			@Override
			public void onException(Exception e) {
				e.printStackTrace();

			}
		});
	
		System.out.println("Completed... async may be going on.");
	}

	public void downloadReport1() {
		try {
			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(new NetHttpTransport())
					.setJsonFactory(new GsonFactory())
					.setServiceAccountId(
							LinMobileVariables.SERVICE_ACCOUNT_EMAIL)
					.setServiceAccountScopes(
							ImmutableList
									.of("https://www.googleapis.com/auth/dfp"))
					.setServiceAccountPrivateKeyFromP12File(
							new File(
									"C:/Users/user/git/linmobile-dev/src/main/resources/env/keys/"
											+ LinMobileVariables.SERVICE_ACCOUNT_KEY))
					.build();
			credential.refreshToken();

			// Construct a DfpSession.
			DfpSession dfpSession = new DfpSession.Builder()
					.withNetworkCode("4206")
					.withApplicationName(
							LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME)
					.withOAuth2Credential(credential).build();

			List<String> orderIds = new ArrayList<String>();
			orderIds.add("214471819");
			String start = "2014-09-01";
			String end = "2014-10-30";

			String[] startArray = start.split("-");
			String[] endArray = end.split("-");

			// Create report query.
			com.google.api.ads.dfp.axis.v201403.ReportQuery reportQuery = new com.google.api.ads.dfp.axis.v201403.ReportQuery();
			reportQuery
					.setDateRangeType(com.google.api.ads.dfp.axis.v201403.DateRangeType.CUSTOM_DATE);
			// reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);

			com.google.api.ads.dfp.axis.v201403.Date startDate = new com.google.api.ads.dfp.axis.v201403.Date();

			startDate.setDay(Integer.parseInt(startArray[2]));
			startDate.setMonth(Integer.parseInt(startArray[1]));
			startDate.setYear(Integer.parseInt(startArray[0]));

			com.google.api.ads.dfp.axis.v201403.Date endDate = new com.google.api.ads.dfp.axis.v201403.Date();
			endDate.setDay(Integer.parseInt(endArray[2]));
			endDate.setMonth(Integer.parseInt(endArray[1]));
			endDate.setYear(Integer.parseInt(endArray[0]));

			reportQuery.setStartDate(startDate);
			reportQuery.setEndDate(endDate);

			Dimension[] dimensionArray = new Dimension[] { Dimension.DATE,
					Dimension.ADVERTISER_ID, Dimension.ADVERTISER_NAME,
					Dimension.AD_UNIT_ID, Dimension.AD_UNIT_NAME,
					Dimension.ORDER_ID, Dimension.ORDER_NAME,
					Dimension.LINE_ITEM_ID, Dimension.LINE_ITEM_NAME,
					Dimension.LINE_ITEM_TYPE, Dimension.CREATIVE_ID,
					Dimension.CREATIVE_NAME, Dimension.CREATIVE_SIZE,
					Dimension.CREATIVE_TYPE, Dimension.SALESPERSON_NAME };

			Column[] columnArray = new Column[] { Column.AD_SERVER_IMPRESSIONS,
					Column.AD_SERVER_CLICKS,
					Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
					Column.AD_SERVER_CTR, Column.AD_SERVER_CPM_AND_CPC_REVENUE,
					Column.AD_SERVER_CPD_REVENUE, Column.AD_SERVER_ALL_REVENUE,
					Column.AD_SERVER_DELIVERY_INDICATOR,
					Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,
					Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
					Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
					Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
					Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
					Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
					Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
					Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE };

			DimensionAttribute[] dimAttributeArray = new DimensionAttribute[] {
					DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
					DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
					DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
					DimensionAttribute.LINE_ITEM_COST_TYPE,
					DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
					DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
					DimensionAttribute.LINE_ITEM_START_DATE_TIME,
					DimensionAttribute.LINE_ITEM_END_DATE_TIME,
					DimensionAttribute.ORDER_AGENCY,
					DimensionAttribute.ORDER_AGENCY_ID,
					DimensionAttribute.ORDER_LIFETIME_CLICKS,
					DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
					DimensionAttribute.ORDER_PO_NUMBER,
					DimensionAttribute.ORDER_START_DATE_TIME,
					DimensionAttribute.ORDER_END_DATE_TIME,
					DimensionAttribute.ORDER_TRAFFICKER };

			/****** using statement code starts *******/
			Statement statement = new Statement();
			StringBuffer query = new StringBuffer();
			query.append(" WHERE ORDER_ID IN ( ");

			for (int i = 0; i < orderIds.size(); i++) {
				if (i == 0) {
					query.append(orderIds.get(i));
				} else {
					query.append(",");
					query.append(orderIds.get(i));
				}
			}
			query.append(" )");

			statement.setQuery(query.toString());
			reportQuery.setStatement(statement);
			/****** using statement code ends *******/

			reportQuery
					.setAdUnitView(com.google.api.ads.dfp.axis.v201403.ReportQueryAdUnitView.HIERARCHICAL);
			List<Dimension> list = new ArrayList<Dimension>();
			list.addAll(Arrays.asList(dimensionArray));
			reportQuery.setDimensions(dimensionArray);
			reportQuery.setColumns(columnArray);
			reportQuery.setDimensionAttributes(dimAttributeArray);
			DfpServices dfpServices = new DfpServices();
			ReportServiceInterface reportService = dfpServices.get(dfpSession,
					ReportServiceInterface.class);

			ReportJob reportJob = new ReportJob();
			reportJob.setReportQuery(reportQuery);

			reportJob = reportService.runReportJob(reportJob);

			// Create report downloader.
			final ReportDownloader reportDownloader = new ReportDownloader(
					reportService, reportJob.getId());

			reportDownloader.whenReportReady(new ReportCallback() {
				public void onSuccess() {
					// Change to your file location.
					String filePath;
					try {
						filePath = File.createTempFile("async-report-",
								".csv.gz", new File("c:\\testdownload"))
								.toString();
						System.out.printf("Downloading report to %s ...",
								filePath);

						// Download the report.
						reportDownloader.downloadReport(ExportFormat.CSV_DUMP,
								filePath);

						System.out.println("done.");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onInterruption() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onException(Exception e) {
					// TODO Auto-generated method stub

				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void loadDFPReportTest() throws Exception {

		ForcastDTO forecastDTO = null;
		if (forecastDTO == null) {

			DfpSession dfpSession;
			try {
				GoogleCredential credential = new GoogleCredential.Builder()
						.setTransport(new NetHttpTransport())
						.setJsonFactory(new GsonFactory())
						.setServiceAccountId(
								LinMobileVariables.SERVICE_ACCOUNT_EMAIL)
						.setServiceAccountScopes(
								ImmutableList
										.of("https://www.googleapis.com/auth/dfp"))
						.setServiceAccountPrivateKeyFromP12File(
								new File(
										"C:/Users/user/git/linmobile-dev/src/main/resources/env/keys/"
												+ LinMobileVariables.SERVICE_ACCOUNT_KEY))
						.build();
				credential.refreshToken();

				// Construct a DfpSession.
				dfpSession = new DfpSession.Builder()
						.withNetworkCode("4206")
						.withApplicationName(
								LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME)
						.withOAuth2Credential(credential).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void bqTest(){/*
		TableRow row = new TableRow();
		row.set("column_name", 7.7);
		TableDataInsertAllRequest.Rows rows = new TableDataInsertAllRequest.Rows();
		rows.setInsertId(timestamp);
		rows.setJson(row);
		List  rowList =
		    new ArrayList();
		rowList.add(rows);
		TableDataInsertAllRequest content = 
		    new TableDataInsertAllRequest().setRows(rowList);
		TableDataInsertAllResponse response =
		    bigquery.tabledata().insertAll(
		        projectId, datasetId, tableId, content).execute();
	*/}
	public static void main(String[] args) throws Exception {
		String[] s = new String[]{"1","2","2","1"};
		HashSet set = new HashSet<String>();
		set.addAll(Arrays.asList(s));
		System.out.println(set.toArray(new String[set.size()]));
		//new Test().downloadReport();
	}

}
