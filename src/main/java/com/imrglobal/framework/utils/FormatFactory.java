package com.imrglobal.framework.utils;

import com.imrglobal.framework.nls.Language;
import com.imrglobal.framework.nls.LocaleResourceKey;

import java.text.*;
import java.util.Locale;

/**
 *
 *
 */
public class FormatFactory {

  private static Locale DEFAULT_LOCALE = Locale.getDefault();
  /**
   * @deprecated use {@link #getDefaultDateFormat()}
   */
  public static SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat();
  private static ThreadLocalMap<LocaleResourceKey, DateFormat> dateFormatCache = new ThreadLocalMap<LocaleResourceKey, DateFormat>("__dateFormatCache__");


  private static ThreadLocalMap<Object, NumberFormat> numberFormatLgCache = new ThreadLocalMap<Object, NumberFormat>("__numberFormatLgCache__");
  private static ThreadLocalMap<Object, NumberFormat> integerFormatLgCache = new ThreadLocalMap<Object, NumberFormat>("__integerFormatLgCache__");


  public static DateFormat getDateFormat(String pattern) {
    return getDateFormat(pattern, DEFAULT_LOCALE);
  }

  public static DateFormat getDateFormat(String pattern, Locale locale) {
    DateFormat format = null;
    if (locale == null) {
      locale = DEFAULT_LOCALE;
    }
    LocaleResourceKey cacheKey = new LocaleResourceKey(locale, pattern != null ? pattern : "");
    format = (SimpleDateFormat) dateFormatCache.get(cacheKey);
    if (format == null) {
      format = initDateFormat(pattern, locale, cacheKey);
    }
    return format;
  }

  private static DateFormat initDateFormat(String pattern, Locale locale, LocaleResourceKey cacheKey) {
    DateFormat format;
    if ((pattern == null) && (DEFAULT_LOCALE.equals(locale))) {
      format = getDefaultDateFormat();
    } else {
      format = new SimpleDateFormat(pattern, locale);
    }
    dateFormatCache.put(cacheKey, format);
    return format;
  }

  protected static DateFormat getDefaultDateFormat() {
    DateFormat dateFmt = dateFormatCache.getDefaultValue();
    if (dateFmt == null) {
      dateFmt = new SimpleDateFormat();
      dateFormatCache.setDefaultValue(dateFmt);
    }
    return dateFmt;
  }

  /**
   * Returns the default number format used.
   * <br>This format is used to display a string representation of a number.
   * <BR>The default format is define by the parameter DEFAULT_NUMBER_FORMAT
   * in the framework.properties file.
   * <BR>The structure of the format is like #,##0.00
   *
   * @return the number format
   * @see java.text.DecimalFormat for more information
   */
  public static NumberFormat getDefaultNumberFormat() {
    NumberFormat defaultNumberFormat = numberFormatLgCache.getDefaultValue();
    if (defaultNumberFormat == null) {
      String format = getDefaultNumberFormatPattern();
      if (format != null) {
        try {
          defaultNumberFormat = new DecimalFormat(format);
        } catch (Throwable ex) {
          //On n'utilise pas Log.error car ce composant peut être utilisé
          //avant l'initialisation du composant Log (dans Parameters).
          System.err.println("[ERROR] Invalid number format [" + format + "] " + ex.getMessage());
          defaultNumberFormat = NumberFormat.getNumberInstance();
        }
      } else {
        defaultNumberFormat = NumberFormat.getNumberInstance();
      }
      numberFormatLgCache.setDefaultValue(defaultNumberFormat);
    }
    return (defaultNumberFormat);
  }

  private static String getDefaultNumberFormatPattern() {
    return  null;
  }

  /**
   * Returns the number format used for the given language.
   * <br>This format is used to display a string representation of a number.
   * <BR>The format is define by the parameter NUMBER_FORMAT_<language int value>
   * in the framework.properties file.
   * <BR>The structure of the format is like #,##0.00
   *
   * @param l language
   *          (@see java.text.DecimalFormat for more information).
   * @return the number format for the language
   */
  public static NumberFormat getNumberFormat(Language l) {
    NumberFormat format = (NumberFormat) numberFormatLgCache.get(l);
    if (format == null) {
      String sFormat = getNumberFormatPattern(l);
      if (sFormat != null) {
        try {
          format = new DecimalFormat(sFormat);
          DecimalFormatSymbols symbols = new DecimalFormatSymbols(l.toLocale());
          ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        } catch (Throwable ex) {
          //On n'utilise pas Log.error car ce composant peut être utilisé
          //avant l'initialisation du composant Log (dans Parameters).
          System.err.println("[ERROR] Invalid number format [" + format + "] for language [" + l + "] " + ex.getMessage());
          format = NumberFormat.getNumberInstance(l.toLocale());
        }
      } else {
        format = NumberFormat.getNumberInstance(l.toLocale());
      }
      if (format != null) {
        numberFormatLgCache.put(l, format);
      }
    }
    return (format);
  }

  private static String getNumberFormatPattern(Language l) {
    return null;
  }

  /**
   * Returns the default integer format used.
   * <br>This format is used to display a string representation of an integer.
   * <BR>The default format is define by the parameter DEFAULT_NUMBER_FORMAT
   * in the framework.properties file.
   * <BR>The structure of the format is like #,##0.00
   * <br>Only the value before the '.' is used for integer.
   *
   * @return the defulat number format for integer
   * @see java.text.DecimalFormat for more information
   */
  public static NumberFormat getDefaultIntegerFormat() {
    NumberFormat defaultIntegerFormat = integerFormatLgCache.getDefaultValue();
    if (defaultIntegerFormat == null) {
      String format = getDefaultIntegerFormatPattern();
      if (format != null) {
        try {
          defaultIntegerFormat = new DecimalFormat(format);
        } catch (Throwable ex) {
          //On n'utilise pas Log.error car ce composant peut être utilisé
          //avant l'initialisation du composant Log (dans Parameters).
          System.err.println("[ERROR] Invalid number format [" + format + "] " + ex.getMessage());
          defaultIntegerFormat = NumberFormat.getNumberInstance();
        }
      } else {
        defaultIntegerFormat = NumberFormat.getNumberInstance();
      }
      integerFormatLgCache.setDefaultValue(defaultIntegerFormat);
    }
    return (defaultIntegerFormat);
  }

  private static String getDefaultIntegerFormatPattern() {
    String pattern = getDefaultNumberFormatPattern();
    if (pattern != null) {
      int i = pattern.indexOf('.');
      if (i > 0) {
        pattern = pattern.substring(0, i);
      }
    }
    return pattern;
  }

  /**
   * Returns the number format used for the given language.
   * <br>This format is used to display a string representation of an integer.
   * <BR>The format is define by the parameter NUMBER_FORMAT_<language int value>
   * in the framework.properties file.
   * <BR>The structure of the format is like #,##0.00
   * <br>Only the value before the '.' is used for integer.
   *
   * @param l language
   * @return the format for integer in the given language
   * @see java.text.DecimalFormat for more information
   */
  public static NumberFormat getIntegerFormat(Language l) {
    NumberFormat format = (NumberFormat) integerFormatLgCache.get(l);
    if (format == null) {
      String sFormat = getIntegerFormatPattern(l);
      if (sFormat != null) {
        try {
          format = new DecimalFormat(sFormat);
          DecimalFormatSymbols symbols = new DecimalFormatSymbols(l.toLocale());
          ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        } catch (Throwable ex) {
          //On n'utilise pas Log.error car ce composant peut être utilisé
          //avant l'initialisation du composant Log (dans Parameters).
          System.err.println("[ERROR] Invalid number format [" + format + "] for language [" + l + "] " + ex.getMessage());
          format = NumberFormat.getNumberInstance(l.toLocale());
        }
      } else {
        format = NumberFormat.getNumberInstance(l.toLocale());
      }
      if (format != null) {
        integerFormatLgCache.put(l, format);
      }
    }
    return (format);
  }

  private static String getIntegerFormatPattern(Language l) {
    String pattern = getNumberFormatPattern(l);
    if (pattern != null) {
      int i = pattern.indexOf('.');
      if (i > 0) {
        pattern = pattern.substring(0, i);
      }
    }
    return pattern;
  }

}
