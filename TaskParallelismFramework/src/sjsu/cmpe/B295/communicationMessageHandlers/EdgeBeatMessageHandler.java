package sjsu.cmpe.B295.communicationMessageHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class EdgeBeatMessageHandler implements ICommunicationMessageHandler {
	private static Logger logger = LoggerFactory
		.getLogger("EdgeBeatMessageHandler");
	private ICommunicationMessageHandler successor;

	private NodeState nodeState;

	public EdgeBeatMessageHandler(NodeState nodeState) {
		this.nodeState = nodeState;
	}

	@Override
	public void handleCommunicationMessage(CommunicationMessage commMsg,
		Channel channel) {
		if (commMsg.hasBeat()) {
			handle(commMsg, channel);
		} else {
			if (successor != null) {
				successor.handleCommunicationMessage(commMsg, channel);
			} else {
				logger.info("No handler available");
			}

		}

	}

	private void handle(CommunicationMessage commMsg, Channel channel) {
		// TODO Auto-generated method stub
		logger.info(this.nodeState.getRoutingConfig().getNodeId()
			+ " got EdgeBeat from " + commMsg.getHeader().getNodeId() + " as I("
			+ commMsg.getHeader().getDestination()
			+ ") was supposed to get it.");
	}

	@Override
	public void setNextCommunicationMessageHandler(
		ICommunicationMessageHandler commMsgHandler) {
		this.successor = commMsgHandler;
	}

}
