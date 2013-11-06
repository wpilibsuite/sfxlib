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
package dashfx.lib.decorators;

import dashfx.lib.controls.Designable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author patrick
 */
@Designable(value = "Labeler", description = "Adds a label to a control")
public class LabelDecorator implements Decorator
{
	private StackPane root;
	private Node node;
	private Pane box;
	private Label lbl;
	private SimpleObjectProperty<Orientation> orientationProp = new SimpleObjectProperty<>(this, "orientation", Orientation.HORIZONTAL);

	public LabelDecorator()
	{
		root = new StackPane();
		root.getChildren().add(box = new HBox());
		((HBox) box).setAlignment(Pos.CENTER_LEFT);
		box.getChildren().add(lbl = new Label(""));
		orientationProperty().addListener(new ChangeListener<Orientation>()
		{
			@Override
			public void changed(ObservableValue<? extends Orientation> ov, Orientation t, Orientation t1)
			{
				box.getChildren().clear();
				root.getChildren().clear();
				if (t1 == Orientation.HORIZONTAL)
				{
					box = new HBox();
					((HBox) box).setAlignment(Pos.CENTER_LEFT);
					box.getChildren().add(lbl);
				}
				else
				{
					box = new VBox();
					((VBox) box).setAlignment(Pos.TOP_CENTER);
				}
				root.getChildren().add(box);
				if (node != null)
					box.getChildren().add(node);
				if (t1 == Orientation.VERTICAL)
					box.getChildren().add(lbl);
			}
		});
	}

	@Override
	public void decorate(Node parent)
	{
		this.node = parent;
		box.getChildren().clear();
		box.getChildren().add(lbl);
		box.getChildren().add(getOrientation() == Orientation.HORIZONTAL ? 1 : 0, parent);
		HBox.setHgrow(node, Priority.SOMETIMES);
		VBox.setVgrow(node, Priority.SOMETIMES);
	}

	@Override
	public void undecorate()
	{
		// we don't need to do anything special, just ignore this
	}

	@Override
	public Node getUi()
	{
		return root;
	}

	@Designable(value = "Label", description = "What to label the control")
	public StringProperty labelProperty()
	{
		return lbl.textProperty();
	}

	public String getLabel()
	{
		return labelProperty().getValue();
	}

	public void setLabel(String value)
	{
		labelProperty().setValue(value);
	}

	@Designable(value = "Orientation", description = "Where to put the label")
	public ObjectProperty<Orientation> orientationProperty()
	{
		return orientationProp;
	}

	public Orientation getOrientation()
	{
		return orientationProperty().getValue();
	}

	public void setOrientation(Orientation orientation)
	{
		orientationProperty().setValue(orientation);
	}
}
