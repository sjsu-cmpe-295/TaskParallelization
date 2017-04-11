package sjsu.cmpe.B295.raspberrypi.node.edges;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdgeHealthMonitorTask extends TimerTask {
	protected static Logger logger = LoggerFactory
		.getLogger("EdgeHealthMonitorTask");
	private final EdgeMonitor edgeMonitor;

	public EdgeHealthMonitorTask() {
		this.edgeMonitor = null;
	}

	public EdgeHealthMonitorTask(EdgeMonitor edgeMonitor) {
		this.edgeMonitor = edgeMonitor;
	}

	@Override
	public void run() {
		// Make the Edge Inactive in case of connection lost/ node is down. 
		// Can also add code here for updating UI
		edgeMonitor.getOutboundEdges().getEdgesMap().values().stream()
			.forEach(ei -> {
				if (ei.getChannel() != null) {
					if (!ei.getChannel().isOpen()) {
						ei.setActive(false);
						ei.setChannel(null);
					}
				}
			});
	}

}
