package com.lin.web.gaebigquery;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.Bigquery.Jobs.Insert;
import com.google.api.services.bigquery.model.Job;
import com.google.api.services.bigquery.model.JobConfiguration;
import com.google.api.services.bigquery.model.JobConfigurationLoad;
import com.google.api.services.bigquery.model.JobReference;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableSchema;
import com.lin.web.dto.ReadBigQuerySchemaDTO;
import com.lin.web.util.CloudStorageUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;

public class  CloudBigQueryAuth extends HttpServlet{

  private static final String SCOPE = "https://www.googleapis.com/auth/bigquery";
  private static final HttpTransport TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static final String CLIENT_ID = "15890596879-ucoa3p29vcpbbdpss2t85uuti82pc7hk.apps.googleusercontent.com";
  private static final Logger log = Logger.getLogger(CloudBigQueryAuth.class.getName());
  private static final String PROJECT_ID = "mediaagility.com:ma-maps";
  private static final String DATASET_ID = "LIN";
  private static final String TABLE_ID   = "Core_Performance";
 // private static final String bucketName = "ma-bigquery";
  private static Bigquery bigquery;
  private static List<String> scopeList=null;

  public void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException, ServletException {
	  resp.setContentType("text/plain");
	  try
	  {  
	     String path = req.getParameter("path");
	     String schemaFile = req.getParameter("schemaFile");
	     System.out.println(path);
     	//String bucketName = "ma-bigquery";
     	String jobId=storeDataToBigQuery(path,schemaFile);
		resp.getWriter().print("JOBId:"+jobId);
  }catch(Exception e){
		log.severe("exception in do get method"+e.getMessage());
	}
  }
  
  public String storeDataToBigQuery(String path, String schemaFile ) throws GeneralSecurityException, IOException{
	//  String schemaFile = null;
	  String reportFolder = null;
	  if(scopeList ==null){
		  scopeList=new ArrayList<String>();
		  scopeList.add(SCOPE);	
	  } 

  
	  GoogleCredential credential = new GoogleCredential.Builder().setTransport(TRANSPORT)
      .setJsonFactory(JSON_FACTORY)
      .setServiceAccountId("15890596879-kp0i5r5lk77nlvmpl9bsnjufmeksqqrj@developer.gserviceaccount.com")
      .setServiceAccountScopes(scopeList)
      .setServiceAccountPrivateKeyFromP12File(new File("892d0834292bff62001a53a17624d1b22f1002a3-privatekey.p12"))
      .build();
	  
	  bigquery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credential)
      .setApplicationName("BigQuery-Service-Accounts/0.1")
      .setHttpRequestInitializer(credential).build();
  
      Job job = new Job();
      JobConfiguration config = new JobConfiguration();
      JobConfigurationLoad loadConfig = new JobConfigurationLoad();
      config.setLoad(loadConfig);
      job.setConfiguration(config);
      // Set where you are importing from (i.e. the Google Cloud Storage paths).
      List<String> sources = new ArrayList<String>();
      String currentDate=DateUtil.getCurrentTimeStamp("yyyy_MM");
     // sources.add("gs://"+LinMobileConstants.APPLICATION_BUCKET_NAME+"/"+reportFolder+"/"+currentDate+"/"+fileName);
      sources.add(path);
      loadConfig.setSourceUris(sources);
      //FileContent content = new FileContent("application/octet-stream", new File(fileName));
      // Describe the resulting table you are importing to:
      TableReference tableRef = new TableReference();
      tableRef.setDatasetId(DATASET_ID);
      tableRef.setTableId(TABLE_ID);
      tableRef.setProjectId(PROJECT_ID);
      loadConfig.setDestinationTable(tableRef);
      loadConfig.setSchema(generateSchemaFromCloud(schemaFile));
      loadConfig.setSkipLeadingRows(1);
    //  Bigquery bigquery = new Bigquery(TRANSPORT, JSON_FACTORY, credential);
  	  Insert insert = bigquery.jobs().insert(PROJECT_ID, job);
      insert.setProjectId(PROJECT_ID);
      JobReference jobRef = insert.execute().getJobReference();
      String jobId = jobRef.getJobId();
      System.out.println("after insert projectId:"+jobRef.getProjectId());
  	  System.out.println("after insert.execute()"+jobId);
  	 return jobId;
  }

  public static TableSchema generateSchemaFromCloud(String schemaFile) {
	  String schemaFileName =null;
	  if(schemaFile!=null){
		  schemaFileName = schemaFile;
	  }
      List<TableFieldSchema> schemaList = new ArrayList<TableFieldSchema>();
      List<ReadBigQuerySchemaDTO> schemaDTO = new ArrayList<ReadBigQuerySchemaDTO>();
      schemaDTO = CloudStorageUtil.readBigQuerySchemaCSVFromCloudStorage(schemaFileName,LinMobileVariables.APPLICATION_BUCKET_NAME);
      Iterator<ReadBigQuerySchemaDTO> iterator = schemaDTO.iterator();
      ReadBigQuerySchemaDTO obj;
      while(iterator.hasNext()){
    	  obj = iterator.next();
    	  log.info("column name :"+obj.getColumnName()+"  column type : "+obj.getColumnType());
    	  schemaList.add(new TableFieldSchema().setName(obj.getColumnName())
    	                                       .setType(obj.getColumnType()));
    	  
      }
      TableSchema schema = new TableSchema();
      schema.setFields(schemaList);

      return schema;
  }

}

/*public static TableSchema generateSchema() {
List<TableFieldSchema> schemaList = new ArrayList<TableFieldSchema>();
schemaList.add(new TableFieldSchema().setName("OrderId")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("LineItemId")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("LineItemName")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("OrderName")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("ClicksDelivered")
                                     .setType("INTEGER"));
schemaList.add(new TableFieldSchema().setName("ImpressionsDelivered")
        							 .setType("INTEGER"));
schemaList.add(new TableFieldSchema().setName("CostType")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("Last_7_days_delivered_clicks")
                                     .setType("INTEGER"));
schemaList.add(new TableFieldSchema().setName("Last_7_days_delivered_impressions")
                                     .setType("INTEGER"));
schemaList.add(new TableFieldSchema().setName("actualDeliveryPercentage")
                                     .setType("FLOAT"));
schemaList.add(new TableFieldSchema().setName("expectedDeliveryPercentage")
                                     .setType("FLOAT"));
schemaList.add(new TableFieldSchema().setName("CurrencyType")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("CostPerUnit")
                                     .setType("FLOAT"));
schemaList.add(new TableFieldSchema().setName("Budget")
                                     .setType("FLOAT"));
schemaList.add(new TableFieldSchema().setName("ValueCostPerUnit")
                                     .setType("FLOAT"));
schemaList.add(new TableFieldSchema().setName("Status")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("ReportTimestamp")
                                     .setType("TIMESTAMP"));
schemaList.add(new TableFieldSchema().setName("StartDateTime")
                                     .setType("TIMESTAMP"));
schemaList.add(new TableFieldSchema().setName("EndDateTime")
                                     .setType("TIMESTAMP"));
schemaList.add(new TableFieldSchema().setName("TargetPlatform")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("LineItemType")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("Discount")
                                     .setType("FLOAT"));
schemaList.add(new TableFieldSchema().setName("DiscountType")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("LastModifiedByApp")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("ExternalId")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("Priority")
                                     .setType("INTEGER"));
schemaList.add(new TableFieldSchema().setName("UnitsBought")
                                     .setType("INTEGER"));
schemaList.add(new TableFieldSchema().setName("UnitType")
                                     .setType("STRING"));
schemaList.add(new TableFieldSchema().setName("Duration")
                                     .setType("STRING"));

TableSchema schema = new TableSchema();
schema.setFields(schemaList);

return schema;
}
*/