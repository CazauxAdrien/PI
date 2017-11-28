package com.imrglobal.framework.utils;

import java.text.Collator;
import java.text.RuleBasedCollator;

/**
 * Utility used to handle {@link Collator} services
 */
public class CollatorUtils {

  private static final RuleBasedCollator DEFAULT_COLLATOR;


  static {
    Collator c = Collator.getInstance();
    RuleBasedCollator defCol = null;
    try {
      boolean adjustCollator = c.compare("des t", "de t") < 0;
      if (adjustCollator && c instanceof RuleBasedCollator) {

        String defRules = ((RuleBasedCollator) c).getRules().replace("<'\u005f'", "<' '<'\u005f'");
        defCol = new RuleBasedCollator(defRules);

      }
    } catch (Exception e) {
      defCol = null;
    }
    DEFAULT_COLLATOR = defCol;
  }

  /**
   * Gets the Collator for the current default locale.
   * <br>Use instead of {@link java.text.Collator#getInstance()} to bypass the problem with space</br>
   *
   * @return the Collator for the current default locale.
   */
  public static final Collator getInstance() {
    if (DEFAULT_COLLATOR != null) {
      return (Collator) DEFAULT_COLLATOR.clone();
    }
    return Collator.getInstance();
  }

}
