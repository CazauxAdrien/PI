package com.imrglobal.framework.utils;

import java.util.Comparator;

/**
 * This abstract class for comparator allows to build complex comparator. Each comparator can be associated with a
 * parent comparator. When the comparator is used to compare two objects, first uses the
 * parent comparator and only if the two objects are equals for the parent comparator, then use this comparator.
 * <br>
 * For example, to perform a sort on a list of BusinessExtract on the first part and the second part :
 * <br>
 * <code>
 * BusinessExtractComparator comp = new BusinessExtractComparator(1);
 * comp = new  BusinessExtractComparator(comp,2);
 * </code>
 * User: thibaut_c
 * Date: 2 avr. 2003
 * Time: 08:56:08
 */
public abstract class AbstractComparator<T>  implements Comparator<T> {

	private Comparator<? super T> parent;

	private boolean reverse;

  /**
   * Create a new comparator.
   */
	public AbstractComparator() {
	}

	/**
	 * Create a new Comparator with the specified comparator as parent.
	 * @param parent the parent compator
	 */
	public AbstractComparator(Comparator<? super T> parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent comparator associated with this comparator.
	 * @return the parent comparator or null
	 */
	public Comparator<? super T> getParent() {
		return parent;
	}

	/**
	 * Set the parent comparator of this comparator.
	 * @param parent the parent comparator to use
	 */
	public void setParent(Comparator<? super T> parent) {
		this.parent = parent;
	}

	/**
	 * Returns true if the result of comparaison must be reverse.
	 * @return true if the result of compare must be reverse
	 */
	public boolean isReverse() {
		return reverse;
	}

	/**
	 * Set the reverse property of this comparator.
	 * @param reverse true to reverse the result of comparaison
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	/**
	 * Compares its two arguments for order.  Returns a negative integer,
	 * zero, or a positive integer as the first argument is less than, equal
	 * to, or greater than the second.<p>
	 *
	 * If the comparator is associated with a parent comparator, then first compares the
	 * two objects using the parent comparator and only if the result is 0 (objects are equals) uses this
	 * comparator.
	 *
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the
	 * 	       first argument is less than, equal to, or greater than the
	 *	       second.
	 * @throws ClassCastException if the arguments' types prevent them from
	 * 	       being compared by this Comparator.
	 */
	@Override
	public int compare(T o1, T o2) {
		int res;
		Comparator<? super T> parent = getParent();
		if (parent != null) {
			res = parent.compare(o1, o2);
			if (res == 0) {
				res = doCompare(o1, o2);
				res = (isReverse() ? -1 * res : res);
			}
		} else {
			res = doCompare(o1, o2);
			res = (isReverse() ? -1 * res : res);
		}
		return res;
	}

	/**
	 * Compares its two arguments for order.
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the
	 * 	       first argument is less than, equal to, or greater than the
	 *	       second.
	 */
	protected abstract int doCompare(T o1, T o2);

	/**
	 * Call this method when one or the two values is null.
	 * @param o1 first objet to compare
	 * @param o2 second object to compare
	 * @return 0 if the both object are null; -1 if only first object is null; 1 if only second
   * object is null
	 */
	protected int compareNullValue(Object o1, Object o2) {
		int res;
		if (o1 == null) {
			res = (o2 == null ? 0 : -1);
		} else {
			res = 1;
		}
		return res;
	}

}
