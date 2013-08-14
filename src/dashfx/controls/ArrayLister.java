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

import dashfx.lib.data.ObservableArrayAdapter;
import dashfx.lib.controls.Control;
import dashfx.lib.controls.*;
import dashfx.lib.data.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.*;
import javafx.scene.control.*;

/**
 *
 * @author patrick
 */
@Designable(value = "Array View", description = "Displays a list of array elements", image = "/dashfx/controls/res/listview.png")
@Category("General")
@SupportedTypes({
	SmartValueTypes.Array,
	SmartValueTypes.BooleanArray,
	SmartValueTypes.DoubleArray,
	SmartValueTypes.FloatArray,
	SmartValueTypes.IntegerArray,
	SmartValueTypes.ObjectArray,
	SmartValueTypes.StringArray
})
public class ArrayLister implements Control, ChangeListener<Object>
{
	private ListView listView = new ListView();
	private SmartValue smrtVal;
	private ObservableArrayAdapter obstensiblyAntiAircraft = new ObservableArrayAdapter();

	public ArrayLister()
	{
		listView.itemsProperty().bind(obstensiblyAntiAircraft);
	}



	@Override
	public Node getUi()
	{
		return listView;
	}
	@Override
	public void registered(final DataCoreProvider provider)
	{
		//TODO: add better getObservable Overrides
		if (provider == null)
		{
			if (smrtVal != null)
				smrtVal.removeListener(this);
		}
		else if (getName() != null)
		{
			smrtVal = provider.getObservable(getName());
			smrtVal.addListener(this);
			if (smrtVal.getValue() != null && !smrtVal.getData().isHash())
				changed(smrtVal, null, smrtVal.getValue());
		}
		// TODO: don't leak on multiple calls
		nameProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				if (smrtVal != null)
					smrtVal.removeListener(ArrayLister.this);
				smrtVal = provider.getObservable(t1);
				smrtVal.addListener(ArrayLister.this);
				if (smrtVal.getValue() != null && !smrtVal.getData().isHash())
					ArrayLister.this.changed(smrtVal, null, smrtVal.getValue());
			}
		});
	}

	@Override
	public void changed(ObservableValue<? extends Object> ov, Object t, Object t1)
	{
		obstensiblyAntiAircraft.set(smrtVal.getData().asArray());
	}

	private StringProperty name = new SimpleStringProperty(this, "name");
	@Designable(value = "Path", description = "The path the control binds to")
	public StringProperty nameProperty()
	{
		return name;
	}

	public String getName()
	{
		return nameProperty().getValue();
	}

	public void setName(String value)
	{
		nameProperty().setValue(value);
	}
}
