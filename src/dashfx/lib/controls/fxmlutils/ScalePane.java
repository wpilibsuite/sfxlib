/*
 * Copyright (C) 2013 Patrick Plenefisch
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

import javafx.collections.ListChangeListener;
import javafx.scene.*;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
public class ScalePane extends StackPane
{

	public ScalePane()
	{
		getChildren().addListener(new ListChangeListener<Node>() {

			@Override
			public void onChanged(ListChangeListener.Change<? extends Node> change)
			{
				reScale();
			}
		});
	}


	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		reScale();

	}

	@Override
	protected void impl_geomChanged()
	{
		super.impl_geomChanged();
		reScale();
	}

	private void reScale()
	{
		double width = getWidth();
		double height = getHeight();
		double top = getInsets().getTop();
		double right = getInsets().getRight();
		double left = getInsets().getLeft();
		double bottom = getInsets().getBottom();
		double contentWidth = width - left - right;
		double contentHeight = height - top - bottom;

		if (getChildren().size() > 0)
		{
			try
			{
				Node child = getChildren().get(0);
				double diffH = contentHeight / child.getBoundsInLocal().getHeight();
				double diffW = contentWidth / child.getBoundsInLocal().getWidth();
				child.setScaleX(diffW);
				child.setScaleY(diffH);
			}
			catch (ArithmeticException e)
			{
				// no way to scale
				// so fail
			}
		}
	}


}
