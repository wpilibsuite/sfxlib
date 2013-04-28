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

import dashfx.data.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
@Designable(value = "Canvas", description = "Cartesian coordinate based panel")
public class DataAnchorPane extends AnchorPane implements DataCoreProvider, Registerable, DesignablePane
{
	private DataCoreProvider superprovider = null;
	private ArrayList<Registerable> unregistered = new ArrayList<>();
	private ArrayList<Registerable> registered = new ArrayList<>();
	private boolean designing;
	private int originalIndex = -1;
	private Pane slurpee = new Pane();
	private Runnable exitRequest = null;

	public DataAnchorPane()
	{
		getChildren().addListener(new ListChangeListener<Node>()
		{
			@Override
			public void onChanged(ListChangeListener.Change<? extends Node> change)
			{
				try
				{
					for (Node n : change.getAddedSubList())
					{
						if (n instanceof Registerable)
							addControl((Registerable) n);
					}
				}
				catch (IllegalStateException ex)
				{
					// this always happens
					for (Node ctrls : getChildren())
					{
						if (ctrls instanceof Registerable)
						{
							if (unregistered.contains(ctrls) || registered.contains(ctrls))
							{
								//TODO: no way to remove items
							}
							else
							{
								addControl((Registerable)ctrls);
							}
						}
					}
				}
			}
		});

		slurpee.setLayoutX(0);
		slurpee.setLayoutY(0);
		slurpee.prefWidthProperty().bind(widthProperty());
		slurpee.prefHeightProperty().bind(heightProperty());
		slurpee.addEventFilter(EventType.ROOT, new EventHandler<Event>() {

			@Override
			public void handle(Event t)
			{
				// schluuurp!
				t.consume();
				if (t.getEventType() == MouseEvent.MOUSE_CLICKED)
				{
					if (((MouseEvent)t).getClickCount() > 1)
					{
						//exit
						exitRequest.run();
					}
				}
			}
		});
	}

	@Override
	public void addControl(Registerable r)
	{
		if (superprovider != null)
		{
			r.registered(this);
			registered.add(r);
		}
		else
			unregistered.add(r);
	}

	@Override
	public void addDataEndpoint(DataEndpoint r)
	{
		superprovider.addDataEndpoint(r);
	}

	@Override
	public void addDataFilter(DataProcessor r)
	{
		superprovider.addDataFilter(r);
	}

	@Override
	public SmartValue getObservable(String name)
	{
		return superprovider.getObservable(name);
	}

	@Override
	public void registered(DataCoreProvider provider)
	{
		superprovider = provider;
		for (Registerable r : unregistered)
		{
			r.registered(this);
			registered.add(r);
		}
	}

	@Override
	public void pause()
	{
		superprovider.pause();
	}

	@Override
	public void resume()
	{
		superprovider.resume();
	}

	@Override
	public boolean isJumps()
	{
		return false;
	}
	private Node[] overlays;
	private Region[] childs;
	private double[] layoutX, layoutY, lastSizeX, lastSizeY;
	private double sizeX, sizeY, posX, posY;

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
		layoutX = new double[childs.length];
		layoutY = new double[childs.length];
		lastSizeX = new double[childs.length];
		lastSizeY = new double[childs.length];
		for (int i = 0; i < childs.length; i++)
		{
			layoutX[i] = overlays[i].getLayoutX();
			layoutY[i] = overlays[i].getLayoutY();
			lastSizeX[i] = childs[i].getWidth();
			lastSizeY[i] = childs[i].getHeight();
		}
	}

	@Override
	public void ContinueDragging(double dx, double dy)
	{
		// TODO: asert that its valid to continue
		for (int i = 0; i < overlays.length; i++)
		{
			Node overlay = overlays[i];
			Region childContainer = childs[i];
			overlay.setLayoutX(layoutX[i] + dx * posX);
			overlay.setLayoutY(layoutY[i] + dy * posY);
			childContainer.setPrefSize(lastSizeX[i] + dx * sizeX, lastSizeY[i] + dy * sizeY);
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
		return EnumSet.copyOf(Arrays.asList(ResizeDirections.values()));
	}

	@Override
	public void addChildAt(Node child, double x, double y)
	{
		child.setLayoutX(x);
		child.setLayoutY(y);
		getChildren().add(child);
	}

	@Override
	public void editNested(Node overlay, Runnable onExitRequested)
	{
		if (originalIndex == -1)
		{
			exitRequest = onExitRequested;
			getChildren().add(slurpee);
			originalIndex = getChildren().indexOf(overlay);
			getChildren().remove(overlay);
			getChildren().add(overlay);
		}
	}

	@Override
	public void exitNested()
	{
		if (originalIndex != -1)
		{
			getChildren().remove(slurpee);
			Node itm = getChildren().remove(getChildren().size() - 1);
			getChildren().add(originalIndex, itm);
			originalIndex = -1;
			exitRequest = null;
		}
	}
}
