package com.lin.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {
	private static final Logger log = Logger
			.getLogger(DateUtil.class.getName());
	
	private static enum monthEnum  {January, February, March, April, May, June, July, August, September, October, November, December};

	public static String getFormatedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
		return sdf.format(date);
	}

	public static String getEventFormatedTime(Date date) {
		return DateFormat.getTimeInstance().format(date);
	}

	public static String getEventFormatedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(date);
	}

	public static Date getFormatedDate(String pubDate) {
		// date= "Thu, 4 Oct 2012 15:20:00 EDT "
		DateFormat formatter = new SimpleDateFormat(
				"EEE, d MMM yyyy HH:mm:ss zzz");
		try {
			Date date = formatter.parse(pubDate);
			DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String newDate = newFormat.format(date);
			date = newFormat.parse(newDate);
			return date;
		} catch (ParseException e) {
			System.out.println("Exception in converting string to date:" + e);
			
			return null;
		}

	}

	public static Date getFormatedDate(String pubDate, String format) {
		// date= "Thu, 4 Oct 2012 15:20:00 EDT "
		DateFormat formatter = new SimpleDateFormat(format);
		try {
			Date date = formatter.parse(pubDate);
			DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String newDate = newFormat.format(date);
			date = newFormat.parse(newDate);
			return date;
		} catch (ParseException e) {
			System.out.println("Exception in converting string to date:" + e);
			
			return null;
		}

	}

	public static String getDateFormatDDMMYYY(String pubDate) {
		String[] dateParts = pubDate.split("-");
		StringBuffer sb = new StringBuffer();
		for (int ii = dateParts.length - 1; ii > 0; ii--) {
			sb.append(dateParts[ii] + ".");
		}
		sb.append(dateParts[0]);
		return sb.toString();
	}

	public static String getCurrentTimeStamp(String format) {
		// SimpleDateFormat sdf = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// String format="yyyy-MM-dd HH:mm:ss.SSS" ;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date now = new Date();
		String strDate = sdf.format(now);
		return strDate;
	}

	public static Date getNewFormatedDate(String format, Date dateObj) {
		// SimpleDateFormat sdf = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// String format="yyyy-MM-dd HH:mm:ss.SSS" OR hh:mm a;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String strDate = sdf.format(dateObj);
		Date formattedDate;
		try {
			DateFormat newFormat = new SimpleDateFormat(format);
			String newDate = newFormat.format(strDate);
			formattedDate = newFormat.parse(newDate);
			return formattedDate;
		} catch (ParseException e) {
			
			return null;
		}
	}

	public static String getDateFromString(String dateStr) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(dateStr);
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String reportDate = df.format(date);
			return reportDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			return null;
		}
	}

	/*
	 * get time difference in miliSeconds(time2-time1) Parameter: String time1
	 * and String time2 Return :long difference(time2-time1)
	 */
	public static long checkTimeStampDifference(String time1, String time2) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1;
		Date date2;
		long difference = 0;
		try {
			date1 = format.parse(time1);
			date2 = format.parse(time2);
			difference = date2.getTime() - date1.getTime();
		} catch (ParseException e) {
			
		}
		return difference;

	}

	/*
	 * get time difference in miliSeconds(time2-time1) Parameter: String time1
	 * and String time2 Return :long difference(time2-time1)
	 */
	public static long getTimeStampDifference(String time1, String time2,
			String currentFormat) {
		SimpleDateFormat format = new SimpleDateFormat(currentFormat);
		Date date1;
		Date date2;
		long difference = 0;
		try {
			date1 = format.parse(time1);
			date2 = format.parse(time2);
			difference = date2.getTime() - date1.getTime();
		} catch (ParseException e) {
			
		}
		return difference;

	}

	public static String getFormatedDate(String pubDate, String currentFormat,
			String requiredFormat) {

		DateFormat formatter = new SimpleDateFormat(currentFormat);
		try {
			Date date = formatter.parse(pubDate);
			DateFormat newFormat = new SimpleDateFormat(requiredFormat);
			String newDate = newFormat.format(date);

			return newDate;
		} catch (ParseException e) {
			
			return null;
		}

	}

	public static Date getRequiredFormatDate(String pubDate,
			String currentFormat, String requiredFormat) {

		DateFormat formatter = new SimpleDateFormat(currentFormat);
		try {
			Date date = formatter.parse(pubDate);
			DateFormat newFormat = new SimpleDateFormat(requiredFormat);
			String newDate = newFormat.format(date);
			Date myDt = newFormat.parse(newDate);

			return myDt;
		} catch (ParseException e) {
			
			return null;
		}

	}

	public static String getFormatedDate(Date date, String requiredFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(requiredFormat);
		return sdf.format(date);
	}

	public static Date convertStringToDate(String dateStr) {
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date myDt = sdf.parse(dateStr);
			// System.out.println("date :"+myDt); // Sat Jan 02 00:00:00 BOT
			// 2010
			return myDt;
		} catch (ParseException e) {
			
			return null;
		}

	}

	public static String getDayOfMonthSuffix(int n) {
		if (n >= 11 && n <= 13) {
			return "th";
		}
		switch (n % 10) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}

	public static String addTimeBySecondsInDateString(String dateStr,
			String format, int seconds) {
		Date date = convertStringToDate(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.SECOND, seconds);
		Date changeDate = cal.getTime();
		String changeDateStr = DateUtil.getFormatedDate(changeDate, format);
		return changeDateStr;
	}

	public static String getModifiedDateStringByDays(Date date, int days,
			String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		String modifiedDate = sdf.format(c.getTime());
		return modifiedDate;
	}

	public static String getModifiedDateStringByDays(String dateStr, int days,
			String format) {
		Date date = getFormatedDate(dateStr, format);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		String modifiedDate = sdf.format(c.getTime());
		return modifiedDate;
	}

	public static Date getYesterdayDate(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		return getFormatedDate(sdf.format(c.getTime()), format);
	}

	public static String getYesterdayDateString(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		return sdf.format(c.getTime());
	}

	public static String getModifiedDateByYears(Date date, int years,
			String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		String modifiedDate = sdf.format(c.getTime());
		return modifiedDate;
	}

	public static String getModifiedDateByYears(String dateStr, int years,
			String format) {
		Date date = getFormatedDate(dateStr, format);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		String modifiedDate = sdf.format(c.getTime());
		return modifiedDate;
	}

	public static String getDateByYearMonthDays(int year, int month, int day,
			String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		String modifiedDate = sdf.format(c.getTime());
		return modifiedDate;
	}

	public static Date getDateYYYYMMDD() {
		// YYYY-MM-DD

		Calendar c = Calendar.getInstance();
		Date d = new GregorianCalendar(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).getTime();
		return d;
	}

	public static Date getDateYYYYMMDD(String dateStr) {
		// yyyy-MM-dd
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date myDt = sdf.parse(dateStr);
			return myDt;
		} catch (ParseException e) {
			log.severe("ParseException in getDateYYYYMMDD(String dateStr) of DateUtil.");
			
			return null;
		}
	}
	public static Date getDateMMDDYYYY(String dateStr) {
		// yyyy-MM-dd
		try {
			DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			Date myDt = sdf.parse(dateStr);
			return myDt;
		} catch (ParseException e) {
			log.severe("ParseException in getDateYYYYMMDD(String dateStr) of DateUtil.");
			
			return null;
		}
	}
	public static String getFormatedStringDateYYYYMMDD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String getFormatedStringDateMMDDYYYY(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		return sdf.format(date);
	}

	public static DateTime getStartOfToday() {
		DateTime now = new DateTime();
		LocalDate today = now.toLocalDate();

		DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
		return startOfToday;
	}

	public static LocalDate getJodaDateYYYYMMDD(String dateStr) {

		// YYYY-MM-DD
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		LocalDate dt = formatter.parseLocalDate(dateStr);
		return dt;
	}

	public static Date getDateYYYYMMDDHHMMSS() {
		// YYYY-MM-DD
		Calendar c = Calendar.getInstance();
		Date d = new GregorianCalendar(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND)).getTime();
		return d;
	}

	public static Date getDate(Date date, String requiredFormat) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(requiredFormat);
			String modifiedDate = sdf.format(date);
			Date myDt = sdf.parse(modifiedDate);
			return myDt;
		} catch (ParseException e) {
			log.severe("ParseException in getDate(Date date, String requiredFormat) of DateUtil.");
			
			return null;
		}
	}

	public static long getDifferneceBetweenTwoDates(String startDate,
			String endDate, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat); // dateFormat
																	// :
																	// yyyy-MM-dd
																	// HH:mm:ss

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(startDate);
			d2 = format.parse(endDate);

			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			/*
			 * System.out.print(diffDays + " days, ");
			 * System.out.print(diffHours + " hours, ");
			 * System.out.print(diffMinutes + " minutes, ");
			 * System.out.print(diffSeconds + " seconds.");
			 */

			return diffDays;
		} catch (Exception e) {
			
			return 0;
		}
	}

	public static String getFormatedDateUsingJodaLib(String dateStr,
			String currentFormat, String requiredFormat) {
		DateTimeFormatter format = DateTimeFormat.forPattern(currentFormat)
				.withOffsetParsed(); // "yyyy-MM-dd'T'HH:mm:ssZZ"
		DateTime dateTime = format.parseDateTime(dateStr);
		format = DateTimeFormat.forPattern(requiredFormat); // "yyyy-MM-dd"
		String formatDate = format.print(dateTime);
		return formatDate;
	}

	public static boolean isDateFormatYYYYMMDD(String dateStr) {
		String regex = "^\\d{4}-\\d{2}-\\d{2}$";
		if (dateStr.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMonthFormatYYYYMM(String dateStr) {
		String regex = "^\\d{4}-\\d{2}$";
		if (dateStr.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getStartDateOfMonthFromCurrent(int monthFromCurrent,
			String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, monthFromCurrent);
		// set DATE to 1, so first date of month
		calendar.set(Calendar.DATE, 1);
		Date firstDateOfMonth = calendar.getTime();
		// System.out.println("firstDateOfPreviousMonth:"+firstDateOfMonth);
		String dateStr = getFormatedDate(firstDateOfMonth, dateFormat); // Ex:dateFormat:"yyyy-MM-dd"
		return dateStr;
	}

	public static String getEndDateOfMonthFromCurrent(int monthFromCurrent,
			String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, monthFromCurrent);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date lastDateOfMonth = calendar.getTime();
		String dateStr = getFormatedDate(lastDateOfMonth, dateFormat); // Ex:dateFormat:"yyyy-MM-dd"
		return dateStr;
	}

	public static String getModifiedDateByMonths(String dateStr, int months,
			String format) throws Exception {
		Date date = DateUtil.getFormatedDate(dateStr, format);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		String modifiedDate = sdf.format(c.getTime());
		return modifiedDate;
	}

	public static String getEndDateOfMonth(String date, String currentDateFormat)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(currentDateFormat);
		Date convertedDate = dateFormat.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertedDate);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date lastDateOfMonth = calendar.getTime();
		String dateStr = getFormatedDate(lastDateOfMonth, currentDateFormat); // Ex:dateFormat:"yyyy-MM-dd"
		return dateStr;
	}
	
	public static Date getLastDateOfMonth(String date, String currentDateFormat)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(currentDateFormat);
		Date convertedDate = dateFormat.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(convertedDate);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static int getDayOfDate(String dateStr, String format) {
		// SUNDAY = 1,,,,,,SATURDAY = 7
		Date date = DateUtil.getFormatedDate(dateStr, format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}

	public static String getJustPreviousDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		return sdf.format(c.getTime());
		// return getFormatedDate(sdf.format(c.getTime()), format);
	}
	public static String getDateAsString(Date date, String requiredFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(requiredFormat);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return sdf.format(c.getTime());
	}
	public static String getJustPreviousDate(String dateStr,
			String currentFormat, String expectedFormat) {
		Date date = DateUtil.getFormatedDate(dateStr, currentFormat);
		SimpleDateFormat sdf = new SimpleDateFormat(expectedFormat);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		return sdf.format(c.getTime());
		// return getFormatedDate(sdf.format(c.getTime()), format);
	}

	public static String getTimeStamp(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = DateUtil.getFormatedDate(dateStr, format);
		String strDate = sdf.format(date);
		return strDate;
	}
	
	/**
	 * @author Naresh Pokhriyal<br />
	 * @param dateStr : Date value from which the month value will be taken.
	 * @param format : Format of the dateStr passed.
	 * @param isAppendYear : if true then year will also append after the month name.
	 * @return {@link String} Returns name of the month, of the date passed
	 */
	public static String getMonthName(String dateStr, String format, boolean isAppendYear) {
		String month = "";
		Date date = DateUtil.getFormatedDate(dateStr, format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int monthNumber = c.get(Calendar.MONTH);
		if(monthNumber >= 0 && monthNumber <= 11) {
			month = monthEnum.values()[monthNumber].name();
		}
		if(isAppendYear) {
			month = month + ", "+c.get(Calendar.YEAR);
		}
        return month;
    }


	/**
	 * @author Manish Mudgal<br />
	 * @param days : The value defines the days difference to which the current date has to be changed and return.
	 * @return {@link Date} Returns date based on <b> plus or minus value</b>. After/Before
	 *         current date. <BR />
	 *         Also takes care of Hours/minutes/seconds value for comparison
	 *         later.
	 * 
	 */
	public static Date getDateBeforeAfterDays(int days) {
		try {
			Calendar cal = GregorianCalendar.getInstance();
			cal.add(Calendar.DATE, days);
			return getAbsoluteDate(cal.getTime());
		} catch (Exception e) {
			log.severe("Error in getDateBeforeAfterDays ");
			return null;
		}
	}

	/**
	 * @author Manish Mudgal<br />
	 * Absolute date is a date where hours , minutes, seconds and millisecond values are set equal to the start of the date. i.e. 00.
	 * @param date
	 * @return
	 */
	public static Date getAbsoluteDate(Date date) {
		try {
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		} catch (Exception e) {
			log.severe("Error in getting absolute date");
			return null;
		}
	}
	
	
	/**
	 * @author Manish Mudgal<br />
	 * Generic function to return the first date of the month. 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.set(Calendar.DAY_OF_MONTH, 1);  
        return calendar.getTime();		
	}
	
	/**
	 * @author Manish Mudgal<br />
	 *  This method returns last date of the month in which the input date falls. Becomes useful in finding 30th or 31st date.
	 */
	public static Date getLastDateOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.MONTH, 1);  
        calendar.set(Calendar.DAY_OF_MONTH, 1);  
        calendar.add(Calendar.DATE, -1);	
        return calendar.getTime();
	}
 	
	/**
	 * @author Manish Mudgal<br />
	 *  This methods returns the difference in months between two dates. It becomes helpful in situation particularly when we have date like
	 *  15 nov 2013 To 3 May 2014.<br />
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getMonthDiff(Date startDate, Date endDate){
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

		return diffMonth+1; 
	}
	
	
	public static int getMonthOfDate(Date date){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH); 
	}
	
	public static int getYearOfDate(Date date){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR); 
	}

	/**
	 * @author Manish Mudgal<br />
	 * For a given start date and end date, this function returns a two dimensional array.
	 * each element of array has first and last dates of a month.
	 * The months span from the month of start date till the month of end date.
	 */
	public static String[][] getMonthlyStartEndDates(Date startDate, Date endDate, String format){
		 	Calendar cal = GregorianCalendar.getInstance();
		 	cal.setTime(startDate);
		 	int monthDiff = getMonthDiff(startDate, new Date());
		 	System.out.println("diffs"+monthDiff);
		 	String[][] dateArr = new String[500][2];
		 	int i = 0 , j=0;
		 	do{
		 		dateArr[j][0] =   getDateAsString((cal.getTime()), format);
		 		Date blockStartDate = cal.getTime();
		 		cal.add(Calendar.DAY_OF_MONTH, 14);
		 		if(getDaysDiff(cal.getTime(), getLastDateOfMonth(blockStartDate)) < 3){
		 			cal.setTime(getLastDateOfMonth(blockStartDate));
		 		}
		 		if(cal.getTime().after(getLastDateOfMonth(blockStartDate))){
//		 			cal.add(Calendar.DAY_OF_MONTH, -14);
		 			cal.setTime(getLastDateOfMonth(blockStartDate));
		 		} 
			 		dateArr[j][1] =   getDateAsString((cal.getTime()), format);		 			
		 		 

		 		cal.add(Calendar.DAY_OF_MONTH, 1);
		 		i++;j++;
		 	}while(!cal.getTime().after(new Date()));
		 	return dateArr;
	}

	/**
	 * @author Anup Dutta
	 * This method takes date as argument and return back diffrence of days 
	 */
	
	public static long getDaysDiff(Date start,Date end){
		long dayDiff = 0;
		try{
			long diff = end.getTime() - start.getTime();
			dayDiff = diff / (24 * 60 * 60 * 1000);
		}catch(Exception e){
			log.info(e.getMessage());
		}
		return dayDiff;
	}
	
	public static void main(String[] args) {
		Date startDate = DateUtil.getAbsoluteDate(DateUtil.getDateMMDDYYYY("1-1-2014"));
		Date endDate = DateUtil.getAbsoluteDate(DateUtil.getDateMMDDYYYY("1-25-2015"));
					Date historicalCapDate = DateUtil.getDateBeforeAfterDays(-(LinMobileConstants.CHANGE_WINDOW_SIZE));
String[][] arr = DateUtil.getMonthlyStartEndDates(startDate, endDate, "yyyy-MM-dd");
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i][0] + "   "+arr[i][1]); 
		}
	}
}