package sjsu.cmpe.B295.httpRequestMessageHandlers;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class HttpRequestHandler
	extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static Logger logger = LoggerFactory
		.getLogger("HttpRequestHandler");

	private final NodeState nodeState;
	private JSONParser parser ; 

	

	public HttpRequestHandler(NodeState nodeState) {
		// TODO Auto-generated constructor stub
		this.nodeState = nodeState;
		this.parser = new JSONParser();
	}
	
	public void handleRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		this.nodeState.handleHttpRequest(ctx, request);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
		throws Exception {
		ctx.flush();
		// The close is important here in an HTTP request as it sets the
		// Content-Length of a
		// response body back to the client.
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
		FullHttpRequest request) throws Exception {
		handleRequest(ctx, request);
	}

	public static void sendError(ChannelHandlerContext ctx,
		HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
			status, Unpooled.copiedBuffer("Failure: " + status + "\r\n",
				CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,
			"text/plain; charset=UTF-8");

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
