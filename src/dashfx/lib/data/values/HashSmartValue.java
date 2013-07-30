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

import dashfx.lib.data.SmartValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author patrick
 */


public class HashSmartValue extends SmartValueAdapterBase
{

	public HashSmartValue(ObservableMap<String, SmartValue> observableHashMap)
	{
		setRaw(observableHashMap);
	}

	@Override
	public Double asNumber()
	{
		return 0.0;
	}

	@Override
	public boolean isNumber()
	{
		return false;
	}

	@Override
	public String asString()
	{
		return String.valueOf(asRaw());
	}

	@Override
	public boolean isString()
	{
		return false;
	}

	@Override
	public Boolean asBoolean()
	{
		return asRaw() != null;
	}

	@Override
	public boolean isBoolean()
	{
		return false;
	}

	@Override
	public boolean isArray()
	{
		return false;
	}

	@Override
	public ObservableList asArray()
	{
		return FXCollections.emptyObservableList();
	}

	@Override
	public ObservableMap<String, SmartValue> asHash()
	{
		return (ObservableMap<String, SmartValue>) asRaw();
	}

	@Override
	public boolean isHash()
	{
		return true;
	}

}
