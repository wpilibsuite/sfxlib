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
package dashfx.controls.bases;

import dashfx.lib.controls.Category;
import dashfx.lib.controls.Designable;
import dashfx.lib.data.values.SmartValueAdapter;
import javafx.beans.property.*;

/**
 *
 * @author patrick
 */
public class StringControlBase extends ControlBase
{
	private SimpleStringProperty value = new SimpleStringProperty(this, "value");

	public String getValue()
	{
		return value.get();
	}

	public void setValue(String value)
	{
		this.value.set(value);
	}

	@Designable(value = "Value", description = "The value of the control")
	@Category("Data")
	public SimpleStringProperty valueProperty()
	{
		return value;
	}

	@Override
	protected void changed(Object newValue, SmartValueAdapter data)
	{
		setValue(data.asString());
	}
}

