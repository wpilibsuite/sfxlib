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
package dashfx.lib.data.values;

/**
 *
 * @author patrick
 */


public class BooleanSmartValue extends SingleValueSmartValueAdapter
{

	public BooleanSmartValue(Boolean bool)
	{
		setRaw(bool);
	}


	@Override
	public Double asNumber()
	{
		return asBoolean() ? 1.0 : 0.0;
	}

	@Override
	public boolean isNumber()
	{
		return false;
	}

	@Override
	public String asString()
	{
		return String.valueOf(asBoolean());
	}

	@Override
	public boolean isString()
	{
		return false;
	}

	@Override
	public Boolean asBoolean()
	{
		return (Boolean) asRaw();
	}

	@Override
	public boolean isBoolean()
	{
		return true;
	}

}
