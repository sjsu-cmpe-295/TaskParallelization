package sjsu.cmpe.B295.raspberrypi.node;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class HttpCommunicationChannelHandler
	extends SimpleChannelInboundHandler<HttpMessage> {
	private NodeState nodeState;

	public HttpCommunicationChannelHandler() {
		// TODO Auto-generated constructor stub
	}

	public HttpCommunicationChannelHandler(NodeState nodeState) {
		// TODO Auto-generated constructor stub
		this.nodeState = nodeState;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		// JSONObject json = new JSONObject();
		// json.put("myKey", "I'm Up Response");
		//
		// ByteBuf incomingRequestContent = Unpooled
		// .copiedBuffer(json.toJSONString(), StandardCharsets.UTF_8);
		//
		// DefaultFullHttpResponse response = new
		// DefaultFullHttpResponse(HTTP_1_1,
		// OK, incomingRequestContent);
		// ctx.write(response);

		// DefaultFullHttpResponse response = new DefaultFullHttpResponse(
		// HTTP_1_1, OK);
		// ByteBuf buffer = Unpooled.copiedBuffer(
		// “Hello World, This is working!!“, CharsetUtil.UTF_8);
		// response.headers().set(HttpHeaderNames.CONTENT_TYPE,
		// “text/html; charset=UTF-8”);
		// response.content().writeBytes(buffer);

		// ctx.write(response);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
		throws Exception {
		ctx.flush();
		// The close is important here in an HTTP
		// request as it sets the Content-Length of a
		// response body back to the client.
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpMessage incMsg) {

		try {
			if (incMsg instanceof FullHttpRequest) {
				FullHttpRequest msg = (FullHttpRequest) incMsg;
				System.err.println(msg.content().toString(CharsetUtil.UTF_8));

				// DefaultFullHttpResponse response = new
				// DefaultFullHttpResponse(
				// HTTP_1_1, OK, msg.content().copy());
				// ctx.write(response);

				JSONObject json = new JSONObject();
				json.put("myKey",
					this.nodeState.getRoutingConfig().getNodeId()
						+ "'s Response to "
						+ msg.content().toString(CharsetUtil.UTF_8));
				System.out.println("*****************");
				System.out.println(json.toString());
				ByteBuf incomingRequestContent = Unpooled
					.copiedBuffer(json.toJSONString(), StandardCharsets.UTF_8);

				DefaultFullHttpResponse response = new DefaultFullHttpResponse(
					HTTP_1_1, OK, incomingRequestContent);
				ctx.write(response);

			} else {
				FullHttpResponse resp = (FullHttpResponse) incMsg;
				final String echo = resp.content().toString(CharsetUtil.UTF_8);
				System.err.println(echo);
				// log.info("Response: {}", echo);
			}
			// FullHttpResponse msg = (FullHttpResponse) incMsg;
			// System.err.println("Got Response from Leader");
			// System.err.println(msg.toString());
		} finally {
			// TODO: handle finally clause
			ReferenceCountUtil.release(incMsg);
		}

	}
}
