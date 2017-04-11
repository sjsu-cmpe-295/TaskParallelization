package sjsu.cmpe.B295.election;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;

public interface IElectionNodeState extends ILeaderElection {
	void beforeStateChange();

	void afterStateChange();

	void handleHeartBeat(CommunicationMessage msg, Channel channel);

	void handleVoteRequest(CommunicationMessage msg, Channel channel);

	void handleVoteResponse(CommunicationMessage msg, Channel channel);

	void handleElectionEvent(CommunicationMessage msg, Channel channel);
}
