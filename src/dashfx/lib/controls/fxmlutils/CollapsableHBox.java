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
package dashfx.lib.controls.fxmlutils;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 *
 * @author patrick
 */


public class CollapsableHBox extends HBox
{
	ArrayList<Node> knownNodes = new ArrayList<>();
	public CollapsableHBox()
	{
		getChildren().addListener(new ListChangeListener<Node>() {

			@Override
			public void onChanged(ListChangeListener.Change<? extends Node> change)
			{
				while (change.next())
				{
					for (final Node node : change.getAddedSubList())
					{
						if (!knownNodes.contains(node))
						{
							knownNodes.add(node);
							node.visibleProperty().addListener(new ChangeListener<Boolean>() {

								@Override
								public void changed(ObservableValue<? extends Boolean> ov, Boolean old, Boolean New)
								{
									if (New)
									{
										if (!CollapsableHBox.this.getChildren().contains(node))
										{
											CollapsableHBox.this.getChildren().add(node);
										}
									}
									else
									{

										if (CollapsableHBox.this.getChildren().contains(node))
										{
											CollapsableHBox.this.getChildren().remove(node);
										}
									}
								}
							});
						}
					}
				}
			}
		});
	}

}
