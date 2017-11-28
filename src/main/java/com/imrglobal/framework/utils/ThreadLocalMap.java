package com.imrglobal.framework.utils;

import com.imrglobal.framework.factory.ThreadLocalFactory;

import java.util.*;

/**
 * Implementation of {@link java.util.Map} which handle key-value mappings specific to each thread.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class ThreadLocalMap<K, V> implements Map<K, V> {

  private static final String THREAD_KEY_PREFIX = "ThreadLocalMap.";
  private static final String THREAD_KEY_DEF_VALUE_SUFFIX = ".defValue";
  int initialCapacity;
  String threadContextKey;
  private ThreadLocal<Map<K, V>> mapThreadLocal;
  private ThreadLocal<V> defValue;

  public ThreadLocalMap(int initialCapacity) {
    this(UUID.randomUUID().toString(), initialCapacity);
  }

  public ThreadLocalMap() {
    this(UUID.randomUUID().toString(), Integer.MIN_VALUE);
  }

  public ThreadLocalMap(String threadContextKey) {
    this(threadContextKey, Integer.MIN_VALUE);
  }

  public ThreadLocalMap(String threadContextKey, int initialCapacity) {
    super();
    this.initialCapacity = initialCapacity;
    this.threadContextKey = threadContextKey;
    mapThreadLocal = ThreadLocalFactory.newThreadLocal(buildThreadMapKey());
    defValue = ThreadLocalFactory.newThreadLocal(buildThreadDefValueKey());
  }

  private String buildThreadDefValueKey() {
    return THREAD_KEY_PREFIX + threadContextKey + THREAD_KEY_DEF_VALUE_SUFFIX;
  }

  private String buildThreadMapKey() {
    return THREAD_KEY_PREFIX + threadContextKey;
  }


  /**
   * @return the number of key-value mappings in this map for the current thread
   */
  public int size() {
    return getMapForThread().size();
  }

  /**
   * @return <tt>true</tt> if this map contains no key-value mappings for the current thread.
   */
  public boolean isEmpty() {
    return getMapForThread().isEmpty();
  }

  /**
   * @param key key whose presence in this map is to be tested.
   * @return <tt>true</tt> if this map contains a mapping for the specified key for the current thread.
   */
  public boolean containsKey(Object key) {
    return getMapForThread().containsKey(key);
  }

  /**
   * @param value value whose presence in this map is to be tested.
   * @return <tt>true</tt> if this map maps one or more keys to the specified value for the current thread.
   */
  public boolean containsValue(Object value) {
    return getMapForThread().containsValue(value);
  }

  /**
   * @param key key whose associated value is to be returned.
   * @return the value to which this map maps the specified key for the current thread, or
   *         <tt>null</tt> if the map contains no mapping for this key for the current thread.
   */
  public V get(Object key) {
    return key != null ? getMapForThread().get(key) : getDefaultValue();
  }

  /**
   * Associates the specified value with the specified key in this map for the current thread
   *
   * @param key   key with which the specified value is to be associated.
   * @param value value to be associated with the specified key.
   * @return previous value associated with specified key for the current thread, or <tt>null</tt>
   *         if there was no mapping for key for the current thread.
   */
  public V put(K key, V value) {
    return getMapForThread().put(key, value);
  }

  /**
   * Removes the mapping for this key from this map if it is present for the current thread
   *
   * @param key key whose mapping is to be removed from the map.
   * @return previous value associated with specified key for the current thread, or <tt>null</tt>
   *         if there was no mapping for key for the current thread.
   */
  public V remove(Object key) {
    return getMapForThread().remove(key);
  }

  /**
   * Copies all of the mappings from the specified map to this map for the current thread
   *
   * @param t Mappings to be stored in this map.
   */
  public void putAll(Map<? extends K, ? extends V> t) {
    getMapForThread().putAll(t);
  }

  /**
   * Removes all mappings from this map for the current thread
   */
  public void clear() {
    getMapForThread().clear();
  }

  /**
   * Returns a set view of the keys contained in this map for the current thread
   *
   * @return a set view of the keys contained in this map for the current thread.
   */
  public Set<K> keySet() {
    return getMapForThread().keySet();
  }

  /**
   * Returns a collection view of the values contained in this map for the current thread.
   *
   * @return a collection view of the values contained in this map for the current thread.
   */
  public Collection<V> values() {
    return getMapForThread().values();
  }

  /**
   * Returns a set view of the mappings contained in this map for the current thread.
   *
   * @return a set view of the mappings contained in this map for the current thread.
   */
  public Set<Entry<K, V>> entrySet() {
    return getMapForThread().entrySet();
  }

  /**
   * @return the default value (for the null key) for the current thread
   */
  public V getDefaultValue() {
    return defValue.get();
  }

  /**
   * Set the default value for the current thread
   *
   * @param defValue the default value
   */
  public void setDefaultValue(V defValue) {
    this.defValue.set(defValue);
  }

  private Map<K, V> getMapForThread() {
    Map<K, V> map = mapThreadLocal.get();
    if (map == null) {
      if (this.initialCapacity == Integer.MIN_VALUE) {
        map = new HashMap<K, V>();
      } else {
        map = new HashMap<K, V>(this.initialCapacity);
      }
      mapThreadLocal.set(map);
    }
    return map;
  }

}

