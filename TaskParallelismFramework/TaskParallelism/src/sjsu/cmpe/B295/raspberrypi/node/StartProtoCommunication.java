package sjsu.cmpe.B295.raspberrypi.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import sjsu.cmpe.B295.election.ElectionNodeStates;
import sjsu.cmpe.B295.raspberrypi.node.edges.EdgeMonitor;

public class StartProtoCommunication implements Runnable {

	protected static Logger logger = LoggerFactory
		.getLogger("StartHttpCommunication");

	NodeState nodeState;
	ConcreteFileMonitor monitor;

	/**
	 * @param state
	 */
	public StartProtoCommunication(NodeState nodeState,
		ConcreteFileMonitor monitor) {
		if (nodeState == null)
			throw new RuntimeException("missing state");
		this.nodeState = nodeState;
		this.monitor = monitor;

		EdgeMonitor edgeMonitor = new EdgeMonitor(this.nodeState, this.monitor);
		Thread t = new Thread(edgeMonitor);
		t.start();

		this.nodeState.setElectionNodeState(ElectionNodeStates.FOLLOWER);
		// completed leader election
		// start Task parallelism-input analyzing, bla, bla
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			Node.bootstrap.put(nodeState.getRoutingConfig().getWorkPort(), b);

			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.option(ChannelOption.SO_BACKLOG, 100);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.SO_KEEPALIVE, true);

			// b.option(ChannelOption.MESSAGE_SIZE_ESTIMATOR);

			b.childHandler(new CommunicationChannelInitializer(nodeState));

			// Start the server.
			ChannelFuture f = b.bind(nodeState.getRoutingConfig().getWorkPort())
				.syncUninterruptibly();

			// block until the server socket is closed.
			f.channel().closeFuture().sync();
		} catch (Exception ex) {
			// on bind().sync()
			// logger.error("Failed to setup handler.", ex);
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();

			// shutdown monitor
			EdgeMonitor emon = nodeState.getEdgeMonitor();
			if (emon != null)
				emon.shutdown();
		}

	}

}
