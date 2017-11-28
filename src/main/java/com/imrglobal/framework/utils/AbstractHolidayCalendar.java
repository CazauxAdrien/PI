package com.imrglobal.framework.utils;

import java.util.*;

/**
 * 
 * 
 */
public abstract class AbstractHolidayCalendar implements HolidaysCalendar {

  private static Map easterCache = new HashMap();
  private String country;

  protected AbstractHolidayCalendar() {
    this(null);
  }

  protected AbstractHolidayCalendar(String country) {
    this.country = country;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Returns true if the date correspond to an holiday for this calendar.
   * @param date
   * @return true if the date corresponds to an holiday
   */
	public boolean isHoliday(Date date) {
		return getHolidayCoef(date)<1;
	}

  /**
   * Check if the date corresponds to a week-end.
   * @param date
   * @return true if the date corresponds to a saturday or sunday
   */
  public boolean isWeekEnd(Date date) {
		return getWeekDayCoef(date)<1;
  }

  public boolean isWorkday(Date date) {
    return (getWorkCoef(date)>0);
  }

  public int getNbWorkDay(Date start, Date end) {
    int res=0;
    if ((start != null) && (end != null)) {
      Date d, endDate;
      if (start.compareTo(end)<0) {
				d=start;
	      endDate=end;
      } else {
	      d=end;
	      endDate=start;
      }
      GregorianCalendar calendar = new GregorianCalendar();
		  calendar.setTime(d);
	    while ((d != null)&&(!DateUtils.isNextDay(endDate,d))) {
        if (isWorkday(d)) {
          res++;
        }
        calendar.add(Calendar.DATE,1);
		    d=calendar.getTime();
	    }
    } else {
	    res = Integer.MIN_VALUE;
    }
	  return res;
  }

  public double getExactNbWorkDay(Date start, Date end) {
    double res=0;
    if ((start != null) && (end != null)) {
      Date d, endDate;
      if (start.compareTo(end)<0) {
				d=start;
	      endDate=end;
      } else {
	      d=end;
	      endDate=start;
      }
      GregorianCalendar calendar = new GregorianCalendar();
		  calendar.setTime(d);
	    while ((d != null)&&(!DateUtils.isNextDay(endDate,d))) {
				res=res+getWorkCoef(d);
				calendar.add(Calendar.DATE,1);
		    d=calendar.getTime();
	    }
    } else {
	    res = Double.NaN;
    }
	  return res;

  }

  public double getWorkCoef(Date date) {
    double c1 = getHolidayCoef(date);
    if (c1<1) return c1;
    return getWeekDayCoef(date);
  }

  /**
   * Returns a coef according to the date correspond to an holiday.
   * <br>This implementation call :
   * <ul>
   * <li><code>checkFixedHoliday</code>
   * <li><code>checkMovingHoliday</code>
   * </ul>
   * @param d
   * @return 0 if the date is an holiday, 0.5 if it's an half day, 1 if it's not an holiday
   */
  public double getHolidayCoef(Date d) {
    int[] dateInfo = DateUtils.getDateDetails(d,false);
    int jj = dateInfo[0];
		int mm = dateInfo[1];
		int aa = dateInfo[2];

    //Vérification de jours férié à date fixe
    double c1 = checkFixedHoliday(jj,mm);
    if (c1 < 1) return c1;

    //Vérification des jours férié à date mobile
    double c2 = checkMovingHoliday(jj,mm,aa);
    if (c2 < 1) return c2;
    return 1;
  }

  /**
   * Returns the coef for a regular day
   * @param d
   * @return the coef for the week day corresponding to the date
   */
  public double getWeekDayCoef(Date d) {
    int weekDay = DateUtils.getWeekDay(d);
		return getWeekDayCoef(weekDay);
  }

  /**
   * Returns the coef for the week day
   * @param weekDay
   * @return 0 for staurday and sunday, 1 otherwise
   */
  public double getWeekDayCoef(int weekDay) {
    return ((weekDay==GregorianCalendar.SATURDAY) ||(weekDay == GregorianCalendar.SUNDAY)) ? 0 : 1;
  }

  /**
   * Check if the day and month correspond to fixed holiday (as 01/01, 25/12...)
   * @param jj
   * @param mm
   * @return 0 if day and month correspond to a fixed holiday or 0.5 if it's a half holiday
   */
  protected abstract double checkFixedHoliday(int jj, int mm);

  /**
   * Check if the date correspond to a moving holiday (as easter...)
   * @param jj
   * @param mm
   * @param aa
   * @return 0 if day and month correspond to a moving holiday or 0.5 if it's a half holiday
   */
  protected abstract double checkMovingHoliday(int jj, int mm, int aa);

  /**
   * Returns the easter day for the specified year
   * @param year
   * @return the date corresponding to easter monday
   */
  public static Date getEasterMonday(int year) {
    String key = Integer.toString(year);
    Date easter = (Date) easterCache.get(key);
    if (easter == null) {
      easter = calculateEasterMonday(year);
      easterCache.put(key,easter);
    }
    return easter;
  }

  /**
   * Returns the ascension day for the specified year
   * @param year
   * @return the ascension day
   */
  public static Date getAscension(int year) {
    return calculateAscension(getEasterMonday(year));
  }

  /**
   * Returns the pentecost day for the specified year
   * @param year
   * @return the pentecost day
   */
  public static Date getPentecost(int year) {
    return calculatePentecost(getEasterMonday(year));
  }

  protected static Date calculateEasterMonday(int aa) {
    int nbOr;
    int ePacte;
    nbOr = (aa % 19) + 1;
    ePacte = (11 * nbOr - (3 + (int) ((2 + (int) (aa / 100)) * 3 / 7))) % 30;
    Date plune = DateUtils.getDate(aa, 4, 19);
    long ll = plune.getTime() - DateUtils.DAY_IN_MILLIS * ((ePacte + 6) % 30);
    if (ePacte == 24)
      ll = ll - DateUtils.DAY_IN_MILLIS;
    if ((ePacte == 25) && ((aa >= 1900) && (aa < 2200)))
      ll = ll - DateUtils.DAY_IN_MILLIS;

    plune = new Date(ll);

    Date paques = DateUtils.addDays(plune, 7 + GregorianCalendar.MONDAY - DateUtils.getWeekDay(plune));
    return paques;
  }

  protected static Date calculateAscension(Date easterMonday) {
    return DateUtils.addDays(easterMonday, 38);
  }

  protected static Date calculatePentecost(Date easterMonday) {
    return DateUtils.addDays(easterMonday, 49);
  }
}
