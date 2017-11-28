package com.imrglobal.framework.nls;

/**
 * Implémentation simple de {@link NLSCode}.
 */
public class SimpleNLSCode implements NLSCode {

  private String bundle;
  private String code;

  public SimpleNLSCode(String bundle, String code) {
    this.bundle = bundle;
    this.code = code;
  }

  @Override
  public String getBundle() {
    return bundle;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SimpleNLSCode that = (SimpleNLSCode) o;

    if (bundle != null ? !bundle.equals(that.bundle) : that.bundle != null) {
      return false;
    }
    if (code != null ? !code.equals(that.code) : that.code != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = bundle != null ? bundle.hashCode() : 0;
    result = 31 * result + (code != null ? code.hashCode() : 0);
    return result;
  }
}
