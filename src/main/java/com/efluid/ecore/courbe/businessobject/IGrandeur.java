package com.efluid.ecore.courbe.businessobject;

import java.util.*;

import com.imrglobal.framework.type.Period;

import com.efluid.ecore.commun.utils.EcoreComparators;
import com.efluid.ecore.temps.businessobject.*;

/**
 * Cette interface représente une grandeur sous forme d'une liste de valeurs.
 */
public interface IGrandeur extends IPossedePeriodeEtBaseTemps, IPossedeValeurs<Collection<Valeur>> {


  /**
   * Ajoute cette valeur à la grandeur, en mettant à jour les différentes structures qui contiennent des valeurs, et met à jour la période.
   *
   */
  public Valeur addToValeurs(Valeur valeur);

  /**
   * Ajoute ces valeurs à la grandeur, en mettant à jour les différentes structures qui contiennent des valeurs, et met à jour la période.
   *
   */
  public void addAllToValeurs(Collection<? extends Valeur> valeurs);

  /**
   * Supprime cette valeur de la grandeur, en mettant à jour les différentes structures qui contiennent des valeurs, et met à jour la période.
   *
   * <p>
   * <strong>Attention :</strong> Il est impossible de supprimer une valeur lors de l'itération sur la structure de valeurs (même si c'est une sous structure, via subMap() ou values() par exemple).
   * Voir 74605. <strong>Le code suivant est erroné :</strong>
   *
   * <pre>
   * // BRISÉ -- ne fonctionne pas
   * for (Valeur valeur : grandeur.getValeurs().subMap(dateDebut, dateFin)) {
   *   this.grandeur.removeFromValeurs(valeur);
   * }
   * </pre>
   *
   * Il faut utiliser la méthode {@link #removeFromValeurs(Collection)}.
   *
   * @see #removeFromValeurs(Collection)
   */
  public Valeur removeFromValeurs(Valeur valeur);

  /**
   * Supprime ces valeurs de la grandeur, en mettant à jour les différentes structures qui contiennent des valeurs, et met à jour la période.
   *
   * <p>
   * Pour palier au manque de la méthode {@link #removeFromValeurs(Valeur)}, cette méthode peut-être utilisée de la manière suivante :
   *
   * <pre>
   * Collection<Valeur> valeursASupprimer = grandeur.getValeurs().subMap(dateDebut, dateFin).values());
   * grandeur.removeFromValeurs(valeursASupprimer);
   * </pre>
   *
   * Il n'est pas garanti que les valeurs seront supprimées dans le même ordre que les valeurs dans la liste passé en argument, mais il est garanti qu'elles le seront toutes.
   *
   * @throws NullPointerException si valeurs est nul
   */
  public Collection<Valeur> removeFromValeurs(Collection<Valeur> valeur);

  /**
   * Supprime toutes les valeurs de la grandeur, en mettant à jour les différentes structures qui contiennent des valeurs, et met à jour la période.
   *
   * @deprecated utiliser {@link #clearAllValeurs()} pour supprimer toutes les valeurs et les structures correspondantes ou {@link #clearValeursSimple()} pour supprimer seulement les valeurs
   */
  @Deprecated
  public void clearValeurs();

  /** Supprime toutes les valeurs de la grandeur, en mettant à jour les différentes structures qui contiennent des valeurs, et met à jour la période. */
  public void clearAllValeurs();

  /** Supprime toutes les valeurs de la grandeur seulement, sans le reste, et met à jour la période. */
  public void clearValeursSimple();

  /**
   * Supprime toutes les valeurs de la grandeur seulement, sans le reste, et sans mettre à jour la période.
   * Present principalement pour etre sur que cela ne provoque pas de régression pour la 2.01 d'enercom lorsque l'on decharge
   * les partions de valeur
   */
  public void clearValeursUniquement();
  
  /**
   * Retourne les valeurs.
   *
   * @return les valeurs associées à la grandeur.
   */
  public SortedMap<Date, Valeur> getValeurs();

  /**
   * Retourne la courbe.
   *
   * @return the courbe
   */
  public Courbe getCourbe();

  /**
   * Met cette courbe.
   *
   * @param courbe the courbe to set
   */
  public void setCourbe(Courbe courbe);

  /**
   * Retourne la base de temps.
   *
   * @return the baseTemps
   * @deprecated utiliser {@link #getBaseTempsTransitif(ModeleCourbe)} ou {@link #getBaseTempsNonTransitif()}, cette méthode ne devrait utilisée que par l'archi pour persister le champs baseTemps
   */
  @Deprecated
  public BaseTemps getBaseTemps();

  /**
   * Retourne la base de temps de manière non transitive, donc retourne le champs base temps de la grandeur.
   *
   * @return the baseTemps
   */
  @Override
public BaseTemps getBaseTempsNonTransitif();


  /**
   * Met cette base de temps.
   *
   * @param baseTemps the baseTemps to set
   */
  public void setBaseTemps(BaseTemps baseTemps);

  /** @return the selectionnable */
  public boolean isSelectionnable();

  /**
   * Retourne la période.
   *
   * @return the période
   */
  @Override
public Period getPeriode();

  /**
   * Cette méthode retourne la période sur laquelle des valeurs sont définies pour la grandeur. Cette méthode diffère de la méthode {@link getPeriode()} dans le sens où elle étend la période des pas
   * de temps de chaque grandeur.
   *
   */
  public Period getPeriodeAffichage();

  /**
   * Met cette période.
   *
   * @param period the période to set
   */
  public void setPeriode(Period periode);

  /**
   * Retourne les valeurs calculées.
   *
   * @return les valeurs calculées associées à la grandeur.
   */
  public ValeursCalculees getValeursCalculees();

  /**
   * Met ces valeurs calculées.
   *
   * @param valeursCalculees the valeursCalculees to set
   */
  public void setValeursCalculees(ValeursCalculees valeursCalculees);

}
