/*
 * Created by thibaut_c
 * Date: 24 oct. 02
 * Time: 16:36:18
 */
package com.imrglobal.framework.utils;

import com.imrglobal.framework.nls.Language;
import com.imrglobal.framework.type.EnumType;

import java.util.Comparator;

/**
 * Comparator used to compare enum type value. This comparator offers several modes to
 * compare two enum type values. This different modes ared described by constants of this class :
 * <ul>
 * <li>ALPHABETIC_ASC ascending comparison of the the description associated to each type value for the
 * language associated with the comparator (if a language has been associated).
 * <li>ALPHABETIC_DESC descending comparison of the the description associated to each type value for the
 * language associated with the comparator (if a language has been associated).
 * <li>VALUE_ASC ascending comparison of the the internal value of each type value.
 * <li>VALUE_DESC descending comparison of the the internal value of each type value.
 * </ul>
 */
public class EnumComparator<E extends EnumType> extends AbstractComparator<E> {

  public static final int ALPHABETIC_ASC = 0;
  public static final int ALPHABETIC_DESC = 1;
  public static final int VALUE_ASC = 2;
  public static final int VALUE_DESC = 3;

  public static final String STR_ALPHABETIC_ASC = "ASC";
  public static final String STR_ALPHABETIC_DESC = "DESC";
  public static final String STR_VALUE_ASC = "VALUE_ASC";
  public static final String STR_VALUE_DESC = "VALUE_DESC";


  public static final String VALID_MODE = new StringBuffer("[").append(STR_ALPHABETIC_ASC).append("][")
      .append(STR_ALPHABETIC_DESC).append("][")
      .append(STR_VALUE_ASC).append("][")
      .append(STR_VALUE_DESC).append("]").toString();

  private Language language;
  private int mode;

  public EnumComparator(int mode) {
    this(mode, null);
  }

  public EnumComparator(int mode, Language language) {
    this.language = language;
    this.mode = mode;
  }

  public EnumComparator(Comparator<? super E> parent, int mode, Language language) {
    super(parent);
    this.language = language;
    this.mode = mode;
  }

  /**
   * Returns the language used by this comparator
   *
   * @return the language
   */
  public Language getLanguage() {
    return language;
  }

  /**
   * Returns the mode used to sort enum type values
   *
   * @return the sort mode
   */
  public int getMode() {
    return mode;
  }

  @Override
  protected int doCompare(E enum1, E enum2) {
    switch (this.mode) {
      case ALPHABETIC_ASC:
        return sortAlphabeticAsc(enum1, enum2);
      case ALPHABETIC_DESC:
        return sortAlphabeticDesc(enum1, enum2);
      case VALUE_DESC:
        return sortValueDesc(enum1, enum2);
      default:
        return sortValueAsc(enum1, enum2);
    }
  }

  private int sortAlphabeticAsc(EnumType enum1, EnumType enum2) {
    return compareEnumLabel(getStringToCompare(enum1), getStringToCompare(enum2));
  }

  /**
   * Compare the label of two enum instance.
   * <br>This triger is called, when the sort mode is {@link #ALPHABETIC_ASC} or {@link #ALPHABETIC_DESC}
   *
   * @param lbEnum1 label of first enum
   * @param lbEnum2 label of second enum
   * @return a negative integer, zero, or a positive integer as the first label is less than, equal to, or greater
   *         than the second.
   */
  protected int compareEnumLabel(String lbEnum1, String lbEnum2) {
    return StringUtils.compare(lbEnum1, lbEnum2);
  }

  /**
   * Returns the string used to compare the forThisEnum type value in an alphabetic
   * sort.
   *
   * @param forThisEnum
   * @return string used to compare the forThisEnum
   */
  protected String getStringToCompare(EnumType forThisEnum) {
    String res = null;
    if (this.language != null) {
      res = forThisEnum.toString(this.language);
    } else {
      res = forThisEnum.toString();
    }
    return res;
  }

  private int sortAlphabeticDesc(EnumType enum1, EnumType enum2) {
    return -1 * sortAlphabeticAsc(enum1, enum2);
  }

  private int sortValueAsc(EnumType enum1, EnumType enum2) {
    int i1 = enum1.getValue();
    int i2 = enum2.getValue();
    return compareInternalValue(i1, i2);
  }

  /**
   * Compare the internal value of two enum instance.
   * <br>This triger is called, when the sort mode is {@link #VALUE_ASC} or {@link #VALUE_DESC}
   *
   * @param vEnum1 internal value of first enum
   * @param vEnum2 internal value of second enum
   * @return a negative integer, zero, or a positive integer as the first value is less than, equal to, or greater
   *         than the second.
   */
  protected int compareInternalValue(int vEnum1, int vEnum2) {
    int res = 0;
    if (vEnum1 == vEnum2) {
      res = 0;
    } else if (vEnum1 < vEnum2) {
      res = -1;
    } else {
      res = 1;
    }
    return res;
  }

  private int sortValueDesc(EnumType enum1, EnumType enum2) {
    return -1 * sortValueAsc(enum1, enum2);
  }

  /**
   * Return the sort mode corresponding to the given string.
   * <br>The possible string value are :
   * <ul>
   * <li>ASC  for an ascending alphabetic sort
   * <li>DESC  for a descending alphabetic sort
   * <li>VALUE_ASC  for an ascending value sort
   * <li>VALUE_DESC  for a descending value sort
   * </ul>
   */
  public static int getSortMode(String sMode) {
    int mode = Integer.MIN_VALUE;
    if (sMode != null) {
      if (STR_ALPHABETIC_ASC.equalsIgnoreCase(sMode)) {
        mode = ALPHABETIC_ASC;
      } else if (STR_ALPHABETIC_DESC.equalsIgnoreCase(sMode)) {
        mode = ALPHABETIC_DESC;
      } else if (STR_VALUE_ASC.equalsIgnoreCase(sMode)) {
        mode = VALUE_ASC;
      } else if (STR_VALUE_DESC.equalsIgnoreCase(sMode)) {
        mode = VALUE_DESC;
      } else {
        mode = Integer.MIN_VALUE;
      }
    }
    return mode;
  }

  /**
   * Check if the given string represents a possible sort mode.
   */
  public static boolean isValidMode(String mode) {
    String s = new StringBuffer("[").append(mode.toUpperCase()).append("]").toString();
    return (VALID_MODE.indexOf(s) >= 0);
  }
}
