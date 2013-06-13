/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.lib.designers;

import java.util.HashMap;

/**
 *
 * @author patrick
 */
@SuppressWarnings("StaticNonFinalUsedInInitialization")
public class Designers
{
	private static HashMap<Class, Class> all = new HashMap<>();

	static
	{
		// speed known lookups
		all.put(String.class, StringDesigner.class);
		all.put(Double.class, NumberDesigner.class);
		all.put(Integer.class, NumberDesigner.class);
		all.put(Float.class, NumberDesigner.class);
		all.put(Number.class, NumberDesigner.class);
		all.put(double.class, NumberDesigner.class);
		all.put(int.class, NumberDesigner.class);
		all.put(float.class, NumberDesigner.class);
		all.put(long.class, NumberDesigner.class);
		all.put(short.class, NumberDesigner.class);
		all.put(Boolean.class, BoolDesigner.class);
		all.put(boolean.class, BoolDesigner.class);
		all.put(Enum.class, EnumDesigner.class);
	}

	public static void addDesigner(Class type, Class designer)
	{
		all.put(type, designer);
	}

	public static PropertyDesigner getFor(Class type)
	{
		//TODO: if not found, search entire classpath
		Class dznrClz = all.get(type);
		if (type.isEnum())
			dznrClz = all.get(Enum.class);
		if (dznrClz == null)
			return null;
		try
		{
			PropertyDesigner pd = (PropertyDesigner) dznrClz.newInstance();
			if (type.isEnum())
				((EnumDesigner)pd).setEnum(type);
			return pd;
		}
		catch (InstantiationException | IllegalAccessException ex)
		{
			return null;
		}
	}
}
