package so.microcloud.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日期转换和格式化工具类
 * 
 *
 */
public class DateUtil {

	private static Log log = LogFactory.getLog(DateUtil.class);

	public final static String shortFormat = "yyyyMMdd";
	public final static String longFormat = "yyyyMMddHHmmss";
	public final static String webFormat = "yyyy-MM-dd";
	public final static String timeFormat = "HHmmss";
	public final static String monthFormat = "yyyy-MM";
	public final static String chineseDtFormat = "yyyy年MM月dd日";
	public final static String defaultFormat = "yyyy-MM-dd HH:mm:ss";
	public final static String noSecondFormat = "yyyy-MM-dd HH:mm";
	public final static String millisDay = "MM月dd日";

	private static class DateFormatPattern {

		private String pattern;

		private String regex;

		public DateFormatPattern(String pattern, String regex) {
			this.pattern = pattern;
			this.regex = regex;
		}

		public String getPattern() {
			return pattern;
		}

		public boolean matchPattern(String date) {
			return date.matches(regex);
		}

	}

	public static final String XML_GREGORIAN_CALENDAR_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXZ";

	private static final List<DateFormatPattern> patternList = new ArrayList<DateFormatPattern>();

	private static volatile String lastUniqueTimeStamp = DateUtil.getFormattedDate(new Date(), "yyyyMMddhhmmssSSS");
	private static volatile long lastUniqueMilliseconds = new Date().getTime();

	private static final Object uniqueTimeStampLock = new Object();
	private static final Object uniqueMillisecondsLock = new Object();

	static {
		patternList.add(new DateFormatPattern("yyyy-MM-dd HH:mm:ss", "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}"));
		patternList.add(new DateFormatPattern("yyyy-MM-dd HH:mm", "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}"));
		patternList.add(new DateFormatPattern("yyyy-MM-dd HH", "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}"));
		patternList.add(new DateFormatPattern("yyyy-MM-dd", "\\d{4}-\\d{1,2}-\\d{1,2}"));
		patternList.add(new DateFormatPattern("yyyy-MM", "\\d{4}-\\d{1,2}"));
		patternList.add(new DateFormatPattern("dd-MM-yyyy HH:mm:ss", "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}"));
		patternList.add(new DateFormatPattern("dd-MM-yyyy HH:mm", "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}"));
		patternList.add(new DateFormatPattern("dd-MM-yyyy HH", "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}"));
		patternList.add(new DateFormatPattern("dd-MM-yyyy", "\\d{1,2}-\\d{1,2}-\\d{4}"));
		patternList.add(new DateFormatPattern("MM-yyyy", "\\d{1,2}-\\d{4}"));
		patternList.add(new DateFormatPattern("yyyyMMddHHmmss", "\\d{14}"));
		patternList.add(new DateFormatPattern("yyyyMMddHHmm", "\\d{12}"));
		patternList.add(new DateFormatPattern("yyyyMMddHH", "\\d{10}"));
		patternList.add(new DateFormatPattern("yyyyMMdd", "\\d{8}"));
		patternList.add(new DateFormatPattern("yyyyMM", "\\d{6}"));
		patternList.add(new DateFormatPattern("yyyy", "\\d{4}"));
		patternList.add(new DateFormatPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[-+]\\d{4}"));// 2012-03-15T13:48:49.733+0800
		patternList.add(new DateFormatPattern(XML_GREGORIAN_CALENDAR_PATTERN, "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[-+]\\d{2}:\\d{2}"));// 2012-03-15T13:48:49.733+08:00

	}

	/**
	 * 解析时间，把指定格式的日期/时间转化为时间类型。目前支持的格式有: <br>
	 * yyyy-MM-dd HH:mm:ss <br>
	 * yyyy-MM-dd HH:mm <br>
	 * yyyy-MM-dd HH <br>
	 * yyyy-MM-dd <br>
	 * yyyy-MM <br>
	 * dd-MM-yyyy HH:mm:ss <br>
	 * dd-MM-yyyy HH:mm <br>
	 * dd-MM-yyyy HH <br>
	 * dd-MM-yyyy <br>
	 * MM-yyyy <br>
	 * yyyyMMddHHmmss <br>
	 * yyyyMMddHHmm <br>
	 * yyyyMMddHH <br>
	 * yyyyMMdd <br>
	 * yyyyMM <br>
	 * yyyy <br>
	 * yyyy-MM-dd'T'HH:mm:ss.SSSZ (2012-03-15T13:48:49.733+0800) <br>
	 * yyyy-MM-dd'T'HH:mm:ss.SSSXZ (2012-03-15T13:48:49.733+08:00) <br>
	 * 
	 * @param sDate
	 *            待解析的时间
	 * @return 解析所得时间，如果不满足指定的格式类型，则返回null
	 */
	public static Date parseDate(String sDate) {
		if (sDate == null)
			return null;
		for (DateFormatPattern pattern : patternList) {
			if (pattern.matchPattern(sDate)) {
				if (pattern.getPattern().equals(XML_GREGORIAN_CALENDAR_PATTERN)) {
					return parseXMLGregorianCalendar(sDate).getTime();
				} else {
					SimpleDateFormat parser = new SimpleDateFormat(pattern.getPattern());
					try {
						return parser.parse(sDate);
					} catch (ParseException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		throw new RuntimeException("Cannot parse String to Date: " + sDate);
	}

	public static Calendar parseXMLGregorianCalendar(String sDate) {
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			XMLGregorianCalendar xmlGCalendar = df.newXMLGregorianCalendar(sDate);
			return xmlGCalendar.toGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 取得指定日期的开始点，值为yyyy-MM-dd HH:mm:ss:000，如2011-09-07的开始时间为2011-09-07
	 * 00:00:00:000
	 * 
	 * @param d
	 *            待操作的日期
	 * @return 指定的日期的开始时间点
	 */
	public static Date getDayBegin(Date d) {
		if (d == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 取得指定日期的结束点，值为yyyy-MM-dd 23:59:59:999，如2011-09-07的结束时间为2011-09-07
	 * 23:59:59:999
	 * 
	 * @param d
	 *            待操作的日期
	 * @return 指定的日期的结束时间点
	 */
	public static Date getDayEnd(Date d) {
		if (d == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 取得系统的当前时间，不带微秒
	 * 
	 * @return 系统当前时间，不带微秒
	 */
	public static Date getNow() {
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.MILLISECOND, 0);
		return c1.getTime();
	}

	/**
	 * 取得以格式yyyy-MM-dd表示的日期
	 * 
	 * @param date
	 *            待格式化的日期
	 * @return yyyy-MM-dd格式化的日期
	 */
	public static String getDateYMD(Date date) {
		return getFormattedDate(date, "yyyy-MM-dd");
	}

	/**
	 * 取得指定格式表示的日期/时间
	 * 
	 * @param date
	 *            待格式化的日期/时间
	 * @param pattern
	 *            目标格式
	 * @return 按目标格式格式化后的日期/时间，如果时间或者目标格式为null则返回null
	 */
	public static String getFormattedDate(Date date, String pattern) {
		if (date == null || pattern == null)
			return null;
		if (XML_GREGORIAN_CALENDAR_PATTERN.equals(pattern)) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			return toXMLGregorianCalendarFormat(calendar);
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 把Calendar对象转成符合W3C XML Schema 1.0 标准推荐规范的词法格式
	 * 
	 * @param calendar
	 * @return
	 */
	public static String toXMLGregorianCalendarFormat(Calendar calendar) {
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			GregorianCalendar gCalendar = new GregorianCalendar(calendar.getTimeZone());
			gCalendar.setTime(calendar.getTime());
			XMLGregorianCalendar xmlGCalendar = df.newXMLGregorianCalendar(gCalendar);
			return xmlGCalendar.toXMLFormat();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 取一个yyyyMMddHHmmssSSS格式的时间戳，只保证单JVM唯一
	 * 
	 * @return
	 */
	public static String getUniqueTimeStamp() {
		synchronized (uniqueTimeStampLock) {
			String s;
			do {
				s = DateUtil.getFormattedDate(new Date(), "yyyyMMddHHmmssSSS");
			} while (s.equals(lastUniqueTimeStamp));
			lastUniqueTimeStamp = s;
			return s;
		}
	}

	/**
	 * 取一个系统时间毫秒数作为时间戳，只保证单JVM唯一
	 * 
	 * @return
	 */
	public static long getUniqueMilliseconds() {
		synchronized (uniqueMillisecondsLock) {
			long next;
			do {
				next = new Date().getTime();
			} while (next == lastUniqueMilliseconds);
			lastUniqueMilliseconds = next;
			return next;
		}
	}

	/**
	 * 删除某日期中的秒和毫秒
	 * 
	 * @param d
	 * @return
	 */
	public static Date truncateSecond(Date d) {
		if (d == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 删除某日期中的毫秒
	 * 
	 * @param d
	 * @return
	 */
	public static Date truncateMillisecond(Date d) {
		if (d == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 判断一个时间是否介于另外两个时间之间（包括等于）
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param theTime
	 * @return
	 */
	public static boolean isBetween(Date beginTime, Date endTime, Date theTime) {
		if (theTime == null)
			return false;
		boolean flag = true;
		if (beginTime != null)
			flag = beginTime.getTime() <= theTime.getTime();
		if (flag && endTime != null)
			flag = endTime.getTime() >= theTime.getTime();
		return flag;
	}

	/**
	 * 返回一个日期据当前的时间间隔，表示为自然语言意义上的x年y月z天
	 * 
	 * @param dob
	 *            始点日期
	 * @return 返回的整数数组表示始点日期据当前的时间间隔。[0]位表示年，[1]位表示月，[2]位表示日。
	 */
	public static int[] getInterval(Date dob) {
		Calendar now = Calendar.getInstance();
		Calendar dobc = Calendar.getInstance();
		dobc.setTimeInMillis(dob.getTime());
		int sYears = dobc.get(Calendar.YEAR);
		int sMonths = dobc.get(Calendar.MONTH);
		int sDays = dobc.get(Calendar.DAY_OF_MONTH);
		int eYears = now.get(Calendar.YEAR);
		int eMonths = now.get(Calendar.MONTH);
		int eDays = now.get(Calendar.DAY_OF_MONTH);
		int intervalYear;
		int intervalMonth;
		int intrevalDay;
		intervalYear = eYears - sYears;
		intervalMonth = eMonths - sMonths;
		if (intervalMonth < 0) {
			intervalMonth += 12;
			--intervalYear;
		}
		dobc.add(Calendar.YEAR, intervalYear);
		dobc.add(Calendar.MONTH, intervalMonth);
		sDays = dobc.get(Calendar.DAY_OF_MONTH);
		intrevalDay = eDays - sDays;
		if (intrevalDay < 0) {
			--intervalMonth;
			if (intervalMonth < 0) {
				--intervalYear;
				intervalMonth += 12;
			}
			dobc.setTimeInMillis(dob.getTime());
			dobc.add(Calendar.YEAR, intervalYear);
			dobc.add(Calendar.MONTH, intervalMonth);
			intrevalDay = dobc.getActualMaximum(Calendar.DATE) - dobc.get(Calendar.DATE) + eDays;
		}
		return new int[] { intervalYear, intervalMonth, intrevalDay };
	}

	/**
	 * 时间加上某个时间
	 * 
	 * @param date
	 *            时间
	 * @param type
	 *            类型：minute hour day month year
	 * @return date
	 * @throws ParseException
	 */
	public static Date addDate(Date date, String type, int time) {
		if (date == null) {
			log.error("date is null");
			return null;
		}
		if (type == null) {
			log.error("type is null");
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		try {
			if (type.equalsIgnoreCase("minute")) {
				calendar.add(Calendar.MINUTE, time);
			} else if (type.equalsIgnoreCase("hour")) {
			} else if (type.equalsIgnoreCase("day")) {
				calendar.add(Calendar.DATE, time);
			} else if (type.equalsIgnoreCase("month")) {
				calendar.add(Calendar.MONTH, time);
			} else if (type.equalsIgnoreCase("year")) {
				calendar.add(Calendar.YEAR, time);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return calendar.getTime();
	}

	/**
	 * Date转String
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String getDateString(Date date, String formatString) {
		if (date == null)
			return null;
		if (formatString == null)
			formatString = defaultFormat;
		DateFormat format = new SimpleDateFormat(formatString);
		return format.format(date);
	}

	/**
	 * String转Date
	 * 
	 * @param dateStr
	 * @return date
	 */
	public static Date getDate(String dateStr, String formatString) {
		if (StringUtils.isEmpty(dateStr))
			return null;
		if (StringUtils.isEmpty(formatString))
			formatString = defaultFormat;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getTimeSampleString(java.util.Date date) {
		if (date == null)
			return null;
		synchronized (new SimpleDateFormat(defaultFormat)) {
			return new SimpleDateFormat(defaultFormat).format(date);
		}
	}

	public static java.util.Date getTime(String str) throws ParseException {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		synchronized (new SimpleDateFormat(defaultFormat)) {
			return new SimpleDateFormat(defaultFormat).parse(str);
		}
	}

	public static java.util.Date getGenTime(String str) throws ParseException {
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		if (str.length() == 10)
			return getDate(str);
		if (str.length() == 13)
			str = str + ":00:00";
		else if (str.length() == 16) {
			str = str + ":00";
		}
		return getTime(str);
	}

	public static java.sql.Date getDate(String dateFormat) {
		if ((dateFormat == null) || (dateFormat.trim().length() < 10)) {
			return null;
		}
		String year = dateFormat.substring(0, 4);
		String month = dateFormat.substring(5, 7);
		String date = dateFormat.substring(8);
		StringBuffer str = new StringBuffer();
		str.append(year);
		str.append("-");
		str.append(month);
		str.append("-");
		str.append(date);
		return java.sql.Date.valueOf(str.toString());
	}

	public static Boolean checkHours(String v) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			long now = df.parse(df.format(new Date())).getTime();
			long fbsj = df.parse(v).getTime();
			long hour = (now - fbsj) / (60 * 60 * 1000);
			if (hour >= 12) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String  calcuDNT(String dysj,String sjgysj) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			if(dysj != null && !dysj.equals("") && sjgysj != null && !sjgysj.equals("")){
				long a = df.parse(dysj).getTime();
				long b = df.parse(sjgysj).getTime();
				Long min = (b - a) / (60 * 1000);
				System.out.println("dnt:"+min);
				return min.toString();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 比较两个时间的大小
	 * @return 1大于   -1小于
	 */
	public static int compareDate(Date date1,Date date2){
		if (date1.getTime() > date2.getTime()) {
			return 1;
		} else if (date1.getTime() < date2.getTime()) {
			return -1;
		} else {//相等
			return 0;
		}
	}

	
	public static String formatDateString(String date,String dateFormat) {
		String result = "";
		try {
			 result  = getDateString(parseStrDate(date, dateFormat), dateFormat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param str
	 *            要创建的标准时间格式
	 * @return 时间
	 */
	public static Date parseStrDate(String str, String format) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(format);
		ParsePosition pos = new ParsePosition(0);
		return sd.parse(str, pos);
	}
}
