package com.imrglobal.framework.nls;

/**
 * Représente un code multilingue.
 * 
 * Exemple d'utilisation :
 * 
 * <pre>
 * private enum MonEnumereLabels implements NLSCode {
 *   MA_PREMIERE_VALEUR,
 *   MA_DEUXIEME_VALEUR;
 *
 *   &#64;Override
 *   public String getBundle() {
 *     return BUNDLE;
 *   }
 *
 *   &#64;Override
 *   public String getCode() {
 *     return name();
 *   }
 * }
 * </pre>
 * 
 * Pour un besoin "standard", respecter les 2 règles suivantes :
 * <ul>
 * <li>Suffixer l'énuméré par : <code>Labels</code></li>
 * <li>Ranger l'énuméré dans le package : <code>[monDomaine].nls</code></li>
 * </ul>
 * 
 * Pour la gestion d'énuméré d'erreur, utiliser la classe fille : {@link com.efluid.framework.erreur.CodeErreur}.
 */
public interface NLSCode {

  /** @return le bundle où est défini le code */
  public String getBundle();

  /** @return le code */
  public String getCode();

  /**
   * @param params données dynamiques nécessaires à la traduction
   * @return la traduction du NLSCode avec la langue par défaut de l'application {@link Language#DEFAULT}
   */
  default String translate(Object... params) {
    return translate(Language.DEFAULT, params);
  }

  /**
   * @param language langue à utiliser pour la traduction
   * @param params données dynamiques nécessaires à la traduction
   * @return la traduction du NLSCode
   */
  default String translate(Language language, Object... params) {
    return NLS.translate(language, this, params);
  }
}
