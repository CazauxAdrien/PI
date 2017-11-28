package com.imrglobal.framework.utils;

import java.io.UnsupportedEncodingException;
import java.text.*;
import java.util.ArrayList;

/**
 * Utility. This class contains a collection of methods to format
 * and handle Strings
 */

public class StringUtils {

  /**
   * Character(s) use by the system to separate lines.
   */
  public static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Format action to trim a String.
   */
  public static final int FMT_TRIM = 1;
  /**
   * Format action to uppercase a String.
   */
  public static final int FMT_UPPERCASE = 2;
  /**
   * Format action to lowercase a String.
   */
  public static final int FMT_LOWERCASE = 4;
  /**
   * Format action to uppercase the first character of a String.
   */
  public static final int FMT_UPCASE_FIRST_CHAR = 8;
  /**
   * Format action to lowercase the first character of a String.
   */
  public static final int FMT_LOWERCASE_FIRST_CHAR = 16;


  /**
   * Calculates how many times a char is present in the String.
   *
   * @param string to analyse
   * @param car    char to find in the String
   * @return int, occurence of the char
   */
  public static int charCount(String string, char car) {
    int res = 0;
    if (string != null) {
      for (int i = 0; i < string.length(); i++) {
        char c = string.charAt(i);
        if (c == car) {
          res++;
        }
      }
    }
    return res;

  }

  /**
   * Calculates how many times a substring is present in the String.
   *
   * @param string    to analyse
   * @param subString find in the String
   * @return number of occurence of the substring
   */
  public static int stringCount(String string, String subString) {
    String str = string;
    int nb = 0;
    int lSep = subString.length();
    int index = str.indexOf(subString);
    while (index >= 0) {
      nb++;
      str = str.substring(index + lSep);
      index = str.indexOf(subString);
    }
    return nb;
  }

  /**
   * Returns the left hand side of the nth char separator.
   * <br>Example :
   * <ul>
   * <li>copyLeft("Monsieur,Madame,Mademoiselle",",",0) returns ""</li>
   * <li>copyLeft("Monsieur,Madame,Mademoiselle",",",1) returns Monsieur</li>
   * <li>copyLeft("Monsieur,Madame,Mademoiselle",",",2) returns Monsieur,Madame</li>
   * <li>copyLeft("Monsieur,Madame,Mademoiselle",",",3) returns Monsieur,Madame,Mademoiselle</li>
   * </ul>
   *
   * @param string   to analyse
   * @param sep      separator
   * @param position number of the separator
   * @return result of the analysis
   */
  public static String copyLeft(String string, String sep, int position) {

    if (position > 0) {
      int i = 0;
      int index;
      int lSep = sep.length();
      int off=0;
      while (i < position) {
        index = string.indexOf(sep, off);
        if (index >= 0) {
          off=index+lSep;
          i++;
        } else {
          off = 0;
          break;
        }
      }
      return off>0 ? string.substring(0,off-sep.length()) : string;
    }
    return "";
  }

  /**
   * Returns the right hand side of the nth char separator.
   * <br>Example :
   * <ul>
   * <li>copyRight("Monsieur,Madame,Mademoiselle",",",0) returns Monsieur,Madame,Mademoiselle</li>
   * <li>copyRight("Monsieur,Madame,Mademoiselle",",",1) returns Madame,Mademoiselle</li>
   * <li>copyRight("Monsieur,Madame,Mademoiselle",",",2) returns Mademoiselle</li>
   * <li>copyRight("Monsieur,Madame,Mademoiselle",",",3) returns ""</li>
   * </ul>
   *
   * @param string   to analyse
   * @param sep      separator
   * @param position number of the separator
   * @return result of the analysis
   */

  public static String copyRight(String string, String sep, int position) {
    int i = 0;
    int index;
    int lSep = sep.length();
    if (position >= 0) {
      int off=0;
      while (i < position) {
        index = string.indexOf(sep, off);
        if (index >= 0) {
          off=index+lSep;
          i++;
        } else {
          off=string.length();
          break;
        }
      }
      return off > 0 ? string.substring(off) : string;
    }
    return "";
  }

  /**
   * Returns the substring contained between 2 consecutive separators.
   * <br>Example :	<ul>
   * <li>extract("Monsieur,Madame,Mademoiselle",",",0) returns ""</li>
   * <li>extract("Monsieur,Madame,Mademoiselle",",",1) returns Monsieur</li>
   * <li>extract("Monsieur,Madame,Mademoiselle",",",2) returns Madame</li>
   * <li>extract("Monsieur,Madame,Mademoiselle",",",3) returns Mademoiselle</li>
   * <li>extract("Monsieur,Madame,Mademoiselle",",",4) returns un chaîne vide</li>
   * </ul>
   *
   * @param string   to analyse
   * @param sep      separator
   * @param position positin of the first separator
   * @return result of the analysis
   */
  public static String extract(String string, String sep, int position) {
    String str;

    if (position > 0) {
      str = copyRight(string, sep, position - 1);
      str = copyLeft(str, sep, 1);
    } else {
      str = "";
    }

    return str;
  }

  /**
   * Replace a substring with another substring in a main string.
   * <br>Example :
   * <ul>
   * <li>swapSubString("Monsieur et Madame et Mademoiselle"," et ",",") returns Monsieur,Madame,Mademoiselle</li>
   * </ul>
   *
   * @param string    Main string
   * @param oldSubStr substring to be replaced
   * @param newSubStr sustring replacing the first substring
   * @return string, result of the process
   */
  public static String swapSubString(String string, String oldSubStr, String newSubStr) {
    String str = string;

    if (stringCount(str, oldSubStr) > 0) {
      StringBuilder buffer = new StringBuilder();
      buffer.append(copyLeft(str, oldSubStr, 1));
      buffer.append(newSubStr);
      buffer.append(swapSubString(copyRight(str, oldSubStr, 1), oldSubStr, newSubStr));
      str = buffer.toString();
    }

    return str;
  }

  /**
   * Removes the blank spaces at the beginning of a string.
   *
   * @param str String from which the spaces must be removed
   * @return the string whithout blanks at the begining
   */
  public static String lSkip(String str) {
    if ((str == null) || (str.isEmpty())) {
      return str;
    } else {
      char c;
      int i = 0;
      c = str.charAt(i);
      while ((i < str.length()) && ((c == ' ') || (c == '\t'))) {
        if (++i < str.length()) {
          c = str.charAt(i);
        }
      }

      return (i < str.length() ? str.substring(i) : "");
    }
  }

  /**
   * Removes the blank spaces at the enf of a string.
   *
   * @param str String from which the spaces must be removed
   * @return the string without the blank at the end
   */
  public static String rSkip(String str) {
    if ((str == null) || (str.isEmpty())) {
      return str;
    } else {
      char c;
      int i;
      i = str.length() - 1;
      c = str.charAt(i);
      while ((i >= 0) && ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r'))) {
        if (--i >= 0) {
          c = str.charAt(i);
        }
      }

      return (i >= 0 ? str.substring(0, i + 1) : "");
    }
  }

  /**
   * Removes the blank spaces at the beginning and at the end of a string.
   *
   * @param string String from which the spaces must be removed
   * @return te string without the blanks at beginning and at the end
   */
  public static String skip(String string) {
    if ((string == null) || (string.isEmpty())) {
      return string;
    } else {
      String str = lSkip(string);
      return rSkip(str);
    }
  }

  /**
   * Returns the "short" name of a class <BR>
   * ex : if the original name is com.imrglobal.framework.businessObject.GenericBusinessObject, la méthode
   * it returns GenericBusinessObject.
   *
   * @param forThisClass class whose short name one wants to get
   * @return the short name of the class
   */
  public static String getShortName(Class forThisClass) {
    String className = "";
    if (forThisClass != null) {
      Package packageOfClass = forThisClass.getPackage();
      className = forThisClass.getName();

      if (packageOfClass != null) {
        String packageName = packageOfClass.getName();
        className = className.substring(packageName.length() + 1);
      } else {
        int i = className.indexOf('.');
        while (i > -1) {
          className = className.substring(i + 1);
          i = className.indexOf('.');
        }
      }
    }
    return className;
  }

  /**
   * Returns the "package" name of a class <BR>
   * ex : if the original name is com.imrglobal.framework.businessObject.GenericBusinessObject,
   * it returns com.imrglobal.framework.businessObject
   *
   * @param forThisClass class whose short name one wants to get
   * @return the package name of the class
   */
  public static String getPackageName(Class forThisClass) {
    String className;
    StringBuilder packageName = new StringBuilder();
    if (forThisClass != null) {
      Package packageOfClass = forThisClass.getPackage();
      className = forThisClass.getName();

      if (packageOfClass != null) {
        packageName.append(packageOfClass.getName());
      } else {
        int i = className.indexOf('.');
        while (i > -1) {
          if (packageName.length() == 0) {
            packageName.append(className.substring(0, i));
          } else {
            packageName.append('.').append(className.substring(0, i));
          }
          className = className.substring(i + 1);
          i = className.indexOf('.');
        }
      }
    }
    return packageName.toString();
  }

  /**
   * Formats the given string at the given size.
   * <ul>
   * <li> if the given string is shorter than the given size, completes the
   * string with blanks.
   * <li> if the given string is longer than the given size, truncates the
   * string at the given size.
   * </ul>
   *
   * @param str  initial string.
   * @param size desired size for the string.
   * @return the new String at the specified size
   */
  public static String format(String str, int size) {
    return (format(str, size, ' '));
  }

  /**
   * Formats the given string at the given size.
   * <ul>
   * <li> if the given string is shorter than the given size, completes the
   * string with blanks. The blanks are added at the start, at the
   * end or to center the string according to the value of the parameter "mode".
   * <ul>
   * <li> mode = 1 : Blanks are added to center the string.
   * <li> mode = 2 : Blanks are added at the start of the string.
   * <li> otherwise : Blanks are added at the end of the string.
   * </ul>
   * <li> if the given string is longer than the given size, truncates the
   * string at the given size.
   * </ul>
   *
   * @param str  initial string.
   * @param size desired size for the string.
   * @param mode mode used to add the characters
   * @return the new String at the specified size
   */
  public static String format(String str, int size, int mode) {
    return (format(str, size, ' ', mode));
  }

  /**
   * Formats the given string at the given size.
   * <ul>
   * <li> if the given string is shorter than the given size, completes the
   * string with the given character.
   * <li> if the given string is longer than the given size, truncates the
   * string at the given size.
   * </ul>
   *
   * @param str  initial string.
   * @param size desired size for the string.
   * @param c    char used to complete the string
   * @return the new String at the specified size
   */
  public static String format(String str, int size, char c) {
    return (format(str, size, c, 0));
  }

  /**
   * Formats the given string at the given size.
   * <ul>
   * <li> if the given string is shorter than the given size, completes the
   * string with the given character. Characters are added at the start, at the
   * end or to center the string according to the value of the parameter "mode".
   * <ul>
   * <li> mode = 1 : Characters are added to center the string.
   * <li> mode = 2 : Characters are added at the start of the string.
   * <li> otherwise : Characters are added at the end of the string.
   * </ul>
   * <li> if the given string is longer than the given size, truncates the
   * string at the given size.
   * </ul>
   *
   * @param str  initial string.
   * @param size desired size for the string.
   * @param c    character used to complete the string.
   * @param mode mode used to add the characters
   * @return the new String at the specified size
   */
  public static String format(String str, int size, char c, int mode) {
    StringFormatter formatter = new StringFormatter();
    formatter.setJustification(mode);
    formatter.setCharToJustify(c);
    formatter.setCharToJustifyAtRight(c);
    formatter.setMinLength(size);
    formatter.setMaxLength(size);
    return (formatter.format(str));
  }


  /**
   * Replace all the '<' and '>' characters by their HTML equivalent.
   *
   * @param s the string
   * @return the new string compatible with HTML
   */
  public static String supHTMLTag(String s) {
    if (s == null) {
      return null;
    }
    String news;
    news = swapSubString(s, "&", "&amp;");
    news = swapSubString(news, "<", "&lt;");
    news = swapSubString(news, ">", "&gt;");
    return (news);
  }

  /**
   * Replace all the '<' and '>' characters by their HTML equivalent.
   *
   * @param sb the buffer
   * @return a string buffer, HTML compatible
   */
  public static StringBuffer supHTMLTag(StringBuffer sb) {
    if (sb == null) {
      return null;
    }
    return new StringBuffer(supHTMLTag(sb.toString()));
  }

  /**
   * Transforms a string constituted with several parts separated by a separator
   * in an array of String.
   *
   * @param s   the string
   * @param sep the separator
   * @return the array
   */
  public static String[] getStringArray(String s, String sep) {
    if (s != null) {
      int off = 0;
      int next = 0;
      int sepLength = sep.length();
      ArrayList<String> list = new ArrayList<>();
      while ((next = s.indexOf(sep, off)) != -1) {
        list.add(s.substring(off, next));
        off = next + sepLength;
      }
      if (off == 0) {
        return new String[]{s};
      }
      list.add(s.substring(off));
      return list.toArray(new String[list.size()]);
    }
    return null;
  }


  /**
   * Returns a substring of the string, starting at index 0 till index maxlength.
   * If maxLength = -1, no truncation is made
   *
   * @param theString String to truncate
   * @param maxLength maximum length of the String. If -1, no change is made
   * @return the substring
   */
  public static String subString(String theString, int maxLength) {
    if (maxLength == -1 || theString == null) {
      return theString;
    }
    int length = theString.length();
    if (length <= maxLength) {
      return theString;
    }
    return theString.substring(0, maxLength);
  }

  /**
   * Converts the first character in the String to upper case using the rules of the default locale.
   *
   * @param theString the String
   * @return the new string with the first character to uppercase
   */
  public static String upcaseFirstChar(String theString) {
    if ((theString != null) && (!theString.isEmpty())) {
      StringBuilder buffer = new StringBuilder();
      String tmp = theString.substring(0, 1);
      tmp = tmp.toUpperCase();
      buffer.append(tmp);
      buffer.append(theString.substring(1));
      return (buffer.toString());
    } else {
      return theString;
    }
  }

  /**
   * Converts the first character in the String to lower case using the rules of the default locale.
   *
   * @param theString the string
   * @return the String whith the first character in lowercase
   */
  public static String lowercaseFirstChar(String theString) {
    if ((theString != null) && (!theString.isEmpty())) {
      StringBuilder buffer = new StringBuilder();
      String tmp = theString.substring(0, 1);
      tmp = tmp.toLowerCase();
      buffer.append(tmp);
      buffer.append(theString.substring(1));
      return (buffer.toString());
    } else {
      return theString;
    }
  }


  /**
   * Return the last character of the given String.
   *
   * @param theString the string
   * @return the last character
   * @throws IllegalArgumentException if the string is null or empty.
   */
  public static char getLastChar(String theString) {
    if (theString != null) {
      if (!theString.isEmpty()) {
        return (theString.charAt(theString.length() - 1));
      } else {
        throw new IllegalArgumentException("StringUtils.getLastChar empty String!");
      }
    } else {
      throw new IllegalArgumentException("StringUtils.getLastChar null String!");
    }
  }

  /**
   * Replace simple quote ' by double quote ''.
   *
   * @param mess the string
   * @return the new String where simple quote has been replaced by double quote
   */
  public static String handleQuote(String mess) {
    int dummy = -1;
    StringBuilder bf = new StringBuilder();
    int previousChar = dummy;
    int l = mess.length();
    for (int i = 0; i < l; i++) {
      int c = mess.charAt(i);
      int nextChar = ((i < l - 1) ? mess.charAt(i + 1) : -1);
      if (c == '\'') {
        if ((previousChar != c) && (nextChar != c)) {
          bf.append((char) c);
          bf.append((char) c);
          c = -1;
        } else {
          bf.append((char) c);
        }
      } else {
        bf.append((char) c);
      }
      previousChar = c;
    }
    return bf.toString();
  }

  /**
   * Returns the specified message using the given objects.
   *
   * @param message the message
   * @param param   the dynamic values in message
   * @return the message
   * @see java.text.MessageFormat
   */
  public static String messageFormat(String message, Object[] param) {
    return MessageFormat.format(handleQuote(message), param);
  }

  /**
   * Return true if the specified string is null or equals to "".
   *
   * @param s the string
   * @return true if the string is null or empty
   */
  public static boolean isNullOrEmpty(String s) {
    return ((s == null) || (s.isEmpty()));
  }

  /**
   * Returns true if the specified string is null or contains only spaces.
   *
   * @param s the string
   * @return true if the string is null, empty or contents only blanks
   */
  public static boolean isNullOrBlank(String s) {
    return ((s == null) || (s.trim().isEmpty()));
  }

  /**
   * Applies a combination of actions to the specified string and returns the result.
   * The actions to apply are defined by a combination of the following constants.
   * <ul>
   * <li>FMT_TRIM : removes white space from both ends of the string.
   * <li>FMT_UPPERCASE : converts all of the characters in the String to upper case using the rules of the default locale
   * <li>FMT_LOWERCASE : converts all of the characters in the String to lower case using the rules of the default locale
   * <li>FMT_UPCASE_FIRST_CHAR : Converts the first character in the String to upper case using the rules of the default locale
   * <li>FMT_LOWERCASE_FIRST_CHAR : Converts the first character in the String to lower case using the rules of the default locale
   * </ul>
   * <br>example :
   * <code>StringUtils.reformat(" my string ",StringUtils.FMT_TRIM+StringUtils.FMT_UPPERCASE)</code> returns <code>MY STRING</code>
   *
   * @param str    the string to modify
   * @param action a combination of action to apply as a combination of the constants <code>FMT_TRIM, FMT_UPPERCASE, FMT_LOWERCASE, FMT_UPCASE_FIRST_CHAR, FMT_LOWERCASE_FIRST_CHAR</code>
   * @return the result
   */
  public static String reformat(String str, int action) {
    String res = str;
    if (str != null) {
      if ((action & FMT_TRIM) != 0) {
        res = res.trim();
      }
      if ((action & FMT_UPPERCASE) != 0) {
        res = res.toUpperCase();
      }
      if ((action & FMT_LOWERCASE) != 0) {
        res = res.toLowerCase();
      }
      if ((action & FMT_UPCASE_FIRST_CHAR) != 0) {
        res = upcaseFirstChar(res);
      }
      if ((action & FMT_LOWERCASE_FIRST_CHAR) != 0) {
        res = lowercaseFirstChar(res);
      }
    }
    return res;
  }

  /**
   * Convert a string to an encoding to another
   *
   * @param s              the initial string
   * @param encodingSource the encoding or null
   * @param encodingDest   the encoding or null
   * @return the new String
   * @throws UnsupportedEncodingException if a encoding is unsupported
   */
  public static final String convertString(String s, String encodingSource, String encodingDest) throws UnsupportedEncodingException {
    byte[] srcByte;
    if (encodingSource != null) {
      if (!encodingSource.equals(encodingDest)) {
        srcByte = s.getBytes(encodingSource);
      } else {
        return s;
      }
    } else {
      if (encodingDest != null) {
        srcByte = s.getBytes();
      } else {
        return s;
      }
    }
    if (encodingDest != null) {
      return new String(srcByte, encodingDest);
    } else {
      return new String(srcByte);
    }
  }


  /**
   * Converts encoded &#92;uxxxx to unicode chars
   * and changes special saved chars to their original forms
   *
   * @param str the string to convert
   * @return the encoded string
   */
  public static String convertUnicode(String str) {
    char c;
    int len = str.length();
    StringBuilder res = new StringBuilder(len);

    for (int x = 0; x < len;) {
      c = str.charAt(x++);
      if (c == '\\') {
        c = str.charAt(x++);
        if (c == 'u') {
          // Read the xxxx
          int value = 0;
          for (int i = 0; i < 4; i++) {
            c = str.charAt(x++);
            switch (c) {
              case '0':
              case '1':
              case '2':
              case '3':
              case '4':
              case '5':
              case '6':
              case '7':
              case '8':
              case '9':
                value = (value << 4) + c - '0';
                break;
              case 'a':
              case 'b':
              case 'c':
              case 'd':
              case 'e':
              case 'f':
                value = (value << 4) + 10 + c - 'a';
                break;
              case 'A':
              case 'B':
              case 'C':
              case 'D':
              case 'E':
              case 'F':
                value = (value << 4) + 10 + c - 'A';
                break;
              default:
                throw new IllegalArgumentException("Malformed \\uxxxx encoding in " + str);
            }
          }
          res.append((char) value);
        } else {
          if (c == 't') {
            c = '\t';
          } else if (c == 'r') {
            c = '\r';
          } else if (c == 'n') {
            c = '\n';
          } else if (c == 'f') {
            c = '\f';
          }
          res.append(c);
        }
      } else {
        res.append(c);
      }
    }
    return res.toString();
  }

  /**
   * Suppress the part of the string between the specified separator.
   * "xx 'test' zzzz" => "xx  zzzz"
   *
   * @param str the initial string
   * @param sep the separator
   * @return the string without the elements betweeen the separator
   */
  public static String clearBetweenSeparator(String str, String sep) {
    String res = null;
    if (str != null) {
      String[] strings = str.split(sep);
      if (strings.length > 1) {
        StringBuilder bf = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
          String string = strings[i];
          if (i % 2 == 0) {
            bf.append(string);
          }
        }
        res = bf.toString();
      } else {
        res = str;
      }
    }
    return res;
  }


  /**
   * replace accent with no accentutaed character.
   * <ul>
   * <li>é è ê ë => e
   * <li>à â => a
   * <li>î ï => i
   * <li>ô => o
   * <li>ù û ü => u
   * <li>ç => c
   * </ul>
   * <br>This method does not modify the case of the initial string
   *
   * @param theStr the initial string
   * @return the string without accentuted characters
   */
  public static String toDeleteAccents(String theStr) {
    return Normalizer.normalize(theStr, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
  }


  /**
   * Check if the two strings are the same
   *
   * @param str1 first string
   * @param str2 first string
   * @return true if : <ul>
   *         <li>the two strings are null or blank
   *         <li>the two strings are not null or blank and are equals
   *         </ul>
   */
  public static boolean isSameString(String str1, String str2) {
    return (isNullOrBlank(str1) && isNullOrBlank(str2)) || (!isNullOrBlank(str1) && !isNullOrBlank(str2) && str1.equals(str2));
  }

  /**
   * Check if the two strings are the same ignoring case consideration
   *
   * @param str1 first string
   * @param str2 first string
   * @return true if : <ul>
   *         <li>the two strings are null or blank
   *         <li>the two strings are not null or blank and are equals ignoring case consideration
   *         </ul>
   */
  public static boolean isSameStringIgnoreCase(String str1, String str2) {
    return (isNullOrBlank(str1) && isNullOrBlank(str2)) || (!isNullOrBlank(str1) && !isNullOrBlank(str2) && str1.equalsIgnoreCase(str2));
  }

  /**
   * Check if the two strings are the same ignoring case consideration and leading and trailing whitespace
   *
   * @param str1 first string
   * @param str2 first string
   * @return true if : <ul>
   *         <li>the two strings are null or blank
   *         <li>the two strings are not null or blank and are equals ignoring case consideration and leading and trailing whitespace
   *         </ul>
   */
  public static boolean isSameStringIgnoreCaseAndTrim(String str1, String str2) {
    return isSameStringIgnoreCase(str1 != null ? str1.trim() : str1, str2 != null ? str2.trim() : str2);
  }

  /**
   * Check if the two strings are different
   *
   * @param str1 first string
   * @param str2 first string
   * @return true if : <ul>
   *         <li>one of the two strings is null or blank, the other is not
   *         <li>the two strings are not null or blank and are not equals
   *         </ul>
   */
  public static boolean isDifferentString(String str1, String str2) {
    return !isSameString(str1, str2);
  }

  /**
   * Check if the two strings are different ignoring case consideration
   *
   * @param str1 first string
   * @param str2 first string
   * @return true if : <ul>
   *         <li>one of the two strings is null or blank, the other is not
   *         <li>the two strings are not null or blank and are not equals ignoring case consideration
   *         </ul>
   */
  public static boolean isDifferentStringIgnoreCase(String str1, String str2) {
    return !isSameStringIgnoreCase(str1, str2);
  }

  /**
   * Check if the two strings are different ignoring case consideration and leading and trailing whitespace
   *
   * @param str1 first string
   * @param str2 first string
   * @return true if : <ul>
   *         <li>one of the two strings is null or blank, the other is not
   *         <li>the two strings are not null or blank and are not equals ignoring case consideration and leading and trailing whitespace
   *         </ul>
   */
  public static boolean isDifferentStringIgnoreCaseAndTrim(String str1, String str2) {
    return !isSameStringIgnoreCaseAndTrim(str1, str2);
  }

  /**
   * Compares its two string arguments for order. Returns a negative integer, zero, or a positive integer as the first string
   * is less than, equal to, or greater than the second.
   * <br>A null string si considered as less than a not null string.
   *
   * @param str1 first string
   * @param str2 second string
   * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
   */
  public static int compare(String str1, String str2) {
    if (str1 == null) {
      return str2 == null ? 0 : -1;
    } else if (str2 == null) {
      return 1;
    } else {
      return CollatorUtils.getInstance().compare(str1, str2);
    }
  }

  /**
   * Supprime les caractères blacns en début et fin de chaîne, et retourne <code>null</code> si la chaine ainsi modifiée est vide (ou était <code>null</code> au départ).
   *
   * @param str la chaîne à nettoyer (peut être <code>null</code>)
   * @return la chaîne nettoyée.
   */
  public static String trimToNull(String str) {
    String s = null == str ? null : str.trim();
    return s==null || s.isEmpty() ? null : s;
  }

  /**
   * Supprime dans une chaîne le suffixe passé en paramètre. Si la fin de la chaîne de caractères n'était pas exactement le suffixe donné, la chaîne est retournée inchangée.
   *
   * @param str la chaîne dont on souhaite supprimer un motif terminal (peut être <code>null</code>)
   * @param remove le motif à supprimer de la fin de la chaîne
   * @return la chaîne tronquée (si elle se terminait par le motif spécifé), ou sinon la chaîne initiale, inchangée.
   */
  public static String removeEnd(String str, String remove) {
    if (isNullOrEmpty(str) || isNullOrEmpty(remove) || !str.endsWith(remove)) {
      return str;
    }
    return str.substring(0, str.length() - remove.length());
  }
  
}
