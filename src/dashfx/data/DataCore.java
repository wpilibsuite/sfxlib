/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.data;

import com.sun.scenario.effect.impl.prism.PrCropPeer;
import java.util.ArrayList;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

/**
 *
 * @author patrick
 */
public class DataCore implements DataCoreProvider, DataProcessor
{
	private SmartValue dataTree = new SmartValue(FXCollections.observableHashMap(), SmartValueTypes.Hash, "");
	private ArrayList<DataEndpoint> endpoints = new ArrayList<>();
	private boolean running = false;

	@Override
	public void addControl(Registerable r)
	{
		r.registered(this);
	}

	@Override
	public void addDataEndpoint(DataEndpoint r)
	{
		endpoints.add(r);
		r.setProcessor(this);
	}

	@Override
	public void addDataFilter(DataProcessor r)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public synchronized SmartValue getObservable(String name)
	{
		if (name.equals("/") || name.equals(""))
		{
			return dataTree;
		}
		if (name.startsWith("/"))
			name = name.substring(1);
		String[] bits = name.split("/");
		SmartValue tmp = dataTree;
		for (String string : bits)
		{
			if (tmp.asHash() == null)
			{
				tmp.setData(FXCollections.observableHashMap());
				tmp.setType(SmartValueTypes.Hash);
			}
			if (tmp.getSubKey(string, true) == null)
			{
				//TODO: should we set the name to the full path?
				tmp.asHash().put(string, new SmartValue(FXCollections.observableHashMap(), SmartValueTypes.Hash, string));
			}
			tmp = tmp.getSubKey(name);
		}
		return tmp;
	}

	@Override
	public synchronized void processData(DataProcessor source, ValueTransaction data)
	{
		if (!running)
			return;
		//TODO: delete data
		for (SmartValue smartValue : data.getValues())
		{
			getObservable(smartValue.getName()).setType(smartValue.getType());
			getObservable(smartValue.getName()).setValue(smartValue.getValue());
		}
	}

	@Override
	public void pause()
	{
		running = false;
	}

	@Override
	public void resume()
	{
		running = true;
	}
}
