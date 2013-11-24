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

import dashfx.controls.bases.*;
import dashfx.lib.controls.Category;
import dashfx.lib.controls.Designable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

/**
 *
 * @author patrick
 */
@Designable(description = "Number editor with scrolling", value = "Number Box")
@Category("General")
public class NumberSpinner extends RangedNumberControlBase
{
	//property_accessor :value
	DoubleProperty step;
	boolean noback;
	BooleanProperty logStyle, showButtons;
	TextField field;
	Button plus, minus;

	@Designable(value = "Step", description = "Amount to step when +/- buttons pressed")
	public DoubleProperty stepProperty()
	{
		return step;
	}

	public double getStep()
	{
		return stepProperty().getValue();
	}

	public void setStep(double value)
	{
		stepProperty().setValue(value);
	}

	@Designable(value = "Log", description = "Use log10(x) to calculate step")
	public BooleanProperty logStyleProperty()
	{
		return logStyle;
	}

	public boolean getLogStyle()
	{
		return logStyleProperty().getValue();
	}

	public void setLogStyle(boolean value)
	{
		logStyleProperty().setValue(value);
	}

	@Designable(value = "Buttons", description = "Show the + and - increment buttons")
	public BooleanProperty showButtonsProperty()
	{
		return showButtons;
	}

	public boolean getShowButtons()
	{
		return showButtonsProperty().getValue();
	}

	public void setShowButtons(boolean value)
	{
		showButtonsProperty().setValue(value);
	}

	public NumberSpinner()
	{
		init();
	}

	private void init()
	{
		final HBox hbox = new HBox();
		setUi(hbox);
		this.minProperty().setValue(Double.NEGATIVE_INFINITY);
		this.step = new SimpleDoubleProperty(this, "step", 0);
		maxProperty().setValue(Double.POSITIVE_INFINITY);
		this.logStyle = new SimpleBooleanProperty(this, "logStyle", false);
		this.showButtons = new SimpleBooleanProperty(this, "showButtons", true);
		valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number t, Number t1)
			{
				if (!noback)
					field.setText(t1.toString());
			}
		});
		showButtons.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1)
			{
				if (t1) // if show
				{
					hbox.getChildren().addAll(plus, minus);
				}
				else
				{
					hbox.getChildren().removeAll(plus, minus);
				}
			}
		});
		Font font = new Font("System Bold", 13);

		field = new TextField("0.0");
		field.setAlignment(Pos.CENTER_RIGHT);
		field.setMaxHeight(Double.MAX_VALUE);
		field.setPrefWidth(200.0);
		field.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1)
			{
				if (!t1)
					reparseValue();
			}
		});

		field.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				noback = true;
				reparseValue();
				noback = false;
			}
		});

		HBox.setHgrow(field, Priority.ALWAYS);

		minus = new Button("-");
		minus.setFont(font);
		minus.setMaxHeight(Double.MAX_VALUE);
		minus.setMinWidth(24);
		minus.getStyleClass().add("minus");
		minus.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent t)
			{
				minusPress(t);
			}
		});
		// minus.on_mouse_released &method(:released)

		plus = new Button("+");
		plus.setFont(font);
		plus.setMaxHeight(Double.MAX_VALUE);
		plus.setMinWidth(24);
		plus.getStyleClass().add("plus");
		plus.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent t)
			{
				plusPress(t);
			}
		});
		// plus.on_mouse_released &method(:released)

		hbox.getChildren().addAll(field, minus, plus);
		hbox.getStyleClass().add("number-spinner");
	}

	private void minusPress(MouseEvent e)
	{
		reparseValue();
		double stepped = getValue() - step();
		if (stepped < getMin() || stepped > getMax())
			setValue(getMin());
		else
			setValue(stepped);

	}

	private void plusPress(MouseEvent e)
	{
		reparseValue();
		double stepped = getValue() + step();
		if (stepped < getMin() || stepped > getMax())
			setValue(getMax());
		else
			setValue(stepped);

	}

	private double step()
	{
		if (step.getValue() == 0)
		{
			double x = getValue();
			if (Math.abs(x) > Double.MAX_VALUE)
				return 0;
			double k = 1;
			if (x > 10)
			{
				while (x > 10)
				{
					x /= 10.0;
					k *= 10;
				}
				if (x < 2)
					k /= 10.0;

			}
			else if (x == 0)
				k = 1;
			else if (logStyle.getValue())
			{
				if (x == 1)
					k = 0.1;
				else
				{
					while (x < 1)
					{
						if (Math.abs(x) > Double.MAX_VALUE)
							return -1;
						x *= 10.0;
						k /= 10.0;
					}
				}
			}
			else
				k = 1;
			return k;
		}
		else
		{
			return step.getValue();
		}
	}

	private void reparseValue()
	{
		try
		{
			setValue(Double.parseDouble(field.getText()));
		}
		catch (NumberFormatException e)
		{
			setValue(0.0);
		}
	}
}
