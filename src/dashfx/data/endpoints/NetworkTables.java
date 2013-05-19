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
import java.io.IOException;

/**
 *
 * @author patrick
 */
@DesignableData(name = "NetworkTables", types =
{
	DataProcessorType.DataSender
}, description = "Full NetworkTables 2.0 Client for bidirection Robot communication")
public class NetworkTables implements DataSource
{
	//NetworkTableClient nwt;
	private DataProcessor proc;

	public NetworkTables()
	{
		
	}
	

	@Override
	public boolean isConnected()
	{
		//return nwt != null && nwt.isConnected();
		return true;
	}

	@Override
	public void setProcessor(DataProcessor proc)
	{
		this.proc = proc;
	}

	@Override
	public boolean init(InitInfo info)
	{
//		try
//		{
//			//nwt = new NetworkTableClient(new SocketStreamFactory(info.getHost(), info.getPort()));
//			nwt = null;
//		}
//		catch (IOException ex)
//		{
//			ex.printStackTrace();
//			return false;
//		}
//		return nwt.isConnected();
		return true;
	}
	
}
