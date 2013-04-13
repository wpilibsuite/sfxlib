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

import dashfx.data.DataCoreProvider;
import dashfx.data.Registerable;
import dashfx.data.SmartValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

/**
 *
 * @author patrick
 */
@Designable(value = "Bad Slider Control", description = "Uses built-in slider. Horrible")
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
public class BadSliderControl extends Slider implements Registerable, ChangeListener<Object>
{
	StringProperty name = new SimpleStringProperty("sin");

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
			provider.getObservable(getName()).addListener(this);
		}
		name.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				provider.getObservable(t).removeListener(BadSliderControl.this);
				provider.getObservable(t1).addListener(BadSliderControl.this);
			}
		});
	}

	@Override
	public void changed(ObservableValue<? extends Object> ov, Object t, Object t1)
	{
		setValue(((SmartValue)ov).asNumber(0));
	}
}
