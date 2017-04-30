package sjsu.cmpe.B295.election;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.clusterMonitoring.Cluster;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.sensorDataCollection.IParallelizable;

public class Leader extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Leader");
	private BlockingQueue<IParallelizable> taskQueue = new LinkedBlockingQueue<>();
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
		UpdateGraph(null);

	}

	public long getHeartBeatTimeout() {
		return nodeState.getRoutingConfig().getHeartbeatDt();
	}
	
	public void cleanup() {

	}
	
	public void UpdateGraph(ArrayList<Integer> data) {
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(10);
		values.add(20);
		values.add(29);
		values.add(23);
		values.add(12);
		values.add(24);
		logger.info("In Update Graph");
		String masterResponse = "";
		String humiResponse = "";
		String resp = "";
		StringBuffer response=new StringBuffer();
		final String RESPONSE_START="";
		final String RESPONSE_END="";
		
		masterResponse="{\"tempValues\": \""+values+"\"}";
		logger.info(masterResponse);
		URL url;
		URL url1;
		try {
			url = new URL("http://localhost:1300/updateTemperatureGraph");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			response.append(RESPONSE_START);
			response.append(masterResponse);
			response.append(RESPONSE_END);
			out.write(response.toString());
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while (in.readLine() != null) {}
			in.close();
			logger.info("updateMetrics Invoked Successfully..");
			response.setLength(0);
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
}
