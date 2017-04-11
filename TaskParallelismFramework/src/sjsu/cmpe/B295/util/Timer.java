package sjsu.cmpe.B295.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timer {
	private static final Logger logger = LoggerFactory.getLogger("Timer");
	private static final boolean debug = true;
	private final String identifier;
	private long timeout;
	private ITimeoutListener listener;
	private TimerThread timerThread;

	public Timer(ITimeoutListener listener, long timeout, String identifier) {
		this.listener = listener;
		this.timeout = timeout;
		this.identifier = identifier;
	}

	public void start() {
		if (debug)
			logger.info("******** " + identifier + ", Request to start timer: "
				+ Thread.currentThread().getName());

		timerThread = new TimerThread();
		timerThread.start();
	}

	public void start(long timeout) {
		this.timeout = timeout;
		start();
	}

	public void start(ITimeoutListener listener, long timeout) {
		this.listener = listener;
		this.timeout = timeout;
		start();
	}

	public void cancel() {
		if (timerThread == null) {
			return;
		}
		if (debug)
			logger.info("********" + identifier + ", Request to cancel timer: "
				+ Thread.currentThread().getName());

		timerThread.interrupt();
	}
	
	public void reset(long heartbeatTimeout){
		if (debug)
			logger.info("********" + identifier + ", Request to reset timer: "
				+ Thread.currentThread().getName());
		
		timerThread.interrupt();
		
		start(heartbeatTimeout);
	}
	

	private class TimerThread extends Thread {

		@Override
		public void run() {
			try {
				if (debug){
					logger.info("********Timer started: "
						+ Thread.currentThread().getName());
					logger.info("Timeout:"+timeout);
				}
					

				synchronized (this) {
					wait(timeout);
				}

				if (debug)
					logger.info("********Timed out: "
						+ Thread.currentThread().getName());

				listener.notifyTimeout();

			} catch (InterruptedException e) {
				logger.info("********Timer was interrupted: "
					+ Thread.currentThread().getName());
			}
		}
	}
}
