package com.imrglobal.framework.nls;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractLocaleFactory implements LocaleFactory {

  private Map<Language, Locale> locales = new ConcurrentHashMap<Language, Locale>();

  @Override
  public Locale getLocale(Language lg) {
    Locale locale = lg != null ? locales.get(lg) : Locale.getDefault();
    if (locale == null) {
      locale = defineLocale(lg);
    }
    return locale;
  }

  protected Locale defineLocale(Language lg) {
    Locale locale;
    Locale defLocale = lg.toLocale();
    String variant = getVariant(lg);
    String country = getCountry(lg);
    if (variant != null && country == null) {
      country = defLocale.getCountry();
    }
    if (country != null) {
      locale = new Locale(defLocale.getLanguage(), country, variant != null ? variant : "");
    } else {
      locale = defLocale;
    }
    if (isCacheLocale()) {
      this.locales.put(lg, locale);
    }
    return locale;
  }

  protected boolean isCacheLocale() {
    return true;
  }

  protected abstract String getCountry(Language lg);

  protected abstract String getVariant(Language lg);
}
