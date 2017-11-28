package com.imrglobal.framework.factory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for {@link ThreadLocal} <br>
 * Use {@link #newThreadLocal(String)} instead of {@link ThreadLocal#ThreadLocal()} <br>
 * Allows to clear all ThreadLocal created using {@link #clearThreadLocals()}
 */
public class ThreadLocalFactory {

  private static final Map<String, ThreadLocal> threadLocalMap = new ConcurrentHashMap<>();

  /**
   * Create en new instance of {@link ThreadLocal}
   *
   * @param id id for the instance. Can not be null
   * @return the instancce
   * @throws IllegalArgumentException if id is null
   */
  public static synchronized ThreadLocal newThreadLocal(String id) {
    if (id != null) {
      ThreadLocal threadLocal = threadLocalMap.get(id);
      if (threadLocal == null) {
        threadLocal = new ThreadLocal();
        threadLocalMap.put(id, threadLocal);
      }
      return threadLocal;
    } else {
      throw new IllegalArgumentException("ThreadLocalFactory.newThreadLocal ID can not be null!");
    }
  }

  /**
   * Get an instance of ThreadLocal already initialized or used the instances given
   *
   * @param id id for the instance. Can not be null
   * @param def instance to use if not already initialized
   * @return the instancce
   * @throws IllegalArgumentException if id is null
   */
  public static synchronized ThreadLocal newThreadLocal(String id, ThreadLocal def) {
    if (id != null) {
      ThreadLocal threadLocal = (ThreadLocal) threadLocalMap.get(id);
      if (threadLocal == null) {
        threadLocal = def;
        threadLocalMap.put(id, threadLocal);
      }
      return threadLocal;
    } else {
      throw new IllegalArgumentException("ThreadLocalFactory.newThreadLocal ID can not be null!");
    }
  }

  /**
   * Clear all {@link ThreadLocal} created using {@link #newThreadLocal(String)}.
   */
  public static void clearThreadLocals() {
    System.err.println("ThreadLocalFactory.clearThreadLocals ");
    final Collection<ThreadLocal> values = threadLocalMap.values();
    for (ThreadLocal threadLocal : values) {
      try {
        threadLocal.set(null);
      } catch (Throwable e) {
        System.err.println("ThreadLocalFactory.clearThreadLocals ");
      }
    }
  }

  /**
   * Check if at least one ThreadLocal created using {@link #newThreadLocal(String)} has a not null value for the crurrent thread
   *
   * @return <code>true</code> if one ThreadLocal has a not null value for the current thread
   */
  public static boolean isThreadLocalSet() {
    boolean ok = false;
    final Collection<ThreadLocal> values = threadLocalMap.values();
    for (Iterator<ThreadLocal> iterator = values.iterator(); iterator.hasNext() && !ok;) {
      try {
        ThreadLocal threadLocal = iterator.next();
        ok = threadLocal.get() != null;
      } catch (Throwable e) {
        System.err.println("ThreadLocalFactory.isThreadLocalSet ");
      }
    }
    return ok;
  }

}
