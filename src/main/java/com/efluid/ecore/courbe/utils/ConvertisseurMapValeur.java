package com.efluid.ecore.courbe.utils;

import java.util.*;

import com.efluid.ecore.courbe.businessobject.Valeur;

 /**
 * cette classe permet de transformer une Valeur en Date, Valeur ou Entry<Date, Valeur> en fonction de la classe de conversion demandée
 */
public class ConvertisseurMapValeur<E> {
  private final Class classConversion;
  public ConvertisseurMapValeur(Class<? extends E> classConversion){
    this.classConversion = classConversion;
  }
  
  public Comparator<? super E> getComparateur() {
    return new ComparateurValeurTemporelle<>();
  }

  public E getObjetAttenduDeValeur(Valeur valeur) {
    if (valeur != null){
      if (Date.class.isAssignableFrom(classConversion)){
        return (E) valeur.getDate();
      } else if (Valeur.class.isAssignableFrom(classConversion)){
        return (E) valeur;
      } else if (EntryDateValeur.class.isAssignableFrom(classConversion)){
        return (E) new EntryDateValeur(valeur.getDate(), valeur);
      }
    }
    
    return null;
  }
  
  public E[] getNewArray(int taille){
    if (Date.class.isAssignableFrom(classConversion)){
      return (E[]) new Date[taille];
    } else if (Valeur.class.isAssignableFrom(classConversion)){
      return (E[]) new Valeur[taille];
    } else if (EntryDateValeur.class.isAssignableFrom(classConversion)){
      return (E[]) new EntryDateValeur[taille];
    }
    
    return null;
  }
  
  public Class getClassConversion(){
    return this.classConversion;
  }
}
