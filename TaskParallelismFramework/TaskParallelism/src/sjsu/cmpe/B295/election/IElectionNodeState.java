package sjsu.cmpe.B295.election;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;

public interface IElectionNodeState {
	void beforeStateChange();

	void afterStateChange();

	void handleHeartBeat(CommunicationMessage msg, Channel channel);

	void handleVoteRequest(CommunicationMessage msg, Channel channel);

	void handleVoteResponse(CommunicationMessage msg, Channel channel);

	void handleElectionEvent(CommunicationMessage msg, Channel channel);

	// Http Request Handling methods

	void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request);

	void handleGetTemperatureRequest(ChannelHandlerContext ctx,
		FullHttpRequest request);

	void handleGetHumidityRequest(ChannelHandlerContext ctx,
		FullHttpRequest request);

	void handleGetBothRequest(ChannelHandlerContext ctx,
		FullHttpRequest request);
}
