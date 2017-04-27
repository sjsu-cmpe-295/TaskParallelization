package sjsu.cmpe.B295.raspberrypi.node;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpCommunicationChannelInitializer
	extends ChannelInitializer<SocketChannel> {
	private NodeState nodeState;

	public HttpCommunicationChannelInitializer(NodeState nodeState) {
		this.nodeState = nodeState;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// HttpServerCodec is a helper ChildHandler that encompasses
		// both HTTP request decoding and HTTP response encoding
		ch.pipeline().addLast(new HttpServerCodec());
		// HttpObjectAggregator helps collect chunked HttpRequest
		// pieces into
		// a single FullHttpRequest. If you don't make use of
		// streaming, this is
		// much simpler to work with.
		ch.pipeline().addLast(new HttpObjectAggregator(1048576));
		// Finally add your FullHttpRequest handler. Real examples
		// might replace this
		// with a request router
		ch.pipeline()
			.addLast(new HttpCommunicationChannelHandler(this.nodeState));
	}

}
