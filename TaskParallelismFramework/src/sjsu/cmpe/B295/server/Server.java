package sjsu.cmpe.B295.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import sjsu.cmpe.B295.common.IdentityProto.Identity;
import sjsu.cmpe.B295.common.MessageProto.Message;

public class Server {
	private int port;

	public Server(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new Server(port).run();
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class) // (3)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						/**
						 * length (4 bytes).
						 * 
						 * Note: max message size is 64 Mb = 67108864 bytes this
						 * defines a framer with a max of 64 Mb message, 4 bytes
						 * are the length, and strip 4 bytes
						 */
						ch.pipeline().addLast("frameDecoder",
							new LengthFieldBasedFrameDecoder(67108864, 0, 4, 0,
								4));
						ch.pipeline().addLast("frameEncoder",
							new LengthFieldPrepender(4));

						// decoder must be first

						ch.pipeline().addLast("protobufIdentityDecoder",
							new ProtobufDecoder(Identity.getDefaultInstance()));
						ch.pipeline().addLast("protobufDecoder",
						new ProtobufDecoder(Message.getDefaultInstance()));
						ch.pipeline().addLast("protobufEncoder",
							new ProtobufEncoder());

						ch.pipeline().addLast(new ServerIdentityHandler());
//						ch.pipeline().addLast(new ServerHandler());

						
					}
				}).option(ChannelOption.SO_BACKLOG, 128); // (5)
			// .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); // (7)
			

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
