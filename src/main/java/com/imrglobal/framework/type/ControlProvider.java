package com.imrglobal.framework.type;

/**
 * The interface has to be implemented by classes whose basic toString method
 * does not return the value involved in controls.<br>
 * Exemple : Quantity.toString returns a number concatenated with a unit<br>
 * => in range controls, the value to be compared is the number, but the
 * unit doesn't have to be included. That's why we have to use toStringControl
 * instead of toString
 */
public interface ControlProvider {

	public String toStringControl();
}
