package sjsu.cmpe.B295.election;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.clusterMonitoring.Cluster;
import sjsu.cmpe.B295.clusterMonitoring.PiNode;
import sjsu.cmpe.B295.clusterMonitoring.PiNodeState;
import sjsu.cmpe.B295.clusterMonitoring.PiNodeType;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.loadBalancing.DefaultLoadBalancer;
import sjsu.cmpe.B295.loadBalancing.HostAndPort;
import sjsu.cmpe.B295.loadBalancing.RoundRobinBalancingStrategy;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.sensorDataCollection.IParallelizable;

public class Leader extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Leader");
	private BlockingQueue<IParallelizable> taskQueue = new LinkedBlockingQueue<>();
	private Cluster cluster;

	private DefaultLoadBalancer loadBalancer;

	private static int previousDestinationsCount;

	public Cluster getCluster() {
		return cluster;
	}

	public void addToCluster(PiNode node) {
		getCluster().addPiNode(node);
		if ((node.getPiNodeState().equals(PiNodeState.ACTIVE)
			&& node.getPiNodeType().equals(PiNodeType.WORKER))
			|| previousDestinationsCount > 0) {
			restartLoadBalancer();
		}
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

		restartLoadBalancer();
	}

	private void restartLoadBalancer() {
		logger.info("Re-Starting Loading Balancer");
		List<HostAndPort> destinations = new ArrayList<HostAndPort>();

		for (Integer nodeId : getCluster().getPiNodes().keySet()) {
			PiNode piNode = getCluster().getPiNodes().get(nodeId);
			if (piNode.getPiNodeType().equals(PiNodeType.WORKER)
				&& piNode.getPiNodeState().equals(PiNodeState.ACTIVE)) {
				destinations.add(new HostAndPort(piNode.getIpAddress(),
					piNode.getCommandPort()));
			}
		}

		previousDestinationsCount = destinations.size();
		logger.info("targets count = " + destinations.size());

		RoundRobinBalancingStrategy strategy = new RoundRobinBalancingStrategy(
			nodeState, destinations);
		if (loadBalancer != null) {
			loadBalancer.terminate();
		}
		loadBalancer = new DefaultLoadBalancer("defaultLoadBalancer",
			new HostAndPort(this.nodeState.getRoutingConfig().getHost(),
				this.nodeState.getRoutingConfig().getCommandPort()),
			strategy);

		if (!loadBalancer.init()) {
			System.err.println("Failed to launch LoadBalancer with options: ");
			return;
		}

		Thread shutdownHook = new Thread() {

			@Override
			public void run() {
				loadBalancer.terminate();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	public long getHeartBeatTimeout() {
		return nodeState.getRoutingConfig().getHeartbeatDt();
	}

	public void cleanup() {
		loadBalancer.terminate();
	}
}
