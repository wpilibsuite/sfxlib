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
package dashfx.controls;

import javafx.beans.property.*;
import javafx.beans.property.adapter.ReadOnlyJavaBeanDoubleProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanDoublePropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author patrick
 */
public class BooleanControlBase extends ControlBase
{
	private SimpleBooleanProperty value = new SimpleBooleanProperty(this, "value", false);

	public BooleanControlBase()
	{
		value.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1)
			{
				dblvalue.set(t1 ? 1.0 : 0.0);
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
	protected void changed(Object newValue)
	{
		if (newValue instanceof Integer)
			setValue(0 != (Integer) newValue);
		else if (newValue instanceof Double)
			setValue(0 !=(Double) newValue);
		else if (newValue instanceof Float)
			setValue(0 != (Float) newValue);
		else if (newValue instanceof Boolean)
			setValue((Boolean) newValue);
		else
		{
			try
			{
				setValue(Boolean.valueOf(newValue.toString()));
			}
			catch (Throwable t)
			{
				setValue(false);
			}
		}
	}
}
