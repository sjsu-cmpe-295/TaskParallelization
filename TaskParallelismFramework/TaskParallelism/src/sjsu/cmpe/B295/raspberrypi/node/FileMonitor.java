package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMonitor extends ConcreteFileMonitor {
	protected static Logger logger = LoggerFactory.getLogger("FileMonitor");
	String dirPath;

	public FileMonitor() {

	}

	public FileMonitor(String dirPath) {
		this.dirPath = dirPath;
	}

	@Override
	public void monitorFile() {
		logger.debug("In monitor File");
		File file = new File(this.dirPath);

		if (!file.exists())
			throw new RuntimeException(file.getAbsolutePath() + " not found");

		super.setConfigFile(file);
		FileWatcher f = new FileWatcher(file);
		f.start();
	}

	public void setConfigFile(File file) {
		logger.debug("In setConfigFile File");
		super.setConfigFile(file);
	}

	private class FileWatcher extends Thread {

		private AtomicBoolean stop = new AtomicBoolean(false);
		public File file;

		public FileWatcher(File file) {
			this.file = file;
		}

		@Override
		public void run() {
			logger.debug("In File Watcher run");
			try (WatchService watcher = FileSystems.getDefault()
				.newWatchService()) {
				Path path = file.toPath().getParent();
				path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
				while (!isStopped()) {
					WatchKey key;
					try {
						key = watcher.poll(200, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						return;
					}
					if (key == null) {
						Thread.yield();
						continue;
					}

					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();

						@SuppressWarnings("unchecked")
						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path filename = ev.context();

						if (kind == StandardWatchEventKinds.OVERFLOW) {
							Thread.yield();
							continue;
						} else if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY) {
							// super.
							setConfigFile(file);
							// notifyObservers(file);
						}
						boolean valid = key.reset();
						if (!valid) {
							break;
						}
					}
					Thread.yield();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		public boolean isStopped() {
			return stop.get();
		}

		public void stopThread() {
			stop.set(true);
		}

	}

}
