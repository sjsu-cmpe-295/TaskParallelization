package sjsu.cmpe.B295.raspberrypi.node;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.election.Candidate;
import sjsu.cmpe.B295.election.ElectionNodeState;
import sjsu.cmpe.B295.election.ElectionNodeStates;
import sjsu.cmpe.B295.election.Follower;
import sjsu.cmpe.B295.election.Leader;
import sjsu.cmpe.B295.raspberrypi.node.Node.JsonUtil;
import sjsu.cmpe.B295.raspberrypi.node.edges.EdgeMonitor;

public class NodeState implements IFileObserver {
	protected static Logger logger = LoggerFactory.getLogger("NodeState");
	protected static RoutingConfig routingConfig;
	protected ConcreteFileMonitor subject;
	private EdgeMonitor edgeMonitor;

	// Election Node State
	ElectionNodeState follower;
	ElectionNodeState candidate;
	ElectionNodeState leader;
	ElectionNodeState currentElectionNodeState;

	private AtomicInteger leaderId;
	private AtomicInteger votedFor;
	private AtomicInteger termId; // termId

	public int getLeaderId() {
		return leaderId.get();
	}

	public int getVotedFor() {
		return votedFor.get();
	}

	public int getTermId() {
		return termId.get();
	}

	public void setLeaderId(int leaderId) {
		this.leaderId.getAndSet(leaderId);
	}

	public void setVotedFor(int votedFor) {
		this.votedFor.getAndSet(votedFor);
	}

	public void setTermId(int termId) {
		this.termId.getAndSet(termId);
	}

	public NodeState(ConcreteFileMonitor subject) {
		this.subject = subject;
		this.subject.addObserver(this);

		this.follower = new Follower(this);
		this.candidate = new Candidate(this);
		this.leader = new Leader(this);
		this.currentElectionNodeState = follower;

		this.leaderId = new AtomicInteger(-1);
		// To ensure that I will wait for heart beat timeout
		this.termId = new AtomicInteger(0);
		this.votedFor = new AtomicInteger(-1);
	}

	public ElectionNodeState getCurrentElectionNodeState() {
		return currentElectionNodeState;
	}

	public void setElectionNodeState(ElectionNodeStates nextNodeState) {
		synchronized (this) {

			getCurrentElectionNodeState().beforeStateChange();
			logger.info("############# Node "+getRoutingConfig().getNodeId() + " is  "
				+ nextNodeState.toString()+" now. #############");

			switch (nextNodeState) {
			case FOLLOWER:
				currentElectionNodeState = follower;
				break;
			case CANDIDATE:
				currentElectionNodeState = candidate;
				break;
			case LEADER:
				currentElectionNodeState = leader;
				break;
			default:
				break;
			}
			getCurrentElectionNodeState().afterStateChange();
		}

	}

	public void handleElectionMessage(CommunicationMessage msg,
		Channel channel) {
		currentElectionNodeState.handleElectionEvent(msg, channel);
	}

	private boolean verifyConf(RoutingConfig conf) {
		return (conf != null);
	}

	/**
	 * @return the edgeMonitor
	 */
	public EdgeMonitor getEdgeMonitor() {
		return edgeMonitor;
	}

	/**
	 * @param edgeMonitor
	 *            the edgeMonitor to set
	 */
	public void setEdgeMonitor(EdgeMonitor edgeMonitor) {
		this.edgeMonitor = edgeMonitor;
	}

	/**
	 * @return the routingConfig
	 */
	public static RoutingConfig getRoutingConfig() {
		return routingConfig;
	}

	/**
	 * @param routingConfig
	 *            the routingConfig to set
	 */
	public static void setRoutingConfig(RoutingConfig routingConfig) {
		NodeState.routingConfig = routingConfig;
	}

	@Override
	public void update() {

		logger.debug("Updating RoutingConfig");
		RoutingConfig conf = null;
		File configFile = this.subject.getConfigFile();
		if (!configFile.exists())
			throw new RuntimeException(
				configFile.getAbsolutePath() + " not found");
		// resource initialization - how message are processed
		BufferedInputStream br = null;
		try {
			byte[] raw = new byte[(int) configFile.length()];
			br = new BufferedInputStream(new FileInputStream(configFile));
			br.read(raw);
			conf = JsonUtil.decode(new String(raw), RoutingConfig.class);
			// System.out.println(conf.getNodeId());
			if (!verifyConf(conf))
				throw new RuntimeException(
					"verification of configuration failed");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		setRoutingConfig(conf);
	}
}
