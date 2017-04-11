package sjsu.cmpe.B295.election;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.clusterMonitoring.Cluster;
import sjsu.cmpe.B295.clusterMonitoring.PiNode;
import sjsu.cmpe.B295.clusterMonitoring.PiNodeState;
import sjsu.cmpe.B295.clusterMonitoring.PiNodeType;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.raspberrypi.node.edges.EdgeInfo;

public class HeartbeatSenderTask extends TimerTask {
	protected static Logger logger = LoggerFactory
		.getLogger("HeartbeatSenderTask");
	private NodeState nodeState;
	private Leader leader;
	private ElectionUtil util;
	private Cluster cluster;

	public HeartbeatSenderTask(Leader leader, NodeState nodeState) {
		this.nodeState = nodeState;
		this.leader = leader;
		this.cluster = leader.getCluster();
		this.util = new ElectionUtil();
	}

	@Override
	public void run() {

		PiNode piNodeLeader = new PiNode();
		piNodeLeader.setId(nodeState.getRoutingConfig().getNodeId());
		piNodeLeader.setIpAddress(nodeState.getRoutingConfig().getHost());
		piNodeLeader.setPiNodeState(PiNodeState.ACTIVE);
		piNodeLeader.setPiNodeType(PiNodeType.MASTER);
		cluster.addPiNode(piNodeLeader);

		nodeState.getEdgeMonitor().getOutboundEdges().getEdgesMap().values()
			.stream().forEach(ei -> {
				if (ei.getChannel() != null) {
					PiNode piNode = new PiNode();
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
		logClusterDetails();
	}

	public void logClusterDetails() {

		nodeState.getRoutingConfig().getRoutingEntries().stream()
			.forEach(ri -> {
				int id = ri.getId();
				PiNode piNode = new PiNode();
				piNode.setId(id);
				piNode.setIpAddress(ri.getHost());
				piNode.setPiNodeState(PiNodeState.INACTIVE);
				piNode.setPiNodeType(PiNodeType.WORKER);
				EdgeInfo ei = nodeState.getEdgeMonitor().getOutboundEdges()
					.getEdgesMap().get(id);
				if (ei != null) {
					if (ei.getChannel() != null) {
						if (ei.getChannel().isOpen()) {
							piNode.setPiNodeState(PiNodeState.ACTIVE);
						}
					}
				}
				cluster.addPiNode(piNode);

			});
		logger.info("@@@@@@@@@@@@@@ Cluster Details @@@@@@@@@@@@@@ ");
		for (Integer piNodeId : cluster.getPiNodes().keySet()) {
			PiNode piNode = cluster.getPiNodes().get(piNodeId);
			logger.info(piNode.getId() + "-" + piNode.getIpAddress() + "-"
				+ piNode.getPiNodeState() + "-" + piNode.getPiNodeType());
		}
		logger.info("@@@@@@@@@@@@@@ Cluster Details @@@@@@@@@@@@@@ ");
	}

}
