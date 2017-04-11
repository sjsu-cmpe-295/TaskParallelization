package sjsu.cmpe.B295.election;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.util.ITimeoutListener;
import sjsu.cmpe.B295.util.Timer;

public class Follower extends ElectionNodeState implements ITimeoutListener {
	protected static Logger logger = LoggerFactory.getLogger("Follower");
	private static final Random random = new Random();
	private Timer timer;

	public Follower(NodeState nodeState) {
		super(nodeState);

	}

	private int getElectionTimeout() {
		return nodeState.getRoutingConfig().getElectionTimeout() + 150 + random.nextInt(150);
//		return 250 + random.nextInt(150);
		
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
	public void beforeStateChange() {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void afterStateChange() {
		timer = new Timer(this, getElectionTimeout(),
			"Follower- Election Timeout Timer");
		timer.start();
	}

	@Override
	public void notifyTimeout() {
		// TODO Auto-generated method stub
		logger.info("Election Timeout");
		onElectionTimeout();
	}

	private void onElectionTimeout() {
		// TODO Auto-generated method stub
		nodeState.setElectionNodeState(ElectionNodeStates.CANDIDATE);
	}
}
