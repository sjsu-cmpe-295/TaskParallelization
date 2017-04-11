package sjsu.cmpe.B295.server;

import java.util.ArrayList;
import java.util.HashMap;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import sjsu.cmpe.B295.common.MessageProto.Message;
import sjsu.cmpe.B295.common.MessageProto.Message.Builder;
import sjsu.cmpe.B295.common.MessageProto.Message.MessageType;
import sjsu.cmpe.B295.s2da.IMessageHandler;
import sjsu.cmpe.B295.s2da.InputAnalyzer;
import sjsu.cmpe.B295.s2da.InputSplitter;
import sjsu.cmpe.B295.s2da.Mapper;

@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
	ArrayList<Integer> workerChunkSumsArr;
	int availableWorkers;
	InputSplitter is;
	static HashMap<String, ChannelHandlerContext> channelsMap = new HashMap<>();
	IMessageHandler analysisHandler = new InputAnalyzer();
	IMessageHandler splitHandler = new InputSplitter();
	IMessageHandler mapHandler = new Mapper(channelsMap);

	public ServerHandler() {
		// TODO Auto-generated constructor stub
		workerChunkSumsArr = new ArrayList<>();
		this.is = new InputSplitter();
		this.availableWorkers = this.is.availableWorkers();

		// Chain of Responsibility for analysing, splitting, and mapping the
		// input/tasks

		analysisHandler.setNextHandler(splitHandler);
		splitHandler.setNextHandler(mapHandler);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	};

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// Retrieve Message from channel
		System.out.println("Here");
		Message receivedMsg = (Message) msg;
		System.out.println("****Received Message by Server****");
		System.out.println(receivedMsg.toString());
		System.out.println("****Received Message Ends Here****");

		// Store the channel in the hashmap if not available
		if (channelsMap.get(receivedMsg.getMessageType()) == null) {
			channelsMap.put(receivedMsg.getMessageType().toString(), ctx);
		}

		if (receivedMsg.getMessageType().equals(MessageType.INPUT)) {

			System.out.println("****Received Message by Server****");
			System.out.println(receivedMsg.toString());
			System.out.println("****Received Message Ends Here****");

			Builder builder = Message.newBuilder().setMessageType(MessageType.ANALYZE)
				.setMessageStr("Analyze Input");
			Message msgForAnalyzer = builder.build();
			analysisHandler.handleMessage(msgForAnalyzer);

		} else if (receivedMsg.getMessageType().equals(MessageType.REDUCE_RESULT)) {

			// All reduce results received?
			if (hasReceivedResultsFromAllReducers()) {
				Builder builder = Message.newBuilder()
					.setMessageType(MessageType.INPUT_RESULT)
					.setMessageStr("INPUT RESULT");
				Message msgToSendToClient = builder.build();
				channelsMap.get(MessageType.INPUT.toString())
					.writeAndFlush(msgToSendToClient);
			}
		} else{
			System.out.println("Else condition");
		}

		// System.out.println("In Channel Read for Server");
		// ByteBuf recvdMsg = (ByteBuf) msg;
		// System.out
		// .println("Received Bytes from Client =" + recvdMsg.readableBytes());
		//
		// // Client Part
		// byte[] recvdMsgBytes = new byte[recvdMsg.readableBytes()];
		// recvdMsg.readBytes(recvdMsgBytes);
		//
		// IntBuffer source = ByteBuffer.wrap(recvdMsgBytes).asIntBuffer();
		// IntBuffer destination = IntBuffer.allocate(recvdMsgBytes.length / 4);
		// destination.put(source);
		// int[] arr = destination.array();
		//
		// System.out.println("arr[0]== " + arr[0]);
		// System.out.println("Size of map" + channelsMap.size());
		// channelsMap.put(arr[0], ctx);
		//
		// if (arr[0] == 0) {
		//
		// int[] arrToBeAdded = new int[arr.length - 1];
		// for (int i = 1; i < arr.length; i++) {
		// arrToBeAdded[i - 1] = arr[i];
		// }
		//
		// // Client
		// // InputSplitter
		// InputSplitter is = new InputSplitter(arrToBeAdded);
		// int chunks[][] = is.getChunks();
		// for (int i = 0; i < chunks.length; i++) {
		// for (int j = 0; j < chunks[i].length; j++) {
		// System.out.print(chunks[i][j] + " ");
		// }
		// System.out.println();
		// }
		//
		// System.out.println("Available Workers" + is.availableWorkers());
		// if (channelsMap.size() < is.availableWorkers() + 1) {
		// return;
		// }
		// for (int i = 0; i < this.availableWorkers; i++) {
		//
		// ChannelHandlerContext ctw = channelsMap.get(i + 1);
		// System.out
		// .println("Channel" + channelsMap.get(i + 1).channel());
		//
		// ByteBuffer byteBuffer = ByteBuffer
		// .allocate(chunks[i].length * 4);
		// IntBuffer intBuffer = byteBuffer.asIntBuffer();
		// intBuffer.put(chunks[i]);
		//
		// byte[] arrBytes = byteBuffer.array();
		//
		// ByteBuf firstMessage = Unpooled.buffer(arrBytes.length);
		// firstMessage.writeBytes(arrBytes);
		// ctw.writeAndFlush(firstMessage);
		// // write
		// }
		//
		// } else if (arr[0] == 1) {
		// // Worker 1
		// System.out.println("For worker " + arr[0]);
		//
		// } else if (arr[0] == 2) {
		// // Worker 2
		// System.out.println("For worker " + arr[0]);
		// } else if (arr[0] == 3) {
		// // Worker 3
		// System.out.println("For worker " + arr[0]);
		// }

		// Mapper

	}

	private boolean hasReceivedResultsFromAllReducers() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		System.out.println("In Channel Read Complete for Server");
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
