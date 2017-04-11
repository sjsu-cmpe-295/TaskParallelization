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
	private int retries;

	public Follower(NodeState nodeState) {
		super(nodeState);
		retries = 1;

	}

	private int getElectionTimeout() {
		return nodeState.getRoutingConfig().getElectionTimeout() + 150
			+ random.nextInt(150);
	}

	private long getHeartBeatTimeout() {
		return nodeState.getRoutingConfig().getHeartbeatDt();
	}

	@Override
	public void handleWhoIsTheLeader(CommunicationMessage msg,
		Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleLeaderIs(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHeartBeat(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		logger.info(
			"Got Heartbeat from:" + msg.getElectionMessage().getLeaderId());
		timer.reset(getHeartBeatTimeout());
	}

	@Override
	public void handleVoteRequest(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVoteResponse(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub

	}

	// handleLeaderHeartbeat(){
	// timer.reset(heartbeat);
	// }

	@Override
	public void beforeStateChange() {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void afterStateChange() {
		startTimer();
	}

	public void startTimer() {
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
		// if(retries-- >0){
		// startTimer();
		// } else {
		nodeState.setElectionNodeState(ElectionNodeStates.CANDIDATE);
		// }
	}
}
