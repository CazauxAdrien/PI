package com.imrglobal.framework.utils;

/**
 * Abstract implementation of Filter which allows to create compound filter.
 *
 */
public abstract class AbstractFilter<T> implements Filter<T> {

	private Filter<? super T> parent;
	private boolean reverse;

  /**
   * create a new filter.
   */
  public AbstractFilter() {
		this.parent = null;
	}

	/**
	 * Create a new filter with the specified filter as parent.
	 * @param parent the filter used as parent of this comparator
	 */
	public AbstractFilter(Filter<? super T> parent) {
		this.parent = parent;
	}

	/**
	 * Create a new filter with the specified filter as parent.
	 * @param parent the filter used as parent of this comparator
	 * @param reverse the reverse property of this filter
	 */
	public AbstractFilter(Filter<? super T> parent, boolean reverse) {
		this.parent = parent;
		this.reverse = reverse;
	}

	/**
	 * If this filter has a parent, check if this object satisfies the parent filter
	 * then if it is the case, call the check method. Else, only call the check method.
   * @param obj the object to check
   * @return true if the object is accepted by this filter
	 */
	@Override
  public boolean isOk(T obj) {
		boolean res = false;
		if (getParent() != null) {
			if (getParent().isOk(obj)) {
				res = performCheck(obj);
			}
		} else {
			res = performCheck(obj);
		}
		return res;
	}

	/**
	 * Returns the parent of this filter.
	 * @return the parent filter or null
	 */
	public Filter<? super T> getParent() {
		return parent;
	}

	/**
	 * Sets the parent of this filter.
   *
	 * @param parent the filter which mus be used as parent of this filter
	 */
	public void setParent(Filter<? super T> parent) {
		this.parent = parent;
	}

	/**
	 * Returns true if the result of the check must be reverse.
	 * @return true to reverse the result of the check
	 */
	public boolean isReverse() {
		return reverse;
	}

	/**
	 * Sets the reverse property of this filter.
	 * @param reverse the reverse property
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	private boolean performCheck(T obj) {
		boolean res = check(obj);
		return isReverse() ? !res : res;
	}

	/**
	 * Returns true if the object is accepted by this filter.
	 * <br>Must be overriden by implementation.
	 * @param obj the object to check
	 * @return true if the object is accepted by this filter, false otherwise
	 */
	protected abstract boolean check(T obj);
}