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
package dashfx.lib.designers;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;

/**
 *
 * @author patrick
 */
@Designer(Enum.class)
public class EnumDesigner implements PropertyDesigner<Enum>
{
	ComboBox ui = new ComboBox();
	Property<Enum> lprop;

	public void setEnum(Class x)
	{
		ui.setItems(FXCollections.observableArrayList(x.getEnumConstants()));
		ui.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue ov, Object t, Object t1)
			{
				if (lprop != null && (lprop.getValue() == null || !lprop.getValue().equals(t1)))
				{
					lprop.setValue((Enum)t1);
				}
			}
		});
	}

	@Override
	public void design(Property<Enum> prop)
	{
		lprop = prop;
		ui.getSelectionModel().select(prop.getValue());
		prop.addListener(new ChangeListener<Enum>()
		{
			@Override
			public void changed(ObservableValue<? extends Enum> ov, Enum t, Enum t1)
			{
				ui.getSelectionModel().select(t1);
			}
		});
	}

	@Override
	public Node getUiBits()
	{
		return ui;
	}
}
