package sjsu.cmpe.B295.communicationMessageHandlers;

import io.netty.channel.Channel;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;

public interface ICommunicationMessageHandler {
	public void handleCommunicationMessage(CommunicationMessage commMsg,
		Channel channel);

	public void setNextCommunicationMessageHandler(
		ICommunicationMessageHandler commMsgHandler);
}
