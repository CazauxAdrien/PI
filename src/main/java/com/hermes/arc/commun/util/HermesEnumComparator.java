package com.hermes.arc.commun.util;

import com.imrglobal.framework.utils.EnumComparator;
import com.imrglobal.framework.nls.Language;
import com.imrglobal.framework.type.EnumType;
import com.hermes.arc.commun.type.HermesNLSEnumType;

import java.util.Comparator;

/**
 * Permet de comparer deux énumérés selon leurs libellés.
 */
public class HermesEnumComparator<E extends EnumType> extends EnumComparator<E> {

  private boolean longLabel;

  public static final int SHORT_ALPHABETIC_ASC = 4;
  public static final int SHORT_ALPHABETIC_DESC = 5;

  public static final String STR_SHORT_ASC = "SHORT_ASC";
  public static final String STR_SHORT_DESC = "SHORT_DESC";

  public HermesEnumComparator(int mode) {
    this(mode == SHORT_ALPHABETIC_ASC ? ALPHABETIC_ASC : mode == SHORT_ALPHABETIC_DESC ? ALPHABETIC_DESC : mode,mode != SHORT_ALPHABETIC_ASC && mode != SHORT_ALPHABETIC_DESC);
  }

  public HermesEnumComparator(int mode, boolean longLabel) {
    super(mode);
    this.longLabel = longLabel;
  }

  public HermesEnumComparator(int mode, Language language, boolean longLabel) {
    super(mode, language);
    this.longLabel = longLabel;
  }

  public HermesEnumComparator(Comparator<? super E> parent, int mode, Language language, boolean longLabel) {
    super(parent, mode, language);
    this.longLabel = longLabel;
  }

  public boolean isLongLabel() {
    return longLabel;
  }

  @Override
  protected String getStringToCompare(EnumType forThisEnum) {
    String res;
    if ((isLongLabel()) || (!(forThisEnum instanceof HermesNLSEnumType))) {
      res = super.getStringToCompare(forThisEnum);
    } else {
      res = ((HermesNLSEnumType)forThisEnum).getShortLabel(getLanguage());
    }
    return res;
  }

  /**
   * Crée un comparateur pour le type de tri spécifié, dans la langue indiquée
   * @param sortMode le type de tri. Une des constantes suivantes : <ul>
   * <li>{@link #STR_ALPHABETIC_ASC} : tri ascendant sur le libellé long
   * <li>{@link #STR_ALPHABETIC_DESC} : tri descendant sur le libellé long
   * <li>{@link #STR_SHORT_ASC} : tri ascendant sur le libellé court
   * <li>{@link #STR_SHORT_DESC} : tri descendant sur le libellé court
   * <li>{@link #STR_VALUE_ASC} : tri ascendant sur la valeur interne
   * <li>{@link #STR_VALUE_DESC} : tri descendant sur la valeur interne
   * </ul>
   * @param lg la langue
   * @return une instance de {@link com.hermes.arc.commun.util.HermesEnumComparator}
   */
  public static HermesEnumComparator<? extends EnumType> newComparator(String sortMode, Language lg) {
    if (STR_SHORT_ASC.equals(sortMode)) {
      return new HermesEnumComparator<>(ALPHABETIC_ASC,lg, false);
    }
    if (STR_SHORT_DESC.equals(sortMode)) {
      return new HermesEnumComparator<>(ALPHABETIC_DESC,lg, false);
    }
    return new HermesEnumComparator<>(getSortMode(sortMode), lg, true);
  }

  public static int getSortMode(String sMode) {
    if (STR_SHORT_ASC.equalsIgnoreCase(sMode)) {
      return SHORT_ALPHABETIC_ASC;
    } else if (STR_SHORT_DESC.equalsIgnoreCase(sMode)) {
      return SHORT_ALPHABETIC_DESC;
    } else {
      return EnumComparator.getSortMode(sMode);
    }
  }

}
