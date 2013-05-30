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

import dashfx.lib.controls.Control;
import dashfx.lib.controls.Designable;
import dashfx.lib.data.DataCoreProvider;
import dashfx.lib.data.InitInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 *
 * @author patrick
 */
@Designable(value = "Camera", image = "/dashfx/controls/res/camera.png", description = "MJPEG ip camera view")
public class CameraView implements Control
{
	private static final int[] START_BYTES = new int[]
	{
		0xFF, 0xD8
	};
	private static final int[] END_BYTES = new int[]
	{
		0xFF, 0xD9
	};
	private boolean urlChanged = true;
	ImageView ui;
	Pane uil;
	SimpleStringProperty urlProperty;
	BGThread bg = new BGThread();

	public CameraView()
	{
		urlProperty = new SimpleStringProperty(this, "url", "http://10."+ InitInfo.getTeamNumber()  / 100 + "." + InitInfo.getTeamNumber() % 100 +".11/mjpg/video.mjpg");
		uil = new Pane();
		ui = new ImageView();
		uil.getChildren().add(ui);
		uil.setPrefHeight(120);
		uil.setPrefWidth(160);
		uil.setManaged(false);
		ui.fitHeightProperty().bind(uil.heightProperty());
		ui.fitWidthProperty().bind(uil.widthProperty());
		ui.setPreserveRatio(false);
		urlProperty.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				urlChanged = true;
			}
		});
	}

	@Designable(value = "URL", description = "The MJPEG url to connect to")
	public SimpleStringProperty urlProperty()
	{
		return urlProperty;
	}

	public String getUrl()
	{
		return urlProperty.get();
	}

	public void setUrl(String value)
	{
		urlProperty.set(value);
	}

	@Override
	public Node getUi()
	{
		return uil;
	}

	@Override
	public void registered(DataCoreProvider provider)
	{
		// bah, we dont need anything
		if (provider != null)
		{
			bg.start();
		}
		else
			bg.destroyed = true;
	}

	public class BGThread extends Thread
	{
		boolean destroyed = false;

		public BGThread()
		{
			super("Camera Viewer Background");
			setDaemon(true);
		}

		@Override
		public void run()
		{
			URLConnection connection = null;
			InputStream stream = null;
			ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();
			while (!destroyed)
			{ //TODO: THIS is a horrible impl. very inefficient. FIXME: use mime and http
				try
				{
					urlChanged = false;
					URL url = new URL(getUrl());
					connection = url.openConnection();
					connection.setReadTimeout(250);
					stream = connection.getInputStream();
					long lastRepaint = 0;

					while (!destroyed && !urlChanged)
					{
						// don't flood
						while (System.currentTimeMillis() - lastRepaint < 10)
						{
							stream.skip(stream.available());
							Thread.sleep(10);
						}
						stream.skip(stream.available());

						imageBuffer.reset();
						for (int i = 0; i < START_BYTES.length;)
						{
							int b = stream.read();
							if (b == START_BYTES[i])
								i++;
							else
								i = 0;
						}
						for (int i = 0; i < START_BYTES.length; ++i)
						{
							imageBuffer.write(START_BYTES[i]);
						}

						for (int i = 0; i < END_BYTES.length;)
						{
							int b = stream.read();
							imageBuffer.write(b);
							if (b == END_BYTES[i])
								i++;
							else
								i = 0;
						}

						lastRepaint = System.currentTimeMillis();
						ByteArrayInputStream tmpStream = new ByteArrayInputStream(imageBuffer.toByteArray());
						ui.setImage(new Image(tmpStream));
					}

				}
				catch (UnknownHostException | java.net.NoRouteToHostException ex)
				{
					while (!urlChanged)
					{
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException x)
						{
						}
					}
				}
				catch (NullPointerException e)
				{
					// must be failing now
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}

				if (!urlChanged)
				{
					try
					{
						Thread.sleep(500);
					}
					catch (InterruptedException ex)
					{
					}
				}
			}

		}

		@Override
		public void destroy()
		{
			destroyed = true;
		}
	}
}
