package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;

import org.codehaus.jackson.map.ObjectMapper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import sjsu.cmpe.B295.election.RandomMessageSenderTask;
import sjsu.cmpe.B295.raspberrypi.node.edges.EdgeMonitor;

public class Node {

	public static String configFilePath;
	public static ConcreteFileMonitor monitor;
	public static NodeState nodeState;
	protected static HashMap<Integer, ServerBootstrap> bootstrap = new HashMap<Integer, ServerBootstrap>();

	public Node(String configFilePath) {
		this.configFilePath = configFilePath;
		this.monitor = new FileMonitor(this.configFilePath);
		this.nodeState = new NodeState(monitor);
		this.monitor.monitorFile();

	}

	public void start() {
		StartCommunication comm = new StartCommunication(this.nodeState,
			this.monitor);
		Thread workThread = new Thread(comm);
		workThread.start();
	}

	private static class StartCommunication implements Runnable {
		NodeState nodeState;
		ConcreteFileMonitor monitor;
		private RandomMessageSenderTask randomMessageSenderTask;

		/**
		 * @param state
		 */
		public StartCommunication(NodeState nodeState,
			ConcreteFileMonitor monitor) {
			if (nodeState == null)
				throw new RuntimeException("missing state");
			this.nodeState = nodeState;
			this.monitor = monitor;

			EdgeMonitor edgeMonitor = new EdgeMonitor(this.nodeState,
				this.monitor);
			Thread t = new Thread(edgeMonitor);
			t.start();
			
			this.nodeState.currentElectionNodeState.bePartOfCluster();
			
//			randomMessageSenderTask = new RandomMessageSenderTask(this.nodeState);
//
//			// Schedule this task only after Delay Time is set..
//			Timer timer = new Timer();
//			timer.scheduleAtFixedRate(randomMessageSenderTask, 0, 6000);			

		}

		@Override
		public void run() {
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			EventLoopGroup workerGroup = new NioEventLoopGroup();

			try {
				ServerBootstrap b = new ServerBootstrap();
				bootstrap.put(nodeState.getRoutingConfig().getWorkPort(), b);

				b.group(bossGroup, workerGroup);
				b.channel(NioServerSocketChannel.class);
				b.option(ChannelOption.SO_BACKLOG, 100);
				b.option(ChannelOption.TCP_NODELAY, true);
				b.option(ChannelOption.SO_KEEPALIVE, true);

				// b.option(ChannelOption.MESSAGE_SIZE_ESTIMATOR);

				b.childHandler(new CommunicationChannelInitializer(nodeState));

				// Start the server.
//				logger.info("Starting work server ("
//					+ state.getConf().getNodeId() + "), listening on port = "
//					+ state.getConf().getWorkPort());
				ChannelFuture f = b.bind(nodeState.getRoutingConfig().getWorkPort())
					.syncUninterruptibly();

//				logger.info(f.channel().localAddress() + " -> open: "
//					+ f.channel().isOpen() + ", write: "
//					+ f.channel().isWritable() + ", act: "
//					+ f.channel().isActive());

				// block until the server socket is closed.
				f.channel().closeFuture().sync();
			} catch (Exception ex) {
				// on bind().sync()
//				logger.error("Failed to setup handler.", ex);
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

	public static class JsonUtil {
		private static JsonUtil instance;

		public static void init(File cfg) {

		}

		public static JsonUtil getInstance() {
			if (instance == null)
				throw new RuntimeException("Server has not been initialized");

			return instance;
		}

		public static String encode(Object data) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(data);
			} catch (Exception ex) {
				return null;
			}
		}

		public static <T> T decode(String data, Class<T> theClass) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(data.getBytes(), theClass);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

}
