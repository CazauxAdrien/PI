package com.imrglobal.framework.context;

import java.util.*;
import java.util.function.BiFunction;

import com.imrglobal.framework.businessobject.PropertiesValues;

/**
 * This interface defines the services which must be offered by a context object.
 * A context is a group of properties offering more functionnality.
 */
public interface Context extends PropertiesValues {

	/**
	 * Removes the value in the context.
	 * @param key
	 * @return the previous value of the specified key in this context,
	 * or null if it did not have one.
	 */
	public Object removeValue(String key);

	/**
	 * Returns a Set view of the values contained in the context.
	 */
	public Set getAllValues();

	/**
	 * Returns a Set view of the keys contained in the context.
	 */
	public Set getAllKeys();

	/**
	 * Removes all values from this context.
	 */
	public void clear();

  /**
   * Returns the size of this context
   * @return
   */
  public int size();
  
  /**
   * Returns the content of the context as map
   *
   * @return all the mapping contains in this context
   */
  default Map asMap() {
    Map res = new HashMap();
    Set<String> allKeys = getAllKeys();
    allKeys.forEach(k -> res.put(k, this.getValue(k)));
    return res;
  }
  
  /**
   * Copies all of the mappings from the specified context to this context
   *
   * @param ctx Mappings to be stored in this context.
   */
  default void addAll(Context ctx) {
    addAll(ctx, null);
  }
  
  /**
   * Copies all of the mappings from the specified context to this context and compute a new mapping given the key and its mapped value in the original context.
   *
   * @param ctx Mappings to be stored in this context.
   * @param remappingFunction he function to recompute a value
   */
  default void addAll(Context ctx, BiFunction<String, Object, Object> remappingFunction) {
    if (ctx != null) {
      final BiFunction<String, Object, Object> f = remappingFunction != null ? remappingFunction : ((k ,  o) -> o);
      Set<String> allKeys = ctx.getAllKeys();
      allKeys.forEach(key -> setValue(key, f.apply(key, ctx.getValue(key))));
    }
  }

  /**
   * Copies all of the mappings from the specified map to this context.
   *
   * @param ctx Mappings to be stored in this context.
   */
  default void addAll(Map ctx) {
    addAll(ctx, null);
  }
  
  /**
   * Copies all of the mappings from the specified map to this context and compute a new mapping given the key and its mapped value in the map.
   *
   * @param ctx Mappings to be stored in this context.
   * @param remappingFunction he function to recompute a value
   */
  default void addAll(Map ctx, BiFunction<String, Object, Object> remappingFunction) {
    if (ctx != null) {
      final BiFunction<String, Object, Object> f = remappingFunction != null ? remappingFunction : ((k ,  o) -> o); 
      Set<String> allKeys = ctx.keySet();
      allKeys.forEach(key -> setValue(key, f.apply(key, ctx.get(key))));
    }
  }
}
