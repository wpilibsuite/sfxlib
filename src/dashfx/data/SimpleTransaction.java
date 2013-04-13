/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author patrick
 */
public class SimpleTransaction implements ValueTransaction
{
	private ArrayList<String> deleted = new ArrayList<>(1);
	private ArrayList<SmartValue> values = new ArrayList<>();

	public SimpleTransaction(String[] deleted)
	{
		this.deleted.addAll(Arrays.asList(deleted));
	}

	public SimpleTransaction(String deleted)
	{
		this.deleted.add(deleted);
	}

	public SimpleTransaction()
	{
	}

	public SimpleTransaction(SmartValue[] values)
	{
		this.values.addAll(Arrays.asList(values));
	}

	public SimpleTransaction(SmartValue value)
	{
		this.values.add(value);
	}

	public SimpleTransaction(SmartValue[] values, String[] deleted)
	{
		this.values.addAll(Arrays.asList(values));
		this.deleted.addAll(Arrays.asList(deleted));
	}

	public void addValue(SmartValue value)
	{
		this.values.add(value);
	}

	public void deleteName(String name)
	{
		this.deleted.add(name);
	}

	@Override
	public String[] getDeletedNames()
	{
		return deleted.toArray(new String[]
		{
		});
	}

	@Override
	public SmartValue[] getValues()
	{
		return values.toArray(new SmartValue[]
		{
		});
	}
}
