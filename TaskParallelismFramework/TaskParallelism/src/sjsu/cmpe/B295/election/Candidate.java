package sjsu.cmpe.B295.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class Candidate extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Candidate");

	public Candidate(NodeState nodeState) {
		super(nodeState);
		logger.info("*********Candidate*********");
	}

	@Override
	public void handleElectionEvent(CommunicationMessage msg, Channel channel) {
		if (msg.getElectionMessage().getMsg().equals("CANDIDATE")) {
			logger.info("Candidate is handling the event.");
			logger.info("Handling message " + msg.getHeader().getNodeId() + " "
				+ msg.getHeader().getDestination() + " "
				+ this.nodeState.getRoutingConfig().getNodeId());
			logger.info(msg.getElectionMessage().getMsg());
		} else {
			logger.info("Got message for my previous state:"
				+ msg.getElectionMessage().getMsg());
		}
	}

}
