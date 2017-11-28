package com.efluid.ecore.courbe.utils;

import java.util.*;
import java.util.Map.Entry;

import com.efluid.ecore.courbe.businessobject.Valeur;

/**
 * implémentation de Entry<Date, Valeur>
 */
public class EntryDateValeur implements Entry<Date, Valeur> {
    Date date;
    Valeur valeur;
    
    public EntryDateValeur(Date date, Valeur valeur) {
      this.date = date;
      this.valeur = valeur;
    }
    
    @Override
    public Date getKey() {
      return this.date;
    }

    @Override
    public Valeur getValue() {
      return this.valeur;
    }

    @Override
    public Valeur setValue(Valeur value) {
      Valeur ancienneValeur = this.valeur;
      this.valeur = value;
      return ancienneValeur;
    }
  }