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
	private boolean nested = false;

	public PaneControlBase(T pane)
	{
		ui = pane;
		ui.getChildren().addListener(new ListChangeListener<Node>()
		{
			@Override
			@SuppressWarnings("element-type-mismatch")
			public void onChanged(ListChangeListener.Change<? extends Node> change)
			{
				change.next();
				for (Node n : change.getAddedSubList())
				{
					if (n instanceof Registerable && !unregistered.contains(n) && !registered.contains(n))
						addControl((Registerable) n);
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
	T ui;

	@Override
	public Node getUi()
	{
		return ui;
	}
}
