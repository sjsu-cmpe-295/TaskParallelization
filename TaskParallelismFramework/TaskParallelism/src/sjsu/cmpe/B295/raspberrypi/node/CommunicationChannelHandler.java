package sjsu.cmpe.B295.raspberrypi.node;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.communicationMessageHandlers.ElectionMessageHandler;
import sjsu.cmpe.B295.communicationMessageHandlers.EdgeBeatMessageHandler;
import sjsu.cmpe.B295.communicationMessageHandlers.ICommunicationMessageHandler;

public class CommunicationChannelHandler
	extends SimpleChannelInboundHandler<CommunicationMessage> {
	private static Logger logger = LoggerFactory
		.getLogger("CommunicationChannelHandler");
	NodeState nodeState;
	private ICommunicationMessageHandler communicationMessageHandler;
	private MessageRouter messageRouter;

	public CommunicationChannelHandler(NodeState nodeState) {
		if (nodeState != null) {
			this.nodeState = nodeState;
		}

		this.messageRouter = new MessageRouter(nodeState);
		initializeMessageHandlers();
	}

	private void initializeMessageHandlers() {

		// Define Handlers
		ICommunicationMessageHandler edgeBeatMessageHandler = new EdgeBeatMessageHandler(
			nodeState);
		ICommunicationMessageHandler electionMessageHandler = new ElectionMessageHandler(
			nodeState);

		// Chain all the handlers
		edgeBeatMessageHandler
			.setNextCommunicationMessageHandler(electionMessageHandler);
		// Define the start of Chain
		communicationMessageHandler = edgeBeatMessageHandler;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
		CommunicationMessage msg) throws java.lang.Exception {
		handleMessage(msg, ctx.channel());
	}

	public void handleMessage(CommunicationMessage msg, Channel channel) {
		if (msg == null) {
			logger.info("ERROR: Null message is received");
			return;
		}

		msg = messageRouter.route(msg);

		if (msg == null) {
			logger.debug("No need to handle message.. ");
			return;
		}

		try {
			communicationMessageHandler.handleCommunicationMessage(msg,
				channel);

			/*
			 * Create in-bound edge's if it is not created/if it was removed
			 * when connection was down.
			 */
			InetSocketAddress socketAddress = (InetSocketAddress) channel
				.remoteAddress();
			nodeState.getEdgeMonitor().createInboundIfNew(
				msg.getHeader().getNodeId(), socketAddress.getHostName(),
				socketAddress.getPort(), channel);
		} catch (Exception e) {
			logger.error("Got an exception in work");
			e.printStackTrace();
			// FailureMessage failureMessage = new FailureMessage (msg, e);
			// failureMessage.setNodeId (state.getConf ().getNodeId ());
			// channel.write(failureMessage.getWorkMessage ());
		}
		System.out.flush();
	}
}
