package com.efluid.ecore.temps.businessobject;

import com.efluid.ecore.commun.businessobject.IPossedePeriode;

/**
 * Cette interface définit un objet qui possède une période et une base de temps.
 */
public interface IPossedePeriodeEtBaseTemps extends IPossedePeriode {

  /** Cette méthode retourne la base de temps. */
  public BaseTemps getBaseTempsNonTransitif();

}
