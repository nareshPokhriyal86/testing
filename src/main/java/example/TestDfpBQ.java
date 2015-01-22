package example;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.Bigquery.Jobs.Insert;
import com.google.api.services.bigquery.Bigquery.Tables.Get;
import com.google.api.services.bigquery.model.DatasetReference;
import com.google.api.services.bigquery.model.ErrorProto;
import com.google.api.services.bigquery.model.Job;
import com.google.api.services.bigquery.model.JobConfiguration;
import com.google.api.services.bigquery.model.JobConfigurationExtract;
import com.google.api.services.bigquery.model.JobConfigurationLink;
import com.google.api.services.bigquery.model.JobConfigurationLoad;
import com.google.api.services.bigquery.model.JobConfigurationQuery;
import com.google.api.services.bigquery.model.JobReference;
import com.google.api.services.bigquery.model.JobStatus;
import com.google.api.services.bigquery.model.Table;
import com.google.api.services.bigquery.model.TableDataInsertAllRequest;
import com.google.api.services.bigquery.model.TableDataInsertAllResponse;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.common.collect.ImmutableList;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.web.action.AdSdkAction;
import com.lin.web.action.HistoricalDataLoaderAction;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IHistoricalReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.HistoricalReportService;
import com.lin.web.util.DFPTableSchemaUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ProcessQueryBuilder;

public class TestDfpBQ {
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static List<String> SCOPES = new ArrayList<String>();
	  private static final Logger log = Logger.getLogger(TestDfpBQ.class.getName());  

	public static String testDownload() throws ApiException_Exception,
			InterruptedException, GeneralSecurityException, IOException,
			ValidationException {

		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(new NetHttpTransport())
				.setJsonFactory(new GsonFactory())
				.setServiceAccountId(LinMobileVariables.SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountScopes(
						ImmutableList.of("https://www.googleapis.com/auth/dfp"))
				.setServiceAccountPrivateKeyFromP12File(
						new File(
								"C:/Users/user/git/linmobile-dev/src/main/webapp/keys/"
										+ LinMobileVariables.SERVICE_ACCOUNT_KEY))
				.build();
		credential.refreshToken();

		// Construct a DfpSession.
		DfpSession dfpSession = new DfpSession.Builder()
				.withNetworkCode("5678")
				.withApplicationName(
						LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME)
				.withOAuth2Credential(credential).build();
		DfpServices dfpServices = LinMobileProperties.getInstance()
				.getDfpServices();

		IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator
				.locate(IHistoricalReportService.class);
		IDFPReportService dfpReportService = new DFPReportService();
		List<String> orderIdList = new ArrayList<String>();
		String[] arr = new String[] { "15752622", "24659502", "25870542",
				"26561262", "26902062", "26931702", "27484902", "28013982",
				"28020822", "28876182", "29080302", "29087742", "29362902",
				/*"29363022", "29444622", "29549382", "29552022", "29579502",
				"29579622", "29627742", "29681982", "30177102", "30728142",
				"31250982", "31572822", "31572942", "32483382", "32590782",
				"32590902", "32829702", "33206142", "33503142", "33503262",
				"33503382", "34000182", "34000302", "35545782", "35703102",
				"36098502", "36110862", "36470382", "36470502", "36638142",
				"36811782", "37747182", "38299062", "38648022", "39611142",
				"40346142", "41246382", "41418582", "41580342", "41580702",
				"42039462", "42784062", "42784902", "43043982", "43437342",
				"43592742", "44056182", "44058702", "44800542", "45060342",
				"45337782", "45495342", "45512982", "45524022", "45953382",
				"46585662", "47319582", "47595222", "47600622", "48008982",
				"48381702", "48384702", "48462582", "49058022", "49670622",
				"49916742", "50038422", "50038542", "50038902", "50477622",
				"50629422", "51143022", "51150702", "51416982", "51533742",
				"52090542", "52090662", "52285422", "52530822", "52641222",
				"52757742", "52856022", "52856142", "53299542", "53378382",
				"53390982", "54006342", "54006462", "54366222", "54503382",
				"54503502", "55187862", "55451262", "56067462", "56214702",
				"56363382", "56534862", "57378462", "57803262", "57998142",
				"58534182", "59066622", "59865942", "60022542", "60024582",
				"60174462", "60754902", "60903582", "61217022", "61217142",
				"61993182", "62283222", "62886582", "62886942", "63035142",
				"63772662", "63971742", "65351262", "65915742", "66040182",*/
				"66043662", "66044742" };
		orderIdList = Arrays.asList(arr);

		String downloadUrl = dfpReportService.getDFPReportByAccountIds(
				dfpServices, dfpSession, "2014-12-04", "2014-12-04",
				orderIdList);
		System.out.println(downloadUrl);
return downloadUrl;
	}

	private static void loadCorePerfDataFromDFP() throws Exception {

		String projectId = LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;

		SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
		GoogleCredential credentials = new GoogleCredential.Builder()
				.setTransport(TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(
						LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)
				.setServiceAccountScopes(SCOPES)
				.setServiceAccountPrivateKeyFromP12File(
						new File(
								"C:/Users/user/git/linmobile-dev/src/main/webapp/keys/"
										+ LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))
				.build();

		Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY,
				credentials)
				.setApplicationName("BigQuery-Service-Accounts/0.1")
				.setHttpRequestInitializer(credentials).build();
		String queryStatus = null;

		Job job = new Job();
		JobConfiguration config = new JobConfiguration();
		JobConfigurationLoad loadConfig = new JobConfigurationLoad();
		loadConfig.setWriteDisposition("WRITE_TRUNCATE");
		// loadConfig.setCreateDisposition("CREATE_IF_NEEDED");
		config.setLoad(loadConfig);

		job.setConfiguration(config);

		// Set where you are importing from (i.e. the Google Cloud Storage
		// paths).
		List<String> sources = new ArrayList<String>();
		sources.add("gs://dev_linmobile/inbox/advanced/2014_12/CorePerformance_2014_12_15_2014_12_15_1418727807876.gz");
		loadConfig.setSourceUris(sources);
		// FileContent content = new FileContent("application/octet-stream", new
		// File(fileName));
		// Describe the resulting table you are importing to:

		TableReference tableRef = new TableReference();

		tableRef.setDatasetId("LIN_DEV");
		tableRef.setTableId("a_raw_coreperformance_2014_12_15_2014_12_15_new");
		tableRef.setProjectId(projectId);
		loadConfig.setDestinationTable(tableRef);
		loadConfig.setAllowJaggedRows(true);
		TableSchema schema = DFPTableSchemaUtil.getRawSchema("common");
		if (schema == null) {
			System.out.println("no schem found...");
			queryStatus = "Failed: Schema not found.";
		} else {
			loadConfig.setSchema(schema);
			System.out.println("Going to insert data....");
			//loadConfig.setSkipLeadingRows(1);

			Insert insert = bigQuery.jobs().insert(projectId, job);
			insert.setProjectId(projectId);

			job = insert.execute();

			JobReference jobRef = job.getJobReference();
			JobStatus jobStatus = job.getStatus();
			String state = jobStatus.getState();
			System.out.println("after insert : state:" + state);
			String jobId = jobRef.getJobId();
			System.out.println("after insert : JobId: " + jobId);
			
			try {
				job = checkQueryResults(bigQuery, projectId, jobRef);
				jobStatus = job.getStatus();
				state = jobStatus.getState();
				log.info("after checkQueryResults(): state:" + state);
				if (jobStatus.getErrorResult() != null) {
					queryStatus = jobStatus.getErrorResult().getMessage();
					for(ErrorProto proto: jobStatus.getErrors()){
						log.info("Proto Message ["+proto.getMessage()+"] ");
						log.info("Proto Reason ["+proto.getReason()+"] ");
					}
					
					log.info("Error ::queryStatus::" + queryStatus);
				} else {
					queryStatus = "Success";
				}

			} catch (InterruptedException e) {
				log.severe("InterruptedException:" + e.getMessage());
				queryStatus = e.getMessage();
				
			} 

		}
		
		}

	 

	public static void copyRawToProcTableBQ()throws Exception{


		String projectId = LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;

		SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
		GoogleCredential credentials = new GoogleCredential.Builder()
				.setTransport(TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(
						LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)
				.setServiceAccountScopes(SCOPES)
				.setServiceAccountPrivateKeyFromP12File(
						new File(
								"C:/Users/user/git/linmobile-dev/src/main/webapp/keys/"
										+ LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))
				.build();

		Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY,
				credentials)
				.setApplicationName("BigQuery-Service-Accounts/0.1")
				.setHttpRequestInitializer(credentials).build();

		Job job = new Job();
		JobConfiguration config = new JobConfiguration();
		 
		JobConfigurationQuery query = new JobConfigurationQuery();
		query.setWriteDisposition("WRITE_APPEND");
		query.setQuery(ProcessQueryBuilder.getCorePerformanceRawToProcQuery(null, "5678","1", "LinMedia", "[LIN_DEV.a_raw_coreperformance_2014_12_15_2014_12_15_new]", null));

		DatasetReference dref = new DatasetReference();
		dref.setDatasetId("LIN_RAW");
		query.setDefaultDataset(dref);
		TableReference tableRef = new TableReference();
		tableRef.setDatasetId("LIN_RAW");
		tableRef.setTableId("raw_test_coreperformance"); // lin_dev_test_core_perf_3
		tableRef.setProjectId(projectId);

		query.setDestinationTable(tableRef);
		 
		config.setQuery(query);

	 

		System.out.println("Going to insert data....");
		job.setConfiguration(config);
		Insert insert = bigQuery.jobs().insert(projectId, job);
		insert.setProjectId(projectId);

		job = insert.execute();


		 
		JobReference jobRef = job.getJobReference();
		JobStatus jobStatus = job.getStatus();
		String state = jobStatus.getState();
		System.out.println("after insert : state:" + state);
		String jobId = jobRef.getJobId();
		System.out.println("after insert : JobId: " + jobId);

		 Job completedJob = checkQueryResults1(bigQuery, projectId, jobRef);	 

	
		
	}
	
	private static Job checkQueryResults1(Bigquery bigquery, String projectId,
			JobReference jobId) throws IOException, InterruptedException {
		// Variables to keep track of total query time
		long startTime = System.currentTimeMillis();
		long elapsedTime;

		while (true) {
			Job pollJob = bigquery.jobs().get(projectId, jobId.getJobId())
					.execute();
			elapsedTime = System.currentTimeMillis() - startTime;
			log.info("Job status :"+pollJob.getStatus().getState()+", jobId:"+jobId.getJobId()+", Time:"+elapsedTime+"\n");
			if (pollJob.getStatus().getState().equals("DONE")) {
				if(pollJob.getStatus().getErrors() != null){
					for(ErrorProto proto : pollJob.getStatus().getErrors()){
						log.severe(proto.getDebugInfo());
					}
				}
				return pollJob;
			}
		
			Thread.sleep(2000);
		}
	}

	public static void jsonUploadBQ()throws Exception{


		String projectId = LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;

		SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
		GoogleCredential credentials = new GoogleCredential.Builder()
				.setTransport(TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(
						LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)
				.setServiceAccountScopes(SCOPES)
				.setServiceAccountPrivateKeyFromP12File(
						new File(
								"C:/Users/user/git/linmobile-dev/src/main/webapp/keys/"
										+ LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))
				.build();

		Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY,
				credentials)
				.setApplicationName("BigQuery-Service-Accounts/0.1")
				.setHttpRequestInitializer(credentials).build();
		String json = "{\"key\":\"\",\"timeStamp\":\"2014-12-17 16:21:10\",\"make\":\"\",\"model\":\"\",\"idfa\":\"\",\"idfv\":\"\",\"androidId\":\"weurhuie432ee\",\"googleAdId\":\"\",\"deviceId\":\"er483232bewd83d\",\"odin\":\"\",\"pushNotificationId\":\"\",\"deviceParam\":{\"os\":\"iOS\",\"osVersion\":\"8.1.2\",\"imei\":\"hehd3ueiwhdh932ed\",\"meid\":\"\",\"serialId\":\"\",\"multiSim\":true,\"networkParams\":{\"wifiTimeStamp\":\"2014-12-17 16:21:10\",\"wifiMacAddress\":\"\",\"wifiName\":\"\",\"wifiIpAddress\":\"192.168.1.10\",\"type\":\"wifi\",\"connectionProvider\":[{\"timeStamp\":\"2014-12-17 16:21:10\",\"sim\":\"sim1\",\"mcc\":\"404\",\"mnc\":\"10\",\"timezone\":\"\",\"networkConnection\":{\"timeStamp\":\"2014-12-17 16:21:10\",\"macAddress\":\"er:33:e3:w2:33:45\",\"ipAddress\":\"10.0.3.13\",\"type\":\"hspda\"},\"geoParams\":[{\"timeStamp\":\"2014-12-17 16:21:10\",\"latitue\":\"34.98475\",\"longitude\":\"45.88477\",\"cellId\":\"302\",\"lac\":\"34564\"},{\"timeStamp\":\"2014-12-17 16:21:10\",\"latitue\":\"34.98475\",\"longitude\":\"45.88477\",\"cellId\":\"302\",\"lac\":\"34564\"}]}]},\"userParams\":{\"language\":\"en\",\"appstoreid\":\"agrim@mediaagility.com\",\"country\":\"INDIA\",\"firstName\":\"\",\"lastName\":\"\"}}}";
		//new AdSdkAction().parseJsonAndSave(json, bigQuery);
	  
		/*
		
		TableSchema schema =  DFPTableSchemaUtil.getProcSchema("adSdkBQSchema");

		Table table = new Table();
		table.setSchema(schema);
		TableReference tableRef = new TableReference();
		tableRef.setDatasetId("LIN_DEV");
		tableRef.setProjectId(projectId);
		tableRef.setTableId("Lin_Mobile_Ad_Sdk_Data");
		table.setTableReference(tableRef);
		try {
		    bigQuery.tables().insert(projectId, "LIN_DEV", table).execute();
		} catch (IOException e) {
			
		}
*/
		 
	 
		System.out.println("Done");
	
		
	}
	private static void createTableInBQ() throws GeneralSecurityException, IOException{/*
		String projectId = LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;

		SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
		GoogleCredential credentials = new GoogleCredential.Builder()
				.setTransport(TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(
						LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)
				.setServiceAccountScopes(SCOPES) 
				.setServiceAccountPrivateKeyFromP12File(new File("C:/Users/user/git/linmobile-dev/src/main/webapp/keys/"
										+ LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))
				.build();

		Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY,
				credentials)
				.setApplicationName("BigQuery-Service-Accounts/0.1")
				.setHttpRequestInitializer(credentials).build();
		TableSchema schema =  DFPTableSchemaUtil.getProcSchema("adSdkBQSchema");

		Table table = new Table();
		table.setSchema(schema);
		TableReference tableRef = new TableReference();
		tableRef.setDatasetId("LIN");
		tableRef.setProjectId(projectId);
		tableRef.setTableId("Lin_Mobile_Ad_Sdk_Data");
		table.setTableReference(tableRef);
		try {
		    bigQuery.tables().insert(projectId, "LIN", table).execute();
		} catch (IOException e) {
			
		}
	*/}
	public static void main(String[] args) throws Exception {
		Bigquery bigQuery = getBigQuery();
		bigQuery.datasets().delete("", "").setDeleteContents(true);
		
	}

	private static TableSchema generateSchema() {

		String[][] schemaFields = new String[][] {
				{ "Dimension_DATE", "STRING" },
				{ "Dimension_ADVERTISER_ID", "STRING" },
				{ "Dimension_ADVERTISER_NAME", "STRING" },
				{ "Ad_unit_ID_1", "STRING" },
				{ "Ad_unit_ID_2", "STRING" },
				{ "Ad_unit_1", "STRING" },
				{ "Ad_unit_2", "STRING" },
				{ "Dimension_ORDER_ID", "STRING" },
				{ "Dimension_ORDER_NAME", "STRING" },
				{ "Dimension_LINE_ITEM_ID", "STRING" },
				{ "Dimension_LINE_ITEM_NAME", "STRING" },
				{ "Dimension_LINE_ITEM_TYPE", "STRING" },
				{ "Dimension_CREATIVE_ID", "STRING" },
				{ "Dimension_CREATIVE_NAME", "STRING" },
				{ "Dimension_CREATIVE_SIZE", "STRING" },
				{ "Dimension_CREATIVE_TYPE", "STRING" },
				{ "Dimension_SALESPERSON_NAME", "STRING" },
				{ "Dimension_SALESPERSON_ID", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_GOAL_QUANTITY", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_CONTRACTED_QUANTITY", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_COST_PER_UNIT", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_COST_TYPE", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_LIFETIME_CLICKS", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_LIFETIME_IMPRESSIONS", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_START_DATE_TIME", "STRING" },
				{ "DimensionAttribute_LINE_ITEM_END_DATE_TIME", "STRING" },
				{ "DimensionAttribute_ORDER_AGENCY", "STRING" },
				{ "DimensionAttribute_ORDER_AGENCY_ID", "STRING" },
				{ "DimensionAttribute_ORDER_LIFETIME_CLICKS", "STRING" },
				{ "DimensionAttribute_ORDER_LIFETIME_IMPRESSIONS", "STRING" },
				{ "DimensionAttribute_ORDER_PO_NUMBER", "STRING" },
				{ "DimensionAttribute_ORDER_START_DATE_TIME", "STRING" },
				{ "DimensionAttribute_ORDER_END_DATE_TIME", "STRING" },
				{ "DimensionAttribute_ORDER_TRAFFICKER", "STRING" },
				{ "Column_AD_SERVER_IMPRESSIONS", "STRING" },
				{ "Column_AD_SERVER_CLICKS", "STRING" },
				{ "Column_AD_SERVER_WITH_CPD_AVERAGE_ECPM", "STRING" },
				{ "Column_AD_SERVER_CTR", "STRING" },
				{ "Column_AD_SERVER_CPM_AND_CPC_REVENUE", "STRING" },
				{ "Column_AD_SERVER_CPD_REVENUE", "STRING" },
				{ "Column_AD_SERVER_ALL_REVENUE", "STRING" },
				{ "Column_AD_SERVER_DELIVERY_INDICATOR", "STRING" },
				{ "Column_AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM", "STRING" },
				{ "Column_AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS",
						"STRING" },
				{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM", "STRING" },
				{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS", "STRING" },
				{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_CTR", "STRING" },
				{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS", "STRING" },
				{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS",
						"STRING" },
				{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE", "STRING" },

		};
		List<TableFieldSchema> tableFieldSchema = new ArrayList<TableFieldSchema>();
		for (String[] strArr : schemaFields) {

			TableFieldSchema schemaEntry = new TableFieldSchema();
			schemaEntry.setName(strArr[0]);
			schemaEntry.setType(strArr[1]);

			tableFieldSchema.add(schemaEntry);

		}
		TableSchema schema = new TableSchema();
		schema.setFields(tableFieldSchema);

		// TODO Auto-generated method stub
		return schema;
	}
	private static Job checkQueryResults(Bigquery bigquery, String projectId,
			JobReference jobId) throws IOException, InterruptedException {
		// Variables to keep track of total query time
		long startTime = System.currentTimeMillis();
		long elapsedTime;

		while (true) {
			Job pollJob = bigquery.jobs().get(projectId, jobId.getJobId())
					.execute();
			elapsedTime = System.currentTimeMillis() - startTime;
			if (pollJob.getStatus().getState().equals("DONE")) {
				return pollJob;
			}
		
			Thread.sleep(2000);
		}
	}
	private static Bigquery getBigQuery(){
		SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
		GoogleCredential credentials = null;
		try {
			credentials = new GoogleCredential.Builder()
					.setTransport(TRANSPORT)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId(
							LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)
					.setServiceAccountScopes(SCOPES)
					.setServiceAccountPrivateKeyFromP12File(
							new File(
									"C:/Users/user/git/linmobile-dev/src/main/webapp/keys/"
											+ LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))
					.build();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}

		Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY,
				credentials)
				.setApplicationName("BigQuery-Service-Accounts/0.1")
				.setHttpRequestInitializer(credentials).build();
		return bigQuery;
	}

	 
}
