/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.lib.data;

import java.util.*;

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

	public SimpleTransaction(ValueTransaction vt)
	{
		this.values.addAll(Arrays.asList(vt.getValues()));
		this.deleted.addAll(Arrays.asList(vt.getDeletedNames()));
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

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{Simple Transaction:: ");
		for (SmartValue sv : values)
		{
			sb.append(sv.toString());
			sb.append(",   ");
		}
		sb.append("}");
		return sb.toString();
	}



	public static SimpleTransaction merge(ValueTransaction st, ValueTransaction get)
	{
		//TOOD: deleted items!
		SimpleTransaction qt = new SimpleTransaction(st);
		//TODO: non-optimal
		HashMap<String, Integer> allTheNames =  new HashMap<>(st.getValues().length + get.getValues().length);
		for (int i = 0; i < qt.values.size(); i++)
		{
			allTheNames.put(qt.values.get(i).getName(), i);
		}
		for (SmartValue smartValue : get.getValues())
		{
			if (allTheNames.containsKey(smartValue.getName()))
			{
				qt.values.set(allTheNames.get(smartValue.getName()), smartValue);
			}
			else
				qt.addValue(smartValue);
		}
		return qt;
	}
}
