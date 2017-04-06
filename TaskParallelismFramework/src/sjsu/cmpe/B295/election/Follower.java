package sjsu.cmpe.B295.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class Follower extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Follower");

	public Follower(NodeState nodeState) {
		super(nodeState);
		logger.info("*********Follower*********");
	}

	@Override
	public void handleElectionEvent(CommunicationMessage msg, Channel channel) {
		if (msg.getElectionMessage().getMsg().equals("FOLLOWER")) {
			logger.info("Follower is handling the event.");
			logger.info("Handling message " + msg.getHeader().getNodeId() + " "
				+ msg.getHeader().getDestination() + " "
				+ this.nodeState.getRoutingConfig().getNodeId());
			logger.info(msg.getElectionMessage().getMsg());
		} else {
			logger.info("Got message for my previous state:"+msg.getElectionMessage().getMsg());
		}
	}
}
