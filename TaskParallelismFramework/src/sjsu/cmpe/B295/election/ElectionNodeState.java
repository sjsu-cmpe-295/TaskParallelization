package sjsu.cmpe.B295.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;


public class ElectionNodeState implements IElectionNodeState{
	protected static Logger logger = LoggerFactory.getLogger("ElectionNodeState");
	protected NodeState nodeState;


	public ElectionNodeState(NodeState nodeState) {
		super();
		this.nodeState = nodeState;
	}


	@Override
	public void handleElectionEvent(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		logger.info("In ElectionNodeState's handleEvent");
	}

}
