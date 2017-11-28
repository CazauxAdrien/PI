package com.imrglobal.framework.utils;

import java.lang.reflect.*;
import java.util.Map;

import com.imrglobal.framework.cache.LRUHashMap;

/**
 * Factory of HolidaysCalendar. <br>
 * The implementation used for the default HolidaysCalendar is defined in the framework properties file by the property <code>DEFAULT_HOLIDAYS_CALENDAR</code>. If this property is not defined, or the
 * implementation specified does not exists, one use FrencHolidaysCalendar as default implementation. <br>
 * <br>
 * The implementation used for the HolidaysCalendar associated with a country or region code is defined in the framework properties file by the property
 * <code>HOLIDAYS_CALENDAR_[country/region code]</code>. If this property is not defined, or the implementation specified does not exists, one use the implementation of the default HolidaysCalendar.
 * 
 */
public class HolidaysCalendarFactory {

  public static HolidaysCalendar DEFAULT_HOLLYDAY_CALENDAR;
  private static Map calendarCache = new LRUHashMap();
  private static final String CALENDAR_PROPERTY_PREFIX = "HOLIDAYS_CALENDAR_";
  private static final String DEFAULT_CALENDAR_PROPERTY = "DEFAULT_HOLIDAYS_CALENDAR";

  static {
    initDefaultHolidaysCalendar();
  }

  private static void initDefaultHolidaysCalendar() {
    String defClsName = FrenchHolidaysCalendar.class.getName();
    String clsName = null;
    if (defClsName.equals(clsName)) {
      DEFAULT_HOLLYDAY_CALENDAR = new FrenchHolidaysCalendar();
    } else {
      try{
      Class cls = ClassUtils.classForName(clsName);
      try {
        DEFAULT_HOLLYDAY_CALENDAR = newCalendarInstance(cls, null);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      }catch(Exception e){
        
      }
      
      if (DEFAULT_HOLLYDAY_CALENDAR == null) {
        DEFAULT_HOLLYDAY_CALENDAR = new FrenchHolidaysCalendar();
      }
    }
  }

  /**
   * Returns the holidays calendar for the specified country. <br>
   * If the country specified is null, this method returns the default HolidaysCalendar. <br>
   * The implementation used for the HolidaysCalendar associated with a country or region code is defined in the framework properties file by the property
   * <code>HOLIDAYS_CALENDAR_[country/region code]</code>. If this property is not defined, or the implementation specified does not exists, one use the implementation of the default HolidaysCalendar.
   * 
   * @param country the country/region code as it's specified in Locale
   * @return the holidays calendar or the default holidays calendar if the country is null
   */
  public static HolidaysCalendar getHolidaysCalendar(String country) {
    HolidaysCalendar holidaysCalendar;
    if (country != null) {
      holidaysCalendar = (HolidaysCalendar) calendarCache.get(country);
      if (holidaysCalendar == null) {
        holidaysCalendar = initHolidaysCalendar(country);
        calendarCache.put(country, holidaysCalendar);
      }
    } else {
      holidaysCalendar = DEFAULT_HOLLYDAY_CALENDAR;
    }
    return holidaysCalendar;
  }

  private static HolidaysCalendar initHolidaysCalendar(String country) {
    HolidaysCalendar holidaysCalendar = null;
    String clsName = hollidayCalendarClassName(country);

    Class cls;
    try{
    if (clsName != null) {
      cls = ClassUtils.classForName(clsName);
    } else {
      cls = DEFAULT_HOLLYDAY_CALENDAR.getClass();
    }
    holidaysCalendar = newCalendarInstance(cls, country);
    }catch(Exception e) {
      
    }
    
    if (holidaysCalendar == null) {
      holidaysCalendar = DEFAULT_HOLLYDAY_CALENDAR;
    }
    return holidaysCalendar;
  }

  private static String hollidayCalendarClassName(String country) {

    return null;
  }

  private static String getCalendarProperty(String country, Boolean mode) {
    StringBuffer prop = new StringBuffer(CALENDAR_PROPERTY_PREFIX);
    if (country != null) {
      if (mode == null) {
        prop.append(country);
      } else if (mode == Boolean.TRUE) {
        prop.append(country.toLowerCase());
      } else if (mode == Boolean.FALSE) {
        prop.append(country.toUpperCase());
      }
    }
    return prop.toString();
  }

  private static HolidaysCalendar newCalendarInstance(Class cls, String country) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    Constructor constructor = getConstructorWithCountryCode(cls);
    HolidaysCalendar holidaysCalendar;
    if (constructor == null) {
      holidaysCalendar = (HolidaysCalendar) cls.newInstance();
      holidaysCalendar.setCountry(country);
    } else {
      holidaysCalendar = (HolidaysCalendar) constructor.newInstance(new Object[] { country });
    }
    return holidaysCalendar;
  }

  private static Constructor getConstructorWithCountryCode(Class ofThisClass) {
    Class[] Params;
    Constructor constructor;

    if (ofThisClass != null) {
      Params = new Class[1];
      try {
        Params[0] = String.class;
        constructor = ofThisClass.getConstructor(Params);
        return constructor;
      } catch (NoSuchMethodException ex1) {
        return null;
      } catch (SecurityException ex2) {
        return null;
      }
    } else {
      throw new IllegalArgumentException("Class cannot be Null!!");
    }
  }

}
