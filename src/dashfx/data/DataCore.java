/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.data;

import java.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;

/**
 *
 * @author patrick
 */
public class DataCore implements DataCoreProvider, DataProcessor
{
	private SmartValue dataTree = new SmartValue(FXCollections.observableHashMap(), SmartValueTypes.Hash, "");
	private ArrayList<DataEndpoint> endpoints = new ArrayList<>();
	private LinkedList<DataProcessor> filters = new LinkedList<>();
	private boolean running = false;
	private ReadOnlyListWrapper<String> knownNames;

	public DataCore()
	{
		this.knownNames = new ReadOnlyListWrapper<>(FXCollections.observableArrayList(new TreeSet<String>()));
	}

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
		filters.add(r);
		r.setProcessor(this);
	}

	@Override
	public synchronized SmartValue getObservable(String name)
	{
		if (name.equals("/") || name.isEmpty())
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
			return;//TODO: whats the best plan of action?
		if (source != null) // normal data source
		{
			int idx = filters.indexOf(source);
			if (idx == -1) //WOOT magic number!
			{
				System.out.println("Warning: unable to determine source filter; discarding data. If this is because it's a data source, use null.");
				System.out.println(source);
			}
			else if (idx == filters.size() - 1) // last element, therefore, we process it
			{
				mergeToTree(data);
			}
			else
			{
				filters.get(idx + 1).processData(this, data); //TODO: whats the source for this?
			}
		}
		else
		{
			// it is from the data sources , then send it through the filters
			if (filters.size() > 0)
			{
				filters.get(0).processData(this, data);
			}
			else // nothing to filter through, so we merge it
				mergeToTree(data);
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

	@Override
	public void setProcessor(DataProcessor proc)
	{
		throw new UnsupportedOperationException("Not supported yet. Only one core is permitted");
	}

	private void mergeToTree(final ValueTransaction data)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				//TODO: delete data
				for (SmartValue smartValue : data.getValues())
				{
					if (!knownNames.contains(smartValue.getName()))
						knownNames.add(smartValue.getName());
					getObservable(smartValue.getName()).setType(smartValue.getType());
					getObservable(smartValue.getName()).setValue(smartValue.getValue());
				}
			}
		});
	}

	public ObservableList<String> getKnownNames()
	{
		return knownNames.getReadOnlyProperty();
	}
}
