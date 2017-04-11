package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcreteFileMonitor implements IFileSubject {
	protected static Logger logger = LoggerFactory
		.getLogger("ConcreteFileMonitor");
	private File configFile;
	private ArrayList<IFileObserver> observers = new ArrayList<>();

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
		notifyObservers();
	}

	@Override
	public void addObserver(IFileObserver observer) {
		logger.debug("Adding Observer");
		observers.add(observer);
	}

	@Override
	public void removeObserver(IFileObserver observer) {
		logger.debug("Removing Observer");
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		logger.debug("Notifying Observers");
		for (IFileObserver observer : observers) {
			observer.update();
		}
	}

	public void monitorFile() {

	}
}
