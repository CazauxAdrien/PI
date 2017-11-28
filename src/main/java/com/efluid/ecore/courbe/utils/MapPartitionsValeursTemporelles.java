package com.efluid.ecore.courbe.utils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

import com.efluid.ecore.courbe.businessobject.*;
import com.efluid.ecore.courbe.type.EDureePartition;
import com.efluid.ecore.temps.type.EPasTemps;

/**
 * implémentation de SortedMap<Date, Valeur> permettant de concatener les maps des partitions de valeur du champ partitionsValeurs de cette classe
 */
public class MapPartitionsValeursTemporelles implements SortedMap<Date, Valeur>, Serializable {
  private final SortedMap<Date, PartitionValeurs> partitionsValeurs;
  private final EDureePartition dureePartition;
  private final EPasTemps pasTemps;
  private int compteurModifications = 0;
  private final Grandeur grandeur;
  private final Date dateDebut;
  private final Date dateFin;

  public MapPartitionsValeursTemporelles(SortedMap<Date, PartitionValeurs> mapContientSortedMaps, EDureePartition dureePartition, EPasTemps pasTemps, Grandeur grandeur) {
    this.partitionsValeurs = mapContientSortedMaps;
    this.dureePartition = dureePartition;
    this.pasTemps = pasTemps;
    this.grandeur = grandeur;
    this.dateDebut = null;
    this.dateFin = null;
  }

  public MapPartitionsValeursTemporelles(SortedMap<Date, PartitionValeurs> mapContientSortedMaps, EDureePartition dureePartition, EPasTemps pasTemps, Grandeur grandeur, Date dateDebut, Date dateFin) {
    this.partitionsValeurs = mapContientSortedMaps;
    this.dureePartition = dureePartition;
    this.pasTemps = pasTemps;
    this.grandeur = grandeur;
    this.dateDebut = dateDebut;
    this.dateFin = dateFin;
  }

  public SortedMap<Date, PartitionValeurs> getMapContientSortedMaps() {
    return this.partitionsValeurs;
  }

  /**
   * récupère un stream concaténé des valeurs des partitions de valeur.
   */
  public Stream<ValeurInterne> stream() {
    return partitionsValeurs.values().stream().flatMap(p -> {
      if (p.getValeurs() instanceof MapOptimiseeValeursTemporelles) {
        return ((MapOptimiseeValeursTemporelles) p.getValeurs()).getStream();
      }
      return p.getValeurs().values().stream().map(Valeur::getEnveloppeDouble);
    });
  }

  @Override
  public int size() {
    int taille = 0;
    for (PartitionValeurs contientSortedMap : partitionsValeurs.values()) {
      if (contientSortedMap != null) {
        SortedMap<Date, Valeur> valeurs = contientSortedMap.getValeurs();
        if (valeurs != null) {
          taille += valeurs.size();
        }
      }
    }
    return taille;
  }

  protected int getCompteurModifications() {
    return compteurModifications;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    SortedMap<Date, Valeur> map = getMapPartitions(key);
    if (map != null) {
      return map.containsKey(key);
    }
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    if (value instanceof Valeur) {
      return containsKey(((Valeur) value).getDate());
    }
    return false;
  }

  @Override
  public Valeur get(Object key) {
    SortedMap<Date, Valeur> map = getMapPartitions(key);
    if (map != null) {
      return map.get(key);
    }
    return null;
  }

  private SortedMap<Date, Valeur> getMapPartitions(Object key) {
    PartitionValeurs map = getPartitionValeur(key);
    if (map != null && map.getValeurs() != null) {
      return map.getValeurs();
    }

    return null;
  }

  private PartitionValeurs getPartitionValeur(Object key) {
    if (key instanceof Date) {
      for (PartitionValeurs partitionValeurs : this.partitionsValeurs.values()) {
        long dateTime = ((Date) key).getTime();
        if (partitionValeurs.getDateDebut().getTime() <= dateTime && dateTime < partitionValeurs.getDatefin().getTime()) {
          return partitionValeurs;
        }
      }
    }
    return null;
  }

  private SortedMap<Date, Valeur> getMapPartitionsCreeSiExistePas(Object key) {
    SortedMap<Date, Valeur> map = getMapPartitions(key);
    if (map == null && key instanceof Date) {
      Date date = dureePartition.obtenirDateDebutPeriode((Date) key);
      PartitionValeurs partition = new PartitionValeurs(pasTemps, date, dureePartition);
      partition.setGrandeur(grandeur);
      SortedMap<Date, Valeur> valeurs = partition.getValeurs();

      partitionsValeurs.put(date, partition);
      return retournePartitionFonctionBorneMap(valeurs);
    }

    return map;
  }

  private SortedMap<Date, Valeur> retournePartitionFonctionBorneMap(SortedMap<Date, Valeur> valeurs) {
    if (this.dateDebut != null && this.dateFin != null) {
      return valeurs.subMap(this.dateDebut, this.dateFin);
    } else if (this.dateDebut != null) {
      return valeurs.tailMap(this.dateDebut);
    } else if (this.dateFin != null) {
      return valeurs.headMap(this.dateFin);
    }
    return valeurs;
  }

  @Override
  public Valeur put(Date date, Valeur value) {
    SortedMap<Date, Valeur> map = getMapPartitionsCreeSiExistePas(date);

    if (map != null) {
      Valeur valeurAncienne = map.put(date, value);
      ++compteurModifications;
      return valeurAncienne;
    }
    return null;
  }

  @Override
  public Valeur remove(Object key) {
    SortedMap<Date, Valeur> map = getMapPartitions(key);

    if (map != null) {
      Valeur ancienneValeur = map.remove(key);
      PartitionValeurs partitionValeurs = getPartitionValeur(key);
      if (partitionValeurs.getValeurs().isEmpty()) {
        Date date = dureePartition.obtenirDateDebutPeriode((Date) key);
        partitionsValeurs.remove(date);
      }
      if (ancienneValeur != null) {
        ++compteurModifications;
      }

      return ancienneValeur;
    }
    return null;
  }

  @Override
  public void putAll(Map<? extends Date, ? extends Valeur> m) {
    for (Valeur valeur : m.values()) {
      if (valeur != null) {
        put(valeur.getDate(), valeur);
      }
    }
  }

  @Override
  public void clear() {
    partitionsValeurs.clear();
    ++compteurModifications;
  }

  @Override
  public Comparator<? super Date> comparator() {
    return new ComparateurValeurTemporelle<Date>();
  }

  @Override
  public SortedMap<Date, Valeur> subMap(Date fromKey, Date toKey) {
    if (fromKey.compareTo(toKey) > 0) {
      throw new IllegalArgumentException("fromKey > toKey");
    }

    return subMap(fromKey, toKey, false, false);
  }

  private SortedMap<Date, Valeur> subMap(Date fromKey, Date toKey, boolean avecHead, boolean avecTail) {
    SortedMap<Date, PartitionValeurs> mapSousPartitions = creerSubMapPartitionsValeurs(fromKey, toKey, avecHead, avecTail);
    return new MapPartitionsValeursTemporelles(mapSousPartitions, dureePartition, pasTemps, this.grandeur, fromKey, toKey);
  }

  private SortedMap<Date, PartitionValeurs> creerSubMapPartitionsValeurs(Date fromKey, Date toKey, boolean avecHead, boolean avecTail) {
    SortedMap<Date, PartitionValeurs> mapSousPartitions = new TreeMap<>();
    for (PartitionValeurs partitionValeurs : partitionsValeurs.values()) {
      if (partitionValeurs.getValeurs() != null) {
        SortedMap<Date, Valeur> mapSousValeurs;
        if (avecHead) {
          mapSousValeurs = partitionValeurs.getValeurs().headMap(toKey);
        } else if (avecTail) {
          mapSousValeurs = partitionValeurs.getValeurs().tailMap(fromKey);
        } else {
          mapSousValeurs = partitionValeurs.getValeurs().subMap(fromKey, toKey);
        }

        if (!mapSousValeurs.isEmpty()) {
          PartitionValeurs nouvellePartition = new PartitionValeurs(partitionValeurs.getValeurs() instanceof TreeMap ? null : pasTemps, partitionValeurs.getDateDebut(), dureePartition);
          nouvellePartition.setValeurs(mapSousValeurs);
          nouvellePartition.setGrandeur(this.grandeur);
          mapSousPartitions.put(nouvellePartition.getDateDebut(), nouvellePartition);
        }
      }
    }
    return mapSousPartitions;
  }

  @Override
  public SortedMap<Date, Valeur> headMap(Date toKey) {
    if (toKey == null) {
      throw new NullPointerException();
    }
    return subMap(null, toKey, true, false);
  }

  @Override
  public SortedMap<Date, Valeur> tailMap(Date fromKey) {
    if (fromKey == null) {
      throw new NullPointerException();
    }
    return subMap(fromKey, null, false, true);
  }

  @Override
  public Date firstKey() {
    if (!isEmpty()) {
      PartitionValeurs premierePartitionNonVide = recupererPremierePartitionNonVide(true);
      if (premierePartitionNonVide != null && !premierePartitionNonVide.getValeurs().isEmpty()) {
        return premierePartitionNonVide.getValeurs().firstKey();
      }
    }

    throw new NoSuchElementException();
  }

  @Override
  public Date lastKey() {
    if (!isEmpty()) {
      PartitionValeurs dernierePartitionNonVide = recupererPremierePartitionNonVide(false);
      if (dernierePartitionNonVide != null && !dernierePartitionNonVide.getValeurs().isEmpty()) {
        return dernierePartitionNonVide.getValeurs().lastKey();
      }
    }

    throw new NoSuchElementException();
  }

  private PartitionValeurs recupererPremierePartitionNonVide(boolean ordreCroissant) {
    Date premiereDatePartition = partitionsValeurs.firstKey();
    Date derniereDatePartition = partitionsValeurs.lastKey();
    Date dateAVerifier = ordreCroissant ? premiereDatePartition : derniereDatePartition;
    PartitionValeurs premierePartitionNonVide = partitionsValeurs.get(dateAVerifier);
    while (isContinuerRechercherPartitionNonVide(ordreCroissant, dateAVerifier, premiereDatePartition, derniereDatePartition, premierePartitionNonVide)) {
      dateAVerifier = ordreCroissant ? dureePartition.prochaineDate(dateAVerifier) : dureePartition.obtenirDatePrecedente(dateAVerifier);
      premierePartitionNonVide = partitionsValeurs.get(dateAVerifier);
    }
    return premierePartitionNonVide;
  }

  private boolean isContinuerRechercherPartitionNonVide(boolean ordreCroissant, Date dateAVerifier, Date premiereDatePartition, Date derniereDatePartition, PartitionValeurs premierePartitionNonVide) {
    boolean isContinuer = ordreCroissant ? !dateAVerifier.after(derniereDatePartition) : !dateAVerifier.before(premiereDatePartition);
    return isContinuer && (premierePartitionNonVide == null || premierePartitionNonVide.getValeurs().isEmpty());
  }

  @Override
  public Set<Date> keySet() {
    return new SetOrdonnePartitionsValeursTemporelles(this, new ConvertisseurMapValeur<>(Date.class));
  }

  @Override
  public Collection<Valeur> values() {
    return new SetOrdonnePartitionsValeursTemporelles(this, new ConvertisseurMapValeur(Valeur.class));
  }

  @Override
  public Set<java.util.Map.Entry<Date, Valeur>> entrySet() {
    return new SetOrdonnePartitionsValeursTemporelles(this, new ConvertisseurMapValeur(EntryDateValeur.class));
  }

  static class SetOrdonnePartitionsValeursTemporelles<E> extends AbstractSetValeursTemporelles<E> {
    SetOrdonnePartitionsValeursTemporelles(SortedMap<Date, Valeur> map, ConvertisseurMapValeur<E> convertisseur) {
      super(map, convertisseur);
    }

    @Override
    public Iterator<E> iterator() {
      return new IterateurSousMap((MapPartitionsValeursTemporelles) this.mapValeur, this.convertisseur);
    }
  }

  static final class IterateurSousMap<E> implements Iterator<E> {
    protected final MapPartitionsValeursTemporelles mapPartitionsValeursTemporelles;
    private Iterator<E> iterateurValeur = null;
    private Iterator<PartitionValeurs> iteratorPartitionValeur = null;
    private final ConvertisseurMapValeur<E> convertisseur;
    private int nbModificationAttendues;

    IterateurSousMap(MapPartitionsValeursTemporelles map, ConvertisseurMapValeur<E> convertisseur) {
      this.mapPartitionsValeursTemporelles = map;
      this.convertisseur = convertisseur;
      if (!map.isEmpty()) {
        iteratorPartitionValeur = map.getMapContientSortedMaps().values().iterator();
        PartitionValeurs premierePartitionValeurs = iteratorPartitionValeur.next();
        iterateurValeur = getIterateurPourConvertisseur(convertisseur, premierePartitionValeurs);
      }
      nbModificationAttendues = map.getCompteurModifications();
    }

    private Iterator<E> getIterateurPourConvertisseur(ConvertisseurMapValeur<E> convertisseur, PartitionValeurs partitionValeurs) {
      if (convertisseur.getClassConversion().isAssignableFrom(Valeur.class)) {
        return (Iterator<E>) partitionValeurs.getValeurs().values().iterator();
      }
      if (convertisseur.getClassConversion().isAssignableFrom(Date.class)) {
        return (Iterator<E>) partitionValeurs.getValeurs().keySet().iterator();
      }
      return (Iterator<E>) partitionValeurs.getValeurs().entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
      if (iterateurValeur != null && iteratorPartitionValeur != null) {
        if (iterateurValeur.hasNext()) {
          return true;
        }
        if (iteratorPartitionValeur.hasNext()) {
          PartitionValeurs partitionValeurs = iteratorPartitionValeur.next();
          iterateurValeur = getIterateurPourConvertisseur(convertisseur, partitionValeurs);
          return hasNext();
        }
      }

      return false;
    }

    @Override
    public E next() {
      if (mapPartitionsValeursTemporelles.getCompteurModifications() != nbModificationAttendues) {
        throw new ConcurrentModificationException();
      }

      if (hasNext()) {
        return iterateurValeur.next();
      }

      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      E element = next();
      if (element instanceof Valeur) {
        mapPartitionsValeursTemporelles.remove(((Valeur) element).getDate());
      } else if (element instanceof Date) {
        mapPartitionsValeursTemporelles.remove(element);
      }
    }
  }

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
}
