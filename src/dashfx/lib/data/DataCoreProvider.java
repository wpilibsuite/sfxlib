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

import dashfx.lib.controls.Control;
import dashfx.lib.data.video.VideoCore;

/**
 *
 * @author patrick
 */
public interface DataCoreProvider
{
	void addControl(Control r);
	void addDataEndpoint(DataEndpoint r);
	void addDataFilter(DataProcessor r);
	void mountDataEndpoint(DataInitDescriptor<DataEndpoint> r);
	SmartValue getObservable(String name);
	VideoCore getVideoCore(); // TODO: kind of leaky

	void dispose();

	DataInitDescriptor<DataEndpoint>[] getAllDataEndpoints();
	DataProcessor[] getAllDataFilters();
	void clearAllDataEndpoints();
}
