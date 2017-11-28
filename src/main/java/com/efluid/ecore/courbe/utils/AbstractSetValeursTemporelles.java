package com.efluid.ecore.courbe.utils;

import java.util.*;

import com.efluid.ecore.courbe.businessobject.Valeur;

/**
 * retourne un set ordonné sur le champs protected final mapValeur de la classe (soit sur l'objet Date, Valeur ou Entry<Date, Valeur>)
 */
abstract class AbstractSetValeursTemporelles<E> implements SortedSet<E> {
  protected final SortedMap<Date, Valeur> mapValeur;
  protected final ConvertisseurMapValeur<E> convertisseur;

  AbstractSetValeursTemporelles(SortedMap<Date, Valeur> map, ConvertisseurMapValeur<E> convertisseur) {
    this.mapValeur = map;
    this.convertisseur = convertisseur;
  }

  @Override
  public int size() {
    return mapValeur.size();
  }

  @Override
  public boolean isEmpty() {
    return mapValeur.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    if (convertisseur.getClassConversion().isAssignableFrom(Date.class)) {
      return mapValeur.containsKey(o);
    } else if (convertisseur.getClassConversion().isAssignableFrom(Valeur.class)) {
      return mapValeur.containsValue(o);
    }

    return false;
  }

  @Override
  public Object[] toArray() {
    return toArray(convertisseur.getNewArray(mapValeur.size()));
  }

  @Override
  public <T> T[] toArray(T[] a) {
    if (a == null) {
      return (T[]) toArray();
    } else if (a.getClass().getComponentType() != null && convertisseur.getClassConversion().isAssignableFrom(a.getClass().getComponentType())) {
      if (a.length <= mapValeur.size()) {
        E[] elements = (E[]) a;
        int i = 0;
        for (Valeur valeur : this.mapValeur.values()) {
          elements[i++] = convertisseur.getObjetAttenduDeValeur(valeur);
        }

        return a;
      }
      return (T[]) toArray();
    }

    return null;
  }

  @Override
  public boolean add(E e) {
    return false;
  }

  @Override
  public boolean remove(Object o) {
    Date date = null;
    if (o instanceof Valeur) {
      date = ((Valeur) o).getDate();
    } else if (o instanceof Date) {
      date = (Date) o;
    }

    return mapValeur.remove(date) != null;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    if (c != null) {
      for (Object elem : c) {
        if (!contains(elem)) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    if (c != null) {
      for (Valeur valeur : mapValeur.values()) {
        if (valeur != null && !c.contains(convertisseur.getObjetAttenduDeValeur(valeur))) {
          mapValeur.remove(valeur);
        }
      }
    }

    return true;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    if (c != null) {
      for (Valeur valeur : mapValeur.values()) {
        if (valeur != null && c.contains(convertisseur.getObjetAttenduDeValeur(valeur))) {
          mapValeur.remove(convertisseur.getObjetAttenduDeValeur(valeur));
        }
      }
    }

    return true;
  }

  @Override
  public void clear() {
    mapValeur.clear();
  }

  @Override
  public Comparator<? super E> comparator() {
    return convertisseur.getComparateur();
  }

  @Override
  public E first() {
    Date key = mapValeur.firstKey();
    if (key != null) {
      return convertisseur.getObjetAttenduDeValeur(mapValeur.get(key));
    }

    return null;
  }

  @Override
  public E last() {
    Date key = mapValeur.lastKey();
    if (key != null) {
      return convertisseur.getObjetAttenduDeValeur(mapValeur.get(key));
    }

    return null;
  }

  public Date dateFromElement(E element) {
    if (element instanceof Date) {
      return (Date) element;
    } else if (element instanceof Valeur) {
      return ((Valeur) element).getDate();
    }

    return null;
  }

  @Override
  public SortedSet<E> subSet(E fromElement, E toElement) {
    Date fromDate = dateFromElement(fromElement);
    Date toDate = dateFromElement(toElement);

    return getSortedSetElementFromSortedMap(mapValeur.subMap(fromDate, toDate));
  }

  @Override
  public SortedSet<E> headSet(E toElement) {
    Date toDate = dateFromElement(toElement);

    return getSortedSetElementFromSortedMap(mapValeur.headMap(toDate));
  }

  @Override
  public SortedSet<E> tailSet(E fromElement) {
    Date fromDate = dateFromElement(fromElement);

    return getSortedSetElementFromSortedMap(mapValeur.tailMap(fromDate));
  }

  private SortedSet<E> getSortedSetElementFromSortedMap(SortedMap<Date, Valeur> map) {
    if (Date.class.isAssignableFrom(convertisseur.getClassConversion())) {
      return (SortedSet<E>) map.keySet();
    } else if (Valeur.class.isAssignableFrom(convertisseur.getClassConversion())) {
      return (SortedSet<E>) map.values();
    } else {
      return null;
    }
  }

  public String toString() {
    Iterator<E> it = iterator();
    if (!it.hasNext()) {
      return "[]";
    }
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (;;) {
      E e = it.next();
      sb.append(e == this ? "(this Collection)" : e);
      if (!it.hasNext()) {
        return sb.append(']').toString();
      }
      sb.append(',').append(' ');
    }
  }
}
