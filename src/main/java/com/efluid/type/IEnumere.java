package com.efluid.type;

import java.util.Collection;

/**
 * Type énuméré qui contient ses valeurs.
 */
public interface IEnumere {

  /**
   * @return le code de l'énuméré, le {@link Class#getSimpleName()} pour un énuméré statique.
   */
  public String getCode();

  /**
   * @return le libellé de l'énuméré.
   */
  public String getLibelle();

  /**
   * @return les valeurs de l'énuméré.
   */
  public Collection getValeurs();

}
