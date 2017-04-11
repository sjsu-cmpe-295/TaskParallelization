package sjsu.cmpe.B295.election;

import java.util.TimerTask;

import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class HeartbeatSenderTask extends TimerTask {
	private NodeState nodeState;
	private Leader leader;
	private ElectionUtil util;

	public HeartbeatSenderTask(Leader leader, NodeState nodeState) {
		// TODO Auto-generated constructor stub
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
						leader.addWorker(ei.getRef(), ei.getChannel());
						CommunicationMessage commMsg = this.util
							.createHeartBeat(nodeState, ei.getRef());
						ei.getChannel().writeAndFlush(commMsg);
					}
				}
			});

	}

}
