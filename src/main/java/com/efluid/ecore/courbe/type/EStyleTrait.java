package com.efluid.ecore.courbe.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.imrglobal.framework.type.EnumType;

import com.hermes.arc.commun.type.HermesNLSEnumType;

/**
 * Cette classe représente les styles de trait sous forme d'un énuméré statique.
 */
public class EStyleTrait extends HermesNLSEnumType {
	private static final int _PLEIN = 0;
	public static final EStyleTrait PLEIN = new EStyleTrait(_PLEIN);
	private static final int _POINTILLE = 1;
	public static final EStyleTrait POINTILLE = new EStyleTrait(_POINTILLE);
	private static final int _DOUBLE = 2;
	public static final EStyleTrait DOUBLE = new EStyleTrait(_DOUBLE);

	private EStyleTrait(int v) {
		setValue(v);
	}

	public static EnumType from_int(int value) {
		switch (value) {
		case _PLEIN:
			return PLEIN;
		case _POINTILLE:
			return POINTILLE;
		case _DOUBLE:
			return DOUBLE;
		default:
			return null;
		}
	}

	@Override
	public Iterator<EStyleTrait> iterator() {
		List<EStyleTrait> l = new ArrayList<EStyleTrait>();
		l.add(PLEIN);
		//l.add(POINTILLE); //CTH - pour l'instant on ne propose pas car pas supportee par JQplot
		l.add(DOUBLE);
		return l.iterator();
	}

}