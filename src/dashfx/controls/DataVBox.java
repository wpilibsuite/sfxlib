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

import dashfx.controls.bases.PaneControlBase;
import dashfx.lib.controls.Designable;
import dashfx.lib.data.*;
import dashfx.lib.controls.*;
import java.util.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
@Designable(value = "VStack Panel", image = "/dashfx/controls/res/hbox.png", description = "Fitting vertical stacking panel")
@Category("Grouping")
@DesignableChildProperty(property = {"Vgrow"}, name = {"VGrow"}, description = {"Vorizontal Growth"})
public class DataVBox extends PaneControlBase<VBox> implements EventHandler<Event>
{
	private boolean designing;
	private boolean nested = false;

	public DataVBox()
	{
		super(new VBox());
	}

	@Override
	public void handle(Event t)
	{
		// schluuurp!
		t.consume();
	}

	@Override
	public boolean isJumps()
	{
		return true;
	}
	private Node[] overlays;
	private Region[] childs;
	private int[] indexes;
	private double[] lastSizeY;
	private double sizeX, sizeY, posX, posY, dxDiff;

	@Override
	public void BeginDragging(Node[] overlays, Region[] childs, double x, double y, double sizeX, double sizeY, double posX, double posY)
	{
		this.overlays = overlays;
		this.childs = childs;
		assert overlays.length == childs.length;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.posX = posX;
		this.posY = posY;
		indexes = new int[childs.length];
		lastSizeY = new double[childs.length];
		for (int i = 0; i < childs.length; i++)
		{
			indexes[i] = getChildren().indexOf(overlays[i]);
			lastSizeY[i] = childs[i].getHeight();
		}
	}

	@Override
	public void ContinueDragging(double dx, double dy)
	{
		//TODO: support multi-dragging
		for (int i = 0; i < overlays.length; i++)
		{
			Node overlay = overlays[i];
			Region childContainer = childs[i];
			if (sizeX != 0)
				childContainer.setPrefHeight(lastSizeY[i] + dx * sizeX);
			else
			{
				//if the dx > width of next/previous, move it
				if ((dx - dxDiff) > 0)
				{
					//move to the right. only bother if we can
					while (indexes[0] < getChildren().size() - 1 && (dx - dxDiff) > ((Region) getChildren().get(indexes[0] + 1)).getHeight())
					{
						dxDiff += ((Region) getChildren().get(indexes[0] + 1)).getHeight();
						Node oc = getChildren().get(indexes[0]);
						getChildren().remove(oc);
						getChildren().add(indexes[0] + 1, oc);
						indexes[0]++;
					}
				}
				else if ((dx - dxDiff) < 0)
				{
					//move to the left. only bother if we can
					while (indexes[0] > 0 && (dx - dxDiff) < -((Region) getChildren().get(indexes[0] - 1)).getHeight())
					{
						dxDiff -= ((Region) getChildren().get(indexes[0] - 1)).getHeight();
						Node oc = getChildren().get(indexes[0]);
						getChildren().remove(oc);
						getChildren().add(indexes[0] - 1, oc);
						indexes[0]--;
					}
				}
			}
		}
	}

	@Override
	public void FinishDragging()
	{
		overlays = childs = null;
		sizeX = sizeY = posX = posY = 0.0;
	}

	@Override
	public void setDesigning(boolean designing)
	{
		this.designing = designing;
	}

	@Override
	public boolean getDesigning()
	{
		return designing;
	}

	@Override
	public EnumSet<ResizeDirections> getSupportedOps()
	{
		return EnumSet.of(ResizeDirections.Move, ResizeDirections.UpDown);
	}

	@Override
	public void addChildAt(Node child, double x, double y)
	{
		if (getChildren().size() == 0 || x <= 0)
			getChildren().add(child);
		else
		{
			int idx = 0;
			double width = 0;
			while (x > width && idx < getChildren().size())
			{
				// TODO: halfwidth
				width += ((Region) getChildren().get(idx++)).getHeight();
			}
			if (getChildren().size() <= idx)
				getChildren().add(child);
			else
				getChildren().add(idx, child);
		}
	}

	@Override
	public void editNested(Node overlay, Runnable onExitRequest)
	{
		if (!nested)
		{
			//TODO: check the overlay BUG! FIXME!
			ui.addEventFilter(EventType.ROOT, this);
			nested = true;
		}
	}

	@Override
	public void exitNested()
	{
		if (nested)
		{
			ui.removeEventFilter(EventType.ROOT, this);
			nested = false;
		}
	}

	@Override
	public void zEdit(Node child, ZPositions diff)
	{
		// Does not compute
	}

	@Override
	public boolean isAppendable()
	{
		return true;
	}
}
