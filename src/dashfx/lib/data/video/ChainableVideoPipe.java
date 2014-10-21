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
package dashfx.lib.data.video;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author patrick
 */
public class ChainableVideoPipe implements VideoPipe
{
	List<VideoPipe> targets = new ArrayList<>();

	@Override
	public void updateFrame(BufferedImage next)
	{
		for (VideoPipe out : targets)
		{
			out.updateFrame(next);
		}
	}

	public void removeDest(VideoPipe dest)
	{
		targets.remove(dest);
	}

	public void addDest(VideoPipe dest)
	{
		if (!targets.contains(dest))
			targets.add(dest);
	}
	
}
