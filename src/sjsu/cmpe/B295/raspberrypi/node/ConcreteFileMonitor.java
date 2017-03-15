package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcreteFileMonitor implements Subject {
	protected static Logger logger = LoggerFactory.getLogger("ConcreteFileMonitor");
	private File configFile;
	private ArrayList<Observer> observers = new ArrayList<>();

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
		notifyObservers();
	}

	@Override
	public void addObserver(Observer observer) {
		logger.info("Adding Observer");
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		logger.info("Removing Observer");
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		logger.info("Notifying Observers");
		for (Observer observer : observers) {
			observer.update();
		}
	}
	
	public void monitorFile(){
		
	}
}
