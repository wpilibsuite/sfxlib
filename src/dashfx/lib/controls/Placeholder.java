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
package dashfx.lib.controls;

import javafx.beans.property.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.*;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
public class Placeholder extends BorderPane
{
	private SimpleStringProperty controlPath = new SimpleStringProperty(this, "controlPath");

	public StringProperty controlPathProperty()
	{
		return controlPath;
	}

	public String getControlPath()
	{
		return controlPathProperty().get();
	}

	public void setControlPath(String cpath)
	{
		controlPathProperty().set(cpath);
	}

	private SimpleStringProperty propList = new SimpleStringProperty(this, "propList");

	public StringProperty propListProperty()
	{
		return propList;
	}

	public String getPropList()
	{
		return propListProperty().get();
	}

	public void setPropList(String cpath)
	{
		propListProperty().set(cpath);
	}

	public void replace(Node child)
	{
		setCenter(child);
		if (getOnReplaced() != null)
			getOnReplaced().handle(new Event(this, this, EventType.ROOT));
	}

	private ObjectProperty<EventHandler<? super Event>> onReplaced = new SimpleObjectProperty<>(this, "onReplaced");

	public ObjectProperty<EventHandler<? super Event>> onReplacedProperty()
	{
		return onReplaced;
	}

	public void setOnReplaced(EventHandler<? super Event> event)
	{
		onReplacedProperty().set(event);
	}

	public EventHandler<? super Event> getOnReplaced()
	{
		return onReplacedProperty().get();
	}
}
