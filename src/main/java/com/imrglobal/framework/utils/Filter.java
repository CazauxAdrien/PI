package com.imrglobal.framework.utils;

import java.util.function.Predicate;

/**
 * Filter object used to filter objects.
 */
@FunctionalInterface
public interface Filter<T> extends Predicate<T> {

  public boolean isOk(T obj);

  @Override
  public default boolean test(T t) {
    return isOk(t);
  }
}
