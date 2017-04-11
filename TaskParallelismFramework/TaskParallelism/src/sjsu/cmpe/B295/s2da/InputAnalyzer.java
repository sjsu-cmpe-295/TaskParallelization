package sjsu.cmpe.B295.s2da;

import sjsu.cmpe.B295.common.MessageProto.Message;
import sjsu.cmpe.B295.common.MessageProto.Message.Builder;
import sjsu.cmpe.B295.common.MessageProto.Message.MessageType;

public class InputAnalyzer implements IMessageHandler {
	private IMessageHandler successor = null;

	@Override
	public void handleMessage(Message msg) {
		Message receivedMsg = (Message) msg;
		System.out.println("****Received Message by Analyzer****");
		System.out.println(receivedMsg.toString());
		System.out.println("****Received Message Ends Here****");
		if (msg.getMessageType().equals(MessageType.ANALYZE)) {
			if (hasIndependentTasks()) {
				Builder builder = Message.newBuilder()
					.setMessageType(Message.MessageType.SPLIT)
					.setMessageStr("Split Input");
				Message msgForSplitter = builder.build();

				successor.handleMessage(msgForSplitter);
			}
		} else {
			if (successor != null) {
				successor.handleMessage(receivedMsg);
			}
		}
	}

	private boolean hasIndependentTasks() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setNextHandler(IMessageHandler nextHandler) {
		// TODO Auto-generated method stub
		this.successor = nextHandler;
	}

}
