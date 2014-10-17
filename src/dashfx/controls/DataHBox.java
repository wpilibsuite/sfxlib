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
import dashfx.lib.data.*;
import dashfx.lib.controls.Control;
import dashfx.lib.controls.*;
import dashfx.lib.data.video.VideoCore;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
@Designable(value = "HStack Panel", image = "/dashfx/controls/res/hbox.png", description = "Fitting horizontal stacking panel")
@Category("Grouping")
@DesignableChildProperty(property =
{
	"Hgrow"
}, name =
{
	"HGrow"
}, description =
{
	"Horizontal Growth"
})
@DashFXProperties("Sealed: false, Save Children: true")
public class DataHBox extends HBox implements DataCoreProvider, Control, DesignablePane, EventHandler<Event>
{
	private DataCoreProvider superprovider = null;
	private ArrayList<Registerable> unregistered = new ArrayList<>();
	private ArrayList<Registerable> registered = new ArrayList<>();
	private boolean designing;
	private boolean nested = false;
	private SimpleObjectProperty<DataPaneMode> dataMode = new SimpleObjectProperty<>(this, "dataMode", DataPaneMode.Passthrough);
	private SimpleStringProperty name = new SimpleStringProperty(this, "name"),
			baseName = new SimpleStringProperty(this, "baseName");
	private SmartValue defaults = null;//;new SmartValue(false, SmartValueTypes.Hash, "nokey"); //TODO: total hack

	public DataHBox()
	{
		setStyle("-fx-border-color: black;");
		getChildren().addListener(new ListChangeListener<Node>()
		{
			@Override
			public void onChanged(ListChangeListener.Change<? extends Node> change)
			{
				try
				{
					for (Node n : change.getAddedSubList())
					{
						if (n instanceof Control)
							addControl((Control) n);
					}
				}
				catch (IllegalStateException ex)
				{

					// this always happens
					for (Node ctrls : getChildren())
					{
						if (ctrls instanceof Control)
						{
							if (unregistered.contains(ctrls) || registered.contains(ctrls))
							{
								//TODO: no way to remove items
							}
							else
							{
								addControl((Control) ctrls);
							}
						}
					}
				}
			}
		});
		dataMode.addListener(new ChangeListener<DataPaneMode>()
		{
			@Override
			public void changed(ObservableValue<? extends DataPaneMode> ov, DataPaneMode t, DataPaneMode t1)
			{
				for (Registerable registerable : registered)
				{
					registerable.registered(DataHBox.this);
				}
				if (superprovider != null && getOnRegisterRequest() != null)
					getOnRegisterRequest().handle(null);
			}
		});
		name.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				String bname = name.getValue();
				if (bname.endsWith("/"))
					bname = bname.substring(0, bname.length() - 2);
				bname = bname.substring(bname.lastIndexOf('/') + 1);
				baseName.setValue(bname);
				if (dataMode.get() == DataPaneMode.Passthrough)
					return;

				for (Registerable registerable : registered)
				{
					registerable.registered(DataHBox.this);
				}
				if (superprovider != null && getOnRegisterRequest() != null)
					getOnRegisterRequest().handle(null);
			}
		});
	}
	private ObjectProperty<EventHandler<? super Event>> onRegisterRequest = new SimpleObjectProperty<>(this, "onRegisterRequest");

	public ObjectProperty<EventHandler<? super Event>> onRegisterRequestProperty()
	{
		return onRegisterRequest;
	}

	public void setOnRegisterRequest(EventHandler<? super Event> event)
	{
		onRegisterRequestProperty().set(event);
	}

	public EventHandler<? super Event> getOnRegisterRequest()
	{
		return onRegisterRequestProperty().get();
	}

	@Designable(value = "Data Mode", description = "Determines how much this node proxies name requests when resolving")
	public ObjectProperty<DataPaneMode> dataModeProperty()
	{
		return dataMode;
	}

	public DataPaneMode getDataMode()
	{
		return dataMode.get();
	}

	public void setDataMode(DataPaneMode dataMode)
	{
		this.dataMode.set(dataMode);
	}

	@Designable(value = "Name", description = "Proxy Resolving Name prefix")
	public StringProperty nameProperty()
	{
		return name;
	}

	public String getName()
	{
		return name.get();
	}

	public void setName(String dataMode)
	{
		this.name.set(dataMode);
	}

	public StringProperty baseNameProperty()
	{
		return baseName;
	}

	public String getBaseName()
	{
		return baseNameProperty().getValue();
	}

	@Override
	public void handle(Event t)
	{
		// schluuurp!
		t.consume();
	}

	@Override
	public void addControl(Control r)
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
	public void clearAllDataEndpoints()
	{
		superprovider.clearAllDataEndpoints();
	}

	@Override
	public void mountDataEndpoint(DataInitDescriptor<DataEndpoint> r)
	{
		superprovider.mountDataEndpoint(r);
	}

	@Override
	public DataInitDescriptor<DataEndpoint>[] getAllDataEndpoints()
	{
		return superprovider.getAllDataEndpoints();
	}

	@Override
	public DataProcessor[] getAllDataFilters()
	{
		return superprovider.getAllDataFilters();
	}

	@Override
	@SuppressWarnings("AssignmentToMethodParameter")
	public SmartValue getObservable(String name)
	{
		if (getName() != null && (getDataMode() == DataPaneMode.ForceNested || (getDataMode() == DataPaneMode.Nested && !name.startsWith("/"))))
			name = getName() + (getName().endsWith("/") || name.startsWith("/") ? "" : "/") + name;
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
		if (getOnRegisterRequest() != null)
			getOnRegisterRequest().handle(null);
	}

	@Override
	public boolean isJumps()
	{
		return false;
	}
	private Node[] overlays;
	private Region[] childs;
	private int[] indexes;
	private double[] lastSizeX;
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
		lastSizeX = new double[childs.length];
		for (int i = 0; i < childs.length; i++)
		{
			indexes[i] = getChildren().indexOf(overlays[i]);
			lastSizeX[i] = childs[i].getWidth();
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
				childContainer.setPrefWidth(lastSizeX[i] + dx * sizeX);
			else
			{
				//if the dx > width of next/previous, move it
				if ((dx - dxDiff) > 0)
				{
					//move to the right. only bother if we can
					while (indexes[0] < getChildren().size() - 1 && (dx - dxDiff) > ((Region) getChildren().get(indexes[0] + 1)).getWidth())
					{
						dxDiff += ((Region) getChildren().get(indexes[0] + 1)).getWidth();
						Node oc = getChildren().get(indexes[0]);
						getChildren().remove(oc);
						getChildren().add(indexes[0] + 1, oc);
						indexes[0]++;
					}
				}
				else if ((dx - dxDiff) < 0)
				{
					//move to the left. only bother if we can
					while (indexes[0] > 0 && (dx - dxDiff) < -((Region) getChildren().get(indexes[0] - 1)).getWidth())
					{
						dxDiff -= ((Region) getChildren().get(indexes[0] - 1)).getWidth();
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
		return EnumSet.of(ResizeDirections.Move, ResizeDirections.LeftRight);
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
				width += ((Region) getChildren().get(idx++)).getWidth();
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
			this.addEventFilter(EventType.ROOT, this);
			nested = true;
		}
	}

	@Override
	public void exitNested()
	{
		if (nested)
		{
			this.removeEventFilter(EventType.ROOT, this);
			nested = false;
		}
	}

	@Override
	public void zEdit(Node child, ZPositions diff)
	{
		// Does not compute
	}

	@Override
	public void dispose()
	{
		superprovider.dispose();
	}

	@Override
	public Node getUi()
	{
		return this;
	}

	@Override
	public boolean isAppendable()
	{
		return true;
	}

	@Override
	public VideoCore getVideoCore()
	{
		return superprovider.getVideoCore();
	}
}
