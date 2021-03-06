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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author patrick
 */
@SupportedTypes(
	{
	SmartValueTypes.Number
})
public class NumberControlBase extends ControlBase
{
	protected SimpleDoubleProperty value = new SimpleDoubleProperty(this, "value", 0.0);

	public NumberControlBase()
	{
		value.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number t, Number t1)
			{
				simpleValueChanged(t1);
			}
		});
	}

	protected void simpleValueChanged(Number t1)
	{
		if (!ignore && getSmartValue() != null)
		{
			ignore = true;
			getSmartValue().setType(SmartValueTypes.Double);
			getSmartValue().setData(new DoubleSmartValue(t1.doubleValue()));
			double tvalue = t1.doubleValue();
			stringValue.set(String.valueOf(Math.round(tvalue * 100) / 100.0));
			ignore = false;
		}
	}

	public double getValue()
	{
		return value.get();
	}

	public void setValue(double value)
	{
		double tvalue = value;
		this.value.set(tvalue);
		stringValue.set(String.valueOf(Math.round(tvalue * 100) / 100.0));
	}

	@Designable(value = "Value", description = "The value of the control")
	public SimpleDoubleProperty valueProperty()
	{
		return value;
	}
	
	@Override
	protected void changed(Object newValue, SmartValueAdapter data)
	{
		setValue(data.asNumber());
	}
}
