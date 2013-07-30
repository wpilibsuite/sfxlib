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

import dashfx.lib.controls.*;
import dashfx.lib.data.*;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

/**
 *
 * @author patrick
 */
public class SendableChooser implements Control
{
	private ComboBox cbox = new ComboBox();
	private final ChangeListener nameChange = new ChangeListener()
	{
		@Override
		public void changed(ObservableValue ov, Object t, Object t1)
		{
			if (provider == null)
				return;
			// remove old bindings
			if (options != null)
				options.removeListener(optionsChange);
			if (selected != null)
				selected.removeListener(selectedChange);
			cbox.itemsProperty().unbind();

			//get new OV
			deFault = provider.getObservable(getName() + "/default");
			options = provider.getObservable(getName() + "/options");
			selected = provider.getObservable(getName() + "/selected");

			// add new bindings
			options.addListener(optionsChange);
			selected.addListener(selectedChange);
			optionsChange.changed(null, null, null);
		}
	},
		optionsChange = new ChangeListener()
	{
		@Override
		public void changed(ObservableValue ov, Object t, Object t1)
		{
			ObservableList ary = options.getData().asArray();
			cbox.itemsProperty().set(ary);
			String selection = "";
			if (selected.isEmpty() || selected.getData().isHash())
			{
				selection = deFault.getData().asString();
			}
			else
			{
				selection = selected.getData().asString();
			}
			int indx = ary.indexOf(selection);
			if (indx >= 0)
			{
				cbox.getSelectionModel().clearAndSelect(indx);
			}
		}
	},
		selectedChange = new ChangeListener()
	{
		@Override
		public void changed(ObservableValue ov, Object t, Object t1)
		{
			ObservableList ary = options.getData().asArray();
			String selection = "";
			if (!(selected.isEmpty() || selected.getData().isHash()))
			{
				selection = selected.getData().asString();
			}
			int indx = ary.indexOf(selection);
			if (indx >= 0)
			{
				cbox.getSelectionModel().clearAndSelect(indx);
			}
		}
	};
	private DataCoreProvider provider;
	private SmartValue deFault, options, selected;

	public SendableChooser()
	{
		nameProperty().addListener(nameChange);
		cbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue ov, Object t, Object t1)
			{
				if (selected != null && !selected.getData().asString().equals(t1.toString()))
				{
					selected.setValue(t1.toString());
				}
			}
		});
		cbox.setMaxHeight(Double.MAX_VALUE);
		cbox.setMaxWidth(Double.MAX_VALUE);
	}

	@Override
	public Node getUi()
	{
		return cbox;
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

	@Override
	public void registered(DataCoreProvider provider)
	{
		this.provider = provider;
		nameChange.changed(null, null, null);
	}
}
