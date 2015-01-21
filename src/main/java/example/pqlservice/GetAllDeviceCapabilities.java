package example.pqlservice;

//Copyright 2013 Google Inc. All Rights Reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.


import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.utils.CsvFiles;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201403.Pql;
import com.google.api.ads.dfp.axis.utils.v201403.StatementBuilder;
import com.google.api.ads.dfp.axis.v201403.PublisherQueryLanguageServiceInterface;
import com.google.api.ads.dfp.axis.v201403.ResultSet;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.appengine.labs.repackaged.com.google.common.collect.ImmutableList;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;

import java.io.File;

/**
* This example gets all browsers available to target from the Browser table.
* Other tables include 'Bandwidth_Group', 'Browser_Language',
* 'Device_Capability', 'Operating_System', etc...
*
* A full list of available criteria tables can be found at
* https://developers.google.com/doubleclick-publishers/docs/reference/v201403/PublisherQueryLanguageService
*
* Credentials and properties in {@code fromFile()} are pulled from the
* "ads.properties" file. See README for more info.
*
* Tags: PublisherQueryLanguageService.select
*
* @author Adam Rogal
*/
public class GetAllDeviceCapabilities {

public static void runExample(DfpServices dfpServices, DfpSession session)
   throws Exception {
 // Get the PublisherQueryLanguageService.
 PublisherQueryLanguageServiceInterface pqlService =
     dfpServices.get(session, PublisherQueryLanguageServiceInterface.class);

 // Create statement to select all browsers.
 StatementBuilder statementBuilder = new StatementBuilder()
     .select("Id, DeviceCapabilityName")
     .from("Device_Capability")
     .orderBy("DeviceCapabilityName ASC")
     .offset(0)
     .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

 // Default for result sets.
 ResultSet combinedResultSet = null;
 ResultSet resultSet;
 int i = 0;

 do {
   // Get all browsers.
   resultSet = pqlService.select(statementBuilder.toStatement());

   // Combine result sets with previous ones.
   combinedResultSet = combinedResultSet == null
       ? resultSet
       : Pql.combineResultSets(combinedResultSet, resultSet);

   System.out.printf("%d) %d criteria beginning at offset %d were found.\n", i++,
       resultSet.getRows() == null ? 0 : resultSet.getRows().length,
       statementBuilder.getOffset());

   statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
 } while (resultSet.getRows() != null && resultSet.getRows().length > 0);

 // Change to your file location.
 String filePath = new File("C:\\testdownload\\DeviceCapabilities-.csv").toString();

 // Write the result set to a CSV.
 CsvFiles.writeCsv(Pql.resultSetToStringArrayList(combinedResultSet), filePath);

 System.out.printf("Browsers saved to %s\n", filePath);
}

public static void main(String[] args) throws Exception {
 // Generate a refreshable OAuth2 credential similar to a ClientLogin token
 // and can be used in place of a service account.
  

 // Construct a DfpSession.
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
			LinMobileConstants.LIN_MOBILE_DFP_APPLICATION_NAME)
	.withOAuth2Credential(credential).build();
 DfpServices dfpServices = new DfpServices();

 runExample(dfpServices, dfpSession);
}
}