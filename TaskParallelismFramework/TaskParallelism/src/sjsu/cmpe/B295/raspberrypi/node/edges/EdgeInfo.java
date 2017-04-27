package sjsu.cmpe.B295.raspberrypi.node.edges;

import io.netty.channel.Channel;

public class EdgeInfo {
	private int ref;
	private String host;
	private int port;
	private int commandPort;
	private long lastHeartbeat = 0;
	private boolean active = false;
	private Channel channel;

	EdgeInfo(int ref, String host, int port, int commandPort) {
		this.ref = ref;
		this.host = host;
		this.port = port;
		this.commandPort = commandPort;
	}

	public int getRef() {
		return ref;
	}

	public void setRef(int ref) {
		this.ref = ref;
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

	public int getCommandPort() {
		return commandPort;
	}

	public void setCommandPort(int commandPort) {
		this.commandPort = commandPort;
	}

	public long getLastHeartbeat() {
		return lastHeartbeat;
	}

	public void setLastHeartbeat(long lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}