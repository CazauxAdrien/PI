package com.hermes.arc.commun.type;

import java.util.*;

import com.imrglobal.framework.nls.Language;


/**
 * Interface des types énumérés statiques
 */
public interface IHermesNLSEnumType {
	
	/**
	 * 
	 * @return
	 */
	public int to_int();
	
	/**
	 * 
	 * @return
	 */
	public Iterator iterator();
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj);
	
	/**
	 * 
	 * @param comparator
	 * @return
	 */
	public Iterator sort(Comparator comparator);
	
	/**
	 * 
	 * @param mode
	 * @param lg
	 * @return
	 */
	public Iterator sort(int mode, Language lg);
	
	/**
	 * 
	 * @return
	 */
  public String toStringControl();
	
  /**
   * 
   * @return
   */
	public String getNlsKey();
	
	/**
	 * 
	 * @return
	 */
	public String getShortNlsKey();
	
	/**
	 * 
	 * @return
	 */
	public String toString();
	
	/**
	 * 
	 * @param language
	 * @return
	 */
	public String toString(Language language);
	
	/**
	 * 
	 * @param lg
	 * @return
	 */
	public String getShortLabel(Language lg);
	

}
