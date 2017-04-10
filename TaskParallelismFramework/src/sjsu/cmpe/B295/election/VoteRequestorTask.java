package sjsu.cmpe.B295.election;

import java.util.TimerTask;

import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class VoteRequestorTask extends TimerTask {
	private Candidate candidate;
	private NodeState nodeState;
	private ElectionUtil util;

	public VoteRequestorTask(Candidate candidate, NodeState nodeState) {
		this.candidate = candidate;
		this.nodeState = nodeState;
		this.util = new ElectionUtil();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		nodeState.getEdgeMonitor().getOutboundEdges().getEdgesMap().values()
			.stream().forEach(ei -> {
				if (ei.getChannel() != null) {
					if (ei.getChannel().isOpen()) {
						CommunicationMessage commMsg = util.createVoteRequest(
							nodeState, nodeState.getTermId(), ei.getRef());
						ei.getChannel().writeAndFlush(commMsg);
					}
				}
			});
	}

}
