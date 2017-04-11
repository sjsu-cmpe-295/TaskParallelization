package sjsu.cmpe.B295.election;

import java.util.HashMap;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class Leader extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Leader");
	private Timer heartbeatTimeoutTimer;
	private HashMap<Integer, Channel> workers;
	private HeartbeatSenderTask heartbeatSenderTask;

	public Leader(NodeState nodeState) {
		super(nodeState);
		workers = new HashMap<>();
	}

	public void addWorker(Integer nodeId, Channel channel) {
		workers.put(nodeId, channel);
	}

	public Channel getWorker(Integer nodeId) {
		return workers.get(nodeId);
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
		
	}

	@Override
	public void handleVoteRequest(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleVoteResponse(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeStateChange() {
		// TODO Auto-generated method stub
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
	
	public void cleanup(){
		workers = new HashMap<>();
	}
}
