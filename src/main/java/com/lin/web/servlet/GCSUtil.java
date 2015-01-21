package com.lin.web.servlet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.logging.Logger;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

/*
 * @author Youdhveer Panwar
 * @created 14-sep-2014
 * 
 * This is a cloud storage util which will read and upload file on google cloud storage
 */
@SuppressWarnings("deprecation")
public class GCSUtil {

  
private static final int BUFFER_SIZE = 4* 1024 * 1024;
final int fetchSize = 4 * 1024 * 1024;
final int readSize = 2 * 1024 * 1024;

/**
 * This is the service from which all requests are initiated.
 * The retry and exponential backoff settings are configured here.
 */
private final GcsService gcsService =
    GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());


private static final Logger log = Logger.getLogger(GCSUtil.class.getName());

/**
 * Writes the provided object to the specified file using Java serialization. One could use
 * this same technique to write many objects, or with another format such as Json or XML or just a
 * DataOutputStream.
 *
 * Notice at the end closing the ObjectOutputStream is not done in a finally block.
 * See below for why.
 */
private void writeObjectToFile(GcsFilename fileName, Object content) throws IOException {
  GcsOutputChannel outputChannel =
      gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
  @SuppressWarnings("resource")
  ObjectOutputStream oout =
      new ObjectOutputStream(Channels.newOutputStream(outputChannel));
  oout.writeObject(content);
  oout.close();
}

/**
 * Writes the byte array to the specified file. Note that the close at the end is not in a
 * finally.This is intentional. Because the file only exists for reading if close is called, if
 * there is an exception thrown while writing the file won't ever exist. (This way there is no
 * need to worry about cleaning up partly written files)
 */
private void writeToFile(GcsFilename fileName, byte[] content) throws IOException {
  @SuppressWarnings("resource")
  GcsOutputChannel outputChannel =
      gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
  outputChannel.write(ByteBuffer.wrap(content));
  outputChannel.close();
}

/**
 * Reads an object from the specified file using Java serialization. One could use this same
 * technique to read many objects, or with another format such as Json or XML or just a
 * DataInputStream.
 *
 * The final parameter to openPrefetchingReadChannel is a buffer size. It will attempt to buffer
 * the input by at least this many bytes. (This must be at least 1kb and less than 10mb) If
 * buffering is undesirable openReadChannel could be called instead, which is totally unbuffered.
 */
private Object readObjectFromFile(GcsFilename fileName)
    throws IOException, ClassNotFoundException {
  GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, 1024 * 1024);
  try (ObjectInputStream oin = new ObjectInputStream(Channels.newInputStream(readChannel))) {
    return oin.readObject();
  }
}

/**
 * Reads the contents of an entire file and returns it as a byte array. This works by first
 * requesting the length, and then fetching the whole file in a single call. (Because it calls
 * openReadChannel instead of openPrefetchingReadChannel there is no buffering, and thus there is
 * no need to wrap the read call in a loop)
 *
 * This is really only a good idea for small files. Large files should be streamed out using the
 * prefetchingReadChannel and processed incrementally.
 */
private byte[] readFromFile(GcsFilename fileName) throws IOException {
  int fileSize = (int) gcsService.getMetadata(fileName).getLength();
  ByteBuffer result = ByteBuffer.allocate(fileSize);
  try (GcsInputChannel readChannel = gcsService.openReadChannel(fileName, 0)) {
    readChannel.read(result);
  }
  return result.array();
}

private void writeToFile(GcsFilename fileName,GcsFileOptions gcsFileOptions, byte[] content) throws IOException {
	  @SuppressWarnings("resource")
	  GcsOutputChannel outputChannel =
	      gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
	  outputChannel.write(ByteBuffer.wrap(content));
	  outputChannel.close();
}


/*
 * @author Youdhveer Panwar
 * This is a method using new cloud storage client api to upload file
 * @date 16-Sep-2014 (CreatedOn)
 *  
 */
public String uploadFileUsingGCSClient(String csvDate, String fileName, String dirName,String bucketName) throws IOException {
	log.info("Going for read file before writing at cloud storage ..");
	  // init the bucket access
    GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
    GcsFilename filename = new GcsFilename(bucketName+"/"+dirName, fileName);
    GcsFileOptions fileOptions = new GcsFileOptions.Builder()
							    .mimeType("application/CSV")							   
							    .acl("public-read")
							    //.addUserMetadata("myfield1", "my field value")
							    .build();
    GcsOutputChannel outputChannel = gcsService.createOrReplace(filename, fileOptions);
    
    // write file using this stream
    BufferedOutputStream outStream = new BufferedOutputStream(Channels.newOutputStream(outputChannel));
    
    // read the input stream
    byte[] buffer = new byte[1024];
    //List<byte[]> allBytes = new LinkedList<byte[]>();
    InputStream reader = new ByteArrayInputStream(csvDate.getBytes());
    while(true) {
        int bytesRead = reader.read(buffer);
        //log.info("bytesRead:"+bytesRead);
        if (bytesRead == -1) {
            break; // have a break up with the loop.
        } else if (bytesRead < 1024) {
            byte[] temp = Arrays.copyOf(buffer, bytesRead);
            //allBytes.add(temp);
            outStream.write(temp);
           
        } else {
        	outStream.write(buffer);
            //allBytes.add(buffer);
        }
    }
    log.info("Write at cloud storage done");
   
   /* for (byte[] b : allBytes) {
        outStream.write(b);
    }*/
    
    outStream.close();
    outputChannel.close();
    String uploadedFileURL="gs://"+bucketName+"/"+dirName+"/"+fileName;
    log.info("uploadedFileURL : "+uploadedFileURL);
    return uploadedFileURL;
  }

public static String uploadCsvFile(String csvDate,
		String fileName, String dirName,String bucketName) throws IOException {
   
	GCSUtil gcsUtil = new GCSUtil();
	
    //GcsService gcsService = GcsServiceFactory.createGcsService();
    GcsFilename gcsFilename = new GcsFilename(bucketName+"/"+dirName, fileName);
    GcsFileOptions gcsFileOptions = new GcsFileOptions.Builder()
        .mimeType("text/csv")
        .acl("public-read")
        //.addUserMetadata("myfield1", "my field value")
        .build();

    byte[] content=gcsUtil.readFromFile(gcsFilename);
    
    gcsUtil.writeToFile(gcsFilename, gcsFileOptions, content);
    log.info("Wrote " + Arrays.toString(content));

    String uploadedFileURL="gs://"+bucketName+"/"+dirName+"/"+fileName;
    log.info("uploadedFileURL : "+uploadedFileURL);
    return uploadedFileURL;
        
  }


}