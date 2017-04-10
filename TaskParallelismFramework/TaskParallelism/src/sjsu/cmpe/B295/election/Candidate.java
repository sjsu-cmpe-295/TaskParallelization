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
		logger.info("Got heartbeat in Candidate state from leader:"
			+ msg.getElectionMessage().getLeaderId());
		if (msg.getElectionMessage().getTermId() >= nodeState.getTermId()) {
			nodeState.setLeaderId(msg.getElectionMessage().getLeaderId());
			nodeState.setVotedFor(msg.getElectionMessage().getLeaderId());
			nodeState.setTermId(msg.getElectionMessage().getTermId());
			nodeState.setElectionNodeState(ElectionNodeStates.FOLLOWER);
		}
	}

	@Override
	public void handleVoteRequest(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		logger.info("Got Vote Request from:"+msg.getHeader().getNodeId());
		logger.info("My term id:"+nodeState.getTermId());
		logger.info("Incoming Term Id:"+ msg.getElectionMessage().getTermId());
		if (msg.getElectionMessage().getTermId() > nodeState.getTermId()) {
			nodeState.setTermId(msg.getElectionMessage().getTermId());
			nodeState.setVotedFor(msg.getHeader().getNodeId());
			channel.writeAndFlush(util.createVoteResponse(nodeState,
				msg.getElectionMessage().getTermId(), msg.getHeader().getNodeId()));
			nodeState.setElectionNodeState(ElectionNodeStates.FOLLOWER);
		}
	}

	@Override
	public void handleVoteResponse(CommunicationMessage msg, Channel channel) {

		if (msg.getElectionMessage().getVotedFor() == nodeState
			.getRoutingConfig().getNodeId()) {

			votesObtained += 1;
			logger.info("Got Vote from " + msg.getHeader().getNodeId()
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
		// TODO Auto-generated method stub
		logger.info("Calculating cluster size...");
		setClusterSize(1);
		nodeState.getEdgeMonitor().getOutboundEdges().getEdgesMap().values()
			.stream().forEach(ei -> {
				if (ei.getChannel() != null) {
					if (ei.getChannel().isOpen()) {
						setClusterSize(getClusterSize() + 1);
					}
				}
			});
		logger.info("Cluster size:" + getClusterSize());
	}

	@Override
	public void afterStateChange() {
		// TODO Auto-generated method stub
		nodeState.setTermId(nodeState.getTermId() + 1);
		votesObtained = 1;
		calculateClusterSize();
		
		logger.info("Start Election by sending Vote Requests");

		voteRequestorTask = new VoteRequestorTask(this, nodeState);
		voteRequestorTask.run();

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

		calculateClusterSize();
		minimumVotesRequired = Math.round((getClusterSize() / 2) + 0.5f);

		if (votesObtained >= minimumVotesRequired) {
			logger.info(this.nodeState.getRoutingConfig().getNodeId() + " got "
				+ votesObtained + " votes. Becoming leader now...");
			nodeState.setElectionNodeState(ElectionNodeStates.LEADER);
		} else {
			logger.info(this.nodeState.getRoutingConfig().getNodeId() + " got "
				+ votesObtained + " votes; Min required were "
				+ minimumVotesRequired + ". Requesting votes again...");
			afterStateChange();
		}

	}

}
