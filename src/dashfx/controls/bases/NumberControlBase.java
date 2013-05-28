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
import dashfx.lib.data.SupportedTypes;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author patrick
 */
@SupportedTypes({SmartValueTypes.Number})
public class NumberControlBase extends ControlBase
{
	private SimpleDoubleProperty value = new SimpleDoubleProperty(this, "value", 0.0);

	public double getValue()
	{
		return value.get();
	}

	public void setValue(double value)
	{
		this.value.set(value);
	}

	@Designable(value = "Value", description = "The value of the control")
	public SimpleDoubleProperty valueProperty()
	{
		return value;
	}


	private SimpleDoubleProperty min = new SimpleDoubleProperty(this, "min");

	public double getMin()
	{
		return min.get();
	}

	public void setMin(double min)
	{
		this.min.set(min);
	}

	@Designable(value = "Min", description = "The minimum value of the control")
	public SimpleDoubleProperty minProperty()
	{
		return min;
	}
	private SimpleDoubleProperty max = new SimpleDoubleProperty(this, "max");

	public double getMax()
	{
		return max.get();
	}

	public void setMax(double max)
	{
		this.max.set(max);
	}

	@Designable(value = "Min", description = "The maximum value of the control")
	public SimpleDoubleProperty maxProperty()
	{
		return max;
	}

	@Override
	protected void changed(Object newValue)
	{
		if (newValue instanceof Integer)
			setValue((Integer) newValue);
		else if (newValue instanceof Double)
			setValue((Double) newValue);
		else if (newValue instanceof Float)
			setValue((Float) newValue);
		else
		{
			try
			{
				setValue(Double.valueOf(newValue.toString()));
			}
			catch (Throwable t)
			{
				setValue(0.0);
			}
		}
	}
}
