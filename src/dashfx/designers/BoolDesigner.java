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
package dashfx.designers;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author patrick
 */
@Designer(Boolean.class)
public class BoolDesigner implements PropertyDesigner<Boolean>
{
	CheckBox ticker = new CheckBox();

	public BoolDesigner()
	{
	}

	@Override
	public void design(Property<Boolean> prop)
	{
		ticker.selectedProperty().bindBidirectional(prop);
	}

	@Override
	public Node getUiBits()
	{
		return ticker;
	}
}
