package sjsu.cmpe.B295.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import sjsu.cmpe.B295.common.IdentityProto.Identity;
import sjsu.cmpe.B295.common.MessageProto.Message;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// System.out
		// .println("In Channel Read");
		// ByteBuf sameMsg = (ByteBuf) msg;
		// ByteBuf msgToBeSentBack= Unpooled.buffer(sameMsg.capacity());
		// String recvdMsg = "";
		// while (sameMsg.isReadable()){
		// char chr = (char)sameMsg.readByte();
		// recvdMsg+=chr;
		// msgToBeSentBack.writeChar(chr);
		// }
		// System.out.println("Msg from server:"+recvdMsg);
		// ctx.write(msgToBeSentBack);

		// System.out.println("In Channel Read for Client");
		// ByteBuf recvdMsg = (ByteBuf) msg;
		// System.out
		// .println("Received Bytes from Client:" + recvdMsg.readableBytes());
		// byte[] recvdMsgBytes = new byte[recvdMsg.readableBytes()];
		// recvdMsg.readBytes(recvdMsgBytes);
		// System.out.println(
		// "Received Message from Server:" + new String(recvdMsgBytes));
		//
		// ByteBuf msgToSendBack = Unpooled.buffer(recvdMsgBytes.length);
		// msgToSendBack.writeBytes(recvdMsgBytes);
		// ctx.write(msgToSendBack);

		// System.out.println("In Channel Read for Client");
		// ByteBuf recvdMsg = (ByteBuf) msg;
		// System.out.println("Sum of array elements:" + recvdMsg.readInt());

		// Message msgFromServer = (Message) msg;
		// System.out.println(msgFromServer.toString());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		System.out.println("In Channel Read Complete");
		ctx.flush();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Identity.Builder identityBuilder = Identity.newBuilder()
			.setIdentityType(Identity.IdentityType.CLIENT)
			.setNodeState(Identity.NodeState.FOLLOWER)
			.setIdentityStr("I'm the client");
		Identity identityMsg = identityBuilder.build();
		ctx.writeAndFlush(identityMsg);

		 Message.Builder builder = Message.newBuilder()
		 .setMessageType(Message.MessageType.INPUT)
		 .setMessageStr("Input");
		 Message clientMsg = builder.build();
		
		 ctx.writeAndFlush(clientMsg);

		// System.out.println("In Channel Active-Client");
		// char[] st = "Hello".toCharArray();
		// ByteBuf firstMessage = Unpooled.buffer(st.length);
		// for (int i = 0; i < st.length; i++) {
		// firstMessage.writeChar(st[i]);
		// }
		// ctx.writeAndFlush(firstMessage);

		// System.out.println("In Channel Active-Client");
		// String msg = "Hello";
		// byte[] msgBytes = msg.getBytes();
		//
		// ByteBuf firstMessage = Unpooled.buffer(msgBytes.length);
		// firstMessage.writeBytes(msgBytes);
		// System.out.println("sent capacity:" + firstMessage.capacity());
		// ctx.writeAndFlush(firstMessage);

		// System.out.println("In Channel Active-Client");
		// int[] arr = new int[15];
		// arr[0] = 0;
		// for (int i = 1; i < arr.length; i++) {
		// arr[i] = 1;
		// }
		//
		// ByteBuffer byteBuffer = ByteBuffer.allocate(arr.length * 4);
		// IntBuffer intBuffer = byteBuffer.asIntBuffer();
		// intBuffer.put(arr);
		//
		// byte[] arrBytes = byteBuffer.array();
		// System.out.println(arrBytes.length);
		//
		// ByteBuf firstMessage = Unpooled.buffer(arrBytes.length);
		// firstMessage.writeBytes(arrBytes);
		// System.out.println("sent capacity:" + firstMessage.capacity());
		// ctx.writeAndFlush(firstMessage);

	};

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
