package sjsu.cmpe.B295.raspberrypi.node;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;

public class CommunicationChannelInitializer
	extends ChannelInitializer<SocketChannel> {
	NodeState nodeState;

	public CommunicationChannelInitializer(NodeState nodeState) {
		this.nodeState = nodeState;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		/**
		 * length (4 bytes).
		 * 
		 * Note: max message size is 64 Mb = 67108864 bytes this defines a
		 * framer with a max of 64 Mb message, 4 bytes are the length, and strip
		 * 4 bytes
		 */
		
		//HttpServerCodec is a helper ChildHandler that encompasses
        //both HTTP request decoding and HTTP response encoding
        ch.pipeline().addLast(new HttpServerCodec());
        //HttpObjectAggregator helps collect chunked HttpRequest pieces into
        //a single FullHttpRequest. If you don't make use of streaming, this is
        //much simpler to work with.
        ch.pipeline().addLast(new HttpObjectAggregator(1048576));
        
        ch.pipeline().addLast(new HttpRequestHandler());
		
		pipeline.addLast("frameDecoder",
			new LengthFieldBasedFrameDecoder(67108864, 0, 4, 0, 4));

		// decoder must be first
		pipeline.addLast("protobufDecoder",
			new ProtobufDecoder(CommunicationMessage.getDefaultInstance()));
		pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
		pipeline.addLast("protobufEncoder", new ProtobufEncoder());

		// our server processor (new instance for each connection)
		pipeline.addLast("handler", new CommunicationChannelHandler(nodeState));
	}

}
