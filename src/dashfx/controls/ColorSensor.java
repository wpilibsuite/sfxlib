/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.controls;

import dashfx.controls.bases.ControlBase;
import dashfx.lib.data.values.SmartValueAdapter;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

/**
 *
 * @author Sam
 */
public class ColorSensor extends ControlBase
{
	private Pane pane = new Pane();

	public ColorSensor()
	{
		pane.setStyle("-fx-background-color: #000");
		pane.setPrefSize(32, 32);
		setUi(pane);
	}

	@Override
	protected void changed(Object newValue, SmartValueAdapter data)
	{
		ObservableList vals = data.asArray();
		String hexstring;
		if (vals.size() == 3)
		{
			hexstring = "";
			for (int i = 0; i < 3; i++)
			{
				int tmp = 0;
				try
				{
					// try to parse all colors
					tmp = (int)Double.parseDouble(vals.get(i).toString());
				}
				catch (NumberFormatException e)
				{
					// default to 0 (aka black) for invalid numbers
				}
				tmp = Math.max(0, Math.min(255, tmp));
				String stemp = Integer.toHexString(tmp);
				if (stemp.length() < 2)
					stemp = "0" + stemp;
				hexstring += stemp;
			}
		}
		else
		{
			hexstring = "000";
		}

		pane.setStyle("-fx-background-color: #" + hexstring);
	}
}
