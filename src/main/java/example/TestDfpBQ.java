package example;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.v201403.OrderPage;
import com.google.api.ads.dfp.jaxws.v201403.OrderServiceInterface;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.Order;
import com.google.api.ads.dfp.jaxws.v201403.ReportServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Statement;
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
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IHistoricalReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.HistoricalReportService;
import com.lin.web.util.DFPTableSchemaUtil;
import com.lin.web.util.DataLoaderUtil;
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
		public static void main(String[] args) throws Exception {
			//testDownload();
	 		DfpServices dfpServices = LinMobileProperties.getInstance().getDfpServices();
			OrderServiceInterface orderService = dfpServices.get(getDfpSession(), OrderServiceInterface.class);
			 Statement statement=new Statement();	    
		     StringBuffer query=new StringBuffer();
		     query.append(" WHERE ID IN ( 181012302,181140222)");     
		    
		     log.info("PQL Query : "+query.toString());
		     statement.setQuery(query.toString());		   
			OrderPage page = orderService.getOrdersByStatement(statement);
			for(Order order : page.getResults()){
				System.out.println("impressions ["+order.getTotalImpressionsDelivered()+"]  clicks["+order.getTotalClicksDelivered()+"] ctr["+
							new Double((order.getTotalClicksDelivered()*100/order.getTotalImpressionsDelivered()))
						+ "]");
			} 
		}
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
						LinMobileConstants.LIN_MOBILE_DFP_APPLICATION_NAME)
				.withOAuth2Credential(credential).build();
		DfpServices dfpServices = LinMobileProperties.getInstance()
				.getDfpServices();

		IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator
				.locate(IHistoricalReportService.class);
		IDFPReportService dfpReportService = new DFPReportService();
		List<String> orderIdList = new ArrayList<String>();
		String[] arr = new String[] { "182141742"/*,"181401702","132015822","232241909","166591062","181694022","181385022","181557222","226432349","179537382","181140222","176849502","180634662","182125662","181909782","179527422","181601982","181266222","180444222","181285782","181656342","181268742","179039742","151688862","182137182","249206549","181283262","181239342","181647222","180970542","181328502","174463302",
	*/ };
		orderIdList = Arrays.asList(arr);

		String downloadUrl = dfpReportService.getDFPTargetReportByOrderIds(dfpServices, dfpSession, "2014-12-01", "2015-02-10",
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
	
	
	
	public static void copyTable(String tableFrom, String tableTo, final String whereclause)throws Exception{


		String projectId = LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
 

		Bigquery bigQuery = getBigQuery();

		Job job = new Job();
		JobConfiguration config = new JobConfiguration();
		 
		JobConfigurationQuery query = new JobConfigurationQuery();
		query.setWriteDisposition("WRITE_TRUNCATE");
		query.setQuery(" SELECT * FROM "+tableFrom+" "+ (whereclause == null ? "" : whereclause));
		
		DatasetReference dref = new DatasetReference();
		dref.setDatasetId("LIN_DEV");
		dref.setProjectId(projectId);
		query.setDefaultDataset(dref);

		
		TableReference tableRef = new TableReference();
		tableRef.setDatasetId("LIN_DEV");
		tableRef.setTableId(tableTo); // lin_dev_test_core_perf_3
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
	
	
	
	 	
 
	public static String[] getAllProjects(){ return null;}
	
	public static String[] getAllDataSets(String projectId){ return null;}
	
	public static String[] getAllTables(String projectId, String datasetId){ return null;}

	public static void copyDatasetTablesInOneProject(String projectId, String dataSetId, String destinationDataSetId){
		
	}
	
	public static Bigquery getBigQuery(){
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
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}

		Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY,
				credentials)
				.setApplicationName("BigQuery-Service-Accounts/0.1")
				.setHttpRequestInitializer(credentials).build();
		return bigQuery;
	}

	public static void testFinalise(String publisherId, Bigquery bigQuery) throws Exception{ /*
		Date currentDate=new Date();
		String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-LinMobileConstants.CHANGE_WINDOW_SIZE, "yyyy-MM-dd");
	    String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    System.out.println("start ["+startDate+"] month ["+month+"]");
	    CloudProjectDTO cloudDto = DataLoaderUtil.getCloudProjectDTO(publisherId);
	    
	    String[] loadTypes = new String[]{LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE,LinMobileConstants.LOAD_TYPE_LOCATION,
	    		LinMobileConstants.LOAD_TYPE_TARGET,LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT,LinMobileConstants.LOAD_TYPE_RICH_MEDIA};
	    for(int i=0;i<loadTypes.length;i++){
	    	String schemaName = DataLoaderUtil.getSchemaNameByLoadType(loadTypes[i]);
	    	String readyToMergeTableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
	        String finaliseTableId=schemaName+"_"+month.replaceAll("-", "_");
	      boolean success =   BigQueryUtil.copyTable(bigQuery, readyToMergeTableId, 
	        		finaliseTableId, 
	        		"", 
	        		"LIN", 
	        		"LIN", 
	        		cloudDto.getBigQueryServiceAccountEmail(), 
	        		cloudDto.getBigQueryServicePrivateKey(), 
	        		cloudDto.getBigQueryProjectId(), true);
	      if(success){
	    	  System.out.println("Done for "+ loadTypes[i]);
	    	//  BigQueryUtil.deleteTableFromBigQuery(cloudDto.getBigQueryServiceAccountEmail(), 
		      //  		cloudDto.getBigQueryServicePrivateKey(), 
		       // 		cloudDto.getBigQueryProjectId(), 	LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID,  readyToMergeTableId)  ;
	      }else{
	    	  System.out.println("failed for "+ loadTypes[i]);
	      }
	    }
	
	*/}
	


public static DfpSession getDfpSession() throws ValidationException, GeneralSecurityException, IOException{
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
return dfpSession;
}	
}
