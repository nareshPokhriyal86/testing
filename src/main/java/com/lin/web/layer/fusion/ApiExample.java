package com.lin.web.layer.fusion;

import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;
import com.lin.web.util.LinMobileConstants;

import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Java example using the Google Fusion Tables API to query, insert, update, and delete.
 * Dependencies:
 *   - GData Java Client Library
 *   - opencsv
 *
 * 
 */
public class ApiExample {

 /**
  * Google Fusion Tables API URL. 
  * All requests to the Google Fusion Tables service begin with this URL.
  */
 private static final String SERVICE_URL = "https://www.google.com/fusiontables/api/query";

 /**
  * Service to handle requests to Google Fusion Tables.
  */
 private GoogleService service;

 /**
  * Authenticates the given account for {@code fusiontables} service using the provided Gmail
  * account and password.
  *
  * @param  email Gmail account's email address
  * @param  password password for the given Gmail account
  * @throws AuthenticationException when the authentication fails
  */
 public ApiExample(String email, String password) throws AuthenticationException {
   service = new GoogleService("fusiontables", "fusiontables.ApiExample");
   service.setUserCredentials(email, password, ClientLoginAccountType.GOOGLE);
 }

 /**
  * Authenticates to the {@code fusiontables} service using the auth token. The
  * auth token can be retrieved for an authenticated user by invoking
  * {@link service#getAuthToken} on the email and password. The auth token is valid
  * for two weeks and can be reused rather than having to specify the token repeatedly.
  *
  * @param  authToken the auth token
  * @see ClientLogin docs
  * @throws AuthenticationException when the authentication fails
  */
 public ApiExample(String authToken) throws AuthenticationException {
   service = new GoogleService("fusiontables", "fusiontables.ApiExample");
   service.setUserToken(authToken);
 }

 /**
  * Returns the results of running a Fusion Tables SQL query.
  *
  * @param  query the SQL query to send to Fusion Tables
  * @param  isUsingEncId includes the encrypted table ID in the result if {@code true}, otherwise
  *         includes the numeric table ID
  * @return the results from the Fusion Tables SQL query
  * @throws IOException when there is an error writing to or reading from GData service
  * @throws ServiceException when the request to the Fusion Tables service fails
  * @see    com.google.gdata.util.ServiceException
  */
 public QueryResults run(String query, boolean isUsingEncId) throws IOException, ServiceException {

   String lowercaseQuery = query.toLowerCase();
   String encodedQuery = URLEncoder.encode(query, "UTF-8");

   GDataRequest request;
   // If the query is a select, describe, or show query, run a GET request.
   if (lowercaseQuery.startsWith("select") ||
       lowercaseQuery.startsWith("describe") ||
       lowercaseQuery.startsWith("show")) {
     URL url = new URL(SERVICE_URL + "?sql=" + encodedQuery + "&encid=" + isUsingEncId);
     request = service.getRequestFactory().getRequest(RequestType.QUERY, url,
         ContentType.TEXT_PLAIN);
   } else {
     // Otherwise, run a POST request.
     URL url = new URL(SERVICE_URL + "?encid=" + isUsingEncId);
     request = service.getRequestFactory().getRequest(RequestType.INSERT, url,
         new ContentType("application/x-www-form-urlencoded"));
     OutputStreamWriter writer = new OutputStreamWriter(request.getRequestStream());
     writer.append("sql=" + encodedQuery);
     writer.flush();
   }

   request.execute();

   return getResults(request);
 }

 /**
  * Returns the Fusion Tables CSV response as a {@code QueryResults} object.
  *
  * @return an object containing a list of column names and a list of row values from the
  *         Fusion Tables response
  */
 private QueryResults getResults(GDataRequest request)
     throws IOException {
   InputStreamReader inputStreamReader = new InputStreamReader(request.getResponseStream());
   BufferedReader bufferedStreamReader = new BufferedReader(inputStreamReader);
   CSVReader reader = new CSVReader(bufferedStreamReader);
   // The first line is the column names, and the remaining lines are the rows.
   List<String[]> csvLines = reader.readAll();
   List<String> columns = Arrays.asList(csvLines.get(0));
   List<String[]> rows = csvLines.subList(1, csvLines.size());
   QueryResults results = new QueryResults(columns, rows);
   return results;
 }

 /**
  * Result of a Fusion Table query.
  */
 private static class QueryResults {
   final List<String> columnNames;
   final List<String[]> rows;

   public QueryResults(List<String> columnNames, List<String[]> rows) {
     this.columnNames = columnNames;
     this.rows = rows;
   }

  /**
   * Prints the query results.
   *
   * @param the results from the query
   */
  public void print() {
    String sep = "";
    for (int i = 0; i < columnNames.size(); i++) {
      System.out.print(sep + columnNames.get(i));
      sep = ", ";
    }
    System.out.println();

    for (int i = 0; i < rows.size(); i++) {
      String[] rowValues = rows.get(i);
      sep = "";
      for (int j = 0; j < rowValues.length; j++) {
        System.out.print(sep + rowValues[j]);
        sep = ", ";
      }
      System.out.println();
    }
  }
 }

 /**
  * Authorizes the user with either a Google Account email and password
  * or auth token, then exercises various queries to:
  * 1. Create a table
  * 2. Insert data into the table
  * 3. Select data from the table
  * 4. Drop the table
  */
 public static void main(String[] args) throws ServiceException, IOException {
	
   if (args.length == 0) {
     System.err.println("Usage: java ApiExample   OR " + "java ApiExample ");
    // System.exit(1);
   }
   String authToken="DQAAAFgBAADaAc6d2Puhns2IT21Qih83RpANQx1qjr9J7l3TEPxMylk_DJoLt3HmHP4tgt8-DMb4ZBMki6ePHcgM9EME8ljcSZkm3bqFchcE8HIL97LjDZcQO6HpE-2Q_ayoqKGTXd6YthJGoVniWSx4qbEIYHHKDjS1Vsg6m4Gkg7AGBpjYJMwHPAiy0ZyqtAZvegLwXJ1ZNxejMnO1_pOzopbHQvTFUzL7CiZLwZTz5wQipT4e_8IG7qePQ5rZ2s_iU2fFJFXjP2_ZbwMIqaUrFsjPI2SrDu0naw6On11-TrT8f-XSorJkBt8Cj770CivoRVmzATxyEGO6N4o1K9PFK7iSdTnA9bnu6j1074Jjdp1XBP4uWrmCzMp_oZOPgrIPQUKlBd4a2GBwUwMj9IcYE_ER7wDFzT9zhSrvGimMtl-z-ZyNwbDWejb7LHtcuxtmn0HjMQt0cjT_VuEWv8lbZ0aXyjnH";
   //ApiExample example =  new ApiExample(LinMobileConstants.EMAIL_ADDRESS,LinMobileConstants.EMAIL_PASSWORD);
   ApiExample example =  new ApiExample(authToken);

   boolean useEncId = true;

   System.out.println("--- Create a table ---");
   QueryResults results = example.run("CREATE TABLE demo (name:STRING, date:DATETIME)", useEncId);
   results.print();
   String tableId = (results.rows.get(0))[0];

   System.out.println("--- Insert data into the table ---");
   results = example.run("INSERT INTO " + tableId + " (name, date) VALUES ('bob', '1/1/2012')",
       useEncId);
   results.print();

   System.out.println("--- Insert more data into the table ---");
   results = example.run("INSERT INTO " + tableId + " (name, date) VALUES ('george', '1/4/2012')",
       useEncId);
   results.print();

   System.out.println("--- Select data from the table ---");
   results = example.run("SELECT * FROM " + tableId + " WHERE date > '1/3/2012'", useEncId);
   results.print();

   System.out.println("--- Drop the table ---");
  // results = example.run("DROP TABLE " + tableId, useEncId);
   //results.print();
 }
}
