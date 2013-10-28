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

import dashfx.lib.controls.*;
import dashfx.lib.data.SmartValueTypes;
import dashfx.lib.data.SupportedTypes;
import dashfx.lib.data.values.DoubleSmartValue;
import dashfx.lib.data.values.SmartValueAdapter;
import javafx.beans.property.*;

/**
 *
 * @author patrick
 */
@SupportedTypes(
	{
	SmartValueTypes.Number
})
public class RangedNumberControlBase extends NumberControlBase
{
	@Override
	protected void simpleValueChanged(Number t1)
	{
		if (!ignore && getSmartValue() != null)
		{
			ignore = true;
			getSmartValue().setType(SmartValueTypes.Double);
			getSmartValue().setData(new DoubleSmartValue(t1.doubleValue()));
			double tvalue = (Math.max(getMin(), Math.min(getMax(), t1.doubleValue())));
			if (!getClip())
				tvalue = t1.doubleValue();
			stringValue.set(String.valueOf(Math.round(tvalue * 100) / 100.0));
			ignore = false;
		}
	}

	public void setValue(double value)
	{
		double tvalue = (Math.max(getMin(), Math.min(getMax(), value)));
		if (!getClip())
			tvalue = value;
		this.value.set(tvalue);
		stringValue.set(String.valueOf(Math.round(tvalue * 100) / 100.0));
	}

	@Designable(value = "Value", description = "The value of the control")
	@Range(minProp = "min", maxProp = "max")
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

	@Designable(value = "Clip", description = "Should we Clip the value to [Min..Max]")
	public BooleanProperty clipProperty()
	{
		return clip;
	}
	private SimpleBooleanProperty clip = new SimpleBooleanProperty(this, "clip", true);

	public boolean getClip()
	{
		return clip.get();
	}

	public void setClip(boolean clip)
	{
		this.clip.set(clip);
	}

	@Designable(value = "Min", description = "The minimum value of the control")
	@Category("Basic")
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

	@Designable(value = "Max", description = "The maximum value of the control")
	@Category("Basic")
	public SimpleDoubleProperty maxProperty()
	{
		return max;
	}

	@Override
	protected void changed(Object newValue, SmartValueAdapter data)
	{
		setValue(data.asNumber());
	}
}
