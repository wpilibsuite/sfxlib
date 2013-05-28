/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.lib.designers;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author patrick
 */
@Designer(Number.class)
public class NumberDesigner implements PropertyDesigner<Number>
{
	Slider ui = new Slider();
	BorderPane pane = new BorderPane();

	public NumberDesigner()
	{
		ui.setMax(40);
		ui.setMin(-40);
		pane.setCenter(ui);
		Label lbl = new Label();
		lbl.textProperty().bind(ui.valueProperty().asString("%.2f"));
		pane.setRight(lbl);
	}

	//FIXME: TODO: use normal spinner
	@Override
	public void design(Property<Number> prop)
	{
		ui.valueProperty().bindBidirectional(prop);
	}

	@Override
	public Node getUiBits()
	{
		return pane;
	}
}
