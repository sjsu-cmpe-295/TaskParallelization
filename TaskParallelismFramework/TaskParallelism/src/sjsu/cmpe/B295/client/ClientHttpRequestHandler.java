package sjsu.cmpe.B295.client;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;

public class ClientHttpRequestHandler
	extends SimpleChannelInboundHandler<FullHttpResponse> {
	private static Logger logger = LoggerFactory
		.getLogger("ClientHttpRequestHandler");

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg)
		throws Exception {
		
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(
			HTTP_1_1, OK);
		ByteBuf buffer = Unpooled.copiedBuffer(
			"Hello World, This is working in client!!", CharsetUtil.UTF_8);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,
			"text/html; charset=UTF-8");
		response.content().writeBytes(buffer);

		ctx.write(response);
		// final String echo = msg.content().toString(CharsetUtil.UTF_8);
		// logger.info("Response: {}", echo);
	}
}
