package sjsu.cmpe.B295.election;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;

public interface IElectionNodeState extends ILeaderElection {
	void handleElectionEvent(CommunicationMessage msg, Channel channel);
}