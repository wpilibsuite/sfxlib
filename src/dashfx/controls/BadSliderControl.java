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

import dashfx.lib.data.SupportedTypes;
import dashfx.lib.controls.Designable;
import dashfx.lib.data.*;
import dashfx.lib.controls.*;
import dashfx.lib.data.values.DoubleSmartValue;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.Node;
import javafx.scene.control.Slider;

/**
 *
 * @author patrick
 */
@Designable(value = "Raw Slider", image = "/dashfx/controls/res/BadSlider.png", description = "Uses built-in slider")
@DesignableProperty(value =
{
	"value",
	"min",
	"max"
}, descriptions =
{
	"The value of it, duh",
	"The minimum value",
	"The maximum value"
})
@Category("General")
@SupportedTypes(
{
	SmartValueTypes.Number
})
public class BadSliderControl extends Slider implements Control, ChangeListener<Object>
{
	private StringProperty name = new SimpleStringProperty();
	private SmartValue ov = null;
	private boolean modifying = false;

	public BadSliderControl()
	{
		this.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> oov, Number t, Number t1)
			{
				if (!modifying && ov != null && t1.doubleValue() != ov.getData().asNumber())
				{
					ov.setData(new DoubleSmartValue(t1.doubleValue()));
				}
			}
		});
	}

	@Designable(value = "Name", description = "The name the control binds to")
	public StringProperty nameProperty()
	{
		return name;
	}

	public String getName()
	{
		return name.getValue();
	}

	public void setName(String value)
	{
		name.setValue(value);
	}

	@Override
	public void registered(final DataCoreProvider provider)
	{
		this.setMax(1.0);
		this.setMin(-1.0);
		this.setValue(0.5);
		//TODO: add better getObservable Overrides
		if (getName() != null)
		{
			(ov = provider.getObservable(getName())).addListener(this);
		}
		name.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				if (t != null)
					provider.getObservable(t).removeListener(BadSliderControl.this);
				(BadSliderControl.this.ov = provider.getObservable(t1)).addListener(BadSliderControl.this);
			}
		});
	}

	@Override
	public void changed(ObservableValue<? extends Object> ov, Object t, Object t1)
	{
		modifying = true;
		setValue(((SmartValue) ov).getData().asNumber());
		modifying = false;
	}

	@Override
	public Node getUi()
	{
		return this;
	}
}
