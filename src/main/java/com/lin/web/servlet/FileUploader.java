package com.lin.web.servlet;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.lin.web.util.CloudStorageUtil;


public class FileUploader extends HttpServlet{

	 private static final long serialVersionUID = 1L;
	 private File csvData ;
	
	 private static final Logger log = Logger.getLogger(FileUploader.class.getName()); 
	 @Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {		   
		   
		    //response.setContentType("text/plain");
		    log.info("File uploader servlet called....");
		    CloudStorageUtil fileUtil=new CloudStorageUtil();
		    ServletFileUpload upload = new ServletFileUpload();
		    FileItemIterator iter;
		    RequestDispatcher view=request.getRequestDispatcher("Success.jsp");
			try {
				iter = upload.getItemIterator(request);
				 while (iter.hasNext()) {
				        FileItemStream item = iter.next();
				        String fileName = item.getName();
				        String mime = item.getContentType();	
				        log.info("fileName:"+fileName+ " and mime:"+mime);
				        String uploadedFileURL=fileUtil.writeFileOnCloudStorage(fileName, "test_data", null, mime);
				        log.info(" File uploaded/written successfully....URL:"+uploadedFileURL);
				 }				 
				 view.forward(request, response);
			} catch (FileUploadException e) {				
				log.severe(this.getServletName()+":FileUploadException::"+e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				log.severe(this.getServletName()+":Exception::"+e.getMessage());				
				e.printStackTrace();
			}
	}

}