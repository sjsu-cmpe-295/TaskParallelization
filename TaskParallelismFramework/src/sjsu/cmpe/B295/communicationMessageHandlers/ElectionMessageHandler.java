package sjsu.cmpe.B295.communicationMessageHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class ElectionMessageHandler implements ICommunicationMessageHandler {
	private static Logger logger = LoggerFactory
		.getLogger("ElectionMessageHandler");
	private ICommunicationMessageHandler successor;

	private NodeState nodeState;

	public ElectionMessageHandler(NodeState nodeState) {
		this.nodeState = nodeState;
	}

	@Override
	public void handleCommunicationMessage(CommunicationMessage commMsg,
		Channel channel) {
		if (commMsg.hasElectionMessage()) {
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
		this.nodeState.handleElectionMessage(commMsg, channel);
	}

	@Override
	public void setNextCommunicationMessageHandler(
		ICommunicationMessageHandler commMsgHandler) {
		this.successor = commMsgHandler;
	}

}
