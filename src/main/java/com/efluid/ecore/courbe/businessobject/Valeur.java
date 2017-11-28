package com.efluid.ecore.courbe.businessobject;

import java.io.ObjectInput;
import java.util.*;

/**
 * Cette classe représente une valeur associée ou non à une référence
 * temporelle. La classe est sérialisable (avec gestion de version) par
 * {@link SerializationExterne}.
 */
public class Valeur implements Comparable<Valeur> {
  /** Instance singleton du comparateur par date pour les valeurs. */
  public static final Comparator<Valeur> COMPARATEUR_PAR_DATE = new ComparateurValeur();


	/**
	 * Instance singleton du comparateur par défaut pour les valeurs. Référence
	 * {@link #COMPARATEUR_PAR_DATE}.
	 */
	public static final Comparator<Valeur> COMPARATEUR_PAR_DEFAUT = COMPARATEUR_PAR_DATE;

	/**
	 * <b>Version post 0.0.8</b> : le numéro de version est le
	 * {@value #VERSION_0} . Sont enregistrées dans l'ordre les attributs
	 * suivants :
	 * 
	 * <ul>
	 * <li>L'attribut {@link #referenceTemporelle} sous forme de
	 * {@link ReferenceTemporelle}
	 * <li>L'attribut {@link #valeur} sous forme de <code>double</code>
	 * </ul>
	 */
	private static final int VERSION_0 = 0;

	/**
	 * Ne doit jamais changer. Utilisé par la VM pour déterminer toutes les
	 * classes de {@link Valeur} qui sont "compatibles", donc mutuellement
	 * sérialisables et désérialisables. Cela n'est pas la version, voir plutét
	 * le champs {@link Valeur#version}.<br>
	 * <br>
	 * <b>Attention :</b> c'est bien la méme version pour toutes les classes
	 * filles.
	 */
	private static final long serialVersionUID = 2983845307266774654L;

	/**
	 * Incrémenter é chaque changement dans la structure de la classe pour la
	 * sérialisation et désérialisation (et ajouter un
	 * {@link #readExternal(ObjectInput)} corresspondant). Les changements dans
	 * la classes {@link ReferenceTemporelle} doivent aussi faire changer cette
	 * version.
	 */
	private static final int VERSION = VERSION_0;

	/** La référence temporelle de cette valeur. */
	private ReferenceTemporelle referenceTemporelle;

	/** La valeur de cette valeur. */
	private final ValeurInterne enveloppeDouble;

	/**
	 * Nécessaire pour l'interface {@link SerializationExterne}, ne fait rien, ce
	 * commentaire est pour référence.
	 */
	public Valeur() {
		super();
    this.enveloppeDouble = initialiserEnveloppe();
	}

	/** Constructeur par défaut. */
	public Valeur(double valeur, ReferenceTemporelle referenceTemporelle) {
		super();
    this.enveloppeDouble = initialiserEnveloppe();
		this.setValeur(valeur);
		this.setReferenceTemporelle(referenceTemporelle);
	}

	 public Valeur(ValeurInterne enveloppeDouble, ReferenceTemporelle referenceTemporelle) {
      this.enveloppeDouble = enveloppeDouble;
	    this.setReferenceTemporelle(referenceTemporelle);
	  }
	 
	/**
	 * Retourne la référence temporelle.
	 * 
	 * @return
	 */
	public ReferenceTemporelle getReferenceTemporelle() {
		return this.referenceTemporelle;
	}

	/** Met la référence temporelle. */
	public void setReferenceTemporelle(ReferenceTemporelle referenceTemporelle) {
		this.referenceTemporelle = referenceTemporelle;
	}

	/** Retourne la valeur. */
	public double getValeur() {
		return this.enveloppeDouble.getValeur();
	}

	/** Met la valeur. */
	public void setValeur(double valeur) {
		this.enveloppeDouble.setValeur(valeur);
	}

	/** Retourne la date de la référence temporelle */
	public Date getDate() {
		if (null == this.referenceTemporelle) {
			return null;
		}
		return this.referenceTemporelle.getDate();
	}
	
	public ValeurInterne getEnveloppeDouble() {
    return enveloppeDouble;
  }

  /**
	 * Méthode utilitaire pour retourner la version de la classe de cette
	 * instance lorsque le type est masqué. é surcharger dans les sous-classes.
	 */
	public int getVersion() {
		return VERSION;
	}


	/** Comparaison de la {@link Valeur} sur la {@link #valeur}. */
	public int compareTo(Valeur o) {
		return Double.valueOf(this.getValeur()).compareTo(Double.valueOf(o.getValeur()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Retourne la concaténation de la référence temporelle et de la valeur.
	 */
	@Override
	public String toString() {
		if (null == this.referenceTemporelle) {
			return "null:" + getValeur();
		}
		return this.referenceTemporelle.toString() + ":" + getValeur();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Voir
	 * 
	 * <ul>
	 * <li>Bloch J. (2011). <i>Effective Java (2nd edition)</i>. Stoughton: Sun
	 * Microsystems.</li>
	 * </ul>
	 */
	@Override
	public int hashCode() {
		int result = 17;
		if (getValeur() > 0) {
			result = 31 * result + (int) (Double.doubleToLongBits(getValeur()) ^ (Double.doubleToLongBits(getValeur()) >>> 32));
			result = 31 * result + (null == this.referenceTemporelle ? 0 : this.referenceTemporelle.hashCode());
		}
		else {
			result = 31 * result + (null == this.referenceTemporelle ? 0 : this.referenceTemporelle.hashCode());
			result = 31 * result + (int) (Double.doubleToLongBits(getValeur()) ^ (Double.doubleToLongBits(getValeur()) >>> 32));
		}
		return result;
	}
	
  @Override
  public boolean equals(Object obj) {
    if (obj != null && getClass().equals(obj.getClass())){
      Valeur valeurObj = (Valeur) obj;
      if (valeurObj.getDate().getTime() == getDate().getTime() && getValeur() == valeurObj.getValeur()){
        return true;
      }
    }
    
    return false;
  }
  

  /** @return la valeur a afficher pour le champ identifié par cle sur le menu droit du composant graphique */
  public String getValeurFormattee(Integer cle) {
    return "";
  }
  
  /**
   * @return une nouvelle instance de l'enveloppe contenant les données de la valeur
   */
  protected ValeurInterne initialiserEnveloppe() {
    return new ValeurInterne();
  }
}
