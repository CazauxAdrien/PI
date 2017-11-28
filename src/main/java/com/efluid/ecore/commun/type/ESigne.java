package com.efluid.ecore.commun.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.imrglobal.framework.type.EnumType;

import com.hermes.arc.commun.type.HermesNLSEnumType;

public class ESigne extends HermesNLSEnumType {

	private static final int _SIGNE_MOINS = -1;
	public static final ESigne SIGNE_MOINS = new ESigne(_SIGNE_MOINS);
	private static final int _SIGNE_PLUS = 1;
	public static final ESigne SIGNE_PLUS = new ESigne(_SIGNE_PLUS);

	private ESigne(int v) {
		setValue(v);
	}

	public static EnumType from_int(int value) {
		switch (value) {
		case _SIGNE_PLUS:
			return SIGNE_PLUS;
		case _SIGNE_MOINS:
			return SIGNE_MOINS;
		default:
			return null;
		}
	}

	@Override
	public Iterator<ESigne> iterator() {
		List<ESigne> l = new ArrayList<ESigne>();
		l.add(SIGNE_PLUS);
		l.add(SIGNE_MOINS);
		return l.iterator();
	}
}
