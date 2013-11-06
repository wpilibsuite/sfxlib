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
package dashfx.lib.decorators;

import dashfx.lib.controls.Designable;
import dashfx.lib.controls.Range;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 *
 * @author patrick
 */
@Designable(value = "Rotator", description = "Allows rotating controls")
public class RotateDecorator implements Decorator
{
	private final StackPane rotated = new StackPane();


	@Override
	public void decorate(Node parent)
	{
		rotated.getChildren().add(parent);
	}

	@Override
	public void undecorate()
	{
		// nothing to do...
	}

	@Override
	public Node getUi()
	{
		return rotated;
	}

	@Designable(value = "Angle", description = "Amount to rotate the control")
	@Range(maxValue = 180, minValue = -180)
	public DoubleProperty rotateProperty()
	{
		return rotated.rotateProperty();
	}

	public double getRotate()
	{
		return rotateProperty().get();
	}

	public void setRotate(double vlaue)
	{
		rotateProperty().set(vlaue);
	}
}
