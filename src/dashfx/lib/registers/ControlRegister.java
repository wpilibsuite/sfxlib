/*
 * Copyright (C) 2013 patrick
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dashfx.lib.registers;

import dashfx.lib.controls.Designable;
import dashfx.meta.registers.DesignableControlIntRes;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author patrick
 */
public class ControlRegister
{
	private static ArrayList<Class> knownClasses = null;

	public static void register(Class clz)
	{
		if (knownClasses == null)
		{
			knownClasses = new ArrayList<>(Arrays.asList(DesignableControlIntRes.KNOWN));
		}
		knownClasses.add(clz);
	}

	public static Class[] getAll()
	{
		if (knownClasses == null)
		{
			knownClasses = new ArrayList<>(Arrays.asList(DesignableControlIntRes.KNOWN));
		}
		return knownClasses.toArray(new Class[]{});
	}

	public static InputStream getDesignableImage(Class clz)
	{
		String imgRel = ((Designable)clz.getAnnotation(Designable.class)).image();
		if (imgRel.isEmpty())
			return null;
		return ControlRegister.class.getResourceAsStream(imgRel);
	}
}
