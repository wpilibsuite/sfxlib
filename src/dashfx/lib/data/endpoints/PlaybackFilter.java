/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.lib.data.endpoints;

import dashfx.lib.data.*;
import dashfx.lib.data.video.VideoProcessor;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.*;

/**
 *
 * @author patrick
 */
public class PlaybackFilter implements DataProcessor, VideoProcessor
{
	private final long FIVE_MINUTES = 1000 * 60 * 5;
	private DataProcessor next;
	private boolean viewHist = false;
	private ArrayList<Date> dates = new ArrayList<>();
	private ArrayList<VideoValueTransaction> forward = new ArrayList<>();
	private ArrayList<VideoValueTransaction> backward = new ArrayList<>();
	private HashMap<String, SmartValue> latest = new HashMap<>();
	private HashMap<String, BufferedImage> vlatest = new HashMap<>();
	private SimpleIntegerProperty pointerIndex = new SimpleIntegerProperty(-1);
	private SimpleDoubleProperty len = new SimpleDoubleProperty(0);
	private VideoProcessor vnext;

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

		while (len.get() > 0 && dates.get(0).getTime() < dates.get(dates.size() - 1).getTime() - FIVE_MINUTES)
		{
			dates.remove(0);
			forward.remove(0);
			backward.remove(0);
			pointerIndex.set(Math.max(0, pointerIndex.get() - 1));
			len.set(len.get() - 1);
		}

		int nextIndex = forward.size();
		forward.add(new VideoValueTransaction(data));
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
		backward.add(new VideoValueTransaction(vt));
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

	private void scrubTo(int from, int index)
	{
		final VideoValueTransactions vt = scrubDiffTo(from, index);
		next.processData(this, vt.transaction);
		for (String names : vt.frame.keySet())
			vnext.processFrame(names, vt.frame.get(names).image);
	}

	private synchronized VideoValueTransactions scrubDiffTo(int from, int index)
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
			return new VideoValueTransactions();
		if (index < ipointerIndex)
		{
			// must go in reverse
			VideoValueTransactions vvt = new VideoValueTransactions();
			VideoValueTransaction vt = backward.get(ipointerIndex - 1);
			ValueTransaction st = vt.transaction;
			if (st == null)
				st = new SimpleTransaction();
			vvt.add(vt.frame);
			ipointerIndex -= 1;
			while (index < ipointerIndex)
			{
				vt = backward.get(ipointerIndex - 1);
				ValueTransaction trans = vt.transaction;
				if (trans != null)
					st = SimpleTransaction.merge(st, trans);
				vvt.add(vt.frame);
				ipointerIndex -= 1;
			}
			vvt.transaction = st;
			return vvt;
		}
		else if (index > ipointerIndex)
		{
			// must go in forward
			VideoValueTransactions vvt = new VideoValueTransactions();
			VideoValueTransaction vt = forward.get(ipointerIndex + 1);
			ValueTransaction st = vt.transaction;
			if (st == null)
				st = new SimpleTransaction();
			vvt.add(vt.frame);
			ipointerIndex += 1;
			while (index < ipointerIndex)
			{
				vt = forward.get(ipointerIndex + 1);
				ValueTransaction trans = vt.transaction;
				if (trans != null)
					st = SimpleTransaction.merge(st, trans);
				vvt.add(vt.frame);
				ipointerIndex += 1;
			}
			vvt.transaction = st;
			return vvt;
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
							while (idx < 0 || (idx + next) < 0 || (idx + next) > (dates.size() - 1))
							{
								try
								{
									Thread.sleep(50);
								}
								catch (InterruptedException eeeee)
								{
									// just loop again, see if we have anything
								}
							}
							Date cur = getDateAtIndex(idx);
							Date nxt = getDateAtIndex(idx + next);
							long diff = (long) (scale * (nxt.getTime() - cur.getTime()));
							Thread.sleep(Math.max(1, diff));
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
		throw new RuntimeException("impl_save disabled currently");
		//return null;
		/*return new Object[]
		{
			forward.toArray(new ValueTransaction[]
			{
			}),
			dates.toArray()
		};*/
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
			forward.add(new VideoValueTransaction(data)); // TODO: videos
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
			// TODO: videos
			throw new RuntimeException("Yea, cant load either");
			//backward.add(vt);
		}

		int nextIndex = forward.size();
		len.set(nextIndex + 1);
		pointerIndex.set(0);
	}

	@Override
	public synchronized void processFrame(String source, BufferedImage odata)
	{
		BufferedImage data = new BufferedImage(odata.getWidth(), odata.getHeight(), odata.getType());
		data.setData(odata.getData());
		if (!isViewingHistory())
		{
			vnext.processFrame(source, data);
			pointerIndex.set(pointerIndex.get() + 1);
		}
		else if (!isLive())
		{
			// TODO: dont dump
			return;
		}

		while (len.get() > 0 && dates.get(0).getTime() < dates.get(dates.size() - 1).getTime() - FIVE_MINUTES)
		{
			dates.remove(0);
			forward.remove(0);
			backward.remove(0);
			pointerIndex.set(Math.max(0, pointerIndex.get() - 1));
			len.set(len.get() - 1);
		}

		int nextIndex = forward.size();
		forward.add(new VideoValueTransaction(source, data));
		Date di = new Date();
		dates.add(di);
		// compute the reverse patch and add to latest
		FrameUpdate fu = null;
		if (vlatest.containsKey(source))
		{
			fu = new FrameUpdate(source, vlatest.get(source));
		}
		vlatest.put(source, data);
		backward.add(new VideoValueTransaction(fu));
		len.set(nextIndex + 1);
	}

	@Override
	public void setVProcessor(VideoProcessor proc)
	{
		this.vnext = proc;
	}

	private static class VideoValueTransaction
	{
		ValueTransaction transaction;
		FrameUpdate frame;

		public VideoValueTransaction(ValueTransaction vt)
		{
			transaction = vt;
			frame = null;
		}
		public VideoValueTransaction(String name, BufferedImage bi)
		{
			frame = new FrameUpdate(name, bi);
			transaction = null;
		}
		public VideoValueTransaction(FrameUpdate fr)
		{
			frame = fr;
			transaction = null;
		}
	}

	private static class VideoValueTransactions
	{
		ValueTransaction transaction;
		Map<String, FrameUpdate> frame = new HashMap();

		public void add(FrameUpdate fu)
		{
			if (fu == null)
				return;
			frame.put(fu.imageName, fu);
		}
	}

	private static class FrameUpdate
	{
		String imageName;
		BufferedImage image;

		public FrameUpdate(String name, BufferedImage bi)
		{
			imageName = name;
			image = bi;
		}
		public FrameUpdate()
		{

		}
	}
}
