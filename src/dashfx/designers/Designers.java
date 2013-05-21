/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.designers;

import java.util.HashMap;
import javafx.beans.property.*;

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
		all.put(Boolean.class, BoolDesigner.class);
	}

	public static void addDesigner(Class type, Class designer)
	{
		all.put(type, designer);
	}

	public static PropertyDesigner getDesignerForClass(Class type)
	{
		//TODO: if not found, search entire classpath
		Class dznrClz = all.get(type);
		if (dznrClz == null)
			return null; //TODO: should this throw stuff?
		try
		{
			return (PropertyDesigner) dznrClz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException ex)
		{
			return null;
		}
	}

	public static PropertyDesigner getDesignerFor(Property p)
	{
		Class it = null;
		//FIXME: SO HACK!
		if (p instanceof DoubleProperty)
		{
			it = Double.class;
		}
		else if (p instanceof StringProperty)
		{
			it = String.class;
		}
		else if (p instanceof BooleanProperty)
		{
			it = Boolean.class;
		}
		PropertyDesigner pd = getDesignerForClass(it);
		pd.design(p);
		return pd;
	}
}
