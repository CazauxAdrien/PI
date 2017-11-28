package com.imrglobal.framework.nls;

import java.util.Locale;

/**
 * Implémentation par défaut de l'interface {@link LocaleFactory}.
 */
public class DefaultLocaleFactory implements LocaleFactory {

  @Override
  public Locale getLocale(Language lg) {
    return lg != null ? lg.toLocale() : Locale.getDefault();
  }
}
