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

import dashfx.lib.controls.Category;
import dashfx.lib.controls.DashFXProperties;
import dashfx.lib.controls.Designable;
import dashfx.lib.controls.GroupType;
import dashfx.lib.data.DataCoreProvider;
import dashfx.lib.data.DataPaneMode;
import dashfx.lib.data.SmartValue;
import dashfx.lib.data.values.ArraySmartValue;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author patrick
 */
@Category("General")
@Designable(value = "Scheduler", description = "The Command Scheduler", image = "/dashfx/controls/res/scheduler.png")
@GroupType("Scheduler")
@DashFXProperties("Sealed: true, Save Children: false")
public class Scheduler extends DataVBox
{
	SmartValue names, ids, canceller;
	Label lbl = new Label();
	private ChangeListener namesChanged = new ChangeListener()
	{
		@Override
		public void changed(ObservableValue ov, Object t, Object t1)
		{
			rebuild(false); //TODO: be more granular
		}
	};
	private ChangeListener idsChanged = namesChanged;

	public Scheduler()
	{
		// UI setup
		lbl.setMaxWidth(Double.MAX_VALUE);
		lbl.setText("Running Commands");
		lbl.setFont(Font.font("System", 16.0));
		VBox.setMargin(lbl, new Insets(6.0, 6.0, 6.0, 12.0));
		ui.setPrefHeight(250);

		//Data
		setDataMode(DataPaneMode.Nested);
		nameProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				unregister();
				try
				{
					names = getObservable("Names");
					ids = getObservable("Ids");
					canceller = getObservable("Cancel");
					rebuild(true);
				}
				catch(NullPointerException n)
				{
					//fail, ignore
				}
			}
		});
	}

	@Override
	public void registered(DataCoreProvider provider)
	{
		super.registered(provider); //To change body of generated methods, choose Tools | Templates.
		unregister();
		if (provider != null)
		{
			names = getObservable("Names");
			ids = getObservable("Ids");
			canceller = getObservable("Cancel");
			rebuild(true);
		}
	}

	private void unregister()
	{
		if (names != null)
			names.removeListener(namesChanged);

		if (ids != null)
			ids.removeListener(idsChanged);

	}

	private void rebuild(boolean register)
	{
		if (register)
		{
			names.addListener(namesChanged);
			ids.addListener(idsChanged);
		}
		getChildren().clear();
		getChildren().add(lbl);
		ObservableList nay = names.getData().asArray();
		ObservableList iay = ids.getData().asArray();
		int times = Math.max(nay.size(), iay.size());
		if (times == 0)
		{
			lbl.setText("No Running Commands");
		}
		else
		{
			lbl.setText("Running Commands");
		}
		for (int i = 0; i < times; i++)
		{
			getChildren().add(new CommandLister(i < nay.size() ? nay.get(i).toString() : "", i < iay.size() ? iay.get(i) : null, (i % 2 == 0) ? true:false));
		}
	}

	private class CommandLister extends HBox
	{
		String name;
		Object id;
		Label lbll;
		ImageView iv = new ImageView();

		private CommandLister(String name, Object id, boolean striped)
		{
			this.name = name;
			this.id = id;

			setAlignment(Pos.CENTER_LEFT);
			setPadding(new Insets(0, 6, 0, 6));
			lbll = new Label(name);
			lbll.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(lbll, Priority.ALWAYS);

			iv.setFitHeight(32.0);
			iv.setFitWidth(32.0);
			iv.setImage(new Image(getClass().getResourceAsStream("/dashfx/controls/res/media-playback-stop.png")));
			if (striped)
			{
				setStyle("-fx-background-color: #eee");
			}

			getChildren().add(lbll);
			if (id instanceof Double)
			{
				getChildren().add(iv);
				final double lid = (Double) id;
				iv.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent t)
					{
						ObservableList cc = canceller.getData().asArray();
						Object[] oo = cc.toArray();
						cc = FXCollections.observableArrayList(oo);
						cc.add(lid);//TODO: dont hack this. ATM this needs to be not the same object, or it fails to update
						canceller.setData(new ArraySmartValue(cc));
					}
				});
			}
		}
	}
}
