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
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author patrick
 */
public interface SmartValueAdapter
{
	public boolean isArray();

	public ObservableList asArray();

	public Double asNumber();

	public boolean isNumber();

	public String asString();

	public boolean isString();

	public ObservableMap<String, SmartValue> asHash();

	public boolean isHash();

	public Boolean asBoolean();

	public boolean isBoolean();

	public Object asRaw();
}
