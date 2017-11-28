package com.imrglobal.framework.utils;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import com.imrglobal.framework.cache.FIFOMap;

/**
 * Classe utilitaire pour l'utilisation de la réflexion dans l'application.
 */
public class ReflectUtils extends ClassUtils {

  private static final Map fieldReadAccessorCache = false ? newCache() : null;
  private static final Map fieldWriteAccessorCache = false ? newCache() : null;
  private static final Map fieldAddAccessorCache = false ? newCache() : null;
  private static final Map fieldRemoveAccessorCache = false ? newCache() : null;
  private static final Map propertyDescriptorCache = false ? newCache() : null;
  private static final Map propertyReadAccessorCache = false ? newCache() : null;

  private static final String NO_ACCESSOR = "NO";

  private static Map newCache() {
    return new FIFOMap(200000);
  }

  /**
   * Return the field of the given class (or one of its super class) with the given name.<BR>
   *
   * @param aClass class with the field.
   * @param fieldName field name
   * @return field of the class or of a super class.
   */
  public static Field getField(Class aClass, String fieldName) {
    Field field = null;
    try {
      field = aClass.getDeclaredField(fieldName);
    } catch (NoSuchFieldException nsfex) {
      Class supClass = aClass.getSuperclass();
      if (supClass != null) {
        field = getField(supClass, fieldName);
      }
    } catch (SecurityException exs) {
      System.err.println("Error : Security problem with variable" + fieldName + " :" + exs);
    }
    return field;
  }

  /**
   * Return the list of fields of the given class and its super class.
   *
   * @param aClass class which one seeks fields.
   * @return list of fields.
   */
  public static List getFields(Class aClass) {
    Class theClass = aClass;
    List list = new ArrayList();
    while (theClass != null) {
      getDeclaredFields(theClass, list);
      theClass = theClass.getSuperclass();
    }
    return list;
  }

  /**
   * Return the list of fields of the given class and its super class which satisfied the given filter.
   *
   * @param aClass class which one seeks fields.
   * @param filter the filter
   * @return list of fields.
   */
  public static List getFields(Class aClass, Filter filter) {
    return (List) ListGenericsUtils.filter(getFields(aClass), filter);
  }

  /**
   * Return the list of fields of the given class and its super class which satisfied the specifieds types.
   *
   * @param aClass class which one seeks fields.
   * @param possibleTypes the types of the fields
   * @return list of fields.
   */
  public static List getFields(Class aClass, Class[] possibleTypes) {
    return (List) ListGenericsUtils.filter(getFields(aClass), new FieldFilterByType(possibleTypes));
  }

  /**
   * Return the list of fields declared in the given class.
   *
   * @param aClass class which one seeks the declared fields.
   * @param list list to fill with the founded fields.
   */
  private static void getDeclaredFields(Class aClass, List list) {
    list.addAll(Arrays.asList(aClass.getDeclaredFields()));
  }

  /**
   * Return the type of the field of the given class (or one of its super class).<BR>
   *
   * @param aClass class which one seeks the field.
   * @param fieldName field name
   * @return type of the field.
   */
  public static Class getFieldtype(Class aClass, String fieldName) {
    Field field = getField(aClass, fieldName);
    if (field != null) {
      return getField(aClass, fieldName).getType();
    }
    return (null);
  }

  /**
   * Return the value of the field of the given object.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param object object.
   * @param fieldName field name
   * @return field value.
   */
  public static Object getFieldValue(Object object, String fieldName) {
    Field field = getField(object.getClass(), fieldName);
    if (field != null) {
      return (getFieldValue(object, field));
    }
    throw new RuntimeException("ReflectUtils.getFieldValue field [" + fieldName + "] does not belong to object [" + object.getClass() + ']');
  }

  /**
   * Return the value of the field of the given object.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param object object.
   * @param field object field
   * @return field value.
   */
  public static Object getFieldValue(Object object, Field field) {
    Object value;

    if (object == null) {
      return (null);
    }

    if (field != null) {
      Method accessor = getAccessor(field, false);
      if (accessor != null) {
        try {
          value = accessor.invoke(object, new Object[0]);
        } catch (java.lang.reflect.InvocationTargetException iex) {
          throw new RuntimeException("ReflectUtils.getFieldValue field [" + field + "] can not returned by the accessor in object [" + object.getClass() + ']', iex);
        } catch (java.lang.IllegalAccessException illEx) {
          throw new RuntimeException("ReflectUtils.getFieldValue field [" + field + "] is not accessible in object [" + object.getClass() + "] using the accessor", illEx);
        }
      } else {
        try {
          try {
            field.setAccessible(true);
          } catch (Throwable ex) {
            System.err.println("Can not set the accessible flag for the field [" + field + ']');
          }
          value = field.get(object);
        } catch (IllegalAccessException ex) {
          throw new RuntimeException("ReflectUtils.getFieldValue field [" + field + "] is not accessible in object [" + object.getClass() + ']');
        }
      }
    } else {
      throw new IllegalArgumentException("ReflectUtils.getFieldValue field can not be null!");
    }
    return (value);
  }

  /**
   * Sets the value of the field of the given object.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param object object.
   * @param fieldName field name
   * @param value field value.
   */
  public static void setFieldValue(Object object, String fieldName, Object value) {
    Field field = getField(object.getClass(), fieldName);
    if (field != null) {
      setFieldValue(object, field, value);
    } else {
      throw new RuntimeException("ReflectUtils.setFieldValue field [" + fieldName + "] does not belong to object [" + object.getClass() + ']');
    }
  }

  /**
   * Sets the value of the field of the given object.<BR>
   * The field can be a field of the object class or one of its super class.
   *
   * @param object object.
   * @param field object field.
   * @param value value.
   */
  public static void setFieldValue(Object object, Field field, Object value) {
    if (field != null) {
      Method accessor = getAccessor(field, true);
      if (accessor != null) {
        try {
          Object[] parameters = new Object[1];
          parameters[0] = value;
          accessor.invoke(object, parameters);
        } catch (java.lang.reflect.InvocationTargetException iex) {
          throw new RuntimeException("ReflectUtils.setFieldValue Problem to update [" + field + "] using the accessor (" + accessor + ") in object [" + object.getClass()
              + "] : an exception is throw : [" + iex.getTargetException() + ']', iex);
        } catch (java.lang.IllegalAccessException illEx) {
          throw new RuntimeException("ReflectUtils.setFieldValue field [" + field + "] is not accessible in object [" + object.getClass() + "] using the accessor", illEx);
        }
      } else {
        try {
          try {
            field.setAccessible(true);
          } catch (Throwable ex) {
            System.err.println("Can not set the accessible flag for the field [" + field + ']');
          }
          field.set(object, value);
        } catch (IllegalAccessException ex) {
          throw new RuntimeException("ReflectUtils.setFieldValue field [" + field + "] is not accessible in object [" + object.getClass() + ']');
        }
      }
    } else {
      throw new IllegalArgumentException("ReflectUtils.setFieldValue field can not be null!");
    }
  }

  /**
   * Sets the static value of the field of the given class.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param theClass class class .
   * @param fieldName field name
   * @param value field value.
   */
  public static void setFieldValue(Class theClass, String fieldName, Object value) {
    Field field = getField(theClass, fieldName);
    if (field != null) {
      setFieldValue(theClass, field, value);
    } else {
      throw new RuntimeException("ReflectUtils.setFieldValue field [" + fieldName + "] does not belong to class [" + theClass + ']');
    }
  }

  /**
   * Sets the static value of the field of the given class.<BR>
   * The field can be a field of class or one of its super class.
   *
   * @param theClass class class.
   * @param field object field.
   * @param value field value.
   */
  public static void setFieldValue(Class theClass, Field field, Object value) {
    if (field != null) {
      Method accessor = getAccessor(field, true);
      if (accessor != null) {
        try {
          Object[] parameters = new Object[1];
          parameters[0] = value;
          accessor.invoke(theClass, parameters);
        } catch (java.lang.reflect.InvocationTargetException iex) {
          throw new RuntimeException("ReflectUtils.setFieldValue field [" + field + "] can not returned by the accessor in class [" + theClass + ']', iex);
        } catch (java.lang.IllegalAccessException illEx) {
          throw new RuntimeException("ReflectUtils.setFieldValue field [" + field + "] is not accessible in class [" + theClass + "] using the accessor", illEx);
        }
      } else {
        try {
          try {
            field.setAccessible(true);
          } catch (Throwable ex) {
            System.err.println("Can not set the accessible flag for the field [" + field + ']');
          }
          field.set(null, value);
        } catch (IllegalAccessException ex) {
          throw new RuntimeException("ReflectUtils.setFieldValue field [" + field + "] is not accessible in class [" + theClass + ']');
        }
      }
    } else {
      throw new IllegalArgumentException("ReflectUtils.setFieldValue field can not be null!");
    }
  }

  /**
   * Add a value to a collection field of the given object using an access method <code>add<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param object object.
   * @param fieldName field name
   * @param value field value.
   */
  public static void addFieldValue(Object object, String fieldName, Object value) {
    addFieldValue(object, fieldName, value, false);
  }

  /**
   * Add a value to a collection field of the given object using an access method <code>add<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super class.
   *
   * @param object object.
   * @param field object field.
   * @param value value.
   */
  public static void addFieldValue(Object object, Field field, Object value) {
    addFieldValue(object, field, value, false);
  }

  /**
   * Add a value to a collection field of the given object using an access method <code>add<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param object object.
   * @param fieldName field name
   * @param value field value.
   * @param checkDoublon true to remove potentially doublon (if it's a collection of BusinessObject)
   */
  public static void addFieldValue(Object object, String fieldName, Object value, boolean checkDoublon) {
    Field field = getField(object.getClass(), fieldName);
    if (field != null) {
      addFieldValue(object, field, value, checkDoublon);
    } else {
      throw new RuntimeException("ReflectUtils.addFieldValue field [" + fieldName + "] does not belong to object [" + object.getClass() + ']');
    }
  }

  /**
   * Add a value to a collection field of the given object using an access method <code>add<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super class.
   *
   * @param object object.
   * @param field the object field.
   * @param value the value.
   * @param checkDoublon true to remove potentially doublon (if it's a collection of BusinessObject)
   */
  public static void addFieldValue(Object object, Field field, Object value, boolean checkDoublon) {
    if (field != null) {
      Method accessor = getAddAccessor(field, object.getClass(), value.getClass());
      if (accessor != null) {
        try {
          if (checkDoublon) {
            checkDoublon(object, field, value);
          }
          Object[] parameters = new Object[1];
          parameters[0] = value;
          accessor.invoke(object, parameters);
        } catch (java.lang.reflect.InvocationTargetException iex) {
          throw new RuntimeException("ReflectUtils.addFieldValue field [" + field + "] can not returned by the accessor in object [" + object.getClass() + ']', iex);
        } catch (java.lang.IllegalAccessException illEx) {
          throw new RuntimeException("ReflectUtils.addFieldValue field [" + field + "] is not accessible in object [" + object.getClass() + "] using the accessor", illEx);
        }
      } else {
        throw new RuntimeException("ReflectUtils.addFieldValue no add accessor found for field [" + field + "] in object [" + object.getClass() + ']');
      }
    } else {
      throw new IllegalArgumentException("ReflectUtils.addFieldValue field can not be null!");
    }
  }

  private static void checkDoublon(Object object, Field field, Object value) {
    Object doublon = getObjectInSetof(object, field, value);
    if (doublon != null) {
      removeFieldValue(object, field, doublon);
    }
  }

  /**
   * Returns the object in setof wich corresponds to the specified object (the object with the same id)
   *
   * @param object the referenced object
   * @param field the setof field
   * @param value a BusinessObject or BUsinessExtract
   * @return the object in setof or null
   */
  public static Object getObjectInSetof(Object object, Field field, Object value) {
    return value;
  }

  /**
   * Remove a value from a collection field of the given object using an access method <code>remove<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param object object.
   * @param fieldName field name
   * @param value field value.
   */
  public static void removeFieldValue(Object object, String fieldName, Object value) {
    Field field = getField(object.getClass(), fieldName);
    if (field != null) {
      removeFieldValue(object, field, value);
    } else {
      throw new RuntimeException("ReflectUtils.removeFieldValue field [" + fieldName + "] does not belong to object [" + object.getClass() + ']');
    }
  }

  /**
   * Add a value to a collection field of the given object using an access method <code>add<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super class.
   *
   * @param object the object.
   * @param field the object field.
   * @param value the value.
   */
  public static void removeFieldValue(Object object, Field field, Object value) {
    if (field != null) {
      Method accessor = getRemoveAccessor(field, object.getClass(), value.getClass());
      if (accessor != null) {
        try {
          Object[] parameters = new Object[1];
          parameters[0] = value;
          accessor.invoke(object, parameters);
        } catch (java.lang.reflect.InvocationTargetException iex) {
          throw new RuntimeException("ReflectUtils.removeFieldValue field [" + field + "] can not removed by the accessor in object [" + object.getClass() + ']', iex);
        } catch (java.lang.IllegalAccessException illEx) {
          throw new RuntimeException("ReflectUtils.removeFieldValue accessor to remove field [" + field + "] is not accessible in object [" + object.getClass() + ']', illEx);
        }
      } else {
        throw new RuntimeException("ReflectUtils.removeFieldValue no remove accessor found for field [" + field + "] in object [" + object.getClass() + ']');
      }
    } else {
      throw new IllegalArgumentException("ReflectUtils.removeFieldValue field can not be null!");
    }
  }

  /**
   * Remove all values corresponding to a specified value (same id) from a collection field of the given object using an access method <code>remove<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super classes.
   *
   * @param object object.
   * @param fieldName field name
   * @param value field value.
   */
  public static void removeFieldAllValue(Object object, String fieldName, Object value) {
    Field field = getField(object.getClass(), fieldName);
    if (field != null) {
      removeFieldAllValue(object, field, value);
    } else {
      throw new RuntimeException("ReflectUtils.removeFieldAllValue field [" + fieldName + "] does not belong to object [" + object.getClass() + ']');
    }
  }

  /**
   * Remove all values corresponding to a specified value (same id) from a collection field of the given object using an access method <code>remove<Field name></code>.<BR>
   * The field can be a field of the object class or one of its super class.
   *
   * @param object the object.
   * @param field the object field.
   * @param value the value.
   */
  public static void removeFieldAllValue(Object object, Field field, Object value) {
    Object objToRemove = getObjectInSetof(object, field, value);
    if (objToRemove == null) {
      objToRemove = value;
    }
    while (objToRemove != null) {
      removeFieldValue(object, field, objToRemove);
      objToRemove = getObjectInSetof(object, field, value);
    }
  }

  /**
   * Return the method declared in the given class with the given name.<brt> If more than one method with the same name is declared in a class, one of the methods is chosen arbitrarily.
   *
   * @param theClass class which declared the method
   * @param methodName method name
   * @return the method declared in the class.
   */
  public static Method getDeclaredMethod(Class theClass, String methodName) {
    Method[] methods = theClass.getDeclaredMethods();
    Method method = null;
    int j = 0;
    while (j < methods.length) {
      String mName = methods[j].getName();
      if (mName.equals(methodName)) {
        method = methods[j];
        break;
      }
      j++;
    }
    return (method);
  }

  /**
   * Return the method of the given class (or one of its super class) with the given name.<BR>
   * If more than one method with the same name is declared in a class, one of the methods is chosen arbitrarily.
   *
   * @param aClass class with the method.
   * @param methodName field name
   * @return method of the class or of a super class.
   */
  public static Method getMethod(Class aClass, String methodName) {
    Method method = null;
    method = getDeclaredMethod(aClass, methodName);
    Class supClass = aClass.getSuperclass();
    while ((method == null) && (supClass != null)) {
      method = getDeclaredMethod(supClass, methodName);
      supClass = supClass.getSuperclass();
    }
    if (method == null) {
      method = InterfaceUtils.getMethodOnInterface(aClass, cls -> getDeclaredMethod(cls, methodName));
    }
    return method;
  }

  /**
   * Returns a Method object that reflects the specified method of the given class (or one of its superclass).
   *
   * @param aClass the class with the method.
   * @param methodName String that specifies the simple name of the desired method
   * @param parameterTypes array of Class objects that identify the method's formal parameter types, in declared order.
   * @return a method that reflects the specified method of the given class or null
   */
  public static Method getMethod(Class aClass, String methodName, Class[] parameterTypes) {
    Method method = null;
    try {
      method = aClass.getDeclaredMethod(methodName, parameterTypes);
    } catch (java.lang.NoSuchMethodException ex) {
      method = getMethodOnSuperClass(aClass, methodName, parameterTypes);
    }
    if (method == null) {
      method = InterfaceUtils.getMethodOnInterface(aClass,
          cls -> {
            try {
              return cls.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
              return null;
            }
          });
    }
    return method;
  }

  private static Method getMethodOnSuperClass(Class aClass, String methodName, Class[] parameterTypes) {
    Method method = null;
    Class supClass = aClass.getSuperclass();
    if (supClass != null) {
      method = getMethod(supClass, methodName, parameterTypes);
    }
    return method;
  }

  /**
   * Return the accessor (read or write) for the given field.
   * 
   * @param field field
   * @param write true, if you're looking for the write accessor, false for the read accessor.
   * @return the accessor.
   */
  public static Method getAccessor(Field field, boolean write) {
    if (field == null) {
      throw new IllegalArgumentException("ReflectUtil.getAccessor field can not be null!");
    }
    return (getAccessor(field, field.getDeclaringClass(), write));
  }

  /**
   * Return the accessor (read or write) for the given field.
   *
   * @param field field
   * @param forThisClass accessor for this class.
   * @param write true, if you're looking for the write accessor, false for the read accessor.
   * @return the accessor.
   */
  public static Method getAccessor(Field field, Class forThisClass, boolean write) {
    if (field == null) {
      throw new IllegalArgumentException("ReflectUtil.getAccessor field can not be null!");
    }
    if (forThisClass == null) {
      forThisClass = field.getDeclaringClass();
    }
    if (!field.getDeclaringClass().isAssignableFrom(forThisClass)) {
      throw new RuntimeException("Field [" + field + "] does not belong to class [" + forThisClass + ']');
    }
    return getAccessor(field.getName(), field.getType(), forThisClass, write);
  }

  private static Method getAccessor(String fieldName, Class fieldType, Class forThisClass, boolean write) {
    Method result;
    Object meth = getAccessorFromCache(fieldName, forThisClass, write);
    if (meth == null) {
      String accessorName = null;
      String accessorName2 = null;
      String varName = fieldName;
      Class varType = fieldType;

      Class[] parameters = null;
      if (write) {
        parameters = new Class[1];
        parameters[0] = varType;
      }

      boolean isBooleanVar = ((varType != null) && ((varType.equals(Boolean.TYPE)) || (varType.equals(Boolean.class))));
      accessorName = getAccessorName(varName, write, true, isBooleanVar);

      result = getPossibleMethod(forThisClass, accessorName, parameters);
      if (result == null) {
        accessorName2 = getAccessorName(varName, write, false, isBooleanVar);
        result = getPossibleMethod(forThisClass, accessorName2, parameters);
      }

      if ((result == null) && (isBooleanVar)) {
        if (!write) {
          accessorName = getAccessorName(varName, write, true, false);
          result = getPossibleMethod(forThisClass, accessorName, parameters);
          if (result == null) {
            accessorName2 = getAccessorName(varName, write, false, false);
            result = getPossibleMethod(forThisClass, accessorName2, parameters);
          }
          if (result == null) {
            result = getPossibleMethod(forThisClass, varName, parameters);
          }
        }
      }

      if (result == null) {
        System.out.println("ReflectUtils.getAccessor : no accessor [{0} or {1}] for field {2}");
      }
      setAccessorFromCache(fieldName, forThisClass, write, result);
    } else {
      if (meth instanceof Method) {
        result = (Method) meth;
      } else {
        // On a déjà cherché l'accesseur auparavant et on a rien trouvé...
        result = null;
      }
    }
    return (result);
  }

  private static Object getAccessorFromCache(String fieldName, Class forThisClass, boolean write) {
    Object obj;
    if (false) {
      Object key = getAccessorCacheKey(fieldName, forThisClass);
      if (write) {
        obj = fieldWriteAccessorCache.get(key);
      } else {
        obj = fieldReadAccessorCache.get(key);
      }
    } else {
      obj = null;
    }
    return obj;
  }

  private static void setAccessorFromCache(String fieldName, Class forThisClass, boolean write, Method accessor) {
    if (false) {
      Object key = getAccessorCacheKey(fieldName, forThisClass);
      Object obj;
      if (accessor != null) {
        obj = accessor;
      } else {
        obj = NO_ACCESSOR;
      }
      if (write) {
        fieldWriteAccessorCache.put(key, obj);
      } else {
        fieldReadAccessorCache.put(key, obj);
      }
    }
  }

  private static Object getAccessorCacheKey(String fieldName, Class forThisClass) {
    return new InternalKey(forThisClass, fieldName);
  }

  /**
   * Return the add accessor for the given collection field which allows to add an element to the field.
   *
   * @param field field
   * @param forThisClass accessor for this class.
   * @param classOfValue class of the value object.
   * @return the accessor.
   */
  public static Method getAddAccessor(Field field, Class forThisClass, Class classOfValue) {
    if (field == null) {
      throw new IllegalArgumentException("ReflectUtil.getAddAccessor field can not be null!");
    }
    if (forThisClass == null) {
      forThisClass = field.getDeclaringClass();
    }
    Method result;
    Object meth = getAddAccessorFromCache(field, forThisClass);
    if (meth == null) {
      String addAccessorName = null;
      String addAccessorName2 = null;
      String addAccessorName3 = null;
      String addAccessorName4 = null;
      String varName = field.getName();
      if (!field.getDeclaringClass().isAssignableFrom(forThisClass)) {
        throw new RuntimeException("Field [" + field + "] does not belong to class [" + forThisClass + ']');
      }

      Class[] parameters = null;
      parameters = new Class[1];
      parameters[0] = classOfValue;

      addAccessorName = getAddAccessorName(varName, "addTo", true);
      result = getPossibleMethod(forThisClass, addAccessorName, parameters);
      if (result == null) {
        addAccessorName2 = getAddAccessorName(varName, "addTo", false);
        result = getPossibleMethod(forThisClass, addAccessorName2, parameters);
        if (result == null) {
          addAccessorName3 = getAddAccessorName(varName, "add", true);
          result = getPossibleMethod(forThisClass, addAccessorName3, parameters);
          if (result == null) {
            addAccessorName4 = getAddAccessorName(varName, "add", false);
            result = getPossibleMethod(forThisClass, addAccessorName4, parameters);

          }
        }
      }

      setAddAccessorFromCache(field, forThisClass, result);
    } else {
      if (meth instanceof Method) {
        result = (Method) meth;
      } else {
        result = null;
      }
    }
    return (result);
  }

  private static Object getAddAccessorFromCache(Field field, Class forThisClass) {
    Object o;
    if (false) {
      Object key = getAccessorCacheKey(field.getName(), forThisClass);
      o = fieldAddAccessorCache.get(key);
    } else {
      o = null;
    }
    return o;
  }

  private static void setAddAccessorFromCache(Field field, Class forThisClass, Method accessor) {
    if (false) {
      Object key = getAccessorCacheKey(field.getName(), forThisClass);
      if (accessor != null) {
        fieldAddAccessorCache.put(key, accessor);
      } else {
        fieldAddAccessorCache.put(key, NO_ACCESSOR);
      }
    }
  }

  /**
   * Return the remove accessor for the given collection field which allows to remove an element to the field.
   *
   * @param field field
   * @param forThisClass accessor for this class.
   * @param classOfValue class of the value object.
   * @return the accessor.
   */
  public static Method getRemoveAccessor(Field field, Class forThisClass, Class classOfValue) {
    if (field == null) {
      throw new IllegalArgumentException("ReflectUtil.getAddAccessor field can not be null!");
    }
    if (forThisClass == null) {
      forThisClass = field.getDeclaringClass();
    }
    Method result;
    Object meth = getRemoveAccessorFromCache(field, forThisClass);
    if (meth == null) {
      String removeAccessorName = null;
      String removeAccessorName2 = null;
      String removeAccessorName3 = null;
      String removeAccessorName4 = null;
      String varName = field.getName();
      if (!field.getDeclaringClass().isAssignableFrom(forThisClass)) {
        throw new RuntimeException("Field [" + field + "] does not belong to class [" + forThisClass + ']');
      }

      Class[] parameters = null;
      parameters = new Class[1];
      parameters[0] = classOfValue;

      removeAccessorName = getRemoveAccessorName(varName, "removeFrom", true);
      result = getPossibleMethod(forThisClass, removeAccessorName, parameters);
      if (result == null) {
        removeAccessorName2 = getRemoveAccessorName(varName, "removeFrom", false);
        result = getPossibleMethod(forThisClass, removeAccessorName2, parameters);
        if (result == null) {
          removeAccessorName3 = getRemoveAccessorName(varName, "remove", true);
          result = getPossibleMethod(forThisClass, removeAccessorName3, parameters);
          if (result == null) {
            removeAccessorName4 = getRemoveAccessorName(varName, "remove", false);
            result = getPossibleMethod(forThisClass, removeAccessorName4, parameters);
          }
        }
      }

      setRemoveAccessorFromCache(field, forThisClass, result);
    } else {
      if (meth instanceof Method) {
        result = (Method) meth;
      } else {
        result = null;
      }
    }
    return (result);
  }

  private static Object getRemoveAccessorFromCache(Field field, Class forThisClass) {
    Object o;
    if (false) {
      Object key = getAccessorCacheKey(field.getName(), forThisClass);
      o = fieldRemoveAccessorCache.get(key);
    } else {
      o = null;
    }
    return o;
  }

  private static void setRemoveAccessorFromCache(Field field, Class forThisClass, Method accessor) {
    if (false) {
      Object key = getAccessorCacheKey(field.getName(), forThisClass);
      if (accessor != null) {
        fieldRemoveAccessorCache.put(key, accessor);
      } else {
        fieldRemoveAccessorCache.put(key, NO_ACCESSOR);
      }
    }
  }

  private static String getAccessorName(String varName, boolean write, boolean upcase, boolean booleanVar) {
    String prefix;
    if (write) {
      prefix = "set";
    } else {
      prefix = booleanVar ? "is" : "get";
    }
    return buildAccessorName(varName, prefix, upcase);
  }

  private static String getAddAccessorName(String varName, String prefix, boolean upcase) {
    return buildAccessorName(varName, null == prefix ? "addTo" : prefix, upcase);
  }

  private static String getRemoveAccessorName(String varName, String prefix, boolean upcase) {
    return buildAccessorName(varName, null == prefix ? "removeFrom" : prefix, upcase);
  }

  protected static String buildAccessorName(String varName, String prefix, boolean upcase) {
    if (upcase) {
      return prefix + StringUtils.upcaseFirstChar(varName);
    }
    return prefix + varName;
  }

  /**
   * Returns a Method object that reflects the specified method of the given class (or one of its superclass). <BR>
   * The services returns a method which can be invoked using parameters of the given type. So, the returned method can have parameter types less specific than the given parameter types. To find a
   * matching method in a class C: If C (or one of this super class) declares exactly one method with the specified name and exactly the same formal parameter types, that is the method reflected. If C
   * (or one of this super class) declares a method with the specified name and with parameters assignable from given parameters that is the method reflected. <BR>
   * Primitive type are considered equivalent to their wrapper class (int is considered as Integer).
   *
   * @param aClass the class with the method.
   * @param methodName String that specifies the simple name of the desired method
   * @param parameterTypes array of Class objects that identify the method's formal parameter types, in declared order.
   * @return a method or null if not found
   */
  public static Method getPossibleMethod(Class aClass, String methodName, Class[] parameterTypes) {
    Method method = null;
    try {
      method = aClass.getDeclaredMethod(methodName, parameterTypes);
    } catch (java.lang.NoSuchMethodException ex) {
      method = getMethodWithAssignableParamters(aClass, methodName, parameterTypes);
      if (method == null) {
        method = getPossibleMethodOnSuperclass(aClass, methodName, parameterTypes);
      }
      if (method == null) {
        method = getPossibleMethodOnInterfaces(aClass, methodName, parameterTypes);
      }
    }
    return method;
  }

  private static Method getPossibleMethodOnSuperclass(Class aClass, String methodName, Class[] parameterTypes) {
    Method method = null;
    Class supClass = aClass.getSuperclass();
    if (supClass != null) {
      method = getPossibleMethod(supClass, methodName, parameterTypes);
    }
    return method;
  }

  private static Method getPossibleMethodOnInterfaces(Class aClass, String methodName, Class[] parameterTypes) {
    return InterfaceUtils.getMethodOnInterface(aClass, cls -> getPossibleMethod(cls, methodName, parameterTypes));
  }

  private static Method getMethodWithAssignableParamters(Class aClass, String methodName, Class[] parameterTypes) {
    Method method;
    method = null;
    Method[] methods = aClass.getDeclaredMethods();
    int i = 0;
    while ((method == null) && (i < methods.length)) {
      Method meth = methods[i];
      if (meth.getName().equals(methodName)) {
        if (checkParameterTypes(parameterTypes, meth.getParameterTypes())) {
          method = meth;
        }
      }
      i++;
    }
    return method;
  }

  private static boolean checkParameterTypes(Class[] type1, Class[] type2) {
    boolean ok = true;
    if (type1.length == type2.length) {
      if (type1.length > 0) {
        int i = 0;
        while (ok && i < type1.length) {
          Class class1 = type1[i];
          Class class2 = type2[i];
          if (!isAssignable(class2, class1)) {
            ok = false;
          }
          i++;
        }
      }
      return ok;
    }
    return false;
  }

  /**
   * Determines if the class or interface represented by theClass parameter is either the same as, or is a superclass or superinterface of, the class or interface represented by the fromThisClass
   * parameter. It returns true if so; otherwise it returns false. If one of the parameters represents a primitive type, this method returns true if the other parameter represents the same primitive
   * type or the wrapper of its primitive type; otherwise it returns false.
   *
   * @param theClass first class
   * @param fromThisClass second class
   * @return true if second class is assignable from first
   */
  public static boolean isAssignable(Class theClass, Class fromThisClass) {
    if (theClass.isAssignableFrom(fromThisClass)) {
      return true;
    }

    if (theClass.isPrimitive()) {
      if (fromThisClass.equals(getWrapper(theClass))) {
        return true;
      }
    } else if (fromThisClass.isPrimitive()) {
      if (theClass.equals(getWrapper(fromThisClass))) {
        return true;
      }
    }

    return false;
  }

  /**
   * @param thisClass the class
   * @return true if the given class is a primitive, a primitive wrapper or String.class
   */
  public static boolean isSimpleType(Class thisClass) {
    if ((thisClass.isPrimitive()) ||
        (isPrimitiveWrapper(thisClass)) ||
        (String.class.equals(thisClass))) {
      return (true);
    }
    return (false);
  }

  /**
   * Returns the object referenced by the given object according a string which describes the "path" of the referenced object. <BR>
   * The string indicates the field(s), or the index in List or Array or the key in Map to use to access to the referenced object.
   * <ul>
   * <li>Use the tag [i] to indicate the index of the object in an Array, a list or a businessExtract.
   * <li>Use the tag {key} to indicate the key of the object in a Map or a Context.
   * <li>Use the name of the field to access to a field of an Object. (in the case of an array you can use "length" and "empty" to know the array's length and if the array is empty.
   * </ul>
   * You can use a combination of all this tag. <br>
   * For example : <br>
   * <code>ReflectUtils.getReferencedObject(myObject,"myVar")<code> <br>
   * Retrieve the value of the field "myVar" of the object "myObject". <br>
   * <code>ReflectUtils.getReferencedObject(myObject,"myVar.refVar")<code> <br>
   * Retrieve the value of the field "refVar" of the object which is the value of the field "myVar" of "myObject". <br>
   * <code>ReflectUtils.getReferencedObject(myObject,"myVar[1].refVar")<code> <br>
   * Retrieve the value of the field "refVar" of the object with the index 1 in the List or Array object which is the value of the field "myVar" of "myObject".
   *
   * @param referencingObject the referencing object
   * @param propertyPath The string describing the "path" to the object.
   * @return the object referenced
   * 
   * @TODO : REFACTORISER // En découpant les problématiques : analyse / navigation (+ utilisation d'un pattern visiteur)
   */
  public static Object getReferencedObject(Object referencingObject, String propertyPath) {
    while (true) {
      int index;
      Object referencedObject = null;
      if (referencingObject == null) {
        return (null);
      }

      // Determine if the next child is a Map, Array, or Property.
      char firstChar = propertyPath.charAt(0);
      if (firstChar == '{') {
        //
        if ((index = propertyPath.indexOf('}')) > 0) {
          if (referencingObject instanceof Map) {
            referencedObject = ((Map) referencingObject).get(propertyPath.substring(1, index));
          } else {
            throw new RuntimeException("ReflectUtils.getReferencedObject, object \"" + referencingObject + "\" is not a Map or Context object");
          }
          index++;
        } else {
          throw new RuntimeException("ReflectUtils.getReferencedObject : invalid Map object reference \"" +
              propertyPath + "\", no closing \"}\" character.");
        }
      } else if (firstChar == '[') {
        // This is an Array
        if ((index = propertyPath.indexOf(']')) > 0) {
          int array_index;
          try {
            array_index = Integer.valueOf(propertyPath.substring(1, index)).intValue();
          } catch (NumberFormatException nfe) {
            throw new RuntimeException("ReflectUtils.getReferencedObject, the index inside the [] characters is not a number.", nfe);
          }
          if (referencingObject instanceof List) {
            List list = (List) referencingObject;
            if (array_index < list.size()) {
              referencedObject = list.get(array_index);
            }
          } else if (referencingObject.getClass().isArray()) {
            if (array_index < Array.getLength(referencingObject)) {
              referencedObject = Array.get(referencingObject, array_index);
            }
          } else {
            throw new RuntimeException(
                "ReflectUtils.getReferencedObject : object \"" + referencingObject +
                    "\" is not an Array or a List object");
          }
          index++;
        } else {
          throw new RuntimeException(
              "ReflectUtils.getReferencedObject : invalid Array object reference \"" +
                  propertyPath + "\", no closing \"]\" character.");
        }
      } else {
        int start = 0;
        if (firstChar == '.') {
          start = 1;
        }
        // Get the property name
        for (index = start; index < propertyPath.length(); index++) {
          char c = propertyPath.charAt(index);
          if (c == '.' || c == '[' || c == '{') {
            break;
          }
        }
        String fieldName = propertyPath.substring(start, index);

        if (referencingObject.getClass().isArray()) {
          // Cas d'un objet de type tableau
          if ("length".equalsIgnoreCase(fieldName)) {
            referencedObject = new Integer(Array.getLength(referencingObject));
          } else if ("empty".equalsIgnoreCase(fieldName)) {
            referencedObject = Boolean.valueOf((Array.getLength(referencingObject) == 0));
          } else {
            throw new RuntimeException("ReflectUtils.getReferencedObject, field \"" + fieldName +
                "\" not managed for Array objects.");
          }
        } else {
          // On veut récupérer la valeur d'une propriété de l'objet ou le
          // résultat d'une méthode...

          if (("size".equals(fieldName)) && (referencingObject instanceof Collection)) {
            // Cas spécial : quand la collection est le résultat de l'appel à Collections.unmodifiableList ou
            // Collections.unmodifiableSet, on ne peut pas utiliser la réflection. Elle provoque une
            // IllegalAccessException
            referencedObject = new Integer(((Collection) referencingObject).size());
          } else {
            try {
              referencedObject = getPropertyValue(referencingObject, fieldName);
            } catch (RuntimeException e) {
              referencedObject = simulatePropertyValue(referencingObject, fieldName);
            }
          }
        }
      }

      // Determine it this object has any children
      if ((index < propertyPath.length()) && (referencedObject != null)) {
        referencingObject = referencedObject;
        propertyPath = propertyPath.substring(index);
        continue;
      }
      return referencedObject;
    }
  }

  /**
   * Look for a method which allow to interpret the specified name as a property of the given objet. <br>
   * Look for a read accessor method (get<code>varName</code>),if not find, look for a method whith the same name than the name in parameter.
   *
   * @param referencingObject the referencing object
   * @param varName the property name
   * @return the method if it's exist
   */
  public static Object simulatePropertyValue(Object referencingObject, String varName) {
    String methodName = getAccessorName(varName, false, true, false);
    Method result = getPossibleMethod(referencingObject.getClass(), methodName, null);
    if (result == null) {
      methodName = getAccessorName(varName, false, false, false);
      result = getPossibleMethod(referencingObject.getClass(), methodName, null);
    }
    if (result == null) {
      methodName = getAccessorName(varName, false, true, true);
      result = getPossibleMethod(referencingObject.getClass(), methodName, null);
    }
    if (result == null) {
      methodName = getAccessorName(varName, false, false, true);
      result = getPossibleMethod(referencingObject.getClass(), methodName, null);
    }
    if (result == null) {
      result = getPossibleMethod(referencingObject.getClass(), varName, null);
    }
    if (result != null) {
      try {
        return result.invoke(referencingObject, (Object[]) null);
      } catch (Exception e) {
        throw new RuntimeException("ReflectUtils : Problem to invoke method " + result.getName() + " on object " + referencingObject + " (" + referencingObject.getClass() + ")", e);
      }
    }
    throw new RuntimeException("ReflectUtils : no way to retrieve the value of the property " + varName + " of the object " + referencingObject.getClass());
  }

  /**
   * Sets the value of the property of the given object.<BR>
   * The property can be a property of the object class or one of its super classes. <BR>
   * This method used bean introspection.
   *
   * @param object object.
   * @param property property of the object.
   * @param value the value
   */
  public static void setPropertyValue(Object object, String property, Object value) {
    PropertyDescriptor propertyDescriptor = getPropertyDescriptor(object.getClass(), property);
    if (propertyDescriptor != null) {
      Method writeMethod = propertyDescriptor.getWriteMethod();
      if (writeMethod != null) {
        try {
          Object[] param = { value };
          writeMethod.invoke(object, param);
        } catch (java.lang.reflect.InvocationTargetException iEx) {
          throw new RuntimeException("ReflectUtils.setPropertyValue Invocation of method [" + writeMethod + "] failed for [" + object + "].", iEx);
        } catch (java.lang.IllegalAccessException iaEx) {
          throw new RuntimeException("ReflectUtils.setPropertyValue Can not access to method [" + writeMethod + "] for [" + object + "].", iaEx);
        }
      } else {
        setFieldValue(object, property, value);
      }
    } else {
      setFieldValue(object, property, value);
    }
  }

  /**
   * Return the value of the property of the given object.<BR>
   * The property can be a property of the object class or one of its super classes. <BR>
   * This method used bean introspection.
   *
   * @param object object.
   * @param property property of the object.
   * @return field value.
   */
  public static Object getPropertyValue(Object object, String property) {
    Method readMethod = getPropertyReadMethod(object.getClass(), property);
    if (readMethod != null) {
      try {
        return readMethod.invoke(object);
      } catch (java.lang.reflect.InvocationTargetException iEx) {
        throw new RuntimeException("ReflectUtils.getPropertyValue Invocation of method [" + readMethod + "] failed for [" + object + "].", iEx);
      } catch (java.lang.IllegalAccessException iaEx) {
        throw new RuntimeException("ReflectUtils.getPropertyValue Can not access to method [" + readMethod + "] for [" + object + "].", iaEx);
      }
    }
    return getFieldValue(object, property);
  }

  /**
   * @param obj the object
   * @param property the property name
   * @return true if the given property is a property of the object.
   */
  public static boolean checkProperty(Object obj, String property) {
    boolean isOk = false;
    PropertyDescriptor propertyDescriptor = getPropertyDescriptor(obj.getClass(), property);
    if (propertyDescriptor != null) {
      isOk = true;
    } else {
      if (getField(obj.getClass(), property) != null) {
        isOk = true;
      }
    }
    return (isOk);
  }

  public static PropertyDescriptor getPropertyDescriptor(Class theClass, String property) {
    PropertyDescriptor propertyDescriptor = null;
    Object o = getPropertyDescriptoFromCache(theClass, property);
    if (o == null) {
      try {
        BeanInfo beanInfo = IntrospectorUtils.getBeanInfo(theClass);
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < properties.length; i++) {
          if (properties[i].getName().equals(property)) {
            propertyDescriptor = properties[i];
            break;
          }
        }
        setPropertyDescriptorInCache(theClass, property, propertyDescriptor);
      } catch (java.beans.IntrospectionException ex) {
        System.err.println("Bean introspection failed for [" + theClass.getName() + ']');
      }
    } else {
      if (o instanceof PropertyDescriptor) {
        propertyDescriptor = (PropertyDescriptor) o;
      }
    }
    return propertyDescriptor;
  }

  public static Method getPropertyReadMethod(Class theClass, String property) {
    Method m = null;
    Object o = getPropertyReadMethodFromCache(theClass, property);
    if (o == null) {
      PropertyDescriptor propertyDescriptor = getPropertyDescriptor(theClass, property);
      if (propertyDescriptor != null) {
        m = propertyDescriptor.getReadMethod();
      }
      setPropertyReadMethodInCache(theClass, property, m);
    } else {
      if (o instanceof Method) {
        m = (Method) o;
      }
    }
    return m;
  }

  private static Object getPropertyDescriptoFromCache(Class forThisClass, String property) {
    Object o;
    if (false) {
      Object key = getAccessorCacheKey(property, forThisClass);
      o = propertyDescriptorCache.get(key);
    } else {
      o = null;
    }
    return o;
  }

  private static void setPropertyDescriptorInCache(Class forThisClass, String property, PropertyDescriptor descriptor) {
    if (false) {
      Object key = getAccessorCacheKey(property, forThisClass);
      if (descriptor != null) {
        propertyDescriptorCache.put(key, descriptor);
      } else {
        propertyDescriptorCache.put(key, NO_ACCESSOR);
      }
    }
  }

  private static Object getPropertyReadMethodFromCache(Class forThisClass, String property) {
    Object o;
    if (false) {
      Object key = getAccessorCacheKey(property, forThisClass);
      o = propertyReadAccessorCache.get(key);
    } else {
      o = null;
    }
    return o;
  }

  private static void setPropertyReadMethodInCache(Class forThisClass, String property, Method m) {
    if (false) {
      Object key = getAccessorCacheKey(property, forThisClass);
      if (m != null) {
        propertyReadAccessorCache.put(key, m);
      } else {
        propertyReadAccessorCache.put(key, NO_ACCESSOR);
      }
    }
  }

  public static List<Method> getMethods(Class forThisClass, String withThisName) {
    List<Method> l = new ArrayList<>();
    Class cls = forThisClass;
    while (cls != null) {
      getMethods(cls, withThisName, l);
      cls = cls.getSuperclass();
    }
    return l;
  }

  private static void getMethods(Class forThisClass, String withThisName, List<Method> l) {
    Method[] methods = forThisClass.getDeclaredMethods();
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      if (method.getName().equals(withThisName)) {
        l.add(method);
      }
    }
  }

  /**
   * Returns true if an instance of the specified class can be cloned. <br>
   * This method returns true if :
   * <ul>
   * <li>the class implements java.lang.Cloneable
   * <li>and the method clone is public
   * </ul>
   *
   * @param cls the class
   * @return true if an instance of the specified class can be cloned
   */
  public static boolean isCloneable(Class cls) {
    boolean ok = false;
    if (Cloneable.class.isAssignableFrom(cls)) {
      Method cloneMeth = getMethod(cls, "clone", null);
      if (cloneMeth != null) {
        ok = Modifier.isPublic(cloneMeth.getModifiers());
      }
    }
    return ok;
  }

  /**
   * If the object is cloneable (implements Cloneable and has a public method clone), clone the object by calling the clone method.
   *
   * @param obj the object
   * @return the clone
   * @throws CloneNotSupportedException if the object is not cloneable
   * @see #isCloneable(java.lang.Class)
   */
  public static Object clone(Object obj) throws CloneNotSupportedException {
    Object clone = null;
    if ((obj != null) && (isCloneable(obj.getClass()))) {
      Method cloneMeth = getMethod(obj.getClass(), "clone", null);
      if (cloneMeth != null) {
        try {
          clone = cloneMeth.invoke(obj, (Object[]) null);
        } catch (IllegalAccessException e) {
          throw new CloneNotSupportedException("Can not invoke the clone method : " + e.getMessage());
        } catch (InvocationTargetException e) {
          Throwable cause = e.getCause();
          if (cause instanceof CloneNotSupportedException) {
            throw (CloneNotSupportedException) cause;
          }
          throw new CloneNotSupportedException("Can not invoke the clone method : " + cause.getMessage());
        }
      }
    } else {
      throw new CloneNotSupportedException("Object is not cloneable");
    }
    return clone;
  }

  static class InternalKey {
    final Class cls;
    final String property;
    final int hashCode;

    InternalKey(Class cls, String property) {
      this.cls = cls;
      this.property = property;
      int result = cls.hashCode();
      this.hashCode = 31 * result + (property != null ? property.hashCode() : 0);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      InternalKey that = (InternalKey) o;
      if (!cls.equals(that.cls)) {
        return false;
      }
      if (property != null ? !property.equals(that.property) : that.property != null) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return this.hashCode;
    }
  }

  /**
   * Retourne la classe du type générique déclaré dans l'ordre passé en paramètre<br>
   * Exemple : <br>
   * private class Objet1Ressource extends AbstractResource<Objet1, Objet1Representation><br>
   *
   * getClassTypeGeneric(Objet1Ressource, 0) retourne Object1.class <br>
   * getClassTypeGeneric(Objet1Ressource, 1) retourne Objet1Representation.class<br>
   *
   * Fonctionne également avec des types variables : <br>
   * private class Objet1Ressource<T extends Objet1, U extends Objet1Representation> extends AbstractResource<T, U> {<br>
   */
  public static Class getClassTypeGeneric(Class classe, int ordreTypeArgument) {
    if (classe == null || !(classe.getGenericSuperclass() instanceof ParameterizedType)) {
      return null;
    }
    Type type = ((ParameterizedType) classe.getGenericSuperclass()).getActualTypeArguments()[ordreTypeArgument];
    if (type instanceof Class) {
      return (Class) type;
    } else if (type instanceof TypeVariable) {
      return (Class) ((TypeVariable) type).getBounds()[0];
    }
    return null;
  }

  /**
   * Retourne la classe du type générique de la classe passée en argument
   * 
   * @see ReflectUtils#getClassTypeGeneric(Class, int)
   */
  public static Class getClassTypeGeneric(Class classe) {
    return getClassTypeGeneric(classe, 0);
  }

  /**
   * Retourne la classe du paramètre utilisée dans la méthode addTo du field spécifié.
   */
  public static Class getAddAccessorParameterType(Class classe, String fieldName) {
    Method method = getMethod(classe, getAddAccessorName(fieldName, null, true));
    if (method == null) {
      method = getMethod(classe, getAddAccessorName(fieldName, null, false));
    }
    if (method == null) {
      method = getMethod(classe, getAddAccessorName(fieldName, "add", true));
    }
    if (method == null) {
      method = getMethod(classe, getAddAccessorName(fieldName, "add", false));
    }
    return method != null && method.getParameterCount() == 1 ? method.getParameterTypes()[0] : null;
  }
}