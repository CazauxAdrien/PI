package com.imrglobal.framework.utils;

import java.lang.reflect.*;
import java.util.*;

import com.imrglobal.framework.cache.LRUHashMap;
import com.imrglobal.framework.type.EnumType;

/**
 * Use this class to cast a List into a Set or a Set into a List
 */

public final class CastUtil {

  private static Map fromStringMethodCache = new LRUHashMap(true);
  private static final String NO_FROM_STRING_METH = "NO";

  /**
   * Cast a List into a Set
   *
   * @param theList list to Cast
   * @return Set
   */
  public static Set fromListToSet(List theList) {
    Set theSet = new HashSet();
    if (theList != null) {
      Iterator it = theList.iterator();
      while (it.hasNext()) {
        Object theObject = it.next();
        theSet.add(theObject);
      }
    } else
      throw new RuntimeException("Invalid List");
    return theSet;
  }

  /**
   * Cast a Set into a List
   *
   * @param theSet Set to Cast
   * @return List
   */
  public static List fromSetToList(Set theSet) {
    List theList = new ArrayList();
    if (theSet != null) {
      Iterator it = theSet.iterator();
      while (it.hasNext()) {
        Object theObject = it.next();
        theList.add(theObject);
      }
    } else
      throw new RuntimeException("Invalid Set");
    return theList;
  }


  /**
   * Returns an instance of the given class corresponding to the given
   * String.
   * The string must have the following format and/or values according to the type :
   * <ul>
   * <li> for a Number (int, short, long, double, float) : a valid number
   * <li> for a boolean : 'true' or 'false'.
   * <li> for a char : the character
   * <li> for a Date : the default format specified in the framework properties
   * file by the property DEFAULT_DATE_FORMAT.
   * <li> for an EnumType : the int value of the type or the name of the static fiel which represents the value.
   * <li> for a persistent enum type : the code of the value
   * <li> for an amount : the result of toString()
   * <li> for a class which has a static <code>fromString</code> method : see the format
   * expected by this method
   * </ul>
   * The other type are not managed.
   *
   * @param type
   * @param value
   * @return the instance of object corresponding to the string representation
   * @throws IllegalArgumentException if the type is not supported.
   */
  public static Object fromStringToObject(Class type, String value) {
    Object obj = null;
    if (String.class.equals(type)) {
      obj = value;
    } else if ((type.equals(Integer.TYPE)) || (type.equals(Integer.class))) {
      obj = new Integer(NumberUtils.parseInt(value));
    } else if ((type.equals(Long.TYPE)) || (type.equals(Long.class))) {
      obj = new Long(NumberUtils.parseLong(value));
    } else if ((type.equals(Float.TYPE)) || (type.equals(Float.class))) {
      obj = new Float(NumberUtils.parseFloat(value));
    } else if ((type.equals(Double.TYPE)) || (type.equals(Double.class))) {
      obj = new Double(NumberUtils.parseDouble(value));
    } else if ((type.equals(Character.TYPE)) || (type.equals(Character.class))) {
      if (value != null) {
        obj = new Character(value.charAt(0));
      }
    } else if ((type.equals(Short.TYPE)) || (type.equals(Short.class))) {
      obj = new Short(NumberUtils.parseShort(value));
    } else if ((type.equals(Boolean.TYPE)) || (type.equals(Boolean.class))) {
      if ("1".equals(value)) {
        obj = Boolean.TRUE;
      } else {
        obj = Boolean.valueOf(value);
      }
    } else if ((type.equals(Byte.TYPE)) || (type.equals(Byte.class))) {
      obj = new Byte(value);
    } else if (java.util.Date.class.isAssignableFrom(type)) {
      obj = DateUtils.getDate(value);
    } else if (StringBuffer.class.equals(type)) {
      obj = new StringBuffer(value);
    } else {
      Method meth = getFromStringMethod(type);
      if (meth != null) {
        try {
          obj = meth.invoke(null, new Object[]{value});
        } catch (Exception e) {
          throw new RuntimeException("CastUtils.fromStringToObject This value [" + value + "] is not valid for this type [" + type + "]!", e);
        }
      } else if (EnumType.class.isAssignableFrom(type)) {
        if (!StringUtils.isNullOrEmpty(value)) {
          try {
            obj = EnumType.from_int(type, NumberUtils.parseInt(value));
          } catch (NumberFormatException ex) {
            try {

              if (obj == null) {
                java.lang.reflect.Field field = ReflectUtils.getField(type, value);
                if (field != null) {
                  obj = field.get(null);
                } else {
                  throw new RuntimeException("CastUtil.fromStringToObject Invalid string value [" + value + "] for enum [" + type + "]!");
                }
              }
            } catch (IllegalAccessException illEx) {
              throw new RuntimeException("CastUtil.fromStringToObject Invalid string value [" + value + "] for enum [" + type + "]!", illEx);
            }
          }
        } else {
          obj = null;
        }
      }else {
        throw new IllegalArgumentException("CastUtils.fromStringToObject This type [" + type + "] is not managed!");
      }
    }

    return (obj);
  }

  /**
   * Returns true if it's possible to retrieve a value of the specified type from
   * a string.
   * <br>If this method returns true, it's possible to use the <code>fromStringToObject</code> method.
   *
   * @param type
   * @return true if it's possible to retrieve a value of the specified type from
   *         a string
   */
  public static boolean isCastFromString(Class type) {
    boolean ok;
    if (String.class.equals(type)) {
      ok = true;
    } else if ((type.equals(Integer.TYPE)) || (type.equals(Integer.class))) {
      ok = true;
    } else if ((type.equals(Long.TYPE)) || (type.equals(Long.class))) {
      ok = true;
    } else if ((type.equals(Float.TYPE)) || (type.equals(Float.class))) {
      ok = true;
    } else if ((type.equals(Double.TYPE)) || (type.equals(Double.class))) {
      ok = true;
    } else if ((type.equals(Character.TYPE)) || (type.equals(Character.class))) {
      ok = true;
    } else if ((type.equals(Short.TYPE)) || (type.equals(Short.class))) {
      ok = true;
    } else if ((type.equals(Boolean.TYPE)) || (type.equals(Boolean.class))) {
      ok = true;
    } else if ((type.equals(Byte.TYPE)) || (type.equals(Byte.class))) {
      ok = true;
    } else if (java.util.Date.class.isAssignableFrom(type)) {
      ok = true;
    } else if (EnumType.class.isAssignableFrom(type)) {
      ok = true;
    }else if (StringBuffer.class.equals(type)) {
      ok = true;
    } else {
      Method meth = getFromStringMethod(type);
      ok = meth != null;
    }
    return ok;
  }


  private static Method getFromStringMethod(Class enumType) {
    Method meth;
    Object o = fromStringMethodCache.get(enumType);
    if (NO_FROM_STRING_METH.equals(o)) {
      //On a déjà recherché la méthode mais on a rien trouvé...
      //Pa la peine de recommencer le binz...
      meth = null;
    } else if (o instanceof Method) {
      meth = (Method) o;
    } else {
      meth = null;
      Method aMeth = ReflectUtils.getMethod(enumType, "fromString");
      if (aMeth != null) {
        int modifiers = aMeth.getModifiers();
        if ((Modifier.isStatic(modifiers)) && (Modifier.isPublic(modifiers))) {
          Class[] params = aMeth.getParameterTypes();
          if ((params.length == 1) && (String.class.equals(params[0]))) {
            meth = aMeth;
          }
        }
      }
      if (meth != null) {
        fromStringMethodCache.put(enumType, meth);
      } else {
        fromStringMethodCache.put(enumType, NO_FROM_STRING_METH);
			}
		}
		return (meth);
	}

}