package com.imrglobal.framework.context;

import java.io.Serializable;
import java.util.*;

/**
 * This interface defines the services which must be offered by a context object.
 */
public class GenericContext implements Context {

  protected transient Map unSerializableProperties;
  protected Map serializableProperties;
  protected boolean serializable;

  protected Map bindingListeners = new HashMap();

  /**
   * See {@link #GenericContext(true)}
   */
  public GenericContext() {
    this(true);
  }

  /**
   * CReate a context indicating if it must be always serializable.
   * <br>If the context is serializable all the key not serializable are stored in separate
   * map.
   *
   * @param serializable
   */
  public GenericContext(boolean serializable) {
    this.serializable = serializable;
    this.serializableProperties = new HashMap();
    if (serializable) {
      this.unSerializableProperties = new HashMap();
    }
  }

  /**
   * Indicates if this context must be always serializable.
   * <br>If the context is serializable all the key not serializable are stored in separate
   * map.
   *
   * @return <code>true</code> if this context is always serializable
   */
  public boolean isSerializable() {
    return serializable && this.unSerializableProperties != null;
  }

  /**
   * This constructor allows to wrap a Map object in a context. The serializableProperties object
   * should contains only String key.
   */
  public GenericContext(Map properties) {
    this();
    if (properties != null) {
      Set keysSet = properties.keySet();
      if (keysSet != null) {
        Iterator it = keysSet.iterator();
        while (it.hasNext()) {
          Object key = it.next();
          Object value = properties.get(key);
          setValue(key.toString(), value);
        }
      }
    }
  }

  /**
   * Retrieves the value in the context corresponding with the given key.
   *
   * @param key key.
   * @return value of the property.
   */
  @Override
  public Object getValue(String key) {
    Object res = this.serializableProperties.get(key);
    if ((res == null) && (this.unSerializableProperties != null)) {
      res = this.unSerializableProperties.get(key);
    }
    return (res);
  }

  /**
   * Sets the value of the context with the given key.
   *
   * @param key
   * @param value of the property.
   * @return the previous value of the specified key in this context,
   *         or null if it did not have one.
   */
  @Override
  public Object setValue(String key, Object value) {
    Object res = null;
    if (value != null) {
      if ((value instanceof Serializable) || (this.unSerializableProperties == null)) {
        res = this.serializableProperties.put(key, value);
        if ((res == null) && (this.unSerializableProperties != null)) {
          res = this.unSerializableProperties.remove(key);
        }
      } else {
        res = this.unSerializableProperties.put(key, value);
        if (res == null) {
          res = this.serializableProperties.remove(key);
        }
      }
    } else {
      res = this.serializableProperties.remove(key);
      if ((res == null) && (this.unSerializableProperties != null)) {
        res = this.unSerializableProperties.remove(key);
      }
    }
    
    return (res);
  }
  
  @Override
  public Map asMap() {
    Map res = new HashMap();
    if (this.unSerializableProperties != null) {
      res.putAll(this.unSerializableProperties);
    }
    if (this.serializableProperties != null) {
      res.putAll(this.serializableProperties);
    }
    return res;
  }

  /**
   * Removes the value in the context.
   *
   * @param key
   * @return the previous value of the specified key in this context,
   *         or null if it did not have one.
   */
  @Override
  public Object removeValue(String key) {
    Object res = this.serializableProperties.remove(key);
    if ((res == null) && (this.unSerializableProperties != null)) {
      res = this.unSerializableProperties.remove(key);
    }
    return (res);
  }

  /**
   * Removes all values from this context.
   */
  @Override
  public void clear() {
    this.serializableProperties.clear();
    if (this.unSerializableProperties != null) {
      this.unSerializableProperties.clear();
    }
  }

  /**
   * Returns a Set view of the values contained in the context.
   */
  @Override
  public Set getAllValues() {
    Set res = new HashSet(this.serializableProperties.values());
    if (this.unSerializableProperties != null) {
      res.addAll(this.unSerializableProperties.values());
    }
    return (res);
  }

  /**
   * Returns a Set view of the keys contained in the context.
   */
  @Override
  public Set getAllKeys() {
    Set res = new HashSet(this.serializableProperties.keySet());
    if (this.unSerializableProperties != null) {
      res.addAll(this.unSerializableProperties.keySet());
    }
    return (res);
  }

  @Override
  public int size() {
    return this.serializableProperties.size() + (this.unSerializableProperties != null ? this.unSerializableProperties.size() : 0);
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    if (this.unSerializableProperties != null) {
      Set notSerializableKeys = this.unSerializableProperties.keySet();
      if ((notSerializableKeys != null) && (!notSerializableKeys.isEmpty())) {
        }
    }
    out.defaultWriteObject();
  }

  private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
    stream.defaultReadObject();
    if (this.serializable) {
      this.unSerializableProperties = new HashMap();
    }
  }
}
