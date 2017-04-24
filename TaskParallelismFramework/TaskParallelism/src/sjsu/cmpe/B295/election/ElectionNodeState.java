package sjsu.cmpe.B295.election;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.common.ElectionProto.Election;
import sjsu.cmpe.B295.httpRequestMessageHandlers.HttpRequestHandler;
import sjsu.cmpe.B295.httpRequestMessageHandlers.IHttpRequestHandler;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class ElectionNodeState
	implements IElectionNodeState, IHttpRequestHandler {
	protected static Logger logger = LoggerFactory
		.getLogger("ElectionNodeState");
	protected NodeState nodeState;
	protected JSONParser parser;

	public ElectionNodeState(NodeState nodeState) {
		super();
		this.nodeState = nodeState;
		this.parser = new JSONParser();
	}

	@Override
	public void handleElectionEvent(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub
		Election electionMsg = msg.getElectionMessage();
		switch (electionMsg.getAction()) {
		case VOTEREQUEST:
			handleVoteRequest(msg, channel);
			break;
		case VOTERESPONSE:
			handleVoteResponse(msg, channel);
			break;
		case BEAT:
			handleHeartBeat(msg, channel);
			break;
		default:
		}
	}

	@Override
	public void handleHttpRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		// TODO Auto-generated method stub
		if (request.method() == GET) {

			DefaultFullHttpResponse response = new DefaultFullHttpResponse(
				HTTP_1_1, OK);

			String welcomeString = "Hello, World!";
			ByteBuf buffer = Unpooled.copiedBuffer(welcomeString,
				CharsetUtil.UTF_8);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE,
				"text/html; charset=UTF-8");
			response.content().writeBytes(buffer);
			ctx.write(response);
		} else if (request.method() == POST) {
			String endpoint = request.uri();
			boolean isRequestFromLeader = false;
			String requestBody = request.content()
				.toString(StandardCharsets.UTF_8);
			JSONObject jsonRequestBody = null;
			try {
				jsonRequestBody = (JSONObject) parser.parse(requestBody);
				if (jsonRequestBody.containsKey("fromLeader"))
					isRequestFromLeader = true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			switch (endpoint) {
			case "/getTemperatureData":
				if (isRequestFromLeader) {
					handleGetTemperatureRequest(ctx, request);
				} else {
					handleHttpRequestEvent(ctx, request);
				}
				break;
			case "/getHumidityData":
				if (isRequestFromLeader) {
					handleGetHumidityRequest(ctx, request);
				} else {
					handleHttpRequestEvent(ctx, request);
				}
				break;
			case "/getBothData":
				if (!isRequestFromLeader)
					handleHttpRequestEvent(ctx, request);
				break;
			default:
				HttpRequestHandler.sendError(ctx,
					HttpResponseStatus.METHOD_NOT_ALLOWED);
				break;
			}
		} else {
			HttpRequestHandler.sendError(ctx,
				HttpResponseStatus.METHOD_NOT_ALLOWED);
		}

	}

	@Override
	public void beforeStateChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterStateChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHeartBeat(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVoteRequest(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVoteResponse(CommunicationMessage msg, Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleGetTemperatureRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleGetHumidityRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleGetBothRequest(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNextHandler(IHttpRequestHandler nextHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHttpRequestEvent(ChannelHandlerContext ctx,
		FullHttpRequest request) {
		// TODO Auto-generated method stub

	}
}
