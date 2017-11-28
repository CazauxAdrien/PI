package com.imrglobal.framework.utils;

/**
 * Classe de conversion des attributs non définis en chaine de caractères vide
 * et réciproquement (pour affichage des contrôles dans les jsp)
 */

public class UndefinedAttributes {

	public static final double UNDEF_DOUBLE = Double.MIN_VALUE;
	public static final float UNDEF_FLOAT = Float.MIN_VALUE;
	public static final int UNDEF_INT = Integer.MIN_VALUE;
	public static final long UNDEF_LONG = Long.MIN_VALUE;
	public static final short UNDEF_SHORT = Short.MIN_VALUE;

	/**
	 * Constructeur par défaut
	 */
	public UndefinedAttributes() {
	}

	/**
	 * Méthode de conversion des valeurs de type double non définies en chaine
	 * de caractères vide
	 */
	public static String toString(double attribute) {
		if (attribute == UNDEF_DOUBLE)
			return "";
		else
			return String.valueOf(attribute);
	}

	/**
	 * Méthode de conversion des valeurs de type float non définies en chaine
	 * de caractères vide
	 */
	public static String toString(float attribute) {
		if (attribute == UNDEF_FLOAT)
			return "";
		else
			return String.valueOf(attribute);
	}

	/**
	 * Méthode de conversion des valeurs de type int non définies en chaine
	 * de caractères vide
	 */
	public static String toString(int attribute) {
		if (attribute == UNDEF_INT)
			return "";
		else
			return String.valueOf(attribute);
	}

	/**
	 * Méthode de conversion des valeurs de type long non définies en chaine
	 * de caractères vide
	 */
	public static String toString(long attribute) {
		if (attribute == UNDEF_LONG)
			return "";
		else
			return String.valueOf(attribute);
	}

	/**
	 * Méthode de conversion des valeurs de type short non définies en chaine
	 * de caractères vide
	 */
	public static String toString(short attribute) {
		if (attribute == UNDEF_SHORT)
			return "";
		else
			return String.valueOf(attribute);
	}
}
