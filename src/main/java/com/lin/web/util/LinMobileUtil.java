package com.lin.web.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

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
import javax.servlet.http.HttpServletRequest;

public class LinMobileUtil {

	private static final Logger log = Logger.getLogger(LinMobileUtil.class.getName());
	
	
   
   public static boolean sendMailOnGAE(String fromAddress,String toAddress,String subject,String message){
		 Properties props = new Properties();
	     Session session = Session.getDefaultInstance(props, null);
	     subject=LinMobileVariables.APPLICATION_TYPE+"-"+subject;
	     try {
	         Message msg = new MimeMessage(session);
	         msg.setFrom(new InternetAddress(LinMobileVariables.SENDER_EMAIL_ADDRESS, "Lin Admin"+" : Sent via :"+fromAddress));
	         msg.addRecipient(Message.RecipientType.TO,
	                          new InternetAddress(toAddress, "Mr. "+toAddress.substring(0, toAddress.indexOf("@"))));
	       
	         //msg.addRecipient(Message.RecipientType.CC,new InternetAddress(fromAddress, "Mr. "+fromAddress.substring(0, fromAddress.indexOf("@"))));
	         msg.setContent(message,"text/html;");
	         msg.setSubject(subject);
	         msg.setText(message);
	         Transport.send(msg);
	         return true;
	     }catch (AddressException e) {
	    	 log.severe("sendMailGAE: AddressException:"+e.getMessage());
	     	 e.printStackTrace();
	     	 return false;
	     } catch (MessagingException e) {
	    	 log.severe("sendMailGAE: MessagingException:"+e.getMessage());
	     	 e.printStackTrace();
	     	 return false;
	     } catch (UnsupportedEncodingException e) {
	    	 log.severe("sendMailGAE: UnsupportedEncodingException:"+e.getMessage());
			 e.printStackTrace();
			 return false;
		 }
	}
   
   /*
    *  Send email 
    *  @author Youdhveer Panwar
    *  @param String fromAddress,
    *         String toAddress,
    *         String toCCAddres,
    *         String subject,
    *         String message
    *                  
    *  @return boolean
    */
   public static boolean sendMailOnGAE(String fromAddress,String toAddress,String toCCAddres,String subject,String message){
		 Properties props = new Properties();
	     Session session = Session.getDefaultInstance(props, null);
	     subject=LinMobileVariables.APPLICATION_TYPE+"-"+subject;
	     try {
	         Message msg = new MimeMessage(session);
	         msg.setFrom(new InternetAddress(LinMobileVariables.SENDER_EMAIL_ADDRESS, "Lin Admin"+" : Sent via :"+fromAddress));
	         msg.addRecipient(Message.RecipientType.TO,
	                          new InternetAddress(toAddress, "Mr. "+toAddress.substring(0, toAddress.indexOf("@"))));
	       
	         msg.addRecipient(Message.RecipientType.CC,new InternetAddress(toCCAddres, ""));
	         msg.setContent(message,"text/html;");
	         msg.setSubject(subject);
	         msg.setText(message);
	         Transport.send(msg);
	         return true;
	     }catch (AddressException e) {
	    	 log.severe("sendMailGAE: AddressException:"+e.getMessage());
	     	 e.printStackTrace();
	     	 return false;
	     } catch (MessagingException e) {
	    	 log.severe("sendMailGAE: MessagingException:"+e.getMessage());
	     	 e.printStackTrace();
	     	 return false;
	     } catch (UnsupportedEncodingException e) {
	    	 log.severe("sendMailGAE: UnsupportedEncodingException:"+e.getMessage());
			 e.printStackTrace();
			 return false;
			}
	}
   public static boolean shareByEmailOnGAE(String toAddress,String subject,String message){
		 Properties props = new Properties();
	     Session session = Session.getDefaultInstance(props, null);

	     try {
	    	 MimeMessage msg = new MimeMessage(session);
	         msg.setFrom(new InternetAddress(LinMobileVariables.SENDER_EMAIL_ADDRESS, "MA Admin"));
	         msg.addRecipient(Message.RecipientType.TO,
	                          new InternetAddress(toAddress, "Mr. "+toAddress.substring(0, toAddress.indexOf("@"))));
	       
	         //msg.addRecipient(Message.RecipientType.CC,new InternetAddress(fromAddress, "Mr. "+fromAddress.substring(0, fromAddress.indexOf("@"))));
	         
	         msg.setSubject(subject,"UTF-8");
	         
	         Multipart mp = new MimeMultipart();
	         MimeBodyPart htmlPart = new MimeBodyPart();
	         htmlPart.setContent(message, "text/html");
             mp.addBodyPart(htmlPart);
             msg.setContent(mp);
             
	         //msg.setContent(message,"text/html");
	         //msg.setSubject(subject);
	         //msg.setText(message);
	         
	         Transport.send(msg);
	         return true;
	     }catch (AddressException e) {
	    	 log.severe("sendMailGAE: AddressException:"+e.getMessage());
	     	 e.printStackTrace();
	     	 return false;
	     } catch (MessagingException e) {
	    	 log.severe("sendMailGAE: MessagingException:"+e.getMessage());
	     	 e.printStackTrace();
	     	 return false;
	     } catch (UnsupportedEncodingException e) {
	    	 log.severe("sendMailGAE: UnsupportedEncodingException:"+e.getMessage());
			 e.printStackTrace();
			 return false;
			}
	}
   
   /*
	 * Get Client IP Address
	 */
	public static String getClientIPAddress(HttpServletRequest request){	
	    String ipAddress=request.getHeader("X-FORWARDED-FOR");  // Due Apache redirection, the real client ip-address will be in this header value
	    
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	        ipAddress = request.getHeader("Proxy-Client-IP");  
	    }  
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	        ipAddress = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	        ipAddress = request.getHeader("HTTP_CLIENT_IP");  
	    }  
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	        ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");  
	    }  
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	        ipAddress = request.getRemoteAddr();  
	    }  
	    if(ipAddress.contains(",")){
	    	ipAddress=ipAddress.substring(0, ipAddress.indexOf(","));
	    }
	    log.warning("getClientIPAddress():: ipAddress:"+ipAddress);
	    return ipAddress;
	}
	
	/*
	 * validate ip address
	 */
	public static boolean isAllowedIPAddress(HttpServletRequest request){
		boolean validIPAddress=false;
		String clientIPAddress=getClientIPAddress(request);
		
		String regExp="(.*)"+clientIPAddress+"(.*)";
		if(LinMobileConstants.ALLOWED_IP_ADDRESS.matches(regExp)){
			validIPAddress=true;
			log.warning("Allowed IP addess:"+clientIPAddress);
		}else{
			validIPAddress=false;
			log.severe("IP Addess not allowed:"+clientIPAddress);
		}
		
		/*String [] allowedIPAddressArray=ETradeConstants.ALLOWED_IP_ADDRESS.split(",");
		if(allowedIPAddressArray !=null && allowedIPAddressArray.length>0){
			for(int i=0;i<allowedIPAddressArray.length;i++){
				if(clientIPAddress.equalsIgnoreCase(allowedIPAddressArray[i])){
					validIPAddress=true;
					break;
				}
			}
		}*/
		return validIPAddress;
	}
	
	public static String convertPassToMD5(String password){		
		try {
			 log.warning("convertPassTo......");
			 MessageDigest md = MessageDigest.getInstance("MD5");
			 md.update(password.getBytes());
			 byte byteData[] = md.digest();
			 StringBuffer sb = new StringBuffer();
			 for (int i = 0; i < byteData.length; i++){
			        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			 }
			 return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			 log.severe("NoSuchAlgorithmException():: :"+e.getMessage());
			 e.printStackTrace();
			 return null;
		}
	}
	
		
	public static boolean isNumeric(String str){  
		  if(str == null) {
			  return false; 
		  }
		  try {  
		    double d = Double.parseDouble(str);  
		  }catch(NumberFormatException nfe){  
		    return false;  
		  }  
		  return true;  
	}
	
	public static String changeFromToLog(String updatedFieldName, String earlierValue, String currentValue) {
		if((earlierValue == null || earlierValue.trim().length() == 0) && (currentValue == null || currentValue.trim().length() == 0)) {
			return "";
		}else if(earlierValue == null || earlierValue.trim().length() == 0) {
			return updatedFieldName + " set to "+currentValue + ".<br>";
		}else if(currentValue == null || currentValue.trim().length() == 0) {
			return "Cleared all values of "+updatedFieldName + ", earlier values : "+earlierValue + ".<br>";
		}
		return updatedFieldName + " changed from "+earlierValue+ " to "+currentValue + ".<br>";
	}
	
	public static String getCampaignStatusValue(int i) {
		if(CampaignStatusEnum.All.ordinal() == i) {
			return CampaignStatusEnum.All.name();
		}else if(CampaignStatusEnum.Active.ordinal() == i) {
			return CampaignStatusEnum.Active.name();
		}else if(CampaignStatusEnum.Running.ordinal() == i) {
			return CampaignStatusEnum.Running.name();
		}else if(CampaignStatusEnum.Paused.ordinal() == i) {
			return CampaignStatusEnum.Paused.name();
		}else if(CampaignStatusEnum.Draft.ordinal() == i) {
			return CampaignStatusEnum.Draft.name();
		}else if(CampaignStatusEnum.Completed.ordinal() == i) {
			return CampaignStatusEnum.Completed.name();
		}else if(CampaignStatusEnum.Archived.ordinal() == i) {
			return CampaignStatusEnum.Archived.name();
		}else if(CampaignStatusEnum.Approve.ordinal() == i) {
			return CampaignStatusEnum.Approve.name();
		}else if(CampaignStatusEnum.Ready.ordinal() == i) {
			return CampaignStatusEnum.Ready.name();
		}else if(CampaignStatusEnum.Inactive.ordinal() == i) {
			return CampaignStatusEnum.Inactive.name();
		}else if(CampaignStatusEnum.Canceled.ordinal() == i) {
			return CampaignStatusEnum.Canceled.name();
		} else {
			log.warning("Not a valid Enum value : "+i);
			return "";
		}
	}
}
