package sjsu.cmpe.B295.election;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.clusterMonitoring.Cluster;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class Leader extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Leader");
	private Cluster cluster;

	public Cluster getCluster() {
		return cluster;
	}

	private Timer heartbeatTimeoutTimer;
	private HeartbeatSenderTask heartbeatSenderTask;
	private ElectionUtil util;

	public Leader(NodeState nodeState) {
		super(nodeState);
		util = new ElectionUtil();
		cluster = new Cluster();
	}

	@Override
	public void handleHeartBeat(CommunicationMessage msg, Channel channel) {
		logger.info("Got heartbeat in Leader state from leader:"
			+ msg.getElectionMessage().getLeaderId());
		logger.debug("My term id:" + nodeState.getTermId());
		logger
			.debug("Incoming Term Id:" + msg.getElectionMessage().getTermId());
		if (msg.getElectionMessage().getTermId() > nodeState.getTermId()) {
			nodeState.setLeaderId(msg.getElectionMessage().getLeaderId());
			nodeState.setVotedFor(msg.getElectionMessage().getLeaderId());
			nodeState.setTermId(msg.getElectionMessage().getTermId());
			nodeState.setElectionNodeState(ElectionNodeStates.FOLLOWER);
		} else if (msg.getElectionMessage().getTermId() == nodeState
			.getTermId()) {
			nodeState.setElectionNodeState(ElectionNodeStates.CANDIDATE);
		} else {
			logger.info("Ignoring Heartbeat from:"
				+ msg.getElectionMessage().getLeaderId());
		}
	}

	@Override
	public void handleVoteRequest(CommunicationMessage msg, Channel channel) {
		logger.info("Got Vote Request from:" + msg.getHeader().getNodeId());
		logger.debug("My term id:" + nodeState.getTermId());
		logger
			.debug("Incoming Term Id:" + msg.getElectionMessage().getTermId());
		if (msg.getElectionMessage().getTermId() > nodeState.getTermId()) {
			logger.info(
				"Acknowledging vote request" + msg.getHeader().getNodeId());
			nodeState.setTermId(msg.getElectionMessage().getTermId());
			nodeState.setVotedFor(msg.getHeader().getNodeId());
			channel.writeAndFlush(util.createVoteResponse(nodeState,
				msg.getElectionMessage().getTermId(),
				msg.getHeader().getNodeId()));
			nodeState.setElectionNodeState(ElectionNodeStates.FOLLOWER);
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
		// stop sending heartbeats
		heartbeatTimeoutTimer.cancel();
		// cleanup tasks
		cleanup();
	}

	@Override
	public void afterStateChange() {
		// Send Heartbeat messages to outbound active edges
		nodeState.setLeaderId(nodeState.getRoutingConfig().getNodeId());
		heartbeatSenderTask = new HeartbeatSenderTask(this, nodeState);
		heartbeatTimeoutTimer = new Timer();
		heartbeatTimeoutTimer.scheduleAtFixedRate(heartbeatSenderTask, 0,
			getHeartBeatTimeout());

	}

	public long getHeartBeatTimeout() {
		return nodeState.getRoutingConfig().getHeartbeatDt();
	}

	public void cleanup() {

	}
}
