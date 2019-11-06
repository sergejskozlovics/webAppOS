package lv.lumii.tda.util;

import java.util.Timer;
import java.util.TimerTask;

public class ReschedulableTimer extends Timer {
	private Runnable  task = null;
	private TimerTask timerTask = null;

		public synchronized void schedule(Runnable runnable, long delay)
		{
			if (timerTask != null)
				timerTask.cancel();
	        task = runnable;
	        timerTask = new TimerTask()
	        {
	            @Override
	            public void run()
	            {
	                task.run();
	            }
	        };
	        this.schedule(timerTask, delay);
	    }

	    public synchronized void reschedule(long delay)
	    {
	    	if (task==null) {
	    		return; // ignore
	    	}
	        if (timerTask != null) 
	        	timerTask.cancel();
	        timerTask = new TimerTask()
	        {
	            @Override
	            public void run()
	            {
	                task.run();
	            }
	        };
	        this.schedule(timerTask, delay);
	    }
	    
	    public synchronized void cancel()
	    {
	    	if (timerTask == null)
	    		return; // ignore
	        timerTask.cancel();
	        timerTask = null;
	    }
}
