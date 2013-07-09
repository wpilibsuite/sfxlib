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
package dashfx.lib.controls.fxmlutils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 *
 * @author patrick
 */
public class FormattableLabel extends Label
{
	SimpleStringProperty formatString = new SimpleStringProperty(this, "format", "%d");

	public SimpleStringProperty formatProperty()
	{
		return formatString;
	}

	public void setFormat(String format)
	{
		formatProperty().setValue(format);
	}

	public String getFormat()
	{
		return formatProperty().getValue();
	}
	SimpleObjectProperty<Object> valueProp = new SimpleObjectProperty<>(this, "value");

	public SimpleObjectProperty valueProperty()
	{
		return valueProp;
	}

	public void setValue(Object val)
	{
		valueProperty().setValue(val);
	}

	public Object getValue()
	{
		return valueProperty().getValue();
	}

	public FormattableLabel()
	{
		valueProp.addListener(new ChangeListener<Object>()
		{
			@Override
			public void changed(ObservableValue<? extends Object> ov, Object t, Object t1)
			{
				reformat();
			}
		});

		formatString.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				reformat();
			}
		});
	}

	public synchronized void reformat()
	{
		try
		{
			this.setText(String.format(getFormat(), getValue()));
		}
		catch (Throwable t)
		{
			//shut up *chirp*
		}
	}
}
