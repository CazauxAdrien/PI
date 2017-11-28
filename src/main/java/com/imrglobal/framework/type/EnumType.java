package com.imrglobal.framework.type;

/**
 * Super class of all the enumeration type.
 */

import com.imrglobal.framework.cache.LRUHashMap;
import com.imrglobal.framework.nls.Language;
import com.imrglobal.framework.utils.EnumComparator;
import com.imrglobal.framework.utils.ReflectUtils;
import com.imrglobal.framework.utils.SortVector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class EnumType extends EnumerationType implements ControlProvider {

  private static Map fromIntMethodCache = new LRUHashMap(true);
  /**
   * int value of the enum type.
   */
  private int value;

  /**
   * Returns the EnumType instance for the given int value.
   *
   * @param theValue int value of the EnumType
   * @return instance of the enumeration type.
   */
  public static EnumType from_int(int theValue) {
    throw new RuntimeException("La méthode from_int() doit être surchargée");
  }

  /**
   * Return the possible values of the enumeration type as an array.
   *
   * @deprecated use iterator
   */
  @Deprecated
  public String[] getEnumTypeAsArray() {
    return null;
  }

  /**
   * Returns the int value of this enumeration type.
   *
   * @return int value
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Returns an iterator over all the possible value of the enumeration type.
   */
  public abstract Iterator iterator();

  /**
   * Set the int value for this enumeration type.
   *
   * @param intValue int value.
   */
  public void setValue(int intValue) {
    this.value = intValue;
  }

  /**
   * Returns the int value of this enumeration type.
   *
   * @return int value
   */
  public int to_int() {
    return this.getValue();
  }

  /**
   * Returns a string representation for this enumeration type value.
   *
   * @return a string representation.
   */
  @Override
  public String toString() {
    return "";
  }

  /**
   * Returns a string representation for this enumeration type value for the given language.
   *
   * @return a string representation.
   */
  @Override
  public String toString(Language language) {
    return this.toString();
  }

  /**
   * Get the enum type value of the given enum type corresponding to the given int.
   *
   * @param classType class of the enum type
   * @param theValue int value
   * @return enum type value
   */
  public static EnumType from_int(Class classType, int theValue) {
    try {
      Method meth = getFromIntMethod(classType);

      Object[] args;
      args = new Object[1];
      args[0] = new Integer(theValue);
      EnumType res = (EnumType) meth.invoke(null, args);
      return res;
    } catch (Throwable ex) {
      throw new RuntimeException("Problem to retrieve enum type value for type ["
          + classType + "] and value [" + theValue + "]", ex);
    }
  }

  private static Method getFromIntMethod(Class enumType) {
    Method meth = (Method) fromIntMethodCache.get(enumType);
    if (meth == null) {
      meth = findFromIntMethod(enumType);
    }
    return (meth);
  }

  /**
   * @param enumType
   * @return
   */
  protected static Method findFromIntMethod(Class enumType) {
    Method meth;
    synchronized (enumType) {
      meth = (Method) fromIntMethodCache.get(enumType);
      if (meth == null) {
        meth = ReflectUtils.getMethod(enumType, "from_int");
        fromIntMethodCache.put(enumType, meth);
      }
    }
    return meth;
  }

  /**
   * Two enum type are equals if they have the same class and their internal value are equals.
   *
   * @param obj
   * @return true if the enume type is equals to this enum type
   */
  @Override
  public boolean equals(Object obj) {
    boolean equal = super.equals(obj);
    if ((obj != null) && (!equal)) {
      if (getClass().equals(obj.getClass())) {
        EnumType _enum = (EnumType) obj;
        if (this.value == _enum.value) {
          equal = true;
        }
      }
    }
    return (equal);
  }

  // La méthode est surchargée pour que la déserialization d'un type énuméré retourne
  // seulement une des instances possibles et non pas une nouvelle instances
  protected Object readResolve() {
    return (from_int(getClass(), getValue()));
  }

  /**
   * Sort the list of possible value according to the specified comparator
   *
   * @param comparator comparator used to sort the possible values of this type
   */
  public Iterator sort(Comparator comparator) {
    List res = new ArrayList();
    Iterator it = iterator();
    while (it.hasNext()) {
      res.add(it.next());
    }
    return SortVector.sort(res, comparator);
  }

  /**
   * Sort the list of possible value according to the specified mode and for the given language using an EnumComparator. <BR>
   * The possible mode to sort are defined as constant of com.imrglobal.framework.utils.EnumComparator:
   * <ul>
   * <li>EnumComparator.ALPHABETIC_ASC : ascending alphabetic sort
   * <li>EnumComparator.ALPHABETIC_DESC : descending alphabetic sort
   * <li>EnumComparator.VALUE_ASC : ascending internal value sort
   * <li>EnumComparator.VALUE_DESC : descending internal value sort
   * </ul>
   *
   * @param mode mode used to sort the possible values of this type
   * @param lg language used to sort.
   */
  public Iterator sort(int mode, Language lg) {
    return sort(new EnumComparator(mode, lg));
  }

  /**
   * Returns the list of all the items of the specified enum type.
   *
   * @param forThisType class of the enum type
   * @return the list of all the items of the type
   */
  public static List getAllItems(Class forThisType) {
    List res = null;
    if (EnumType.class.isAssignableFrom(forThisType)) {
      EnumType theEnum = getAnItem(forThisType);
      if (theEnum != null) {
        res = getAllItems(theEnum);
      }
    }
    return res != null ? res : new ArrayList();
  }

  /**
   * Returns the list of all the items of the specified enum type.
   *
   * @param forThisType an item of the enum type
   * @return the list of all the items of the enum type
   */
  public static List getAllItems(EnumType forThisType) {
    List res = new ArrayList();
    Iterator it = forThisType.iterator();
    while (it.hasNext()) {
      res.add(it.next());
    }
    return res;
  }

  /**
   * Returns the list of values of the specified enum type which are compatible with the given enumeration type
   *
   * @param forThisType the enum type
   * @param dependence the dependence enumeration type value
   * @return the possible items
   */
  public static List getPossibleItems(Class forThisType, EnumerationType dependence) {
    List res = null;
    if (EnumType.class.isAssignableFrom(forThisType)) {
      EnumType theEnum = getAnItem(forThisType);
      if (theEnum != null) {
        res = getPossibleItems(theEnum, dependence);
      }
    }
    return res != null ? res : new ArrayList();
  }

  /**
   * Returns the list of values of the specified enum type which are compatible with the given enumeration type
   *
   * @param forThisType the enum type
   * @param dependence the dependence enumeration type value
   * @return the possibles items
   */
  public static List getPossibleItems(EnumType forThisType, EnumerationType dependence) {
    List res = new ArrayList();
    Iterator it = forThisType.iterator();
    while (it.hasNext()) {
      EnumType enumValue = (EnumType) it.next();
      if ((dependence == null) || (enumValue.isPossibleValue(dependence))) {
        res.add(enumValue);
      }
    }
    return res;
  }

  /**
   * Return an item of the specified enum type searching a constant on the class
   *
   * @param forThisType the enum type
   * @return an item's enum or null if no constant founded
   */
  public static EnumType getAnItem(Class forThisType) {
    EnumType anItem = null;
    if (EnumType.class.isAssignableFrom(forThisType)) {
      final List list = ReflectUtils.getFields(forThisType);
      if (list != null) {
        final Iterator itOnFields = list.iterator();
        while ((anItem == null) && (itOnFields.hasNext())) {
          final Field field = (Field) itOnFields.next();
          final int fieldModifiers = field.getModifiers();
          if ((Modifier.isStatic(fieldModifiers)) && (Modifier.isPublic(fieldModifiers))) {
            if (forThisType.equals(field.getType())) {
              try {
                anItem = (EnumType) field.get(null);
              } catch (IllegalArgumentException e) {
              } catch (IllegalAccessException e) {
             }
            }
          }
        }
      }
    } else {
      throw new IllegalArgumentException("EnumType.getAnItem invalid unit type : the specified type [" + forThisType + "] does not inherited of EnumType.");
    }
    return anItem;
  }

  @Override
  public String toStringControl() {
    return String.valueOf(to_int());
  }

  @Override
  public String getCodeValeur() {
    return Integer.toString(value);
  }

  @Override
  public String getLibelle() {
    return toString();
  }

  @Override
  public String getLibelleCourt() {
    return toString();
  }

  @Override
  public int getOrdre() {
    return value;
  }
}
