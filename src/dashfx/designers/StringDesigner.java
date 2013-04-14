/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.designers;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 *
 * @author patrick
 */
@Designer(String.class)
public class StringDesigner implements PropertyDesigner<String>
{
	private TextField ui = new TextField();

	public StringDesigner()
	{
	}


	@Override
	public void design(Property<String> prop)
	{
		ui.textProperty().bindBidirectional(prop);
	}

	@Override
	public Node getUiBits()
	{
		return ui;
	}

}
