package com.imrglobal.framework.utils;

import java.util.*;

/**
 * The <code>SortVector</code> class extends the <code>Vector</code> class.
 * It allows to sort the elements contains in a Vector of objects.
 * <p/>
 * An instance of the class is associated with a Comparator. The comparator is
 * used to sort the elements. It is specified at the creation of the instance.
 * By default, the comparator is null, so the sort is not performed.
 */
public class SortVector<E> extends Vector<E> {

  public static final int QUICK_SORT = 1;
  public static final int N2_SORT = 2;
  public static final int DICHOTOMY_SORT = 3;
  public static final int JAVA_SORT = 4;

  //----------------------------------------------------------------------------
  protected Comparator<? super E> comparator;
  protected int sortAlgorithm = JAVA_SORT;
  protected boolean isSorted = false;
  protected boolean isAutomaticSort = false;

  /**
   * Constructs an empty vector so that its internal data array
   * has size <tt>10</tt> and its standard capacity increment is
   * zero.
   */
  public SortVector() {
    super();
    comparator = null;
  }

  /**
   * Constructs an empty vector with the specified initial capacity and
   * with its capacity increment equal to zero.
   *
   * @param i the initial capacity of the vector.
   * @throws IllegalArgumentException if the specified initial capacity
   *                                  is negative
   */
  public SortVector(int i) {
    this(i, null);
  }

  /**
   * Constructs an empty vector with the specified initial capacity and
   * capacity increment.
   *
   * @param i   the initial capacity of the vector.
   * @param j the amount by which the capacity is
   *                          increased when the vector overflows.
   * @throws IllegalArgumentException if the specified initial capacity
   *                                  is negative
   */
  public SortVector(int i, int j) {
    this(i, j, null);
  }

  /**
   * Constructs a vector containing the elements of the specified
   * collection, in the order they are returned by the collection's
   * iterator.
   *
   * @since JDK1.2
   */
  public SortVector(Collection<? extends E> c) {
    this(c, null);
  }

  //----------------------------------------------------------------------------
  /**
   * Constructs an empty vector so that its internal data array
   * has size <tt>10</tt> and its standard capacity increment is
   * zero and using the specified comparator to sort the
   * elements.
   *
   * @param comp Comparator used to sort the element.
   */
  public SortVector(Comparator<? super E> comp) {
    super();
    comparator = comp;
  }

  /**
   * Constructs an empty vector with the specified initial capacity and
   * with its capacity increment equal to zero and using the specified
   * comparator to sort the elements.
   *
   * @param i the initial capacity of the vector.
   * @param comp            Comparator used to sort the element.
   * @throws IllegalArgumentException if the specified initial capacity
   *                                  is negative
   */
  public SortVector(int i, Comparator<? super E> comp) {
    super(i);
    comparator = comp;
  }

  /**
   * Constructs an empty vector with the specified initial capacity and
   * capacity increment and using the specified comparator to sort the
   * elements.
   *
   * @param i the initial capacity of the vector.
   * @param j the amount by which the capacity is
   *                          increased when the vector overflows.
   * @param comp              Comparator used to sort the element.
   * @throws IllegalArgumentException if the specified initial capacity
   *                                  is negative
   */
  public SortVector(int i, int j, Comparator<? super E> comp) {
    super(i, j);
    comparator = comp;
  }

  /**
   * Constructs a vector containing the elements of the specified
   * collection, in the order they are returned by the collection's
   * iterator, and using the specified comparator to sort the
   * elements.
   *
   * @param comp Comparator used to sort the element.
   * @since JDK1.2
   */
  public SortVector(Collection<? extends E> c, Comparator<? super E> comp) {
    super(c);
    comparator = comp;
  }

  //------- Method of Vector overiden ------------------------------------------

  public void add(int index, E element) {
    super.add(index, element);
    isSorted = false;
  }

  public boolean add(E element) {
    boolean res = super.add(element);
    if ((res) && (isAutomaticSort)) {
      if (size() == 1) {
        isSorted = true;
      } else {
        int currentIndex = size() - 1;
        int index = getRightIndex(element, 0, currentIndex - 1);
        deplace(currentIndex, index);
      }
    } else {
      isSorted = false;
    }

    return res;
  }

  public boolean addAll(Collection<? extends E> c) {
    isSorted = false;
    return super.addAll(c);
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    isSorted = false;
    return (super.addAll(index, c));
  }

  public void addElement(E obj) {
    isSorted = false;
    super.addElement(obj);
  }

  public E set(int index, E element) {
    isSorted = false;
    return (super.set(index, element));
  }

  public void setElementAt(E obj, int index) {
    isSorted = false;
    super.setElementAt(obj, index);
  }


  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  /**
   * Set the algorithm used to sort the elements.
   * <BR>You can choose the following algorithm
   * <ul>
   * <li> JAVA_SORT : default algorithm, use the Arrays.sort method (faster algorithm).
   * <li> QUICK_SORT : fast if the list is not sort at all.
   * <li> DICHOTOMY_SORT : algorithm usnig dichotomy, fast if the list is almost
   * sorted (for example, if you have sort the list and add some nex elements).
   */
  public void setSortAlgorithm(int algo) {
    sortAlgorithm = algo;
  }

  //----------------------------------------------------------------------------
  /**
   * Defines the comparator used to compare the elements of the list.
   */
  public void setComparator(Comparator<? super E> c) {
    comparator = c;
  }

  //----------------------------------------------------------------------------
  /**
   * Returns the algorithm used to sort.
   */
  public int getSortAlgorithm() {
    return sortAlgorithm;
  }
  //----------------------------------------------------------------------------

  /**
   * Returns the comparator used to compare the elements of the list.
   */
  public Comparator<? super E> getComparator() {
    return comparator;
  }

  /**
   * Set the automatic sort mode.
   * <BR>If true, when you add an elemnt to the list (calling the add(Object) method)
   * the element is automtically add to right position in the list.
   * <BR>If the automatic set mode is selected, add an element to the list will take
   * more time.
   */
  public void setIsAutomaticSort(boolean b) {
    isAutomaticSort = b;
  }

  //----------------------------------------------------------------------------
  /**
   * Sort the elements in the Vector accoriding to the specified algorithm and
   * using the comparator associated with the vector.
   * <BR> There are two algorithms available :
   * <ul>
   * <li> QUICK_SORT
   * <li> N2_SORT
   * </ul>
   */
  public synchronized void sort(int sortAlgo) {
    if (!isSorted) {
      if (comparator != null) {
        switch (sortAlgo) {
          case QUICK_SORT:
            quickSort(0, size() - 1);
            break;
          case N2_SORT:
            n2sort();
            break;
          case DICHOTOMY_SORT:
            dichotomySort();
            break;
          case JAVA_SORT:
            javaSort();
            break;
          default:
            break;
        }
      }
      isSorted = true;
    }
  }

  //----------------------------------------------------------------------------
  /**
   * Sort the elements in the Vector according to the comparator associated with the
   * vector.
   * <BR> This implementation merely calls
   * <code>sort(sortAlgorithm)</code>
   */
  public void sort() {
    sort(sortAlgorithm);
  }

  //----------------------------------------------------------------------------
  // PAS BESOIN DE SE SYNCHRONISER ICI CAR ON EST SUR DE L'AVOIR FAIT AVANT A
  // L'APPEL DE SORT
  //----------------------------------------------------------------------------
  /**
   * Swap the position in the vector of the two objects at the given positions.
   *
   * @param loc1 index of the first element.
   * @param loc2 index of the second element.
   */
  protected void swap(int loc1, int loc2) {
    E tmp = set(loc1, (E)elementData[loc2]);
    set(loc2, tmp);
  }

  //----------------------------------------------------------------------------
  //--------  ALGORITHMES DE TRI  ----------------------------------------------
  //----------------------------------------------------------------------------
  /**
   * Algorithm associated with QUICK_SORT used to sort the vector elements.
   */
  protected void quickSort(int left, int right) {
    if (right > left) {
      E o1 = (E)elementData[right];
      int i = left - 1;
      int j = right;
      while (true) {
        while (comparator.compare((E)elementData[++i], o1) < 0) ;
        while (j > 0)
          if (comparator.compare((E)elementData[--j], o1) <= 0)
            break; // out of while
        if (i >= j) break;
        swap(i, j);
      }
      swap(i, right);
      quickSort(left, i - 1);
      quickSort(i + 1, right);
    }
  }

  //-------   ALGO PAS TESTé : -------------------------------------------------
  /**
   * Algorithm associated with N2_SORT used to sort the vector elements.
   */
  protected void n2sort() {
    for (int i = 0; i < elementCount; i++) {
      for (int j = i + 1; j < elementCount; j++) {
        if (comparator.compare((E)elementData[j], (E)elementData[i]) < 0) {
          swap(i, j);
        }
      }
    }
  }

  //----------------------------------------------------------------------------
  /**
   * Returns an iterator over the elements in the given Collection.
   * The elements have been sorted using the given Comparator.
   *
   * @param col  Collection with  the elements to sort.
   * @param comp Comparator used to sort the elements of  the vector.
   * @return iterator over the elements in the given Vector.
   */
  public static <E> Iterator<E> sort(Collection<? extends E> col, Comparator<? super E> comp) {
    return (sort(col, comp, JAVA_SORT));
  }

  /**
   * Returns an iterator over the elements in the given Collection.
   * The elements have been sorted using the given Comparator.
   *
   * @param col  Collection with  the elements to sort.
   * @param comp Comparator used to sort the elements of  the vector.
   * @param algo Alorithm used to sort the elements of the vector.
   * @return iterator over the elements in the given Vector.
   */
  public static <E> Iterator<E> sort(Collection<? extends E> col, Comparator<? super E> comp, int algo) {
    SortVector<E> sv = new SortVector<>(col, comp);
    sv.setSortAlgorithm(algo);
    sv.sort();
    return (sv.iterator());
  }

  /**
   * Algorithm to sort using dichotomy.
   * <BR>This algorithm is better when the list is almost already sorted.
   */
  protected void dichotomySort() {
    for (int i = 1; i < size(); i++) {
      E obj = (E) elementData[i];
      int index = getRightIndex(obj, 0, i - 1);
      deplace(i, index);
    }
  }

  /**
   * Give the right index between the both given index for the given object using dichotomy.
   *
   * @param obj   object
   * @param start first index
   * @param end   last index
   */
  protected int getRightIndex(E obj, int start, int end) {
    int index = 0;
    if (comparator.compare(obj, (E)elementData[start]) <= 0) {
      index = start;
    } else if (comparator.compare(obj, (E)elementData[end]) >= 0) {
      index = end + 1;
    } else {
      if ((end - start) == 1) {
        index = end;
      } else {
        int middle = start + (end - start) / 2;
        if (comparator.compare(obj, (E)elementData[middle]) > 0) {
          index = getRightIndex(obj, middle, end);
        } else if (comparator.compare(obj, (E)elementData[middle]) < 0) {
          index = getRightIndex(obj, start, middle);
        } else {
          index = middle;
        }
      }
    }
//		}
    return (index);
  }

  /**
   * Deplace the object from an old position to a new position.
   * <BR>If the new position is before the old, all the element between
   * the positions will be moved.
   */
  protected void deplace(int oldPos, int newPos) {
    if (oldPos > newPos) {
      for (int i = oldPos; i > newPos; i--) {
        swap(i - 1, i);
      }
    } else if (oldPos < newPos) {
      swap(oldPos, newPos);
    } else {
      return;
    }
  }

  /**
   * Algorithm associated with JAVA_SORT used to sort the vector elements.
   * <BR>This Algorithm uses the Arrays.sort() method.
   */
  protected void javaSort() {
    E[] array = (E[]) this.toArray();
    Arrays.sort(array, comparator);
		this.clear();
    this.addAll(Arrays.asList(array));
	}

}

