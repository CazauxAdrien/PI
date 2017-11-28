package com.imrglobal.framework.type;

import java.io.Serializable;
import java.text.*;
import java.util.Date;

import com.imrglobal.framework.utils.*;

/**
 * This class represents a time gap, with millisecond precision.
 */
public class TimeGap implements Serializable, Comparable {

  private long gap;

  private long nbDays;
  private int nbHours;
  private int nbMinutes;
  private int nbSecondes;

  private static final long DAY_IN_MILLIS = DateUtils.DAY_IN_MILLIS;
  private static final long HOUR_IN_MILLIS = DateUtils.HOUR_IN_MILLIS;
  private static final long MINUTE_IN_MILLIS = DateUtils.MINUTE_IN_MILLIS;
  private static final long serialVersionUID = 1484139074431456660L;

  /**
   * Represents an infinite gap
   */
  public static final TimeGap INFINITE = new TimeGap(Long.MAX_VALUE);

  /**
   * Allocates a TimeGap object and initializes it to represent
   * a time gap of the specified number of milliseconds.
   *
   * @param gap gap in milliseconds
   */
  public TimeGap(long gap) {
    setGap(gap);
  }

  /**
   * Allocates a TimeGap object and initializes it so that it represents
   * a time gap which is the sum of the spcified numbers of day, hour, minutes,
   * seconds end milliseconds.
   * <BR> For example : <code>new TimeGap(1,2,3,4,5)<:code> allocates an
   * object whic represents a time gap of 1 day 2 hours 3 minutes 4 seconds and
   * 5 milliseconds.
   *
   * @param days         number of days
   * @param hours        number of hour
   * @param minutes      number of minutes
   * @param seconds      number of seconds
   * @param milliSeconds number of milliseconds
   */
  public TimeGap(int days, int hours, int minutes, int seconds, int milliSeconds) {
    this.nbDays = days;
    this.nbHours = hours;
    this.nbMinutes = minutes;
    this.nbSecondes = seconds;
    this.gap = (days * DAY_IN_MILLIS + hours * HOUR_IN_MILLIS + minutes * MINUTE_IN_MILLIS + seconds * 1000 + milliSeconds);
  }

  /**
   * Allocates a TimeGap object and initializes it so that it represents
   * a time gap between the two specified dates.
   *
   * @param d1 first date.
   * @param d2 second date.
   */
  public TimeGap(Date d1, Date d2) {
    long l1 = d1.getTime();
    long l2 = d2.getTime();
    if (l1 <= l2) {
      setGap(l2 - l1);
    } else {
      setGap(l1 - l2);
    }
  }

  /**
   * Check if this TimeGap represents an infinte time gap.
   *
   * @return true if this TimeGap represents an infinte time gap.
   */
  public boolean isInfinite() {
    return (this.gap == Long.MAX_VALUE);
  }

  protected void setGap(long gap) {
    this.gap = gap;
    this.nbDays = (this.gap / DAY_IN_MILLIS);
    long dayTime = getNbDays() * DAY_IN_MILLIS;
    this.nbHours = (int) ((this.gap - dayTime) / HOUR_IN_MILLIS);
    long hourTime = getNbHours() * HOUR_IN_MILLIS;
    this.nbMinutes = (int) ((this.gap - dayTime - hourTime) / MINUTE_IN_MILLIS);
    long minTime = getNbMinutes() * MINUTE_IN_MILLIS;
    this.nbSecondes = (int) ((this.gap - dayTime - hourTime - minTime) / 1000);
  }


  /**
   * Returns the number of days of this gap
   * in a time slicing in days, hours, minutes, seconds, milliseconds.
   *
   * @return the number of days (0 if the gap is less than one day).
   */
  public long getNbDays() {
    return this.nbDays;
  }


  /**
   * Returns the number of hours of this gap
   * in a time slicing in days, hours, minutes, seconds, milliseconds.
   *
   * @return the number of hours
   */
  public int getNbHours() {
    return this.nbHours;
  }


  /**
   * Returns the number of minutes of this gap
   * in a time slicing in days, hours, minutes, seconds, milliseconds.
   *
   * @return the number of minutes
   */
  public int getNbMinutes() {
    return this.nbMinutes;
  }


  /**
   * Returns the number of seconds of this gap
   * in a time slicing in days, hours, minutes, seconds, milliseconds.
   *
   * @return the number of seconds
   */
  public int getNbSeconds() {
    return this.nbSecondes;
  }

  /**
   * Returns the number of milliseonds of this gap
   * in a time slicing in days, hours, minutes, seconds, milliseconds.
   *
   * @return the number of milliseonds of this gap
   */
  public long getMilliSeconds() {
    return (this.gap - (getNbDays() * DAY_IN_MILLIS + getNbHours() * HOUR_IN_MILLIS + getNbMinutes() * MINUTE_IN_MILLIS + getNbSeconds() * 1000));
  }


  /**
   * Returns the gap in milliseconds.
   *
   * @return the gap in milliseconds
   */
  public long getGap() {
    return (this.gap);
  }

  /**
   * Returns this gap in days.
   * <br>If <code>getNbHours()</code> >= 12 then returns <code>getNbDays()+1</code>
   * <br>Else, returns <code>getNbDays()+1</code>
   *
   * @return this gap in days
   */
  public long inDays() {
    int h = getNbHours();
    return (h >= 12 ? getNbDays() + 1 : getNbDays());
  }


  /**
   * Returns a string representation of this gap.
   *
   * @return a string representation of this gap
   */
  public String toString() {
    StringBuilder buf = new StringBuilder();
    if (!isInfinite()) {
      long l = getNbDays();
      if (l > 0) {
        buf.append(l);
        buf.append(" day(s) ");
      }
      l = getNbHours();
      if (l > 0) {
        buf.append(l);
        buf.append(" h ");
      }
      l = getNbMinutes();
      if (l > 0) {
        buf.append(l);
        buf.append(" mn ");
      }
      l = getNbSeconds();
      if (l > 0) {
        buf.append(l);
        buf.append(" s ");
      }
      l = getMilliSeconds();
      if (l > 0 || (l ==0 && buf.length()==0)) {
        buf.append(l);
        buf.append(" ms ");
      }
    } else {
      buf.append("infinite time gap");
    }
    return (buf.toString());
  }

  /**
   * Returns a string representation of this time gap using the specified format.
   * <br>The format use pattern as {@link java.text.SimpleDateFormat}. The only elements allowed in the format are :
   * <ul>
   * <li>dd : to represents the number of days
   * <li>HH : to represents the number of hours
   * <li>mm : to represents the number of minutes
   * <li>ss : to represents the number of seconds
   * <li>SS : to represents the number of milliseconds
   * </ul>
   * <br>Eg. :
   * <ul>
   * <li><code>dd 'day(s)' HH 'h' mm 'mn' ss 's' SS 'ms'</code> is a valid format
   * <li><code>d 'day(s)' H 'h' m 'mn' s 's' S 'ms'</code> is not a valid format
   * </ul>
   *
   * @param format the format
   * @return a string representation of this time gap using the specified format
   */
  public String format(String format) {
    String[] parts = format.split("'");
    if (parts.length > 1) {
      StringBuilder bf = new StringBuilder();
      for (int i = 0; i < parts.length; i++) {
        String part = parts[i];
        if (i % 2 == 0) {
          bf.append(format(part));
        } else {
          bf.append(part);
        }
      }
      return bf.toString();
    } else {
      String res = format.replaceAll("dd", longAsString(getNbDays()));
      res = res.replaceAll("HH", longAsString(getNbHours()));
      res = res.replaceAll("mm", longAsString(getNbMinutes()));
      res = res.replaceAll("ss", longAsString(getNbSeconds()));
      res = res.replaceAll("SS", longAsString(getMilliSeconds()));
      return res;
    }

  }

  private String longAsString(long l) {
    if (l < 10) {
      return "0" + l;
    } else {
      return Long.toString(l);
    }
  }


  /**
   * Returns the date corresponding to this gap add to the specified
   * date.
   *
   * @param d the date
   * @return the date + this gap
   */
  public Date addToDate(Date d) {
    return (new Date(d.getTime() + this.gap));
  }

  /**
   * Two TimeGap are equals if their gap are equals.
   */
  public boolean equals(Object obj) {
    boolean equal = false;
    if (obj instanceof TimeGap) {
      TimeGap g = (TimeGap) obj;
      equal = (this.gap == g.gap);
    }
    return (equal);
  }

  @Override
  public int hashCode() {
    return (int) (gap ^ (gap >>> 32));
  }

  public int compareTo(Object o) {
    TimeGap timeGap = (TimeGap) o;
    final long thisGap = this.gap;
    final long otherGap = timeGap.gap;
    return (thisGap < otherGap ? -1 : (thisGap == otherGap ? 0 : 1));
  }

  /**
   * Return the instance of time gap corresponding to the string using the specified format
   *
   * @param str    the string
   * @param format using {@link java.text.SimpleDateFormat} pattern (eg. : HH:mm)
   * @return the gap or null
   */
  public static TimeGap fromString(String str, String format) {
    try {
      if (!StringUtils.isNullOrBlank(str)) {
        if (StringUtils.isNullOrBlank(str)) {
          throw new IllegalArgumentException("TimeGap.fromString invalid format! :" + format);
        }

        DateFormat dateFormat = FormatFactory.getDateFormat(format);
        Date d0 = initialDate(dateFormat, format);
        Date d;
        synchronized (dateFormat) {
          d = dateFormat.parse(str);
        }

        return new TimeGap(d0, d);
      } else {
        return null;
      }
    } catch (ParseException e) {
      return null;
    }
  }

  private static Date initialDate(DateFormat dateFormat, String format) throws ParseException {
    //cas des format complexes avec des chaînes de caractère : dd'days' HH 'h' mm par exemple
    DateFormat dateFormatclean;
    String fmt = StringUtils.clearBetweenSeparator(format, "'");
    if (fmt.equals(format)) {
      dateFormatclean = dateFormat != null ? dateFormat : FormatFactory.getDateFormat(fmt);
    } else {
      dateFormatclean = FormatFactory.getDateFormat(fmt);
    }
    synchronized (dateFormatclean) {
      return dateFormatclean.parse(fmt.replaceAll("\\w", "0"));
    }
  }

}