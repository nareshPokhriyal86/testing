package com.lin.web.util;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;



public class EmailUtil { 
	public static Logger logger = Logger.getLogger("FileUploadUtil");
	
	public static void sendMessage(String toField, String fromField, String subject, String messageText,byte[] byteArr,String fileName,String attchmentContents){
        try{
        	Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);		 
			String msgBody ="";
            String emailHtml =messageText+" <hr> "+attchmentContents;
			Multipart mp = new MimeMultipart(); 
			MimeBodyPart htmlPart = new MimeBodyPart(); 
			htmlPart.setContent(emailHtml, "text/html"); 
			mp.addBodyPart(htmlPart); 

			MimeBodyPart attachment = new MimeBodyPart(); 
			attachment.setFileName(fileName+".pdf"); 
			DataSource src = new ByteArrayDataSource(byteArr, "application/pdf"); 
			attachment.setDataHandler(new DataHandler(src)); 
			mp.addBodyPart(attachment); 

			Message msg = new MimeMessage(session); 
			msg.setFrom(new InternetAddress(fromField, LinMobileVariables.SENDER_NAME)); 
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toField, "Mr. User")); 
			msg.setSubject(subject); 
			msg.setText(msgBody); 
			msg.setContent(mp); 
			msg.saveChanges(); 
			Transport.send(msg); 
			logger.info("Email Successfuly sent.");
		} catch (AddressException e) {
			
			logger.severe("Exception Found in MailSending."+e.getMessage());
		} catch (MessagingException e) {
			
			logger.severe("Exception Found in MailSending."+e.getMessage());
		}catch(Exception e){
			
			logger.severe("Exception Found in MailSending."+e.getMessage());
		}
	
	}
	
	public static void sendMailWithAttachment(String toField, String fromField, String subject, String messageText,byte[] byteArr,String fileName,String attchmentContents){
        try{
        	Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);	 
			String msgBody ="";
            String emailHtml =messageText+" <hr> "+attchmentContents;
			Multipart mp = new MimeMultipart(); 
			MimeBodyPart htmlPart = new MimeBodyPart(); 
			htmlPart.setContent(emailHtml, "text/html"); 
			mp.addBodyPart(htmlPart); 

			MimeBodyPart attachment = new MimeBodyPart(); 
			attachment.setFileName(fileName+".xls"); 
			DataSource src = new ByteArrayDataSource(byteArr, "application/excel"); 
			attachment.setDataHandler(new DataHandler(src)); 
			mp.addBodyPart(attachment);

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromField, LinMobileVariables.SENDER_NAME)); 
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toField, "")); 
			msg.setSubject(subject);
			msg.setText(msgBody); 
			msg.setContent(mp); 
			msg.saveChanges(); 
			Transport.send(msg); 
			logger.info("Email Successfuly sent.");
		} catch (AddressException e) {
			
			logger.severe("Exception Found in MailSending to "+toField+"."+e.toString());
		} catch (MessagingException e) {
			
			logger.severe("Exception Found in MailSending to "+toField+"."+e.toString());
		}catch(Exception e){
			
			logger.severe("Exception Found in MailSending to "+toField+"."+e.toString());
		}
	
	}
	
	public static void sendAuthMail(String toField, String fromField, String subject, String messageText, String attchmentContents) {
        try{
        	logger.info("Sending mail : to -> "+toField+", from -> "+fromField+", subject -> "+subject);
        	Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);		 
			String msgBody ="";
            /*String emailHtml =messageText+" <hr> "+attchmentContents;*/
			String emailHtml =messageText+" <br><br> "+attchmentContents;
			Multipart mp = new MimeMultipart(); 
			MimeBodyPart htmlPart = new MimeBodyPart(); 
			htmlPart.setContent(emailHtml, "text/html"); 
			mp.addBodyPart(htmlPart); 

			Message msg = new MimeMessage(session); 
			msg.setFrom(new InternetAddress(fromField, LinMobileVariables.SENDER_NAME)); 
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toField)); 
			msg.setSubject(subject); 
			msg.setText(msgBody); 
			msg.setContent(mp);
			msg.saveChanges(); 
			Transport.send(msg); 
			logger.info("Email Successfuly sent.");
		
		}catch(Exception e){
			
			logger.severe("Exception Found in MailSending."+e.getMessage());
		}
	}
	
	
	public static void mailForForgotPassword(String toField, String fromField, String subject,String userName,String userId, String password){
		try { 
		    Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);		 
			String msgBody ="";
            String emailHtml ="Hi "+userName+" Your New Password is successfully created.<br> UserId: "+userId+" Password :"+password+"";
			Multipart mp = new MimeMultipart(); 

			MimeBodyPart htmlPart = new MimeBodyPart(); 
			htmlPart.setContent(emailHtml, "text/html"); 
			mp.addBodyPart(htmlPart);
			
			Message msg = new MimeMessage(session); 
			msg.setFrom(new InternetAddress(fromField, LinMobileVariables.SENDER_NAME)); 
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toField, "Mr. "+userName)); 
			msg.setSubject(subject); 
			msg.setText(msgBody); 
			msg.setContent(mp); 
			msg.saveChanges(); 
			Transport.send(msg); 
			}catch(Exception e){ 
	            logger.severe("Exception Found in mail Sending"+e.getMessage());
				 
			} 
	}
	public static boolean validateEmailAddress(String emailAddress){
		  final String EMAIL_ADDRESS_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		  Pattern pattern = Pattern.compile(EMAIL_ADDRESS_PATTERN);		  
		  Matcher matcher = pattern.matcher(emailAddress);
		  return matcher.matches();
	}
	
  
  
	
}
