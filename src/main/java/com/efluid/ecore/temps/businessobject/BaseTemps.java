package com.efluid.ecore.temps.businessobject;

import com.efluid.ecore.temps.type.EPasTemps;

/**
 * Cette classe représente une base de temps.
 */
public class BaseTemps  implements Comparable<BaseTemps> {

  private String reference;
  private String libelle;
  private EPasTemps pasTempsValeurs;


  public BaseTemps() {
    super();
  }


  /** @return the libelle */
  public String getLibelle() {
    return this.libelle;
  }

  /** @param libelle the libelle to set */
  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  /** @return the pasTempsValeurs */
  public EPasTemps getPasTempsValeurs() {
    return this.pasTempsValeurs;
  }


  public void setPasTempsValeurs(EPasTemps pasTempsValeurs) {
    this.pasTempsValeurs = pasTempsValeurs;
  }

  /** @return the reference */
  public String getReference() {
    return this.reference;
  }

  /** @param reference the reference to set */
  public void setReference(String reference) {
    this.reference = reference;
  }

  @Override
  public int compareTo(BaseTemps o) {
    return this.getPasTempsValeurs().compareTo(o.getPasTempsValeurs());
  }

}
