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
	private int clusterSize;
	private int minimumVotesRequired = 0;
	private int votesObtained;
	private ElectionUtil util;
	private VoteRequestorTask voteRequestorTask;

	public void setClusterSize(int clusterSize) {
		this.clusterSize = clusterSize;
	}

	public int getClusterSize() {
		return this.clusterSize;
	}

	public Candidate(NodeState nodeState) {
		super(nodeState);
		setClusterSize(1);
		util = new ElectionUtil();
	}

	private int getElectionTimeout() {
		return nodeState.getRoutingConfig().getElectionTimeout() + 150
			+ random.nextInt(150);
		// return 150 + random.nextInt(150);
	}

	@Override
	public void handleHeartBeat(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		logger.debug("Got heartbeat in Candidate state from leader:"
			+ msg.getElectionMessage().getLeaderId());
		logger.debug("My term id:" + nodeState.getTermId());
		logger
			.debug("Incoming Term Id:" + msg.getElectionMessage().getTermId());
		if (msg.getElectionMessage().getTermId() >= nodeState.getTermId()) {
			nodeState.setLeaderId(msg.getElectionMessage().getLeaderId());
			nodeState.setVotedFor(msg.getElectionMessage().getLeaderId());
			nodeState.setTermId(msg.getElectionMessage().getTermId());
			nodeState.setElectionNodeState(ElectionNodeStates.FOLLOWER);
		} else {
			logger.debug("Ignoring Heartbeat from:"
				+ msg.getElectionMessage().getLeaderId());
		}
	}

	@Override
	public void handleVoteRequest(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		logger.debug("Got Vote Request from:" + msg.getHeader().getNodeId());
		logger.debug("My term id:" + nodeState.getTermId());
		logger
			.debug("Incoming Term Id:" + msg.getElectionMessage().getTermId());
		if (msg.getElectionMessage().getTermId() > nodeState.getTermId()) {
			logger.debug(
				"Acknowledging vote request" + msg.getHeader().getNodeId());
			nodeState.setTermId(msg.getElectionMessage().getTermId());
			nodeState.setVotedFor(msg.getHeader().getNodeId());
			channel.writeAndFlush(util.createVoteResponse(nodeState,
				msg.getElectionMessage().getTermId(),
				msg.getHeader().getNodeId()));
			nodeState.setElectionNodeState(ElectionNodeStates.FOLLOWER);
		} else {
			logger.debug(
				"Ignoring vote request from" + msg.getHeader().getNodeId());
		}
	}

	@Override
	public void handleVoteResponse(CommunicationMessage msg, Channel channel) {

		if (msg.getElectionMessage().getVotedFor() == nodeState
			.getRoutingConfig().getNodeId()) {

			votesObtained += 1;
			logger.debug("Got Vote from " + msg.getHeader().getNodeId()
				+ "; Total vote count:" + votesObtained);
		}
	}

	@Override
	public void beforeStateChange() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}

	}

	private void calculateClusterSize() {
		logger.debug("Calculating cluster size...");
		setClusterSize(1);
		nodeState.getEdgeMonitor().getOutboundEdges().getEdgesMap().values()
			.stream().forEach(ei -> {
				if (ei.getChannel() != null) {
					if (ei.getChannel().isOpen()) {
						setClusterSize(getClusterSize() + 1);
					}
				}
			});
		logger.debug("Cluster size:" + getClusterSize());
	}

	@Override
	public void afterStateChange() {
		// TODO Auto-generated method stub
		nodeState.setTermId(nodeState.getTermId() + 1);
		votesObtained = 1;
		// calculateClusterSize();

		logger.debug("Sending Vote Requests");

		voteRequestorTask = new VoteRequestorTask(this, nodeState);
		voteRequestorTask.run();

		logger.debug("Start Election Timeout Timer");
		timer = new Timer(this, getElectionTimeout(),
			"Candidate- Election Timeout Timer");
		timer.start();

	}

	@Override
	public void notifyTimeout() {
		timer.cancel();
		onElectionTimeout();
	}

	private void onElectionTimeout() {

		calculateClusterSize();
		minimumVotesRequired = Math.round((getClusterSize() / 2) + 0.5f);

		if (votesObtained >= minimumVotesRequired) {
			logger.debug("Node " + this.nodeState.getRoutingConfig().getNodeId()
				+ " got " + votesObtained + " votes. Becoming leader now...");
			nodeState.setElectionNodeState(ElectionNodeStates.LEADER);
		} else {
			logger.debug("Node " + this.nodeState.getRoutingConfig().getNodeId()
				+ " got " + votesObtained + " votes; Min required were "
				+ minimumVotesRequired + ". Requesting votes again...");
			afterStateChange();
		}

	}

}
