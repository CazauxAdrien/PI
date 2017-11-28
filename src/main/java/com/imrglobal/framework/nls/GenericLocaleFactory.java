package com.imrglobal.framework.nls;

/**
 * Implementation of {@link LocaleFactory} using value of parameters in {@code framework.properties} to specified the country and the variant to use
 * <ul>
 * <li>{@code DEFAULT_COUNTRY} : specified the country</li>
 * <li>{@code DEFAULT_LOCALE_VARIANT} : specified the variant</li>
 * </ul>
 */
public class GenericLocaleFactory extends AbstractLocaleFactory {

  public static final String DEFAULT_COUNTRY_PARAM = "DEFAULT_COUNTRY";
  public static final String DEFAULT_LOCALE_VARIANT_PARAM = "DEFAULT_LOCALE_VARIANT";

  @Override
  protected String getCountry(Language lg) {
    return null;
  }

  @Override
  protected String getVariant(Language lg) {
    return null;
  }

  /**
   * Returns the default value for country to use if the parameter is not specified.
   * 
   * @return {@code null}
   */
  protected String defaultCountry() {
    return null;
  }

  /**
   * Returns the default value for variant to use if the parameter is not specified
   * 
   * @return {@code null}
   */
  protected String defaultVariant() {
    return null;
  }
}
