package com.efluid.ecore.courbe.businessobject;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface ValeurSerialisable {
	
	public int getVersion();

	public void ecrireObjet(ObjectOutputStream oos) throws IOException;

}
