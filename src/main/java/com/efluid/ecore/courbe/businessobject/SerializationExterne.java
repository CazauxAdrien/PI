package com.efluid.ecore.courbe.businessobject;

import java.io.*;

/** Interface ayant un r�le similaire � {@link Externalizable}, mais tout en �vitant de contourner la s�rialisation standard */
public interface SerializationExterne extends Serializable {
  /**
	 * Ne doit jamais changer. Utilis� par la VM pour d�terminer toutes les
	 * classes de {@link Valeur} qui sont "compatibles", donc mutuellement
	 * s�rialisables et d�s�rialisables. Cela n'est pas la version, voir plut�t
	 * le champs {@link Valeur#version}.<br>
	 * <br>
	 * <b>Attention :</b> c'est bien la m�me version pour toutes les classes
	 * filles.
	 */
	static final long serialVersionUID = 2983845307266774654L;

/** m�thode permettant d'�crire des donn�es � serializer dans au format externe, voir {@link Externalizable#writeExternal(ObjectOutput)} */
  void writeExternal(ObjectOutput out) throws IOException;

  /** m�thode permettant de lire les donn�es serializer � partir d'un format externe, voir {@link Externalizable#readExternal(ObjectInput)} */
  void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;
}
