package com.imrglobal.framework.cache;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation de Map, avec les options suivantes:
 * <ul>
 * <li>Synchronisée(thread-safe) ou non synchronisée</li>
 * <li>Avec limite de capacité (LRU) ou sans</li>
 * </ul>
 * Particularités :
 * <ul>
 * <li>Cette implémentation tolère la clef <code>null</code>, qui sera représentée par {@link #DEFAULT_KEY}</li>
 * <li>Le fait d'enregistrer unitairement une valeur <code>null</code> supprime l'enregistrement.</li>
 * </ul>
 * <b>Note</b> : ne pas utiliser pour de nouveaux développements; préférer l'utilisation directe de collections typées ( {@link HashMap}, {@link ConcurrentHashMap} ou {@link FIFOMap} )
 */
public class LRUHashMap implements Map, Serializable {

  public static final String DEFAULT_KEY = "__DEF_KEY__";

  private final Map<Object, Object> map;
  private final int maxCapacity;
  private boolean synchro;

  /**
   * @param synchro <code>true</code> si la map doit être synchronisée, <code>false</code> dans le cas contraire
   * @param maxCapacity une valeur >0 définit une limite maximale de la capacité. Dans ce cas, les éléments les plus anciens seront supprimés.
   */
  public LRUHashMap(boolean synchro, int maxCapacity) {
    this.maxCapacity = maxCapacity;
    this.synchro = synchro;

    if (maxCapacity > 0) {
      Map<Object, Object> lru = new FIFOMap<>(maxCapacity);
      this.map = synchro ? Collections.synchronizedMap(lru) : lru;
    } else {
      this.map = synchro ? new ConcurrentHashMap<>() : new HashMap<>();
    }
  }

  /**
   * Crée une {@link HashMap} simple
   */
  public LRUHashMap() {
    this(false, 0);
  }

  /**
   * Créée une {@link HashMap} ou une {@link ConcurrentHashMap}
   * 
   * @param synchro <code>true</code> créera une {@link ConcurrentHashMap}
   */
  public LRUHashMap(boolean synchro) {
    this(synchro, 0);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(handleKey(key));
  }

  @Override
  public Object get(Object key) {
    Object theKey = handleKey(key);
    return map.get(theKey);
  }

  @Override
  public Object put(Object key, Object value) {
    Object theKey = handleKey(key);
    Object oldVal;
    if (value != null) {
      oldVal = map.put(theKey, value);
    } else {
      oldVal = remove(key);
    }
    return oldVal;
  }

  @Override
  public Object remove(Object key) {
    return map.remove(handleKey(key));
  }

  @Override
  public void putAll(Map t) {
    map.putAll(t);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<Object> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<Object> values() {
    return map.values();
  }

  @Override
  public Set<Entry<Object, Object>> entrySet() {
    return map.entrySet();
  }

  @Override
  public String toString() {
    return map.toString();
  }

  protected Object handleKey(Object key) {
    return (key != null ? key : DEFAULT_KEY);
  }

  /**
   * @return la map est-elle synchronisée?
   */
  public boolean isSynchro() {
    return synchro;
  }

  /**
   * @return la limite de capacité spécifiée à la création ?
   */
  public int getMaxCapacity() {
    return maxCapacity;
  }
}
