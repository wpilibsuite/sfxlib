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

import java.util.HashMap;

/**
 *
 * @author patrick
 */
public class InitInfo
{
	private String host;
	private Integer port;
	private HashMap<String, String> options = new HashMap<>();

	/**
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public Integer getPort()
	{
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port)
	{
		this.port = port;
	}

	/**
	 * @return the options
	 */
	public String getOption(String name)
	{
		return options.get(name);
	}

	/**
	 * @param options the options to set
	 */
	public void setOption(String name, String value)
	{
		this.options.put(name, value);
	}
}
