package sjsu.cmpe.B295.httpRequestMessageHandlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface IHttpRequestHandler {
	public void handleHttpRequestEvent(ChannelHandlerContext ctx,
		FullHttpRequest request);

	public void setNextHandler(IHttpRequestHandler nextHandler);
}
