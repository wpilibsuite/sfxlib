/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.designers;

import javafx.beans.property.Property;
import javafx.scene.Node;

/**
 *
 * @author patrick
 */
public interface PropertyDesigner<T>
{
	void design(Property<T> prop);
	Node getUiBits();
}
