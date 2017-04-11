package sjsu.cmpe.B295.s2da;

import sjsu.cmpe.B295.common.MessageProto.Message;

public interface IMessageHandler {
	
	/**
	 * @param msg
	 */
	public void handleMessage(Message msg);

	/**
	 * @param nextHandler
	 */
	public void setNextHandler(IMessageHandler nextHandler);
}
