package com.imrglobal.framework.cache;

import java.util.*;

/**
 * Implémentation simple d'une map LRU.
 * <p>
 * Attention - l'implémentation n'est pas thread-safe, même avec le flag <i><code>synchro</code></i>.<br>
 * Pour obtenir une version thread-safe :
 * 
 * <pre>
 * int taille = ...
 * Map&ltX,Y&gt mySafeMap = Collections.synchronizedMap(new FIFOMap&ltX,Y&gt(taille));
 * </pre>
 */
public class FIFOMap<K, V> extends LinkedHashMap<K, V> {

  private final int maxCapacity;
  private final boolean synchro;

  /**
   * Créée une {@link FIFOMap} sans limite de capacité
   */
  public FIFOMap() {
    this(0);
  }

  /**
   * Créée une {@link FIFOMap} avec limite de capacité
   * 
   * @param maxcapacity limite de capacité. Note: aucune limite n'est appliquée si maxcapacity n'est pas > 0.
   */
  public FIFOMap(int maxCapacity) {
    synchro = false;
    this.maxCapacity = (maxCapacity > 0) ? maxCapacity : 0;
  }

  /**
   * @param synchro active un mode <i>synchro</i> sur la classe (restreint à put , putAll, remove).
   * @deprecated Ne pas utiliser synchro=<code>true</code> car seule 3 méthodes sont alors synchronisées.<br>
   *             Utiliser le constructeur sans paramètres, et faire appel à {@link Collections#synchronizedMap(Map)} si besoin de synchro.
   */
  @Deprecated
  public FIFOMap(boolean synchro) {
    this(synchro, 0);
  }

  /**
   * <b>Attention :</b> La notion de <i>synchro</i> ne concerne que 3 méthodes... Cela ne rend pas la classe thread-safe...
   * 
   * @param synchro active un mode <i>synchro</i> sur la classe (restreint à put , putAll, remove).
   * @param maxcapacity limite de capacité. Note: aucune limite n'est appliquée si maxcapacity n'est pas > 0.
   * @deprecated Ne pas utiliser synchro=<code>true</code> car seule 3 méthodes sont alors synchronisées.<br>
   *             Utiliser le constructeur avec paramètre entier, et faire appel à {@link Collections#synchronizedMap(Map)} si besoin de synchro.
   */
  @Deprecated
  public FIFOMap(boolean synchro, int maxCapacity) {
    this.synchro = synchro;
    this.maxCapacity = (maxCapacity > 0) ? maxCapacity : 0;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return maxCapacity > 0 && size() > maxCapacity;
  }

  @Override
  public V put(K key, V value) {
    if (synchro) {
      synchronized (this) {
        return super.put(key, value);
      }
    }
    return super.put(key, value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    if (synchro) {
      synchronized (this) {
        super.putAll(m);
      }
    } else {
      super.putAll(m);
    }
  }

  @Override
  public V remove(Object key) {
    if (synchro) {
      synchronized (this) {
        return super.remove(key);
      }
    }
    return super.remove(key);
  }
}
