/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.data.endpoints;

import dashfx.data.*;

/**
 *
 * @author patrick
 */
public class TestDataSource implements DataSource, Runnable
{
	private DataProcessor proc;
	private Thread nNeedles;

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
			nNeedles.start();
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
