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

import dashfx.data.DataCoreProvider;
import dashfx.data.DataEndpoint;
import dashfx.data.DataProcessor;
import dashfx.data.DesignablePane;
import dashfx.data.Registerable;
import dashfx.data.SmartValue;
import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author patrick
 */
public class DataAnchorPane extends AnchorPane implements DataCoreProvider, Registerable, DesignablePane
{
	private DataCoreProvider superprovider = null;
	private ArrayList<Registerable> unregistered = new ArrayList<>();

	@Override
	public void addControl(Registerable r)
	{
		if (superprovider != null)
			r.registered(this);
		else
			unregistered.add(r);
	}

	@Override
	public void addDataEndpoint(DataEndpoint r)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void addDataFilter(DataProcessor r)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ObservableValue<SmartValue> getObservable(String name)
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
		}
	}
}
