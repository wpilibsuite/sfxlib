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
package dashfx.lib.data.video;

import dashfx.lib.data.InitInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import javafx.scene.image.Image;

/**
 *
 * @author patrick
 */
public class MjpegVideo implements VideoEndpoint
{
	private static final int[] START_BYTES = new int[]
	{
		0xFF, 0xD8
	};
	private static final int[] END_BYTES = new int[]
	{
		0xFF, 0xD9
	};
	private VideoProcessor proc;
	private BGThread bg;
	private String url;

	public boolean init(InitInfo init)
	{
		url = "http://" + init.getHost() + init.getOption("path");
		//TODO: decide reloading policy
		return true;
	}

	public void setProcessor(VideoProcessor proc)
	{
		// bah, we dont need anything
		if (proc != null)
		{
			this.proc = proc;
			if (bg == null)
			{
				bg = new BGThread();
				bg.start();
			}
		}
		else
			bg.destroyed = true;
	}

	public boolean isConnected()
	{
		return false; //TODO: do
	}

	class BGThread extends Thread
	{
		boolean destroyed = false;

		public BGThread()
		{
			super("MJPEG Video Stream Background");
			setDaemon(true);
		}

		@Override
		@SuppressWarnings("SleepWhileInLoop")
		public void run()
		{
			ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();
			while (!destroyed)
			{ //TODO: THIS is a horrible impl. very inefficient. FIXME: use mime and http
				try
				{
					URL url = new URL(MjpegVideo.this.url);
					URLConnection connection = url.openConnection();
					connection.setReadTimeout(250);
					InputStream stream = connection.getInputStream();
					long lastRepaint = 0;

					while (!destroyed)
					{
						// don't flood
						while (System.currentTimeMillis() - lastRepaint < 10 && !destroyed)
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
						proc.processFrame(null, new Image(tmpStream));
					}

				}
				catch (UnknownHostException | java.net.SocketException ex)
				{
					return; //TODO: decide what is the best thing to do
//					while (false)
//					{
//						try
//						{
//							Thread.sleep(1000);
//						}
//						catch (InterruptedException x)
//						{
//						}
//					}
				}
				catch (NullPointerException e)
				{
					// must be failing now
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
				return;
//				if (!urlChanged)
//				{
//					try
//					{
//						Thread.sleep(500);
//					}
//					catch (InterruptedException ex)
//					{
//					}
//				}
			}

		}

		@Override
		public void destroy()
		{
			destroyed = true;
		}
	}
}
