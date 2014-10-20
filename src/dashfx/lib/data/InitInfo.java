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
package dashfx.lib.data;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patrick
 */
public class InitInfo
{
	private static int teamNumber;
	private String host;
	private String path;
	private String protocol;
	private Integer port;
	private HashMap<String, String> options = new HashMap<>();

	/**
	 * @return the host
	 */
	public String getHost()
	{
		if (host == null || "".equals(host))
		{
			if (teamNumber == 0)
				return "127.0.0.1";
			return "roborio-"+teamNumber + ".local";
		}
		return host;
	}

	public String getRawHost()
	{
		return host;
	}

	public String getProtocol()
	{
		return getProtocol(null);
	}

	public String getProtocol(String Default)
	{
		if (protocol == null)
			return Default;
		return protocol;
	}

	public static void setTeamNumber(int num)
	{
		teamNumber = num;
	}

	public static int getTeamNumber()
	{
		return teamNumber;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		if (host.contains("://"))
		{
			int index = host.indexOf("://");
			protocol = host.substring(0, index);
			this.host = host.substring(index+3);
		}
		else
		{
			protocol = null;
			this.host = host;
		}
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

	public HashMap<String, String> getAllOptions()
	{
		return this.options;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getUrl()
	{
		return getUrl("???");
	}

	public String getUrl(String defProtocol)
	{
		StringBuilder sb = new StringBuilder();
		if (getProtocol(null) == null)
		{
			sb.append(defProtocol);
			sb.append("://");
		}
		sb.append(getHost());
		if (getPort()!= null)
		{
			sb.append(':');
			sb.append(getPort());
		}
		if (getPath() == null || !getPath().startsWith("/"))
			sb.append('/');
		if (getPath() != null);
			sb.append(getPath());

		int join = 0;
		
		for (String k : options.keySet())
		{
			if (join++ == 0)
				sb.append("?");
			else
				sb.append("&");
			try
			{
				sb.append(URLEncoder.encode(k, "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(options.get(k), "UTF-8"));
			}
			catch (UnsupportedEncodingException ex)
			{
				throw new RuntimeException(ex); // this is strange
			}
		}
			
		return sb.toString();
	}
}
