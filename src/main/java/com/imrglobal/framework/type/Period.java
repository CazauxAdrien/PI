package com.imrglobal.framework.type;

import java.util.Date;
import java.io.Serializable;

import com.imrglobal.framework.nls.Language;
import com.imrglobal.framework.utils.DateUtils;

/**
 * This class represents a period between two date.
 */
public class Period implements Serializable, Cloneable {

  /**
   * Represents an infinite period.
   */
  public static final Period INFINITE = new Period(Long.MIN_VALUE, Long.MAX_VALUE);

  /**
   * Represents an empty period.
   */
  public static final Period EMPTY = new Period();

  protected long startOfPeriodTime;
  protected long endOfPeriodTime;
  private static final long serialVersionUID = 3021099547973155250L;

  /**
   * Allocates a Period object and initializes it to represent
   * an empty period.
   */
  public Period() {
    this(0, 0);
  }

  private Period(long startOfPeriodTime, long endOfPeriodTime) {
    this.startOfPeriodTime = startOfPeriodTime;
    this.endOfPeriodTime = endOfPeriodTime;
  }

  /**
   * Allocates a Period object and initializes it to represent
   * a period between the two specified dates.
   * <BR>If the startPeriod and the endPeriod are null, it will create an infinite period.
   * <BR>If only the start period is null, it will create a startless period.
   * <BR>If only the end period is null, it will create a endless period.
   *
   * @param startOfPeriod date of start of the period
   * @param endOfPeriod   date of end of the period
   */
  public Period(Date startOfPeriod, Date endOfPeriod) {
    setStartOfPeriod(startOfPeriod);
    setEndOfPeriod(endOfPeriod);
  }


  /**
   * Allocates a Period object and initializes it to represent
   * a period which starting to the specified date and which have the
   * specified length.
   *
   * @param startOfPeriod date of start of the period
   * @param gap           length of the period
   */
  public Period(Date startOfPeriod, TimeGap gap) {
    setStartOfPeriod(startOfPeriod);
    if (!gap.isInfinite()) {
      setEndOfPeriod(startOfPeriod.getTime() + gap.getGap());
    } else {
      setEndOfPeriod(Long.MAX_VALUE);
    }
  }

  /**
   * Allocates a Period object and initializes it to represent
   * a period which ending to the specified date and which have the
   * specified length.
   *
   * @param gap         length of the period
   * @param endOfPeriod date of end of the period
   */
  public Period(TimeGap gap, Date endOfPeriod) {
    setEndOfPeriod(endOfPeriod);
    if (!gap.isInfinite()) {
      setStartOfPeriod(endOfPeriod.getTime() - gap.getGap());
    } else {
      setStartOfPeriod(Long.MIN_VALUE);
    }
  }

  /**
   * Set the start date of this period.
   *
   * @param startOfPeriod start of the period, null if is a startless period.
   */
  public void setStartOfPeriod(Date startOfPeriod) {
    this.startOfPeriodTime = (startOfPeriod != null ? startOfPeriod.getTime() : Long.MIN_VALUE);
  }

  /**
   * Set the end date of this period.
   *
   * @param endOfPeriod end of the period, null if is a endless period.
   */
  public void setEndOfPeriod(Date endOfPeriod) {
    this.endOfPeriodTime = (endOfPeriod != null ? endOfPeriod.getTime() : Long.MAX_VALUE);
  }

  private void setStartOfPeriod(long startOfPeriod) {
    this.startOfPeriodTime = startOfPeriod;
  }

  private void setEndOfPeriod(long endOfPeriod) {
    this.endOfPeriodTime = endOfPeriod;
  }

  /**
   * Indicates if this period represents an empty period.
   * <BR>Any date is not include in an empty period.
   *
   * @return true if this period represents an empty period.
   */
  public boolean isEmpty() {
    return (this.startOfPeriodTime == this.endOfPeriodTime);
  }

  /**
   * Indicates if this period have no start date and no end date.
   * <BR>Any date is include in an infinite period.
   *
   * @return true if this period have no start date and no end date.
   */
  public boolean isInfinite() {
    return ((this.startOfPeriodTime == Long.MIN_VALUE) && (this.endOfPeriodTime == Long.MAX_VALUE));
  }

  /**
   * Indicates if this period have a start date and no end date.
   * <BR>Any date after the start date is include in an endless period.
   *
   * @return true if this period have a start date and no end date.
   */
  public boolean isEndless() {
    return ((this.startOfPeriodTime > Long.MIN_VALUE) && (this.endOfPeriodTime == Long.MAX_VALUE));
  }

  /**
   * Indicates if this period have no start date but has an end date.
   * <BR>Any date before to the end date is include in a startless period.
   *
   * @return true if this period have no start date but has an end date.
   */
  public boolean isStartless() {
    return ((this.startOfPeriodTime == Long.MIN_VALUE) && (this.endOfPeriodTime < Long.MAX_VALUE));
  }


  /**
   * Returns the end date of the period
   * <BR>Return null if is a Endless or infinite period.
   *
   * @return the end date of the period, null if infinite
   */
  public Date getEndOfPeriod() {
    return ((isInfinite()) || (isEndless()) ? null : new Date(this.endOfPeriodTime));
  }

  /**
   * Returns the start date of this period
   * <BR>Return null if is a Startless or infinite period.
   *
   * @return the start date of this period
   */
  public Date getStartOfPeriod() {
    return ((isInfinite()) || (isStartless()) ? null : new Date(this.startOfPeriodTime));
  }

  /**
   * Returns the length of the period.
   * <BR>If this period is infinite, endless or startless, then return an infinite time gap.
   *
   * @return a TimeGap representing the length of the period.
   */
  public TimeGap getTimeGap() {
    TimeGap gap = null;
    if ((isInfinite()) || (isEndless()) || (isStartless())) {
      gap = new TimeGap(Long.MAX_VALUE);
    } else {
      gap = new TimeGap(endOfPeriodTime - startOfPeriodTime);
    }
    return (gap);
  }


  /**
   * Checks if the specified date is in this period.
   * <br>The start date and the end date are considered inside the
   * period.
   *
   * @param d the date to check
   * @return true if the date is in the period, false if the date is null or
   * not in the period.
   */
  public boolean isInPeriod(Date d) {
    return isInPeriod(d, false, false);
  }

  /**
   * Vérifie que la date passée en paramètre est incluse dans la période en excluant éventuellement les dates de début et de fin de la période
   *
   * @param d la date à vérifier
   * @param exclureDateDebut {@code true} pour exclure la date de début de la période
   * @param exclureDateFin {@code true} pour exclure la date de fin de la période
   * @return true if the date is in the period, false if the date is null or
   * not in the period.
   */
  public boolean isInPeriod(Date d, boolean exclureDateDebut, boolean exclureDateFin) {
    if (d != null) {
      long time = d.getTime();
      return ((exclureDateDebut ? time > this.startOfPeriodTime : time >= this.startOfPeriodTime) &&
        (exclureDateFin ? time < this.endOfPeriodTime : time <= this.endOfPeriodTime));
    }
    return false;
  }

  /**
   * Returns the period corresponding to the intersection between this period
   * and the given period.
   * <BR>If there is no intersection, returns an empty period.
   *
   * @param period the period
   * @return the period corresponding to the intersection between this period
   * and the given period.
   */
  public Period getIntersection(Period period) {
    Period inter = null;
    if (period != null) {
      long startTime = (this.startOfPeriodTime >= period.startOfPeriodTime ? this.startOfPeriodTime : period.startOfPeriodTime);
      long endTime = (this.endOfPeriodTime <= period.endOfPeriodTime ? this.endOfPeriodTime : period.endOfPeriodTime);
      if (startTime <= endTime) {
        inter = new Period(startTime, endTime);
      } else {
        inter = EMPTY;
      }
    } else {
      throw new IllegalArgumentException("Period.getIntersection : can not calculate the intersection with a null period.");
    }
    return (inter);
  }

  /**
   * Returns true if the given period is include in this period.
   *
   * @param p the period
   * @return true if the given period is include in this period.
   */
  public boolean isInclude(Period p) {
    boolean isInclude = false;
    if (p != null) {
      if ((this.startOfPeriodTime <= p.startOfPeriodTime) &&
        (this.endOfPeriodTime >= p.endOfPeriodTime)) {
        isInclude = true;
      }
    }
    return (isInclude);
  }


  /**
   * A string representation of this period.
   * <br>Equivalent to <code>toString(Language.DEFAULT)</code>
   *
   * @return a string representation of this period
   */
  public String toString() {
    return (toString(Language.DEFAULT));
  }


  /**
   * A string representation of this period for the specified language.
   *
   * @param language language
   * @return a string representation of this period
   */
  public String toString(Language language) {
    StringBuilder buf = new StringBuilder();
    if (isEmpty()) {
      buf.append("][");
    } else {
      if ((isInfinite()) || (isStartless())) {
        buf.append("]-oo");
      } else {
        buf.append("[");
        buf.append(DateUtils.dateTimeAsString(getStartOfPeriod(), language));
      }
      buf.append(" - ");
      if ((isInfinite()) || (isEndless())) {
        buf.append("+oo[");
      } else {
        buf.append(DateUtils.dateTimeAsString(getEndOfPeriod(), language));
        buf.append("]");
      }
    }
    return (buf.toString());
  }

  /**
   * Two period are equals :
   * <ul>
   * <li>if the both period are infinites.
   * <li>if the both periods are startless and their end date are equals.
   * <li>if the both periods are endless and their start date are equals.
   * <li>if their start date are equals and their end date are equals.
   * </ul>
   */
  public boolean equals(Object obj) {
    boolean equal = false;
    if (obj instanceof Period) {
      Period p = (Period) obj;
      if ((this.startOfPeriodTime == p.startOfPeriodTime) &&
        (this.endOfPeriodTime == p.endOfPeriodTime)) {
        equal = true;
      }
    }
    return (equal);
  }

  @Override
  public int hashCode() {
    int result = (int) (startOfPeriodTime ^ (startOfPeriodTime >>> 32));
    result = 31 * result + (int) (endOfPeriodTime ^ (endOfPeriodTime >>> 32));
    return result;
  }

  @Override
  public Period clone() {
    return new Period(this.startOfPeriodTime, this.endOfPeriodTime);
  }
}