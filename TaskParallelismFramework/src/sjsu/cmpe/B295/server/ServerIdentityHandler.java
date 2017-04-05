package sjsu.cmpe.B295.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import sjsu.cmpe.B295.common.IdentityProto.Identity;

public class ServerIdentityHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("In Channel active Identity");
	};

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("In Channel Read of Server Identity Handler");
		Identity identityMsg = (Identity) msg;
		System.out.println("****Received Message by Server****");
		System.out.println(identityMsg.toString());
		System.out.println("****Received Message Ends Here****");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		System.out.println("In Channel Read Complete for Server Identity");
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

}
