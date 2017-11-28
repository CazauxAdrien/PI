package com.efluid.ecore.temps.type;

import com.imrglobal.framework.type.TimeGap;

/**
 * Interface décrivant des types énumérés représentant des durées
 * 
 * @author christophe.thibaut
 * 
 */
public interface ITypeDuree extends Comparable<ITypeDuree> {

	/**
	 * Retourne la durée de la valeur du type énuméré.
	 * 
	 * <p>
	 * Pour les durées supérieure ou égale à un mois, il s'agit d'une durée
	 * approximative (les mois n'ont pas tous la même durée, prise en compte des
	 * années bissextiles etc..).
	 * 
	 * <p>
	 * Pour les durées supérieure ou égale à un mois, la méthode
	 * {@link #getNbMois()} permet de les exprimer en nombre de mois.
	 * 
	 * <p>
	 * Pour les durées supérieure ou égale à un an, la méthode
	 * {@link #getNbAnnees()} permet de les exprimer en nombre d'années.
	 * 
	 * @return la durée de la valeur sous forme d'instance de {@link TimeGap}
	 */
	public TimeGap getDuree();

	/**
	 * Pour les valeurs représentant des durées supérieure ou égale à un mois,
	 * retourne la durée sous la forme d'un nombre de mois
	 * 
	 * @return le nombre de mois correspondant à la durée si celle-ci est
	 *         supérieure ou égale à un mois, 0 sinon
	 */
	public int getNbMois();

	/**
	 * Pour les valeurs représentant des durées supérieure ou égale à une
	 * années, retourne la durée sous la forme d'un nombre d'années
	 * 
	 * @return le nombre d'années correspondant à la durée si celle-ci est
	 *         supérieure ou égale à une années, 0 sinon
	 */
	public int getNbAnnees();

}
