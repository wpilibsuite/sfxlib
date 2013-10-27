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
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author patrick
 */
public class VideoStream implements VideoProcessor
{
	private final String name;
	private ArrayList<VideoViewer> viewers = new ArrayList<>();

	public VideoStream(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public void register(VideoViewer viewer)
	{
		viewers.add(viewer);
	}

	public void unregister(VideoViewer viewer)
	{
		viewers.remove(viewer);
	}

	@Override
	public void processFrame(VideoProcessor vp, Image data)
	{
		for (VideoViewer videoViewer : viewers)
		{
			videoViewer.processFrame(data);
		}
	}

	@Override
	public void setProcessor(VideoProcessor proc)
	{
		// blank
	}

	@Override
	public boolean init(InitInfo info)
	{
		// don't need to init anything
		return true;
	}
}
