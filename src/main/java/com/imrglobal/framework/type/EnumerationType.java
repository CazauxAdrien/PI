package com.imrglobal.framework.type;

import com.imrglobal.framework.nls.Language;

import com.efluid.type.IValeurEnumere;

/**
 * Super class of all kind of enumeration type (static or persistent).
 * 
 */
public abstract class EnumerationType implements IValeurEnumere {

  /**
   * Handle the dependence between this enumeration type and another enumeration type. Returns true if this value of the enumeration is possible for the specified type. <br>
   * This implementation returns always true. Override this method to implement a dependence of this type with another type.
   * 
   * @param type
   * @return true if this item is possible
   */
  public boolean isPossibleValue(EnumerationType type) {
    return true;
  }

  public abstract String toString(Language lg);

}
