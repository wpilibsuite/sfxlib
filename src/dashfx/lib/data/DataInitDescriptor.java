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

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author patrick
 */
public class DataInitDescriptor<T extends Initable>
{
	private T object;
	private SimpleStringProperty name;
	private InitInfo initInfo;
	private SimpleStringProperty mountPoint;

	public DataInitDescriptor()
	{
		name = new SimpleStringProperty("");
		mountPoint = new SimpleStringProperty("/");
	}

	public DataInitDescriptor(T object, String name, InitInfo initInfo, String mountPoint)
	{
		this.object = object;
		this.name = new SimpleStringProperty(name);
		this.initInfo = initInfo;
		this.mountPoint = new SimpleStringProperty(mountPoint);
	}
	
	public SimpleStringProperty nameProperty()
	{
		return name;
	}
	
	public SimpleStringProperty mountPointProperty()
	{
		return mountPoint;
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
	 * @param object the object to set
	 */
	public void setObject(T object)
	{
		this.object = object;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name.get();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name.set(name);
	}

	/**
	 * @return the initInfo
	 */
	public InitInfo getInitInfo()
	{
		return initInfo;
	}

	/**
	 * @param initInfo the initInfo to set
	 */
	public void setInitInfo(InitInfo initInfo)
	{
		this.initInfo = initInfo;
	}

	/**
	 * @return the mountPoint
	 */
	public String getMountPoint()
	{
		return mountPoint.get();
	}

	/**
	 * @param mountPoint the mountPoint to set
	 */
	public void setMountPoint(String mountPoint)
	{
		this.mountPoint.set(mountPoint);
	}

	@Override
	public String toString()
	{
		return "DataInitDescriptor(" + object.toString()+ ", name = \"" + getName()+"\", mount = \"" + getMountPoint() +"\" )";
	}
}
