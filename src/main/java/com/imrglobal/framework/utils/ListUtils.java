package com.imrglobal.framework.utils;

import java.util.Collection;

/**
 * Utility. This class contains a set of methods to handles lists, collections, and sets.
 */
public class ListUtils {

  /**
   * Check if the collection is a collection of element of the spécified type
   * 
   * @return [@code true} collection si not null and not empty and if the first not null element of the collection is an instance of the specified type
   */
  public static boolean isCollectionOf(Collection collection, Class type) {
    if (collection != null && !collection.isEmpty()) {
      for (Object o : collection) {
        if (o != null) {
          return type.isInstance(o);
        }
      }
    }
    return false;
  }
}