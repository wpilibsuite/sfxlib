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
import javafx.collections.*;

/**
 *
 * @author patrick
 */
public abstract class SingleValueSmartValueAdapter extends SmartValueAdapterBase
{
	protected ObservableList ol = FXCollections.observableArrayList(0.0);

	@Override
	public boolean isArray()
	{
		return false;
	}

	protected void updateUL(Object value)
	{
		if (!ol.get(0).equals(value))
			ol.set(0, value);
	}

	@Override
	public ObservableList asArray()
	{
		updateUL(asRaw());
		return ol;
	}

	@Override
	public ObservableMap<String, SmartValue> asHash()
	{
		return null;
	}

	@Override
	public boolean isHash()
	{
		return false;
	}
}
