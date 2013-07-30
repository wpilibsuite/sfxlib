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

import dashfx.lib.controls.Designable;
import dashfx.lib.data.SmartValueTypes;
import dashfx.lib.data.values.BooleanSmartValue;
import dashfx.lib.data.values.SmartValueAdapter;
import javafx.beans.property.*;
import javafx.beans.value.*;

/**
 *
 * @author patrick
 */
public class BooleanControlBase extends ControlBase
{
	private SimpleBooleanProperty value = new SimpleBooleanProperty(this, "value", false);

	public BooleanControlBase()
	{
		value.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1)
			{
				if (!ignore && getSmartValue() != null)
				{
					ignore = true;
					getSmartValue().setType(SmartValueTypes.Boolean);
					getSmartValue().setData(new BooleanSmartValue(t1));
					dblvalue.set(t1 ? 1.0 : 0.0);
					ignore = false;
				}
			}
		});
	}

	public boolean getValue()
	{
		return value.get();
	}

	public void setValue(boolean value)
	{
		this.value.set(value);
	}

	@Designable(value = "Value", description = "The value of the control")
	public SimpleBooleanProperty valueProperty()
	{
		return value;
	}
	private ReadOnlyDoubleWrapper dblvalue = new ReadOnlyDoubleWrapper(this, "numberValue");

	public double getNumberValue()
	{
		return dblvalue.get();
	}

	public ReadOnlyDoubleProperty numberValueProperty()
	{
		return dblvalue.getReadOnlyProperty();
	}

	@Override
	protected void changed(Object newValue, SmartValueAdapter data)
	{
		setValue(data.asBoolean());
	}
}
