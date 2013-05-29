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
package dashfx.controls;

import dashfx.lib.data.SupportedTypes;
import dashfx.lib.controls.Designable;
import dashfx.lib.data.*;
import dashfx.lib.controls.Control;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.Node;
import javafx.scene.chart.*;

/**
 *
 * @author patrick
 */
@Designable(value = "Graph (A)", image = "/dashfx/controls/res/GraphA.png", description = "Uses built-in graph and manual list storting. Horrible")
@SupportedTypes({SmartValueTypes.Number})
public class GraphA extends LineChart<Number, Number> implements Control, ChangeListener<Object>
{
	StringProperty name = new SimpleStringProperty();

	@Designable(value = "Name", description = "The name the control binds to")
	public StringProperty nameProperty()
	{
		return name;
	}

	public String getName()
	{
		return name.getValue();
	}

	public void setName(String value)
	{
		name.setValue(value);
	}

	private XYChart.Series series = new XYChart.Series();
	private int t = 0;

	public GraphA()
	{
		super(new NumberAxis(), new NumberAxis());
		setAnimated(false);
		((NumberAxis)getXAxis()).setForceZeroInRange(false);
		((NumberAxis)getYAxis()).setForceZeroInRange(false);
		setLegendVisible(false);
        getData().add(series);
	}



//	@Override
//	protected void dataItemAdded(Series<Double, Double> series, int i, Data<Double, Double> data)
//	{
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	}
//
//	@Override
//	protected void dataItemRemoved(Data<Double, Double> data, Series<Double, Double> series)
//	{
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	}
//
//	@Override
//	protected void dataItemChanged(Data<Double, Double> data)
//	{
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	}


	@Override
	public void registered(final DataCoreProvider provider)
	{
		//TODO: add better getObservable Overrides
		if (getName() != null)
		{
			provider.getObservable(getName()).addListener(this);
		}
		name.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				if (t != null)
					provider.getObservable(t).removeListener(GraphA.this);
				provider.getObservable(t1).addListener(GraphA.this);
			}
		});
	}

	@Override
	public void changed(ObservableValue<? extends Object> ov, Object old, Object t1)
	{
		SmartValue sv = (SmartValue) ov;
		double x = sv.asNumber();

		series.getData().add(new XYChart.Data(t++, x));
		if (series.getData().size() > 500)
		{
			series.getData().remove(0);
		}
	}

	@Override
	public Node getUi()
	{
		return this;
	}
}
