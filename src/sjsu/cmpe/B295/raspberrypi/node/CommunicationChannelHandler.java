package sjsu.cmpe.B295.raspberrypi.node;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import sjsu.cmpe.B295.common.IdentityProto.Identity;

public class CommunicationChannelHandler extends SimpleChannelInboundHandler<Identity>{
	NodeState nodeState;
	public CommunicationChannelHandler(NodeState nodeState) {
		this.nodeState = nodeState;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Identity msg)
		throws Exception {
		// TODO Auto-generated method stub
		
	}

}
