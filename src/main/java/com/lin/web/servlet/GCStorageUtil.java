package com.lin.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions.Builder;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.lin.web.util.LinMobileVariables;


/*
 * @author Youdhveer Panwar
 * This is old google cloud storage util to upload file at google cloud storage
 */
@SuppressWarnings("deprecation")
public class GCStorageUtil {

  
private static final int BUFFER_SIZE = 4* 1024 * 1024;
final int fetchSize = 4 * 1024 * 1024;
final int readSize = 2 * 1024 * 1024;

private FileWriteChannel writeChannel = null;
FileService fileService = FileServiceFactory.getFileService();
private OutputStream os = null;
ZipOutputStream zip=null;
private static final Logger log = Logger.getLogger(GCStorageUtil.class.getName());

@SuppressWarnings("static-access")
public void init(String fileName, String mime) throws Exception {
log.info("0) Storage service:init() method:  file name:"+fileName+" and mime:"+mime);
    
    GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
            .setAcl("public_read")
            .setBucket(LinMobileVariables.APPLICATION_BUCKET_NAME)
            .setKey(fileName)
            .setMimeType(mime);
    AppEngineFile writableFile = fileService.createNewGSFile(builder.build());
    boolean lock = true;
    writeChannel = fileService.openWriteChannel(writableFile, lock);
    os = Channels.newOutputStream(writeChannel);
}

/*
 *  Intialize Google cloude storage settings
 *  @params location, fileName, mime
 *  
 */
public void init(String dirName,String fileName, String mime) throws Exception {	
	log.info("1) Storage service:init() method:  file name:"+fileName+" and mime:"+mime);
    
    GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
            .setAcl("public_read")
            .setBucket(LinMobileVariables.APPLICATION_BUCKET_NAME+"/"+dirName)
            .setKey(fileName)
            .setMimeType(mime);
    AppEngineFile writableFile = fileService.createNewGSFile(builder.build());
    boolean lock = true;
    writeChannel = fileService.openWriteChannel(writableFile, lock);
    os = Channels.newOutputStream(writeChannel);
}

/*
 *  Intialize Google cloude storage settings
 *  @params location, fileName, mime
 *  
 */
public void init(String dirName,String fileName, String mime,String bucketName) throws Exception {	
	log.info("2) Storage service:init(....) method:  file name:"+fileName+" and mime:"+mime+" and bucketName:"+bucketName);
    
    GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
            .setAcl("public_read")
            .setBucket(bucketName+"/"+dirName)
            .setKey(fileName)
            .setMimeType(mime);
    AppEngineFile writableFile = fileService.createNewGSFile(builder.build());
    boolean lock = true;
    writeChannel = fileService.openWriteChannel(writableFile, lock);
    os = Channels.newOutputStream(writeChannel);
}


public void initZip(String dirName,String fileName, String mime,String bucketName) throws Exception {	
	log.info("3) Storage service:init(....) method:  file name:"+fileName+" and mime:"+mime+" and bucketName:"+bucketName);
    
    GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
            .setAcl("public_read")
            .setBucket(bucketName+"/"+dirName)
            .setKey(fileName)
            .setMimeType(mime);
            //.setContentEncoding("gzip");
    AppEngineFile writableFile = fileService.createNewGSFile(builder.build());
    boolean lock = true;
    writeChannel = fileService.openWriteChannel(writableFile, lock);
    os = Channels.newOutputStream(writeChannel);
    zip = new ZipOutputStream(os);
}

public void storeFile(byte[] b, int readSize) throws Exception {	
    os.write(b, 0, readSize);
    os.flush();
}

public void storeZipFile(byte[] b, int readSize) throws Exception {	
	zip.write(b, 0, readSize);
	zip.flush();
}

public String readTextFile(String fileName) throws Exception{
	log.info("Reading the text file's first line only...........");
	String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME + "/" + fileName;
    AppEngineFile readableFile = new AppEngineFile(filename);
    FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
    BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
    String line = reader.readLine();
    readChannel.close();
    return line;
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
	log.info("1) Writing file on cloud storage with name..."+fileName);
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
	log.info("2) Writing file on cloud storage with name..."+fileName);
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
	log.info("3) Writing file on cloud storage with name..."+fileName);
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

public String writeFile(String fileName,BufferedInputStream inputStream,String dirName,String bucketName) {
	log.info("4) Writing file on cloud storage with name..."+fileName);
	String uploadedFileURL=null;
	byte[] b = new byte[BUFFER_SIZE];
	int readBytes;
	try {
		readBytes = inputStream.read(b, 0, BUFFER_SIZE);
		//log.info("After reading input stream..readBytes :"+readBytes);
		while (readBytes != -1) {		   
		       storeFile(b, readBytes);
		       readBytes = inputStream.read(b, 0, readBytes);	 
		       //log.info("readBytes : "+readBytes);
		}
		//log.info("Before closing input stream..");
		inputStream.close();
		destroy();
		uploadedFileURL=getUploadedFileURLOnGoogleCloudStorage(fileName,dirName,bucketName);
		//log.info("Writing file done on cloud storage, fileURL..."+uploadedFileURL);
	} catch (IOException e) {
		log.severe("IOException : "+e.getMessage());
	} catch (Exception e) {
		log.severe("Exception : "+e.getMessage());
	}finally{
		log.info("Finally :Writing file done on cloud storage: uploadedFileURL : "+uploadedFileURL);
	}
	
	return uploadedFileURL;
}


@SuppressWarnings("deprecation")
public  String uploadFileAtCloudStorageViaZip(String csvData,
		String fileName, String dirName,String bucketName)  {
	
	ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();	 
	InputStream input = new ByteArrayInputStream(csvData.getBytes());
	
	try {	
		initZip(dirName, fileName, "application/csv", bucketName);		
		zip = new ZipOutputStream(zipBaos);
        
		String zipFileName=fileName.replace(".zip", ".csv");
		System.out.println("zipFileName:"+zipFileName);
        ZipEntry zipEntry1 = new ZipEntry(zipFileName);     // Create new zip entry																	 // the constructor parameter is filename of the entry
        zip.putNextEntry(zipEntry1);            // add the zip entry to the ZipOutputStream
        
       /* byte[] file1Contents = IOUtils.toByteArray(input);
        zip.write(file1Contents);			   // write contents (byte array) to the ZipOutputStream
       */
        BufferedInputStream inputStream=new BufferedInputStream(input);
       
        byte[] b = new byte[readSize];
    	int readBytes = inputStream.read(b, 0, readSize);
    	while (readBytes != -1) {
    		storeZipFile(b, readBytes);
    	    readBytes = inputStream.read(b, 0, readBytes);	       
    	}
    	
    	zip.closeEntry();	
        zip.close();
        
    	
        byte[] zipData = zipBaos.toByteArray();
        os.write(zipData);
       
        os.close();
        writeChannel.closeFinally();    
        
		String uploadedFileURL=getUploadedFileURLOnGoogleCloudStorage(fileName,dirName,bucketName);	
		
		
		return uploadedFileURL;
	} catch (FileNotFoundException e) {
		log.severe(":FileNotFoundException::" + e.getMessage());
		
		return null;
	} catch (Exception e) {
		log.severe(":Exception::" + e.getMessage());
		
		return null;
	}
}

public InputStream resizedImage(byte[] oldImageData,int width,int height) throws IOException{
	ImagesService imagesService = ImagesServiceFactory.getImagesService();
    Image oldImage = ImagesServiceFactory.makeImage(oldImageData);
    log.info(this.getClass()+":oldImage height:"+oldImage.getHeight()+" and width:"+oldImage.getWidth());
    System.out.println("oldImage height:"+oldImage.getHeight()+" and width:"+oldImage.getWidth());
    Transform resize = ImagesServiceFactory.makeResize(width,height);
    Image newImage = imagesService.applyTransform(resize, oldImage);
    byte[] newImageData = newImage.getImageData();
    System.out.println("After resize newImage height:"+newImage.getHeight()+" and width:"+newImage.getWidth());
    log.info(this.getClass()+"After resize newImage height:"+newImage.getHeight()+" and width:"+newImage.getWidth());
    InputStream inputStream = new ByteArrayInputStream(newImageData);
    return inputStream;
}

public void destroy() throws Exception {	
	log.info("Storage service: destroy() method");
    os.close();
    writeChannel.closeFinally();
}


/*
 * uploadReportOnCloudStorage
 * @param : String csvDate
 *           String fileName 
 *           String dirName (directory under a bucket on cloud storage)
 *           String bucketName
 * @return  : UploadedFileURL
 */
public String uploadReportOnCloudStorage(String csvDate,
		String fileName, String dirName,String bucketName){
	String uploadedFileURL =null;
	InputStream input=null;
	
	try {
		
		try{
			input = new ByteArrayInputStream(csvDate.getBytes());
			BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
			log.info("1) Going to upload file on cloud storage: fileName:"
					+ fileName+" :location dirName:"+dirName+" and bucketName:"+bucketName);
			init(dirName, fileName, "application/CSV",bucketName);

			if(dirName !=null){
				uploadedFileURL = writeFile(fileName,bufferedInputStream,dirName,bucketName);
			}else{
				uploadedFileURL = writeFile(fileName,bufferedInputStream);
			}		
		}finally{
			log.info("inside finally : uploadedFileURL : "+uploadedFileURL);
			if(input != null) input.close();
		}
			
	} catch (FileNotFoundException e) {
		log.severe(":FileNotFoundException::" + e.getMessage());			
	} catch (Exception e) {
		log.severe(":Exception::" + e.getMessage());			
	}finally{
		log.info("uploadedFileURL : "+uploadedFileURL);
		
	}
	return uploadedFileURL;
}


}