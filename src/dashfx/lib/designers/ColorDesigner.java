/*
 * Copyright (C) 2013 Sam
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
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

/**
 *
 * @author Sam
 */
@Designer(Color.class)
public class ColorDesigner implements PropertyDesigner<Color>
{
	private ColorPicker colorPicker = new ColorPicker();

	public ColorDesigner()
	{
	}

	@Override
	public void design(Property<Color> prop)
	{
		colorPicker.valueProperty().bindBidirectional(prop);
	}

	@Override
	public Node getUiBits()
	{
		return colorPicker;
	}
}
