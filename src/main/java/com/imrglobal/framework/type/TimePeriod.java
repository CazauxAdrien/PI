/*
 * Created by thibaut_c
 * Date: 26 août 02
 * Time: 08:51:36
 */
package com.imrglobal.framework.type;

import java.util.Date;
import java.util.GregorianCalendar;
import java.io.Serializable;

import com.imrglobal.framework.utils.DateUtils;
import com.imrglobal.framework.utils.StringUtils;

/**
 * This type represents a period between two hours.<br>
 * A period can start a day and finish the day after if the hour of finish is inferior at the hour of start.
 * <ul>
 * <li>if startPeriod=23H30 and endPeriod=01H30, the period starts at 23H30 and finish at 01H30 the day after.
 * <li>if startPeriod=01H30 and endPeriod=23H30, the period starts at 01H30 and finish at 23H30 the same day.
 * <li>if startPeriod=endPeriod, the period is "empty".
 * </ul>
 */
public class TimePeriod implements Serializable {

	private static final String LONG_TIME_FORMAT = "HH:mm:ss";
  private static final String SHORT_TIME_FORMAT = "HH:mm";
	private static final Date MIDNIGHT = DateUtils.getDate(0, 1, 1, 0, 0, 0);
  private static final long MIDNIGHT_TIME = MIDNIGHT.getTime();

	private long startOfPeriodTime;
	private long endOfPeriodTime;
  private static final long serialVersionUID = 4341934165429961296L;

  /**
   * Create an empty period.
   */
  public TimePeriod() {
  }

	/**
	 * Create a period which starts and finish at the hours specified by the given Dates.
	 */
	public TimePeriod(Date startOfPeriod, Date endOfPeriod) {
		setPeriod(startOfPeriod, endOfPeriod);
	}

	/**
	 * Create a period which starts and finish at the hours specified by the given String using the format HH:mm:ss.
	 */
	public TimePeriod(String startOfPeriod, String endOfPeriod) {
		setPeriod(startOfPeriod, endOfPeriod);
	}

	/**
	 * Sets the start time of this period using the time of the specified date.
	 */
	public void setStartTime(Date startTime) {
		this.startOfPeriodTime = getTime(startTime);
	}

	/**
	 * Sets the end time of this period using the time of the specified date.
	 */
	public void setEndTime(Date endTime) {
		this.endOfPeriodTime = getTime(endTime);
	}

	/**
	 * Sets the start time of this period using the time of the specified date.
	 */
	public void setStartTime(String startTime) {
		this.startOfPeriodTime = getTime(startTime);
	}

	/**
	 * Sets the end time of this period using the time of the specified date.
	 */
	public void setEndTime(String endTime) {
		this.endOfPeriodTime = getTime(endTime);
	}

	/**
	 * Sets the start and finsh hours for this period using given Date.
	 */
	public void setPeriod(Date start, Date end) {
		setStartTime(start);
		setEndTime(end);
	}

	/**
	 * Sets the start and finsh hours for this period using given String with HH:mm:ss format.
	 */
	public void setPeriod(String start, String end) {
		setStartTime(start);
		setEndTime(end);
	}

	private long getTime(String sTime) {
    Date time;
    if (sTime != null) {
      if (sTime.length()>5) {
        time = DateUtils.getDate(sTime, LONG_TIME_FORMAT);
      } else {
        time = DateUtils.getDate(sTime, SHORT_TIME_FORMAT);
      }
    } else {
      time = null;
    }
		return getTime(time);
	}

	/**
	 * Returns true if this period represents an empty period.
	 * <BR>Any date is not include in an empty period.
	 */
	public boolean isEmpty() {
		return (this.startOfPeriodTime == this.endOfPeriodTime);
	}

	/**
	 * Returns true if the period start a day and finish the day after.
	 */
	public boolean isOnTwoDay() {
		return (this.startOfPeriodTime > this.endOfPeriodTime);
	}

	/**
	 * Returns the gap of this period.
	 */
	public TimeGap getTimeGap() {
		TimeGap gap = null;
		if (isOnTwoDay()) {
			gap = new TimeGap(endOfPeriodTime - startOfPeriodTime + DateUtils.DAY_IN_MILLIS);
		} else {
			gap = new TimeGap(endOfPeriodTime - startOfPeriodTime);
		}
		return gap;
	}

	/**
	 * Returns the start time of this period
	 * @return the start time of this period
	 */
	public Date getStartTime() {
		return (new Date(this.startOfPeriodTime));
	}

	/**
	 * Returns the end time of this period
	 * @return the end time of this period
	 */
	public Date getEndTime() {
		return (new Date(this.endOfPeriodTime));
	}

  /**
	 * Check if the hour of the given date is in period.
	 */
	public boolean isInPeriod(Date d) {
		long time = getTime(d);
    return checkTimeInMillis(time);
  }

  /**
   * Check if the date given in milliseconds is in period
   * @param dateInMillis
   * @return true if the date is in period
   */
  public boolean isInPeriod(long dateInMillis) {
    return checkTimeInMillis(getTime(dateInMillis));
  }

  /**
	 * Check if the given hour is in period. The hour to test is define by a String using the HH:mm:ss format.
	 */
	public boolean isInPeriod(String time) {
		return isInPeriod(getTime(time));
	}

  private boolean checkTimeInMillis(long time) {
    boolean res = false;
    if (!isEmpty()) {
      if (!isOnTwoDay()) {
        res = ((time >= this.startOfPeriodTime) && (time <= this.endOfPeriodTime));
      } else {
        res = ((time >= this.startOfPeriodTime) || (time <= this.endOfPeriodTime));
      }
    }
    return res;
  }

	private long getTime(Date date) {
		long time;
		if (date != null) {
			time = getTime(date.getTime());
		} else {
			time = MIDNIGHT_TIME;
		}
		return time;
	}

  private long getTime(long dateInMillis) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(dateInMillis);
    calendar.set(0,0,1);
    final long timeInMillis = calendar.getTimeInMillis();
    return timeInMillis;
  }

	/**
	 * A string representation of this period.
	 * <br>[hour of start - hour of finish]
	 * @return a string representation of the period
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (isEmpty()) {
			buf.append("][");
		} else {
			buf.append("[");
			buf.append(DateUtils.dateAsString(getStartTime(), LONG_TIME_FORMAT));
			buf.append(" - ");
			buf.append(DateUtils.dateAsString(getEndTime(), LONG_TIME_FORMAT));
			buf.append("]");
		}
		return (buf.toString());
	}

  /**
	 * Returns the time period corresponding to the given string.
	 * <br>The string must have the folowing format : <code>[startTime - endTime]</code> or <code>startTime - endTime</code>
   * end the startTime and endTime have one of the folowing format : <code>HH:mm:ss</code> or <code>HH:mm</code>
	 * <br> eg : <code>[08:30 - 15:30]</code> or <code>08:30:15 - 15:30:35</code>
	 * @param period
	 * @return a TimePeriod instance corresponding to the given string
	 */
  public static TimePeriod fromString(String period) {
    TimePeriod timePeriod = null;
    if (!StringUtils.isNullOrBlank(period)) {
      String sPeriod = period.trim();
      if (sPeriod.startsWith("[")) {
        sPeriod = sPeriod.substring(1);
      }
      if (sPeriod.endsWith("]")) {
        sPeriod = sPeriod.substring(0,sPeriod.length()-1);
      }
      int idx = sPeriod.indexOf("-");
      String startTime;
      String endTime;
      if (idx > 0) {
        startTime = sPeriod.substring(0,idx).trim();
        if (idx<sPeriod.length()-1) {
          endTime = sPeriod.substring(idx+1).trim();
        } else {
          endTime = null;
        }
      } else if (idx == 0) {
        startTime = null;
        endTime = sPeriod.substring(idx+1).trim();
      } else {
        startTime = sPeriod.trim();
        endTime = null;
      }
      timePeriod = new TimePeriod(startTime,endTime);
    }
    return timePeriod;
  }


}
