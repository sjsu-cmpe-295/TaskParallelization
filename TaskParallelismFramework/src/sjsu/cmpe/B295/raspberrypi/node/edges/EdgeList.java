package sjsu.cmpe.B295.raspberrypi.node.edges;

import java.util.concurrent.ConcurrentHashMap;

public class EdgeList {
	private ConcurrentHashMap<Integer, EdgeInfo> map = new ConcurrentHashMap<> ();

	public EdgeList() {
	}

	public EdgeInfo createIfNew(int ref, String host, int port) {
		if (hasNode(ref))
			return getNode(ref);
		else
			return addNode(ref, host, port);
	}

	public EdgeInfo addNode(int ref, String host, int port) {
		if (!verify(ref, host, port)) {
			// TODO log error
			throw new RuntimeException("Invalid node info");
		}

		if (!hasNode(ref)) {
			EdgeInfo ei = new EdgeInfo(ref, host, port);
			map.put(ref, ei);
			return ei;
		} else
			return null;
	}

	private boolean verify(int ref, String host, int port) {
		return !(ref < 0 || host == null || port < 1024);
	}

	public boolean hasNode(int ref) {
		return map.containsKey(ref);

	}

	public EdgeInfo getNode(int ref) {
		return map.get(ref);
	}

	public void removeNode(int ref) {
		map.remove(ref);
	}

	public void clear() {
		map.clear();
	}

	public ConcurrentHashMap<Integer, EdgeInfo> getEdgesMap()  {
		return map;
	}
}
