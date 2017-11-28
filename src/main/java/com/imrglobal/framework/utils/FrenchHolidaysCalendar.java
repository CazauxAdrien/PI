package com.imrglobal.framework.utils;

import java.util.Date;

/**
 * French holidays calendar.
 * <br>Fixed public hollidays :
 * <ul>
 * <li>01/01
 * <li>01/05
 * <li>08/05
 * <li>14/07
 * <li>15/08
 * <li>01/11
 * <li>11/11
 * <li>24/12
 * </ul>
 * <br>moving hollidays
 * <ul>
 * <li>easter : if {@link #isEasterNonWorking()} returns true
 * <li>ascension : if {@link #isAscensionNonWorking()} returns true
 * <li>pentecost : if {@link #isPentecostNonWorking()} returns true
 * </ul>
 */
public class FrenchHolidaysCalendar extends AbstractHolidayCalendar {

  /**
   * default value of pentecostNonWorking property of the calss : value of property IS_PENTECOTE_FERIE in framework.properties file.
   * <br>false by default
   */
  public static final boolean IS_PENTECOTE_FERIE = true;

  /**
   * default value of ascensionNonWorking property of the calss : value of property IS_ASCENSION_FERIE in framework.properties file
   * <br>true by default
   */
  public static final boolean IS_ASCENSION_FERIE = true;

  /**
   * default value of easterNonWorking property of the calss : value of property IS_PAQUE_FERIE in framework.properties file
   * <br>true by default
   */
  public static final boolean IS_PAQUE_FERIE = true;

  private boolean pentecostNonWorking;
  private boolean ascensionNonWorking;
  private boolean easterNonWorking;

  public FrenchHolidaysCalendar() {
    this(null);
  }

  public FrenchHolidaysCalendar(String country) {
    super(country);
    this.pentecostNonWorking = IS_PENTECOTE_FERIE;
    this.easterNonWorking = IS_PAQUE_FERIE;
    this.ascensionNonWorking = IS_ASCENSION_FERIE;
  }

  /**
   * @return true if ascenscion is a public holyday (by default, {@link #IS_ASCENSION_FERIE})
   */
  public boolean isAscensionNonWorking() {
    return ascensionNonWorking;
  }

  /**
   * Indicate if ascenscion must be considered as a public holyday
   *
   * @param ascensionNonWorking true if ascenscion is a public holyday
   */
  public void setAscensionNonWorking(boolean ascensionNonWorking) {
    this.ascensionNonWorking = ascensionNonWorking;
  }

  /**
   * @return true if eatser is a public holyday (by default, {@link #IS_PAQUE_FERIE})
   */
  public boolean isEasterNonWorking() {
    return easterNonWorking;
  }

  /**
   * Indicate if eatser must be considered as a public holyday
   *
   * @param easterNonWorking true if ascenscion is a public holyday
   */
  public void setEasterNonWorking(boolean easterNonWorking) {
    this.easterNonWorking = easterNonWorking;
  }

  /**
   * @return true if pentecost is a public holyday (by default, {@link #IS_PENTECOTE_FERIE})
   */
  public boolean isPentecostNonWorking() {
    return pentecostNonWorking;
  }

  /**
   * Indicate if pentecost must be considered as a public holyday
   *
   * @param pentecostNonWorking true if pentecost is a public holyday
   */
  public void setPentecostNonWorking(boolean pentecostNonWorking) {
    this.pentecostNonWorking = pentecostNonWorking;
  }

  /**
   * @param jj day in month of the date to test
   * @param mm month of the date to test
   * @return true if it's the 01/01, 01/05, 08/05, 14/07, 15/08, 01/11, 11/11 or 25/12
   */
  protected double checkFixedHoliday(int jj, int mm) {
    if ((mm == 1) && (jj == 1)) return 0;
    if ((mm == 5) && ((jj == 1) || (jj == 8))) return 0;
    if ((mm == 7) && (jj == 14)) return 0;
    if ((mm == 8) && (jj == 15)) return 0;
    if ((mm == 11) && ((jj == 1) || (jj == 11))) return 0;
    if ((mm == 12) && (jj == 25)) return 0;
    return 1;
  }

  /**
   * Check if the date corresponds to easter, ascension or pentecost.
   * <br>If the date is in March or April, call <code>checkEaster</code>
   * <br>If the date is in May or June, call <code>checkAscension</code> and <code>checkPentecost</code>
   *
   * @param jj day in month of the date to test
   * @param mm month of the date to test
   * @param aa year of the date to test
   * @return true if it's eatser, ascension or whitsun
   */
  protected double checkMovingHoliday(int jj, int mm, int aa) {
    if ((mm >= 3) && (mm <= 6)) {
      //en mars,avril, mai et juin on vérifie que la date ne correspond pas à pâque, l'ascencion ou la pentecote

      // one calculates the date for Easter Monday then Ascension (38 days later)
      // then Whitsun (11 days later)
      Date easterMonday = getEasterMonday(aa);
      if (mm < 5) {
        //paque ne tombe qu'en mars ou avril...
        if (checkEaster(easterMonday, jj, mm)) return 0;
      }
      if (mm > 4) {
        if (checkAscension(easterMonday, jj, mm)) return 0;
        if (checkPentecost(easterMonday, jj, mm)) return 0;
      }
    }
    return 1;
  }

  /**
   * @param easterMonday easter monday for the year
   * @param jj           day in month of the date to test
   * @param mm           month of the date to test
   * @return true if the day corresponds to the easter monday
   */
  protected boolean checkEaster(Date easterMonday, int jj, int mm) {
    return ((isEasterNonWorking()) && (jj == DateUtils.getDay(easterMonday)) && ((mm) == (DateUtils.getMonth(easterMonday))));
  }

  /**
   * @param easterMonday easter monday for the year
   * @param jj           day in month of the date to test
   * @param mm           month of the date to test
   * @return true if the day corresponds to the ascension
   */
  protected boolean checkAscension(Date easterMonday, int jj, int mm) {
    if (isAscensionNonWorking()) {
      Date ascension = calculateAscension(easterMonday);
      return ((jj == DateUtils.getDay(ascension)) && ((mm) == (DateUtils.getMonth(ascension))));
    } else {
      return false;
    }
  }

  /**
   * @param easterMonday easter monday for the year
   * @param jj           day in month of the date to test
   * @param mm           month of the date to test
   * @return true if the day corresponds to the pentecost
   */
  protected boolean checkPentecost(Date easterMonday, int jj, int mm) {
    if (isPentecostNonWorking()) {
      Date pentecote = calculatePentecost(easterMonday);
      return ((jj == DateUtils.getDay(pentecote)) && ((mm) == (DateUtils.getMonth(pentecote))));
    } else {
      return false;
    }
  }

}
