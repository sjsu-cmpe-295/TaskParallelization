package sjsu.cmpe.B295.raspberrypi.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoutingConfig {
	private AtomicInteger nodeId;
	private AtomicInteger commandPort;
	private AtomicInteger workPort;

	public AtomicInteger getNodeId() {
		return nodeId;
	}

	public AtomicInteger getCommandPort() {
		return commandPort;
	}

	public AtomicInteger getWorkPort() {
		return workPort;
	}

	public void setNodeId(AtomicInteger nodeId) {
		this.nodeId = nodeId;
	}

	public void setCommandPort(AtomicInteger commandPort) {
		this.commandPort = commandPort;
	}

	public void setWorkPort(AtomicInteger workPort) {
		this.workPort = workPort;
	}

	public RoutingConfig() {
		this.nodeId = new AtomicInteger();
		this.commandPort = new AtomicInteger();
		this.workPort = new AtomicInteger();
	}

	public List<RoutingEntry> routingEntries = Collections
		.synchronizedList(new ArrayList<RoutingEntry>());

	public HashMap<String, Integer> asHashMap() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		if (routingEntries != null) {
			for (RoutingEntry entry : routingEntries) {
				map.put(entry.host, entry.port);
			}
		}
		return map;
	}

	@XmlRootElement(name = "entry")
	@XmlAccessorType(XmlAccessType.PROPERTY)
	public static final class RoutingEntry {
		private String host;
		private int port;
		private int id;

		public RoutingEntry() {
		}

		public RoutingEntry(int id, String host, int port) {
			this.id = id;
			this.host = host;
			this.port = port;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	public void addEntry(RoutingEntry entry) {
		if (entry == null)
			return;

		if (routingEntries == null)
			routingEntries = new ArrayList<RoutingEntry>();

		routingEntries.add(entry);
	}
}
