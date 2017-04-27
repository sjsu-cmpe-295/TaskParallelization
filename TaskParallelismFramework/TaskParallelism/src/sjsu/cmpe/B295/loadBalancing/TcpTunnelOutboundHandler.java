package sjsu.cmpe.B295.loadBalancing;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpTunnelOutboundHandler extends SimpleChannelHandler {

	// constants
	// ------------------------------------------------------------------------------------------------------

	private static final Logger LOG = LoggerFactory
		.getLogger(TcpTunnelOutboundHandler.class);

	// internal vars
	// --------------------------------------------------------------------------------------------------

	private final String id;
	private final Channel inboundChannel;
	private final String host;
	private final int port;

	// constructors
	// ---------------------------------------------------------------------------------------------------

	public TcpTunnelOutboundHandler(String id, String host, int port,
		Channel inboundChannel) {
		this.id = id;
		this.inboundChannel = inboundChannel;
		this.host = host;
		this.port = port;
	}

	// SimpleChannelUpstreamHandler
	// -----------------------------------------------------------------------------------

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception {
		ChannelBuffer msg = (ChannelBuffer) e.getMessage();
		this.inboundChannel.write(msg);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception {
		super.channelConnected(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception {
		String remoteAddress;
		if (e.getChannel().getRemoteAddress() == null) {
			// same format as a InetSocketAddress.toString()
			remoteAddress = "/" + this.host + ":" + this.port;
		} else {
			remoteAddress = e.getChannel().getRemoteAddress().toString();
		}
		LOG.info(
			"Outbound tunnel from {} to {} (TcpTunnel with id '{}') closed; closing matching inbound channel.",
			this.inboundChannel.getRemoteAddress(), remoteAddress, this.id);
		TcpTunnelUtils.closeAfterFlushingPendingWrites(this.inboundChannel);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception {
		if (e.getChannel().isConnected()) {
			LOG.info(
				"Exception caught on tunnel from {} to {} (TcpTunnel with id '{}'); closing channel.",
				e.getCause(), this.inboundChannel.getRemoteAddress(),
				e.getChannel().getRemoteAddress(), this.id);
		} else {
			LOG.info(
				"Exception caught on tunnel connecting to {}:{} (TcpTunnel with id '{}).",
				e.getCause(), this.host, this.port, this.id);
		}
		TcpTunnelUtils.closeAfterFlushingPendingWrites(e.getChannel());
	}
}
