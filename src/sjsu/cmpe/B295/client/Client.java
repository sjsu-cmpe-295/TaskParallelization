package sjsu.cmpe.B295.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import sjsu.cmpe.B295.common.IdentityProto.Identity;
import sjsu.cmpe.B295.common.MessageProto.Message;
import sjsu.cmpe.B295.server.ServerIdentityHandler;

public class Client {
	public static void main(String[] args) throws Exception {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
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
					ch.pipeline().addLast("frameDecoder",
						new LengthFieldBasedFrameDecoder(67108864, 0, 4, 0, 4));
					ch.pipeline().addLast("frameEncoder",
						new LengthFieldPrepender(4));

					// decoder must be first
					ch.pipeline().addLast("protobufDecoder",
						new ProtobufDecoder(Message.getDefaultInstance()));
					ch.pipeline().addLast("protobufIdentityDecoder",
						new ProtobufDecoder(Identity.getDefaultInstance()));
					ch.pipeline().addLast("protobufEncoder",
						new ProtobufEncoder());
					
					ch.pipeline().addLast(new ClientHandler());

				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync(); // (5)

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
