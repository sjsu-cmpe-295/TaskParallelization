package sjsu.cmpe.B295.s2da;

import java.util.HashMap;
import java.util.Iterator;

import io.netty.channel.ChannelHandlerContext;
import sjsu.cmpe.B295.common.MessageProto.Message;
import sjsu.cmpe.B295.common.MessageProto.Message.Builder;
import sjsu.cmpe.B295.common.MessageProto.Message.MessageType;

/**
 * @author Prasanna
 *
 */
public class Mapper implements IMessageHandler {
	private IMessageHandler successor = null;
	HashMap<String, ChannelHandlerContext> channelsMap;

	public Mapper() {

	}

	public Mapper(HashMap<String, ChannelHandlerContext> channelsMap) {
		if (channelsMap == null) {
			System.out.println("Channels Map is null");
		} else {
			this.channelsMap = channelsMap;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		Message receivedMsg = (Message) msg;
		System.out.println("****Received Message by Mapper****");
		System.out.println(receivedMsg.toString());
		System.out.println("****Received Message Ends Here****");

		if (msg.getMessageType().equals(MessageType.MAP)) {

			for (String messageType : channelsMap.keySet()) {
				if (messageType.startsWith("Worker")) {
					Message msgForReducer = Message.newBuilder()
						.setMessageType(MessageType.REDUCE)
						.setMessageStr("Map to " + messageType).build();
					channelsMap.get(messageType).writeAndFlush(msgForReducer);
				}
			}

			Builder builder = Message.newBuilder()
				.setMessageType(Message.MessageType.REDUCE)
				.setMessageStr("Mapper To Worker" + "_0");
			Message msgForReducer = builder.build();

		} else {
			if (successor != null) {
				successor.handleMessage(receivedMsg);
			}
		}
	}

	@Override
	public void setNextHandler(IMessageHandler nextHandler) {
		this.successor = nextHandler;

	}

}
