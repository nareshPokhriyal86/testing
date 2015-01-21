package com.lin.web.layer.fusion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


 public class FusionTableUsingRest {
	 private static final String SERVICE_URL = "https://www.google.com/fusiontables/api/query";
	 private static final String API_KEY="AIzaSyBSW_nbk0I2AlVO3eE6wWt2lwALTTrzbWg";
	 
	 
	 public static void getAllTable(){
		 
		try {
			 
			 String query="SELECT Creative_Size FROM 1gNCPNqGLkdjph9-yPUPfI-b8nRd69XC8hm-iOtw&key=AIzaSyBSW_nbk0I2AlVO3eE6wWt2lwALTTrzbWg";
			 String encodedQuery = URLEncoder.encode(query, "UTF-8");
			 URL url = new URL("https://www.googleapis.com/fusiontables/v1/query?" +"sql="+encodedQuery);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		     String line;
		     while ((line = reader.readLine()) != null) {
		        System.out.println("line:"+line);
		     }
		     reader.close();

		} catch (MalformedURLException e) {
		    System.out.println("MalformedURLException:"+e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException:"+e.getMessage());
		}
	 }
	 
	 public static void main(String [] args){
		 getAllTable();
	 }

}
