package com.imrglobal.framework.utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * This class offer same APIs than {@link ListUtils} using Generics type
 */
public class ListGenericsUtils {

  private static final Map<Class,Supplier<Collection<?>>> COLLECTION_BUILDER;

  static {
    COLLECTION_BUILDER = new HashMap<>();
    COLLECTION_BUILDER.put(ArrayList.class, ArrayList::new);
    COLLECTION_BUILDER.put(HashSet.class, HashSet::new);
    COLLECTION_BUILDER.put(LinkedList.class, LinkedList::new);
    COLLECTION_BUILDER.put(LinkedHashSet.class, LinkedHashSet::new);
    COLLECTION_BUILDER.put(TreeSet.class, TreeSet::new);
  }

  /**
   * Returns a new collection which contains all the elements which satisfy the specified filter.
   */
  public static <E, T extends Collection<E>> T filter(T col, Filter<? super E> filter) {
    if (filter != null) {
      return col.stream().filter(filter).collect(Collectors.toCollection(getCollectionBuilder(col)));
    } 
    return col;
  }

  private static <E, T extends Collection<E>> Supplier<T> getCollectionBuilder(T col) {
    Supplier<? extends Collection> builder = COLLECTION_BUILDER.get(col.getClass());
    return builder != null ? (Supplier<T>) builder : ()->newCol(col);
  }


  /**
   * Returns a new collection which contains all the elements which satisfy the specified filter and allow to cast result in collection of subclass. <br>
   * Allows this kind of notation :<br>
   * <code>Collection&lt;A&gt; col = ...;<br>
   * Collection&lt;B&gt; colFilter = (Collection&lt;B&gt;)ListGenericsUtils.filterInstanceOf(col,f);</code> </br>
   */
  public static <E> Collection<? extends E> filterInstanceOf(Collection<E> col, Filter filter) {
    Collection<E> res;
    if (filter != null) {
      res = newCol(col);
      for (E o : col) {
        if (filter.isOk(o)) {
          res.add(o);
        }
      }
    } else {
      res = col;
    }
    return res;
  }

  /**
   * Returns the first element of the collection
   */
  public static <E> E getFirstElement(Collection<E> col) {
    return getFirstElement(col, null);
  }

  /**
   * Returns the first element of the collection which satisfies the filter.
   */
  public static <E> E getFirstElement(Collection<E> col, Filter<? super E> filter) {
    Predicate<E> predicate = Objects::nonNull;
    if (filter != null) {
      predicate = predicate.and(filter);
    }
    return col.stream()
       .filter(predicate)
       .findFirst()
       .orElse(null);
  }

  /**
   * Sort the elements of the given collection using the specified comparator.<br/>
   * Always create new {@link java.util.ArrayList}.<br/>
   * Use {@link java.util.List#sort(Comparator)} to optimize performance if you use {@link java.util.List}.
   * 
   * @param col collection of elements to sort
   * @param comp comparator used to sort
   * @return the sorted list
   */
  public static <E> List<E> sort(Collection<E> col, Comparator<? super E> comp) {
    List<E> res = new ArrayList<E>(col);
    if (comp != null || ListUtils.isCollectionOf(col,Comparable.class)) {
      Collections.sort(res, comp);
    }
    return res;
  }

  public static <E, T extends Collection<E>> T newCol(T col) {
    T res = null;
    Class<? extends Collection> colImplem = col.getClass();
    Supplier<Collection<?>> collectionSupplier = COLLECTION_BUILDER.get(colImplem);
    if (collectionSupplier != null) {
      return (T) collectionSupplier.get();
    }
    try {
      res = (T) colImplem.newInstance();
    } catch (Exception ex) {
      if (col instanceof List) {
        res = (T)new ArrayList<E>();
      } else {
        res = (T)new HashSet<E>();
      }
    }
    return res;
  }

  /**
   * Returns the last element of the list.
   * @param elements {@code List} of element
   * @return {@code null} if {@code elements} is {@code null} or empty, the last element of the list otherwise.
   */
  public static <E> E getLastElement(List<E> elements) {
    if (elements != null && !elements.isEmpty()) {
      return elements.get(elements.size() - 1);
    }
    return null;
  }
}
