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
	private ElectionUtil util;

	public Follower(NodeState nodeState) {
		super(nodeState);
		retries = 1;
		util = new ElectionUtil();
	}

	private int getElectionTimeout() {
		return nodeState.getRoutingConfig().getElectionTimeout() + 150
			+ random.nextInt(150);
	}

	private long getHeartBeatTimeout() {
		return nodeState.getRoutingConfig().getHeartbeatDt();
	}

	@Override
	public void handleHeartBeat(CommunicationMessage msg, Channel channel) {
		timer.reset(getElectionTimeout());

		logger.info("Got heartbeat in Follower state from leader:"
			+ msg.getElectionMessage().getLeaderId());

		int incomingTermId = msg.getElectionMessage().getTermId();
		int currentTermId = nodeState.getTermId();
		logger.debug("My term id:" + currentTermId);
		logger.debug("Incoming Term Id:" + incomingTermId);

		if ((incomingTermId > currentTermId) && (msg.getElectionMessage()
			.getLeaderId() != nodeState.getLeaderId())) {
			logger.info("Acknowledging Heartbeat from:"
				+ msg.getElectionMessage().getLeaderId());
			nodeState.setLeaderId(msg.getElectionMessage().getLeaderId());
			nodeState.setTermId(msg.getElectionMessage().getTermId());
			nodeState.setVotedFor(msg.getElectionMessage().getLeaderId());
		} else {
			logger.info("Ignoring Heartbeat from:"
				+ msg.getElectionMessage().getLeaderId());
		}
	}
	
	@Override
	public void handleVoteRequest(CommunicationMessage msg, Channel channel) {
		logger.info("Got VoteRequest from:" + msg.getHeader().getNodeId());
		int incomingTermId = msg.getElectionMessage().getTermId();
		int currentTermId = nodeState.getTermId();
		if (incomingTermId > currentTermId) {
			logger.info(
				"Acknowledging vote request" + msg.getHeader().getNodeId());
			nodeState.setTermId(incomingTermId);
			nodeState.setVotedFor(msg.getHeader().getNodeId());
			channel.writeAndFlush(util.createVoteResponse(nodeState,
				incomingTermId, msg.getHeader().getNodeId()));
			
			timer.reset(getElectionTimeout());
		} else {
			logger.info(
				"Ignoring vote request from" + msg.getHeader().getNodeId());
		}

	}

	@Override
	public void handleVoteResponse(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub

	}

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
		logger.debug("Election Timeout");
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
