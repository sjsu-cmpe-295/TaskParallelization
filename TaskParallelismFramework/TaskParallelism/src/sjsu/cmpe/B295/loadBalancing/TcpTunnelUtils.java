package sjsu.cmpe.B295.loadBalancing;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

public class TcpTunnelUtils {

	/**
	 * Closes the specified channel after all queued write requests are flushed.
	 *
	 * @param ch
	 *            Channel to close
	 */
	public static void closeAfterFlushingPendingWrites(Channel ch) {
		if (ch.isConnected()) {
			ch.write(ChannelBuffers.EMPTY_BUFFER)
				.addListener(ChannelFutureListener.CLOSE);
		}
	}
}
