package com.imrglobal.framework.nls;

/**
 * This interface is used for the internationalisation of static ressources, through teh use of ResourceBundle (properties file or static classes)
 */
public interface Internationalizable {

  /**
   * Returns the fully qualified Java Name of the resource bundle managing the internationalisation of static attributes.
   * 
   * @return "fully qualified Java Name" of the ResourceBundle
   */
  public String getBundleBaseName();
}