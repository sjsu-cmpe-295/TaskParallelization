package sjsu.cmpe.B295.election;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import sjsu.cmpe.B295.clusterMonitoring.Cluster;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.framework.Mapper;
import sjsu.cmpe.B295.httpRequestMessageHandlers.HttpRequestHandler;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.sensorDataCollection.IParallelizable;



public class Leader extends ElectionNodeState {
	protected static Logger logger = LoggerFactory.getLogger("Leader");
	public BlockingQueue<IParallelizable> taskQueue = new LinkedBlockingQueue<>();
	private Cluster cluster;

	public Cluster getCluster() {
		return cluster;
	}

	private Timer heartbeatTimeoutTimer;
	private HeartbeatSenderTask heartbeatSenderTask;
	private ElectionUtil util;
	Mapper mapper;

	public Leader(NodeState nodeState) {
		super(nodeState);
		util = new ElectionUtil();
		cluster = new Cluster();
		mapper = new Mapper(this, nodeState);

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

	// Http Request Handling

	@Override
	public void handleHttpRequestEvent(ChannelHandlerContext ctx,
		FullHttpRequest request) {

		DefaultFullHttpResponse response = new DefaultFullHttpResponse(
			HTTP_1_1, OK);

		response.headers().set(HttpHeaderNames.CONTENT_TYPE,
			"text/html; charset=UTF-8");
		response.content().writeBytes(request.content().copy());
		ctx.write(response);
		
		// TODO Auto-generated method stub
		// Input Analyzer --> Input Splitter
		// Input Splitter --> currentNodeState(Leader)
		// TaskMonitor
		// Mapper
		// Reducer
	}

	@Override
	public void handleGetHumidityRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleGetTemperatureRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleGetBothRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
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
