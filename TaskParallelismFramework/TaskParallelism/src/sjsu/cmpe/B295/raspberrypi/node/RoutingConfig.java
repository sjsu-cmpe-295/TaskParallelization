package sjsu.cmpe.B295.raspberrypi.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoutingConfig {
	private AtomicInteger nodeId;
	private String host;
	private String clientIP;

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	private AtomicInteger commandPort;
	private AtomicInteger workPort;
	private AtomicLong heartbeatDt;
	private AtomicInteger electionTimeout;

	public List<RoutingEntry> routingEntries = Collections
		.synchronizedList(new ArrayList<RoutingEntry>());

	public int getNodeId() {
		return nodeId.get();
	}

	public String getHost() {
		return host;
	}

	public Integer getCommandPort() {
		return commandPort.get();
	}

	public Integer getWorkPort() {
		return workPort.get();
	}

	public Integer getElectionTimeout() {
		return electionTimeout.get();
	}

	public void setNodeId(int nodeId) {
		this.nodeId.getAndSet(nodeId);
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setCommandPort(int commandPort) {
		this.commandPort.getAndSet(commandPort);
	}

	public void setWorkPort(int workPort) {
		this.workPort.getAndSet(workPort);
	}

	public void setElectionTimeout(int electionTimeout) {
		this.electionTimeout.getAndSet(electionTimeout);
	}

	public long getHeartbeatDt() {
		return heartbeatDt.get();
	}

	public void setHeartbeatDt(long heartbeatDt) {
		this.heartbeatDt.getAndSet(heartbeatDt);
	}

	public List<RoutingEntry> getRoutingEntries() {
		return routingEntries;
	}

	public void setRoutingEntries(List<RoutingEntry> routingEntries) {
		this.routingEntries = routingEntries;
	}

	public RoutingConfig() {
		this.nodeId = new AtomicInteger();
		this.commandPort = new AtomicInteger();
		this.workPort = new AtomicInteger();
		this.heartbeatDt = new AtomicLong();
		this.electionTimeout = new AtomicInteger();
	}

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
