package com.imrglobal.framework.nls;

import java.io.Serializable;
import java.util.Locale;

/**
 * Key used to reference a ressource which depend of locale.
 */
public class LocaleResourceKey implements Serializable {

  private String resource;
  private Locale locale;

  public LocaleResourceKey(Locale locale, String resource) {
    this.locale = locale;
    this.resource = resource;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LocaleResourceKey that = (LocaleResourceKey) o;
    if (locale != null ? !locale.equals(that.locale) : that.locale != null) {
      return false;
    }
    if (resource != null ? !resource.equals(that.resource) : that.resource != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = resource != null ? resource.hashCode() : 0;
    result = 31 * result + (locale != null ? locale.hashCode() : 0);
    return result;
  }
}
