package com.imrglobal.framework.businessobject;

/**
 * This interface defines the services which must be offered by an object which will
 * be used as a goup of properties. Each property is defined by its name.
 */
public interface PropertiesValues extends java.io.Serializable {

	/**
	 * Retrieves the value of the property corresponding with the given key.
	 * @param propertyName name of the property.
	 * @return value of the property.
	 */
	public Object getValue(String propertyName);

	/**
	 * Set the value of the property with the given name.
	 * @param propertyName name of the property.
	 * @param value of the property.
	 * @return the previous value of the specified property,
	 * or null if it did not have one.
	 */
	public Object setValue(String propertyName, Object value);

}