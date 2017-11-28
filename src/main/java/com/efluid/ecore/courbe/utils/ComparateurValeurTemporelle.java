package com.efluid.ecore.courbe.utils;

import java.util.*;

import com.efluid.ecore.courbe.businessobject.Valeur;

 /**
 * permet de comparer une date ou une valeur en fonction des cas
 */
public class ComparateurValeurTemporelle<E> implements Comparator<E> {
  @Override
  public int compare(E o1, E o2) {
    if (o1 != null && o2 != null) {
      if (o1 instanceof Date) {
        return ((Date) o1).compareTo((Date) o2);
      } else if (o1 instanceof Valeur) {
        return ((Valeur) o1).getDate().compareTo(((Valeur) o2).getDate());
      }
    } else if (o2 != null) {
      return -1;
    } else if (o1 != null) {
      return 1;
    }

    return 0;
  }
}
