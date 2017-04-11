package sjsu.cmpe.B295.raspberrypi.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;


public class MessageRouter {
	private final Logger logger = LoggerFactory.getLogger("Router");
	
	private final NodeState nodeState;

	public MessageRouter(NodeState nodeState) {
		this.nodeState = nodeState;
	}
	
	public CommunicationMessage route(CommunicationMessage msg) {

		// Message is for current node.
		if (msg.getHeader().getDestination() == nodeState.getRoutingConfig().getNodeId()) {
			return msg;
		}

		if (msg.getHeader().getNodeId() == nodeState.getRoutingConfig().getNodeId()) {
			logger.info("Same message received by source! Dropping message...");
			return null;
		}
		return route0(msg);
	}

	private CommunicationMessage route0(CommunicationMessage msg) {
		if (msg.getHeader().getDestination() != nodeState.getRoutingConfig().getNodeId()) {
			// Not for you, drop it!
			return null;
		}
//			if (msg.getHeader().getMaxHops() > 0) {
//				broadcast(msg);
//
//				// If destination is not -1 that means this message is only for
//				// broadcasting. So return null.
//				if (msg.getHeader().getDestination() != -1) {
//					logger.info("Destination is not -1...");
//					return null;
//				}
//			} else {
//				logger.info("MAX HOPS is Zero! Dropping message...");
//				return null;
//			}
//		}
		logger.info("<<<<<<<<<<<<Returning Message>>>>>>>>>>>>>>>>>>>>>>>>" + msg);
		return null;
	}
}
