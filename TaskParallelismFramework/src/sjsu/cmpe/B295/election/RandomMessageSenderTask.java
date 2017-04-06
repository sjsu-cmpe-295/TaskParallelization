package sjsu.cmpe.B295.election;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import io.netty.util.Timeout;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class RandomMessageSenderTask extends TimerTask {
	NodeState nodeState;
	Map<Integer, ElectionNodeStates> map;

	public RandomMessageSenderTask(NodeState nodeState) {
		// TODO Auto-generated constructor stub
		this.nodeState = nodeState;
		this.map = new HashMap<>();
		this.map.put(0, ElectionNodeStates.FOLLOWER);
		this.map.put(1, ElectionNodeStates.CANDIDATE);
		this.map.put(2, ElectionNodeStates.LEADER);
	}

	@Override
	public void run() {
		ElectionNodeStates ens = this.map.get((new Random()).nextInt(3));
		this.nodeState.setElectionNodeState(ens);
		// TODO Auto-generated method stub
		this.nodeState.getEdgeMonitor().getOutboundEdges().getEdgesMap()
			.values().stream().forEach(ei -> {
				if (ei.getChannel() != null) {
					if (ei.getChannel().isOpen()) {
						ElectionMsg electionMessage = new ElectionMsg(
							ens.toString(),
							nodeState.getRoutingConfig().getNodeId(),
							ei.getRef());

						ei.getChannel().writeAndFlush(
							electionMessage.getCommunicationMessage());
					}
				}
			});

	}

}
