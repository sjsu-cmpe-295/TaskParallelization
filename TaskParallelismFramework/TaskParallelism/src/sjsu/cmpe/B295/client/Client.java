package sjsu.cmpe.B295.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ResourceLeakDetector;

public class Client {
	protected static Logger logger = LoggerFactory.getLogger("Client");

	public static void main(String[] args) throws Exception {

		String host = args[0];
		int port = Integer.parseInt(args[1]);
		logger.info("Open your web browser and navigate to " + "://127.0.0.1:"
			+ port + '/');
		logger.info("Echo back any TEXT with POST HTTP requests");
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {

			Bootstrap b = new Bootstrap(); // (1)
			b.group(workerGroup); // (2)
			b.option(ChannelOption.TCP_NODELAY, true);
			b.channel(NioSocketChannel.class); // (3)
			b.handler(new LoggingHandler(LogLevel.INFO));

			// b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					/**
					 * length (4 bytes).
					 * 
					 * Note: max message size is 64 Mb = 67108864 bytes this
					 * defines a framer with a max of 64 Mb message, 4 bytes are
					 * the length, and strip 4 bytes
					 */
					// ch.pipeline().addLast("frameDecoder",
					// new LengthFieldBasedFrameDecoder(67108864, 0, 4, 0, 4));
					// ch.pipeline().addLast("frameEncoder",
					// new LengthFieldPrepender(4));
					//
					// // decoder must be first
					// ch.pipeline().addLast("protobufDecoder",
					// new ProtobufDecoder(Message.getDefaultInstance()));
					// ch.pipeline().addLast("protobufIdentityDecoder",
					// new ProtobufDecoder(Identity.getDefaultInstance()));
					// ch.pipeline().addLast("protobufEncoder",
					// new ProtobufEncoder());

					// HttpClient codec is a helper ChildHandler that
					// encompasses
					// both HTTP response decoding and HTTP request encoding
					ch.pipeline().addLast(new HttpClientCodec());
					// HttpObjectAggregator helps collect chunked HttpRequest
					// pieces into
					// a single FullHttpRequest. If you don't make use of
					// streaming, this is
					// much simpler to work with.
					ch.pipeline().addLast(new HttpObjectAggregator(1048576));

					ch.pipeline().addLast(new ClientHttpRequestHandler());

				}
			});

			final ByteBuf content = Unpooled.copiedBuffer("Hello World!",
				CharsetUtil.UTF_8);

			// Start the client.
			// ChannelFuture f = b.connect(host, port).sync(); // (5)
			ChannelFuture f = b.connect(host, port)
				.addListener(new ChannelFutureListener() {

					@Override
					public void operationComplete(ChannelFuture future)
						throws Exception {
						// TODO Auto-generated method stub

						HttpRequest request = new DefaultFullHttpRequest(
							HttpVersion.HTTP_1_1, HttpMethod.POST, "/",
							content);
						// If we don't set a content length from the client,
						// HTTP RFC
						// dictates that the body must be be empty then and
						// Netty won't read it.
						request.headers().set("Content-Length",
							content.readableBytes());
						future.channel().writeAndFlush(request);

					}
				});

			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
			// System.out.println("sleeping");
			// Thread.sleep(100000);
			// System.out.println("awake");
		} finally {
			// System.out.println("Shutting client");
			workerGroup.shutdownGracefully();
		}
	}
}
