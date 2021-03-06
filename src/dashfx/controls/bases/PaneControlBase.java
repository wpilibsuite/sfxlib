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
package dashfx.controls.bases;

import dashfx.lib.controls.*;
import dashfx.lib.data.*;
import dashfx.lib.controls.Control;
import dashfx.lib.data.video.VideoCore;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
@Category("Grouping")
public abstract class PaneControlBase<T extends Pane> implements DataCoreProvider, Control, DesignablePane
{
	private DataCoreProvider superprovider = null;
	private ArrayList<Registerable> unregistered = new ArrayList<>();
	private ArrayList<Registerable> registered = new ArrayList<>();
	private boolean designing;
	private SimpleObjectProperty<DataPaneMode> dataMode = new SimpleObjectProperty<>(this, "dataMode", DataPaneMode.Passthrough);
	private SimpleStringProperty name = new SimpleStringProperty(this, "name");

	public PaneControlBase(T pane)
	{
		ui = pane;
		ui.setStyle("-fx-border-color: black;");
		getChildren().addListener(new ListChangeListener<Node>()
		{
			@Override
			@SuppressWarnings("element-type-mismatch")
			public void onChanged(ListChangeListener.Change<? extends Node> change)
			{
				change.next();
				for (Node n : change.getAddedSubList())
				{
					if (n instanceof Control && !unregistered.contains(n) && !registered.contains(n))
						addControl((Control) n);
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
					registerable.registered(PaneControlBase.this);
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
				if (dataMode.get() == DataPaneMode.Passthrough)
					return;

				for (Registerable registerable : registered)
				{
					registerable.registered(PaneControlBase.this);
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


	@Designable(value = "CSS", description = "FX CSS Style rules")
	@Category("Style")
	public StringProperty styleProperty()
	{
		return getUi().styleProperty();
	}

	public String getStyle()
	{
		return styleProperty().getValue();
	}

	public void setStyle(String value)
	{
		styleProperty().setValue(value);
	}

	@Designable(value = "Name", description = "Proxy Resolving Name prefix")
	@Category("Basic")
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

	@Override
	public ObservableList<Node> getChildren()
	{
		return ui.getChildren();
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
	public VideoCore getVideoCore()
	{
		return superprovider.getVideoCore(); // TODO: prefix truncate?
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
		if (superprovider != null && getOnRegisterRequest() != null)
			getOnRegisterRequest().handle(null);
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
	public void dispose()
	{
		superprovider.dispose();
	}
	protected T ui;

	@Override
	public Node getUi()
	{
		return ui;
	}
}
