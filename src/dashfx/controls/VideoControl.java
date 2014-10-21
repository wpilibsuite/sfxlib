/*
 * Copyright (C) 2014 patrick
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
import dashfx.lib.data.DataCoreProvider;
import dashfx.lib.data.SmartValue;
import dashfx.lib.data.video.VideoViewer;
import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.*;

/**
 *
 * @author patrick
 */
@Designable(value = "VCamera", image = "/dashfx/controls/res/camera.png", description = "MJPEG ip camera view")
@Category("General")
public class VideoControl extends ImageView implements VideoViewer, Control
{
	WritableImage im;

	public VideoControl()
	{
		im = new WritableImage(120,120);
		
		setImage(im);
	}
	
	@Override
	public void updateFrame(final BufferedImage next)
	{
		Platform.runLater(new Runnable() {

			@Override
			public void run()
			{
				im = SwingFXUtils.toFXImage(next, im);
				setImage(im);
			}
		});
	}
	
	// TODO: we should probbably wrap this so we can resize properly
	public double getWidth()
	{
		return super.getFitWidth();
	}
	
	public double getHeight()
	{
		return super.getFitHeight();
	}

	@Override
	public Node getUi()
	{
		return this;
	}

	StringProperty name = new SimpleStringProperty();

	@Designable(value = "Name", description = "The name the video binds to")
	public StringProperty nameProperty()
	{
		return name;
	}

	public String getName()
	{
		return name.getValue();
	}

	public void setName(String value)
	{
		name.setValue(value);
	}

	@Override
	public void registered(final DataCoreProvider provider)
	{
		//TODO: add better getObservable Overrides
		if (getName() != null)
		{
			provider.getVideoCore().addViewer(getName(), this);
		}
		name.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				if (t != null)
					provider.getVideoCore().removeViewer(t,VideoControl.this);
				System.out.println("Adding: " + t1);
				provider.getVideoCore().addViewer(t1,VideoControl.this);
			}
		});
	}
}
