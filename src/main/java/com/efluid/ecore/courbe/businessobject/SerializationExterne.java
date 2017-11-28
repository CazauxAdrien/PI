package com.efluid.ecore.courbe.businessobject;

import java.io.*;

/** Interface ayant un rôle similaire à {@link Externalizable}, mais tout en évitant de contourner la sérialisation standard */
public interface SerializationExterne extends Serializable {
  /** méthode permettant d'écrire des données à serializer dans au format externe, voir {@link Externalizable#writeExternal(ObjectOutput)} */
  void writeExternal(ObjectOutput out) throws IOException;

  /** méthode permettant de lire les données serializer à partir d'un format externe, voir {@link Externalizable#readExternal(ObjectInput)} */
  void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;
}
