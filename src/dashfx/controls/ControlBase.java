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
import dashfx.data.SmartValue;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;

/**
 *
 * @author patrick
 */
public abstract class ControlBase implements Control, ChangeListener<Object>
{
	@FXML
	public SimpleObjectProperty<Node> ui = new SimpleObjectProperty<>();



	@Override
	@FXML
	public Node getUi()
	{
		return ui.get();
	}
	@FXML
	public void setUi(Node ui)
	{
		this.ui.set(ui);
	}
	@FXML
	public SimpleObjectProperty<Node> uiProperty()
	{
		return this.ui;
	}

	StringProperty name = new SimpleStringProperty(this, "name");

	@Designable(value = "Name", description = "The name the control binds to")
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
	public void registered(final DataCoreProvider provider)
	{
		//TODO: add better getObservable Overrides
		if (getName() != null)
		{
			smrtVal = provider.getObservable(getName());
			smrtVal.addListener(this);
		}
		nameProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				if (smrtVal != null)
					smrtVal.removeListener(ControlBase.this);
				smrtVal = provider.getObservable(t1);
				smrtVal.addListener(ControlBase.this);
			}
		});
	}

	@Override
	public void changed(ObservableValue<? extends Object> ov, Object t, Object t1)
	{
		changed(t1);
	}

	protected abstract void changed(Object newValue);

	public SmartValue getSmartValue()
	{
		return smrtVal;
	}

	private SmartValue smrtVal;
}
