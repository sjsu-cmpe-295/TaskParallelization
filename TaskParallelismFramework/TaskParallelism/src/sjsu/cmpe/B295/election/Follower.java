package sjsu.cmpe.B295.election;

import java.util.Random;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class Follower extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Follower");
	private static final Random random = new Random();

	public Follower(NodeState nodeState) {
		super(nodeState);
		logger.info("*********Follower*********");

	}

	private int getElectionTimeout() {
		return nodeState.getRoutingConfig().getElectionTimeout() + 150
			+ random.nextInt(150);
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
			logger.info("Got message for my previous state:"
				+ msg.getElectionMessage().getMsg());
		}
	}

	@Override
	public void bePartOfCluster() {
		logger.info("Participating in Election");
		ChangeStateTask changeStateTask = new ChangeStateTask(nodeState);
		Timer timer = new Timer();
		timer.schedule(changeStateTask, getElectionTimeout());

	}
}
