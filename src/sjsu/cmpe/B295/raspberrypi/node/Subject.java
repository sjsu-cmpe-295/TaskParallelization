package sjsu.cmpe.B295.raspberrypi.node;

public interface Subject {
	public void addObserver(Observer observer);

	public void removeObserver(Observer observer);

	public void notifyObservers();
}
