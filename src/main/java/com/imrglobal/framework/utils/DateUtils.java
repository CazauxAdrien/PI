package com.imrglobal.framework.utils;

import com.imrglobal.framework.factory.ThreadLocalFactory;
import com.imrglobal.framework.nls.Language;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Classe utilitaire pour aider à la gestion des dates.
 */
public class DateUtils {
  /**
   * Number of milliseconds in a DAY.
   */
  public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
  /**
   * Number of milliseconds in an hour.
   */
  public static final long HOUR_IN_MILLIS = 60 * 60 * 1000;
  private static final long DAY_AND_ONE_HOUR = DAY_IN_MILLIS + HOUR_IN_MILLIS;
  /**
   * NUmber of milliseconds in a minute.
   */
  public static final long MINUTE_IN_MILLIS = 60000;

  private static final Map<Language, DateFormat> dateFormats;
  private static final Map<Language, DateFormat> dateTimeFormats;
  /**
   * Default date format.<BR>
   * It is specified by the parameter DEFAULT_DATE_FORMAT in the properties file If not given it equals to dd/MM/yyyy.
   */
  public static final String DEFAULT_DATE_FORMAT;

  /**
   * Default date and time format.<BR>
   * It is specified by the parameter DEFAULT_DATE_TIME_FORMAT in the properties file If not given it build using {@link #DEFAULT_DATE_FORMAT} : {@code DEFAULT_DATE_FORMAT+" HH:mm}.
   */
  public static final String DEFAULT_DATE_TIME_FORMAT;

  /**
   * The default holidays calendar is defined by the value of the <code>DEFAULT_HOLIDAYS_CALENDAR</code> property of the <code>framework.properties</code> file. <br>
   * The value of its property is the name of the class which implements <code>HolidaysCalendar</code>.
   */
  public static HolidaysCalendar defaultHolidaysCalendar;

  private static ThreadLocal<GregorianCalendar> defaultCalendar = ThreadLocalFactory.newThreadLocal("DateUtils.defaultCalendar");

  private static final ToDay TO_DAY;

  static {
    DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT;
    dateFormats = new ThreadLocalMap<>("_dateFormatLg_");
    dateTimeFormats = new ThreadLocalMap<>("_dateTimeFormatLg_");
    defaultHolidaysCalendar = HolidaysCalendarFactory.DEFAULT_HOLLYDAY_CALENDAR;
    TO_DAY = new ToDay();
  }

  /**
   * Builds a date from the year, month, day.
   *
   * @param year year of the date
   * @param month the month (between 1 and 12)
   * @param day the day (between 1 and 31)
   * @return date, the date
   */
  public static Date getDate(int year, int month, int day) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.clear();
    calendar.set(year, month - 1, day, 0, 0, 0);
    return calendar.getTime();
  }

  /**
   * Builds a date from the year, month, day.
   *
   * @param year of the date
   * @param month between 1 and 12
   * @param day between 1 and 31
   * @param hour hours between 0 and 23
   * @param min minutes between 0 and 59
   * @param sec seconds between 0 and 59
   * @return date, the date
   */
  public static Date getDate(int year, int month, int day, int hour, int min, int sec) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.clear();
    calendar.set(year, month - 1, day, hour, min, sec);
    return calendar.getTime();
  }

  /**
   * Returns the day of the month represented by this Date object. <BR>
   * The value returned is between 1 and 31 representing the day of the month that contains or begins with the instant in time represented by this Date object, as interpreted in the local time zone.
   *
   * @param date whose day one wants to know
   * @return day number
   */
  public static int getDay(Date date) {
    if (date != null) {
      GregorianCalendar defCalendar = getDefaultCalendar();
      defCalendar.setTime(date);
      return defCalendar.get(Calendar.DAY_OF_MONTH);
    }
    return 1;
  }

  /**
   * Returns the day of the month represented by the current Date. <BR>
   * The value returned is between 1 and 31 representing the day of the month that contains or begins with the current date, as interpreted in the local time zone.
   *
   * @return day number
   */
  public static int getCurrentDay() {
    return (getDay(now()));
  }

  /**
   * Returns the day of the week represented by this Date object. <BR>
   * The value returned is between 0 and 6.
   *
   * @param date whose day one wants to know
   * @return week day number
   */
  public static int getWeekDay(Date date) {
    if (date != null) {
      GregorianCalendar defCalendar = getDefaultCalendar();
      defCalendar.setTime(date);
      return defCalendar.get(Calendar.DAY_OF_WEEK);
    }
    return 7;
  }

  /**
   * Returns the day of the week of the current date. <BR>
   * The value returned is between 0 and 6.
   *
   * @return week day number
   */
  public static int getCurrentWeekDay() {
    return (getWeekDay(now()));
  }

  /**
   * Returns a number representing the month that contains or begins with the instant in time represented by this Date object. The value returned is between 1 and 12.
   *
   * @param date whose month one wants to know
   * @return month number
   */
  public static int getMonth(Date date) {
    if (date != null) {
      GregorianCalendar defCalendar = getDefaultCalendar();
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.MONTH) + 1);
    }
    return 1;
  }

  /**
   * Returns a number representing the month that contains or begins with the current date. The value returned is between 1 and 12.
   *
   * @return int, month number
   */
  public static int getCurrentMonth() {
    return (getMonth(now()));
  }

  /**
   * Returns the year that contains or begins with the instant in time represented by this Date object, as interpreted in the local time zone.
   *
   * @param date whose year one wants to know
   * @return int, year
   */
  public static int getYear(Date date) {
    GregorianCalendar defCalendar = getDefaultCalendar();
    if (date != null) {
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.YEAR));
    }
    return 2000;
  }

  /**
   * Returns the detail of the date :
   * <ul>
   * <li>the day of month
   * <li>the month (1 for january, 12 for december)
   * <li>the year
   * </ul>
   * <br>
   * If withTimeDetails, the folowing info are added to the array :
   * <ul>
   * <li>the hour of the day
   * <li>the minute
   * <li>the second
   * </ul>
   *
   * @param date the date
   * @param withTimeDetails true to retrieve the date details
   * @return the detail of the date as int array :
   *         <ul>
   *         <li>the day of month
   *         <li>the month (1 for january, 12 for december)
   *         <li>the year
   *         </ul>
   *         <br>
   *         If withTimeDetails, the folowing info are added to the array :
   *         <ul>
   *         <li>the hour of the day
   *         <li>the minute
   *         <li>the second
   *         </ul>
   */
  public static int[] getDateDetails(Date date, boolean withTimeDetails) {
    int[] detail = withTimeDetails ? new int[6] : new int[3];
    if (date != null) {
      GregorianCalendar defCalendar = getDefaultCalendar();
      dateDetail(defCalendar, date, detail);
    }
    return detail;
  }

  private static void dateDetail(GregorianCalendar defCalendar, Date date, int[] detail) {
    defCalendar.setTime(date);
    detail[0] = defCalendar.get(Calendar.DAY_OF_MONTH);
    detail[1] = defCalendar.get(Calendar.MONTH) + 1;
    detail[2] = defCalendar.get(Calendar.YEAR);
    if (detail.length == 6) {
      detail[3] = defCalendar.get(Calendar.HOUR_OF_DAY);
      detail[4] = defCalendar.get(Calendar.MINUTE);
      detail[5] = defCalendar.get(Calendar.SECOND);
    }
  }

  /**
   * Returns the year that contains or begins with the current date, as interpreted in the local time zone.
   *
   * @return int, year
   */
  public static int getCurrentYear() {
    return (getYear(now()));
  }

  /**
   * Returns the hour represented by this Date object. The returned value is a number (0 through 23) representing the hour within the day that contains or begins with the instant in time represented
   * by this Date object
   *
   * @param date given date
   * @return hour between 0 and 23
   */
  public static int getHour(Date date) {
    if (date != null) {
      GregorianCalendar defCalendar = getDefaultCalendar();
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.HOUR_OF_DAY));
    }
    return 0;
  }

  /**
   * Returns the hour represented by the current date. The returned value is a number (0 through 23) representing the hour within the day that contains or begins with the current date.
   *
   * @return hour between 0 and 23
   */
  public static int getCurrentHour() {
    return (getHour(now()));
  }

  /**
   * Returns the number of minutes past the hour represented by this date, as interpreted in the local time zone. The value returned is between 0 and 59.
   *
   * @param date la date dont on extrait l'année
   * @return the number of minutes
   */
  public static int getMinute(Date date) {
    if (date != null) {
      GregorianCalendar defCalendar = getDefaultCalendar();
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.MINUTE));
    }
    return 0;
  }

  /**
   * Returns the number of minutes past the hour represented by the current date, as interpreted in the local time zone. The value returned is between 0 and 59.
   *
   * @return the number of minutes
   */
  public static int getCurrentMinute() {
    return (getMinute(now()));
  }

  /**
   * Returns the number of seconds past the minute represented by this date. The value returned is between 0 and 61. The values 60 and 61 can only occur on those Java Virtual Machines that take leap
   * seconds into account.
   *
   * @param date la date dont on extrait l'année
   * @return the number of seconds
   */
  public static int getSecond(Date date) {
    if (date != null) {
      GregorianCalendar defCalendar = getDefaultCalendar();
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.SECOND));
    }
    return 0;
  }

  /**
   * Returns the number of seconds past the minute represented by the current date. The value returned is between 0 and 61. The values 60 and 61 can only occur on those Java Virtual Machines that take
   * leap seconds into account.
   *
   * @return the number of seconds
   */
  public static int getCurrentSecond() {
    return (getSecond(now()));
  }

  /**
   * Converts this Date object to a String with the default format.<BR>
   * The default format is given by the parameter DEFAULT_DATE_FORMAT in the properties file.
   *
   * @param date date to convert into String
   * @return String the converted date
   */
  public static String dateAsString(Date date) {
    return dateAsString(date, DEFAULT_DATE_FORMAT);
  }

  /**
   * Converts the current date to a String with the default format.<BR>
   * The default format is given by the parameter DEFAULT_DATE_FORMAT in the properties file.
   *
   * @return String the converted date
   */
  public static String dateAsString() {
    return dateAsString(now(), DEFAULT_DATE_FORMAT);
  }

  /**
   * Converts this Date object to a String with the specified format.<BR>
   *
   * @param date Date to convert into String
   * @param format String format to use to convert the date
   * @return String the converted date
   */
  public static String dateAsString(Date date, String format) {
    try {
      DateFormat formatter = FormatFactory.getDateFormat(format);
      return formatter.format(date);
    } catch (Throwable th) {
      return "";
    }
  }

  /**
   * Converts this Date object to a String with the specified format.<BR>
   *
   * @param date Date to convert into String
   * @param format String format to use to convert the date
   * @param locale Locale locale to use to convert the date
   * @return String the converted date
   */
  public static String dateAsString(Date date, String format, Locale locale) {
    try {
      DateFormat formatter = FormatFactory.getDateFormat(format, locale);
      return formatter.format(date);
    } catch (Throwable th) {
      return "";
    }
  }

  /**
   * Converts the current date to a String with a specified format.<BR>
   *
   * @param format String format to use to convert the date
   * @return String the converted date
   */
  public static String dateAsString(String format) {
    return dateAsString(now(), format);
  }

  /**
   * Converts this Date object to a String with the default date and time format.<BR>
   * The default date and time format is given by the parameter DEFAULT_DATE_TIME_FORMAT in the properties file : {@link #DEFAULT_DATE_TIME_FORMAT}.
   *
   * @param date date to convert into String
   * @return String the converted date
   */
  public static String dateTimeAsString(Date date) {
    return dateAsString(date, DEFAULT_DATE_TIME_FORMAT);
  }

  /**
   * Converts the current date to a String with the default date and time format.<BR>
   * The default date and time format is given by the parameter DEFAULT_DATE_TIME_FORMAT in the properties file : {@link #DEFAULT_DATE_TIME_FORMAT}.
   *
   * @return String the converted date
   */
  public static String dateTimeAsString() {
    return dateAsString(now(), DEFAULT_DATE_TIME_FORMAT);
  }

  /**
   * Converts a String into a Date. <BR>
   * The format of the sDate must be the default format. The default format is given by the parameter DEFAULT_DATE_FORMAT in the properties file.
   *
   * @param sDate String to convert into a Date
   * @return Date from String
   */
  public static Date getDate(String sDate) {
    return getDate(sDate, DEFAULT_DATE_FORMAT);
  }

  /**
   * Converts a String into a Date according to a format and test the result date. <br>
   * This method can adapt the format according to the length of the string using yyyy or yy conversion... <br>
   * This method is equivalent to <code>getDate(sDate,format,true)</code>.
   *
   * @param sDate String to convert
   * @param format date format (ex : dd/MM/yyyy-HH:mm:ss)
   * @return Date from sDate or null if the string can not be parse or if the result date does not match with the input string (for example, <code>getDate("30/02/2004","dd/MM/yyyy")</code> returns
   *         null)
   */
  public static Date getDate(String sDate, String format) {
    return getDate(sDate, format, true);
  }

  /**
   * Converts a String into a Date according to a format <BR>
   * <br>
   * This method can adapt the format according to the length of the string using yyyy or yy conversion...
   *
   * @param sDate String to convert
   * @param format date format (ex : dd/MM/yyyy-HH:mm:ss)
   * @param testResult if true, the method check than the result of the parse formatted as string using the format is same as the input string. With the test,
   *          <code>getDate("30/02/2004","dd/MM/yyyy",true)</code> returns null, without, <code>getDate("30/02/2004","dd/MM/yyyy",false)</code> returns 01/03/2004.
   * @return Date from sDate
   */
  public static Date getDate(String sDate, String format, boolean testResult) {
    Date date;

    if ((sDate == null) || ("".equals(sDate.trim()))) {
      date = null;
    } else {
      String theFormat = format;
      int nbQuotes = StringUtils.charCount(format, '\'');
      int i = sDate.length() - format.length() + nbQuotes;
      if (i == -2) {
        format = StringUtils.swapSubString(format, "yyyy", "yy");
      } else if (i == 2) {
        format = StringUtils.swapSubString(format, "yy", "yyyy");
      }
      date = doParseDate(sDate, format, testResult);
      if (date == null && !theFormat.equals(format)) {
        // cas ou l'astuce de modification du format poserait problème
        date = doParseDate(sDate, theFormat, testResult);
      }
    }
    return date;
  }

  private static Date doParseDate(String sDate, String format, boolean testResult) {
    Date date;
    DateFormat formatter = FormatFactory.getDateFormat(format);
    try {
      date = formatter.parse(sDate);
      if (testResult) {
        String test = dateAsString(date, format);
        if (!test.equals(sDate)) {
          date = null;
        }
      }
    } catch (ParseException ex) {
      date = null;
    }
    return date;
  }

  /**
   * Converts a String into a Date. <BR>
   * The format of the sDate must be the default date time format. The default date and time format is given by the parameter DEFAULT_DATE_TIME_FORMAT in the properties file :
   * {@link #DEFAULT_DATE_TIME_FORMAT}.
   *
   * @param sDate String to convert into a Date
   * @return Date from String
   */
  public static Date getDateTime(String sDate) {
    return getDate(sDate, DEFAULT_DATE_TIME_FORMAT);
  }

  /**
   * Checks if the sDate corresponds to a valid date <BR>
   * The format of the sDate must be the default format. The default format is given by the parameter DEFAULT_DATE_FORMAT in the properties file.
   *
   * @param sDate String to check
   * @return boolean true if the sDate can be converted into a date
   */
  public static boolean isValid(String sDate) {
    return isValid(sDate, DEFAULT_DATE_FORMAT);
  }

  /**
   * Checks if the string corresponds to a valid date according the given format.
   *
   * @param sDate String to check
   * @param format date format (ex : dd/MM/yyyy-HH:mm:ss)
   * @return true if it's a valid date
   */
  public static boolean isValid(String sDate, String format) {
    boolean result = false;
    if (sDate != null) {
      Date date = getDate(sDate, format);
      if (date != null) {
        if (sDate.equals(dateAsString(date, format))) {
          result = true;
        }
      }
    }
    return result;
  }

  /**
   * Checks if the given values corresponds to a valid date.
   *
   * @param year year of the date
   * @param month month the month (between 1 and 12)
   * @param day day (between 1 and 31)
   * @return boolean
   */
  public static boolean isValid(int year, int month, int day) {
    boolean result = false;
    try {
      Date date = getDate(year, month, day);
      if (date != null) {
        int[] details = getDateDetails(date, false);
        result = (year == details[2]) &&
            (month == details[1]) &&
            (day == details[0]);
      } else {
        result = false;
      }
    } catch (Throwable ex) {
      result = false;
    }
    return result;
  }

  /**
   * Gets the maximum between 2 days.
   *
   * @param date1 first date to compare
   * @param date2 second date to compare
   * @return date1 if date1>date2 otherwise returns date2
   */
  public static Date max(Date date1, Date date2) {
    if (date1 == null) {
      return date2;
    }
    if (date2 == null) {
      return date1;
    }
    if (date1.after(date2)) {
      return date1;
    }
    return date2;
  }

  /**
   * Gets the minimum between 2 days.
   *
   * @param date1 first date to compare
   * @param date2 second date to compare
   * @return date1 if date1<date2 otherwise returns date2
   */
  public static Date min(Date date1, Date date2) {
    Date res;
    if (date1 == null) {
      res = date2;
    } else if (date2 == null) {
      res = date1;
    } else {
      res = (date1.after(date2) ? date2 : date1);
    }
    return res;
  }

  /**
   * Checks if the date is the end of the term.
   *
   * @param date to check
   * @return true if the date is the end of the term
   */
  public static boolean isEndOfATerm(Date date) {
    try {
      int day = getDay(date);
      int month = getMonth(date);
      if (((month == 3) || (month == 12)) && (day == 31)) {
        return true;
      }
      return ((month == 6) || (month == 9)) && (day == 30);
    } catch (Throwable th) {
      return false;
    }
  }

  /**
   * Checks if the date is the end of the month.
   *
   * @param date to check
   * @return true if the date is the end of the month
   */
  public static boolean isEndOfAMonth(Date date) {
    boolean res = false;
    try {
      int[] details = getDateDetails(date, false);
      int day = details[0];
      int month = details[1];
      int year = details[2];
      return day == lastDayOfMonth(month, year);
    } catch (Exception ex) {
      
    }
    return (res);
  }

  private static int lastDayOfMonth(int month, int year) {
    switch (month) {
      case 2:
        return isALeapYear(year) ? 29 : 28;
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      default:
        return 31;
    }
  }

  /**
   * Returns true if the specified year is a leap year.
   *
   * @param year the year to test.
   * @return true if it's a leap year, false otherwise.
   */
  public static boolean isALeapYear(int year) {
    boolean isALeapYear = false;

    if (getMonth(getDate(year, 2, 29)) == 2) {
      isALeapYear = true;
    }
    return (isALeapYear);
  }

  /**
   * Returns true if the current year is a leap year.
   *
   * @return true if it's a leap year, false otherwise.
   */
  public static boolean isALeapYear() {
    return (isALeapYear(getCurrentYear()));
  }

  /**
   * Checks if the date is the beginning of a month.
   *
   * @param date to check
   * @return true if the date is the beginning of the month
   */
  public static boolean isBeginningOfAMonth(Date date) {
    int day = getDay(date);
    return day == 1;
  }

  /**
   * Checks if the date is the beginning of a term.
   *
   * @param date to check
   * @return true if the date is the beginning of a term
   */
  public static boolean isBeginningOfATerm(Date date) {
    int day = getDay(date);
    if (day != 1) {
      return false;
    }

    int month = getMonth(date);
    return 1 == (month % 3);
  }

  /**
   * checks if the second date is the next day of the first date.
   *
   * @param date1 initial date
   * @param date2 day to compare
   * @return true si date2 is next day of date1
   */
  public static boolean isNextDay(Date date1, Date date2) {
    Date nextDay = getNextDay(date1);
    return isSameDay(nextDay, date2);
  }

  /**
   * Checks if two dates have the same day.
   *
   * @param date1 initial date
   * @param date2 date to compare
   * @return true si date2 is same day as date1
   */
  public static boolean isSameDay(Date date1, Date date2) {
    if (date1 != null && date2 != null) {
      long l1 = date1.getTime();
      long l2 = date2.getTime();
      if (Math.abs(l1 - l2) < DAY_AND_ONE_HOUR) { // on ajoute une heure pour gérer le jour de chgt d'heure en octoble
        GregorianCalendar cal = getDefaultCalendar();
        int[] detail1 = new int[3];
        dateDetail(cal, date1, detail1);
        int[] detail2 = new int[3];
        dateDetail(cal, date2, detail2);
        return (detail1[2] == detail2[2]) &&
            (detail1[1] == detail2[1]) &&
            (detail1[0] == detail2[0]);
      }
      return false;
    }
    return false;
  }

  /**
   * Gets a date corresponding to the next day of the given date.
   *
   * @param date initial date
   * @return next day of given date
   */
  public static Date getNextDay(Date date) {
    return addDays(date, 1);

  }

  /**
   * Returns the date corresponding to the first day of the current month.
   *
   * @return the date corresponding to the first day of the current month.
   */
  public static Date getFirstDayOfCurrentMonth() {
    return getFirstDayOfMonth(now());
  }

  /**
   * Gets a date corresponding to the first day of next month.
   *
   * @return first day of next month
   */
  public static Date getFirstDayOfNextMonth() {
    return getFirstDayOfNextMonth(now());
  }

  /**
   * Gets the date corresponding to the last day of the current month.
   *
   * @return the date corresponding to the last day of the current month
   */
  public static Date getLastDayOfCurrentMonth() {
    return getLastDayOfMonth(now());
  }

  /**
   * Returns the date corresponding to the first day of the month of the specified date.
   *
   * @param d the date
   * @return the date corresponding to the first day of the month.
   */
  public static Date getFirstDayOfMonth(Date d) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(d);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    return calendar.getTime();
  }

  /**
   * Gets the date corresponding to the first day of the current month.
   *
   * @param date initiale date
   * @return Date corresponding to the first day of the next month of the given date
   */
  public static Date getFirstDayOfNextMonth(Date date) {
    GregorianCalendar cal = getDefaultCalendar();
    cal.setTime(date);
    cal.add(Calendar.MONTH, 1);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    return cal.getTime();
  }

  /**
   * Gets the date corresponding to the last day of the month of the given date.
   *
   * @param date initiale date
   * @return Date corresponding to the last day of the month of the given date
   */
  public static Date getLastDayOfMonth(Date date) {
    GregorianCalendar cal = getDefaultCalendar();
    cal.setTime(date);
    cal.add(Calendar.MONTH, 1);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.add(Calendar.DAY_OF_MONTH, -1);
    return cal.getTime();
  }

  /**
   * Returns the week number of today.
   *
   * @return the current week number
   */
  public static int getWeek() {
    return getWeek(now());
  }

  /**
   * Gets the week number of the date.
   *
   * @param date the date
   * @return week number of the current date
   */
  public static int getWeek(Date date) {
    String week = dateAsString(date, "ww");
    return Integer.valueOf(week).intValue();
  }

  /**
   * Gets the day number of the current date.
   *
   * @return the day number of the current date
   */
  public static int getDayInYear() {
    return getDayInYear(now());
  }

  /**
   * Gets the day number within the year of the given date.
   *
   * @param date the date
   * @return the number of the day in the year
   */
  public static int getDayInYear(Date date) {
    String week = dateAsString(date, "DDD");
    return Integer.valueOf(week).intValue();
  }

  /**
   * Adds a specific amount of time (expressed in days) to the current date.
   *
   * @param nbDays number of days to add
   * @return Date, the calculated date
   */
  public static Date addDays(int nbDays) {
    return addDays(now(), nbDays);
  }

  /**
   * Adds a specific amount of time (experssed in days) to the given date.
   *
   * @param date Date
   * @param nbDays number of days to add
   * @return the calculated date
   */
  public static Date addDays(Date date, int nbDays) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, nbDays);
    return calendar.getTime();
  }

  /**
   * Adds a specific amount of time (expressed in hours) to the given date.
   *
   * @param date Date
   * @param nbHours number of hours to add
   * @return the calculated date
   */
  public static Date addHours(Date date, int nbHours) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.HOUR_OF_DAY, nbHours);
    return calendar.getTime();
  }

  /**
   * Adds a specific amount of time (expressed in hours) to the current date.
   *
   * @param nbHours number of hours to add
   * @return the calculated date
   */
  public static Date addHours(int nbHours) {
    return addHours(now(), nbHours);
  }

  /**
   * Adds a specific amount of time (expressed in minutes) to the given date.
   *
   * @param date Date
   * @param nbMinutes number of minutes to add
   * @return the calculated date
   */
  public static Date addMinutes(Date date, int nbMinutes) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.MINUTE, nbMinutes);
    return calendar.getTime();
  }

  /**
   * Adds a specific amount of time (expressed in minutes) to the current date.
   *
   * @param nbMinutes number of minutes to add
   * @return the calculated date
   */
  public static Date addMinutes(int nbMinutes) {
    return addMinutes(now(), nbMinutes);
  }

  /**
   * Adds a specific amount of time (expressed in second) to the given date.
   *
   * @param date Date
   * @param nbSeconds number of seconds to add
   * @return the calculated date
   */
  public static Date addSeconds(Date date, int nbSeconds) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.SECOND, nbSeconds);
    return calendar.getTime();
  }

  /**
   * Adds a specific amount of time (expressed in second) to the current date.
   *
   * @param nbSeconds number of seconds to add
   * @return the calculated date
   */
  public static Date addSeconds(int nbSeconds) {
    return addSeconds(now(), nbSeconds);
  }

  /**
   * Gets the number of days between the given date and the current date.
   *
   * @param theDate date
   * @return int, number of days between date and current date
   */
  public static int getNbDays(Date theDate) {
    return getNbDays(now(), theDate);
  }

  /**
   * Gets the number of days between two dates. <br>
   * Compare only the date, not the time...
   *
   * @param theFirstDate date
   * @param theSecondDate date
   * @return int, number of days between the 2 dates
   */
  public static int getNbDays(Date theFirstDate, Date theSecondDate) {
    return getNbDays(theFirstDate, theSecondDate, true);
  }

  /**
   * Gets the number of days between two dates. <br>
   * Compare only the date, not the time...
   *
   * @param d1 date
   * @param d2 date
   * @param abs {@code true} for absolute value. If {@code false}, returns a positive value if d1 is after d2, a negative value otherwise
   * @return int, number of days between the 2 dates
   */
  public static int getNbDays(Date d1, Date d2, boolean abs) {
    int result;

    if (d1 == null || d2 == null) {
      result = 0;
    } else {
      if (DateUtils.isSameDay(d1, d2)) {
        result = 0;
      } else {
        long dif = getDayDate(d1).getTime() - getDayDate(d2).getTime();
        if (abs) {
          dif = Math.abs(dif);
        }
        double v = NumberUtils.divide(dif, 86400000, 10);
        result = (int) NumberUtils.round(v, 0);
      }
    }
    return result;
  }

  /**
   * Return the number of hours between two dates in absolute value <br>
   * Use only the date with hour precision to compare. 12H59 is interpreted as 12H, so {@code getNbHours(01/02/2014 13H01, 01/02/2014 12H59)} returns 1.</br>
   * </br>
   * 
   * @param date1 date 1
   * @param date2 date 2
   * @return the number of hours between the 2 dates
   */
  public static int getNbHours(Date date1, Date date2) {
    return getNbHours(date1, date2, true);
  }

  /**
   * Returns the number of hours between two dates. <br>
   * Use only the date with hour precision to compare. 12H59 is interpreted as 12H, so {@code getNbHours(01/02/2014 13H01, 01/02/2014 12H59)} returns 1.</br>
   * 
   * @param date1 date
   * @param date2 date
   * @param abs {@code true} for absolute value. If {@code false}, returns a positive value if d1 is after d2, a negative value otherwise
   * @return the number of hours between the 2 dates
   */
  public static int getNbHours(Date date1, Date date2, boolean abs) {
    int result;
    if (date1 == null || date2 == null) {
      result = 0;
    } else {
      long d1 = date1.getTime();
      long d2 = date2.getTime();
      if (d1 == d2) {
        result = 0;
      } else {
        long nbH1 = d1 / HOUR_IN_MILLIS;
        long nbH2 = d2 / HOUR_IN_MILLIS;
        result = (int) (nbH1 - nbH2);
        if (abs) {
          result = Math.abs(result);
        }
      }
    }

    return result;
  }

  /**
   * Returns the date corresponding to the specified date at 0H00m00s.
   *
   * @param d the date
   * @return the date at 0H00m00s
   */
  public static Date getDayDate(Date d) {
    Date today = TO_DAY.checkIsToday(d);
    if (today != null) {
      return today;
    }
    int[] details = getDateDetails(d, false);
    return getDate(details[2], details[1], details[0]);
  }

  /**
   * Return the currect date.
   *
   * @return the current date
   */
  public static Date now() {
    return (new Date());
  }

  /**
   * Checks if this day is a holiday in this country (language).
   *
   * @param language representing the country
   * @param date the date
   * @return true if the date if a holiday
   * @deprecated use <code>isHoliday(String country, Date date)</code> or an <code>HolydaysCalendar</code>
   */
  @Deprecated
  public static boolean isHoliday(Language language, Date date) {
    return isHoliday(language.toLocale().getCountry(), date);
  }

  /**
   * Checks if the date is an holiday in the specified country.
   *
   * @param country the country/region code as it's specified in Locale
   * @param date the date to check
   * @return true if the date is an holiday
   */
  public static boolean isHoliday(String country, Date date) {
    return isHoliday(date, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Checks if the date is an holiday according to the holiday calendar.
   *
   * @param date the date
   * @param holidaysCalendar the holiday calendar
   * @return true if the date is an holiday
   */
  public static boolean isHoliday(Date date, HolidaysCalendar holidaysCalendar) {
    if (holidaysCalendar != null) {
      return holidaysCalendar.isHoliday(date);
    }
    return defaultHolidaysCalendar.isHoliday(date);
  }

  /**
   * Checks if this day is a holiday using the default holidays calendar.
   *
   * @param date the date to test
   * @return true if the date is an holiday
   */
  public static boolean isHoliday(Date date) {
    return defaultHolidaysCalendar.isHoliday(date);
  }

  /**
   * Returns true if the specified date corresponds to a saturday or sunday.
   *
   * @param date the date to test
   * @return true if the date is a weed end day
   */
  public static boolean isWeekEndDay(Date date) {
    return defaultHolidaysCalendar.isWeekEnd(date);
  }

  /**
   * Returns true if the specified date corresponds to a week-end day in the specified country. <br>
   * A week-end day is not considered as work day.
   *
   * @param country the country/region code as it's specified in Locale
   * @param date the date to test
   * @return true if the date corresponds to a weed end
   */
  public static boolean isWeekEndDay(String country, Date date) {
    return isWeekEndDay(date, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returns true if the specified date corresponds to a week-end according to the specified holiday calendar.
   *
   * @param date the date to test
   * @param holidaysCalendar the holidays calendar
   * @return true if the date is a weed end day
   */
  public static boolean isWeekEndDay(Date date, HolidaysCalendar holidaysCalendar) {
    if (holidaysCalendar != null) {
      return holidaysCalendar.isWeekEnd(date);
    }
    return defaultHolidaysCalendar.isWeekEnd(date);
  }

  /**
   * Returns true if the specified date corresponds to a workday <br>
   * Use the default holidays calendar.
   *
   * @param date the date to test
   * @return true if the date is a workDay
   */
  public static boolean isWorkday(Date date) {
    return isWorkday(date, defaultHolidaysCalendar);
  }

  /**
   * Returns true if the specified date corresponds to a workday in the specified country <br>
   * Use the holidays calendar associated with the country.
   *
   * @param country the country/region code as it's specified in Locale
   * @param date the date to test
   * @return true if the date is a workDay
   */
  public static boolean isWorkday(String country, Date date) {
    return isWorkday(date, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returns true if the specified date corresponds to a workday using the specified holidays calendar.
   *
   * @param date the date
   * @param holidaysCalendar the holidays calendar
   * @return true if the date is a workDay
   */
  public static boolean isWorkday(Date date, HolidaysCalendar holidaysCalendar) {
    if (holidaysCalendar != null) {
      return holidaysCalendar.isWorkday(date);
    }
    return defaultHolidaysCalendar.isWorkday(date);
  }

  /**
   * Returne the number of work days between the two dates. <br>
   * The two dates are includes in the period. <br>
   * An half work day is count as a work day. <br>
   * Use the default holidays calendar. <br>
   * Carreful, do not use this API on large period (several years), that could introduce perf problems
   *
   * @param start start date
   * @param end end date
   * @return the number od work days or Integer.MIN_VALUE if one of the two dates is null.
   */
  public static int getNbWorkDay(Date start, Date end) {
    return getNbWorkDay(start, end, defaultHolidaysCalendar);
  }

  /**
   * Returne the number of work days between the two dates in the specified country. <br>
   * The two dates are includes in the period. <br>
   * An half work day is count as a work day. <br>
   * Carreful, do not use this API on large period (several years), that could introduce perf problems
   *
   * @param country the country/region code as it's specified in Locale
   * @param start start date
   * @param end end date
   * @return the number od work days or Integer.MIN_VALUE if one of the two dates is null.
   */
  public static int getNbWorkDay(String country, Date start, Date end) {
    return getNbWorkDay(start, end, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returne the number of work days between the two dates. <br>
   * The two dates are includes in the period. <br>
   * An half work day is count as a work day. <br>
   * Carreful, do not use this API on large period (several years), that could introduce perf problems
   *
   * @param start start date
   * @param end end date
   * @param holidaysCalendar holidays calendar
   * @return the number od work days or Integer.MIN_VALUE if one of the two dates is null.
   */
  public static int getNbWorkDay(Date start, Date end, HolidaysCalendar holidaysCalendar) {
    if (holidaysCalendar != null) {
      return holidaysCalendar.getNbWorkDay(start, end);
    }
    return defaultHolidaysCalendar.getNbWorkDay(start, end);
  }

  /**
   * Returne the number of work days between the two dates with an half day precision. <br>
   * The two dates are includes in the period. <br>
   * Use the default holidays calendar. <br>
   * Carreful, do not use this API on large period (several years), that could introduce perf problems
   *
   * @param start start date
   * @param end end date
   * @return the number od work days or Integer.MIN_VALUE if one of the two dates is null.
   */
  public static double getExactNbWorkDay(Date start, Date end) {
    return getExactNbWorkDay(start, end, defaultHolidaysCalendar);
  }

  /**
   * Returne the number of work days between the two dates in the specified country with an half day precision. <br>
   * The two dates are includes in the period. <br>
   * Carreful, do not use this API on large period (several years), that could introduce perf problems
   *
   * @param country the country/region code as it's specified in Locale
   * @param start start date
   * @param end end date
   * @return the number od work days or Integer.MIN_VALUE if one of the two dates is null.
   */
  public static double getExactNbWorkDay(String country, Date start, Date end) {
    return getExactNbWorkDay(start, end, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returne the number of work days between the two dates with an half day precision. <br>
   * The two dates are includes in the period. <br>
   * Carreful, do not use this API on large period (several years), that could introduce perf problems
   *
   * @param start start date
   * @param end end date
   * @param holidaysCalendar holidays calendar
   * @return the number od work days or Integer.MIN_VALUE if one of the two dates is null.
   */
  public static double getExactNbWorkDay(Date start, Date end, HolidaysCalendar holidaysCalendar) {
    if (holidaysCalendar != null) {
      return holidaysCalendar.getExactNbWorkDay(start, end);
    }
    return defaultHolidaysCalendar.getExactNbWorkDay(start, end);
  }

  /**
   * Returns the number of workDays in the month of the specified date using the default holidays calendar. <br>
   * An half work day is count as a work day.
   *
   * @param date the date
   * @return the number of workDays in the month
   */
  public static int getNbWorkDayInMonth(Date date) {
    int[] details = getDateDetails(date, false);
    int month = details[1];
    int year = details[2];
    return getNbWorkDayInMonth(month, year);
  }

  /**
   * Returns the number of workDays in the month of the specified date using the holidays calendar of the specified country. <br>
   * An half work day is count as a work day.
   *
   * @param country the country/region code as it's specified in Locale
   * @param date the date
   * @return the number of workDays in month
   */
  public static int getNbWorkDayInMonth(String country, Date date) {
    return getNbWorkDayInMonth(date, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returns the number of workDays in the month of the specified date using the specified holidays calendar. <br>
   * An half work day is count as a work day.
   *
   * @param date the date
   * @param holidaysCalendar the holidays calendar
   * @return the number of workDays
   */
  public static int getNbWorkDayInMonth(Date date, HolidaysCalendar holidaysCalendar) {
    int[] details = getDateDetails(date, false);
    int month = details[1];
    int year = details[2];
    return getNbWorkDayInMonth(month, year, holidaysCalendar);
  }

  /**
   * Returns the number of workDays in the specified month using the default holidays calendar. <br>
   * An half work day is count as a work day.
   *
   * @param month the month (1 for january, etc.)
   * @param year the year on four digit
   * @return the number of workDays
   */
  public static int getNbWorkDayInMonth(int month, int year) {
    Date start = getDate(year, month, 1);
    Date end = getDate(year, month, lastDayOfMonth(month, year));
    return getNbWorkDay(start, end);
  }

  /**
   * Returns the number of workDays in the specified month in the specified country. <br>
   * An half work day is count as a work day.
   *
   * @param country the country/region code as it's specified in Locale
   * @param month the month (1 for january, etc.)
   * @param year the year on four digit
   * @return the number of workDays
   */
  public static int getNbWorkDayInMonth(String country, int month, int year) {
    return getNbWorkDayInMonth(month, year, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returns the number of workDays in the specified month using the specified holidays calendar. <br>
   * An half work day is count as a work day.
   *
   * @param month the month (1 for january, etc.)
   * @param year the year on four digit
   * @param holidaysCalendar the holidays calendar
   * @return the number of workDays
   */
  public static int getNbWorkDayInMonth(int month, int year, HolidaysCalendar holidaysCalendar) {
    Date start = getDate(year, month, 1);
    Date end = getDate(year, month, lastDayOfMonth(month, year));
    return getNbWorkDay(start, end, holidaysCalendar);
  }

  /**
   * Returns the number of workDays in the month with an half day precision of the specified date using the default holidays calendar.
   *
   * @param date the date
   * @return the number of workDays in the month
   */
  public static double getExactNbWorkDayInMonth(Date date) {
    int[] details = getDateDetails(date, false);
    int month = details[1];
    int year = details[2];
    return getExactNbWorkDayInMonth(month, year);
  }

  /**
   * Returns the number of workDays in the month of the specified date with an half day precision using the holidays calendar of the specified country.
   *
   * @param country the country/region code as it's specified in Locale
   * @param date the date
   * @return the number of workDays in month
   */
  public static double getExactNbWorkDayInMonth(String country, Date date) {
    return getExactNbWorkDayInMonth(date, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returns the number of workDays in the month of the specified date with an half day precision using the specified holidays calendar.
   *
   * @param date the date
   * @param holidaysCalendar the holidays calendar
   * @return the number of workDays
   */
  public static double getExactNbWorkDayInMonth(Date date, HolidaysCalendar holidaysCalendar) {
    int[] details = getDateDetails(date, false);
    int month = details[1];
    int year = details[2];
    return getExactNbWorkDayInMonth(month, year, holidaysCalendar);
  }

  /**
   * Returns the number of workDays in the specified month with an half day precision using the default holidays calendar.
   *
   * @param month the month (1 for january, etc.)
   * @param year the year on four digit
   * @return the number of workDays
   */
  public static double getExactNbWorkDayInMonth(int month, int year) {
    Date start = getDate(year, month, 1);
    Date end = getDate(year, month, lastDayOfMonth(month, year));
    return getExactNbWorkDay(start, end);
  }

  /**
   * Returns the number of workDays in the specified month with an half day precision in the specified country.
   *
   * @param country the country/region code as it's specified in Locale
   * @param month the month (1 for january, etc.)
   * @param year the year on four digit
   * @return the number of workDays
   */
  public static double getExactNbWorkDayInMonth(String country, int month, int year) {
    return getExactNbWorkDayInMonth(month, year, HolidaysCalendarFactory.getHolidaysCalendar(country));
  }

  /**
   * Returns the number of workDays in the specified month with an half day precision using the specified holidays calendar.
   *
   * @param month the month (1 for january, etc.)
   * @param year the year on four digit
   * @param holidaysCalendar the holidays calendar
   * @return the number of workDays
   */
  public static double getExactNbWorkDayInMonth(int month, int year, HolidaysCalendar holidaysCalendar) {
    Date start = getDate(year, month, 1);
    Date end = getDate(year, month, lastDayOfMonth(month, year));
    return getExactNbWorkDay(start, end, holidaysCalendar);
  }

  /**
   * Adds a specific number of years to the current date.
   *
   * @param nbYear number of year to add
   * @return the calculated date
   */
  public static Date addYears(int nbYear) {
    return addYears(now(), nbYear);
  }

  /**
   * Adds a specific number of years to the given date.
   *
   * @param date initial date
   * @param nbYear number of year to add
   * @return the calculated date
   */
  public static Date addYears(Date date, int nbYear) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.YEAR, nbYear);
    return calendar.getTime();
  }

  /**
   * Adds a specific number of months to the given date.
   *
   * @param date initial date
   * @param nbMonth number of month to add
   * @return the calculated date
   */
  public static Date addMonths(Date date, int nbMonth) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.MONTH, nbMonth);
    return calendar.getTime();
  }

  /**
   * Adds a specific number of months to the current date.
   *
   * @param nbMonth number of month to add
   * @return the calculated date
   */
  public static Date addMonths(int nbMonth) {
    return addMonths(new Date(), nbMonth);
  }

  /**
   * Returns the date format used for the given language. <br>
   * This format is used to display a string representation of a date. <BR>
   * The format is define by the parameter DATE_FORMAT_<language int value> in the framework.properties file.
   * <ul>
   * <li>DATE_FORMAT_0=dd/MM/yyyy (format for french)
   * <li>DATE_FORMAT_1=MM/dd/yyyy (format for english)
   * <li>...
   * </ul>
   * <BR>
   * If the format is not define for the given language and the language is the default language, we use the default date format : {@link #DEFAULT_DATE_FORMAT}. <BR>
   * If the format is still not define for the given language, we use the format returns by the <code>DateFormat.getDateInstance(SHORT,locale)</code> method.
   *
   * @param l the language
   * @return the date format
   */
  public static DateFormat getDateFormat(Language l) {
    DateFormat format = getDateFormatInCache(l);
    if (format == null) {
      String sFormat = null;
      if (sFormat != null) {
        try {
          format = FormatFactory.getDateFormat(sFormat);
        } catch (Throwable ex) {
         
        }
      }
      if (format == null) {
        format = DateFormat.getDateInstance(DateFormat.SHORT, l.toLocale());
      }
      putDateFormatInCache(l, format);
    }
    return (format);
  }

  /**
   * Returns the date and time format used for the given language. <br>
   * This format is used to display a string representation of a date. <BR>
   * The format is define by the parameter DATETIME_FORMAT_<language int value> in the framework.properties file.
   * <ul>
   * <li>DATETIME_FORMAT_0=dd/MM/yyyy HH:mm:ss (format for french)
   * <li>DATETIME_FORMAT_1=MM/dd/yyyy hh:mm:ss a(format for english)
   * <li>...
   * </ul>
   * <BR>
   * If the format is not define for the given language and the language is the default language, we use the default date time format : {@link #DEFAULT_DATE_TIME_FORMAT} <BR>
   * If the format is not define for the given language, we use the format returns by the <code>DateFormat.getDateTimeInstance(SHORT,SHORT,locale)</code> method.
   *
   * @param l the language
   * @return the date and time format
   */
  public static DateFormat getDateTimeFormat(Language l) {
    DateFormat format = getDateTimeFormatInCache(l);
    if (format == null) {
      String sFormat =null;
      if (sFormat != null) {
        try {
          format = FormatFactory.getDateFormat(sFormat);
        } catch (Throwable ex) {
        }
      }
      if (format == null) {
        format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, l.toLocale());
      }
      putDateTimeFormatInCache(l, format);
    }
    return (format);
  }

  /**
   * Returns a string representation of the given date using the date format corresponding to the given language.<BR>
   *
   * @param date to convert into String
   * @param l language
   * @return String representation of the date
   */
  public static String dateAsString(Date date, Language l) {
    return (getDateFormat(l).format(date));
  }

  /**
   * Returns a string representation of the given date and time using the date format corresponding to the given language.<BR>
   *
   * @param date to convert into String
   * @param l language
   * @return String representation of the date
   */
  public static String dateTimeAsString(Date date, Language l) {
    return (getDateTimeFormat(l).format(date));
  }

  /**
   * Retruns a string representation of the given long using the given time format.
   *
   * @param l the long
   * @param format time format to use.
   * @return a string representation of the time
   */
  public static String longAsTime(long l, String format) {
    Date ref = getDate("01012000", "ddMMyyyy");
    Date d = new Date(ref.getTime() + l);

    return (dateAsString(d, format));
  }

  /**
   * Retruns a string representation of the given long using the default time format (HH:mm:ss).
   *
   * @param l the long
   * @return a string representation of the time
   */
  public static String longAsTime(long l) {
    return (longAsTime(l, "HH:mm:ss"));
  }

  /**
   * Returns the instance of Date corresponding to the current day at 0H00.
   *
   * @return the curent day at 0H00m00s
   */
  public static Date today() {
    return TO_DAY.getToday();
  }

  /**
   * Sets the year of the Date object to the specified value. The Date returned represents a point in time within the specified year, with the year, day, hour, minute, and second the same as before,
   * as interpreted in the local time zone. <br>
   * If the date does not exist for the year, the date returned will corresponds according to the calendar. <br>
   * eg : if date = "29/02/2004" setYear(date,2005) will return "01/03/2005"
   *
   * @param date the date to changed
   * @param year the year value
   * @return the new date
   */
  public static Date setYear(Date date, int year) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.YEAR, year);
    return calendar.getTime();
  }

  /**
   * Sets the month of the Date object to the specified value. The Date returned represents a point in time within the specified month, with the year, day, hour, minute, and second the same as before,
   * as interpreted in the local time zone. <br>
   * If the day does not exist for the month, the date will corresponds according to the calendar. <br>
   * eg : if date = "31/01/2004" setMonth(date,2) will return "02/03/2004"
   *
   * @param date the date to changed
   * @param month the month value (1 for january,...12 for december)
   * @return the new date
   */
  public static Date setMonth(Date date, int month) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.MONTH, month - 1);
    return calendar.getTime();
  }

  /**
   * Sets the day of the Date object to the specified value. The Date returned represents a point in time within the specified day of the month, with the year, month, hour, minute, and second the same
   * as before, as interpreted in the local time zone. <br>
   * If the day does not exist for the month, the date will corresponds according to the calendar. <br>
   * eg : if date = "12/02/2004" setDay(date,30) will return "01/03/2004"
   *
   * @param date the date to changed
   * @param day the day value
   * @return the new date
   */
  public static Date setDay(Date date, int day) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    return calendar.getTime();
  }

  /**
   * Sets the hour of the Date object to the specified value. The Date returned represents a point in time within the specified hour of the day, with the year, month, date, minute, and second the same
   * as before, as interpreted in the local time zone.
   *
   * @param date the date to changed
   * @param hour the hour value
   * @return the new date
   */
  public static Date setHours(Date date, int hour) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    return calendar.getTime();
  }

  /**
   * Sets the minutes of the Date object to the specified value. The returned Date object represents a point in time within the specified minute of the hour, with the year, month, date, hour, and
   * second the same as before, as interpreted in the local time zone.
   *
   * @param date the date to changed
   * @param minute the value of the minutes
   * @return the new date
   */
  public static Date setMinutes(Date date, int minute) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.MINUTE, minute);
    return calendar.getTime();
  }

  /**
   * Sets the seconds of the Date object to the specified value. The returned Date object represents a point in time within the specified second of the minute, with the year, month, date, hour, and
   * minute the same as before, as interpreted in the local time zone.
   *
   * @param date the date to changed
   * @param second the seconds value
   * @return the new date
   */
  public static Date setSeconds(Date date, int second) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.SECOND, second);
    return calendar.getTime();
  }

  /**
   * Sets the hours, minutes and seconds of the Date object to the specified values.
   *
   * @param date the date to changed
   * @param hour the hours value
   * @param minute the minutes value
   * @param second the seconds value
   * @return the new date
   */
  public static Date setTime(Date date, int hour, int minute, int second) {
    GregorianCalendar calendar = getDefaultCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, second);
    return calendar.getTime();
  }

  /**
   * Compares the day corresponding of two Dates for ordering.
   *
   * @param d1 the first Date to be compared
   * @param d2 the second Date to be compared
   * @return the value 0 if the two dates are equals or correpond to the same day; a value less than 0 if the d1 argument is before the d2 argument; and a value greater than 0 if the d1 argument is
   *         after the d2 argument.
   * @throws IllegalArgumentException if one of the date is null
   */
  public static int compareDayDate(Date d1, Date d2) {
    int res;
    if (d1 != null && d2 != null) {
      res = d1.compareTo(d2);
      if ((res != 0) && (isSameDay(d1, d2))) {
        res = 0;
      }
    } else {
      throw new IllegalArgumentException("DateUtils.compareDayDate One of the date is null");
    }
    return res;
  }

  /**
   * @return the date without time precision (date at 00H00:00)
   */
  public static Date removeTime(Date d) {
    if (d != null) {
      GregorianCalendar calendar = getDefaultCalendar();
      calendar.setTime(d);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar.getTime();
    }
    return null;
  }

  private static GregorianCalendar getDefaultCalendar() {
    GregorianCalendar theCalendar = defaultCalendar.get();
    if (theCalendar == null) {
      theCalendar = new GregorianCalendar();
      defaultCalendar.set(theCalendar);
    }
    return theCalendar;
  }

  private static void putDateFormatInCache(Language l, DateFormat format) {
    dateFormats.put(l, format);
  }

  private static DateFormat getDateFormatInCache(Language l) {
    return dateFormats.get(l);
  }

  private static void putDateTimeFormatInCache(Language l, DateFormat format) {
    dateTimeFormats.put(l, format);
  }

  private static DateFormat getDateTimeFormatInCache(Language l) {
    return dateTimeFormats.get(l);
  }

  static class ToDay {
    long maxTime;
    long todayTime;

    public ToDay() {
      super();
      calculate();
    }

    protected void calculate() {
      GregorianCalendar calendar = getDefaultCalendar();
      calendar.clear();
      calendar.setTime(new Date());
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      this.todayTime = calendar.getTimeInMillis();
      calendar.add(Calendar.DATE, 1);
      this.maxTime = calendar.getTime().getTime();
    }

    /**
     * @return the today
     */
    public Date getToday() {
      check();
      return new Date(this.todayTime);
    }

    protected void check() {
      if (System.currentTimeMillis() >= this.maxTime) {
        synchronized (this) {
          calculate();
        }
      }
    }

    /**
     * Check the date correspond to oday
     *
     * @param d la date
     * @return the date for today if ok, null otherwise
     */
    public Date checkIsToday(Date d) {
      if (isToday(d.getTime())) {
        return new Date(todayTime);
      }
      return null;
    }

    public boolean isToday(long t) {
      check();
      return t >= this.todayTime && t < this.maxTime;
    }
  }

  /**
   * Représentation de la date au format ISO avec millisecondes, timezone UTC (horodatage technique)
   */
  public static String dateAsIsoDateTimeMillisUTC(Date date) {
    return date.toInstant().toString();
  }
}