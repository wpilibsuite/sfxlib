/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.data.endpoints;

import dashfx.controls.DataProcessorType;
import dashfx.controls.DesignableData;
import dashfx.data.*;

/**
 *
 * @author patrick
 */
@DesignableData(name = "Test Data", types =
{
	DataProcessorType.DataSender
}, description = "Simple sin/cosine wave generator")
public class TestDataSource implements DataSource, Runnable
{

	private DataProcessor proc;
	private Thread nNeedles;

	@Override
	public boolean init(InitInfo info)
	{
		// nothing we need :)
		return true;
	}

	@Override
	public boolean isConnected()
	{
		return true;
	}

	@Override
	public void setProcessor(DataProcessor proc)
	{
		this.proc = proc;
		if (nNeedles == null)
		{
			nNeedles = new Thread(this);
			nNeedles.setDaemon(true);
		}
		if (!nNeedles.isAlive())
		{
			nNeedles.start();
		}
	}

	@Override
	public void run()
	{
		double t = 0;
		while (true)
		{
			final SimpleTransaction trans = new SimpleTransaction();

			trans.addValue(new SmartValue(Math.sin(t), SmartValueTypes.Double, "sin"));
			trans.addValue(new SmartValue(Math.cos(t), SmartValueTypes.Double, "cos"));
			trans.addValue(new SmartValue(Math.sin(t) + Math.cos(t * 0.989), SmartValueTypes.Double, "complex"));
			trans.addValue(new SmartValue(String.valueOf(Math.sin(t)), SmartValueTypes.String, "sins"));

			proc.processData(null, trans);
			try
			{
				Thread.sleep(50);
			}
			catch (InterruptedException ex)
			{
			}
			t += 0.02;
		}
	}
}
