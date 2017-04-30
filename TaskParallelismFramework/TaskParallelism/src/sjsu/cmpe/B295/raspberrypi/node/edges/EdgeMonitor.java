package sjsu.cmpe.B295.raspberrypi.node.edges;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import sjsu.cmpe.B295.election.EdgeBeatMsg;
import sjsu.cmpe.B295.raspberrypi.node.CommunicationChannelInitializer;
import sjsu.cmpe.B295.raspberrypi.node.ConcreteFileMonitor;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.raspberrypi.node.IFileObserver;
import sjsu.cmpe.B295.raspberrypi.node.RoutingConfig;
import sjsu.cmpe.B295.raspberrypi.node.RoutingConfig.RoutingEntry;

public class EdgeMonitor implements Runnable, IFileObserver {
	protected static Logger logger = LoggerFactory.getLogger("EdgeMonitor");

	private EdgeList outboundEdges;
	private EdgeList inboundEdges;
	private long dt = 2000;
	private NodeState nodeState;
	protected ConcreteFileMonitor subject;
	private boolean forever = true;
	private EventLoopGroup group;
	private EdgeHealthMonitorTask edgeHealthMonitorTask;

	public EdgeMonitor(NodeState state, ConcreteFileMonitor subject) {
		if (state == null)
			throw new RuntimeException("state is null");

		this.outboundEdges = new EdgeList();
		this.inboundEdges = new EdgeList();
		this.nodeState = state;
		this.nodeState.setEdgeMonitor(this);

		this.subject = subject;
		this.subject.addObserver(this);

		this.group = new NioEventLoopGroup();

		if (state.getRoutingConfig().getRoutingEntries() != null) {
			for (RoutingEntry e : state.getRoutingConfig()
				.getRoutingEntries()) {
				outboundEdges.addNode(e.getId(), e.getHost(), e.getPort());
			}
		}

		// cannot go below 2 sec
		// if 3000>2000; this.dt=3000
		// if (state.getRoutingConfig().getHeartbeatDt() > this.dt)
		this.dt = state.getRoutingConfig().getHeartbeatDt();

		edgeHealthMonitorTask = new EdgeHealthMonitorTask(this);

		// Schedule this task only after Delay Time is set..
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(edgeHealthMonitorTask, 0, getDelayTime());
	}

	private long getDelayTime() {
		return this.dt;
	}

	public void createInboundIfNew(int ref, String host, int port,
		Channel channel) {
		EdgeInfo ei = inboundEdges.createIfNew(ref, host, port);
		ei.setChannel(channel);
		ei.setActive(true);
	}

	public void shutdown() {
		forever = false;
	}

	@Override
	public void run() {
		while (forever) {
			try {
				for (EdgeInfo ei : outboundEdges.getEdgesMap().values()) {
					if (ei.isActive() && ei.getChannel() != null) {
						logger.debug(ei.isActive() + "-" + ei.getChannel() + "-"
							+ ei.getRef());

						logger.debug(nodeState.getRoutingConfig().getNodeId()
							+ " sending edgeBeat to " + ei.getRef());
						EdgeBeatMsg beatMessage = new EdgeBeatMsg(
							"Message from "
								+ nodeState.getRoutingConfig().getNodeId()
								+ " to " + ei.getRef(),
							nodeState.getRoutingConfig().getNodeId(),
							ei.getRef());

						ei.getChannel().writeAndFlush(
							beatMessage.getCommunicationMessage());
					} else {
						logger.debug(ei.isActive() + "-" + ei.getChannel() + "-"
							+ ei.getRef());
						try {
							CommunicationChannelInitializer wi = new CommunicationChannelInitializer(
								nodeState);
							Bootstrap b = new Bootstrap();
							b.group(group).channel(NioSocketChannel.class)
								.handler(wi);
							b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
								10000);
							b.option(ChannelOption.TCP_NODELAY, true);
							b.option(ChannelOption.SO_KEEPALIVE, true);

							// Make the connection attempt.
							ChannelFuture channel = b
								.connect(ei.getHost(), ei.getPort())
								.syncUninterruptibly();

							// want to monitor the connection to the server s.t.
							// if we loose the
							// connection, we can try to re-establish it.
							// channel.channel().closeFuture();

							ei.setChannel(channel.channel());
							ei.setActive(channel.channel().isActive());
							ei.setLastHeartbeat(System.currentTimeMillis());
							logger.debug(channel.channel().localAddress()
								+ " -> open: " + channel.channel().isOpen()
								+ ", write: " + channel.channel().isWritable()
								+ ", reg: " + channel.channel().isRegistered());

						} catch (Throwable ex) {
							logger.debug(
								"failed to initialize the client connection");
							// ex.printStackTrace();
						}
						logger
							.debug("trying to connect to node " + ei.getRef());
					}
				}

				Thread.sleep(dt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// @Override
	// public synchronized void onAdd(EdgeInfo ei) {
	// // TODO check connection
	// }
	//
	// @Override
	// public synchronized void onRemove(EdgeInfo ei) {
	// // TODO ?
	// }

	// public void broadcastMessage(WorkMessage msg) {
	// if(msg.hasLeader () && msg.getLeader ().getAction () ==
	// Election.LeaderStatus.LeaderQuery.THELEADERIS) {
	// int termIdBroadCasting = msg.getLeader ().getElectionId ();
	// int leaderIdBroadCasting = msg.getLeader ().getLeaderId ();
	//
	// logger.debug("Edge Monitor - Term: " + termIdBroadCasting + ", " +
	// Thread.currentThread ().getName ());
	// logger.debug("Edge Monitor - Leader Id: " + leaderIdBroadCasting + ", " +
	// Thread.currentThread ().getName ());
	// }
	//
	// broadCastOutBound (msg);
	//
	// broadCastInBound (msg);
	// }

	// public void broadCastInBound(WorkMessage msg) {
	// for(EdgeInfo edge : inboundEdges.getEdgesMap().values()) {
	// if (edge.isActive() && edge.getChannel() != null) {
	// edge.getChannel().writeAndFlush(msg);
	// }
	// }
	// }
	//
	// public void broadCastOutBound(WorkMessage msg) {
	// for (EdgeInfo edge : outboundEdges.getEdgesMap ().values()) {
	// if (edge.isActive() && edge.getChannel() != null) {
	// edge.getChannel().writeAndFlush(msg);
	// }
	// }
	// }

	// @Override
	// protected void finalize() throws Throwable {
	// try{
	// outboundEdges.clear ();
	// inboundEdges.clear ();
	// group.shutdownGracefully ();
	// }finally {
	// super.finalize ();
	// }
	// }

	public EdgeList getInboundEdges() {
		return inboundEdges;
	}

	public EdgeList getOutboundEdges() {
		return outboundEdges;
	}

	@Override
	public void update() {
		RoutingConfig configuration = this.nodeState.getRoutingConfig();
		outboundEdges = new EdgeList();
		for (RoutingEntry e : configuration.getRoutingEntries()) {
			outboundEdges.addNode(e.getId(), e.getHost(), e.getPort());
		}

	}

	// public long getDelayTime() {
	// return dt;
	// }

	// public Logger getLogger() {
	// return logger;
	// }

	// @Override
	// public void onFileChanged(RoutingConf configuration) {
	// logger.debug("in edge monitor ");
	// outboundEdges = new EdgeList();
	// for (RoutingEntry e : configuration.getRouting()) {
	// outboundEdges.addNode(e.getId(), e.getHost(), e.getPort());
	// }
	//
	// }

}
