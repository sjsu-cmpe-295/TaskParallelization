package sjsu.cmpe.B295.raspberrypi.node;

public interface IFileSubject {
	public void addObserver(IFileObserver observer);

	public void removeObserver(IFileObserver observer);

	public void notifyObservers();
}
