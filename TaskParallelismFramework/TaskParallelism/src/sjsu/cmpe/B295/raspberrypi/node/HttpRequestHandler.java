package sjsu.cmpe.B295.raspberrypi.node;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
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

public class HttpRequestHandler
	extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static Logger logger = LoggerFactory
		.getLogger("HttpRequestHandler");

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

		if (request.method() == GET) {
			logger.info("%%%%%%%%%%%%%%%%%% Got " + request.method()
				+ "Request %%%%%%%%%%%%%%%%%% ");
			// sendError(ctx, METHOD_NOT_ALLOWED);
			DefaultFullHttpResponse response = new DefaultFullHttpResponse(
				HTTP_1_1, OK);
			ByteBuf buffer = Unpooled.copiedBuffer(
				"Hello World, This is working in GET method of Server!!", CharsetUtil.UTF_8);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE,
				"text/html; charset=UTF-8");
			response.content().writeBytes(buffer);

			ctx.write(response);

		} else {
			logger.info("%%%%%%%%%%%%%%%%%% Got " + request.method()
				+ "Request %%%%%%%%%%%%%%%%%% ");
			 DefaultFullHttpResponse response = new DefaultFullHttpResponse(
			 HTTP_1_1, OK, request.content().copy());
			 ctx.write(response);
//			DefaultFullHttpResponse response = new DefaultFullHttpResponse(
//				HTTP_1_1, OK);
//			ByteBuf buffer = Unpooled.copiedBuffer(
//				"Hello World, This is working!!", CharsetUtil.UTF_8);
//			response.headers().set(HttpHeaderNames.CONTENT_TYPE,
//				"text/html; charset=UTF-8");
//			response.content().writeBytes(buffer);
//
//			ctx.write(response);
		}

	}

	private static void sendError(ChannelHandlerContext ctx,
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
