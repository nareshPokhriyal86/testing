package com.lin.web.servlet;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;


public class StorageSample extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final String GCS_URI = "http://commondatastorage.googleapis.com";
  static final String UPLOAD_URL="https://www.googleapis.com/upload/storage/v1beta2/b/linmobile_qa/o?uploadType=multipart";

  /** Global configuration of Google Cloud Storage OAuth 2.0 scope. */
  private static final String STORAGE_SCOPE =
      "https://www.googleapis.com/auth/devstorage.read_write";

 
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static final Logger log = Logger.getLogger(BigQueryUtil.class.getName());  
  private static List<String> SCOPES=new ArrayList<>();
  
  private static GoogleCredential credential;
	
  static final int BUFFER_SIZE = 2* 1024 * 1024;
  
  
  public void doGet(HttpServletRequest req, HttpServletResponse res)  {
	   System.out.println("here........");
	   ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
	    try {
	        String fileURL="http://storage.googleapis.com/linmobile_qa/test/test.csv.gz";
	        IDFPReportService dfpReportService=new DFPReportService(); 
	        String csvData=dfpReportService.readCSVGZFile(fileURL);
	        
	        //System.out.println("csv data:"+csvData);
	        
	        String uploadCloudStorage=req.getParameter("upload");
	        if(uploadCloudStorage !=null && uploadCloudStorage.equals("true")){
	        	String path=uploadCloudStorage(csvData);
	        	res.getWriter().print("File has been uploaded at :"+path);
	        }else{
	        	InputStream in = new ByteArrayInputStream(csvData.getBytes());
		        ZipOutputStream zipOut = new ZipOutputStream(zipBaos);
		        
		        byte[] file1Contents = IOUtils.toByteArray(in);
		        
		        ZipEntry zipEntry1 = new ZipEntry("testFile.csv");     // Create new zip entry																	 // the constructor parameter is filename of the entry
		        zipOut.putNextEntry(zipEntry1);            // add the zip entry to the ZipOutputStream
		        zipOut.write(file1Contents);			   // write contents (byte array) to the ZipOutputStream
		        zipOut.closeEntry();	
		        zipOut.close();
		
		        System.out.println("Done............");
		        // Get the zip data from the ByteArrayOutputStream
			    byte[] zipData = zipBaos.toByteArray();
			
			
			    // Serve the data to response's stream
			    String filename = "MyZipFile.zip";	
			    // following header statement instructs the web browser 
			    // to treat the file as attachment (= to download the file)
			    res.setHeader("Content-Disposition", "attachment; filename=" + filename);
			
			    res.setContentType("application/x-download");
			    res.setContentLength(zipData.length);
			    res.getOutputStream().write(zipData);	
	        }
	        
		  } catch (IOException e) {
		      // Creation of the zip file failed; inform the browser about it
				log.severe("Creation of the zip file failed with exception:\n\n" + e.getLocalizedMessage());
			
	    }  
    
	   
  }
  
  public String uploadCloudStorage(String csvData){	  
	  System.out.println("Upload cloud storage....");
	  GCStorageUtil st=new GCStorageUtil();
	  String reportPathOnStorage=st.uploadFileAtCloudStorageViaZip(csvData, "myFile.zip", "test", LinMobileVariables.APPLICATION_BUCKET_NAME);
      System.out.println("reportPathOnStorage:"+reportPathOnStorage);
	  return reportPathOnStorage;
  }
  
  public void uploadUsingServiceAccount(HttpServletRequest req, HttpServletResponse resp) throws IOException{
	  try {
	    	if(SCOPES.size()==0){
	    		SCOPES.add(STORAGE_SCOPE);
	    	}
	    	credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
				     .setJsonFactory(JSON_FACTORY)
					 .setServiceAccountId(LinMobileConstants.GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS)		                            
					 .setServiceAccountScopes(SCOPES)
					 .setServiceAccountPrivateKeyFromP12File(new File("keys/"+LinMobileConstants.GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY))						 
					 .build();
	    	
	      // Set up and execute Google Cloud Storage request.
	      //String bucketName = req.getRequestURI();
	      String bucketName = "/"+LinMobileVariables.APPLICATION_BUCKET_NAME+"/test";
	      if (bucketName.equals("/")) {
	        resp.sendError(404, "No bucket specified - append /bucket-name to the URL and retry.");
	        return;
	      }
	      
	    
	      String csvData="Hello, this is youdhveer........";
	    
	     System.out.println("Done............");
	      // Remove any trailing slashes, if found.
	      String cleanBucketName = bucketName.replaceAll("/$", "");
	      String URI = GCS_URI + cleanBucketName;
	      System.out.println("URI:"+URI);
	      URI=UPLOAD_URL;
	      HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
	     
	      GenericUrl url = new GenericUrl(URI);
	      
	      String requestBody = "{'name': '"+csvData+"'}";

	      HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString(null, requestBody));
	      request.getHeaders().setContentType("application/json");

	      
	    
	    //  HttpRequest request = requestFactory.buildGetRequest(url);
	      HttpResponse response = request.execute();
	      
	      String content = response.parseAsString();

	      // Display the output XML.
	      resp.setContentType("text/html");
	      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));      
	      writer.append(content);
	      writer.flush();
	      resp.setStatus(200);
	    
		  } catch (Exception e) {
	    	System.out.println("error:"+e.getMessage());
	        resp.sendError(404, e.getMessage());
	        
	    }
  }
  
  
}