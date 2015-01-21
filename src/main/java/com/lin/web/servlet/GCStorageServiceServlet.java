package com.lin.web.servlet;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.util.Preconditions;
import com.google.appengine.tools.cloudstorage.GcsFileMetadata;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions.Builder;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.lin.web.util.LinMobileVariables;

/*
 * Not used currently
 * 
 * @author Youdhveer Panwar
 * This is latest cloud storage java client library
 */
public class GCStorageServiceServlet {

  
	private static final int BUFFER_SIZE = 4* 1024 * 1024;
	
	//GcsService gcsService = GcsServiceFactory.createGcsService();
	private final GcsService gcsService =
		    GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
	
	OutputStream outputStream = null;
	
	final int fetchSize = 4 * 1024 * 1024;
    final int readSize = 2 * 1024 * 1024;
    GcsOutputChannel outputChannel = null;
    
	private static final Logger log = Logger.getLogger(GCStorageServiceServlet.class.getName());

	
	public void init(String dirName,String fileName, String mime,String bucketName) throws Exception {	
		log.info("Storage service:init(....) method:  file name:"+fileName+" and mime:"+mime+" and bucketName:"+bucketName);
	    GcsFilename filename = new GcsFilename(bucketName+"/"+dirName, fileName);
		GcsFileOptions options = new GcsFileOptions.Builder()
		    .mimeType(mime)
		    .acl("public-read")
		    //.addUserMetadata("myfield1", "my field value")
		    .build();
		
		outputChannel= gcsService.createOrReplace(filename, options);
		outputStream = Channels.newOutputStream(outputChannel);
		  
	}

	
	@SuppressWarnings("static-access")
	public void init(String fileName, String mime) throws Exception {
	 log.info("Storage service:init() method:  file name:"+fileName+" and mime:"+mime);
	    
	  GcsFilename filename = new GcsFilename(LinMobileVariables.APPLICATION_BUCKET_NAME, fileName);
	  GcsFileOptions options = new GcsFileOptions.Builder()
	    .mimeType(mime)
	    //.acl("public-read")
	    //.addUserMetadata("myfield1", "my field value")
	    .build();
	
	  outputChannel = gcsService.createOrReplace(filename, options);
	  outputStream = Channels.newOutputStream(outputChannel);
	}

	public void writeFile(String file,String mime) throws IOException{			
		
		try{
			GcsFilename filename = new GcsFilename(LinMobileVariables.APPLICATION_BUCKET_NAME, "testFile");
		System.out.println("Here....");
			GcsFileOptions options = new GcsFileOptions.Builder()
			    .mimeType(mime)
			    .acl("public-read")
			    .addUserMetadata("myfield1", "my field value")
			    .build();
			System.out.println("1111111111");
			outputChannel = gcsService.createOrReplace(filename, options);	
			System.out.println("22222222222");
			
			outputChannel.waitForOutstandingWrites();

			outputChannel.write(ByteBuffer.wrap(file.getBytes()));
			
			/*OutputStream outputStream = Channels.newOutputStream(outputChannel);		  
			
			InputStream input = new ByteArrayInputStream(fileName.getBytes());
			BufferedInputStream bufferedInputStream = new BufferedInputStream(input);*/
			
		
			
		}finally {
	        outputChannel.close();
	    }
		
	}
	
	

	public void writeZipFile(String fileName,String mime) throws IOException{		
		ZipOutputStream zip=null;
		try{
			GcsFilename filename = new GcsFilename(LinMobileVariables.APPLICATION_BUCKET_NAME+"/test", fileName);
			GcsFileOptions options = new GcsFileOptions.Builder()
			    .mimeType(mime)
			    //.acl("public-read")
			    //.addUserMetadata("myfield1", "my field value")
			    .build();
			
			outputChannel = gcsService.createOrReplace(filename, options);
			OutputStream outputStream = Channels.newOutputStream(outputChannel);		  
			zip = new ZipOutputStream(outputStream);
			/*InputStream input = new ByteArrayInputStream(csvData.getBytes());
			BufferedInputStream bufferedInputStream = new BufferedInputStream(input);*/
			GcsInputChannel readChannel  = gcsService.openPrefetchingReadChannel(filename, 0, fetchSize);
	        final ByteBuffer buffer = ByteBuffer.allocate(readSize);
	        int bytesRead = 0;
	        while (bytesRead >= 0) {
	            bytesRead = readChannel.read(buffer);
	            buffer.flip();
	            zip.write(buffer.array(), buffer.position(), buffer.limit());
	            buffer.rewind();
	            buffer.limit(buffer.capacity());
	        }  
			
		}finally {
	        zip.flush();
	        zip.close();
	        outputChannel.close();
	    }
		
	}
	
	public void readFile(GcsFilename filename) throws IOException{
		
		GcsInputChannel readChannel = null;
	    BufferedReader reader = null;
	    try {
	      // We can now read the file through the API:
	      readChannel = gcsService.openReadChannel(filename, 0);	   
	      reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
	      String line;
	      while ((line = reader.readLine()) != null) {
	        System.out.println("READ:" + line);
	      }
	    } finally {
	      if (reader != null) { reader.close(); }
	    }
	}


	public void storeFile(byte[] b, int readSize) throws Exception {	
	    outputStream.write(b, 0, readSize);
	    outputStream.flush();
	}
	
	
	public String getUploadedFileURLOnGoogleCloudStorage(String fileName){
		//return "http://storage.googleapis.com/"+BUCKET_NAME+"/"+fileName;
		return "gs://"+LinMobileVariables.APPLICATION_BUCKET_NAME+"/"+fileName;
	}
	
	public String getUploadedFileURLOnGoogleCloudStorage(String fileName,String dirName){
		return "gs://"+LinMobileVariables.APPLICATION_BUCKET_NAME+"/"+dirName+"/"+fileName;
	}
	
	public String getUploadedFileURLOnGoogleCloudStorage(String fileName,String dirName,String bucketName){
		return "gs://"+bucketName+"/"+dirName+"/"+fileName;
	}
	public String writeFile(String fileName,InputStream inputStream) throws Exception{
		log.info("Writing file on cloud storage with name..."+fileName);
		byte[] b = new byte[BUFFER_SIZE];
		int readBytes = inputStream.read(b, 0, BUFFER_SIZE);		
		while (readBytes != -1) {
		       storeFile(b, readBytes);
		       readBytes = inputStream.read(b, 0, readBytes);
		}
		inputStream.close();
		destroy();
		String uploadedFileURL=getUploadedFileURLOnGoogleCloudStorage(fileName);
		log.info("Writing file done on cloud storage, fileURL..."+uploadedFileURL);
		return uploadedFileURL;
	}
	
	public String writeFile(String fileName,BufferedInputStream inputStream) throws Exception{
		log.info("Writing file on cloud storage with name..."+fileName);
		byte[] b = new byte[BUFFER_SIZE];
		int readBytes = inputStream.read(b, 0, BUFFER_SIZE);		
		while (readBytes != -1) {
		       storeFile(b, readBytes);
		       readBytes = inputStream.read(b, 0, readBytes);
		}
		inputStream.close();
		destroy();
		String uploadedFileURL=getUploadedFileURLOnGoogleCloudStorage(fileName);
		log.info("Writing file done on cloud storage, fileURL..."+uploadedFileURL);
		return uploadedFileURL;
	}
	
	public String writeFile(String fileName,BufferedInputStream inputStream,String dirName) throws Exception{
		log.info("Writing file on cloud storage with name..."+fileName);
		byte[] b = new byte[BUFFER_SIZE];
		int readBytes = inputStream.read(b, 0, BUFFER_SIZE);		
		while (readBytes != -1) {		   
		       storeFile(b, readBytes);
		       readBytes = inputStream.read(b, 0, readBytes);	       
		}
		inputStream.close();
		destroy();
		String uploadedFileURL=getUploadedFileURLOnGoogleCloudStorage(fileName,dirName);
		log.info("Writing file done on cloud storage, fileURL..."+uploadedFileURL);
		return uploadedFileURL;
	}
	
	public String writeFile(String fileName,BufferedInputStream inputStream,String dirName,String bucketName) throws Exception{
		log.info("Writing file on cloud storage with name..."+fileName);
		byte[] b = new byte[BUFFER_SIZE];
		int readBytes = inputStream.read(b, 0, BUFFER_SIZE);		
		while (readBytes != -1) {		   
		       storeFile(b, readBytes);
		       readBytes = inputStream.read(b, 0, readBytes);	       
		}
		inputStream.close();
		destroy();
		String uploadedFileURL=getUploadedFileURLOnGoogleCloudStorage(fileName,dirName,bucketName);
		log.info("Writing file done on cloud storage, fileURL..."+uploadedFileURL);
		return uploadedFileURL;
	}
	
	
	public void destroy() throws Exception {	
		log.info("Storage service: destroy() method");
	    
	    if(outputStream !=null){
	    	outputStream.close();
	    }
	    
	    if(outputChannel !=null){
	    	outputChannel.close();
	    }
	    
	}
	
	
	public void zipFiles(final GcsFilename targetZipFile,
	        final GcsFilename... filesToZip) throws IOException {
	
	    Preconditions.checkArgument(targetZipFile != null);
	    Preconditions.checkArgument(filesToZip != null);
	    Preconditions.checkArgument(filesToZip.length > 0);
	
	    final int fetchSize = 4 * 1024 * 1024;
	    final int readSize = 2 * 1024 * 1024;
	    GcsOutputChannel outputChannel = null;
	    ZipOutputStream zip = null;
	    try {
	    	System.out.println("===========");
	    	//System.out.println(MediaType.GZIP);
	        GcsFileOptions options = new GcsFileOptions.
	        		Builder().
	        		mimeType("application/zip").
	        		acl("public-read").
	        		build();
	        System.out.println("===========1=========targetZipFile:"+targetZipFile);
	        GcsFilename fileName = new GcsFilename(LinMobileVariables.APPLICATION_BUCKET_NAME+"/test", "CorePerformanceSchema2.csv");
	        outputChannel = gcsService.createOrReplace(fileName, options);
	        if(outputChannel==null){
	        	System.out.println("outputChannel is null");
	        }
	        zip = new ZipOutputStream(Channels.newOutputStream(outputChannel));
	        GcsInputChannel readChannel = null;
	        System.out.println("2222222222222");
	        for (GcsFilename file : filesToZip) {
	            try {
	                final GcsFileMetadata meta = gcsService.getMetadata(file);
	                if (meta == null) {	                   
	                    continue;
	                }
	              
	                ZipEntry entry = new ZipEntry(file.getObjectName());
	                zip.putNextEntry(entry);
	                readChannel = gcsService.openPrefetchingReadChannel(file, 0, fetchSize);
	                final ByteBuffer buffer = ByteBuffer.allocate(readSize);
	                int bytesRead = 0;
	                while (bytesRead >= 0) {
	                    bytesRead = readChannel.read(buffer);
	                    buffer.flip();
	                    zip.write(buffer.array(), buffer.position(), buffer.limit());
	                    buffer.rewind();
	                    buffer.limit(buffer.capacity());
	                }       
	
	            } finally {
	                zip.closeEntry();
	                readChannel.close();
	            }
	        }
	        System.out.println("done........");
	    } finally {
	        zip.flush();
	        zip.close();
	        outputChannel.close();
	    }
	}
	
	/*public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    // read the input stream
	    byte[] buffer = new byte[1024];
	    List<byte[]> allBytes = new LinkedList<byte[]>();
	    InputStream reader = req.getInputStream();
	    while(true) {
	        int bytesRead = reader.read(buffer);
	        if (bytesRead == -1) {
	            break; // have a break up with the loop.
	        } else if (bytesRead < 1024) {
	            byte[] temp = Arrays.copyOf(buffer, bytesRead);
	            allBytes.add(temp);
	        } else {
	            allBytes.add(buffer);
	        }
	    }

	    // init the bucket access
	    GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
	    GcsFilename filename = new GcsFilename("my-bucket", "my-file");
	    Builder fileOptionsBuilder = new GcsFileOptions.Builder();
	    fileOptionsBuilder.mimeType("text/html"); // or "image/jpeg" for image files
	    GcsFileOptions fileOptions = fileOptionsBuilder.build();
	    GcsOutputChannel outputChannel = gcsService.createOrReplace(filename, fileOptions);

	    // write file out
	    BufferedOutputStream outStream = new BufferedOutputStream(Channels.newOutputStream(outputChannel));
	    for (byte[] b : allBytes) {
	        outStream.write(b);
	    }
	    outStream.close();
	    outputChannel.close();
	}*/
	

}