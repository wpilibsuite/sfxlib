/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.data;

import java.util.HashMap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;

/**
 * This class provides a single way to represent all possible data
 */
public class SmartValue extends SimpleObjectProperty<Object>
{
	private String name;
	private String groupName = "";
	private SmartValueTypes type;

	public SmartValue(Object data, SmartValueTypes type, String name)
	{
		super(data);
		this.name = name;
		this.type = type;
	}

	public SmartValue(Object data, SmartValueTypes type, String name, String groupName)
	{
		super(data);
		this.groupName = groupName;
		this.name = name;
		this.type = type;
	}

	public SmartValue()
	{
		super(null);
		name = "";
		type = SmartValueTypes.Unknown;
	}

	public boolean isEmpty()
	{
		return getValue() == null;
	}

	public double asNumber()
	{
		return asNumber(0.0);
	}

	public double asNumber(double defaultValue)
	{
		if (isEmpty())
			return defaultValue;
		if (getValue() instanceof Double || getValue() instanceof Float || getValue() instanceof Integer)
			return (double) getValue();
		// TODO: shoud we try parsing strings?
		return defaultValue;
	}

	public boolean isNumber()
	{
		return asNumber(0.0) == asNumber(1.0);
	}

	public String asString()
	{
		if (getValue() == null)
			return null;
		return getValue().toString();
	}

	public boolean isString()
	{
		return (getValue() instanceof String);
	}

	// TODO: shoudld the hash's be quiet about if they are observable or not and coalase?
	public ObservableMap<String, SmartValue> asHash()
	{
		return asHash(null);
	}

	public ObservableMap<String, SmartValue> asHash(ObservableMap<String, SmartValue> defaultValue)
	{
		if (getValue() instanceof ObservableMap)
			return (ObservableMap<String, SmartValue>) getValue();
		return defaultValue;
	}

	public boolean isHash()
	{
		return asHash() != null;
	}

	public HashMap<String, SmartValue> asRawHash()
	{
		return asRawHash(null);
	}

	public HashMap<String, SmartValue> asRawHash(HashMap<String, SmartValue> defaulltValue)
	{
		if (getValue() instanceof HashMap)
			return (HashMap<String, SmartValue>) getValue();
		return defaulltValue;
	}

	public boolean isRawHash()
	{
		return asRawHash() != null;
	}

	public SmartValue getSubKey(String name)
	{
		return getSubKey(name, false);
	}

	public SmartValue getSubKey(String name, boolean quiet)
	{
		HashMap<String, SmartValue> hm = asRawHash();
		if (hm != null)
			return hm.get(name);
		ObservableMap<String, SmartValue> om = asHash();
		if (om != null)
			return om.get(name);
		if (quiet)
			return null;
		//TODO: what should we do?
		throw new RuntimeException("Not a hashmap you dimwit!");
	}

	public void setData(Object o)
	{
		super.setValue(o);
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	/**
	 * @return the type
	 */
	public SmartValueTypes getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SmartValueTypes type)
	{
		this.type = type;
	}
}
