package com.efluid.ecore.courbe.businessobject;

import java.util.*;

import com.imrglobal.framework.type.Period;

import com.efluid.ecore.commun.businessobject.IPossedePeriode;
import com.efluid.ecore.commun.utils.EcorePeriodUtils;

/**
 * Cette classe représente une courbe sous forme d'une liste de grandeurs.
 */
public abstract class Courbe implements IPossedePeriode{

  private String reference;
  private String libelle;

  private List<IGrandeur> grandeurs = new ArrayList<>();

  public Courbe() {
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public String getReference() {
    return this.reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  /** @deprecated Utiliser plutôt {@link Courbe#getGrandeursAsList()}. */
  @Deprecated
  public Set<IGrandeur> getGrandeurs() {
    Set<IGrandeur> grandeursAsSet = new HashSet<>();
    for (IGrandeur grandeur : this.grandeurs) {
      grandeursAsSet.add(grandeur);
    }
    return Collections.unmodifiableSet(grandeursAsSet);
  }

  public List<IGrandeur> getGrandeursAsList() {
    return Collections.unmodifiableList(this.grandeurs);
  }

  public boolean addToGrandeurs(IGrandeur grandeur) {
    if (grandeur != null) {
      grandeur.setCourbe(this);
      return !this.grandeurs.contains(grandeur) ? this.grandeurs.add(grandeur) : true;
    }
    return false;
  }

  public boolean addAllToGrandeurs(Collection<? extends IGrandeur> g) {
    if (null != g && !g.isEmpty()) {
      boolean retour = true;
      for (IGrandeur grandeur : g) {
        retour = retour && addToGrandeurs(grandeur);
      }
      return retour;
    }
    return false;
  }

  public boolean removeFromGrandeurs(IGrandeur grandeur) {
    if (grandeur != null) {
      return this.grandeurs.remove(grandeur);
    }
    return false;
  }

  public void clearGrandeurs() {
    this.grandeurs.clear();
  }

  /**
   * Cette méthode retourne la période sur laquelle des valeurs sont définies pour la courbe. S'il n'y a pas de grandeur, ça correspond à EcorePeriodUtils.getPeriodeInfinie(). Sinon, ça correspond à
   * la période [minimum des dates de début pour les grandeurs; maximum des dates de fin pour les grandeurs].
   */
  @Override
  public Period getPeriode() {
    return EcorePeriodUtils.getPeriode(getGrandeursAsList());
  }

  /**
   * Cette méthode retourne la période sur laquelle des valeurs sont définies pour la courbe. Cette méthode diffère de la méthode {@link Courbe#getPeriode()} dans le sens où elle étend la période des
   * pas de temps de chaque grandeur.
   */
  public Period getPeriodeAffichage() {
    return EcorePeriodUtils.getPeriodeAffichage(getGrandeursAsList());
  }
}