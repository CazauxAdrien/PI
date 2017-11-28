package com.imrglobal.framework.type;

import com.imrglobal.framework.nls.Language;
import com.imrglobal.framework.utils.*;

import java.io.Serializable;
import java.util.*;

/**
 * This type represent a selection of item in a specified Enum or Persistent Enum type.
 * User: thibaut_c
 * Date: Mar 11, 2003
 * Time: 8:53:58 AM
 */
public class ListEnum implements Serializable, Cloneable {

  public static final String DEFAULT_SEPARATOR = ";";
  private static final long serialVersionUID = 680750928228167123L;
  private Class enumType;
  private boolean isPersistent;

  private Set<EnumerationType> enumSet = new HashSet<>();

  /**
   * Create a new selection of the specified enum or persistent enum type
   *
   * @param enumType the type of the item of the selection
   */
  public ListEnum(Class enumType) {
    setEnumType(enumType);
  }

  /**
   * Create a new selection of the specified enum or persistent enum type and
   * set this selection of item using the specified pattern. The pattern is constituted with the code of
   * each item of the enum or persistent enum type separated by ;.
   * <br><code>1;6;8;12</code> for an enum type
   * <br><code>CDE;CDX;CDG;CDA</code> for a persistent enum type
   *
   * @param enumType the type of the item of the selection
   * @param pattern  the list of values of selection as string
   */
  public ListEnum(Class enumType, String pattern) {
    setEnumType(enumType);
    setSelection(pattern);
  }

  /**
   * Create a new selection of the enum type using all th items specified.
   *
   * @param enums the selection of enum items.
   */
  public ListEnum(EnumerationType ... enums) {
    setSelection(enums);
  }

  /**
   * Crée une nouvelle sélection d'énumérés pour le type donné et initialisée avec les valeurs spécifiées.
   * <p>Privilégier l'utilisation de ce construteur si le tableau d'énumérés contient des valeurs de classes filles de {@code enumType}</p>
   * <p>Exemple :
   * {@code new ListeEnum(EClasseEfluid.XXXX, EClasseEfluid.YYYY} crée une sélection pour laquelle {@code enumType==EClasseEfluid}.
   * <br>{@code new ListeEnum(EClasse.class, EClasseEfluid.XXXX, EClasseEfluid.YYYY} crée une sélection pour laquelle {@code enumType==EClasse}
   * </p>
   * <p>Seuls les valeurs du tableau d'énumérés compatibles avec le type indiqué seront ajoutées à la sélection</p>
   * @param enumType le type des énumérés
   * @param enums les énumérés composant la sélection
   */
  public ListEnum(Class enumType, EnumerationType ... enums) {
    setEnumType(enumType);
    setSelection(enums);
  }

  /**
   * Create a new selection of the enum type using an array of enum type.
   *
   * @param enums the selection of enum as collection of {@link EnumType} or {@link PersistentEnumType}.
   */
  public ListEnum(Collection enums) {
    setSelection(enums);
  }

  /**
   * Crée une nouvelle sélection d'énumérés pour le type donné et initialisée avec les valeurs spécifiées.
   * <p>Privilégier l'utilisation de ce construteur si la collection contient des valeurs de classes filles de {@code enumType}</p>
   * <p>Seuls les valeurs du tableau d'énumérés compatibles avec le type indiqué seront ajoutées à la sélection</p>
   * @param enumType le type des énumérés
   * @param enums les énumérés composant la sélection
   */
  public ListEnum(Class enumType, Collection enums) {
    setEnumType(enumType);
    setSelection(enums);
  }

  protected void setEnumType(Class enumType) {
    this.enumType = enumType;
    isPersistent = false;
  }

  public Class getEnumType() {
    return enumType;
  }

  /**
   * Add an item of an enum item (persistent ot not) to the selection.
   *
   * @param item the item to add
   * @return true if the item has been added to the selection, false if not or if
   *         the specified item is not an item of the item associated with this instance.
   */
  public boolean add(EnumerationType item) {
    boolean res = false;
    if (enumType.isInstance(item)) {
      res = enumSet.add(item);
    }
    return res;
  }

  /**
   * Add the item of an enum type with the specified internal value to the selection.
   *
   * @param itemValue the internal value of the item to add
   * @return true if the item has been added to the selection, false if not or if
   *         the specified type is not an item of the type associated with this instance.
   */
  public boolean add(int itemValue) {
    boolean res = false;
    EnumType item = EnumType.from_int(this.enumType, itemValue);
    if (item != null) {
      res = add(item);
    }
    return res;
  }

  /**
   * Add the item represented by the specified string.
   * <ul>
   * <li>if it's a selection of persistent enum, the string represents the code of the type tu add to its selection.
   * <li>if it's a selection of static enum, the string represents the internal value of the type or the name of the
   * constant which represents the type to add to its selection.
   * </ul>
   *
   * @param itemCode the internal code of the item to add
   * @return true if the item has been added to the selection, false if not or if
   *         the specified type is not an item of the type associated with this instance.
   */
  public boolean add(String itemCode) {
    boolean res = false;

      res = add(castStringAsEntumType(itemCode));
    
    return res;
  }

  /**
   * Converti une chaîne de caractères en valeur d'énuméré statique :<ul>
   *   <li>Si la chaîne correspond à une valeur numérique, on utilise le from_int</li>
   *   <li>sinon, on utilise éventuellement une méthode fromString sur la classe d'énuméré (cf. {@link CastUtil#fromStringToObject(Class, String)})</li>
   * </ul>
   */
  private EnumType castStringAsEntumType(String itemCode) {
    EnumType item;
    int enumValue;
    try {
      enumValue = NumberUtils.parseInt(itemCode);
    } catch (Exception e) {
      enumValue = UndefinedAttributes.UNDEF_INT;
    }
    if (enumValue == UndefinedAttributes.UNDEF_INT) {
      item = (EnumType) CastUtil.fromStringToObject(this.enumType, itemCode);
    } else {
      item = EnumType.from_int(this.enumType, enumValue);
    }
    return item;
  }

  /**
   * Add all items of an enum item (persistent ot not) to the selection.
   *
   * @param items the items to add
   * @return true if all the items has been added to the selection, false if not or if
   *         one of the specified item is not an item of the type associated with this instance.
   */
  public boolean addAll(EnumerationType ... items) {
    boolean res = true;
    for (int i = 0; i < items.length; i++) {
      EnumerationType item = items[i];
      res = add(item) && res;
    }
    return res;
  }

  /**
   * Add all items of an enum item (persistent ot not) to the selection.
   *
   * @param items the items to add
   * @return true if all the items has been added to the selection, false if not or if
   *         one of the specified item is not an item of the type associated with this instance.
   */
  public boolean addAll(Collection items) {
    boolean res = true;
    for (Object item : items) {
      if (item instanceof EnumerationType) {
        res = add((EnumerationType) item) && res;
      } else {
        res = false;
      }
    }
    return res;
  }

  /**
   * Add all items of a selection of enum item (persistent ot not) to the selection.
   *
   * @param selection a selection of items to add
   * @return true if all the items has been added to the selection, false if not or if
   *         one of the specified item is not an item of the type associated with this instance.
   */
  public boolean addAll(ListEnum selection) {
    boolean res = true;
    for (EnumerationType item : selection.enumSet) {
      res = add(item) && res;
    }
    return res;
  }

  /**
   * Removes the specified item of an enum from this selection if it is present
   *
   * @param item the item to remove
   * @return true if the selection contained the specified item.
   */
  public boolean remove(EnumerationType item) {
    return enumSet.remove(item);
  }

  /**
   * remove all items of an enum item (persistent ot not) to the selection.
   *
   * @param items the items to remove
   * @return true if all the items has been removed from the selection, false if not.
   */
  public boolean removeAll(EnumerationType ... items) {
    boolean res = true;
    for (int i = 0; i < items.length; i++) {
      EnumerationType item = items[i];
      res = remove(item) && res;
    }
    return res;
  }

  /**
   * remove all items of an enum item (persistent ot not) to the selection.
   *
   * @param items the items to remove
   * @return true if all the items has been removed from the selection, false if not.
   */
  public boolean removeAll(Collection items) {
    boolean res = true;
    for (Object item : items) {
      if (item instanceof EnumerationType) {
        res = remove((EnumerationType) item) && res;
      } else {
        res = false;
      }
    }
    return res;
  }

  /**
   * remove all items of a selection of enum item (persistent ot not) from this selection.
   *
   * @param selection a selection of items to remove
   * @return true if all the items has been removed from the selection, false if not.
   */
  public boolean removeAll(ListEnum selection) {
    boolean res = true;
    for (EnumerationType item : selection.enumSet) {
      res = remove(item) && res;
    }
    return res;
  }

  /**
   * clear the selection.
   */
  public void clear() {
    this.enumSet.clear();
  }

  /**
   * Returns true if this selection contains the specified item of an enum type.
   *
   * @param item the item
   * @return true if the selection contained the specified item.
   */
  public boolean contains(EnumerationType item) {
    return this.enumSet.contains(item);
  }

  /**
   * Returns true if this selection contains the specified item.
   *
   * @param item the item
   * @return true if the selection contained the specified item.
   */
  public boolean contains(Object item) {
    return this.enumSet.contains(item);
  }

  /**
   * Returns true if this selection contains all the items of another selection.
   *
   * @param sel a selection of enuml
   * @return true this selection contains all the items of another selection.
   */
  public boolean containsAll(ListEnum sel) {
    return containsAll(sel.getEnumSet());
  }

  /**
   * Returns true if this selection contains all the items of the given collection.
   *
   * @param items collection of item
   * @return true this selection contains all the items of the given collection
   */
  public boolean containsAll(Collection items) {
    boolean ok = true;
    for (Iterator iterator = items.iterator(); iterator.hasNext() && ok; ) {
      ok = contains(iterator.next());
    }
    return ok;
  }

  /**
   * Returns true if this selection contains all the items of another selection.
   *
   * @param items items to test
   * @return true this selection contains all the items
   */
  public boolean containsAll(EnumerationType ... items) {
    boolean ok = true;
    for (int i = 0; i < items.length; i++) {
      ok = contains(items[i]);
    }
    return ok;
  }

  /**
   * Return true if the given ListEnum contain exactly the same elements of this ListEnum.
   * @param sel the ListEnum to test
   * @return true if the given ListEnum contain exactly the same elements of this ListEnum, false otherwise .
   */
  public boolean isEqualListEnum(ListEnum sel) {
    boolean ok = sel != null && size() == sel.size();
    if (ok) {
      for (Iterator iterator = enumSet.iterator(); iterator.hasNext() && ok; ) {
        EnumerationType item = (EnumerationType) iterator.next();
        ok = sel.contains(item);
      }
    }
    return ok;
  }

  /**
   * Returns true if this selection contains no elements.
   *
   * @return true if this set contains no elements.
   */
  public boolean isEmpty() {
    return this.enumSet.isEmpty();
  }

  /**
   * Return the content of the selection.
   *
   * @return the list of item in the selection (a collection of {@link com.imrglobal.framework.type.EnumType} or
   *         {@link com.imrglobal.framework.type.PersistentEnumType})
   */
  public Set getEnumSet() {
    return Collections.unmodifiableSet(this.enumSet);
  }

  /**
   * @return the number of items in this selection
   */
  public int size() {
    return this.enumSet != null ? this.enumSet.size() : 0;
  }

  /**
   * Return the content of the selection sort by value if it's a selection of enum type or by
   * order if it's a list of persistent enum type..
   *
   * @return the list of item of this selection (a collection of {@link com.imrglobal.framework.type.EnumType} or
   *         {@link com.imrglobal.framework.type.PersistentEnumType})
   */
  public List getEnumList() {
    return getEnumList( EnumComparator.VALUE_ASC, null);
  }

  /**
   * Return the list of item in the selection sort using the specified comparator.
   *
   * @param cmp the comprator used to sort the selection
   * @return the list of item in the selection (a collection of {@link com.imrglobal.framework.type.EnumType} or
   *         {@link com.imrglobal.framework.type.PersistentEnumType})
   */
  public List getEnumList(Comparator cmp) {
    List res = new ArrayList();
    Iterator it = SortVector.sort(this.enumSet, cmp);
    while (it.hasNext()) {
      res.add(it.next());
    }
    return res;
  }

  /**
   * Return the list of item in the selection sort using the specified mode for the specified
   * language.
   *
   * @param mode mode used to sort : EnumComparator.ALPHABETIC_ASC, EnumComparator.ALPHABETIC_DESC,
   *             EnumComparator.VALUE_ASC or EnumComparator.VALUE_DESC if it's a selection of enum type. PersistentEnumComparator.SHORT_ASC, PersistentEnumComparator.SHORT_DESC,
   *             PersistentEnumComparator.LONG_ASC, PersistentEnumComparator.LONG_DESC, PersistentEnumComparator.ORDER_ASC,
   *             PersistentEnumComparator.ORDER_DESC, PersistentEnumComparator.CODE_ASC or PersistentEnumComparator.CODE_DESC if it's a selection of
   *             PersistentEnumType.
   * @param lg   language to use
   * @return the list of item in the selection (a collection of {@link com.imrglobal.framework.type.EnumType} or
   *         {@link com.imrglobal.framework.type.PersistentEnumType})
   */
  public List getEnumList(int mode, Language lg) {
    Comparator comp = null;

      comp = new EnumComparator(mode, lg);
    
    return getEnumList(comp);
  }

  /**
   * Set the selection of item using a String. The string is constituted with the code of
   * each item of the enum or persistent enum type separated by a ';'.
   * <br><code>1;6;8;12</code> for an enum type
   * <br><code>CDE;CDX;CDG;CDA</code> for a persistent enum type
   *
   * @param selString the selection as string
   */
  public void setSelection(String selString) {
    setSelection(selString, ";");
  }

  /**
   * Set this selection of item using a String. The string is constituted with the code of
   * each item of the enum or persistent enum type separated by the specified separator.
   * <br><code>1;6;8;12</code> for an enum type
   * <br><code>CDE;CDX;CDG;CDA</code> for a persistent enum type
   *
   * @param selString the selection as string
   * @param separator the separator used in the string
   */
  public void setSelection(String selString, String separator) {
    clear();
    StringTokenizer tokenizer = new StringTokenizer(selString, separator);
    while (tokenizer.hasMoreTokens()) {
      String itemValue = tokenizer.nextToken();
      add(itemValue);
    }
  }

  /**
   * Set this selection of item using the specified array of code or internal value.
   *
   * @param codes the internal codes of items
   */
  public void setSelection(String ... codes) {
    clear();
    for (int i = 0; i < codes.length; i++) {
      String code = codes[i];
      if ((code != null) && (!"".equals(code))) {
        if (isPersistent) {
          add(code);
        } else {
          add(Integer.parseInt(code));
        }
      }
    }
  }

  /**
   * Set this selection of item using the specified array of internal value.
   *
   * @param codes the internal codes of items
   */
  public void setSelection(int ... codes) {
    clear();
    for (int i = 0; i < codes.length; i++) {
      int code = codes[i];
      if (!isPersistent) {
        add(code);
      }
    }
  }

  /**
   * Returns a ListEnum containing the intersection of this ListEnum and the given ListEnum.
   * @param sel the selection
   * @return a ListEnum, eventually empty, containing the item which belongs to each ListEnum, or null if the enumeType of
   * each ListEnum is not compatible.
   */
  public ListEnum intersection(ListEnum sel) {
    ListEnum intersection = null;
    if (getEnumType().isAssignableFrom(sel.getEnumType())) {
      intersection = new ListEnum(getEnumType());
    } else if (sel.getEnumType().isAssignableFrom(getEnumType())) {
      intersection = new ListEnum(sel.getEnumType());
    }
    if (intersection != null) {
      Set<EnumerationType> allItems = new HashSet<EnumerationType>();
      allItems.addAll(getEnumSet());
      allItems.addAll(sel.getEnumSet());
      for (EnumerationType item : allItems) {
        if (contains(item) && sel.contains(item)) {
          intersection.add(item);
        }
      }
    }
    return intersection;
  }

  /**
   * Returns a Collection containing the exclusive disjunction (symmetric difference) of the given Collections.
   * @param sel the selection
   * @return a ListEnum, eventually empty, containing the items which belong to only one of each ListEnum, or null if the enumeType of
   * each ListEnum is not compatible.
   */
  public ListEnum disjunction(ListEnum sel) {
    ListEnum disjunction = null;
    if (getEnumType().isAssignableFrom(sel.getEnumType())) {
      disjunction = new ListEnum(getEnumType());
    } else if (sel.getEnumType().isAssignableFrom(getEnumType())) {
      disjunction = new ListEnum(sel.getEnumType());
    }
    if (disjunction != null) {
      Set<EnumerationType> allItems = new HashSet<EnumerationType>();
      allItems.addAll(getEnumSet());
      allItems.addAll(sel.getEnumSet());
      for (EnumerationType item : allItems) {
        if ((contains(item) && !sel.contains(item)) || (!contains(item) && sel.contains(item))) {
          disjunction.add(item);
        }
      }
    }
    return disjunction;
  }

  /**
   * Set this selection of item using the specified array of enum type.
   *
   * @param enums the items
   */
  public void setSelection(EnumerationType ... enums) {
    setSelection(Arrays.asList(enums));
  }



  /**
   * Set this selection of item using the specified array of enum type.
   *
   * @param enums the items
   */
  public void setSelection(Collection enums) {
    clear();
    for (Object item : enums) {
      if (item instanceof EnumerationType) {
        Class itemClass = item.getClass();
        if (enumType == null) {
          setEnumType(itemClass);
        } else if (itemClass.isAssignableFrom(enumType) && !itemClass.equals(enumType)) {
          setEnumType(itemClass);
        }
        add((EnumerationType) item);
      }
    }
  }


  /**
   * Return a string representation of this selection. The string is constituted with the code or the internal value
   * of each item of the selection separated by a <code>;</code>.
   *
   * @return a string representation of this selection
   */
  public String getSelectionAsString() {
    return getSelectionAsString(DEFAULT_SEPARATOR);
  }

  /**
   * Return a string representation of this selection. The string is constituted with the code or the internal value
   * of each item of the selection separated by the specified separator.
   *
   * @param separator the separator to use
   * @return a string representation of this selection
   */
  public String getSelectionAsString(String separator) {
    StringBuilder bf = new StringBuilder();
    for (Iterator iterator = enumSet.iterator(); iterator.hasNext(); ) {

        EnumType enumType = (EnumType) iterator.next();
        bf.append(enumType.getValue());
      
      if (iterator.hasNext()) {
        bf.append(separator);
      }
    }
    return bf.toString();
  }

  public String toString() {
    return toString(Language.DEFAULT, ", ", false);
  }

  public String toString(Language language, String separator, boolean longLabel) {
    StringBuilder bf = new StringBuilder();
    for (Iterator iterator = enumSet.iterator(); iterator.hasNext(); ) {

        EnumType enumType = (EnumType) iterator.next();
        bf.append(enumType.toString(language));
      
      if (iterator.hasNext()) {
        bf.append(separator);
      }
    }
    return bf.toString();
  }

  /**
   * Creates and returns a copy of this selection of enum.
   * <br>The copy has the same value of the enumeration.
   *
   * @return a clone of this instance.
   * @throws CloneNotSupportedException if the object's class does not
   *                                    support the <code>Cloneable</code> interface. Subclasses
   *                                    that override the <code>clone</code> method can also
   *                                    throw this exception to indicate that an instance cannot
   *                                    be cloned.
   * @see java.lang.Cloneable
   */
  public Object clone() throws CloneNotSupportedException {
    ListEnum clone = (ListEnum) super.clone();
    clone.enumSet = new HashSet<EnumerationType>();
    for (Iterator iterator = enumSet.iterator(); iterator.hasNext(); ) {
      EnumerationType enumeration = (EnumerationType) iterator.next();
      clone.add(enumeration);
    }
    return clone;
  }


}
