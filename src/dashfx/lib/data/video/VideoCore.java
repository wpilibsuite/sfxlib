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

import java.util.HashMap;

/**
 *
 * @author patrick
 */
public class VideoCore
{
	private HashMap<String, VideoEndpoint> endpoints = new HashMap<>();
	private HashMap<String, ChainableVideoPipe> pipes = new HashMap<>();

	//void addVideoFilter(VideoProcessor r);
	public void mountVideoEndpoint(String name, VideoEndpoint r)
	{
		ChainableVideoPipe pipe = new ChainableVideoPipe();
		r.setTarget(pipe);
		endpoints.put(name, r);
		pipes.put(name, pipe);
	}
	
	public void addViewer(String name, VideoViewer viewer)
	{
		ChainableVideoPipe pipe = pipes.get(name);
		if (pipe != null) // TODO: weakness
			pipe.addDest(viewer);
	}
	
	public void removeViewer(String name, VideoViewer viewer)
	{
		ChainableVideoPipe pipe = pipes.get(name);
		if (pipe != null)
			pipe.removeDest(viewer);
	}

	void dispose()
	{
		//TODO: ???
	}

	//TODO: fix these
//	DataInitDescriptor<DataEndpoint>[] getAllDataEndpoints();
//	DataProcessor[] getAllDataFilters();
//	void clearAllDataEndpoints();

}
