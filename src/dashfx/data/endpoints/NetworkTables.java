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
package dashfx.data.endpoints;

import dashfx.controls.*;
import dashfx.data.*;
import edu.wpi.first.wpilibj.networktables2.client.NetworkTableClient;
import edu.wpi.first.wpilibj.networktables2.stream.SocketStreamFactory;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import java.io.IOException;

/**
 *
 * @author patrick
 */
@DesignableData(name = "NetworkTables", types =
{
	DataProcessorType.DataSender
}, description = "Full NetworkTables 2.0 Client for bidirection Robot communication")
public class NetworkTables implements DataSource, Runnable, ITableListener
{
	NetworkTableClient nwt;
	private DataProcessor proc;
	private Thread worker;

	public NetworkTables()
	{
	}

	@Override
	public boolean isConnected()
	{
		return nwt != null && nwt.isConnected();
	}

	@Override
	public void setProcessor(DataProcessor proc)
	{
		this.proc = proc;
		worker = new Thread(this);
		//worker.start();
		this.run();
	}

	@Override
	public boolean init(InitInfo info)
	{
		try
		{
			//System.out.println(info);
			nwt = new NetworkTableClient(new SocketStreamFactory("127.0.0.1", 1735));
			//TODO: info.getHost(), info.getPort()));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
		return nwt.isConnected();
	}

	@Override
	public void run()
	{
		nwt.addTableListener(this, true);//TODO: check what this boolean means
	}

	@Override
	public void valueChanged(ITable itable, String string, Object o, boolean bln)
	{
		if (string.startsWith("/"))
			string = string.substring(1);
		if (string.endsWith("~TYPE~"))
		{
			proc.processData(null, new SimpleTransaction(new SmartValue(null, null, string.substring(0, string.lastIndexOf("/~TYPE~")), o.toString())));
		}
		else
			proc.processData(null, new SimpleTransaction(new SmartValue(o, getType(o), string)));
	}

	public static SmartValueTypes getType(Object value)
	{
		//TODO: this is awful
		if (value == null)
		{
			return SmartValueTypes.Unknown;
		}
		else if (value instanceof ITable)
		{
			return SmartValueTypes.Hash; //TODO: grouped hash?
		}
		else if (value instanceof Double)
		{
			return SmartValueTypes.Double;
		}
		else if (value instanceof Boolean)
		{
			return SmartValueTypes.Boolean;
		}
		else if (value instanceof String)
		{
			return SmartValueTypes.String;
		}
		else
		{
			return SmartValueTypes.Unknown;
		}
	}
}
