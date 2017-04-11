package sjsu.cmpe.B295.election;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.util.ITimeoutListener;
import sjsu.cmpe.B295.util.Timer;

public class Candidate extends ElectionNodeState implements ITimeoutListener {
	protected static Logger logger = LoggerFactory.getLogger("Candidate");
	private static final Random random = new Random();
	private Timer timer;

	public Candidate(NodeState nodeState) {
		super(nodeState);
	}

	private int getElectionTimeout() {
		return nodeState.getRoutingConfig().getElectionTimeout() + 150 + random.nextInt(150);
//		return 250 + random.nextInt();
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

	@Override
	public void beforeStateChange() {
		// TODO Auto-generated method stub

	}

	private void getClusterSize() {
		// TODO Auto-generated method stub
		logger.info("Calculate cluster size.");
	}

	@Override
	public void afterStateChange() {
		// TODO Auto-generated method stub
		getClusterSize();
		logger.info("Start Election by sending Vote Requests");
		logger.info("Start Election Timeout Timer");
		timer = new Timer(this, getElectionTimeout(),
			"Candidate- Election Timeout Timer");
		timer.start();

	}

	@Override
	public void notifyTimeout() {
		// TODO Auto-generated method stub
		timer.cancel();
		onElectionTimeout();
	}

	private void onElectionTimeout() {
		// TODO Auto-generated method stub
		logger.info(
			"If got maximum votes, become Leader, notify everyone. Else request vote again.");

		if (false) {
			nodeState.setElectionNodeState(ElectionNodeStates.CANDIDATE);
		} else {
			afterStateChange();
		}

	}

}
