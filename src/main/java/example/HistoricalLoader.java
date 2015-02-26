package example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HistoricalLoader {

	
	public static String excutePost(String targetURL, String urlParameters) {
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
	public static void main(String[] args) {
		/*String[] orders =new String[] { "174463302","181012302","176410662","176407662","180634662","179527422","177699222","179071662","181909782","181557222","232241909"
		 		,"179537382","179039742","180278622","178434222","181283262","178333782","180444222","180970542","181140222","180157782","181601982","181656342","181647222","181385022",
				"181239342" ,"181401702","181328502","181268742","181285782","181266222","181694022","181644702","181909782"};
		
		int index= 32;
		
		System.out.println("Going to load data for "+ orders[index]);*/
		
		String[] loadTypes = new String[]{"coreperformance","location","target","customevent","richmedia"};
		for(int i=0;i<loadTypes.length;i++){
			 new Thread(new ExecutorThread("118540769", loadTypes[i])).start();;
		} 
	}
}
