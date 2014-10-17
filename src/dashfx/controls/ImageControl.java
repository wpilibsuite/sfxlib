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
import dashfx.lib.controls.Designer;
import dashfx.lib.data.DataCoreProvider;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author patrick
 */
@Designable(value = "Image", description = "Image Control", image = "/dashfx/controls/res/image.png")
@Category("General")
public class ImageControl extends BorderPane implements Control
{
	private SimpleStringProperty file = new SimpleStringProperty(this, "file", "");
	private javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView();

	public ImageControl()
	{
		setPrefWidth(32);
		setPrefHeight(32);
		iv.fitHeightProperty().bind(heightProperty());
		iv.fitWidthProperty().bind(widthProperty());
		getChildren().add(iv);
		file.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				try
				{
					iv.setImage(new Image("file:" + t1));
				}
				catch (Throwable tr)
				{
					//Dont do it! bad path!
				}
			}
		});
	}

	@Designable(value="File", description = "Path to image to load")
	@Designer(File.class)
	public StringProperty fileProperty()
	{
		return file;
	}

	public String getFile()
	{
		return fileProperty().getValue();
	}

	public void setFile(String file)
	{
		fileProperty().setValue(file);
	}

	@Override
	public Node getUi()
	{
		return this;
	}

	@Override
	public void registered(DataCoreProvider provider)
	{
		// Nothing :-)
	}
}
