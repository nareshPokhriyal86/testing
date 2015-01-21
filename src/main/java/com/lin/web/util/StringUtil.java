package com.lin.web.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.api.LifecycleManager;
import com.lin.web.dto.CommonDTO;


public class StringUtil {
	
	private static final Logger log = Logger.getLogger(LinMobileUtil.class.getName());

	public static final String BLANK_STRING = "";
	public static final String WHITESPACE = " ";
	
	public static short getShortValue(String value){
		short val = 0;
		Short shortObj = Short.valueOf(value);
		val = shortObj.shortValue();
		return val;
	}
   

	 /**
     * Converts a String to int
     * @param String - value
     * @param int - default value 
     * @return int 
     */
	public static int getIntegerValue(String value, int defaultValue){
		int i = 0;
		 try {
			 if ( value == null || value.trim().equals(BLANK_STRING)){
				 return defaultValue;
			 }
			value = value.indexOf(".")>0? value.substring(0,value.indexOf(".")):value;
			i = Integer.parseInt(value);
		} catch (NumberFormatException numberFormatExp) {
			return defaultValue;
		}
		return i;
	}
	
	public static int getIntegerValue(String value){
		if(value==null || value.length() == 0){
			return 0;
		}
		int i=0;
		 try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException numberFormatExp) {
			return i;	
		}
		return i;
	}


	public static long getLongValue(String value){
		if(value==null || value.length() == 0){
			return 0;
		}
		long i=0;
		 try {
			i = Long.parseLong(value);
		} catch (NumberFormatException numberFormatExp) {
			return i;	
		}
		return i;
	}
	
	public static double getDoubleValue(String value){
		if(value==null || value.length() == 0){
			return 0;
		}
		double i=0;
		 try {
			 DecimalFormat twoDigitformat = new DecimalFormat("#.##");
			 twoDigitformat.setMaximumFractionDigits(2);
			return Double.valueOf(twoDigitformat.format(Double.parseDouble(value)));
			
		} catch (NumberFormatException numberFormatExp) {
			return i;	
		}
	}

	public static double getDoubleValue(String value,int digit){
		if(value==null){
			value="0";
		 }
		double i=0;
		 try {
			 DecimalFormat digitformat = new DecimalFormat("#.##");
			 digitformat.setMaximumFractionDigits(digit);
			return Double.valueOf(digitformat.format(Double.parseDouble(value)));
			
		} catch (NumberFormatException numberFormatExp) {
			return i;	
		}
	}
	

	 /**
     * get the Boolean value
     * @param String - value
     * @return boolean 
     */
	public static boolean getBooleanValue(String value){
		 if (value == null)
			 return false;
	    if(value.equals("true"))		
		  return true;
	 return value.equals("1")?true:false;
	}
	
	 /**
     * Construct the random String
     * @param String - filename
     * @return String  
     */
	public static String getRandomString(String fileName) {
		char fileNameArray[] = fileName.toCharArray();
		int fileNameLen = fileNameArray.length;
		int c = 'A';
		int r1 = 0, z=0;
		StringBuilder pw = new StringBuilder("");
		for (int j = 0; j < 25; j++) {
			r1 = (int) (Math.random() * 4);
			switch (r1) {
			case 0:
				c = '0' + (int) (Math.random() * 9);
				if (c>'9'){
					z = reset(z,fileNameLen);
					c = fileNameArray[z];
				}
				break;
			case 1:
				c = 'a' + (int) (Math.random() * 25);
				if (c<'a' && c>'z'){
					z = reset(z,fileNameLen);
					c = fileNameArray[z];
				}
				break;
			case 2:
				c = 'A' + (int) (Math.random() * 25);
				if (c<'A' && c>'Z'){
					z = reset(z,fileNameLen);
					c = fileNameArray[z];
				}
				break;
			}
			pw.append((char) c);
		}
		fileNameArray = null;
		String s="";
		try{
			s =fileName.substring(fileName.lastIndexOf(".")); 
			if (s.equalsIgnoreCase(".tmp")){
				s=".jpg";
			}
		}catch(ArrayIndexOutOfBoundsException arrayExp){
			s="";
		}
		return new String(pw+s.trim());
	}
	
	private static int reset(int z, int len){
		if (z>=len){
			z=0;
		}
		return z;
	}
	public static void main(String args[]) throws IOException,Exception{
	}
	
	 /**
     * Replace Single and Double quote in a String
     * @param String  
     * @return String 
     */
	public static final String replaceSpecialCharctor(String str){
		String strReplaced = str;
		if (str.indexOf("'")>=0)
			strReplaced = str.replaceAll("'","\\\\'");
		if (str.indexOf("\"")>=0)
			strReplaced = str.replaceAll("\"","\\\\\"");
		
		
		return strReplaced;
	}
	 /**
     * Checks a String for Blank
     * @param String  
     * @return boolean
     */
	public static final boolean isBlank(String value) {		
		return value != null ? BLANK_STRING.equals(value) : true;
	}
	
	public static final String getBlankIfNull(String value){
		return value != null ? value : BLANK_STRING;
	}
	 /**
     * Concates a String[] in a String seperated by a delimiter
     * @param String[]  
     * @param  delimiter
     * @return String 
     */
	public static String arrayToString(String[] array, String delimiter) {
	    StringBuilder strValue = new StringBuilder();
	    if (array != null ){
	    if (array.length > 0) {
	        strValue.append(array[0]);
	        	for (int i=1; i<array.length; i++) {
	        		strValue.append(delimiter);
	        		strValue.append(array[i]);
	        	}
	    	}
	    }
	    return strValue.toString();
	}

	 /**
     * Concates two  String[] str1, String[] str2
     * @param String[]  str1
     * @param String[]  str2
     * @return String [] str
     */
	
	public static String[] concatStringArrays(String[] str1, String[] str2) {
		String[] str= new String[str1.length+str2.length];
		System.arraycopy(str1, 0, str, 0, str1.length);
		System.arraycopy(str2, 0, str, str1.length, str2.length);
        return str;
	}
	
	 /**
     * return date format dd.MM.yyyy HH:mm:ss
     * @param String  date 
     * @return Date date
     */
	public static Date getTimeStampFromString(String eventDate){
	    //String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String DATE_FORMAT = "dd.MM.yyyy";
	    DateFormat formatter= new SimpleDateFormat(DATE_FORMAT);
	   try {
			Date date = (Date)formatter.parse(eventDate);
			return date;
		} catch (ParseException e) {
			System.out.println("Exception in converting string to date:"+e);
			e.printStackTrace();
			return null;
		} 
	   
	  }
	
	/**
     * return date format dd.MM.yyyy HH:mm:ss for calculate Time only
     * @param String  date 
     * @return Date time
     */
	public static Date getTimeFromString(String eventDateTime) {
	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	Date d;
	try {
		d = df.parse(eventDateTime);
		Calendar gc = new GregorianCalendar();
		gc.setTime(d);		
		Date time = gc.getTime();		
		return time;
	} catch (ParseException e) {	
		e.printStackTrace();
		return null;
	} 
	
	}
	
	/**
     * Converts YYYY-MM-DD to dd.MM.yyyy format 
     * @param String  date in YYYY-MM-DD
     * @ return String date in dd.MM.yyyy 
     */
	public static String getDateFormatDDMMYYY(String eventDate) {
		String[] dateParts = eventDate.split("-");
		StringBuffer sb = new StringBuffer();
		for (int ii=dateParts.length-1; ii>0; ii--) {
		  sb.append( dateParts[ii] + "." );
		}
		sb.append( dateParts[0] );
		return sb.toString();
	}
	
	public static java.sql.Date getDateFormatYYYYMMDD(String eventDate) {		
	    DateFormat formatter= new SimpleDateFormat("yyyy-mm-dd");
	    try {
			Date date = (Date)formatter.parse(eventDate);
			
    		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			return sqlDate;
		} catch (ParseException e) {
			System.out.println("Exception in converting string to date:"+e);
			e.printStackTrace();
			return null;
		} 
	}
	
	public static Date convertSQLDateToUtilDate(Date eventDate) {		
	   return null;
	}
	
	public static String getDoubleStringValue(String value,int digit){
		if(value==null){
			value="0";
		 }		
		 try {
			 DecimalFormat digitformat = new DecimalFormat();
			 digitformat.setMaximumFractionDigits(digit);
			return digitformat.format(Double.parseDouble(value));
			
		} catch (NumberFormatException numberFormatExp) {
			return "0";	
		}
	}
	
	public static String removeMediaPlanFormatters(String str) {
		if(str != null && str.trim().length() > 0) {
			str = str.replaceAll("\\$", "");
			str = str.replaceAll(",","");
			str = str.replaceAll("%", "");
			return str.trim();
		}
		else {
			return "";
		}
    }
	
	public static double getUnformattedDoubleValueForMediaPlan(String value){
		if(value==null){
			value="0";
		}
		else {
			value = value.trim();
		}
		double i=0;
		 try {
			 DecimalFormat twoDigitformat = new DecimalFormat("#.##");
			 twoDigitformat.setMaximumFractionDigits(2);
			return Double.valueOf(twoDigitformat.format(Double.parseDouble(removeMediaPlanFormatters(value))));
			
		} catch (NumberFormatException numberFormatExp) {
			return i;	
		}
	}
	
	public static String capitaliseFirstLetters(String value){
		if(value==null){
			value="";
		}
		else {
			value = value.trim();
		}
		
		int length = value.length();
		if(length == 1) {
			value = value.toUpperCase();
		}
		else if(length > 1) {
			String[] arr = value.split(" ");
			value ="";
			for(String str : arr) {
				str = str.trim();
				length = str.length();
				if(length == 0) {
					continue;
				}
				if(length == 1) {
					str = str.toUpperCase();
				}
				else if(length > 1) {
					str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
				}
				value = value + str + " ";
			}
			value =value.trim();
		}
		return value;
	}
	
	public static String getHashedValue(String stringToHash) {
		if(stringToHash == null || stringToHash.length() == 0) {
			log.info("Null or Empty String passed");
			return "";
		}
		return EncriptionUtil.getEncriptedStrMD5(stringToHash);
	}
	
	public static String hashToMaxLength(String stringToHash, long length) {
		String hashedString = "";
		if(stringToHash != null && stringToHash.trim().length() > 0) {
			hashedString = stringToHash.trim();
			int strLength = hashedString.length();
			while(strLength > length) {
				hashedString = getHashedValue(hashedString);
				strLength = hashedString.length();
			}
		}else {
			log.info("Null or Empty String passed");
		}
		return hashedString;
	}
	
	public static List<Long> commaSeperatedToNumericList(String commaSeperatedStr) {
		log.info("commaSeperatedStr : "+commaSeperatedStr);
		List<Long> returnList = new ArrayList<>();
		if(commaSeperatedStr != null && commaSeperatedStr.length() > 0) {
			String[] arr = commaSeperatedStr.split(",");
			for(String str : arr) {
				long val = getLongValue(str.trim());
				if(val > 0) {
					returnList.add(val);
				}else {
					log.info("Non numeric value : "+str);
				}
			}
		}
		return returnList;
	}
	
	public static List<String> commaSeperatedToList(String commaSeperatedStr) {
		log.info("commaSeperatedStr : "+commaSeperatedStr);
		List<String> returnList = new ArrayList<>();
		if(commaSeperatedStr != null && commaSeperatedStr.length() > 0) {
			String[] arr = commaSeperatedStr.split(",");
			for(String str : arr) {
				returnList.add(str.trim());
			}
		}
		return returnList;
	}
	
	public static String deleteFromLastOccurence(String toSearchString, String toDeleteStr) {
		if(toSearchString != null && toDeleteStr != null && toSearchString.lastIndexOf(toDeleteStr) != -1) {
			toSearchString = toSearchString.substring(0, toSearchString.lastIndexOf(toDeleteStr));
		}
		else {
			log.warning("Null or empty String passed");
		}
		return toSearchString;
	}
	
	public static String getSeperatedValues(List<String> list, String seperator) {
		log.info("inside getSeperatedValues");
		StringBuilder commaSeperatedStr = new StringBuilder();
		if(list != null && list.size() > 0) {
			for (String str : list) {
				commaSeperatedStr.append(str.trim() + seperator);
			}
		}
		return deleteFromLastOccurence(commaSeperatedStr.toString(), seperator);
	}
	
}
