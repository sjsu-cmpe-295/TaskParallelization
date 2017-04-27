package sjsu.cmpe.B295.loadBalancing;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpTunnelInboundHandler extends SimpleChannelHandler {

	// constants
	// ------------------------------------------------------------------------------------------------------

	private static final Logger LOG = LoggerFactory
		.getLogger(TcpTunnelInboundHandler.class);

	// configuration
	// --------------------------------------------------------------------------------------------------

	private final ClientSocketChannelFactory factory;
	private final ChannelGroup channelGroup;
	private BalancingStrategy strategy;
	private String remoteHost;
	private int remotePort;
	private final int connectTime;

	// internal vars
	// --------------------------------------------------------------------------------------------------

	private final String id;
	private volatile Channel outboundChannel;
	private final AtomicLong outboundCounter = new AtomicLong();
	private final AtomicLong inboundCounter = new AtomicLong();

	// constructors
	// ---------------------------------------------------------------------------------------------------

	public TcpTunnelInboundHandler(String id, ChannelGroup channelGroup,
		ClientSocketChannelFactory factory, BalancingStrategy strategy,
		int timeoutInMillis) {
		this.id = id;
		this.channelGroup = channelGroup;
		this.factory = factory;
		this.strategy = strategy;
		this.connectTime = timeoutInMillis;
	}

	// SimpleChannelUpstreamHandler
	// -----------------------------------------------------------------------------------

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception {
		this.channelGroup.add(e.getChannel());

		InetSocketAddress address = (InetSocketAddress) e.getChannel()
			.getRemoteAddress();
		HostAndPort target = this.strategy.selectTarget(address.getHostName(),
			address.getPort());
		this.remoteHost = target.getHost();
		this.remotePort = target.getPort();

		// Suspend incoming traffic until connected to the remote host.
		final Channel inboundChannel = e.getChannel();
		inboundChannel.setReadable(false);

		// Start the connection attempt.
		ClientBootstrap cb = new ClientBootstrap(factory);
		cb.setOption("tcpNoDelay", true);
		cb.setOption("connectTimeMillis", this.connectTime);
		// outbound handler gets no link to channel group because these two
		// connections act as one: if one goes down, it
		// takes the other end with it.
		cb.getPipeline().addLast("handler", new TcpTunnelOutboundHandler(
			this.id, this.remoteHost, this.remotePort, e.getChannel()));
		ChannelFuture f = cb
			.connect(new InetSocketAddress(this.remoteHost, this.remotePort));

		this.outboundChannel = f.getChannel();
		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future)
				throws Exception {
				if (future.isSuccess()) {
					// Connection attempt succeeded: begin to accept incoming
					// traffic.
					inboundChannel.setReadable(true);
					LOG.info(
						"Successfully created tunnel from {} to {} on LoadBalancer with id '{}'.",
						inboundChannel.getRemoteAddress(),
						future.getChannel().getRemoteAddress(), id);
				} else {
					// Close the connection if the connection attempt has
					// failed.
					LOG.info(
						"Failed to create tunnel from {} to {}:{} on LoadBalancer with id '{}'.",
						inboundChannel.getRemoteAddress(), remoteHost,
						remotePort, id);
					inboundChannel.close();
				}
			}
		});
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception {
		ChannelBuffer msg = (ChannelBuffer) e.getMessage();
		this.inboundCounter.addAndGet(msg.readableBytes());
		this.outboundChannel.write(msg);
	}

	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
		throws Exception {
		super.writeComplete(ctx, e);
		this.outboundCounter.addAndGet(e.getWrittenAmount());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception {
		if (this.outboundChannel != null) {
			TcpTunnelUtils
				.closeAfterFlushingPendingWrites(this.outboundChannel);
			LOG.info(
				"Tunnel from {} to {} (LoadBalancer with id '{}') closed (a->b: {}b, b->a: {}b).",
				e.getChannel().getRemoteAddress(),
				this.outboundChannel.getRemoteAddress(), this.id,
				this.inboundCounter.get(), this.outboundCounter.get());
		} else {
			LOG.info(
				"Tunnel from {} to {}:{} on LoadBalancer with id '{}' closed (a->b: {}b, b->a: {}b).",
				e.getChannel().getRemoteAddress(), this.remoteHost,
				this.remotePort, this.id, this.inboundCounter.get(),
				this.outboundCounter.get());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception {
		LOG.info(
			"Exception caught on tunnel from {} to {} (LoadBalancer with id '{}); closing channel.",
			e.getCause(), e.getChannel().getRemoteAddress(),
			this.outboundChannel.getRemoteAddress(), this.id);
		TcpTunnelUtils.closeAfterFlushingPendingWrites(e.getChannel());
	}

	// getters & setters
	// ----------------------------------------------------------------------------------------------

	public String getRemoteHost() {
		return remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}
}
