package sjsu.cmpe.B295.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timer {
	private static final Logger logger = LoggerFactory.getLogger("Timer");

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
		logger.debug("******** " + identifier + ", Request to start timer: "
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
		logger.debug("********" + identifier + ", Request to cancel timer: "
			+ Thread.currentThread().getName());

		timerThread.interrupt();
	}

	public void reset(long heartbeatTimeout) {
		logger.debug("********" + identifier + ", Request to reset timer: "
			+ Thread.currentThread().getName());

		timerThread.interrupt();

		start(heartbeatTimeout);
	}

	private class TimerThread extends Thread {

		@Override
		public void run() {
			try {
				logger.debug("********Timer started: "
					+ Thread.currentThread().getName());
				logger.debug("Timeout:" + timeout);

				synchronized (this) {
					wait(timeout);
				}

				logger.debug(
					"********Timed out: " + Thread.currentThread().getName());

				listener.notifyTimeout();

			} catch (InterruptedException e) {
				logger.debug("********Timer was interrupted: "
					+ Thread.currentThread().getName());
			}
		}
	}
}
