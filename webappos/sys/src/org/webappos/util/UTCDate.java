package org.webappos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UTCDate {

	public static String stringify(java.util.Date d) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		return df.format(d);		
	}

	public static Date parse(String s) {
		if (s == null)
			return null;
		if ((s.length()>=2) && s.startsWith("\"") && s.endsWith("\""))
			s = s.substring(1, s.length()-1);
		try {
			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
			df.setTimeZone(tz);
			return df.parse(s);
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	public static boolean isBeforeOrEquals(String s1, String s2) {
		Date d1 = parse(s1);
		Date d2 = parse(s2);
		if ((d1==null) || (d2==null))
			return false;
		return d1.compareTo(d2) <= 0;
	}
	
	/**
	 * @param s the date to check in ISO UTC format
	 * @return true when the current date is after the given date, or an error occurred 
	 */
	public static boolean expired(String s) {
		Date d1 = new Date();
		Date d2 = parse(s);
		if ((d1==null) || (d2==null))
			return true;
		return d1.compareTo(d2) > 0;
	}
	
	public static String increaseDate(String s, int years, int months, int days, int hours, int minutes, int seconds) {
		Date d = parse(s);
		if (d == null)
			return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.YEAR, years);
		c.add(Calendar.MONTH, months);
		c.add(Calendar.DATE, days);
		c.add(Calendar.HOUR_OF_DAY, hours);
		c.add(Calendar.MINUTE, minutes);
		c.add(Calendar.SECOND, seconds);
		
		return stringify(c.getTime());
	}
	
	public static String addSeconds(String s, int seconds) {
		Date d = parse(s);
		if (d == null)
			return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.SECOND, seconds);
		
		return stringify(c.getTime());		
	}

	public static String addMinutes(String s, int minutes) {
		Date d = parse(s);
		if (d == null)
			return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE, minutes);
		
		return stringify(c.getTime());		
	}
	
	public static String addDays(String s, int days) {
		Date d = parse(s);
		if (d == null)
			return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, days);
		
		return stringify(c.getTime());		
	}	
}
