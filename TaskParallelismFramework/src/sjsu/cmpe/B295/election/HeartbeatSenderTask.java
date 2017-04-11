package sjsu.cmpe.B295.election;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class HeartbeatSenderTask extends TimerTask {
	protected static Logger logger = LoggerFactory
		.getLogger("HeartbeatSenderTask");
	private NodeState nodeState;
	private Leader leader;
	private ElectionUtil util;

	public HeartbeatSenderTask(Leader leader, NodeState nodeState) {
		this.nodeState = nodeState;
		this.leader = leader;
		this.util = new ElectionUtil();
	}

	@Override
	public void run() {
		nodeState.getEdgeMonitor().getOutboundEdges().getEdgesMap().values()
			.stream().forEach(ei -> {
				if (ei.getChannel() != null) {
					if (ei.getChannel().isOpen()) {
						logger.info(
							"Node " + nodeState.getRoutingConfig().getNodeId()
								+ " sending heartbeat to node " + ei.getRef());
						CommunicationMessage commMsg = this.util
							.createHeartBeat(nodeState, ei.getRef());
						ei.getChannel().writeAndFlush(commMsg);
					}
				}
			});

	}

}
