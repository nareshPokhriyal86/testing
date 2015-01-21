package example;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Data;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.bigquery.model.GetQueryResultsResponse;
import com.google.api.services.bigquery.model.QueryRequest;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;

public class TestBQ {
	 // Enter your Google Developer Project number or string id.
	  private static final String PROJECT_ID = "LinDigital";

	  // Use a Google APIs Client standard client_secrets.json OAuth 2.0 parameters file.
	  private static final String CLIENTSECRETS_LOCATION = "auth.json";

	  // Objects for handling HTTP transport and JSON formatting of API calls.
	  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	  
public static void main(String[] args) throws IOException, GeneralSecurityException {
	List SCOPES=new ArrayList<String>();
	SCOPES.add(LinMobileConstants.GOOGLE_BIGQUERY_SCOPE);
    

	GoogleCredential credential = null;
        credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY)
        		
		 .setServiceAccountId(LinMobileConstants.GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)		                            
		 .setServiceAccountScopes(SCOPES)
		 //.setServiceAccountPrivateKeyFromP12File(new File("892d0834292bff62001a53a17624d1b22f1002a3-privatekey.p12"))
		 .setServiceAccountPrivateKeyFromP12File(new File("C:/Users/user/git/linmobile-dev/src/main/resources/env/keys/"
					+ LinMobileVariables.SERVICE_ACCOUNT_KEY))	
				
					
		 .build();
        Bigquery bigquery = 
        new Bigquery.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
		.setApplicationName("BigQuery-Service-Accounts/0.1")
		.setHttpRequestInitializer(credential).build();
        
        String query = "SELECT * FROM  [LinDigital:LIN_DEV.test_table] ";
        runQueryRpcAndPrint(bigquery, PROJECT_ID, query, System.out);

}


static void runQueryRpcAndPrint(
	      Bigquery bigquery, String projectId, String query, PrintStream out) throws IOException {
	    QueryRequest queryRequest = new QueryRequest().setQuery(query);
	    QueryResponse queryResponse = bigquery.jobs().query(projectId, queryRequest).execute();
	    if (queryResponse.getJobComplete()) {
	      printRows(queryResponse.getRows(), out);
	      if (null == queryResponse.getPageToken()) {
	        return;
	      }
	    }
	    // This loop polls until results are present, then loops over result pages.
	    String pageToken = null;
	    while (true) {
	      GetQueryResultsResponse queryResults = bigquery.jobs()
	          .getQueryResults(projectId, queryResponse.getJobReference().getJobId())
	          .setPageToken(pageToken).execute();
	      if (queryResults.getJobComplete()) {
	        printRows(queryResults.getRows(), out);
	        pageToken = queryResults.getPageToken();
	        if (null == pageToken) {
	          return;
	        }
	      }
	    }
	  }

private static void printRows(List<TableRow> rows, PrintStream out) {
    if (rows != null) {
      for (TableRow row : rows) {
        for (TableCell cell : row.getF()) {
          // Data.isNull() is the recommended way to check for the 'null object' in TableCell.
          out.printf("%s, ", Data.isNull(cell.getV()) ? "null" : cell.getV().toString());
        }
        out.println();
      }
    }
  }
}
