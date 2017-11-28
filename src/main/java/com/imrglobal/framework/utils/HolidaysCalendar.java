package com.imrglobal.framework.utils;

import java.util.Date;

/**
 * Allows to determine if a date corresponds to an holiday or a week-end.
 * 
 */
public interface HolidaysCalendar {

	/**
	 * Returns true if the specified date correponds to an holiday date.
	 * @param date the date to check
	 * @return true if the date correponds to an holiday
	 */
	public boolean isHoliday(Date date);

  /**
   * Returns true if the given date corresponds to a week-end.
   * <br>A week-end day is not considered as work day.
   * @param date
   * @return true if the date is a week-end day
   */
  public boolean isWeekEnd(Date date);

  /**
	 * Returns true if the specified date corresponds to a workday
	 * @param date
	 * @return true if the date is not a weekEnd or an holidays
	 */
	public boolean isWorkday(Date date);

  /**
   * Returns the number of work days between the two dates.
   * <br>The two dates are includes in the period.
   * <br>An half work day is considered as a work day
   * <br>Carreful, do not use this API on large period (several years), that could introduce perf problems
   * @param start
   * @param end
   * @return the number od work days or Integer.MIN_VALUE if one of the
   * two dates is null.
   */
  public int getNbWorkDay(Date start, Date end);

  /**
   * Returns the number of work days between the two dates with an half day precision.
   * <br>The two dates are includes in the period.
   * <br>Carreful, do not use this API on large period (several years), that could introduce perf problems
   * @param start
   * @param end
   * @return the number od work days or <code>Double.NaN</code> if one of the
   * two dates is null.
   */
  public double getExactNbWorkDay(Date start, Date end);

  /**
   * Returns the country/region code associated with this holidays calendar as it's specified in Locale
   * @return the country/region code as it's specified in Locale
   */
  public String getCountry();

  /**
   * Returns the country/region code associated with this holidays calendar as it's specified in Locale
   * @param country the country/region code as it's specified in Locale
   */
  public void setCountry(String country);

  /**
   * Returns number between 0 and 1 according to the date correspond to a work day, a half day of work or an holiday.
   * @param date
   * @return 0 if no work, 0.5 if half day of work, 1 if work day
   */
  public double getWorkCoef(Date date);





}
