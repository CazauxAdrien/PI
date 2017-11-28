package com.efluid.ecore.courbe.type;

import java.util.*;

import com.imrglobal.framework.type.EnumType;
import com.imrglobal.framework.utils.DateUtils;

import com.hermes.arc.commun.type.HermesNLSEnumType;

import com.efluid.ecore.temps.type.EPasTemps;

/**
 * Cette classe représente les durées possibles des partitions valeurs
 */
public class EDureePartition extends HermesNLSEnumType {

  private static final int _MOIS = 0;
  public static final EDureePartition MOIS = new EDureePartition(_MOIS);
  private static final int _TRIMESTRE = 1;
  public static final EDureePartition TRIMESTRE = new EDureePartition(_TRIMESTRE);
  private static final int _ANNEE = 2;
  public static final EDureePartition ANNEE = new EDureePartition(_ANNEE);
  private static final int _DEUX_ANNEES = 3;
  public static final EDureePartition DEUX_ANNEES = new EDureePartition(_DEUX_ANNEES);

  private EDureePartition(int v) {
    setValue(v);
  }

  public static EnumType from_int(int value) {
    switch (value) {
      case _MOIS:
        return MOIS;
      case _TRIMESTRE:
        return TRIMESTRE;
      case _ANNEE:
        return ANNEE;
      case _DEUX_ANNEES:
        return DEUX_ANNEES;
      default:
        return null;
    }
  }

  @Override
  public Iterator<EDureePartition> iterator() {
    List<EDureePartition> l = new ArrayList<EDureePartition>();

    l.add(MOIS);
    l.add(TRIMESTRE);
    l.add(ANNEE);
    l.add(DEUX_ANNEES);

    return l.iterator();
  }

  /**
   * Retourne une date sans temps pour la première journée de la période contenant cette date. Par exemple, si la durée de la partition est à un mois, et que la date passée en argument est le 12
   * février 2010 12:00, alors la date retournée sera le 1 février 2010 00:00.
   */
  public Date obtenirDateDebutPeriode(Date date) {
    switch (getValue()) {
      case _MOIS:
        return EPasTemps.MENSUEL.obtenirDateDebutPeriode(date);
      case _TRIMESTRE:
        return EPasTemps.TRIMESTRIEL.obtenirDateDebutPeriode(date);
      case _ANNEE:
        return EPasTemps.ANNUEL.obtenirDateDebutPeriode(date);
      case _DEUX_ANNEES:
        /* on met le début aux années paires */
        return DateUtils.getDate(DateUtils.getYear(date) - (DateUtils.getYear(date) % 2), 1, 1);
      default:
        return null;
    }
  }

  /** Retourne la première date de la prochaine période pour cette durée. */
  public Date prochaineDate(Date date) {
    return ajouterDureeADate(date, 1);
  }

  /** Retourne la date précédente pour cette durée. */
  public Date obtenirDatePrecedente(Date date) {
    return ajouterDureeADate(date, -1);
  }

  private Date ajouterDureeADate(Date date, int nombreApplicationsDuree) {
    switch (getValue()) {
      case _MOIS:
        return DateUtils.addMonths(date, 1 * nombreApplicationsDuree);
      case _TRIMESTRE:
        return DateUtils.addMonths(date, 3 * nombreApplicationsDuree);
      case _ANNEE:
        return DateUtils.addYears(date, 1 * nombreApplicationsDuree);
      case _DEUX_ANNEES:
        return DateUtils.addYears(date, 2 * nombreApplicationsDuree);
      default:
        return null;
    }
  }
}