package com.imrglobal.framework.nls;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.imrglobal.framework.utils.*;

/**
 * Utility containing a set of methods to deal with static internationalisation These methods give equivalencies between codes and internationalized labels.
 */
public class NLS {

  private static Map<LocaleResourceKey, ResourceBundle> bundleCache = new ConcurrentHashMap<>();
  private static LocaleFactory LOCALE_FACTORY;

  static {
    String implem = null;
    if (implem != null) {
      try {
        LOCALE_FACTORY = (LocaleFactory) ClassUtils.classForName(implem).newInstance();
      } catch (Exception e) {
        LOCALE_FACTORY = new DefaultLocaleFactory();
      }
    } else {
      LOCALE_FACTORY = new DefaultLocaleFactory();
    }
  }

  /**
   * Translates a label according to its code (String)
   *
   * @param instance of a class implementing {@link Internationalizable} containing the resource to internationalize
   * @param resourceName Code of the resource to internationalise
   * @param language to use to translate
   * @return translation of the object
   */
  public static String getResource(Internationalizable instance, String resourceName, Language language) {
    if (language != null) {
      return getResource(instance, resourceName, LOCALE_FACTORY.getLocale(language));
    }
    throw new IllegalArgumentException("language cannot be null");
  }

  /**
   * Translates a label according to its code (String)
   *
   * @param instance of a class implementing {@link Internationalizable} containing the resource to internationalize
   * @param resourceName Code of the resource to internationalise
   * @param locale Locale needed to translate
   * @return translation of the object
   */
  public static String getResource(Internationalizable instance, String resourceName, Locale locale) {
    return getResource(instance.getBundleBaseName(), resourceName, locale);
  }

  /**
   * Translates a label according to its code (String)
   *
   * @param bundleName name to use
   * @param resourceName Code of the resource to internationalise
   * @param language into which the resource must be translated
   * @return translation of the object
   */

  public static String getResource(String bundleName, String resourceName, Language language) {
    if (language != null) {
      return getResource(bundleName, resourceName, LOCALE_FACTORY.getLocale(language));
    }
    throw new IllegalArgumentException("language cannot be null");
  }

  /**
   * Translates a label according to its code (String)
   *
   * @param bundleName name to use
   * @param resourceName Code of the resource to internationalise
   * @param locale for the translation
   * @return translation of the object
   */
  public static String getResource(String bundleName, String resourceName, Locale locale) {
    ResourceBundle bundle;
    bundleName = getCompleteBundleName(bundleName);

    bundle = getResourceBundle(bundleName, locale);
    return bundle.getString(resourceName);

  }

  /**
   * @return {@link ResourceBundle} for the bundleName and the language
   */
  public static ResourceBundle getResourceBundle(String bundleName, Language lg) {
    return getResourceBundle(bundleName, LOCALE_FACTORY.getLocale(lg));
  }

  /**
   * @return {@link ResourceBundle} for the bundleName and the locale
   */
  public static ResourceBundle getResourceBundle(String bundleName, Locale locale) {
    ResourceBundle bundle = null;
    LocaleResourceKey cacheKey = new LocaleResourceKey(locale, bundleName);
    bundle = (ResourceBundle) bundleCache.get(cacheKey);
    if (bundle == null) {
      bundle = initResourceBundle(bundleName, locale);
      bundleCache.put(cacheKey, bundle);
    }
    return bundle;
  }

  private static ResourceBundle initResourceBundle(String bundleName, Locale locale) {
    ResourceBundle bundle = null;
    try {
      bundle = ResourceBundle.getBundle(bundleName, locale);
    } catch (java.util.MissingResourceException e) {
      try {
        bundle = ResourceBundle.getBundle(bundleName, locale, Thread.currentThread().getContextClassLoader());
      } catch (java.util.MissingResourceException e2) {
        bundle = searchBundleInPlugins(bundleName, locale);
      }
    }
    return bundle;
  }

  private static ResourceBundle searchBundleInPlugins(String bundleName, Locale locale) {
    return null;
  }

  private static String getCompleteBundleName(String bundleName) {

    return null;
  }

  /**
   * Translate in the language of the session the specified code using the specified bundle. <br>
   * This method returns the code
   * <ul>
   * <li>if the bundle is null or empty
   * <li>if the code can not be translated
   * </ul>
   *
   * @return the result of translation or the code if translation failed
   */
  public static String translate(Language lg, String bundle, String code) {
    return translate(lg, bundle, code, null);
  }

  /**
   * Translate in the language of the session the specified code using the specified bundle and eventually format the message using the specified parameters. <br>
   * This method returns the code
   * <ul>
   * <li>if the bundle is null or empty
   * <li>if the code can not be translated
   * </ul>
   *
   * @return the result of translation or the code if translation failed
   */
  public static String translate(Language lg, String bundle, String code, Object[] param) {
    return translate(lg, bundle, code, code, param);
  }

  /** @return {@link #translate(Language, NLSCode, Object[])} avec {@code null} pour le paramètre {@code param}. */
  public static String translate(Language lg, NLSCode code) {
    return translate(lg, code, null);
  }

  /**
   * Translate in the language of the session the specified code using the specified bundle and eventually format the message using the specified parameters. <br>
   * This method returns the code
   * <ul>
   * <li>if the bundle is null or empty
   * <li>if the code can not be translated
   * </ul>
   *
   * @return the result of translation or the code if translation failed
   */
  public static String translate(Language lg, NLSCode code, Object[] param) {
    return code != null ? translate(lg, code.getBundle(), code.getCode(), param) : null;
  }

  /**
   * Translate in the language of the session the specified code using the specified bundle and eventually format the message using the specified parameters. <br>
   * This method returns the default value
   * <ul>
   * <li>if the bundle is null or empty
   * <li>if the code can not be translated
   * </ul>
   *
   * @param lg language
   * @param bundle bundle
   * @param code the code
   * @param defValue default value
   * @param param parameters used to format the message
   * @return the result of translation or the code if translation failed
   */
  public static String translate(Language lg, String bundle, String code, String defValue, Object[] param) {
    String res;
    if (!StringUtils.isNullOrBlank(bundle)) {
      if (lg == null) {
        lg = Language.DEFAULT;
      }
      try {
        res = getResource(bundle, code, lg);
      } catch (Throwable ex) {
        res = defValue;
      }
    } else {
      res = defValue;
    }
    if (res != null && param != null) {
      res = StringUtils.messageFormat(res, param);
    }
    return res;
  }
}