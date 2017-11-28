package com.efluid.ecore.courbe.utils;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import com.imrglobal.framework.type.Period;

import com.efluid.ecore.courbe.businessobject.*;
import com.efluid.ecore.courbe.type.EDureePartition;
import com.efluid.ecore.temps.type.EPasTemps;

/**
 * map implémentant SortedMap<Date, Valeur> permettant de stocker les valeurs dans un tableau dont les indices représentent l'évolution temporelle (en fonction d'un pas de temps et une date de début
 * donnée)
 */
public class MapOptimiseeValeursTemporelles implements SortedMap<Date, Valeur>, Serializable {

  private ValeurInterne[] valeurs = null;
  private EPasTemps pasTemps;
  private Date dateDebutPartition;
  private EDureePartition dureePartition;
  private int indexDebut;
  private int indexFin;
  private int compteurModifications = 0;

  private int tailleMap = 0;

  public MapOptimiseeValeursTemporelles(Date dateAnneeDebut, EPasTemps pasTemps, EDureePartition dureePartition) {
    Date date = pasTemps.obtenirDateDebutPeriode(dateAnneeDebut);
    if (!date.equals(dateAnneeDebut)) {
      dateAnneeDebut = pasTemps.obtenirProchaineDate(date);
    }

    this.dateDebutPartition = dateAnneeDebut;
    this.pasTemps = pasTemps;
    this.dureePartition = dureePartition;
    int nbValeurMaxDansAnnee = getPositionBrutDansTableau(dureePartition.prochaineDate(dateAnneeDebut), true);
    valeurs = new ValeurInterne[nbValeurMaxDansAnnee];
    this.indexDebut = 0;
    this.indexFin = nbValeurMaxDansAnnee - 1;
  }

  public Stream<ValeurInterne> getStream() {
    return Arrays.stream(valeurs, indexDebut, indexFin + 1);
  }

  public Stream<ValeurInterne> operationsValeurs(BinaryOperator<ValeurInterne> fonction, MapOptimiseeValeursTemporelles mapAConsommer) {
    return IntStream.range(0, valeurs.length).mapToObj(i -> fonction.apply(valeurs[i], mapAConsommer.valeurs[i]));
  }

  public MapOptimiseeValeursTemporelles(ValeurInterne[] valeurs, Date dateAnneeDebut, EPasTemps pasTemps, EDureePartition dureePartition) {
    this.dateDebutPartition = dateAnneeDebut;
    this.pasTemps = pasTemps;
    this.dureePartition = dureePartition;
    this.valeurs = valeurs;
    this.indexDebut = 0;
    this.indexFin = valeurs.length - 1;
  }

  public ValeurInterne[] getTableauValeurs() {
    return this.valeurs;
  }

  protected int getCompteurModifications() {
    return compteurModifications;
  }

  protected int incrementerCompteurModifications() {
    return ++compteurModifications;
  }

  @Override
  public int size() {
    return this.tailleMap;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    if (get(key) != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    if (value != null) {
      Valeur valeur = (Valeur) value;
      if (valeur.getDate() != null) {
        return containsKey(valeur.getDate());
      }
    }
    return false;
  }

  @Override
  public Valeur get(Object key) {
    if (key instanceof Date) {
      Date date = (Date) key;
      return getValeurDePosition(getPositionDansTableau(date, false), date);
    }
    return null;
  }

  private Valeur getValeurDePosition(int positionDansTableau, Date date) {
    if (positionDansTableau != -1) {
      ValeurInterne enveloppeDouble = this.valeurs[positionDansTableau];
      if (enveloppeDouble != null) {
        return enveloppeDouble.creerValeur(new ReferenceTemporelle(date));
      }
    }
    return null;
  }

  protected int getPositionDansTableau(Date date, boolean avecLeverExceptionSiDateHorsTableau) {
    return getPositionDansTableau(date, avecLeverExceptionSiDateHorsTableau, false);
  }

  protected int getPositionDansTableau(Date date, boolean avecLeverExceptionSiDateHorsTableau, boolean sansDateExacte) {
    if (date != null) {
      int positionDansTableau = getPositionBrutDansTableau(date, sansDateExacte);
      if (positionDansTableau >= this.indexDebut && positionDansTableau <= this.indexFin) {
        return positionDansTableau;
      } else if (avecLeverExceptionSiDateHorsTableau && positionDansTableau != -1) {
        throw new IllegalArgumentException("key out of range");
      }
    }
    return -1;
  }

  private int getPositionBrutDansTableau(Date date, boolean sansDateExacte) {
    if (date != null) {
      int positionDansTableau = (int) ((date.getTime() - this.dateDebutPartition.getTime()) / this.pasTemps.getDuree().getGap());
      Date dateObtenu = this.pasTemps.obtenirProchaineDate(this.dateDebutPartition, positionDansTableau);
      if (dateObtenu.getTime() == date.getTime() || sansDateExacte || this.pasTemps.obtenirDateDebutPeriode(date).getTime() == date.getTime()) {
        return positionDansTableau;
      }
    }
    return -1;
  }

  @Override
  public Valeur put(Date key, Valeur value) {
    int positionDansTableau = getPositionDansTableau(key, true);
    if (positionDansTableau != -1) {
      Valeur ancienneValeur = getValeurDePosition(positionDansTableau, key);
      this.valeurs[positionDansTableau] = value != null ? value.getEnveloppeDouble() : null;
      if (ancienneValeur == null) {
        tailleMap++;
      }
      compteurModifications++;
      return ancienneValeur;
    }

    return null;
  }

  protected void putEnveloppe(int position, ValeurInterne enveloppeDouble) {
    this.valeurs[position] = enveloppeDouble;
    if (enveloppeDouble != null) {
      this.tailleMap++;
    }
  }

  @Override
  public Valeur remove(Object key) {
    int positionDansTableau = getPositionDansTableau((Date) key, false);
    if (positionDansTableau != -1) {
      Valeur ancienneValeur = getValeurDePosition(positionDansTableau, (Date) key);
      this.valeurs[positionDansTableau] = null;
      if (ancienneValeur != null) {
        tailleMap--;
        compteurModifications++;
      }
      return ancienneValeur;
    }
    return null;
  }

  @Override
  public void putAll(Map<? extends Date, ? extends Valeur> m) {
    for (Valeur valeur : m.values()) {
      if (valeur != null) {
        this.put(valeur.getDate(), valeur);
      }
    }
  }

  @Override
  public void clear() {
    for (int i = this.indexDebut; i <= this.indexFin; i++) {
      this.valeurs[i] = null;
    }
    compteurModifications++;
    tailleMap = 0;
  }

  @Override
  public Comparator<? super Date> comparator() {
    return new ComparateurValeurTemporelle<>();
  }

  @Override
  public SortedMap<Date, Valeur> subMap(Date fromKey, Date toKey) {
    return subMap(fromKey, toKey, true);
  }

  public SortedMap<Date, Valeur> subMap(Date fromKey, Date toKey, boolean avecControle) {
    if (avecControle && fromKey.compareTo(toKey) > 0) {
      throw new IllegalArgumentException("fromKey > toKey");
    }

    fromKey = recalerDateDebut(fromKey);
    toKey = recalerDateFin(toKey);

    int positionDebut = getPositionBrutDansTableau(fromKey, false);
    int positionFin = getPositionBrutDansTableau(toKey, false);

    return initialiserSubMapFonctionPositionTableau(positionDebut, positionFin);

  }

  private SortedMap<Date, Valeur> initialiserSubMapFonctionPositionTableau(int positionDebut, int positionFin) {
    if (positionDebut < this.indexDebut) {
      positionDebut = this.indexDebut;
    }
    if (positionFin > this.indexFin) {
      positionFin = this.indexFin;
    }
    if (positionDebut == this.indexDebut && positionFin == this.indexFin) {
      return this;
    }

    return new SubMapValeursTrieesOptimisee(this.dateDebutPartition, this.pasTemps, this.dureePartition, this, positionDebut, positionFin);
  }

  private Date recalerDateFin(Date toKey) {
    if (toKey != null) {
      Date date = pasTemps.obtenirDateDebutPeriode(toKey);
      if (!date.equals(toKey)) {
        toKey = date;
      } else {
        toKey = pasTemps.obtenirDatePrecedente(date);
      }
    }
    return toKey;
  }

  private Date recalerDateDebut(Date fromKey) {
    if (fromKey != null) {
      Date date = pasTemps.obtenirDateDebutPeriode(fromKey);
      if (!date.equals(fromKey)) {
        fromKey = pasTemps.obtenirProchaineDate(date);
      }
    }
    return fromKey;
  }

  @Override
  public SortedMap<Date, Valeur> headMap(Date toKey) {
    if (toKey == null) {
      throw new NullPointerException();
    }
    return subMap(this.dateDebutPartition, toKey, false);
  }

  @Override
  public SortedMap<Date, Valeur> tailMap(Date fromKey) {
    if (fromKey == null) {
      throw new NullPointerException();
    }
    return subMap(fromKey, this.dureePartition.prochaineDate(dateDebutPartition), false);
  }

  @Override
  public Date firstKey() {
    if (!isEmpty()) {
      for (int i = this.indexDebut; i <= this.indexFin; i++) {
        if (this.valeurs[i] != null) {
          return this.pasTemps.obtenirProchaineDate(this.dateDebutPartition, i);
        }
      }
    }
    throw new NoSuchElementException();
  }

  @Override
  public Date lastKey() {
    if (!isEmpty()) {
      for (int i = this.indexFin; i >= this.indexDebut; i--) {
        if (this.valeurs[i] != null) {
          return this.pasTemps.obtenirProchaineDate(this.dateDebutPartition, i);
        }
      }
    }
    throw new NoSuchElementException();
  }

  @Override
  public Set<Date> keySet() {
    return new SetOrdonneValeursTemporelles<>(this, new ConvertisseurMapValeur<>(Date.class));
  }

  @Override
  public Collection<Valeur> values() {
    return new SetOrdonneValeursTemporelles<>(this, new ConvertisseurMapValeur<>(Valeur.class));
  }

  @Override
  public Set<java.util.Map.Entry<Date, Valeur>> entrySet() {
    Set<? extends java.util.Map.Entry<Date, Valeur>> entrySet = new SetOrdonneValeursTemporelles<>(this, new ConvertisseurMapValeur<>(EntryDateValeur.class));
    return (Set<java.util.Map.Entry<Date, Valeur>>) entrySet;
  }

  static class SetOrdonneValeursTemporelles<E> extends AbstractSetValeursTemporelles<E> {
    SetOrdonneValeursTemporelles(SortedMap<Date, Valeur> map, ConvertisseurMapValeur<E> convertisseur) {
      super(map, convertisseur);
    }

    @Override
    public Iterator<E> iterator() {
      return new IterateurElementsOrdonnes<>((MapOptimiseeValeursTemporelles) mapValeur, convertisseur);
    }
  }

  static final class IterateurElementsOrdonnes<E> implements Iterator<E> {
    private int elementCourant = 0;
    private int nbElementsParcourus = 0;
    private final MapOptimiseeValeursTemporelles map;
    private final ConvertisseurMapValeur<E> convertisseur;
    private int nbModificationsAttendues;
    private boolean hasnext;

    public IterateurElementsOrdonnes(MapOptimiseeValeursTemporelles map, ConvertisseurMapValeur<E> convertisseur) {
      this.map = map;
      this.convertisseur = convertisseur;
      elementCourant = map.getIndexDebut();
      nbModificationsAttendues = map.getCompteurModifications();
      definirHasNext();
    }

    @Override
    public boolean hasNext() {
      return hasnext;
    }

    public void definirHasNext() {
      if (nbElementsParcourus < map.size()) {
        hasnext = true;
      } else {
        hasnext = false;
      }
    }

    @Override
    public E next() {
      if (map.getCompteurModifications() != nbModificationsAttendues) {
        throw new ConcurrentModificationException();
      }
      for (; this.elementCourant <= map.getIndexFin(); this.elementCourant++) {
        ValeurInterne enveloppeDouble = map.valeurs[elementCourant];
        if (enveloppeDouble != null) {
          Valeur valeur = map.getValeurDePosition(this.elementCourant, map.pasTemps.obtenirProchaineDate(map.dateDebutPartition, this.elementCourant));

          this.elementCourant++;
          this.nbElementsParcourus++;
          definirHasNext();
          return convertisseur.getObjetAttenduDeValeur(valeur);
        }
      }
      definirHasNext();
      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      if (map.getCompteurModifications() != nbModificationsAttendues) {
        throw new ConcurrentModificationException();
      }
      if (map.valeurs[elementCourant] != null) {
        map.valeurs[elementCourant] = null;
        map.tailleMap--;
        nbModificationsAttendues = map.incrementerCompteurModifications();
      }
      definirHasNext();
    }
  }

  public int getIndexDebut() {
    return indexDebut;
  }

  public void setIndexDebut(int indexDebut) {
    this.indexDebut = indexDebut;
  }

  public int getIndexFin() {
    return indexFin;
  }

  public void setIndexFin(int indexFin) {
    this.indexFin = indexFin;
  }

  public void setTailleMap(int tailleMap) {
    this.tailleMap = tailleMap;
  }

  static final class SubMapValeursTrieesOptimisee extends MapOptimiseeValeursTemporelles {
    public SubMapValeursTrieesOptimisee(Date dateAnneeDebut, EPasTemps pasTemps, EDureePartition dureePartition, MapOptimiseeValeursTemporelles mapValeursTrieesOptimisee, int indexDebut,
        int indexFin) {
      super(mapValeursTrieesOptimisee.valeurs, dateAnneeDebut, pasTemps, dureePartition);
      setIndexDebut(indexDebut);
      setIndexFin(indexFin);
      setTailleMap(getTailleMap());
    }

    private int getTailleMap() {
      int size = 0;
      for (int i = getIndexDebut(); i <= getIndexFin(); i++) {
        if (getTableauValeurs()[i] != null) {
          size++;
        }
      }
      return size;
    }
  }

  @Override
  public String toString() {
    Iterator<Entry<Date, Valeur>> i = entrySet().iterator();
    if (!i.hasNext()) {
      return "{}";
    }
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (;;) {
      Entry<Date, Valeur> e = i.next();
      Date key = e.getKey();
      Valeur value = e.getValue();
      sb.append(key).append('=').append(value);
      if (!i.hasNext()) {
        return sb.append('}').toString();
      }
      sb.append(',').append(' ');
    }
  }

  public EPasTemps getPasTemps() {
    return pasTemps;
  }

  public void copierTableau(ValeurInterne[] tableauACopier) {
    IntStream.range(0, valeurs.length).forEach(i -> valeurs[i] = tableauACopier[i] != null ? new ValeurInterne(tableauACopier[i].getValeur()) : null);
  }

  public ValeurInterne[] getValeurs() {
    return valeurs;
  }

  /**
   * Applique une fonction aux deux partitions de valeur en paramètre valeur par valeur sur la periode de calcul afin de mettre à jour le tableau de ValeurInterne cette
   * {@link MapOptimiseeValeursTemporelles}.
   */
  public void appliquerFonction(PartitionValeurs partitionValeurs0, PartitionValeurs partitionValeurs1, BinaryOperator<ValeurInterne> operation, Period periodeCalcul) {
    int indexDebutCalcul = this.indexDebut;
    int indexFinCalcul = this.indexFin + 1;
    if (periodeCalcul != null) {
      if (periodeCalcul.getStartOfPeriod() != null) {
        indexDebutCalcul = getPositionBrutDansTableau(periodeCalcul.getStartOfPeriod(), true);
        indexDebutCalcul += this.pasTemps.isDateComplete(periodeCalcul.getStartOfPeriod()) ? 0 : 1;
        indexDebutCalcul = Math.max(indexDebut, indexDebutCalcul);
      }
      if (periodeCalcul.getEndOfPeriod() != null) {
        indexFinCalcul = getPositionBrutDansTableau(periodeCalcul.getEndOfPeriod(), true) + 1;
        indexFinCalcul = Math.min(indexFin + 1, indexFinCalcul);
      }
    }
    if (indexDebutCalcul <= indexFinCalcul && indexDebutCalcul < indexFin && indexFinCalcul > indexDebut) {
      appliquerFonctionSurTableauValeurInterne(partitionValeurs0, partitionValeurs1, indexDebutCalcul, indexFinCalcul, operation);
    }
  }

  private void appliquerFonctionSurTableauValeurInterne(PartitionValeurs partitionValeurs0, PartitionValeurs partitionValeurs1, int positionIndexDebutCalcul, int positionIndexFinCalcul,
      BinaryOperator<ValeurInterne> operation) {
    ValeurInterne[] valeurs0 = Optional.ofNullable(partitionValeurs0).map(p -> ((MapOptimiseeValeursTemporelles) p.getValeurs()).valeurs).orElse(null);
    ValeurInterne[] valeurs1 = Optional.ofNullable(partitionValeurs1).map(p -> ((MapOptimiseeValeursTemporelles) p.getValeurs()).valeurs).orElse(null);
    IntConsumer operationMiseAJourValeur = getConsommateurValeur(valeurs0, valeurs1, operation);
    IntStream.range(positionIndexDebutCalcul, positionIndexFinCalcul).forEach(i -> appliquerOperationUnitaire(i, operationMiseAJourValeur));
  }

  private void appliquerOperationUnitaire(int i, IntConsumer operationMiseAJourValeur) {
    ValeurInterne ancienneValeur = valeurs[i];
    operationMiseAJourValeur.accept(i);
    if (ancienneValeur == null && valeurs[i] != null) {
      tailleMap++;
    } else if (ancienneValeur != null && valeurs[i] == null) {
      tailleMap--;
    }
  }

  private IntConsumer getConsommateurValeur(ValeurInterne[] valeurs0, ValeurInterne[] valeurs1, BinaryOperator<ValeurInterne> operation) {
    return i -> valeurs[i] = operation.apply(valeurs0 != null ? valeurs0[i] : null, valeurs1 != null ? valeurs1[i] : null);
  }
}
