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

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author patrick
 */
@Designable(value = "Value Meter", image = "/dashfx/controls/BadSlider.png", description = "Excellent slider like control")
public class ValueMeter extends NumberControlBase
{
	public ValueMeter()
	{
		load();
		setMax(1.0);
		setMin(-1.0);
		setValue(0.0);
	}
	private void load()
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ValueMeter.fxml"));
		fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try
        {
            fxmlLoader.load();
        }
        catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }
	}
}
