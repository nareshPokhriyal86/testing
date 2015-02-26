package com.lin.web.gaebigquery;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.Bigquery.Jobs.Insert;
import com.google.api.services.bigquery.model.DatasetReference;
import com.google.api.services.bigquery.model.ErrorProto;
import com.google.api.services.bigquery.model.GetQueryResultsResponse;
import com.google.api.services.bigquery.model.Job;
import com.google.api.services.bigquery.model.JobConfiguration;
import com.google.api.services.bigquery.model.JobConfigurationLoad;
import com.google.api.services.bigquery.model.JobConfigurationQuery;
import com.google.api.services.bigquery.model.JobReference;
import com.google.api.services.bigquery.model.JobStatus;
import com.google.api.services.bigquery.model.QueryRequest;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.Table;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableDataInsertAllRequest;
import com.google.api.services.bigquery.model.TableDataInsertAllResponse;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableList;
import com.google.api.services.bigquery.model.TableList.Tables;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.ReadBigQuerySchemaDTO;
import com.lin.web.service.IQueryService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.HistoricalReportService;
import com.lin.web.util.CloudStorageUtil;
import com.lin.web.util.DFPTableSchemaUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ProcessQueryBuilder;

public class BigQueryUtil {

	  
	  private static final HttpTransport TRANSPORT = new NetHttpTransport();
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	  private static final Logger log = Logger.getLogger(BigQueryUtil.class.getName());  
	  private static List<String> SCOPES=null;
	  private static Bigquery bigquery;
	  private static GoogleCredential credential;
	  
	  private static Map<String,GoogleCredential> credentialMap=new HashMap<String,GoogleCredential>();
	  private static Map<String,Bigquery> bigQueryMap=new HashMap<String,Bigquery>();
	  
	  static String WRITE_TRUNCATE="WRITE_TRUNCATE";
	  
	  /*
	   * @author Youdhveer Panwar
	   * This method will initialize bigquery service
	   */
	  public static void initBigQueryData() {
		  if(SCOPES==null){
		    	SCOPES=new ArrayList<String>();
		    	SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
		  }
		
		  if(credential==null){
		    	try {   
					credential = new GoogleCredential.Builder().setTransport(TRANSPORT)
					     .setJsonFactory(JSON_FACTORY)
						 .setServiceAccountId(LinMobileConstants.GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)		                            
						 .setServiceAccountScopes(SCOPES)
						 //.setServiceAccountPrivateKeyFromP12File(new File("892d0834292bff62001a53a17624d1b22f1002a3-privatekey.p12"))
						 .setServiceAccountPrivateKeyFromP12File(new File("keys/"+LinMobileConstants.GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))						 
						 .build();
					
					 
				} catch (GeneralSecurityException e) {
					credential=null;
					bigquery=null;
					log.severe("initBigQueryData:GeneralSecurityException:"+e.getMessage());
					
				} catch (IOException e) {
					credential=null;
					bigquery=null;
					log.severe("initBigQueryData:IOException:"+e.getMessage());
					
				}
		  }
		  
		  if(bigquery==null){
		    	bigquery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName("BigQuery-Service-Accounts/0.1")
					.setHttpRequestInitializer(credential).build();
		  }
		    
		  
	  }
	  
	  
	  /*
	   * @author Youdhveer Panwar
	   * This method will initialise bigquery service based on service accountId
	   * @param  String serviceAccountId,
	   *         String privateKey
	   */
	  public static void initBigQueryData(String serviceAccountId, String privateKey) {
		  
		  if(SCOPES==null){
		    	SCOPES=new ArrayList<String>();
		    	SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
		  }
		  
		  GoogleCredential credentials=null;
		  
		  if(!credentialMap.containsKey(serviceAccountId)){
			  synchronized (BigQueryUtil.class) {
				  if(!credentialMap.containsKey(serviceAccountId)){
			  log.info("create big query credential and add in credentialMap..");
		    	try {
		    		credentials = new GoogleCredential.Builder().setTransport(TRANSPORT)
					     .setJsonFactory(JSON_FACTORY)
						 .setServiceAccountId(serviceAccountId)		                            
						 .setServiceAccountScopes(SCOPES)
						 .setServiceAccountPrivateKeyFromP12File(new File("keys/"+privateKey))						 
						 .build();
					
					credentialMap.put(serviceAccountId, credentials);
					 
				} catch (GeneralSecurityException e) {
					log.severe("initBigQueryData:GeneralSecurityException:"+e.getMessage());
					//
				} catch (IOException e) {
					log.severe("initBigQueryData:IOException:"+e.getMessage());
					//
				}
				  }
			  }
		  }
		  
		  if(!bigQueryMap.containsKey(serviceAccountId)){
			  synchronized (BigQueryUtil.class) {
				  if(!bigQueryMap.containsKey(serviceAccountId)){
					log.info("create big query object and add in bigQueryMap..");
					credentials = credentialMap.get(serviceAccountId);
					Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credentials)
												.setApplicationName("BigQuery-Service-Accounts/0.1")
												.setHttpRequestInitializer(credentials).build();
			  
					bigQueryMap.put(serviceAccountId, bigQuery);
				  }		    
			  }
		  }
	  }
	  
	  
	  public static QueryResponse getBigQueryData(String queryStr) throws IOException {
		  
		  initBigQueryData();
		  
		  String projectNumber =LinMobileConstants.GOOGLE_API_PROJECT_ID; //"mediaagility.com:ma-maps";
		  QueryRequest queryRequest = new QueryRequest().setQuery(queryStr);
		  Bigquery.Jobs.Query query = bigquery.jobs().query(projectNumber, queryRequest);
		  QueryResponse queryResponse = query.execute();
		  
		  /*JobReference jobRef = queryResponse.getJobReference();
		  Job job = checkQueryResults(bigquery,projectNumber,jobRef);		
		  String state=job.getStatus().getState();*/

		  return  queryResponse;

	  }	  
	  
     public static QueryResponse getBigQueryData(QueryDTO queryDTO) throws IOException {
    	  String queryStr=queryDTO.getQueryData();
    	  String serviceAccountId=queryDTO.getServiceAccountEmail();
    	  String privateKey=queryDTO.getServicePrivateKey();
    	  String projectId=queryDTO.getBigQueryProjectId();
    	  
		  initBigQueryData(serviceAccountId, privateKey);
		  
		  QueryRequest queryRequest = new QueryRequest().setQuery(queryStr);
		  //queryRequest.setUseQueryCache(true);
		  Bigquery bigQueryService=bigQueryMap.get(serviceAccountId);
		  
		  Bigquery.Jobs.Query query = bigQueryService.jobs().query(projectId, queryRequest);
		  String quotaUser=query.getQuotaUser();
		  //System.out.println("==========quotaUser================================="+quotaUser);
		  QueryResponse queryResponse = query.execute();
		  
		  //Boolean cacheHit=queryResponse.getCacheHit();
		  
		  return  queryResponse;

	  }	  
     
	  public static String saveData(String path,String schemaFile,String tableId) throws GeneralSecurityException, IOException{
		   String jobId=storeDataToBigQuery(path,schemaFile,tableId);
	       return jobId;
	  }
	  
	  public static String storeDataToBigQuery(String path, String schemaFile,String tableId) 
			  throws GeneralSecurityException, IOException{
		  
		      String queryStatus=null;
		      initBigQueryData();
		      Job job = new Job();
		      JobConfiguration config = new JobConfiguration();
		      JobConfigurationLoad loadConfig = new JobConfigurationLoad();
		      config.setLoad(loadConfig);
		      job.setConfiguration(config);
		      // Set where you are importing from (i.e. the Google Cloud Storage paths).
		      List<String> sources = new ArrayList<String>();
		    
		      sources.add(path);
		      loadConfig.setSourceUris(sources);
		      //FileContent content = new FileContent("application/octet-stream", new File(fileName));
		      // Describe the resulting table you are importing to:
		      TableReference tableRef = new TableReference();
		      tableRef.setDatasetId(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
		      tableRef.setTableId(tableId);
		      tableRef.setProjectId(LinMobileConstants.GOOGLE_API_PROJECT_ID);
		      loadConfig.setDestinationTable(tableRef);
		      
		      TableSchema schema=generateSchemaFromCloud(schemaFile);
		      if(schema ==null){
		    	  log.warning("no schem found...");
		    	  queryStatus="Failed: Schema not found.";
		      }else{
		    	  loadConfig.setSchema(schema);
		    	  log.info("Going to insert data....");
			      loadConfig.setSkipLeadingRows(1);
			  	  Insert insert = bigquery.jobs().insert(LinMobileConstants.GOOGLE_API_PROJECT_ID, job);		  	  
			      insert.setProjectId(LinMobileConstants.GOOGLE_API_PROJECT_ID);
			      
			      job=insert.execute();
			     
			      JobReference jobRef = job.getJobReference();
			      JobStatus jobStatus= job.getStatus();
			      String state=jobStatus.getState();
			      log.info("after insert : state:"+state);
			      String jobId = jobRef.getJobId();		      
			  	  log.info("after insert : JobId: "+jobId);
			  	  
			  	  try {
					job=checkQueryResults(bigquery, LinMobileConstants.GOOGLE_API_PROJECT_ID, jobRef);
					jobStatus= job.getStatus();
					state=jobStatus.getState();
				    log.info("after checkQueryResults(): state:"+state);
				    if(jobStatus.getErrorResult() !=null){
				    	  queryStatus=jobStatus.getErrorResult().getMessage();
					      log.info("Error ::queryStatus::"+queryStatus);
				    }else{
				    	  queryStatus="Success";
				    }
				    
				  } catch (InterruptedException e) {
				 	log.severe("InterruptedException:"+e.getMessage());
				 	queryStatus=e.getMessage();
					
				  }			      
			  	 
		      }
		     return queryStatus;   
	}

	  public static String uploadRawGzipFileToBigQuery(String path,String tableId, String loadType, String serviceAccountId, String serviceAccountKey, String projectId, String dataSetId) throws GeneralSecurityException,IOException {

		String queryStatus = null;
		initBigQueryData(serviceAccountId, serviceAccountKey);
		Bigquery bigQuery=bigQueryMap.get(serviceAccountId);
		Job job = new Job();
		JobConfiguration config = new JobConfiguration();
		JobConfigurationLoad loadConfig = new JobConfigurationLoad();
		loadConfig.setWriteDisposition("WRITE_TRUNCATE");
		//loadConfig.setCreateDisposition("CREATE_IF_NEEDED");
		config.setLoad(loadConfig);
		job.setConfiguration(config);
		
		log.info("Adding filepath to bq localconfig = ["+path+"]");
 		List<String> sources = new ArrayList<String>();
		sources.add(path);
		loadConfig.setSourceUris(sources);
		
		TableReference tableRef = new TableReference();
		tableRef.setDatasetId(dataSetId);
		tableRef.setTableId(tableId);
		tableRef.setProjectId(projectId);
		loadConfig.setDestinationTable(tableRef);
		loadConfig.setAllowJaggedRows(true);

		// Always create raw table with common load type.
		TableSchema schema = DFPTableSchemaUtil.getRawSchema(LinMobileConstants.LOAD_TYPE_COMMON);
		if (schema == null) {
			log.warning("no schem found...");
			queryStatus = "Failed: Schema not found.";
		} else {
			loadConfig.setSchema(schema);
			log.info("Going to insert data....");
			//Dont skip headers... we require them to convert to actual columns
			//loadConfig.setSkipLeadingRows(1);
			Insert insert = bigQuery.jobs().insert(projectId, job);
			insert.setProjectId(projectId);

			job = insert.execute();

			JobReference jobRef = job.getJobReference();
			JobStatus jobStatus = job.getStatus();
			String state = jobStatus.getState();
			log.info("after insert : state:" + state);
			String jobId = jobRef.getJobId();
			log.info("after insert : JobId: " + jobId);

 	try {
				job = checkQueryResults(bigQuery, projectId, jobRef);
				jobStatus = job.getStatus();
				state = jobStatus.getState();
				log.info("after checkQueryResults(): state:" + state);
				if (jobStatus.getErrorResult() != null) {
					log.info("Error ::queryStatus::" + queryStatus);
				} else {
					queryStatus = "Success";
				}

			} catch (InterruptedException e) {
				log.severe("InterruptedException:" + e.getMessage());
				queryStatus = e.getMessage();
				
			} 

		}
		return queryStatus;
	}
	  
	  public static String uploadAdSdkDTOData(String[][] strings) throws GeneralSecurityException,IOException {
		  	log.info("Going to insert data. column size ["+strings.length+"]");
			initBigQueryData(LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS, LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY);
			Bigquery bigQuery=bigQueryMap.get(LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS);
			log.info("Initialized Bigquery");
			return uploadAdSdkDTOData(strings, bigQuery);
		}
	  public static String uploadAdSdkDTOData(String[][] strings, Bigquery bigQuery) throws IOException{

			TableRow row = new TableRow();
			for(String[] str: strings){
				row.set(str[0]+"", str[1]+"");
			}
			row.set("id", strings[0][1]+"_"+ new Date().getTime());
			log.info("Row preparation done.");
			
			TableDataInsertAllRequest.Rows rows = new TableDataInsertAllRequest.Rows();
			rows.setInsertId(new Date().getTime()+"");
			rows.setJson(row);
			List<TableDataInsertAllRequest.Rows>  rowList =new ArrayList<TableDataInsertAllRequest.Rows>();
			rowList.add(rows);
			TableDataInsertAllRequest content = new TableDataInsertAllRequest().setRows(rowList);
			log.info("Going to insert to bigquery.");
			TableDataInsertAllResponse response = bigQuery.tabledata().insertAll(
					LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID, LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID,
					LinMobileConstants.LIN_MOBILE_AD_SDK_BQ_TABLE_ID, content).execute();
			log.info("Row inserted to bigquery. Response error(if any, should be null)  is ["+response.getInsertErrors()+"]");
			return "success" ;
	  }
	  
	
	
	
	public static TableSchema generateSchemaFromCloud(String schemaFile) {
			  log.info("Generating schema on bigquery...");
			  String schemaFileName =null;
			  if(schemaFile!=null){
				  schemaFileName = schemaFile;
			  }
		      List<TableFieldSchema> schemaList = new ArrayList<TableFieldSchema>();
		      List<ReadBigQuerySchemaDTO> schemaDTO = new ArrayList<ReadBigQuerySchemaDTO>();
		      schemaDTO = CloudStorageUtil.readBigQuerySchemaCSVFromCloudStorage(schemaFileName,LinMobileVariables.APPLICATION_BUCKET_NAME);
		      if(schemaDTO !=null){
		    	  Iterator<ReadBigQuerySchemaDTO> iterator = schemaDTO.iterator();
			      ReadBigQuerySchemaDTO obj;
			      while(iterator.hasNext()){
			    	  obj = iterator.next();
			    	  //log.info("column name :"+obj.getColumnName()+"  column type : "+obj.getColumnType());
			    	  schemaList.add(new TableFieldSchema().setName(obj.getColumnName())
			    	                                       .setType(obj.getColumnType()));
			      }
			      TableSchema schema = new TableSchema();
			      schema.setFields(schemaList);
	             
			      return schema;
		      }else{
		    	  return null;
		      }
		      
	 }
	
	public static TableSchema generateSchemaFromCloud(String schemaFile,String bucketName) {
		  log.info("Generating schema on bigquery...schemaFile:"+schemaFile+" and bucketName:"+bucketName);
		  String schemaFileName =null;
		  if(schemaFile!=null){
			  schemaFileName = schemaFile;
		  }
	      List<TableFieldSchema> schemaList = new ArrayList<TableFieldSchema>();
	      List<ReadBigQuerySchemaDTO> schemaDTO = new ArrayList<ReadBigQuerySchemaDTO>();
	      schemaDTO = CloudStorageUtil.readBigQuerySchemaCSVFromCloudStorage(schemaFileName,bucketName);
	      if(schemaDTO !=null){
	    	  Iterator<ReadBigQuerySchemaDTO> iterator = schemaDTO.iterator();
		      ReadBigQuerySchemaDTO obj;
		      while(iterator.hasNext()){
		    	  obj = iterator.next();
		    	  //log.info("column name :"+obj.getColumnName()+"  column type : "+obj.getColumnType());
		    	  schemaList.add(new TableFieldSchema().setName(obj.getColumnName())
		    	                                       .setType(obj.getColumnType()));
		      }
		      TableSchema schema = new TableSchema();
		      schema.setFields(schemaList);
           
		      return schema;
	      }else{
	    	  return null;
	      }
	      
}
	
	/*
	 * @author Youdhveer Panwar
	 * @param String responseURL- Which contains complete cloud storage path of CSV file
	 *        String schemaFile,
	 *        String tableId
	 *        
	 * @return String bigQueryJobResponse - BigQueryResponse
	 *         
	 */
	public static String uploadDataOnBigQuery(String responseURL,String schemaFile,String tableId){
		String bigQueryJobResponse=null;
		if(responseURL !=null && responseURL.contains(".csv")){
			try {				
				log.info("Before saving data in bigquery");
				bigQueryJobResponse=saveData(responseURL, schemaFile, tableId);
				log.info("bigQueryJobResponse:"+bigQueryJobResponse);
				responseURL="Cloud Storage path:"+responseURL+" And BigQuery JobId:"+bigQueryJobResponse;
				String message="";
				String subject="";
				if(bigQueryJobResponse!=null && bigQueryJobResponse.equals("Success")){
					subject="Data uploaded successfully on BigQuery for tableId:"+tableId;
					message="Data uploaded on BigQuery. \n Please see responseURL:"+responseURL;
				}else{
					subject="Failed to upload data on BigQuery for tableId:"+tableId;
					message="Failed to upload data on BigQuery. \n Please see responseURL:"+responseURL;
					LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);
				}		
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			} catch (GeneralSecurityException e) {
				log.severe("Exception in saving data in bigquery: GeneralSecurityException:"+e.getMessage());
				
			}
		}else{
			log.warning("Cron job failed : Sending mail...");
			String subject="Cron job failed : responseURL - "+responseURL;
			String message="Please check the log, DFP cron job has been falied. schemaFile:"+schemaFile+" tableId:"+tableId+" and responseURL:"+responseURL;
			LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS,
					LinMobileConstants.TO_EMAIL_ADDRESS,LinMobileConstants.CC_EMAIL_ADDRESS, 
					subject, message);
		}	
		return responseURL;
	}
	
	/*
	 * Upload data in bigquery
	 * 
	 * @author Youdhveer Panwar
	 * @param String responseURL- Which contains complete cloud storage path of CSV file
	 *        boolean truncateFirst,
	 *        String schemaFile,
	 *        String tableId
	 *        
	 * @return String bigQueryJobResponse - BigQueryResponse
	 *         
	 */
	public static String uploadDataOnBigQuery(String responseURL,boolean truncateFirst,String schemaFile,String tableId){
		String bigQueryJobResponse=null;
		if(responseURL !=null && responseURL.contains(".csv")){
			try {				
				log.info("Before saving data in bigquery");
				if(truncateFirst){
					bigQueryJobResponse=truncateAndSaveData(responseURL, schemaFile, tableId);
				}else{
					bigQueryJobResponse=saveData(responseURL, schemaFile, tableId);	
				}
				
				log.info("bigQueryJobResponse:"+bigQueryJobResponse);
				responseURL="Cloud Storage path:"+responseURL+" And BigQuery JobId:"+bigQueryJobResponse;
				String message="";
				String subject="";
				if(bigQueryJobResponse!=null && bigQueryJobResponse.equals("Success")){
					subject="Data uploaded successfully on BigQuery for tableId:"+tableId;
					message="Data uploaded on BigQuery. \n Please see responseURL:"+responseURL;
				}else{
					subject="Failed to upload data on BigQuery for tableId:"+tableId;
					message="Failed to upload data on BigQuery. \n Please see responseURL:"+responseURL;
					LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);
				}		
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			} catch (GeneralSecurityException e) {
				log.severe("Exception in saving data in bigquery: GeneralSecurityException:"+e.getMessage());
				
			}
		}else{
			log.warning("Cron job failed : Sending mail...");
			String subject="Cron job failed : responseURL - "+responseURL;
			String message="Please check the log, DFP cron job has been falied. schemaFile:"+schemaFile+" tableId:"+tableId+" and responseURL:"+responseURL;
			LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS,
					LinMobileConstants.TO_EMAIL_ADDRESS,LinMobileConstants.CC_EMAIL_ADDRESS, 
					subject, message);
		}	
		return responseURL;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * This method will delete a table from bigquery and create again to insert fresh data
	 * @param String path, String schemaFile, String tableId
	 * 
	 */
	public static String truncateAndSaveData(String path,String schemaFile,String tableId) throws GeneralSecurityException, IOException{
		log.info("truncateAndSaveData-- tableId:"+tableId);
		initBigQueryData();
		deleteTable(bigquery,LinMobileConstants.GOOGLE_API_PROJECT_ID,LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID,tableId);
     	String jobId=storeDataToBigQuery(path,schemaFile,tableId);
     	return jobId;
    }
	
	/*
	 * @author Youdhveer Panwar
	 * This method will delete a table from bigquery
	 * @param Bigquery service, String projectId, String datasetId, String tableId
	 * 
	 */
	public static boolean deleteTable(Bigquery service, String projectId, String datasetId, String tableId){
		try {
			service.tables().delete(projectId, datasetId, tableId).execute();
			log.info("Table deleted successfully..tableId:"+tableId);
			return true;
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			
			return false;
		}			    
	}
	
	/*
	 * @author Youdhveer Panwar
	 * This method will delete a table from bigquery
	 * @param Bigquery service, String projectId, String datasetId, String tableId
	 * 
	 */
	public static boolean deleteTableFromBigQuery(String serviceAccountId,String privateKey,
			String projectId, String datasetId, String tableId){
		try {			
			initBigQueryData(serviceAccountId, privateKey);
			Bigquery bigQuery=bigQueryMap.get(serviceAccountId);			
			bigQuery.tables().delete(projectId, datasetId, tableId).execute();
			log.info("Table deleted successfully..tableId:"+tableId);
			return true;
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			
			return false;
		}			    
	}
	

/*  ********************************************************************************************************************************** */
	
	
	/*
	 * @author Youdhveer Panwar
	 * This method will delete a table from bigquery
	 * @param String serviceAccountId,String privateKey, String projectId, String datasetId, String tableId
	 * 
	 */
	public static String deleteTable(String serviceAccountId,String privateKey, String projectId, String datasetId,
			String tableId){
		String response="";
		try {
			initBigQueryData(serviceAccountId, privateKey);
			Bigquery bigQueryService=bigQueryMap.get(serviceAccountId);
			bigQueryService.tables().delete(projectId, datasetId, tableId).execute();
			log.info("Table deleted successfully..tableId:"+tableId);
			response="Success";
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			//
			response="Failed : "+e.getMessage();
		}	
		return response;
	}
	
	
	/*
	 * @author Youdhveer Panwar
	 * This method will delete a table from bigquery
	 * @param String serviceAccountId,String privateKey, String projectId, String datasetId, String tableId
	 * 
	 */
	public static boolean doesTableExist(String serviceAccountId,String privateKey, String projectId, String datasetId,
			String tableId){
		boolean response=false;
		try {
			initBigQueryData(serviceAccountId, privateKey);
			Bigquery bigQueryService=bigQueryMap.get(serviceAccountId);
			bigQueryService.tables().get(projectId, datasetId, tableId).execute();
			log.info("Table exist tableId:"+tableId);
			response=true;
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			//
			response=false;
		}	
		return response;
	}
	
		
	/**
	 * @author Manish Mudgal
	 * This method will create a table if already does not exist
	 * @param
	 * 
	 */
	public static boolean createTableIfNotExists(String serviceAccountId,String privateKey, String projectId, String datasetId, String tableId, String loadType){
		boolean response=false;
		try {
			if(doesTableExist(serviceAccountId, privateKey, projectId, datasetId, tableId)){
				return true;
			}
			initBigQueryData(serviceAccountId, privateKey);
			
			TableSchema schema =  DFPTableSchemaUtil.getProcSchema(loadType);
			Table table = new Table();
			table.setSchema(schema);
			TableReference tableRef = new TableReference();
			tableRef.setDatasetId(datasetId);
			tableRef.setProjectId(projectId);
			tableRef.setTableId(tableId);
			table.setTableReference(tableRef);
			
			Bigquery bigQuery =bigQueryMap.get(serviceAccountId);
			bigQuery.tables().insert(projectId, datasetId, table).execute();
			log.info("Table created with tableId:"+tableId);
			response=true;
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			//
			response=false;
		}	
		return response;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * This method will get all tables from bigquery
	 * @param String serviceAccountId,String privateKey, String projectId, String datasetId, String tableId
	 * 
	 */
	public static List<String> getTables(String serviceAccountId,String privateKey, String projectId, String datasetId){
		com.google.api.services.bigquery.Bigquery.Tables.List tableLists=null;		
		List<String> tableList=new ArrayList<String>();
		try {
			initBigQueryData(serviceAccountId, privateKey);
			Bigquery bigQueryService=bigQueryMap.get(serviceAccountId);
			tableLists=bigQueryService.tables().list(projectId, datasetId);
			
			String pageToken=tableLists.getPageToken();
			TableList tableListPage = tableLists.execute();
			List<Tables> tables=tableListPage.getTables();
						
			String nextPageToken=tableListPage.getNextPageToken();			
			while(nextPageToken !=null){
				//log.info("==== nextPageToken : "+nextPageToken);
				tableLists.setPageToken(nextPageToken);
				
				tableListPage = tableLists.execute();
				List<Tables> moreTables=tableListPage.getTables();
				//log.info("moreTables :: "+moreTables);
				if(moreTables !=null){
					tables.addAll(moreTables);
				}				
				nextPageToken=tableListPage.getNextPageToken();
			}
			
			for(Tables table:tables){
				String tableId=table.getId();				
				tableList.add(tableId);
			}		
			log.info("tableList:" + tableList.size());
			
			
			
			
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			
		}	
		
		return tableList;
	}
	
	
	/*
	 *  Upload data in bigquery
	 * 
	 * @author Youdhveer Panwar
	 * @param String sourceFileList- List of csv files containing complete cloud storage path of CSV file
	 *        boolean truncateFirst,
	 *        String schemaFile,
	 *        String tableId
	 *        String projectId, 
	 *        String tableDataSet, 
	 *        String serviceAccountId,
	 *        String privateKey
	 * @return String bigQueryJobResponse - BigQueryResponse      
	 */
	 
	public static String uploadDataOnBigQuery(List<String> sourceFileList,boolean truncateFirst,String schemaFile, 
			String tableId, String projectId, String dataSetId, String serviceAccountId,String privateKey,String companyId){
		String bigQueryJobResponse=null;
		
		initBigQueryData(serviceAccountId, privateKey);
		String responseURL="";
		if(sourceFileList !=null && sourceFileList.size()>0){
			try {
				Bigquery bigQuery=bigQueryMap.get(serviceAccountId);
				log.info("Before saving data in bigquery.."+bigQuery.getApplicationName());
			  
				Map<String,CloudProjectDTO> cloudProjectMap=LinMobileProperties.getInstance().getCloudProjectDTOMap();
			    log.info("cloudProjectMap:"+cloudProjectMap.size());
			    if(cloudProjectMap.size()==0){
			    	throw new Exception("Cloud Project is not found in LinMobileProperties.");
			    }
			    CloudProjectDTO projectDTO=cloudProjectMap.get(companyId);
			    if(projectDTO == null){
			    	throw new Exception("Cloud Project is not found in LinMobileProperties.");
			    }else{
			    	log.info("projectDTO: "+projectDTO.toString());
			    	
			    	bigQueryJobResponse=storeDataToBigQuery(bigQuery, sourceFileList, schemaFile, tableId, projectId,
							dataSetId,truncateFirst,projectDTO.getCloudStorageBucket());
			    	
					log.info("bigQueryJobResponse:"+bigQueryJobResponse);
					responseURL="Cloud Storage path:sourceFileList:"+sourceFileList.size()+" And BigQuery JobId:"+bigQueryJobResponse;
					String message="";
					String subject="";
					if(bigQueryJobResponse!=null && bigQueryJobResponse.equals("Success")){
						subject="Data uploaded successfully on BigQuery for tableId:"+tableId+", projectId : "+projectId;
						message="Data uploaded on BigQuery. \n Please see responseURL:"+responseURL+"\ntableId : "+tableId+", projectId : "+projectId;
						
						String memcacheKey=projectId+"_"+MemcacheUtil.ALL_BQ_TABLES_KEY;
						log.info("Update all tables in memcache also..memcacheKey:"+memcacheKey);
						List<String> bqTableList=getTables(serviceAccountId, privateKey, projectId, dataSetId);
						MemcacheUtil.setDataListInCacheByKey(bqTableList, memcacheKey);
						MemcacheUtil.updateAllFinaliseNonFinaliseTables(projectId);
						
					}else{
						subject="Failed to upload data on BigQuery for tableId:"+tableId;
						message="Failed to upload data on BigQuery. \n Please see responseURL:"+responseURL+"\ntableId : "+tableId+", projectId : "+projectId;
						LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);
					}		
					
			    }
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				//
			} catch (GeneralSecurityException e) {
				log.severe("Exception in saving data in bigquery: GeneralSecurityException:"+e.getMessage());
				//
			} catch (Exception e) {
				log.severe("Exception--"+e.getMessage());
				bigQueryJobResponse=e.getMessage();
			}
		}else{
			log.warning("Cron job failed : Sending mail...");
			String subject="Cron job failed : There is no processed file to upload - ";
			String message="Please check the log, DFP cron job has been falied. schemaFile:"+schemaFile+" tableId:"+tableId+".\nThere is no processed file to upload:";
			LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS,
					LinMobileConstants.TO_EMAIL_ADDRESS,LinMobileConstants.CC_EMAIL_ADDRESS, 
					subject, message);
		}	
		return responseURL;
	}
		
		
	/*
	 * This method upload List of sources(csv files) from cloud storage to bigquery
	 * 
	 */
	public static String storeDataToBigQuery(Bigquery bigQuery,List<String> path, String schemaFile,String tableId,
			 String projectId,String dataSetId,boolean truncateTable,String bucketName) 
					 throws GeneralSecurityException, IOException{
		  
		      String queryStatus=null;
		      log.info("tableID  = "+tableId+" projectId = "+projectId);
		      Job job = new Job();
		      JobConfiguration config = new JobConfiguration();
		      JobConfigurationLoad loadConfig = new JobConfigurationLoad();		     
		      if(truncateTable){
		    	  log.info("Truncate table.."+WRITE_TRUNCATE);
		    	  loadConfig.setWriteDisposition(WRITE_TRUNCATE);
		      }		     
		      config.setLoad(loadConfig);
		      job.setConfiguration(config);
		      
		      // Set where you are importing from (i.e. the Google Cloud Storage paths).
		      List<String> sources =path;
		      loadConfig.setSourceUris(sources);
		      //FileContent content = new FileContent("application/octet-stream", new File(fileName));
		      // Describe the resulting table you are importing to:
		      TableReference tableRef = new TableReference();
		      tableRef.setDatasetId(dataSetId);
		      tableRef.setTableId(tableId);
		      tableRef.setProjectId(projectId);
		      loadConfig.setDestinationTable(tableRef);
		      
		      TableSchema schema=generateSchemaFromCloud(schemaFile,bucketName);
		      if(schema ==null){
		    	  log.warning("no schem found...");
		    	  queryStatus="Failed: Schema not found.";
		      }else{
		    	  loadConfig.setSchema(schema);
		    	  log.info("Going to insert data....");
			      loadConfig.setSkipLeadingRows(1);
			  	  Insert insert = bigQuery.jobs().insert(projectId, job);		  	  
			      insert.setProjectId(projectId);
			      
			      job=insert.execute();
			     
			      JobReference jobRef = job.getJobReference();
			      JobStatus jobStatus= job.getStatus();
			      String state=jobStatus.getState();
			      log.info("after insert : state:"+state);
			      String jobId = jobRef.getJobId();		      
			  	  log.info("after insert : JobId: "+jobId);
			  	  
			  	  try {
					job=checkQueryResults(bigQuery, projectId, jobRef);
					jobStatus= job.getStatus();
					state=jobStatus.getState();
				    log.info("after checkQueryResults(): state:"+state);
				    if(jobStatus.getErrorResult() !=null){
				    	  queryStatus=jobStatus.getErrorResult().getMessage();
					      log.severe("Error ::queryStatus::"+queryStatus);
				    }else{
				    	  queryStatus="Success";
				    }
				    
				  } catch (InterruptedException e) {
				 	log.severe("InterruptedException:"+e.getMessage());
				 	queryStatus=e.getMessage();
					
				  }			      
			  	 
		      }
		     return queryStatus;   
	}
	
	
	public static QueryDTO getQueryDTO(String publisherIdInBQ, String schema) {
		QueryDTO queryDTO = null;
		if(publisherIdInBQ != null && schema != null) {
				log.info("schema.."+schema);
				IQueryService queryService = (IQueryService) BusinessServiceLocator.locate(IQueryService.class);
				String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
				
				queryDTO = queryService.createQueryFromClause(publisherIdInBQ, "2013-01-01", currentDate, schema);
				if(queryDTO != null && !queryDTO.getQueryData().isEmpty()) {
					log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
				}
		}
		return queryDTO;
	}
	
	public static QueryDTO getQueryDTO(String fromDate, String publisherIdInBQ, String schema) {
		QueryDTO queryDTO = null;
		if(!(DateUtil.isDateFormatYYYYMMDD(fromDate))) {
			log.severe("Wrong format for fromDate : "+fromDate);
			return null;
		}
		if(publisherIdInBQ != null && schema != null) {
				log.info("schema.."+schema);
				IQueryService queryService = (IQueryService) BusinessServiceLocator.locate(IQueryService.class);
				String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
				
				queryDTO = queryService.createQueryFromClause(publisherIdInBQ, fromDate, currentDate, schema);
				if(queryDTO != null && !queryDTO.getQueryData().isEmpty()) {
					log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
				}
		}
		return queryDTO;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * This will create query DTO (Finalise and non-finalise tables list) based on parameters
	 */
	public static QueryDTO getQueryDTO(String startDate, String endDate, String publisherIdInBQ, String schema) {
		QueryDTO queryDTO = null;
		if(startDate==null){
			queryDTO=getQueryDTO(publisherIdInBQ, schema);
		}else if(startDate !=null && endDate==null){
			queryDTO=getQueryDTO(startDate, publisherIdInBQ, schema);
		}else{
			if(!(DateUtil.isDateFormatYYYYMMDD(startDate)) && !(DateUtil.isDateFormatYYYYMMDD(endDate))) {
				log.severe("Wrong format for startDate : "+startDate+" and endDate : "+endDate);
				return null;
			}
			if(publisherIdInBQ != null && schema != null) {
					log.info("schema.."+schema);
					IQueryService queryService = (IQueryService) BusinessServiceLocator.locate(IQueryService.class);
					String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
					long diff=DateUtil.getDifferneceBetweenTwoDates(currentDate, endDate, "yyyy-MM-dd");
					if(diff > 0){
						endDate=currentDate;
					}
					queryDTO = queryService.createQueryFromClause(publisherIdInBQ, startDate, endDate, schema);
					if(queryDTO != null && !queryDTO.getQueryData().isEmpty()) {
						log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
					}
			}
		}
		
		return queryDTO;
	}


	public static QueryDTO getGeoQueryDTO() {
		QueryDTO dto = new QueryDTO();
		dto.setBigQueryProjectId(LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID);
		dto.setServiceAccountEmail(LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS);
		dto.setServicePrivateKey(LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY);
		dto.setBigQueryDataSetId(LinMobileVariables.LINMEDIA_BIGQUERY_GEO_DATASET);
		
		dto.setQueryData(LinMobileVariables.LINMEDIA_BIGQUERY_GEO_DATASET + ".census");
		return dto;
	}
	
	public static QueryDTO getProductPerformanceDTO(){
		QueryDTO dto = new QueryDTO();
		dto.setBigQueryProjectId(LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID);
		dto.setServiceAccountEmail(LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS);
		dto.setServicePrivateKey(LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY);
		dto.setBigQueryDataSetId(LinMobileVariables.LINMEDIA_BIGQUERY_GEO_DATASET);
		return dto;
	}
	
	public static String processRawData(String serviceAccountId, String privateKey, String rawTableId, String processTableId, String loadType, String projectId , String datasetId, String networkCode, String publisherId, String publisherName , boolean merge) throws Exception{
		initBigQueryData(serviceAccountId, privateKey);
		Bigquery bigQuery =bigQueryMap.get(serviceAccountId);
		boolean tableExists = false;
		String destDatasetId = LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
		synchronized(BigQueryUtil.class){

		 tableExists = createTableIfNotExists(serviceAccountId, privateKey, projectId, destDatasetId, processTableId, loadType);
		}
		if(tableExists){
			String tableIdforQuery = " ["+datasetId+"."+rawTableId+"] ";
			HashMap<String, String> map = new HashMap<String, String>();
			String[] columns = getColumnNamesFromRawTable(serviceAccountId, privateKey, tableIdforQuery, projectId);
			if(columns == null || columns.length == 0){ return null ; }
			int counter = 0;
			for(String column: columns){
				map.put(column, "col_"+counter+"_0");
				counter ++ ;
			}
			String rawToProcQuery = ProcessQueryBuilder.getRawToProcQueryByLoadType(map, loadType, networkCode, publisherId, publisherName, tableIdforQuery);
			
			Job job = new Job();
			JobConfiguration config = new JobConfiguration();
			JobConfigurationQuery query = new JobConfigurationQuery();
			if(merge){
			query.setWriteDisposition("WRITE_APPEND");
			}else{
				query.setWriteDisposition("WRITE_TRUNCATE");
			}
			query.setQuery(rawToProcQuery);

			DatasetReference dref = new DatasetReference();
			dref.setDatasetId(datasetId);
			query.setDefaultDataset(dref);
			TableReference tableRef = new TableReference();
			
			tableRef.setDatasetId(destDatasetId);
			tableRef.setTableId(processTableId); 
			tableRef.setProjectId(projectId);
			query.setDestinationTable(tableRef);
			config.setQuery(query);
			log.info("Going to process raw data....");
			job.setConfiguration(config);
			Insert insert = bigQuery.jobs().insert(projectId, job);
			insert.setProjectId(projectId);

			job = insert.execute();

			JobReference jobRef = job.getJobReference();
			JobStatus jobStatus = job.getStatus();
			String state = jobStatus.getState();
			log.info("after job insert : state:" + state);
			String jobId = jobRef.getJobId();
			StringBuffer result = new StringBuffer();
			checkQueryResults(bigQuery, projectId, jobId, result);
			log.info("after raw to proc job insert : JobId: " + jobId);
			log.info("deleting raw table "+datasetId+"."+rawTableId);
			deleteTableFromBigQuery(serviceAccountId, privateKey, projectId, datasetId, rawTableId);
			log.info("deleted raw table "+datasetId+"."+rawTableId);
			return result.toString();
		}
		return null;
	}
	
	

	public static String[] getColumnNamesFromRawTable(String serviceAccountId, String privateKey, String rawTableId, String projectId) throws IOException, InterruptedException{
		return getColumnNamesFromRawTable(null, serviceAccountId, privateKey, rawTableId, projectId);
	}
	public static String[] getColumnNamesFromRawTable(Bigquery bigQuery, String serviceAccountId, String privateKey, String rawTableId, String projectId) throws IOException, InterruptedException{
		if(bigQuery == null){
		initBigQueryData(serviceAccountId, privateKey);
		bigQuery = bigQueryMap.get(serviceAccountId);
		}
		 String querySql = "SELECT * FROM "+rawTableId+" LIMIT 1";
			log.info("Query to fetch column names ["+querySql+"]");
		 Job job = new Job();
			JobConfiguration config = new JobConfiguration();
			JobConfigurationQuery queryConfig = new JobConfigurationQuery();
			config.setQuery(queryConfig);
			
			job.setConfiguration(config);
			queryConfig.setQuery(querySql);
			
			Insert insert = bigQuery.jobs().insert(projectId, job);
			insert.setProjectId(projectId);
			JobReference jobId = insert.execute().getJobReference();
			
			
		    Job completedJob = checkQueryResults(bigQuery, projectId, jobId);

		    GetQueryResultsResponse queryResult = bigQuery.jobs().getQueryResults(projectId,
		    		completedJob.getJobReference().getJobId()).execute();
		    		List<TableRow> rows = queryResult.getRows();
		    		log.info("\nQuery Results:\n------------\n");
		    		for (TableRow row : rows) {
		    			int counter = 0;
		    			String[] fields = new String[row.getF().size()];
		    		for (TableCell field : row.getF()) {
		    			if(field.getV() instanceof String){
		    				fields[counter]  = field.getV().toString();
		    				counter ++;
		    			}
		    			
		     		}
		    		return fields;
		     		}
					return null;
			
		
	}
	
	
	/**
	 * @author Manish Mudgal
	 * Required as we need to remove an order from BQ table before loading historical data.
	 */
	public static boolean copyTable(String tableFrom, String tableTo, String whereclause, String fromDataSet, String toDataSet, String serviceAccountId, String privateKey, String projectId, boolean merge)throws Exception{
		return copyTable(null, tableFrom, tableTo, whereclause, fromDataSet, toDataSet, serviceAccountId, privateKey, projectId, merge);
	}
	public static boolean copyTable(Bigquery bigQuery, String tableFrom, String tableTo, String whereclause, String fromDataSet, String toDataSet, String serviceAccountId, String privateKey, String projectId, boolean merge)throws Exception{
		initBigQueryData(serviceAccountId, privateKey);
		if(bigQuery == null){
			bigQuery   = bigQueryMap.get(serviceAccountId);
		}
		Job job = new Job();
		JobConfiguration config = new JobConfiguration();
		 
		JobConfigurationQuery query = new JobConfigurationQuery();
		if(!merge){
			query.setWriteDisposition("WRITE_TRUNCATE");			
		}else{
			query.setWriteDisposition("WRITE_APPEND");
		}

		query.setQuery(" SELECT * FROM "+tableFrom+" "+ (whereclause == null ? "" : whereclause));
		
		DatasetReference dref = new DatasetReference();
		dref.setDatasetId(fromDataSet);
		dref.setProjectId(projectId);
		query.setDefaultDataset(dref);

		
		TableReference tableRef = new TableReference();
		tableRef.setDatasetId(toDataSet);
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

		 Job completedJob = checkQueryResults(bigQuery, projectId, jobRef);	 
		  
		return completedJob.getStatus().getErrors() == null;
		
	}
	
public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
	SCOPES = new ArrayList<String>();
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
	String projectId = LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
	String[] s = getColumnNamesFromRawTable(bigQuery, null, null, "[LIN_RAW._raw_coreperformance_2015_01_02_2015_01_02_1420443834757]", projectId);
	for(int i=0;i<s.length;i++){
		System.out.println(s[i]);
	}
}
private static Job checkQueryResults(Bigquery bigquery, String projectId, JobReference jobId) throws IOException, InterruptedException {
	return checkQueryResults(bigquery, projectId, jobId.getJobId(), null);
}
private static Job checkQueryResults(Bigquery bigquery, String projectId, String jobId, StringBuffer result) throws IOException, InterruptedException {
	// Variables to keep track of total query time
	long startTime = System.currentTimeMillis();
	long elapsedTime;
	if(result == null){
		result = new StringBuffer();
	}
	Job pollJob = null;
	while (true) {
		pollJob = bigquery.jobs().get(projectId, jobId)
				.execute();
		elapsedTime = System.currentTimeMillis() - startTime;
		log.info("Job status :"+pollJob.getStatus().getState()+", jobId:"+jobId+", Time:"+elapsedTime+" \n");
		if (pollJob.getStatus().getState().equals("DONE")) {
			if(pollJob.getStatus().getErrors() != null){
				for(ErrorProto proto : pollJob.getStatus().getErrors()){
					log.severe(proto.getDebugInfo());
					result.append("debugInfo ["+proto.getDebugInfo()+"]");
					result.append("message ["+proto.getMessage()+"]");
					result.append("reason ["+proto.getReason()+"]");
				}
			}
			break;
		}
	
		Thread.sleep(2000);
		
		
	}
	return pollJob;
}

	public static String getJobStatus(String jobId, String serviceAccountId, String privateKey, String projectId) throws IOException, InterruptedException {
		initBigQueryData(serviceAccountId, privateKey);
		Bigquery bigQuery = bigQueryMap.get(serviceAccountId);
		StringBuffer result = new StringBuffer();
		checkQueryResults(bigQuery, projectId, jobId, result);
		return result.toString().trim();
		
	}	
	
	
 }

