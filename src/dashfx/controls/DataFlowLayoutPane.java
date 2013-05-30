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

import dashfx.lib.controls.Designable;
import dashfx.controls.bases.*;
import dashfx.lib.controls.*;
import dashfx.lib.data.*;
import java.util.EnumSet;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

/**
 *
 * @author patrick
 */
@Designable(value = "Flow", image="/dashfx/controls/res/flow.png",  description = "Automatic flowing pane")
@DesignableProperty(value = {"hgap", "vgap"}, descriptions = {"Horizontal Spacing", "Vertical spacing"})
public class DataFlowLayoutPane extends PaneControlBase<FlowPane>
{
	public DataFlowLayoutPane()
	{
		super(new FlowPane());
	}

	@Override
	public EnumSet<ResizeDirections> getSupportedOps()
	{
		return EnumSet.of(ResizeDirections.LeftRight, ResizeDirections.UpDown, ResizeDirections.NorthEastSouthWest, ResizeDirections.SouthEastNorthWest);
	}

	@Override
	public boolean isJumps()
	{
		return true;
	}

	@Override
	public void BeginDragging(Node[] overlays, Region[] childs, double x, double y, double sizeX, double sizeY, double posX, double posY)
	{
		// TODO: FIXME
	}

	@Override
	public void ContinueDragging(double dx, double dy)
	{
		// TODO: FIXME
	}

	@Override
	public void FinishDragging()
	{
		// TODO: FIXME
	}

	@Override
	public void addChildAt(Node child, double x, double y)
	{
		ui.getChildren().add(child);
		//TODO: Fix
	}

	@Override
	public void editNested(Node overlay, Runnable onExitRequest)
	{
		// TODO: FIXME
	}

	@Override
	public void exitNested()
	{
		// TODO: FIXME
	}

	@Override
	public void zEdit(Node child, ZPositions diff)
	{
		// dnc
	}

	@Override
	public boolean isAppendable()
	{
		return true;
	}
}
