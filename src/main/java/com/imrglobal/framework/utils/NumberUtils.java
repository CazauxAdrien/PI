package com.imrglobal.framework.utils;

import com.imrglobal.framework.nls.Language;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class defines some API to manipulate numbers.
 */
public class NumberUtils {

  /**
   * Round the given double with two decimals. <BR>
   * Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up.
   * <li>1.113 => 1.11</li>
   * <li>1.116 => 1.12</li>
   * <li>1.115 => 1.12</li>
   * 
   * @param value double to round
   * @return round value or value if value is not a number or is infinite
   */
  public static double round(double value) {
    return (round(value, 2));
  }

  /**
   * Round the given double with the given number of decimals. <BR>
   * Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up.
   * 
   * @param value value to round
   * @param nbDecimal number of decimal used to round
   * @return round value or value if value is not a number or is infinite
   */
  public static double round(double value, int nbDecimal) {
    if (!Double.isNaN(value) && !Double.isInfinite(value)) {
      // Java Bug Id 4508009 : Using the string constructor instead of the double constructor avoids the
      // numerical surprise occuring here.
      BigDecimal bd = new BigDecimal(Double.toString(value));
      bd = bd.setScale(nbDecimal, BigDecimal.ROUND_HALF_UP);
      return (bd.doubleValue());
    }
    return value;
  }

  /**
   * Returns a double value whose value is (val1 + val2). <br>
   * This method uses BigDecimal to avoid problem with floating and double precision arithmetic
   * 
   * @param val1 value to add
   * @param val2 value to add
   * @return val1 + val2
   */
  public static double add(double val1, double val2) {
    BigDecimal bd1 = new BigDecimal(Double.toString(val1));
    BigDecimal bd2 = new BigDecimal(Double.toString(val2));
    BigDecimal bdRes = bd1.add(bd2);
    return (bdRes.doubleValue());
  }

  /**
   * Returns a double value whose value is (val1 - val2). <br>
   * Thi method uses BigDecimal to avoid problem with floating and double precision arithmetic
   * 
   * @param val1 value
   * @param val2 value to substract
   * @return val1 - val2
   */
  public static double subtract(double val1, double val2) {
    BigDecimal bd1 = new BigDecimal(Double.toString(val1));
    BigDecimal bd2 = new BigDecimal(Double.toString(val2));
    BigDecimal bdRes = bd1.subtract(bd2);
    return (bdRes.doubleValue());
  }

  /**
   * Returns a double value whose value is (val1 * val2). <br>
   * This metho uses BigDecimal to avoid problem with floating and double precision arithmetic
   * 
   * @param val1 value to multiply
   * @param val2 value to multiply
   * @return val1 * val2
   */
  public static double multiply(double val1, double val2) {
    BigDecimal bd1 = new BigDecimal(Double.toString(val1));
    BigDecimal bd2 = new BigDecimal(Double.toString(val2));
    BigDecimal bdRes = bd1.multiply(bd2);
    return (bdRes.doubleValue());
  }

  /**
   * Returns a double value whose value is (val1 / val2), and whose scale is as specified. If rounding must be performed to generate a result with the specified scale, the ROUND_HALF_UP rounding mode
   * is applied.
   * 
   * @param val1 the value to divide
   * @param val2 value by which val1 is to be divided
   * @param scale rounding mode to apply
   * @return val1 / val2
   */
  public static double divide(double val1, double val2, int scale) {
    BigDecimal bd1 = new BigDecimal(Double.toString(val1));
    BigDecimal bd2 = new BigDecimal(Double.toString(val2));
    BigDecimal bdRes = bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP);
    return (bdRes.doubleValue());
  }

  /**
   * Returns a new int initialized to the value represented by the specified String. <BR>
   * This method suppress the blank in the string before parsing.
   * 
   * @param s a string representation of the short
   * @return a new short
   */
  public static short parseShort(String s) {
    try {
      return (Short.parseShort(s));
    } catch (NumberFormatException ex) {
      s = suppressBlank(s);
      return (Short.parseShort(s));
    }
  }

  /**
   * Returns a new int initialized to the value represented by the specified String. <BR>
   * This method suppress the blank in the string before parsing.
   * 
   * @param s a string representation of the integer
   * @return a new int
   */
  public static int parseInt(String s) {
    try {
      return (Integer.parseInt(s));
    } catch (NumberFormatException ex) {
      s = suppressBlank(s);
      return (Integer.parseInt(s));
    }
  }

  /**
   * Returns a new long initialized to the value represented by the specified String. <BR>
   * This method suppress the blank in the string before parsing.
   * 
   * @param s a string representation of the number
   * @return a new long
   */
  public static long parseLong(String s) {
    try {
      return (Long.parseLong(s));
    } catch (NumberFormatException ex) {
      s = suppressBlank(s);
      return (Long.parseLong(s));
    }
  }

  /**
   * Returns a new float initialized to the value represented by the specified String.
   * 
   * @param s a string representation of the float
   * @return a new float or Float.NaN if the string is null.
   */
  public static float parseFloat(String s) {
    if (s != null) {
      try {
        return (Float.parseFloat(s));
      } catch (NumberFormatException ex) {
        s = formatNumberString(s);
        return (Float.parseFloat(s));
      }
    }
    return (Float.NaN);
  }

  /**
   * Returns a new double initialized to the value represented by the specified String.
   * 
   * @param s a string representation of the number
   * @return a new double or Double.NaN if the string is null.
   */
  public static double parseDouble(String s) {
    if (s != null) {
      try {
        return (Double.parseDouble(s));
      } catch (NumberFormatException ex) {
        s = formatNumberString(s);
        return (Double.parseDouble(s));
      }
    }
    return (Double.NaN);
  }

  /**
   * Returns a new number initialized to the value represented by the specified String.
   * 
   * @param s a string representation of the number
   * @return the number corresponding to the string
   */
  public static Number parseNumber(String s) {
    Number number = null;
    try {
      number = new Integer(parseInt(s));
    } catch (Exception e1) {
      try {
        number = new Long(parseLong(s));
      } catch (Exception e2) {
        try {
          number = new Float(parseFloat(s));
        } catch (Exception e3) {
          number = new Double(parseDouble(s));
        }
      }
    }
    return number;
  }

  private static String formatNumberString(String s) {
    if (s != null) {
      s = suppressBlank(s);
      int posComma = s.indexOf(',');
      int posPoint = s.indexOf('.');
      if (posComma > -1) {
        if (posPoint > -1) {
          // 1,000,000.00 ou 1.000.000,00
          if (posPoint > posComma) {
            // 1,000,000.00
            s = StringUtils.swapSubString(s, ",", "");
          } else {
            // 1.000.000,00
            s = StringUtils.swapSubString(s, ".", "");
            s = StringUtils.swapSubString(s, ",", ".");
          }
        } else {
          int lastPosComma = s.lastIndexOf(',');
          if (lastPosComma == posComma) {
            // 10000,00
            s = StringUtils.swapSubString(s, ",", ".");
          } else {
            // 10,0000,000
            s = StringUtils.swapSubString(s, ",", "");
          }
        }
      } else {
        int lastPosPoint = s.lastIndexOf('.');
        if (lastPosPoint != posPoint) {
          // 10.000.000
          s = StringUtils.swapSubString(s, ".", "");
        }
      }
    }
    return (s);
  }

  /**
   * Returns a String representation of the given double using the default format. <BR>
   * The default format is define by the parameter DEFAULT_NUMBER_FORMAT in the framework.properties file. <BR>
   * The structure of the format is like #,##0.00 (@see java.text.DecimalFormat for more information).
   * 
   * @param d the double to convert
   * @return a string representation of the double
   */
  public static String doubleAsString(double d) {
    return (FormatFactory.getDefaultNumberFormat().format(d));
  }

  /**
   * Returns a String representation of the given double using the given format. <BR>
   * The structure of the format is like #,##0.00 (@see java.text.DecimalFormat for more information).
   * 
   * @param d double to convert in string.
   * @param format format used.
   * @return a string representation of th double
   */
  public static String doubleAsString(double d, String format) {
    NumberFormat fmt = new DecimalFormat(format);
    return (fmt.format(d));
  }

  /**
   * Returns a String representation of the given double using the given language.
   * 
   * @param d double to convert in string
   * @param l the language
   * @return a string representation of the double
   */
  public static String doubleAsString(double d, Language l) {
    return (FormatFactory.getNumberFormat(l).format(d));
  }

  /**
   * Returns a String representation of the given integer using the default format. <BR>
   * The default format is define by the parameter DEFAULT_NUMBER_FORMAT in the framework.properties file. <BR>
   * The structure of the format is like #,##0.00 (@see java.text.DecimalFormat for more information).
   * 
   * @param d the integer
   * @return a string representation of the integer
   */
  public static String integerAsString(long d) {
    return (FormatFactory.getDefaultIntegerFormat().format(d));
  }

  /**
   * Returns a String representation of the given integer using the given format. <BR>
   * The structure of the format is like #,##0.00 (@see java.text.DecimalFormat for more information).
   * 
   * @param d integer to convert in string.
   * @param format format used.
   * @return the string representation of the integer
   */
  public static String integerAsString(long d, String format) {
    NumberFormat fmt = new DecimalFormat(format);
    return (fmt.format(d));
  }

  /**
   * Returns a String representation of the given integer using the given language.
   * 
   * @param d integer to convert in string.
   * @param l the language
   * @return the string representation of the integer
   */
  public static String integerAsString(long d, Language l) {
    return (FormatFactory.getIntegerFormat(l).format(d));
  }

  private static String suppressBlank(String theString) {
    StringBuilder str = new StringBuilder();
    char c;
    int i = 0;

    while (i < theString.length()) {
      c = theString.charAt(i++);
      if (!Character.isSpaceChar(c) && !Character.isWhitespace(c)) {
        str.append(c);
      }
    }
    return str.toString();
  }

  /**
   * Returns a String representation of the given number using the default format. <BR>
   * The default format is define by the parameter DEFAULT_NUMBER_FORMAT in the framework.properties file. <BR>
   * The structure of the format is like #,##0.00 (@see java.text.DecimalFormat for more information).
   * 
   * @param nb the number
   * @return a string representation of the number
   */
  public static String numberAsString(Number nb) {
    if (isInteger(nb)) {
      return (integerAsString(nb.longValue()));
    }
    return (doubleAsString(nb.doubleValue()));
  }

  /**
   * Return true if the number represents an integer
   * 
   * @param nb the number
   * @return true if nb instance of Integer, Short, Long, BigInteger, AtomicInteger or AtomicLong
   */
  public static boolean isInteger(Number nb) {
    return (nb instanceof Integer) || (nb instanceof Short) || (nb instanceof Long) || (nb instanceof BigInteger) || (nb instanceof AtomicInteger) || (nb instanceof AtomicLong);
  }

  /**
   * Returns a String representation of the given number using the given format. <BR>
   * The structure of the format is like #,##0.00 (@see java.text.DecimalFormat for more information).
   * 
   * @param nb number to convert in string.
   * @param format format used.
   * @return a string representation of the number
   */
  public static String numberAsString(Number nb, String format) {
    if (isInteger(nb)) {
      return (integerAsString(nb.longValue(), format));
    }
    return (doubleAsString(nb.doubleValue(), format));
  }

  /**
   * Returns a String representation of the given number using the given language.
   * 
   * @param nb number to convert in string.
   * @param l language
   * @return a string representation of the number
   */
  public static String numberAsString(Number nb, Language l) {
    if (isInteger(nb)) {
      return (integerAsString(nb.longValue(), l));
    }
    return (doubleAsString(nb.doubleValue(), l));
  }

  /**
   * Returns true if the specified number is equals to the undefined value. <br>
   * The undefined values are defined by constants of UndefinedAttributes :
   * <ul>
   * <li>UNDEF_SHORT for short
   * <li>UNDEF_INT for int
   * <li>UNDEF_LONG for long
   * <li>UNDEF_FLOAT for float
   * <li>UNDEF_DOUBLE for double
   * </ul>
   * 
   * @param nb the number
   * @return true if the number corresponds to an undefined value
   */
  public static boolean isUndefined(Number nb) {
    boolean undef = false;
    if (nb == null || Double.isNaN(nb.doubleValue())) {
      undef = true;
    } else if (nb instanceof Short) {
      undef = nb.shortValue() == UndefinedAttributes.UNDEF_SHORT;
    } else if (nb instanceof Integer) {
      undef = nb.intValue() == UndefinedAttributes.UNDEF_INT;
    } else if (nb instanceof Long) {
      undef = nb.longValue() == UndefinedAttributes.UNDEF_LONG;
    } else if (nb instanceof Float) {
      undef = nb.floatValue() == UndefinedAttributes.UNDEF_FLOAT;
    } else if (nb instanceof Double) {
      undef = nb.doubleValue() == UndefinedAttributes.UNDEF_DOUBLE;
    }
    return undef;
  }

}