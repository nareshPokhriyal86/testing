package example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExecutorThread implements Runnable{
	
	String orderId, loadType;
	public ExecutorThread(String orderId, String loadType){
		this.orderId = orderId;
		this.loadType  = loadType;
	}
	@Override
	public void run() {
		System.out.println("Running for order ["+ orderId +"] and loadType["+loadType+"]");
		try{
		excutePost("http://linbackend.linmobile-backend.appspot.com/runSmartDataLoader.lin",
				"orderId="+orderId+"&publisherIdInBQ=5&dfpNetworkCode=5678&dfpTaskId=manual02021156&manual=true&loadType="+loadType+"");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public String excutePost(String targetURL, String urlParameters) {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("GET");
//	      connection.setRequestProperty("Content-Type",  "application/x-www-form-urlencoded");

	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  

	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response    
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}
}
