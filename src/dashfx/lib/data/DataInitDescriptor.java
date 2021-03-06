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

/**
 *
 * @author patrick
 */
public class DataInitDescriptor<T extends Initable>
{
	private T object;
	private String name;
	private InitInfo initInfo;
	private String mountPoint;

	public DataInitDescriptor()
	{
		name = "";
		mountPoint = "/";
	}

	public DataInitDescriptor(T object, String name, InitInfo initInfo, String mountPoint)
	{
		this.object = object;
		this.name = name;
		this.initInfo = initInfo;
		_setMountPoint(mountPoint);
	}

	/**
	 * Try to re-initialize the object
	 */
	public void reInit()
	{
		object.init(initInfo);
	}

	/**
	 * @return the object
	 */
	public T getObject()
	{
		return object;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the initInfo
	 */
	public InitInfo getInitInfo()
	{
		return initInfo;
	}

	/**
	 * @return the mountPoint
	 */
	public String getMountPoint()
	{
		return mountPoint;
	}

	/**
	 * Normalizes and sets the mount point
	 * @param mountPoint the mountPoint to set
	 */
	private void _setMountPoint(String mountPoint)
	{
		this.mountPoint = mountPoint;
		if (!mountPoint.startsWith("/"))
			this.mountPoint = "/"+mountPoint;
	}

	@Override
	public String toString()
	{
		return "DataInitDescriptor(" + object.toString() + ", name = \"" + getName() + "\", mount = \"" + getMountPoint() + "\" )";
	}
}
