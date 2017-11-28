package com.efluid.type;

import java.io.Serializable;

import com.imrglobal.framework.type.*;

/**
 * Interface commune à tous les énumérés efluid.
 */
public interface IValeurEnumere extends Serializable {

  /**
   * @return le code de l'énuméré: {@link EnumType#getValue() value} pour l'énuméré non persistant, ou {@link PersistentEnumType#getItemCode() itemCode} pour les énumérés persistants.
   */
  public String getCodeValeur();

  /**
   * @return le libellé dit "long" de l'énuméré.
   */
  public String getLibelle();

  /**
   * @return le libellé court de l'énuméré.
   */
  public String getLibelleCourt();

  /**
   * @return l'ordre, {@link PersistentEnumType#getItemOrder() itemOrder}, pour un énuméré persistant, la {@link EnumType#getValue() value} pour l'énuméré statique.
   */
  public int getOrdre();

  /**
   * @return le type d'énuméré.
   */
  public default IEnumere getTypeEnumere() {
    return null;
  }

  /**
   * @return un pseudo-code globalement unique pour l'énuméré.
   */
  public default String getInternalPseudoCode() {
    return getClass().getName() + "#" + getCodeValeur();
  }

}
