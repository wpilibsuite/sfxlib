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
package dashfx.lib.data.endpoints;

import dashfx.lib.data.*;
import dashfx.lib.controls.DesignableData;
import edu.wpi.first.wpilibj.networktables2.client.NetworkTableClient;
import edu.wpi.first.wpilibj.networktables2.stream.SocketStreamFactory;
import edu.wpi.first.wpilibj.networktables2.type.*;
import edu.wpi.first.wpilibj.tables.*;
import java.io.IOException;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author patrick
 */
@DesignableData(name = "NetworkTables", types =
{
	DataProcessorType.DataSender
}, description = "Full NetworkTables 2.0 Client for bidirection Robot communication")
public class NetworkTables implements DataSource, ITableListener, DataSender
{
	NetworkTableClient nwt;
	private DataProcessor proc;

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
		if (proc != null)
		{
			this.proc = proc;
			this.run();
		}
		else
		{
			nwt.stop();
			nwt = null;
			this.proc = null;
		}
	}

	@Override
	@SuppressWarnings("AssignmentToMethodParameter")
	public boolean init(InitInfo info)
	{
		try
		{
			if (info == null)
				info = new InitInfo();
			Integer port = info.getPort();
			if (port == null)
				port = 1735;
			nwt = new NetworkTableClient(new SocketStreamFactory(info.getHost(), port));
			if (proc != null)
			{
				this.run();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
		return nwt.isConnected();
	}

	public void run()
	{
		nwt.addTableListener(this, true);//TODO: check what this boolean means
	}

	@Override
	@SuppressWarnings("AssignmentToMethodParameter")
	public void valueChanged(ITable itable, String string, Object o, boolean bln)
	{
		if (string.startsWith("/"))
			string = string.substring(1);
		if (string.endsWith("~TYPE~"))
		{
			proc.processData(null, new SimpleTransaction(new SmartValue(null, null, string.substring(0, string.lastIndexOf("/~TYPE~")), o.toString())));
		}
		else
		{
			SmartValueTypes type = getType(o);
			proc.processData(null, new SimpleTransaction(new SmartValue(type.isArray() ? rawClone(o, type) : o, type, string)));
		}
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
		else if (value instanceof NumberArray)
		{
			return SmartValueTypes.DoubleArray;
		}
		else if (value instanceof BooleanArray)
		{
			return SmartValueTypes.BooleanArray;
		}
		else if (value instanceof StringArray)
		{
			return SmartValueTypes.StringArray;
		}
		else if (value instanceof Object[]) // :-(
		{
			Object[] arr = (Object[]) value;
			if (arr.length > 0)
			{
				value = arr[0];

				if (value instanceof Double)
				{
					return SmartValueTypes.DoubleArray;
				}
				else if (value instanceof Boolean)
				{
					return SmartValueTypes.BooleanArray;
				}
				else if (value instanceof String)
				{
					return SmartValueTypes.StringArray;
				}
				else
					return SmartValueTypes.Array;
			}
			else
				return SmartValueTypes.Array;
		}
		else
		{
			throw new RuntimeException("Hey! fix it! " + value.getClass().getCanonicalName());
//			return SmartValueTypes.Unknown;
		}
	}

	// So awful...
	public static ObservableList rawClone(Object data, SmartValueTypes type)
	{
		ObservableList ar = FXCollections.observableArrayList();
		if (data instanceof ArrayData)
		{
			switch (type)
			{
				case DoubleArray:
				{
					// notice how we have to copy this manually since somebody *cough*mitch*cough* didn't want to make it easy to export raw values?
					NumberArray arr = (NumberArray) data;
					for (int i = 0; i < arr.size(); i++)
					{
						ar.add(arr.get(i));
					}
					break;
				}
				case StringArray:
				{
					// notice this is EXACTLY the same as above, except different types. dumb
					StringArray arr = (StringArray) data;
					for (int i = 0; i < arr.size(); i++)
					{
						ar.add(arr.get(i));
					}
					break;
				}
				case BooleanArray:
				{
					// notice this is EXACTLY the same as above, except different types. dumb again
					BooleanArray arr = (BooleanArray) data;
					for (int i = 0; i < arr.size(); i++)
					{
						ar.add(arr.get(i));
					}
					break;
				}
				default:
					throw new RuntimeException("Not an array! " + type.toString());
			}
		}
		else
		{
			switch (type)
			{
				case DoubleArray:
				case StringArray:
				case BooleanArray:
				{
					Object[] arr = (Object[]) data;
					ar.addAll(Arrays.asList(arr));
					break;
				}
				default:
					throw new RuntimeException("Not an array! " + type.toString());
			}
		}
		return ar;
	}

	@Override
	public void send(SmartValue data)
	{
		if (isConnected())
		{
			nwt.putValue(data.getName(), data.getValue());
		}
	}
}
