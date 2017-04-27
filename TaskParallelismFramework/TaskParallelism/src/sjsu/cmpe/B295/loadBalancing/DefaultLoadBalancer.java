package sjsu.cmpe.B295.loadBalancing;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLoadBalancer implements LoadBalancer {

	// constants
	// ------------------------------------------------------------------------------------------------------

	private static final Logger LOG = LoggerFactory
		.getLogger(DefaultLoadBalancer.class);
	private static final int TIMEOUT_IN_MILLIS = 3000;

	// configuration
	// --------------------------------------------------------------------------------------------------

	private final String id;
	private final HostAndPort balancerAddress;
	private BalancingStrategy balancingStrategy;
	private final Executor bossPool;
	private final Executor workerPool;
	private int timeoutInMillis;

	// internal vars
	// --------------------------------------------------------------------------------------------------

	private final boolean internalPools;
	private volatile boolean running;
	private Channel acceptor;
	private ChannelGroup allChannels;
	private ServerBootstrap bootstrap;

	// constructors
	// ---------------------------------------------------------------------------------------------------

	public DefaultLoadBalancer(String id, HostAndPort balancerAddress,
		BalancingStrategy balancingStrategy) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (balancerAddress == null) {
			throw new IllegalArgumentException(
				"Balancer local address cannot be null");
		}

		if (balancingStrategy == null) {
			throw new IllegalArgumentException(
				"Balancing strategy cannot be null");
		}

		this.id = id;
		this.balancerAddress = balancerAddress;
		this.balancingStrategy = balancingStrategy;

		this.internalPools = true;
		this.bossPool = Executors.newCachedThreadPool();
		this.workerPool = Executors.newCachedThreadPool();

		this.timeoutInMillis = TIMEOUT_IN_MILLIS;
	}

	public DefaultLoadBalancer(String id, HostAndPort balancerAddress,
		Executor bossPool, Executor workerPool) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}

		if (balancerAddress == null) {
			throw new IllegalArgumentException(
				"Balancer local address cannot be null");
		}

		// if (balancingStrategy == null) {
		// throw new IllegalArgumentException(
		// "Balancing strategy cannot be null");
		// }

		if (bossPool == null) {
			throw new IllegalArgumentException("BossPool cannot be null");
		}

		if (workerPool == null) {
			throw new IllegalArgumentException("WorkerPool cannot be null");
		}

		this.id = id;
		this.balancerAddress = balancerAddress;
		// this.balancingStrategy = balancingStrategy;

		this.internalPools = true;
		this.bossPool = Executors.newCachedThreadPool();
		this.workerPool = Executors.newCachedThreadPool();

		this.timeoutInMillis = TIMEOUT_IN_MILLIS;
	}

	public void setBalancingStrategy(BalancingStrategy balancingStrategy) {
		this.balancingStrategy = balancingStrategy;
	}

	// LoadBalancer
	// ---------------------------------------------------------------------------------------------------

	@Override
	public synchronized boolean init() {
		if (this.running) {
			return true;
		}

		LOG.info("Launching {} on {}...", this, this.balancerAddress);

		this.bootstrap = new ServerBootstrap(
			new NioServerSocketChannelFactory(this.bossPool, this.workerPool));
		final ClientSocketChannelFactory clientSocketChannelFactory = new NioClientSocketChannelFactory(
			this.bossPool, this.workerPool);
		this.bootstrap.setOption("child.tcpNoDelay", true);
		this.allChannels = new DefaultChannelGroup(
			this.id + "-all-channels-" + Integer.toHexString(this.hashCode()));

		this.bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				if (balancingStrategy.getTargetAddresses().size() == 0) {

					ChannelPipeline pipeline = Channels.pipeline();

					pipeline.addLast("decoder", new HttpRequestDecoder());
					// Uncomment the following line if you don't want to handle
					// HttpChunks.
					// pipeline.addLast("aggregator", new
					// HttpChunkAggregator(1048576));
					pipeline.addLast("encoder", new HttpResponseEncoder());
					// Remove the following line if you don't want automatic
					// content compression.
					pipeline.addLast("deflater", new HttpContentCompressor());
					pipeline.addLast("handler",
						new TcpTunnelInboundHandlerWithoutTargets(id,
							allChannels, clientSocketChannelFactory,
							balancingStrategy, timeoutInMillis));
					return pipeline;
					// return Channels
					// .pipeline(new TcpTunnelInboundHandlerWithoutTargets(id,
					// allChannels, clientSocketChannelFactory,
					// balancingStrategy, timeoutInMillis));
				}
				return Channels.pipeline(new TcpTunnelInboundHandler(id,
					allChannels, clientSocketChannelFactory, balancingStrategy,
					timeoutInMillis));
			}
		});

		// Start up the server.
		if (this.balancerAddress.isAnyHost()) {
			this.acceptor = this.bootstrap
				.bind(new InetSocketAddress(this.balancerAddress.getPort()));
		} else {
			this.acceptor = this.bootstrap
				.bind(new InetSocketAddress(this.balancerAddress.getHost(),
					this.balancerAddress.getPort()));
		}
		boolean bound = this.acceptor.isBound();

		if (!bound) {
			LOG.error("Failed to bound {} to {}.", this, this.balancerAddress);
		} else {
			LOG.info("Successfully bound {} to {}.", this,
				this.balancerAddress);
		}

		return (this.running = true);
	}

	@Override
	public synchronized void terminate() {
		if (!this.running) {
			return;
		}

		LOG.info("Shutting down {}...", this.id);
		this.running = false;

		this.allChannels.close().awaitUninterruptibly();
		this.acceptor.close().awaitUninterruptibly();
		// never close the thread pool, that's a responsibility for whoever
		// provided it
		if (this.internalPools) {
			this.bootstrap.releaseExternalResources();
		}
		LOG.info("{} stopped.", this.id);
	}

	@Override
	public HostAndPort getBalancerAddress() {
		return this.balancerAddress;
	}

	@Override
	public List<HostAndPort> getTargetAddresses() {
		return this.balancingStrategy.getTargetAddresses();
	}

	@Override
	public BalancingStrategy getBalancingStrategy() {
		return this.balancingStrategy;
	}

	// low level overrides
	// --------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + '@'
			+ Integer.toHexString(this.hashCode());
	}
}