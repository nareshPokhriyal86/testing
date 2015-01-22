package com.lin.server.mapengine;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mapsengine.MapsEngine;
import com.google.api.services.mapsengine.model.Map;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;

public class MapEngineAuthentication {
  /** Provide the ID of the map you wish to read. */
  private static final String MAP_ID = "YOUR_MAP_ID_HERE";

  private static final String APPLICATION_NAME = "Google-MapsEngineApiSample/1.0";

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  private static final String SERVICE_ACCOUNT_EMAIL = "SERVICE_ACCOUNT_EMAIL";

  /** Authorizes the installed application to access user's protected data. */
  private static Credential authorize() throws Exception {
    GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
      .setJsonFactory(JSON_FACTORY)
      .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
      .setServiceAccountScopes(
          Collections.singleton("https://www.googleapis.com/auth/mapsengine.readonly"))
      .setServiceAccountPrivateKeyFromP12File(new File("key.p12"))
      .build();
    credential.refreshToken();
    return credential;
  }

  public static void main(String[] args) {
    try {
      // Authorize this application to access the user's data.
      Credential credential = authorize();

      // Create an authorized Maps Engine client with the credential.
      MapsEngine mapsEngine = new MapsEngine.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
          .setApplicationName(APPLICATION_NAME).build();

      // Make a request to get the details of a particular map.
      Map map = mapsEngine.maps().get(MAP_ID).execute();
      System.out.println(map.getName());
      System.out.println(map.getDescription());

    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (Exception t) {
      t.printStackTrace();
    }
  }
}
