/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.lib.data.endpoints;

import dashfx.lib.data.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.*;

/**
 *
 * @author patrick
 */
public class PlaybackFilter implements DataProcessor
{
    
        private final long FIVE_MINUTES = 1000*60*5;
    
	private DataProcessor next;
	private boolean viewHist = false;
	private ArrayList<Date> dates = new ArrayList<>();
	private ArrayList<ValueTransaction> forward = new ArrayList<>();
	private ArrayList<ValueTransaction> backward = new ArrayList<>();
	private HashMap<String, SmartValue> latest = new HashMap<>();
	private SimpleIntegerProperty pointerIndex = new SimpleIntegerProperty(-1);
	private SimpleDoubleProperty len = new SimpleDoubleProperty(0);

	public PlaybackFilter()
	{
		pointerIndex.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number t, Number t1)
			{
				if (isViewingHistory())
					scrubTo(t.intValue(), t1.intValue());
			}
		});
	}

	@Override
	public synchronized void processData(DataProcessor source, ValueTransaction data)
	{
		if (!isViewingHistory())
		{
			next.processData(this, data);
			pointerIndex.set(pointerIndex.get() + 1);
		}
		else if (!isLive())
		{
			// TODO: dont dump
			return;
		}
                
                while (len.get() > 0 && dates.get(0).getTime() < dates.get(dates.size()-1).getTime()-FIVE_MINUTES)
                {
                    dates.remove(0);
                    forward.remove(0);
                    backward.remove(0);
                    pointerIndex.set(Math.max(0,pointerIndex.get()-1));
                    len.set(len.get()-1);
                }
                
		int nextIndex = forward.size();
		forward.add(data);
		Date di = new Date();
		dates.add(di);
		// compute the reverse patch and add to latest
		SimpleTransaction vt = new SimpleTransaction();
		for (SmartValue smartValue : data.getValues())
		{
			if (latest.containsKey(smartValue.getName()))
			{
				vt.addValue(latest.get(smartValue.getName()));
			}
			latest.put(smartValue.getName(), smartValue);
		}
		backward.add(vt);
		len.set(nextIndex + 1);
	}

	public synchronized void enterPlayback()
	{
		viewHist = true;
	}

	public synchronized void leavePlayback()
	{
		viewHist = false;
		isLive = true;
		//TODO: restore pointer and data
	}

	public synchronized IntegerProperty getPointer()
	{
		return pointerIndex;
	}

	public synchronized DoubleProperty getLength()
	{
		return len;
	}

	public synchronized Date getDateAtIndex(int idx)
	{
		//TODO: checkj if correct
		return dates.get(idx);
	}

	private synchronized void scrubTo(int from, int index)
	{
		ValueTransaction vt = scrubDiffTo(from, index);
		next.processData(this, vt);
	}

	private synchronized ValueTransaction scrubDiffTo(int from, int index)
	{
		if (!isViewingHistory())
			throw new IllegalStateException("Must be in playback mode!");
		//TODO: optimize
		int ipointerIndex = from;
		if (index >= forward.size())
			index = forward.size() - 1;
		if (ipointerIndex >= forward.size())
			ipointerIndex = forward.size() - 1;
		if (index == ipointerIndex || index == 0)
			return new SimpleTransaction();
		if (index < ipointerIndex)
		{
			// must go in reverse
			ValueTransaction st = backward.get(ipointerIndex - 1);
			ipointerIndex -= 1;
			while (index < ipointerIndex)
			{
				st = SimpleTransaction.merge(st, backward.get(ipointerIndex - 1));
				ipointerIndex -= 1;
			}
			return st;
		}
		else if (index > ipointerIndex)
		{
			// must go in forward
			ValueTransaction st = forward.get(ipointerIndex + 1);
			ipointerIndex += 1;
			while (index < ipointerIndex)
			{
				st = SimpleTransaction.merge(st, forward.get(ipointerIndex + 1));
				ipointerIndex += 1;
			}
			return st;
		}
		return null;
	}

	@Override
	public void setProcessor(DataProcessor proc)
	{
		this.next = proc;
	}

	private boolean isViewingHistory()
	{
		return viewHist;
	}

	public boolean isLive()
	{
		return isLive;
	}
	private boolean isLive = true;
	private Thread runner;
	private boolean running = false;

	public void playWithTime()
	{
		playWithTime(1.0);
	}

	public synchronized void playWithTime(final double scale)
	{
		if (runner == null && isViewingHistory())
		{
			running = true;
			runner = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						int next = scale > 0 ? 1 : -1;
						while (running)
						{
							int idx = pointerIndex.get();
							if (idx < 0 || (idx + next) < 0 || (idx + next) > (dates.size()-1))
							{
								stopTheFilm();
								return;
							}
							Date cur = getDateAtIndex(idx);
							Date nxt = getDateAtIndex(idx + next);
							long diff = (long) (scale * (nxt.getTime() - cur.getTime()));
							Thread.sleep(Math.max(0, diff));
							pointerIndex.set(pointerIndex.get() + next);
						}
					}
					catch (InterruptedException ex)
					{
						System.out.println("Ohno! Player was interrupted!");
					}
					runner = null;
				}
			});
			runner.setDaemon(true);
			runner.start();
		}
		else
		{
			System.out.println("NOT playing with time, you are already running or not viewing history");
		}
	}

	public synchronized void stopTheFilm()
	{
		if (runner != null)
		{
			running = false;
		}
	}

	@Override
	public boolean init(InitInfo info)
	{
		// we need nothing :-)
		return true;
	}

	public synchronized Object[] _impl_save()
	{
		return new Object[]
		{
			forward.toArray(new ValueTransaction[]
			{
			}),
			dates.toArray()
		};
	}

	public synchronized void _impl_load(ValueTransaction[] trans, List<Date> ndates)
	{
		forward.clear();
		backward.clear();
		isLive = false;
		dates.clear();
		dates.addAll(ndates);
		latest.clear();
		len.set(0);
//TODO: don't dupe this code
		for (ValueTransaction data : trans)
		{
			forward.add(data);
			// compute the reverse patch and add to latest
			SimpleTransaction vt = new SimpleTransaction();
			for (SmartValue smartValue : data.getValues())
			{
				if (latest.containsKey(smartValue.getName()))
				{
					vt.addValue(latest.get(smartValue.getName()));
				}
				latest.put(smartValue.getName(), smartValue);
			}
			backward.add(vt);
		}

		int nextIndex = forward.size();
		len.set(nextIndex + 1);
		pointerIndex.set(0);
	}
}
