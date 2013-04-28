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
package dashfx.data;

import dashfx.controls.*;
import java.util.*;
import javafx.scene.*;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
public interface DesignablePane
{
	EnumSet<ResizeDirections> getSupportedOps();
	/**
	 * Does this pane have discrete locations controls can go or is it a continuous surface?
	 * @return will the control jump around at design time when moving between parts?
	 */
	boolean isJumps();
	void BeginDragging(Node[] overlays, Region[] childs, double x, double y, double sizeX, double sizeY, double posX, double posY);
	void ContinueDragging(double dx, double dy);
	void FinishDragging();
	void setDesigning(boolean designing);
	boolean getDesigning();
	void addChildAt(Node child, double x, double y);
	void editNested(Node overlay, Runnable onExitRequest);
	void exitNested();
}
