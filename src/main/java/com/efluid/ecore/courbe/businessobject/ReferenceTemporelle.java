package com.efluid.ecore.courbe.businessobject;

import java.io.Serializable;
import java.util.Date;

/**
 * Cette classe représente une référence temporelle sous forme d'une date de
 * début.
 */
public class ReferenceTemporelle implements Serializable {

	/** Ne doit jamais changer. */
	private static final long serialVersionUID = 8284544119164483774L;
	
	/** La date de début de la référence temporelle */
	private Date date;

	/** Constructeur par défaut. */
	public ReferenceTemporelle() {
		super();
	}

	/** Construction de la référence temporelle avec cette date. */
	public ReferenceTemporelle(Date date) {
		super();
		this.date = date;
	}

	/** Retourne la date de la référence temporelle */
	public Date getDate() {
		return this.date;
	}

	/** Met la date dans la référence temporelle */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Cette implémentation retourne la date de cette référence temporelle sous
	 * forme de {@link String}.
	 */
	@Override
	public String toString() {
		if (null == this.date) {
			return super.toString();
		}
		return this.date.toString();
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
		result = 31 * result + (null == this.date ? 0 : this.date.hashCode());
		return result;
	}

}
