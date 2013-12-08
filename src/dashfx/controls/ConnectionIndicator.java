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

import dashfx.lib.controls.Category;
import dashfx.lib.controls.Control;
import dashfx.lib.controls.Designable;
import dashfx.lib.data.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;

/**
 *
 * @author patrick
 */
@Designable(value = "Connection", description = "Connection Indicator", image = "/dashfx/controls/res/connection.png")
@Category("General")
public class ConnectionIndicator implements Control
{
	//#60dc00 = green
	//#df0040 = reds
	Label pane = new Label();
	private DataCoreProvider dc;

	public ConnectionIndicator()
	{
		pane.setStyle("-fx-background-color: #df0040;");
		Thread thr = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException ex)
					{
					}
					StringBuilder sb = new StringBuilder();
					boolean allok = true;
					if (dc != null)
					{
						for (DataInitDescriptor<DataEndpoint> dataInitDescriptor : dc.getAllDataEndpoints())
						{
							sb.append(dataInitDescriptor.getName());
							sb.append(": ");
							sb.append(dataInitDescriptor.getObject().isConnected() ? "Ok" : "Error");
							sb.append("\n");
							allok = allok && dataInitDescriptor.getObject().isConnected();
						}
					}
					updateIt(sb.toString(), allok);
				}
			}
		});
		thr.setDaemon(true);
		thr.start();
	}

	private void updateIt(final String sbs, final boolean allokf)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{

				pane.setText(sbs);
				pane.setStyle("-fx-background-color: #" + (allokf ? "60dc00" : "df0040"));
			}
		});
	}

	@Override
	public Node getUi()
	{
		return pane;
	}

	@Override
	public void registered(DataCoreProvider provider)
	{
		dc = provider;
	}
}
