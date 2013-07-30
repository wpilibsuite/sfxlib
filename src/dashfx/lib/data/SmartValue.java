/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.lib.data;

import dashfx.lib.data.values.SmartValueAdapter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.*;

/**
 * This class provides a single way to represent all possible data
 */
public class SmartValue extends SimpleObjectProperty<Object>
{
	private String name;
	private String groupName = "";
	private SmartValueTypes type;
	private SmartValueAdapter adapter;

	public SmartValue(SmartValueAdapter data, SmartValueTypes type, String name)
	{
		super(data.asRaw());
		this.adapter = data;
		this.name = name;
		this.type = type;
	}

	public SmartValue(SmartValueAdapter data, SmartValueTypes type, String name, String groupName)
	{
		super(data.asRaw());
		this.adapter = data;
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
		return super.getValue() == null;
	}

	public SmartValue getSubKey(String name)
	{
		return getSubKey(name, false);
	}

	public SmartValue getSubKey(String name, boolean quiet)
	{
		ObservableMap<String, SmartValue> om = getData().asHash();
		if (om != null)
			return om.get(name);
		if (quiet)
			return null;
		//TODO: what should we do?
		throw new RuntimeException("Not a hashmap you dimwit!");
	}

	public void setData(SmartValueAdapter adpt)
	{
		this.adapter = adpt;
		super.setValue(adpt.asRaw());
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

	@Override
	public String toString()
	{
		return String.format("SmartValue [Name: %s, Group Name: %s, Type: %s, Value: %s]", getName(), getGroupName(), getType(), getValue());
	}

	public SmartValueAdapter getData()
	{
		return this.adapter;
	}
}
