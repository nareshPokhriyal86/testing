package com.lin.web.gaebigquery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.Job;
import com.google.api.services.bigquery.model.JobReference;
import com.google.api.services.bigquery.model.QueryRequest;
import com.google.api.services.bigquery.model.QueryResponse;
import com.lin.web.util.LinMobileConstants;

public class JavaCommandLineServiceAccounts{

  private static final String SCOPE = "https://www.googleapis.com/auth/bigquery";
  private static final HttpTransport TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static List<String> scopeList=null;
  private static GoogleCredential credential=null;
  private static Bigquery bigquery;
  
  public QueryResponse getBigQueryData(String queryStr) throws IOException {
	  if(scopeList ==null){
		  scopeList=new ArrayList<String>();
		  scopeList.add(SCOPE);	
	  }
	  QueryResponse queryResponse = null;
	  String projectNumber =LinMobileConstants.GOOGLE_API_PROJECT_ID; //"mediaagility.com:ma-maps";
	  
	  try{
		  
		  if(credential==null){
			  credential = new GoogleCredential.Builder().setTransport(TRANSPORT)
					    .setJsonFactory(JSON_FACTORY)
					    .setServiceAccountId(LinMobileConstants.GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)
					    .setServiceAccountScopes(scopeList)
					    .setServiceAccountPrivateKeyFromP12File(new File(LinMobileConstants.GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))
					    .build();
		  }
		  
		  if(bigquery==null){
			  bigquery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credential)
			    .setApplicationName("BigQuery-Service-Accounts/0.1")
			    .setHttpRequestInitializer(credential).build();
		  }		

		  QueryRequest queryRequest = new QueryRequest().setQuery(queryStr);
		  Bigquery.Jobs.Query query = bigquery.jobs().query(projectNumber, queryRequest);
		  queryResponse = query.execute();
		  JobReference jobRef = queryResponse.getJobReference();
		  Job job = checkQueryResults(bigquery,projectNumber,jobRef);		
		  String state=job.getStatus().getState();
	    
	  }
	  catch(Exception e){
		  System.out.println("Exception caught = "+e);
	  }

	  return  queryResponse;

  }
  
  public static Job checkQueryResults(Bigquery bigquery, String projectNumber,JobReference jobRef) throws IOException, InterruptedException {
		
		long startTime = System.currentTimeMillis();
		long elapsedTime;
		while (true) {
			Job pollJob = bigquery.jobs().get(projectNumber, jobRef.getJobId()).execute();
			elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println("Job status :"+pollJob.getStatus().getState()+", jobId:"+jobRef.getJobId()+", Time:"+elapsedTime+"\n");
			if (pollJob.getStatus().getState().equals("DONE")) {
				return pollJob;
			}
			Thread.sleep(1000);
		}
	}
  
 
}
