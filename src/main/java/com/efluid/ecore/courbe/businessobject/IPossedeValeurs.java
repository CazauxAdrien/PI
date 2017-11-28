package com.efluid.ecore.courbe.businessobject;

import java.util.Collection;

/**
 * Cette interface encapsule les éléments qui possèdent des valeurs.
 *
 * @param <T> le type de collection qui contient les valeurs
 */
public interface IPossedeValeurs<T extends Collection<Valeur>> {

  /**
   * Retourne le minimum. Cette méthode devrait être appelée en faveur de {@link java.util.Collections#min(Collection)} puisqu'elle risque fortement d'être plus rapide.
   *
   * @return le minimum ou {@link com.imrglobal.framework.utils.UndefinedAttributes#UNDEF_DOUBLE} si cette valeur n'existe pas
   */
  public double getMin();

  /**
   * Retourne le maximum. Cette méthode devrait être appelée en faveur de {@link java.util.Collections#max(Collection)} puisqu'elle risque fortement d'être plus rapide.
   *
   * @return le maximum ou {@link com.imrglobal.framework.utils.UndefinedAttributes#UNDEF_DOUBLE} si cette valeur n'existe pas
   */
  public double getMax();

  /**
   * Retourne les valeurs de base.
   *
   */
  public T getValeursBase();

}
