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

import dashfx.lib.data.DataCoreProvider;
import dashfx.lib.data.SmartValue;
import dashfx.lib.controls.Control;
import dashfx.lib.controls.Designable;
import dashfx.lib.data.values.SmartValueAdapter;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.fxml.FXML;
import javafx.scene.Node;

/**
 *
 * @author patrick
 */
public abstract class ControlBase implements Control, ChangeListener<Object>
{
	@FXML
	private SimpleObjectProperty<Node> ui = new SimpleObjectProperty<>();
	protected boolean ignore;
	private Object customPropObj = null;

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
	private StringProperty name = new SimpleStringProperty(this, "name", "");

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
	private StringProperty lbl = new SimpleStringProperty(this, "label", "");

	@Designable(value = "Label", description = "What to label the control (not all controls support)")
	public StringProperty labelProperty()
	{
		return lbl;
	}

	public String getLabel()
	{
		return labelProperty().getValue();
	}

	public void setLabel(String value)
	{
		labelProperty().setValue(value);
	}
	private BooleanProperty showLabel = new SimpleBooleanProperty(this, "showLabel", false);

	@Designable(value = "Show Label", description = "Show a label (not all controls support)")
	public BooleanProperty showLabelProperty()
	{
		return showLabel;
	}

	public boolean getShowLabel()
	{
		return showLabelProperty().get();
	}

	public void setShowLabel(boolean value)
	{
		showLabelProperty().set(value);
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
			if (!smrtVal.isEmpty() && !smrtVal.getData().isHash())
				changed(smrtVal, null, smrtVal.getData().asRaw());
		}
		// TODO: don't leak on multiple calls
		nameProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				if (smrtVal != null)
					smrtVal.removeListener(ControlBase.this);
				smrtVal = provider.getObservable(t1);
				smrtVal.addListener(ControlBase.this);
				if (!smrtVal.isEmpty() && !smrtVal.getData().isHash())
					ControlBase.this.changed(smrtVal, null, smrtVal.getData().asRaw());
			}
		});
	}

	@Override
	public void changed(ObservableValue<? extends Object> ov, Object t, Object t1)
	{
		if (ignore)
			return;
		ignore = true;
		stringValue.set(t1.toString());
		changed(t1, smrtVal.getData());
		ignore = false;
	}

	protected abstract void changed(Object newValue, SmartValueAdapter adapter);

	public SmartValue getSmartValue()
	{
		return smrtVal;
	}
	private SmartValue smrtVal;
	protected ReadOnlyStringWrapper stringValue = new ReadOnlyStringWrapper(this, "stringValue");

	@Designable(value = "String Value", description = "Value.toString()")
	public ReadOnlyStringProperty stringValueProperty()
	{
		return stringValue.getReadOnlyProperty();
	}

	public String getStringValue()
	{
		return stringValueProperty().getValue();
	}

	public void setCustomPropObject(Object obj)
	{
		customPropObj = obj;
	}

	public Object getCustom()
	{
		return customPropObj;
	}
}

