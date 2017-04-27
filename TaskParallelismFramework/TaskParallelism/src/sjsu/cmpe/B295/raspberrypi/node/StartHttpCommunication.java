package sjsu.cmpe.B295.raspberrypi.node;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class StartHttpCommunication implements Runnable {
	protected static Logger logger = LoggerFactory
		.getLogger("StartHttpCommunication");
	NodeState nodeState;
	private volatile boolean isAlive = true;

	public void requestThreadStop() {
		logger.info("Stopping Http Communication...");
		isAlive = false;
	}

	public StartHttpCommunication(NodeState nodeState) {
		// TODO Auto-generated constructor stub
		this.nodeState = nodeState;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		EventLoopGroup serverWorkgroup = new NioEventLoopGroup();
		try {

			ServerBootstrap b = new ServerBootstrap();
			b.group(serverWorkgroup).channel(NioServerSocketChannel.class)
				// Setting InetSocketAddress to port 0 will assign one at
				// random
				.localAddress(new InetSocketAddress(
					this.nodeState.getRoutingConfig().getCommandPort()))
				.childHandler(new HttpCommunicationChannelInitializer(this.nodeState));
			Channel serverChannel = null;
			try {
				serverChannel = b.bind().sync().channel();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int PORT = ((InetSocketAddress) serverChannel.localAddress())
				.getPort();
			logger.info("Open your web browser and navigate to "
				+ this.nodeState.getRoutingConfig().getHost() + ":" + PORT
				+ '/');
			logger.info("Echo back any TEXT with POST HTTP requests");

			while (isAlive) {
//				logger.info("Http Communication:" + isAlive);
			}

		} finally {
			logger.info("Shutting down the server gracefully....");
			serverWorkgroup.shutdownGracefully();
		}
	}

}