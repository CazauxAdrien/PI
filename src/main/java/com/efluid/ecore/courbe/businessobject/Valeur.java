package com.efluid.ecore.courbe.businessobject;

import java.io.ObjectInput;
import java.util.*;

/**
 * Cette classe repr�sente une valeur associ�e ou non � une r�f�rence
 * temporelle. La classe est s�rialisable (avec gestion de version) par
 * {@link SerializationExterne}.
 */
public class Valeur implements Comparable<Valeur> {
  /** Instance singleton du comparateur par date pour les valeurs. */
  public static final Comparator<Valeur> COMPARATEUR_PAR_DATE = new ComparateurValeur();


	/**
	 * Instance singleton du comparateur par d�faut pour les valeurs. R�f�rence
	 * {@link #COMPARATEUR_PAR_DATE}.
	 */
	public static final Comparator<Valeur> COMPARATEUR_PAR_DEFAUT = COMPARATEUR_PAR_DATE;

	/**
	 * <b>Version post 0.0.8</b> : le num�ro de version est le
	 * {@value #VERSION_0} . Sont enregistr�es dans l'ordre les attributs
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
	 * Incr�menter � chaque changement dans la structure de la classe pour la
	 * s�rialisation et d�s�rialisation (et ajouter un
	 * {@link #readExternal(ObjectInput)} corresspondant). Les changements dans
	 * la classes {@link ReferenceTemporelle} doivent aussi faire changer cette
	 * version.
	 */
	private static final int VERSION = VERSION_0;

	/** La r�f�rence temporelle de cette valeur. */
	private ReferenceTemporelle referenceTemporelle;

	/** La valeur de cette valeur. */
	private final ValeurInterne enveloppeDouble;

	/**
	 * N�cessaire pour l'interface {@link SerializationExterne}, ne fait rien, ce
	 * commentaire est pour r�f�rence.
	 */
	public Valeur() {
		super();
    this.enveloppeDouble = initialiserEnveloppe();
	}

	/** Constructeur par d�faut. */
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
	 * Retourne la r�f�rence temporelle.
	 * 
	 * @return
	 */
	public ReferenceTemporelle getReferenceTemporelle() {
		return this.referenceTemporelle;
	}

	/** Met la r�f�rence temporelle. */
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

	/** Retourne la date de la r�f�rence temporelle */
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
	 * M�thode utilitaire pour retourner la version de la classe de cette
	 * instance lorsque le type est masqu�. � surcharger dans les sous-classes.
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
	 * Retourne la concat�nation de la r�f�rence temporelle et de la valeur.
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
  

  /** @return la valeur a afficher pour le champ identifi� par cle sur le menu droit du composant graphique */
  public String getValeurFormattee(Integer cle) {
    return "";
  }
  
  /**
   * @return une nouvelle instance de l'enveloppe contenant les donn�es de la valeur
   */
  protected ValeurInterne initialiserEnveloppe() {
    return new ValeurInterne();
  }
}
